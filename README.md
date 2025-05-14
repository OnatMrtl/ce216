# Game Library Manager

A JavaFX desktop application for managing your personal game library. Easily add, edit, and browse your collection of games, including details like genre, developer, publisher, release year, hours played, rating, and cover art.

## Features

- Add, edit, and remove games from your library
- Import/export game data to/from `games.json`
- Favorite games and sort/filter your collection
- Display detailed information, including cover art and Steam ID
- Multi-language support (via resource bundles)
- Data persistence using JSON (via Gson)
- UI built with JavaFX

## Getting Started

### Prerequisites

- Java 17 or higher
- [JavaFX SDK 17.0.15](https://gluonhq.com/products/javafx/)
- Maven (for building)

### Building

Clone the repository and build with Maven:
git clone https://github.com/OnatMrtl/ce216
cd game-library-manager
mvn clean package
### Running

You can run the application using Maven or your IDE:
mvn javafx:run
Or run the generated JAR (ensure JavaFX is on your module path):
java --module-path /path/to/javafx-sdk-17.0.15/lib --add-modules javafx.controls,javafx.fxml -jar target/ce216-1.0-SNAPSHOT.jar
### File Structure

- `src/main/java/org/example/b` — Main application source code
- `games.json` — Game data file (created/updated by the app)
- `cover_arts` — Folder for game cover images

## Dependencies

- [JavaFX](https://openjfx.io/)
- [Gson](https://github.com/google/gson)

## License

This project is for educational purposes. See the `javafx-sdk-17.0.15/legal/javafx.base/LICENSE` file for details.

## Credits

- JavaFX and related libraries are © Oracle and/or its affiliates, used under their respective licenses.
- See `javafx-sdk-17.0.15/legal` for third-party license information.

---
# Game Library Manager

A JavaFX desktop application for managing your personal game library. Easily add, edit, and browse your collection of games, including details like genre, developer, publisher, release year, hours played, rating, and cover art.

## Features

- Add, edit, and remove games from your library
- Import/export game data to/from `games.json`
- Favorite games and sort/filter your collection
- Display detailed information, including cover art and Steam ID
- Multi-language support (via resource bundles)
- Data persistence using JSON (via Gson)
- Modern UI built with JavaFX

## Getting Started

### Prerequisites

- Java 17 or higher
- [JavaFX SDK 17.0.15](https://gluonhq.com/products/javafx/)
- Maven (for building)

### Building

Clone the repository and build with Maven:

```
git clone https://github.com/OnatMrtl/ce216
cd ce216
mvn clean package
```

### Running

You can run the application using Maven or your IDE:

```
mvn javafx:run
```

Or run the generated JAR (ensure JavaFX is on your module path):

```
java --module-path /path/to/javafx-sdk-17.0.15/lib --add-modules javafx.controls,javafx.fxml -jar target/ce216-1.0-SNAPSHOT.jar
```

### File Structure

- `src/main/java/org/example/b` — Main application source code
- `games.json` — Game data file (created/updated by the app)
- `cover_arts` — Folder for game cover images

## Dependencies

- [JavaFX](https://openjfx.io/)
- [Gson](https://github.com/google/gson)

## License

This project is for educational purposes. See the `javafx-sdk-17.0.15/legal/javafx.base/LICENSE` file for details.

## Credits

- JavaFX and related libraries are © Oracle and/or its affiliates, used under their respective licenses.
- See `javafx-sdk-17.0.15/legal` for third-party license information.
