# UniMarket

Kotlin/XML Android student marketplace prototype for OPSC6312.

## Included
- Native Kotlin/XML Android app shell
- Configurable REST API contract for auth, categories, listings, wishlist, messages, saved searches, and sync
- Room, Retrofit, and Firebase Messaging dependencies ready for expansion
- GitHub Actions build workflow

## Run
Open `UniMarket` in Android Studio. The app is Kotlin/XML only. Set the API base URL in the Kotlin Retrofit service when connecting a hosted REST API.

## Endpoint contract
`POST /api/auth/register`, `POST /api/auth/login`, `GET /api/categories`, `GET/POST /api/listings`, `GET/POST /api/wishlist`, `GET/POST /api/messages`, `POST /api/saved-searches`, and `POST /api/sync`.

## Release notes
v1.0: initial UniMarket Kotlin/XML shell, Kotlin API contract, marketplace search interaction, and Android automation scaffold. Production hardening still requires database persistence, JWT validation, Firebase project configuration, image/blob storage, and final device testing.

## Evidence
The provided brief images describe the UniMarket marketplace, required screens, REST API, offline synchronization, notifications, multilingual support, GitHub Actions, documentation, and Play Store preparation.
