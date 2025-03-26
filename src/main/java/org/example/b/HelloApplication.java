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
        allGames = readGamesFromJson(savePath);
        if (allGames.isEmpty()) {
            allGames = new ArrayList<>();
            // Varsayılan oyunlar burada eklenebilir
            saveGamesToJson(allGames, savePath);
        }
        allGames.add(new Game("Batman Arkham Knight", "Action-Adventure, Open World, Superhero", "Rocksteady Studios", "Warner Bros. Interactive Entertainment", 2015, 20, "Batman oluyon milleti dövüyon", 208650, 94.4f, "file:src/main/Cover Arts/Batman Arkham Knight Cover Art.jpg"));
        allGames.add(new Game("Bloodborne", "Action Role-Playing", "FromSoftware", "Sony Computer Entertainment", 2015, 44, "Kılıcı alıyon bakıyon karşında canavar kesecem diyon ölüyon yirmi yıl geri gidiyon", 0, 89.5f, null));
        allGames.add(new Game("Dark Souls II: Scholar of the First Sin", "Action Role-Playing", "FromSoftware, QLOC", "Bandai Namco Entertainment", 2017, 100, "Kılıcı alıp ölüyon deliriyon", 0, 89.5f, "file:src/main/Cover Arts/Dark Souls II Scholar of th First Sin Cover Art.jpg"));
        allGames.add(new Game("Dark Souls III", "Action Role-Playing", "FromSoftware", "Sony Computer Entertaiment", 2019, 102.8f, "BAK BİR VARIM BİR YOKUM", 0, 92.5f, "file:src/main/Cover Arts/Dark Souls III Cover Art.jpeg"));
        allGames.add(new Game("Dark Souls Remastered", "Action Role-Playing", "FromSoftware", "Bandai Namco Entertainment", 2022, 37, "Öldün çık", 0, 96.5f, "file:src/main/Cover Arts/Dark Souls Remastered Cover Art.jpeg"));
        allGames.add(new Game("EA Football Club 25", "Sports, Football", "", "", 2015, 44, "", 0, 83.5f, null));
        allGames.add(new Game("Elden Ring", "Open World, Action-Adventure", "FromSoftware", "Bandai Namco Entertainment", 2022, 44, "Açık dünyada kılıçla ölüyon", 1245620, 96.3f, "file:src/main/Cover Arts/Elden Ring Cover Art.jpg"));
        allGames.add(new Game("Hades", "Rouge-Like, Adventure", "SuperGiant Games", "SuperGiant Games", 2015, 44, "Tanrıların gücünü alıyon ölüyon", 0, 92.4f, null));
        allGames.add(new Game("Minecraft", "Sandbox, Survival", "Mojang Studios", "Mojang Studios", 2011, 1002.2f, "Küpürürsün BLOK BLOK BLOK", 0, 98.7f, "file:src/main/Cover Arts/Minecraft Cover Art.jpg"));
        allGames.add(new Game("Resident Evil 4", "Survival Horror, Third-Person Shooter", "Capcom Production Studio 4", "Capcom", 2005, 28.1f, "Zombi var ateş ediyon", 254700, 89.6f, "file:src/main/Cover Arts/Resident Evil 4 Cover Art.jpeg"));
        allGames.add(new Game("Sekiro: Shadows Die Twice", "Action Role-Playing", "FromSoftware", "Activision", 2019, 1001, "Git GUD", 814380, 93.4f, null));
        allGames.add(new Game(
                "The Witcher 3: Wild Hunt",
                "Action RPG, Open World, Fantasy",
                "CD Projekt Red",
                "CD Projekt",
                2015,
                85.3f,
                "Büyülerle ve kılıçla canavar avladığın epik bir hikaye; politik entrikalar, seçimler ve bol bol Gwent kart oyunu.",
                292030,
                97.6f,
                "file:src/main/Cover Arts/The Witcher 3 Cover Art.jpg"
        ));

        allGames.add(new Game(
                "Hollow Knight",
                "Metroidvania, Indie, Platformer",
                "Team Cherry",
                "Team Cherry",
                2017,
                34.7f,
                "Sessiz bir kahramanla yeraltı krallığında kaybolmuş ruhlar ve karanlık sırlarla dolu bir yolculuğa çık.",
                367520,
                94.2f,
                "file:src/main/Cover Arts/Hollow Knight Cover Art.jpg"
        ));

        allGames.add(new Game(
                "Red Dead Redemption 2",
                "Open World, Western, Action-Adventure",
                "Rockstar Studios",
                "Rockstar Games",
                2018,
                112.9f,
                "Bir kanun kaçağının gözünden, Amerika'nın değişen yüzüyle birlikte çürüyen bir çetenin hikayesi.",
                1174180,
                96.5f,
                "file:src/main/Cover Arts/Red Dead Redemption 2 Cover Art.jpg"
        ));

        allGames.add(new Game(
                "Celeste",
                "Platformer, Indie, Pixel Art",
                "Matt Makes Games",
                "Matt Makes Games",
                2018,
                12.6f,
                "Kendi içsel dağını tırmanmaya çalışan bir karakterin duygusal ve zorlu yolculuğu.",
                504230,
                93.7f,
                "file:src/main/Cover Arts/Celeste Cover Art.jpg"
        ));

        allGames.add(new Game(
                "Portal 2",
                "Puzzle, First-Person, Sci-Fi",
                "Valve",
                "Valve",
                2011,
                9.8f,
                "Zekice hazırlanmış bulmacalar ve efsanevi yapay zeka GLaDOS ile mizah dolu bir deneyim.",
                620,
                95.3f,
                "file:src/main/Cover Arts/Portal 2 Cover Art.jpg"
        ));
        allGames.add(new Game("God of War", "Action, Adventure", "Santa Monica Studio", "Sony Interactive Entertainment", 2018, 42.5f, "Kratos oğluyla birlikte tanrılara karşı bir yolculuğa çıkıyor.", 0, 96.0f, null));
        allGames.add(new Game("Cyberpunk 2077", "RPG, Open World, Sci-Fi", "CD Projekt Red", "CD Projekt", 2020, 65.2f, "V'nin distopik bir şehirde hayatta kalma mücadelesi.", 1091500, 89.5f, null));
        allGames.add(new Game("Stardew Valley", "Simulation, Farming, Indie", "ConcernedApe", "ConcernedApe", 2016, 150.0f, "Tarla sür, balık tut, köylülerle arkadaş ol!", 413150, 97.3f, null));
        allGames.add(new Game("Terraria", "Action, Adventure, Survival", "Re-Logic", "Re-Logic", 2011, 180.5f, "Kaz, inşa et, savaş – 2D sandbox evreninde.", 105600, 95.8f, null));
        allGames.add(new Game("DOOM Eternal", "FPS, Action", "id Software", "Bethesda", 2020, 25.7f, "Cehennem ordularını yeryüzünden temizle!", 782330, 91.0f, null));
        allGames.add(new Game("Fallout 4", "RPG, Open World", "Bethesda Game Studios", "Bethesda", 2015, 72.3f, "Nükleer savaş sonrası hayatta kalma ve keşif.", 377160, 88.2f, null));
        allGames.add(new Game("The Elder Scrolls V: Skyrim", "RPG, Open World", "Bethesda Game Studios", "Bethesda", 2011, 220.4f, "Ejderhalarla dolu bir dünyada kaderini yaşa.", 72850, 96.7f, null));
        allGames.add(new Game("Assassin's Creed Valhalla", "Action, RPG, Open World", "Ubisoft Montreal", "Ubisoft", 2020, 80.1f, "Bir Viking olarak İngiltere topraklarına sefer düzenle.", 0, 86.3f, null));
        allGames.add(new Game("Far Cry 5", "FPS, Open World", "Ubisoft", "Ubisoft", 2018, 50.0f, "Kıyamet tarikatına karşı özgürlük mücadelesi.", 552520, 84.6f, null));
        allGames.add(new Game("BioShock Infinite", "FPS, Story Rich", "Irrational Games", "2K", 2013, 20.2f, "Uçan bir şehirde sırlarla dolu bir kurtarma görevi.", 8870, 94.1f, null));
        allGames.add(new Game("Death Stranding", "Adventure, Sci-Fi", "Kojima Productions", "505 Games", 2019, 61.0f, "Kıyamet sonrası Amerika’da bağ kurma yolculuğu.", 1190460, 89.9f, null));
        allGames.add(new Game("It Takes Two", "Co-op, Adventure, Puzzle", "Hazelight", "EA Originals", 2021, 15.4f, "İki kişi, bir aşk hikayesi, bol bulmaca ve aksiyon.", 1426210, 93.6f, null));
        allGames.add(new Game("A Plague Tale: Innocence", "Story Rich, Stealth", "Asobo Studio", "Focus Home Interactive", 2019, 18.3f, "Veba döneminde hayatta kalma ve kardeşlik hikayesi.", 752590, 88.4f, null));
        allGames.add(new Game("Dishonored 2", "Stealth, Action", "Arkane Studios", "Bethesda", 2016, 34.0f, "İntikam, sihir ve gizlilik dolu bir dünya.", 403640, 91.5f, null));
        allGames.add(new Game("Metro Exodus", "FPS, Story Rich", "4A Games", "Deep Silver", 2019, 29.8f, "Post-apokaliptik Rusya’da trenle yolculuk.", 412020, 88.7f, null));
        allGames.add(new Game("The Forest", "Survival, Horror", "Endnight Games", "Endnight Games", 2018, 76.9f, "Bir uçak kazası sonrası canavarlarla dolu ormanda hayatta kal.", 242760, 92.1f, null));
        allGames.add(new Game("Subnautica", "Survival, Underwater, Adventure", "Unknown Worlds", "Unknown Worlds", 2018, 53.5f, "Yabancı bir okyanus gezegeninde yaşam mücadelesi.", 264710, 94.4f, null));
        allGames.add(new Game("Valheim", "Survival, Viking", "Iron Gate AB", "Coffee Stain", 2021, 60.2f, "Viking ölüleriyle dolu bir dünyada destanını yaz.", 892970, 90.7f, null));
        allGames.add(new Game("Ghost of Tsushima", "Action, Open World, Samurai", "Sucker Punch", "Sony", 2020, 51.1f, "Moğol istilasına karşı onurlu bir direniş.", 0, 95.0f, null));
        allGames.add(new Game("Control", "Action, Supernatural", "Remedy", "505 Games", 2019, 26.4f, "Doğaüstü güçlerle dolu gizemli bir federal binayı keşfet.", 870780, 90.0f, null));
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
                Image image = new Image(newValue.getCoverPath());
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