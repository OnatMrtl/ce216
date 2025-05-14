jpackage ^
  --type app-image ^
  --input target ^
  --name Ce216App ^
  --main-jar B-1.0-SNAPSHOT.jar ^
  --main-class org.example.b.HelloApplication ^
  --module-path "C:\Program Files\Java\javafx-sdk-17.0.15\lib" ^
  --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.base ^
  --icon "icon.ico" ^
  --app-version 1.0.0 ^