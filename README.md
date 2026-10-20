# PropTrack

PropTrack helps an independent real-estate agent register properties and clients, check available apartments, and find clients whose operation type, financial capacity, and stated preferences fit a property.

## Main flows

- Register a building and one of its apartments, including location, price, operation, availability, and features.
- Register a client with contact details, rental income or purchase budget, and preferences.
- Browse saved clients and available properties, with compatible clients shown for each property.
- Choose a profile image from the gallery or take one with the device camera.
- Persist the special display preference locally and show animated feedback when it changes.

## Structure

The Android app uses Jetpack Compose and MVVM. Screens live under `ui/screens`, state and validation are handled by `viewmodel`, Room entities and database access are in `data`, and `repository` coordinates local persistence and compatibility filtering. Navigation routes are defined in `navigation`.

Room stores buildings, apartments, and clients in `proptrack.db`; DataStore stores display preferences and the agent profile image URI. Both data sources work locally without a network connection.

## Build

Open the project in Android Studio and run the `app` configuration on an emulator or Android device. The project uses the Gradle wrapper, Kotlin, Compose, Room, KSP, and DataStore.

Camera capture and persisted gallery access should be checked on a physical device as the final device-specific verification step.
