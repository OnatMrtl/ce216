package org.example.b;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;
import java.util.Arrays;
import javafx.stage.FileChooser;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class HelloApplication extends Application {
    private ObservableList<Game> gameList; // Dinamik liste
    private List<Game> allGames; // Tüm oyunların saklandığı liste
    private static Locale currentLocale = new Locale("en"); // Varsayılan Türkçe
    protected Button settingsButton = new Button();
    protected TextField searchField = new TextField();
    protected Button addButton = new Button("+");
    protected Button libButton = new Button();
    protected Text genreText = new Text(messages.getString("genre"));
    protected Text developerText = new Text(messages.getString("developer"));
    protected Text publisherText = new Text(messages.getString("publisher"));
    protected Text releaseYearText = new Text(messages.getString("releaseDate"));
    protected Text hoursPlayedText = new Text(messages.getString("hoursPlayed"));
    protected Text ratingText = new Text(messages.getString("rating"));
    protected Text infoText = new Text(messages.getString("info"));
    protected Text hourText = new Text(messages.getString("hour"));
    protected Text languageText = new Text(messages.getString("language"));
    protected ComboBox<String> filterButton =new ComboBox<>();

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
        ListView<Game> gameListView = new ListView<>(gameList);
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
                } else {
                    setText(game.getGameName());
                    setFont(Font.font("Arial", FontWeight.BOLD, 12));
                    setStyle("-fx-background-color: transparent; -fx-text-fill: white;-fx-control-inner-background: transparent;");
                    setBorder(new Border(new BorderStroke(Color.web("#244658", 0.09), BorderStrokeStyle.SOLID, new CornerRadii(5), BorderWidths.DEFAULT)));
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
                try {
                    image = new Image(coverPath, false);
                    if (image.isError()) {
                        image = new Image("file:src/main/Cover Arts/NemaFoto.jpg");
                    }
                } catch (Exception e) {
                    image = new Image("file:src/main/Cover Arts/NemaFoto.jpg");
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

                detailBox.getChildren().setAll(titleLabel, genreFlow, hoursPlayedFlow, developerFlow, publisherFlow,
                        yearFlow, ratingFlow, descriptionFlow, steamIDFlow);
                InfoBox.getChildren().addAll(gameImageView, detailBox);
            }
        });


        filterButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white;-fx-border-color: #244658; -fx-border-width: 2px; -fx-border-radius: 5 ");
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
                messages.getString("steamID") + " \u2191"            // Steam ID artan
        );
        filterButton.setOnAction(event -> {
            String selected = filterButton.getValue();
            if (selected == null) return;

            List<Game> sorted = new ArrayList<>(gameList);

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

            gameList.setAll(sorted);
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
        Timeline clock = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            clockLabel.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        }));
        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();



        libButton.setText(messages.getString("library"));
        libButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        libButton.setBorder(buttonBorderBold);
        libButton.setStyle("-fx-text-fill: white; -fx-background-color: transparent; -fx-prompt-text-fill: white;");


        settingsButton.setText(messages.getString("settings"));
        settingsButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        settingsButton.setBorder(buttonBorder);
        settingsButton.setStyle("-fx-text-fill: white; -fx-background-color: transparent; -fx-prompt-text-fill: white;");


        addButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        addButton.setBorder(buttonBorder);
        addButton.setStyle("-fx-text-fill: white; -fx-background-color: transparent; -fx-prompt-text-fill: white;");

        addButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("JSON Dosyası Seç");
            fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JSON Dosyaları", "*.json")
            );
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                List<Game> importedGames = readGamesFromJson(selectedFile.getAbsolutePath());
                for (Game g : importedGames) {
                    if (!allGames.contains(g)) {
                        allGames.add(g);
                    }
                }
                gameList.setAll(allGames);
                saveGamesToJson(allGames, "games.json");
            }
        });

        BorderPane menuPane = new BorderPane();

        HBox leftMenu = new HBox(5, addButton, clockLabel);
        leftMenu.setAlignment(Pos.CENTER_LEFT);
        leftMenu.setPadding(new Insets(5, 10, 5, 10));

        HBox rightMenu = new HBox(10, libButton, settingsButton);
        rightMenu.setAlignment(Pos.CENTER_RIGHT);
        rightMenu.setPadding(new Insets(5, 10, 5, 10));

        menuPane.setLeft(rightMenu);
        menuPane.setRight(leftMenu);
        menuPane.setPadding(new Insets(5));

        Separator separator = new Separator();


        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            gameListView.setPrefWidth(newVal.doubleValue() * 0.3);
            detailBox.setPrefWidth(newVal.doubleValue() * 0.6);
        });

        stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            gameListView.setPrefHeight(newVal.doubleValue() * 0.8);
            gameImageView.setFitHeight(newVal.doubleValue() * 0.5);
        });

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


        Image backgroundImage = new Image("file:src/main/Cover Arts/Steam Background.jpeg");
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
                messages.getString("steamID") + " \u2191"            // Steam ID artan
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
}