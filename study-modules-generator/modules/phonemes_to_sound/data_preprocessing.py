from pathlib import Path
import re
SPEAKERS = {

    # US English
    "Ivy": "en-US",
    "Joanna": "en-US",
    "Joey": "en-US",
    "Justin": "en-US",
    "Kendra": "en-US",
    "Kimberly": "en-US",
    "Salli": "en-US",

    # Australian English
    "Nicole": "en-AU",
    "Russell": "en-AU",

    # British English
    "Emma": "en-GB",
    "Brian": "en-GB",
    "Amy": "en-GB",

    # Indian English
    "Raveena": "en-IN",

    # Welsh English
    "Geraint": "en-GB-WLS",

    # Brazilian Portuguese
    "Ricardo": "pt-BR",
    "Victoria": "pt-BR",

    # Dutch
    "Lotte": "nl-NL",
    "Ruben": "nl-NL",

    # French
    "Mathieu": "fr-FR",
    "Celine": "fr-FR",

    # Canadian French
    "Chantal": "fr-CA",

    # German
    "Hans": "de-DE",


    # Icelandic
    "Dora": "is-IS",
    "Karl": "is-IS",

    # Italian
    "Carla": "it-IT",
    "Giorgio": "it-IT",

    # Japanese
    "Mizuki": "ja-JP",

    # Norwegian
    "Liv": "nn-NN",

    # Polish
    "Maja": "pl-PL",
    "Jan": "pl-PL",
    "Ewa": "pl-PL",

    # Portuguese
    "Cristiano": "pt-PT",
    "Ines": "pt-PT",

    # Romanian
    "Carmen": "ro-RO",

    # Russian
    "Maxim": "ru-RU",
    "Tatyana": "ru-RU",

    # Spanish
    "Enrique": "es-ES",

    # US Spanish
    "Penelope": "es-US",
    "Miguel": "es-US",

    # Castilian Spanish
    "Conchita": "es-CA",

    # Swedish
    "Astrid": "sv-SE",

    # Turkish
    "Filiz": "tr-TR",

    # Welsh
    "Gwyneth": "cy-GB"

}


def is_valid_speaker(speaker: str):
    """Checks if the speaker's name is valid"""
    if speaker not in SPEAKERS.keys():
        raise ValueError(f"Unknown Speaker: {speaker}")


def is_valid_language(language: str):
    """Checks if the input language is valid"""
    if language not in SPEAKERS.values():
        raise ValueError(f"Unknown Language: {language}")


def is_valid_phonemes_string(phonemes: str):
    """Checks if phonemes string is valid"""
    if len(phonemes) == 0:
        raise ValueError("Expected phonemes string with length greater than 0!")
    if phonemes.isnumeric():
        raise ValueError("Unable to process number!")


def get_single_speaker(language: str):
    """Returns the first speaker for the input language"""
    is_valid_language(language)
    return list(SPEAKERS.keys())[list(SPEAKERS.values()).index(language)]


def has_single_speaker(language: str):
    """Checks if there is only one speaker for the input language"""
    is_valid_language(language)
    return


def is_valid_filename(filename: str):
    """Checks if output file name is valid"""

    if len(filename) == 0:
        raise ValueError("Length of the filename must be greater than 0 !")

    if re.search(r'[^A-Za-z0-9_\-\\]', Path(filename).stem):
        raise ValueError(f"Wrong Filename: {filename}")

