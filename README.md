# Reflekt

Welcome to Reflekt! This application allows users to document their lives through AI-generated images, offering a unique and interactive journaling experience. Users can create, view, and manage their journal entries, with images generated based on their daily reflections.

## Features

- **User Authentication:** Secure login and account creation using Firebase Authentication.
- **Interactive UI:** Responsive design for sorting, searching, and interacting with AI-generated images.
- **AI Integration:** Utilizes GPT-3 Turbo to generate prompts from user responses and DALL-E to create corresponding images.
- **Journal Entry Management:** Seamless workflow for creating, saving, and viewing journal entries.
- **Data Storage:** Firestore for storing journal entry responses and Firebase Storage for managing images.

## Technologies Used

- **Kotlin:** For app development and core functionality.
- **Jetpack Compose:** For modern UI design and responsive layouts.
- **Firebase:** Includes Authentication, Firestore, and Storage for secure user management and data handling.
- **GPT-3 Turbo:** For generating prompts from user input.
- **DALL-E:** For creating images based on generated prompts.
- **MVVM Architecture:** For a clean and maintainable codebase.

## Installation

To run this app locally, follow these steps:

1. Clone the repository: git clone https://github.com/TusharaTalasila/JournalApp.git
2. Navigate to the project directory: cd JournalApp
3. Open the project in Android Studio.
4. Sync the project with Gradle files.
5. Update local properties file: You need to add your own API keys for the `gptKey` and `dalleKey` variables to use.
6. Build and run the app!

## Tutorial

[Add video runthrough here]

## Contact

For questions or feedback, please reach out to tushara.talasila@gmail.com.
