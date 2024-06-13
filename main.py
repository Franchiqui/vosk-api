import speech_recognition as sr


def listen_to_speech():
    """
    Function to recognize speech input from the microphone and display the recognized text.

    Returns:
        str: The recognized text if successful, or an error message if recognition fails.
    """

    recognizer = sr.Recognizer()
    with sr.Microphone() as source:
        print("Speak your request:")
        audio = recognizer.listen(source)

    try:
        # Use Google Speech Recognition API to convert audio to text
        text = recognizer.recognize_google(audio)
        print(f"You said: {text}")
        return text
    except sr.UnknownValueError:
        print("Could not understand the speech")
        return "Could not understand the speech"
    except sr.RequestError as e:
        print(f"Could not obtain results from Google Speech Recognition; {e}")
        return "Could not obtain results from Google Speech Recognition"


# Call the function to start speech recognition
recognized_text = listen_to_speech()
if recognized_text:
    # Process the recognized text as needed (e.g., perform actions based on the text)
    print(f"Processing recognized text: {recognized_text}")
else:
    print("Speech recognition failed.")
