import requests
import base64
SERVICE_URL = "https://iawll6of90.execute-api.us-east-1.amazonaws.com/production"


def get_single_pronunciation(speaker: str, text: str):
    """Sends to the service phonemes string and speaker and returns the result as the base64 string"""
    json = {
        "text": f"{text}",
        "speaker": f"{speaker}"
    }
    return requests.post(SERVICE_URL, json=json).json()
