import vosk
import speech_recognition as sr
from vosk import Model, KaldiRecognizer


def listen_to_speech(model_path):
    """
    Function to recognize speech input from the microphone using Vosk and display the recognized text.

    Args:
        model_path (str): The path to the Vosk language model file.

    Returns:
        str: The recognized text if successful, or an error message if recognition fails.
    """

    model = Model(model_path)
    recognizer = KaldiRecognizer(model, 16000)  # Sample rate (usually 16000 Hz)

    with sr.Microphone() as source:
        print("Speak your request:")
        audio = recognizer.get_activation()
        while audio:
            data = recognizer.recognize(audio)
            if data[0] is not None:
                text = data[0][0]
                print(f"You said: {text}")
                return text
            audio = recognizer.get_activation()

    print("Could not understand the speech")
    return "Could not understand the speech"


# Example usage
model_path = "D:/vosk-model-es-0.42/"  # Replace with your actual path
recognized_text = listen_to_speech(model_path)

if recognized_text:
    # Process the recognized text as needed
    print(f"Processing recognized text: {recognized_text}")
else:
    print("Speech recognition failed.")

    
