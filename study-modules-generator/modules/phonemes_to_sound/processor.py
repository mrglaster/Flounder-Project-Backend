import modules.phonemes_to_sound
import  modules.phonemes_to_sound.generator
import base64

from modules.phonemes_to_sound.data_preprocessing import *


def generate_single_pronunciation_base64(phonemes: str, language="en-GB", speaker=None):
    """Generates pronunciation for phonemes string and returns it as a base64 string"""
    is_valid_phonemes_string(phonemes)
    speaker_name = ""
    if speaker is None:
        is_valid_language(language)
        speaker_name = get_single_speaker(language)
    else:
        is_valid_speaker(speaker)
        speaker_name = speaker
    return str(modules.phonemes_to_sound.generator.get_single_pronunciation(speaker=speaker_name, text=phonemes)).replace('\n', '')


def generate_single_pronunciation_to_file(phonemes: str, filename: str, language="en-US", speaker=None):
    """Generates pronunciation for single phonemes string and writes the result into the audio file"""
    is_valid_filename(filename)
    is_valid_phonemes_string(phonemes)
    speaker_name = None
    if speaker is None:
        is_valid_language(language)
        speaker_name = get_single_speaker(language)
    else:
        is_valid_speaker(speaker)
        speaker_name = speaker
    response = generate_single_pronunciation_base64(phonemes, language, speaker_name)
    output = open(filename, "wb")
    decoded = base64.b64decode(response)
    output.write(decoded)
