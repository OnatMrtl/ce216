package org.example.b;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;
import java.util.Arrays;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.layout.GridPane;
import javafx.geometry.HPos;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import java.util.Optional;
import java.net.URL;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import java.io.File;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundSize;
import java.util.Objects;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class HelloApplication extends Application {
    private ListView<Game> gameListView;
    private ObservableList<Game> gameList; // Dinamik liste
    private List<Game> allGames; // Tüm oyunların saklandığı liste
    private static Locale currentLocale = new Locale("en"); // Varsayılan İngilizce
    protected Button settingsButton = new Button();
    protected TextField searchField = new TextField();
    protected Button importButton = new Button(messages.getString("import"));
    protected Button exportButton = new Button(messages.getString("export"));
    protected Button helpButton = new Button("Help");
    protected Button libButton = new Button();
    protected Button editButton = new Button();
    protected Text genreText = new Text(messages.getString("genre"));
    protected Text developerText = new Text(messages.getString("developer"));
    protected Text publisherText = new Text(messages.getString("publisher"));
    protected Text releaseYearText = new Text(messages.getString("releaseDate"));
    protected Text hoursPlayedText = new Text(messages.getString("hoursPlayed"));
    protected Text ratingText = new Text(messages.getString("rating"));
    protected Text infoText = new Text(messages.getString("info"));
    protected Text hourText = new Text(messages.getString("hour"));
    protected Text languageText = new Text(messages.getString("language"));
    protected Text favoriteText = new Text(messages.getString("favorite"));
    protected ComboBox<String> filterButton =new ComboBox<>();

    protected Image backgroundImage = new Image(Objects.requireNonNull(getClass().getResource("/cover_arts/Steam Background.jpeg")).toExternalForm());

    private static ResourceBundle messages = ResourceBundle.getBundle("lang", currentLocale);


    public static void main(String[] args) {
        launch(args);
    }

    public static String textToString(Text textNode) {
        if (textNode == null) {
            return "";
        }
        return textNode.getText();
    }

    // Oyun listesini filtreleme fonksiyonu
    private void filterGameList(String searchText) {
        List<Game> filteredGames = allGames.stream()
                .filter(game -> game.getGameName().toLowerCase().contains(searchText.toLowerCase()) ||
                        game.getGameGenre().toLowerCase().contains(searchText.toLowerCase()) ||
                        game.getPublisherName().toLowerCase().contains(searchText.toLowerCase()) ||
                        game.getDeveloperName().toLowerCase().contains(searchText.toLowerCase()) ||
                        game.getYearString().contains(searchText.toLowerCase()))
                .collect(Collectors.toList());

        gameList.setAll(filteredGames);

    }

    @Override
    public void start(Stage stage) {
        String savePath = "games.json";
        genreText = new Text(messages.getString("genre"));
        developerText = new Text(messages.getString("developer"));
        publisherText = new Text(messages.getString("publisher"));
        releaseYearText = new Text(messages.getString("releaseDate"));
        hoursPlayedText = new Text(messages.getString("hoursPlayed"));
        ratingText = new Text(messages.getString("rating"));
        infoText = new Text(messages.getString("info"));
        hourText = new Text(messages.getString("hour"));
        languageText = new Text(messages.getString("language"));
        allGames = readGamesFromJson(savePath);
        if (allGames.isEmpty()) {
            allGames = new ArrayList<>();
            // Varsayılan oyunlar burada eklenebilir
            saveGamesToJson(allGames, savePath);
        }

        Collections.sort(allGames, Comparator.comparing(Game::getGameName));
        gameList = FXCollections.observableArrayList(allGames);
        gameListView = new ListView<>(gameList);
        gameListView.prefWidthProperty().bind(stage.widthProperty().multiply(0.3));
        gameListView.prefHeightProperty().bind(stage.heightProperty().multiply(0.8));
        gameListView.setMinWidth(250);

        gameListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Game game, boolean empty) {
                super.updateItem(game, empty);
                if (empty || game == null) {
                    setText(null);
                    setStyle("-fx-control-inner-background: transparent;");
                    setBorder(null); // Boş hücrede border tamamen kaldırılıyor
                } else {
                    setText(game.getGameName());
                    setFont(Font.font("Arial", FontWeight.BOLD, 12));
                    setStyle("-fx-background-color: transparent; -fx-text-fill: white;");

                    Game selectedGame = gameListView.getSelectionModel().getSelectedItem();
                    if (selectedGame != null && game.equals(selectedGame)) {
                        setBorder(new Border(new BorderStroke(
                                Color.web("#244658"), BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(2)
                        )));
                    } else {
                        setBorder(new Border(new BorderStroke(
                                Color.web("#244658", 0.09), BorderStrokeStyle.SOLID, new CornerRadii(5), BorderWidths.DEFAULT
                        )));
                    }
                }
            }
        });
        gameListView.setStyle("-fx-background-color: transparent; -fx-text-fill: white;-fx-border-color: #244658; -fx-border-width: 2px; -fx-border-radius: 5 ");


        searchField.setPromptText(messages.getString("search"));
        searchField.prefWidthProperty().bind(stage.widthProperty().multiply(0.3)); // Arama çubuğu %30 genişlikte
        searchField.prefHeight(30);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterGameList(newValue);
        });
        searchField.setStyle("-fx-background-color: transparent; -fx-text-fill: white;-fx-border-color: #244658; -fx-border-width: 2px; -fx-border-radius: 5 ");

        VBox detailBox = new VBox(10);
        detailBox.setPadding(new Insets(10));
        detailBox.prefWidthProperty().bind(stage.widthProperty().multiply(0.6));

        ImageView gameImageView = new ImageView();
        gameImageView.fitWidthProperty().bind(stage.widthProperty().multiply(0.2));
        gameImageView.fitHeightProperty().bind(stage.heightProperty().multiply(0.5));
        gameImageView.setPreserveRatio(true);

        HBox InfoBox = new HBox(10);
        gameListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                InfoBox.getChildren().clear();
                String coverPath = newValue.getCoverPath();
                Image image;
                File coverFile = new File(coverPath);
                if (coverFile.exists()) {
                    image = new Image(coverFile.toURI().toString(), false);
                } else {
                    URL coverUrl = getClass().getResource("/" + coverPath);
                    if (coverUrl != null) {
                        image = new Image(coverUrl.toExternalForm(), false);
                    } else {
                        image = new Image(Objects.requireNonNull(getClass().getResource("/cover_arts/NemaFoto.jpg")).toExternalForm());
                    }
                }
                gameImageView.setImage(image);
                Label titleLabel = new Label(newValue.getGameName());
                titleLabel.setTextFill(Color.WHITE);
                titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));

                Font boldFont = Font.font("Arial", FontWeight.BOLD, 12);

                genreText.setFont(boldFont);
                genreText.setFill(Color.WHITE);

                TextFlow genreFlow = new TextFlow(
                        genreText
                        ,
                        new Text(": ") {{
                            setFont(boldFont);
                            setFill(Color.WHITE);
                        }}
                        ,
                        new Text(newValue.getGameGenre()) {{
                            setFont(Font.font("Arial", FontWeight.NORMAL, 12));
                            setFill(Color.WHITE);
                        }}
                );

                developerText.setFont(boldFont);
                developerText.setFill(Color.WHITE);

                TextFlow developerFlow = new TextFlow(
                        developerText,
                        new Text(": ") {{
                            setFont(boldFont);
                            setFill(Color.WHITE);
                        }}
                        ,
                        new Text(newValue.getDeveloperName()) {{
                            setFont(Font.font("Arial", FontWeight.NORMAL, 12));
                            setFill(Color.WHITE);
                        }}
                );

                publisherText.setFont(boldFont);
                publisherText.setFill(Color.WHITE);

                TextFlow publisherFlow = new TextFlow(
                        publisherText,
                        new Text(": ") {{
                            setFont(boldFont);
                            setFill(Color.WHITE);
                        }}
                        ,
                        new Text(newValue.getPublisherName()) {{
                            setFont(Font.font("Arial", FontWeight.NORMAL, 12));
                            setFill(Color.WHITE);
                        }}
                );

                releaseYearText.setFont(boldFont);
                releaseYearText.setFill(Color.WHITE);

                TextFlow yearFlow = new TextFlow(
                        releaseYearText,
                        new Text(": ") {{
                            setFont(boldFont);
                            setFill(Color.WHITE);
                        }}
                        ,
                        new Text(String.valueOf(newValue.getReleaseYear())) {{
                            setFont(Font.font("Arial", FontWeight.NORMAL, 12));
                            setFill(Color.WHITE);
                        }}
                );

                hoursPlayedText.setFont(boldFont);
                hoursPlayedText.setFill(Color.WHITE);
                hourText.setFill(Color.WHITE);

                TextFlow hoursPlayedFlow = new TextFlow(
                        hoursPlayedText,
                        new Text(": ") {{
                            setFont(boldFont);
                            setFill(Color.WHITE);
                        }}
                        ,
                        new Text(newValue.getHoursPlayed() +" ") {{
                            setFont(Font.font("Arial", FontWeight.NORMAL, 12));
                            setFill(Color.WHITE);
                        }},
                        hourText
                );

                ratingText.setFont(boldFont);
                ratingText.setFill(Color.WHITE);
                TextFlow ratingFlow = new TextFlow(
                        ratingText,
                        new Text(": ") {{
                            setFont(boldFont);
                            setFill(Color.WHITE);
                        }}
                        ,
                        new Text(newValue.getPublicRating() + "/100") {{
                            setFont(Font.font("Arial", FontWeight.NORMAL, 12));
                            setFill(Color.WHITE);
                        }}
                );

                infoText.setFont(boldFont);
                infoText.setFill(Color.WHITE);
                TextFlow descriptionFlow = new TextFlow(
                        infoText,
                        new Text(": ") {{
                            setFont(boldFont);
                            setFill(Color.WHITE);
                        }}
                        ,
                        new Text(newValue.getGameInfo()) {{
                            setFont(Font.font("Arial", FontWeight.NORMAL, 12));
                            setFill(Color.WHITE);
                        }}
                );

                TextFlow steamIDFlow = new TextFlow(
                        new Text("SteamID: ") {{
                            setFont(Font.font("Arial", FontWeight.BOLD, 12));
                            setFill(Color.WHITE);
                        }},
                        new Text(String.valueOf(newValue.getSteamID())) {{
                            setFont(Font.font("Arial", FontWeight.NORMAL, 12));
                            setFill(Color.WHITE);
                        }}
                );
                ToggleButton favButton = new ToggleButton("★");
                favButton.getStyleClass().add("button");
                favButton.setText(newValue.isFavGame() ? "★" : "☆");
                favButton.setOnAction(e -> {
                    boolean newState = !newValue.isFavGame();
                    newValue.setFavGame(newState);
                    favButton.setText(newState ? "★" : "☆");
                    saveGamesToJson(allGames, "games.json");

                    String selected = filterButton.getValue();

                    if (selected != null && selected.equals(messages.getString("favorite"))) {
                        List<Game> favorites = allGames.stream()
                                .filter(Game::isFavGame)
                                .collect(Collectors.toList());
                        gameList.setAll(favorites);
                    } else {
                        Collections.sort(allGames, Comparator.comparing(Game::getGameName, String.CASE_INSENSITIVE_ORDER));
                        gameList.setAll(allGames);
                    }

                    gameListView.refresh();
                    gameListView.getSelectionModel().select(newValue);
                });
                Label favLabel = new Label(messages.getString("favorite")+" : ");
                favLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
                favLabel.setStyle("-fx-prompt-text-fill: white;-fx-text-fill: white");
                HBox favHBox = new HBox(10,favLabel,favButton);
                favHBox.setAlignment(Pos.CENTER);
                VBox imageBox = new VBox(10,gameImageView,favHBox);

                editButton = new Button(messages.getString("edit"));
                editButton.getStyleClass().add("button");
                editButton.setMinWidth(Region.USE_PREF_SIZE);
                editButton.setFont(Font.font("Arial", FontWeight.BOLD, 12));
                editButton.setOnAction(e -> {
                    Game game = gameListView.getSelectionModel().getSelectedItem();
                    if (game == null) return;

                    Dialog<ButtonType> dialog = new Dialog<>();
                    dialog.setTitle(messages.getString("edit"));
                    dialog.initOwner(stage);
                    dialog.initModality(Modality.APPLICATION_MODAL);

                    DialogPane pane = dialog.getDialogPane();
                    GridPane grid = new GridPane();
                    grid.setHgap(10);
                    grid.setVgap(10);
                    grid.setPadding(new Insets(20));

                    // Wrap form in a VBox and apply background image
                    VBox contentBox = new VBox(grid);
                    contentBox.setPadding(new Insets(20));
                    BackgroundImage bgImage = new BackgroundImage(
                        backgroundImage,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER,
                        new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
                    );
                    contentBox.setBackground(new Background(bgImage));
                    pane.setBackground(new Background(bgImage));

                    TextField nameField = new TextField(game.getGameName());
                    TextField genreField = new TextField(game.getGameGenre());
                    TextField devField = new TextField(game.getDeveloperName());
                    TextField pubField = new TextField(game.getPublisherName());
                    TextField yearField = new TextField(String.valueOf(game.getReleaseYear()));
                    TextField steamIDField = new TextField(String.valueOf(game.getSteamID()));
                    steamIDField.prefWidthProperty().bind(stage.widthProperty().multiply(0.3));
                    steamIDField.setPrefHeight(30);
                    steamIDField.setStyle(searchField.getStyle());
                    steamIDField.setStyle(steamIDField.getStyle() + "; -fx-text-fill: white; -fx-font-family: Arial;");

                    Label imagePathLabel = new Label(game.getCoverPath());
                    imagePathLabel.setTextFill(Color.WHITE);
                    imagePathLabel.setFont(Font.font("Arial", FontWeight.BOLD, 10));

                    Button uploadButton = new Button(messages.getString("chooseFile"));
                    uploadButton.getStyleClass().add("button");
                    uploadButton.setOnAction(ev -> {
                        FileChooser fileChooser = new FileChooser();
                        fileChooser.setTitle("Select Cover Image");
                        fileChooser.getExtensionFilters().addAll(
                            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
                        );
                        File selectedFile = fileChooser.showOpenDialog(stage);
                        if (selectedFile != null) {
                            try {
                                File destDir = new File("cover_arts");
                                if (!destDir.exists()) destDir.mkdirs();

                                String targetFileName = selectedFile.getName().replaceAll("\\s+", "_");
                                File destFile = new File(destDir, targetFileName);
                                Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                                destFile.setLastModified(System.currentTimeMillis());

                                imagePathLabel.setText("cover_arts/" + targetFileName);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    });

                    // Match searchField style, width, and height for all fields
                    nameField.prefWidthProperty().bind(stage.widthProperty().multiply(0.3));
                    nameField.setPrefHeight(30);
                    nameField.setStyle(searchField.getStyle());

                    genreField.prefWidthProperty().bind(stage.widthProperty().multiply(0.3));
                    genreField.setPrefHeight(30);
                    genreField.setStyle(searchField.getStyle());

                    devField.prefWidthProperty().bind(stage.widthProperty().multiply(0.3));
                    devField.setPrefHeight(30);
                    devField.setStyle(searchField.getStyle());

                    pubField.prefWidthProperty().bind(stage.widthProperty().multiply(0.3));
                    pubField.setPrefHeight(30);
                    pubField.setStyle(searchField.getStyle());

                    yearField.prefWidthProperty().bind(stage.widthProperty().multiply(0.3));
                    yearField.setPrefHeight(30);
                    yearField.setStyle(searchField.getStyle());

                    // Set text color and font family (after base style)
                    nameField.setStyle(nameField.getStyle() + "; -fx-text-fill: white; -fx-font-family: Arial;");
                    genreField.setStyle(genreField.getStyle() + "; -fx-text-fill: white; -fx-font-family: Arial;");
                    devField.setStyle(devField.getStyle() + "; -fx-text-fill: white; -fx-font-family: Arial;");
                    pubField.setStyle(pubField.getStyle() + "; -fx-text-fill: white; -fx-font-family: Arial;");
                    yearField.setStyle(yearField.getStyle() + "; -fx-text-fill: white; -fx-font-family: Arial;");

                    Label nameLabel = new Label(messages.getString("gameName") + ":");
                    nameLabel.setTextFill(Color.WHITE);
                    nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
                    grid.add(nameLabel, 0, 0);
                    grid.add(nameField, 1, 0);

                    Label genreLabel = new Label(messages.getString("genre") + ":");
                    genreLabel.setTextFill(Color.WHITE);
                    genreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
                    grid.add(genreLabel, 0, 1);
                    grid.add(genreField, 1, 1);

                    Label developerLabel = new Label(messages.getString("developer") + ":");
                    developerLabel.setTextFill(Color.WHITE);
                    developerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
                    grid.add(developerLabel, 0, 2);
                    grid.add(devField, 1, 2);

                    Label publisherLabel = new Label(messages.getString("publisher") + ":");
                    publisherLabel.setTextFill(Color.WHITE);
                    publisherLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
                    grid.add(publisherLabel, 0, 3);
                    grid.add(pubField, 1, 3);

                    Label releaseDateLabel = new Label(messages.getString("releaseDate") + ":");
                    releaseDateLabel.setTextFill(Color.WHITE);
                    releaseDateLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
                    grid.add(releaseDateLabel, 0, 4);
                    grid.add(yearField, 1, 4);
                    Label steamIDLabel = new Label("SteamID:");
                    steamIDLabel.setTextFill(Color.WHITE);
                    steamIDLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
                    grid.add(steamIDLabel, 0, 5);
                    grid.add(steamIDField, 1, 5);

                    Label coverImageLabel = new Label(messages.getString("coverImage") + ":");
                    coverImageLabel.setTextFill(Color.WHITE);
                    coverImageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
                    grid.add(coverImageLabel, 0, 6);
                    grid.add(uploadButton, 1, 6);
                    grid.add(imagePathLabel, 1, 7);

                    pane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

                    pane.setContent(contentBox);

                    Button okButton = (Button) pane.lookupButton(ButtonType.OK);
                    okButton.setText(messages.getString("save"));
                    Button cancelButton = (Button) pane.lookupButton(ButtonType.CANCEL);
                    cancelButton.setText(messages.getString("cancel"));
                    okButton.getStyleClass().add("button");
                    cancelButton.getStyleClass().add("button");

                    okButton.setOnAction(ev -> {
                        game.setGameName(nameField.getText());
                        game.setGameGenre(genreField.getText());
                        game.setDeveloperName(devField.getText());
                        game.setPublisherName(pubField.getText());
                        game.setReleaseYear(Integer.parseInt(yearField.getText()));
                        game.setSteamID(Integer.parseInt(steamIDField.getText()));
                        game.setCoverPath(imagePathLabel.getText());
                        // Refresh gameImageView with new cover path
                        File newCoverFile = new File(game.getCoverPath());
                        if (newCoverFile.exists()) {
                            gameImageView.setImage(new Image(newCoverFile.toURI().toString(), false));
                        } else {
                            URL coverUrl = getClass().getResource("/" + game.getCoverPath());
                            if (coverUrl != null) {
                                gameImageView.setImage(new Image(coverUrl.toExternalForm(), false));
                            }
                        }
                        updateInfoBox(game);
                        saveGamesToJson(allGames, savePath);
                        gameListView.refresh();
                        gameListView.getSelectionModel().clearSelection();
                        gameListView.getSelectionModel().select(game);
                        dialog.close();
                    });

                    cancelButton.setOnAction(ev -> dialog.close());
                    dialog.showAndWait();
                });

                detailBox.getChildren().setAll(titleLabel, genreFlow, hoursPlayedFlow, developerFlow, publisherFlow,
                        yearFlow, ratingFlow, descriptionFlow, steamIDFlow);
                InfoBox.getChildren().addAll(imageBox, detailBox,editButton);
            }
        });
        filterButton.setPromptText("≡");
        filterButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white;-fx-border-color: #244658; -fx-border-width: 2px; -fx-border-radius: 5;-fx-arrows-visible: false;-fx-prompt-text-fill: white; ");
        filterButton.setPrefSize(30,30);
        filterButton.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("-fx-background-color: #244658; -fx-text-fill: white;-fx-control-inner-background: transparent;-fx-border-radius: 5;-fx-border-color: white;-fx-border-width: 0.1;-fx-background-radius: 5;-fx-background-insets: 0;");
                } else {
                    setText(item);
                    setFont(Font.font("Arial", FontWeight.BOLD, 12));
                    setStyle("-fx-background-color: #244658; -fx-text-fill: white;-fx-control-inner-background: transparent;-fx-border-radius: 5;-fx-border-color: white;-fx-border-width: 0.1;-fx-background-radius: 5;-fx-background-insets: 0;");
                }
            }
        });

        filterButton.setButtonCell(new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("-fx-background-color: transparent; -fx-text-fill: white;-fx-control-inner-background: transparent;");
                } else {
                    setText(item);
                    setFont(Font.font("Arial", FontWeight.BOLD, 12));
                    setStyle("-fx-background-color: transparent; -fx-text-fill: white;-fx-control-inner-background: transparent;-fx-border-radius: 5;");
                }
            }
        });
        filterButton.getItems().addAll(
                textToString(hoursPlayedText) + " \u2193",           // Oynanma süresi azalan
                textToString(hoursPlayedText) + " \u2191",           // Oynanma süresi artan
                messages.getString("gameName") + " A-Z",             // Oyun ismi A'dan Z'ye
                messages.getString("gameName") + " Z-A",             // Oyun ismi Z'den A'ya
                messages.getString("releaseDate") + " \u2191",       // Çıkış yılı artan
                messages.getString("releaseDate") + " \u2193",       // Çıkış yılı azalan
                messages.getString("rating") + " \u2193",            // Puan azalan
                messages.getString("rating") + " \u2191",            // Puan artan
                messages.getString("steamID") + " \u2193",           // Steam ID azalan
                messages.getString("steamID") + " \u2191",           // Steam ID artan
                messages.getString("favorite") // Favoriler için ayrı filtre
        );
        filterButton.setOnAction(event -> {
            String selected = filterButton.getValue();
            if (selected == null) return;
            if (selected.equals(messages.getString("favorite"))) {
                List<Game> favorites = allGames.stream()
                        .filter(Game::isFavGame)
                        .collect(Collectors.toList());
                Game selectedGame = gameListView.getSelectionModel().getSelectedItem();
                gameList.setAll(favorites);
                if (gameList.contains(selectedGame)) {
                    gameListView.getSelectionModel().select(selectedGame);
                } else {
                    gameListView.getSelectionModel().clearSelection();
                    InfoBox.getChildren().clear();
                }
                gameListView.refresh();
                gameList.sort(Comparator.comparing(Game::getGameName, String.CASE_INSENSITIVE_ORDER));
                return;
            }

            List<Game> sorted = new ArrayList<>(allGames);

            if (selected.contains(hoursPlayedText.getText()) && selected.contains("\u2193")) {
                sorted.sort(Comparator.comparing(Game::getHoursPlayed).reversed());
            } else if (selected.contains(hoursPlayedText.getText()) && selected.contains("\u2191")) {
                sorted.sort(Comparator.comparing(Game::getHoursPlayed));
            } else if (selected.contains(messages.getString("gameName")) && selected.contains("A-Z")) {
                sorted.sort(Comparator.comparing(Game::getGameName, String.CASE_INSENSITIVE_ORDER));
            } else if (selected.contains(messages.getString("gameName")) && selected.contains("Z-A")) {
                sorted.sort(Comparator.comparing(Game::getGameName, String.CASE_INSENSITIVE_ORDER).reversed());
            } else if (selected.contains(messages.getString("releaseDate")) && selected.contains("\u2191")) {
                sorted.sort(Comparator.comparing(Game::getReleaseYear));
            } else if (selected.contains(messages.getString("releaseDate")) && selected.contains("\u2193")) {
                sorted.sort(Comparator.comparing(Game::getReleaseYear).reversed());
            } else if (selected.contains(messages.getString("rating")) && selected.contains("\u2193")) {
                sorted.sort(Comparator.comparing(Game::getPublicRating).reversed());
            } else if (selected.contains(messages.getString("rating")) && selected.contains("\u2191")) {
                sorted.sort(Comparator.comparing(Game::getPublicRating));
            } else if (selected.contains(messages.getString("steamID")) && selected.contains("\u2193")) {
                sorted.sort(Comparator.comparing(Game::getSteamID).reversed());
            } else if (selected.contains(messages.getString("steamID")) && selected.contains("\u2191")) {
                sorted.sort(Comparator.comparing(Game::getSteamID));
            }
            Game selectedGame = gameListView.getSelectionModel().getSelectedItem();
            gameList.setAll(sorted);
            if(!gameList.contains(selectedGame)) {
                gameListView.setCellFactory(param -> new ListCell<>() {
                    @Override
                    protected void updateItem(Game game, boolean empty) {

                        super.updateItem(game, empty);
                        if (empty || game == null) {
                            setText(null);
                            setStyle("-fx-control-inner-background: transparent;");
                        } else {
                            setText(game.getGameName());
                            setFont(Font.font("Arial", FontWeight.BOLD, 12));
                            setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
                            setBorder(new Border(new BorderStroke(
                                    Color.web("#244658", 0.09), BorderStrokeStyle.SOLID, new CornerRadii(5), BorderWidths.DEFAULT
                            )));
                        }
                    }
                });
                gameListView.getSelectionModel().clearSelection();
                InfoBox.getChildren().clear();
                gameListView.refresh();
            }else{
                gameListView.getSelectionModel().clearSelection();
                gameListView.getSelectionModel().select(selectedGame);
                gameListView.refresh();
            }
            gameListView.setStyle("-fx-background-color: transparent; -fx-text-fill: white;-fx-border-color: #244658; -fx-border-width: 2px; -fx-border-radius: 5 ");
            gameListView.refresh();
        });
        HBox searchBox = new HBox(10,searchField,filterButton);



        HBox.setHgrow(gameListView, Priority.ALWAYS);
        VBox.setVgrow(detailBox, Priority.ALWAYS);
        VBox.setVgrow(gameImageView, Priority.ALWAYS);

        VBox leftBox = new VBox(10, searchBox, gameListView);

        HBox appIn = new HBox(20, leftBox, InfoBox);
        appIn.prefWidthProperty().bind(stage.widthProperty());
        appIn.setPadding(new Insets(10));
        appIn.setFillHeight(true);

        Border buttonBorder = new Border(new BorderStroke(Color.web("#244658"), BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(2)));
        Border buttonBorderBold = new Border(new BorderStroke(Color.web("#244658"),BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(3)));

        Label clockLabel = new Label();
        clockLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        clockLabel.setTextFill(Color.WHITE);
        clockLabel.getStyleClass().add("clock");
        Timeline clock = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            clockLabel.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        }));
        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();



        libButton.setText(messages.getString("library"));
        libButton.getStyleClass().add("button");
        libButton.getStyleClass().add("library-button");
        libButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));


        settingsButton.setText(messages.getString("settings"));
        settingsButton.getStyleClass().add("button");
        settingsButton.getStyleClass().add("settings-button");
        settingsButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        exportButton.getStyleClass().add("button");
        exportButton.getStyleClass().add("export-button");
        exportButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        exportButton.setOnAction(e -> {
            //d0n4t
        });

        importButton.getStyleClass().add("button");
        importButton.getStyleClass().add("import-button");
        importButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        importButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("JSON Dosyası Seç");
            fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JSON Dosyaları", "*.json")
            );
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                List<Game> importedGames = readGamesFromJson(selectedFile.getAbsolutePath());
                // After reading importedGames
                for (Game g : importedGames) {
                    String coverPath = g.getCoverPath();
                    File sourceFile = new File(coverPath);
                    if (sourceFile.exists()) {
                        File destDir = new File("cover_arts");
                        if (!destDir.exists()) destDir.mkdirs();
                        String targetName = sourceFile.getName().replaceAll("\\s+", "_");
                        File destFile = new File(destDir, targetName);
                        try {
                            Files.copy(sourceFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                            g.setCoverPath("cover_arts/" + targetName);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
                for (Game g : importedGames) {
                    if (!allGames.contains(g)) {
                        allGames.add(g);
                    }
                }
                gameList.setAll(allGames);
                saveGamesToJson(allGames, "games.json");
            }
        });
        helpButton.getStyleClass().add("button");
        helpButton.getStyleClass().add("help-button");
        helpButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        helpButton.setText("Help");
        helpButton.setOnAction(e -> {
            Dialog<Void> dialog = new Dialog<>();
            
            String helpTitle = messages.containsKey("help") ? messages.getString("help") : "Help";
            String okText = messages.containsKey("ok") ? messages.getString("ok") : "OK";
            String helpMessage = messages.containsKey("help.message") ?
                    messages.getString("help.message") :
                    "- To add games via JSON or XML, click the + button in the top-right corner.\n" +
                            "- Click on a game in the list to view its details.\n" +
                            "- Use the search bar to find games by name, developer, year, etc.\n" +
                            "- Use the filter dropdown to sort or view only favorites.\n" +
                            "- Click the star icon in the details to add/remove from favorites.\n" +
                            "- You can switch languages in the Settings tab.\n" +
                            "- Game cover images are shown automatically; a default image is used if missing.";

            dialog.setTitle(helpTitle);
            dialog.initModality(Modality.APPLICATION_MODAL);

            DialogPane pane = dialog.getDialogPane();
            pane.getButtonTypes().add(ButtonType.OK);

            Text helpContent = new Text(helpMessage);
            helpContent.setFont(Font.font("Arial", 12));
            helpContent.setFill(Color.WHITE);

            VBox content = new VBox(helpContent);
            content.setPadding(new Insets(20));
            content.setSpacing(10);

            BackgroundImage bgImage = new BackgroundImage(
                    backgroundImage,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
            );
            content.setBackground(new Background(bgImage));
            pane.setBackground(new Background(bgImage));

            pane.setContent(content);

            Button okButton = (Button) pane.lookupButton(ButtonType.OK);
            okButton.setText(okText);
            okButton.getStyleClass().add("button");

            dialog.showAndWait();
        });

        BorderPane menuPane = new BorderPane();
        menuPane.getStyleClass().add("top-menu");

        HBox leftMenu = new HBox(5, helpButton, importButton, exportButton, clockLabel);
        leftMenu.setAlignment(Pos.CENTER_LEFT);
        leftMenu.setPadding(new Insets(5, 10, 5, 10));

        HBox rightMenu = new HBox(10, libButton, settingsButton);
        rightMenu.setAlignment(Pos.CENTER_RIGHT);
        rightMenu.setPadding(new Insets(5, 10, 5, 10));

        menuPane.setLeft(rightMenu);
        menuPane.setRight(leftMenu);
        menuPane.setPadding(new Insets(5));

        Separator separator = new Separator();



        languageText.setFill(Color.WHITE);
        languageText.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        HBox settingsHBox = new HBox(5);
        VBox settingsVBox = new VBox(5);
        ComboBox<String> languageSelector = new ComboBox<>();
        HBox langHBox = new HBox(5,languageText,new Text(" "),languageSelector);

        languageSelector.getItems().addAll("Türkçe", "English", "繁體中文");
        languageSelector.setValue("English");
        languageSelector.setStyle("-fx-background-color: transparent; -fx-progress-color: transparent;-fx-text-fill: white;-fx-border-color: #244658;-fx-border-radius: 5;");

        settingsVBox.getChildren().addAll(langHBox);
        settingsHBox.getChildren().add(settingsVBox);

        settingsVBox.setAlignment(Pos.CENTER);
        langHBox.setAlignment(Pos.CENTER);
        settingsHBox.setAlignment(Pos.CENTER);

        languageSelector.setOnAction(event -> {
            String selectedLang = languageSelector.getValue();
            if (selectedLang.equals("English")) {
                currentLocale = new Locale("en");
            } else if (selectedLang.equals("Türkçe")){
                currentLocale = new Locale("tr");
            }
            else if (selectedLang.equals("繁體中文")) {
                currentLocale = Locale.TRADITIONAL_CHINESE;
            }
            messages = ResourceBundle.getBundle("lang", currentLocale);
            updateLanguage();
        });

        languageSelector.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setFont(Font.font("Arial", FontWeight.BOLD, 12));
                    setStyle("-fx-background-color: #244658; -fx-text-fill: white;-fx-control-inner-background: transparent;-fx-border-radius: 5;-fx-border-color: white;-fx-border-width: 0.1;-fx-background-radius: 5;-fx-background-insets: 0;");
                }
            }
        });

        languageSelector.setButtonCell(new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setFont(Font.font("Arial", FontWeight.BOLD, 12));
                    setStyle("-fx-background-color: transparent; -fx-text-fill: white;-fx-control-inner-background: transparent;-fx-border-radius: 5;");
                }
            }
        });

        VBox root = new VBox(menuPane, separator, appIn);

        libButton.setOnAction(e -> {
            root.getChildren().clear();
            root.getChildren().addAll(menuPane,separator,appIn);
            libButton.setBorder(buttonBorderBold);
            settingsButton.setBorder(buttonBorder);
        });

        settingsButton.setOnAction(e -> {
            root.getChildren().clear();
            root.getChildren().addAll(menuPane,separator,settingsHBox);
            libButton.setBorder(buttonBorder);
            settingsButton.setBorder(buttonBorderBold);
        });


        BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT, // Yatay tekrar
                BackgroundRepeat.NO_REPEAT, // Dikey tekrar
                BackgroundPosition.CENTER,  // Konum
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );
        root.setBackground(new Background(background));

        Scene windowedScene = new Scene(root);
        windowedScene.getStylesheets().add(getClass().getResource("/scrollBarStyle.css").toExternalForm());
        windowedScene.getStylesheets().add(getClass().getResource("/buttonStyles.css").toExternalForm());
        windowedScene.getStylesheets().add(getClass().getResource("/topMenuStyles.css").toExternalForm());
        stage.setTitle("STEAM2.0 v31.12.23");
        stage.setScene(windowedScene);
        stage.setMinWidth(800);
        stage.setMinHeight(500);
        windowedScene.setFill(Color.DARKBLUE);
        stage.show();

    }

    private void updateLanguage() {
        libButton.setText(messages.getString("library"));
        settingsButton.setText(messages.getString("settings"));
        searchField.setPromptText(messages.getString("search"));
        genreText.setText(messages.getString("genre"));
        developerText.setText(messages.getString("developer"));
        publisherText.setText(messages.getString("publisher"));
        releaseYearText.setText(messages.getString("releaseDate"));
        hoursPlayedText.setText(messages.getString("hoursPlayed"));
        ratingText.setText(messages.getString("rating"));
        infoText.setText(messages.getString("info"));
        hourText.setText(messages.getString("hour"));
        languageText.setText(messages.getString("language"));
        favoriteText.setText(messages.getString("favorite"));
        editButton.setText(messages.getString("edit"));
        helpButton.setText(messages.getString("help"));
        importButton.setText(messages.getString("import"));
        exportButton.setText(messages.getString("export"));

        filterButton.getItems().clear();
        filterButton.getItems().addAll(
                textToString(hoursPlayedText) + " \u2193",           // Oynanma süresi azalan
                textToString(hoursPlayedText) + " \u2191",           // Oynanma süresi artan
                messages.getString("gameName") + " A-Z",             // Oyun ismi A'dan Z'ye
                messages.getString("gameName") + " Z-A",             // Oyun ismi Z'den A'ya
                messages.getString("releaseDate") + " \u2191",       // Çıkış yılı artan
                messages.getString("releaseDate") + " \u2193",       // Çıkış yılı azalan
                messages.getString("rating") + " \u2193",            // Puan azalan
                messages.getString("rating") + " \u2191",            // Puan artan
                messages.getString("steamID") + " \u2193",           // Steam ID azalan
                messages.getString("steamID") + " \u2191",            // Steam ID artan
                messages.getString("favorite")
        );
    }
    private void saveGamesToJson(List<Game> games, String filePath) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(games, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Game> readGamesFromJson(String filePath) {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(filePath)) {
            Game[] games = gson.fromJson(reader, Game[].class);
            return games != null ? new ArrayList<>(Arrays.asList(games)) : new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void updateInfoBox(Game game) {
        // Seçimi temizleyip tekrar seçerek InfoBox'ı günceller
        gameListView.getSelectionModel().clearSelection();
        gameListView.getSelectionModel().select(game);
    }
}