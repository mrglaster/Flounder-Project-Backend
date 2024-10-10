REQUIRE_ARTICLES = ["de"]
REQUIRE_DECLINATION = ["de"]
SUPPORTED_LANGUAGES = ["ru", "en", 'de']
GERMAN_GENUS = {
    "m": "der",
    "f": "die",
    "n": "das"
}


def articles_required(lang_code):
    """Checks if current language requires defined articles """
    return lang_code in REQUIRE_ARTICLES


def declination_required(lang_code):
    """Checks if current language has words declination by gender etc"""
    return lang_code in REQUIRE_DECLINATION
