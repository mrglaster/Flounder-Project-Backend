import base64
import random
import string
import unicodedata
import io
import re
import requests
from datetime import datetime
from PIL import Image
from datetime import date
from modules.gsmf_processing.languages_parameters import SUPPORTED_LANGUAGES
from modules.gsmf_processing.words_processing import translate_words
from modules.gsmf_processing.words_processing import get_wordinfo_en
from modules.gsmf_processing.words_processing import get_wordinfo_de_proto
from modules.proto import study_service_pb2


def generate_random_string(length=20):
    """Generates random string of selected length"""
    letters_and_digits = string.ascii_letters + string.digits
    return ''.join(random.choice(letters_and_digits) for i in range(length))


def _array_to_string(arr):
    """Converts array to the string with separator"""
    return ';'.join([word.lower() for word in arr])


def parse_request(request):
    """Gets main parameters from request json"""
    author = request.author
    module_id = request.module_id
    title = request.title
    wlang = request.language
    words_list = request.wordlist
    translations_list = request.translations
    trlang = request.translations_language
    cover = request.icon
    cur_date = date.today()
    file_name = request.file_name
    return author, title, wlang.lower(), cur_date, words_list, translations_list, cover, trlang, file_name, module_id


def convert_to_base64(file_path):
    """Converts module file to base64 format"""
    with open(file_path, "rb") as module_file:
        return str(base64.b64encode(module_file.read())).replace("b'", '').replace("'", '')


def create_module(request_dto: study_service_pb2.ModuleCreationRequest):
    """Creates study module file with gsmf format"""
    module_id_pre = 0
    headers = {'Content-Type': 'application/json'}
    try:
        author, title, words_language, cur_date, words_list, translations_list, cover, manual_translation_language, filename, module_id = parse_request(
            request_dto)
        module_id_pre = module_id
        translation_mode = "auto" if not len(translations_list) else "hands"
        with open(filename, "w", encoding="utf-8") as module_file:
            print('Writing init')
            write_init(module_file, author, title, words_language, cur_date, translation_mode, cover)
            print('writing wordlist')
            write_wordlist(module_file, words_list)
            print('writing translations')
            write_translations(module_file, words_language, words_list, translations=translations_list,
                               trlang=manual_translation_language)
            print('writing words info')
            write_words_info(module_file, words_list, language=words_language)
            print('finishing')
            module_file.write("</gsmf>")

        if 'default' not in cover:
            cover_path = f'data/images/{filename[:-4]}_cover.png'
            image_bytes = base64.b64decode(cover)
            img_io = io.BytesIO(image_bytes)
            img = Image.open(img_io)
            img.save(cover_path, "PNG")

        result_request = {
            "status": 1,
            "module_id": module_id_pre
        }
        print("I AM HERE!")
        requests.post(url='http://localhost:5613/api/study/modules/update/status', json=result_request)
    except:
        result_request = {
            "status": 2,
            "module_id": module_id_pre
        }
        requests.post(url='http://localhost:5613/api/study/modules/update/status', data=result_request)


def slugify(value, allow_unicode=False):
    """
    Taken from https://github.com/django/django/blob/master/django/utils/text.py
    Convert to ASCII if 'allow_unicode' is False. Convert spaces or repeated
    dashes to single dashes. Remove characters that aren't alphanumerics,
    underscores, or hyphens. Convert to lowercase. Also strip leading and
    trailing whitespace, dashes, and underscores.
    """
    value = str(value)
    if allow_unicode:
        value = unicodedata.normalize('NFKC', value)
    else:
        value = unicodedata.normalize('NFKD', value).encode('ascii', 'ignore').decode('ascii')
    value = re.sub(r'[^\w\s-]', '', value.lower())
    return re.sub(r'[-\s]+', '-', value).strip('-_')


def generate_name(wlang, author, title):
    """Generates filename by author name and word's language"""
    return "data/modules/" + slugify(
        f"{wlang}_{author}_{title}_{str(datetime.timestamp(datetime.now())).replace('.', '')}".lower().strip().replace(
            ' ', '')) + ".gsmf"


def write_init(module_file, author, title, wlang, cur_date, trfillmode, cover):
    """Writes init block to the study module file"""
    module_file.write("<gsmf>\n")
    module_file.write("  <init>\n")
    module_file.write(f"    <author>{author}</author>\n")
    module_file.write(f"    <title>{title}</title>\n")
    module_file.write(f"    <wlang>{wlang}</wlang>\n")
    module_file.write(f"    <date>{cur_date}</date>\n")
    module_file.write(f"    <trfillmode>{trfillmode}</trfillmode>\n")
    module_file.write(f"    <cover>{cover}</cover>\n")
    module_file.write(f" </init>\n")


def write_wordlist(module_file, word_list):
    """Writes list of words for learning"""
    module_file.write(" <wlist>\n")
    for word in word_list:
        module_file.write(f"    <word>{word}</word>\n")
    module_file.write(' </wlist>' + '\n')


def write_translations(module_file, source_language, word_list, translations=[], trlang=""):
    """Writes translations from source language to other supported ones"""
    module_file.write(" <translations>\n")
    if len(translations) and trlang in SUPPORTED_LANGUAGES and len(translations) == len(word_list):
        module_file.write(f"    <{trlang}>\n")
        for i in translations:
            module_file.write(f"        <tr>{i}</tr>\n")
        module_file.write(f"    </{trlang}>\n")

    for i in SUPPORTED_LANGUAGES:
        if i == source_language or i == trlang:
            continue
        module_file.write(f"    <{i}>\n")
        translated = translate_words(source_lang=source_language, dist_lang=i, words=word_list)
        for j in translated:
            module_file.write(f"        <tr>{j[1:] if j.startswith(' ') else j}</tr>\n")
        module_file.write(f"    </{i}>\n")
    module_file.write(" </translations>\n")


def write_words_info(module_file, wordlist, language='en'):
    """Writes study info about the words"""
    audio = []
    g_examples = []
    g_definitions = []
    pronunciation, definitions, examples = None, None, None
    for i in wordlist:
        if language == 'en':
            pronunciation, definitions, examples = get_wordinfo_en(i)
        elif language == 'de':
            pronunciation, definitions, examples = get_wordinfo_de_proto(i)
        audio.append(pronunciation)
        g_examples.append(examples)
        g_definitions.append(definitions)
    write_audio_info(module_file, audio)
    write_definitions(module_file, g_definitions)
    write_examples(module_file, g_examples)


def write_audio_info(module_file, audio):
    """Writes audio of pronunciation into the module file"""
    module_file.write(" <audio>\n")
    for i in audio:
        module_file.write(f'     <spk>{i}</spk>\n')
    module_file.write(" </audio>\n")


def write_definitions(module_file, definitions):
    """Writes definitions for words into the module file"""
    module_file.write(" <definitions>\n")
    for word in definitions:
        module_file.write("     <wdblock>\n")
        for df in word:
            module_file.write(f"        <df>{df}</df>\n")
        module_file.write("     </wdblock>\n")
    module_file.write(" </definitions>\n")


def write_examples(module_file, examples):
    """Writes Examples for Words into the module file"""
    module_file.write(" <examples>\n")
    for word in examples:
        module_file.write("     <exblock>\n")
        for example in word:
            module_file.write(f"        <ex>{example}</ex>\n")
        module_file.write("     </exblock>\n")
    module_file.write(" </examples>\n")
