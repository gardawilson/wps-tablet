# Repository Guidelines

## Project Structure & Module Organization
This is a single-module Android app. Production code lives in `app/src/main/java/com/example/myapplication/`, with feature activities at the package root and supporting code grouped under `api/`, `model/`, `utils/`, and `config/`. UI resources are in `app/src/main/res/`, static assets in `app/src/main/assets/`, unit tests in `app/src/test/java/`, and instrumented tests in `app/src/androidTest/java/`. Build outputs are generated under `app/build/` and `app/release/` and should not be committed.

## Build, Test, and Development Commands
Use the Gradle wrapper from the repository root:

- `./gradlew assembleDebug` builds the debug APK.
- `./gradlew assembleRelease` builds the release artifact.
- `./gradlew test` runs local JVM unit tests.
- `./gradlew connectedAndroidTest` runs instrumented tests on a device or emulator.
- `./gradlew lint` runs Android lint checks when you want a static review before a PR.

## Coding Style & Naming Conventions
Follow the existing Java style: 4-space indentation, `PascalCase` for classes, `camelCase` for methods and fields, and lowercase package names. Keep feature classes in descriptive files such as `SawnTimber.java` or `ProsesProduksiFJ.java`. Prefer small helper methods over long activity methods, and keep new APIs, models, and utilities in the matching package. No formatter is enforced in Gradle, so match the surrounding code.

## Testing Guidelines
Test files already follow `*Test.java` for unit tests and `*InstrumentedTest.java` for Android tests. Add tests beside the code they cover, and keep names specific to the feature or behavior under test, for example `ProsesProduksiS4SInstrumentedTest`. Run `./gradlew test` for fast feedback and `./gradlew connectedAndroidTest` for UI, database, or device-dependent flows.

## Commit & Pull Request Guidelines
Recent commits use short prefixes such as `feat:` and `chore:` followed by a clear imperative summary. Keep commits focused and descriptive. Pull requests should explain what changed, why it changed, and how it was verified. Include screenshots or screen recordings for UI changes, and mention any required setup such as `local.properties` values or a test device.

## Configuration & Secrets
Keep secrets and machine-specific values out of version control. `local.properties` is used for database and service endpoints such as `DB_IP`, `DB_USER`, and `BASE_URL_API`. Keystores (`*.jks`, `*.keystore`) and log files are ignored; do not add them back to the repo.
