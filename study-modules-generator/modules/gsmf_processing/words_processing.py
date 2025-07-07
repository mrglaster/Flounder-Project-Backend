import requests
import base64
from nostril import nonsense
from deep_translator import GoogleTranslator
import modules.phonemes_to_sound.processor
import modules.phonemes_to_sound.generator
from modules.gsmf_processing.languages_parameters import GERMAN_GENUS
from pprint import pprint
from german_nouns.lookup import Nouns


def is_wordlist_valid(wordlist):
    """Checks if list of words doesn't contain nonsense"""
    return not nonsense(" ".join(wordlist))


def get_as_base64(url):
    """Returns file by link as base64 line"""
    return str(base64.b64encode(requests.get(url).content)).replace("b'", '').replace("'", '')


def translate_words(source_lang, dist_lang, words):
    """Translates a bunch of words to the selected language"""
    delimiter = '.'
    words_line = delimiter.join(words)
    translated = GoogleTranslator(source=f'{source_lang}', target=f'{dist_lang}').translate(text=words_line)
    if delimiter not in translated:
        results = translated.split(',')
    else:
        results = translated.split(delimiter)
    return results


def process_german_articles(word_list):
    """Identifies nouns articles  for german"""
    nouns = Nouns()
    for i in range(len(word_list)):
        word = word_list[i]
        if word.startswith(" "):
            word = word[1:]
        if not word[0].isupper() or len(word.split(" ")) > 1:
            continue
        try:
            article = GERMAN_GENUS[nouns[word][0]["genus"]]
        except:
            article = '???'
        word_list[i] = article + ' ' + word_list[i]


def get_wordinfo_en(word):
    """Finds information about a word from English: definitions, pronunciation, examples when it's possible via Free Dictionary API"""
    request_link = 'https://api.dictionaryapi.dev/api/v2/entries/en/'
    response = requests.get(f"{request_link}{word}").json()
    try:
        pronunciation = get_as_base64(response[0]['phonetics'][0]['audio'])
    except:
        try:
            phonemes = response[0]["phonetic"]
            pronunciation = modules.phonemes_to_sound.processor.generate_single_pronunciation_base64(phonemes)
        except:
            pronunciation = modules.phonemes_to_sound.processor.generate_single_pronunciation_base64(word)
    pronunciation = pronunciation.replace('\n', '')
    definitions = []
    examples = []
    for i in response:
        if 'No Definitions Found' in i or type(i) is not dict:
            print(word)
            continue

        for z in i["meanings"]:
            for j in z["definitions"]:
                try:
                    definitions.append(j["definition"])
                    examples.append(j["example"])
                except:
                    pass

    if not len(examples):
        examples.append("NOT FOUND")
    if not len(definitions):
        definitions.append("NOT FOUND")
    return pronunciation, definitions, examples


def get_wordinfo_de_proto(word, hint_lang="en"):
    """Gets information about the current German word
    PROTOTYPE! WILL BE REMOVED! USE ON YOUR OWN RISK! API KEY FROM EXAMPLE, IT'S NOT VALID"""
    url = "https://lexicala1.p.rapidapi.com/search-entries"
    word_new = word.replace("der", "").replace("die", "").replace("das", "").replace(" ", '')
    querystring = {f"text": f"{word_new}", "language": "de"}
    headers = {
        "X-RapidAPI-Key": "example-api-key",
        "X-RapidAPI-Host": "lexicala1.p.rapidapi.com"
    }
    response = requests.request("GET", url, headers=headers, params=querystring).json()["results"]
    phonemes = None
    try:
        phonemes = response[0]["headword"]["pronunciation"]["value"]
    except:
        phonemes = None
    definitions = []
    examples = []
    for entry in response:
        senses = entry["senses"]
        for sense in senses:
            try:
                definitions.append(sense["definition"])
            except KeyError:
                definitions.append("NOT FOUND")
            try:
                examples_data = sense["examples"]
                if examples_data:
                    example_text = examples_data[0]["text"]
                    example_translation = examples_data[0]["translations"][hint_lang]["text"]
                    examples.append(f"{example_text} - {example_translation}")
                else:
                    examples.append("NOT FOUND")
            except KeyError:
                examples.append("NOT FOUND")
    if phonemes is not None:
        pronunciation = modules.phonemes_to_sound.processor.generate_single_pronunciation_base64(phonemes, language='de-DE',
                                                                                             speaker="Hans")
    else:
        pronunciation = "NOT FOUND"
    return pronunciation, definitions, examples
