package org.example.b;

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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.Comparator;

public class HelloApplication extends Application {
    private ObservableList<Game> gameList; // Dinamik liste
    private List<Game> allGames; // Tüm oyunların saklandığı liste

    public static void main(String[] args) {
        launch(args);
    }

    // Oyun listesini filtreleme fonksiyonu
    private void filterGameList(String searchText) {
        List<Game> filteredGames = allGames.stream()
                .filter(game -> game.getGameName().toLowerCase().contains(searchText.toLowerCase())||
                        game.getGameGenre().toLowerCase().contains(searchText.toLowerCase())||
                        game.getPublisherName().toLowerCase().contains(searchText.toLowerCase())||
                        game.getDeveloperName().toLowerCase().contains(searchText.toLowerCase())||
                        game.getYearString().contains(searchText.toLowerCase()))
                .collect(Collectors.toList());

        gameList.setAll(filteredGames);
    }

    @Override
    public void start(Stage stage) {
        allGames = new ArrayList<>();
        allGames.add(new Game("Batman Arkham Knight","Action-Adventure, Open World, Superhero","Rocksteady Studios","Warner Bros. Interactive Entertainment",2015,20,"Batman oluyon milleti dövüyon",208650,94.4f,"file:src/main/Cover Arts/Batman Arkham Knight Cover Art.jpg" ));
        allGames.add(new Game("Bloodborne","Action Role-Playing","FromSoftware","Sony Computer Entertainment",2015,44,"Kılıcı alıyon bakıyon karşında canavar kesecem diyon ölüyon yirmi yıl geri gidiyon",0,89.5f,null ));
        allGames.add(new Game("Dark Souls II Scholar of the First Sin","Action Role-Playing","FromSoftware, QLOC","Bandai Namco Entertainment",2017,100,"Kılıcı alıp ölüyon deliriyon",0,89.5f,"file:src/main/Cover Arts/Dark Souls II Scholar of th First Sin Cover Art.jpg" ));
        allGames.add(new Game("Dark Souls III","Action Role-Playing","FromSoftware","Sony Computer Entertaiment",2019,102.8f,"BAK BİR VARIM BİR YOKUM",0,92.5f,"file:src/main/Cover Arts/Dark Souls III Cover Art.jpeg" ));
        allGames.add(new Game("Dark Souls Remastered","Action Role-Playing","FromSoftware","Bandai Namco Entertainment",2022,37,"Öldün çık",0,96.5f,"file:src/main/Cover Arts/Dark Souls Remastered Cover Art.jpeg" ));
        allGames.add(new Game("EA Football Club 25","Sports, Football","","",2015,44,"",0,83.5f,null ));
        allGames.add(new Game("Elden Ring","Open World, Action-Adventure","FromSoftware","Bandai Namco Entertainment",2022,44,"Açık dünyada kılıçla ölüyon",1245620,96.3f,"file:src/main/Cover Arts/Elden Ring Cover Art.jpg" ));
        allGames.add(new Game("Hades","Rouge-Like, Adventure","SuperGiant Games","SuperGiant Games",2015,44,"Tanrıların gücünü alıyon ölüyon",0,92.4f,null ));
        allGames.add(new Game("Minecraft","Sandbox, Survival","Mojang Studios","Mojang Studios",2011,1002.2f,"Küpürürsün BLOK BLOK BLOK",0,98.7f,"file:src/main/Cover Arts/Minecraft Cover Art.jpg" ));
        allGames.add(new Game("Resident Evil 4","Survival Horror, Third-Person Shooter","Capcom Production Studio 4","Capcom",2005,28.1f,"Zombi var ateş ediyon",254700,89.6f,"file:src/main/Cover Arts/Resident Evil 4 Cover Art.jpeg" ));
        allGames.add(new Game("Sekiro: Shadows Die Twice","Action Role-Playing","FromSoftware","Activision",2019,1001,"Git GUD",814380,93.4f,null ));

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
                    setBorder(new Border(new BorderStroke(Color.web("#244658",0.09),BorderStrokeStyle.SOLID,new CornerRadii(5),BorderWidths.DEFAULT)));
                }
            }
        });
        gameListView.setStyle("-fx-background-color: transparent; -fx-text-fill: white;-fx-border-color: #244658; -fx-border-width: 2px; -fx-border-radius: 5 ");

        TextField searchField = new TextField();
        searchField.setPromptText("Oyun ara...");
        searchField.prefWidthProperty().bind(stage.widthProperty().multiply(0.3)); // Arama çubuğu %30 genişlikte
        searchField.prefHeight(30) ;
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterGameList(newValue);
        });
        searchField.setStyle("-fx-background-color: transparent; -fx-text-fill: white;-fx-border-color: #244658; -fx-border-width: 2px; -fx-border-radius: 5 ");

        VBox detailBox = new VBox(10);
        detailBox.setPadding(new Insets(10));
        detailBox.prefWidthProperty().bind(stage.widthProperty().multiply(0.6)); // Detay kutusu %60 genişlikte

        ImageView gameImageView = new ImageView();
        gameImageView.fitWidthProperty().bind(stage.widthProperty().multiply(0.2)); // Oyun resmi %20 genişlikte
        gameImageView.fitHeightProperty().bind(stage.heightProperty().multiply(0.5)); // Oyun resmi %50 yükseklikte
        gameImageView.setPreserveRatio(true);

        HBox InfoBox = new HBox(10);
        gameListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                InfoBox.getChildren().clear();
                Image image = new Image(newValue.getCoverPath());
                gameImageView.setImage(image);
                Label titleLabel = new Label( newValue.getGameName());
                titleLabel.setTextFill(Color.WHITE);
                titleLabel.setFont(Font.font("Arial", FontWeight.BOLD,24));

                TextFlow genreFlow = new TextFlow(
                    new Text("Tür: ") {{ setFont(Font.font("Arial", FontWeight.BOLD, 12)); setFill(Color.WHITE); }},
                    new Text(newValue.getGameGenre()) {{ setFont(Font.font("Arial", FontWeight.NORMAL, 12)); setFill(Color.WHITE); }}
                );

                TextFlow developerFlow = new TextFlow(
                    new Text("Geliştirici: ") {{ setFont(Font.font("Arial", FontWeight.BOLD, 12)); setFill(Color.WHITE); }},
                    new Text(newValue.getDeveloperName()) {{ setFont(Font.font("Arial", FontWeight.NORMAL, 12)); setFill(Color.WHITE); }}
                );

                TextFlow publisherFlow = new TextFlow(
                    new Text("Yayıncı: ") {{ setFont(Font.font("Arial", FontWeight.BOLD, 12)); setFill(Color.WHITE); }},
                    new Text(newValue.getPublisherName()) {{ setFont(Font.font("Arial", FontWeight.NORMAL, 12)); setFill(Color.WHITE); }}
                );

                TextFlow yearFlow = new TextFlow(
                    new Text("Çıkış Yılı: ") {{ setFont(Font.font("Arial", FontWeight.BOLD, 12)); setFill(Color.WHITE); }},
                    new Text(String.valueOf(newValue.getReleaseYear())) {{ setFont(Font.font("Arial", FontWeight.NORMAL, 12)); setFill(Color.WHITE); }}
                );

                TextFlow hoursPlayedFlow = new TextFlow(
                    new Text("Oynama süresi: ") {{ setFont(Font.font("Arial", FontWeight.BOLD, 12)); setFill(Color.WHITE); }},
                    new Text(newValue.getHoursPlayed() + " saat") {{ setFont(Font.font("Arial", FontWeight.NORMAL, 12)); setFill(Color.WHITE); }}
                );

                TextFlow ratingFlow = new TextFlow(
                    new Text("Puan: ") {{ setFont(Font.font("Arial", FontWeight.BOLD, 12)); setFill(Color.WHITE); }},
                    new Text(newValue.getPublicRating() + "/100") {{ setFont(Font.font("Arial", FontWeight.NORMAL, 12)); setFill(Color.WHITE); }}
                );

                TextFlow descriptionFlow = new TextFlow(
                    new Text("Açıklama: ") {{ setFont(Font.font("Arial", FontWeight.BOLD, 12)); setFill(Color.WHITE); }},
                    new Text(newValue.getGameInfo()) {{ setFont(Font.font("Arial", FontWeight.NORMAL, 12)); setFill(Color.WHITE); }}
                );

                TextFlow steamIDFlow = new TextFlow(
                    new Text("SteamID: ") {{ setFont(Font.font("Arial", FontWeight.BOLD, 12)); setFill(Color.WHITE); }},
                    new Text(String.valueOf(newValue.getSteamID())) {{ setFont(Font.font("Arial", FontWeight.NORMAL, 12)); setFill(Color.WHITE); }}
                );

                detailBox.getChildren().setAll(titleLabel, genreFlow, hoursPlayedFlow, developerFlow, publisherFlow,
                        yearFlow, ratingFlow, descriptionFlow, steamIDFlow);
                InfoBox.getChildren().addAll(gameImageView, detailBox);
            }
        });

        HBox.setHgrow(gameListView, Priority.ALWAYS);
        VBox.setVgrow(detailBox, Priority.ALWAYS);
        VBox.setVgrow(gameImageView, Priority.ALWAYS);

        // Sol tarafta arama çubuğu ve liste
        VBox leftBox = new VBox(10, searchField, gameListView);

        // Ana layout olan HBox
        HBox appIn = new HBox(20, leftBox, InfoBox);
        appIn.prefWidthProperty().bind(stage.widthProperty()); // Ana bölge ekran genişliği kadar
        appIn.setPadding(new Insets(10));
        appIn.setFillHeight(true);

        Border buttonBorder = new Border(new BorderStroke(Color.web("#244658"),BorderStrokeStyle.SOLID,new CornerRadii(5),new BorderWidths(2)));

        Label clockLabel = new Label();
        clockLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        clockLabel.setTextFill(Color.WHITE);
        Timeline clock = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            clockLabel.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        }));
        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();


        Button libButton = new Button("Kütüphane");
        libButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        libButton.setBorder(buttonBorder);
        libButton.setStyle("-fx-text-fill: white; -fx-background-color: transparent; -fx-prompt-text-fill: white;");

        Button settingsButton = new Button("Ayarlar");
        settingsButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        settingsButton.setBorder(buttonBorder);
        settingsButton.setStyle("-fx-text-fill: white; -fx-background-color: transparent; -fx-prompt-text-fill: white;");

        Button addButton = new Button("+");
        addButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        addButton.setBorder(buttonBorder);
        addButton.setStyle("-fx-text-fill: white; -fx-background-color: transparent; -fx-prompt-text-fill: white;");

        BorderPane menuPane = new BorderPane();

        HBox leftMenu = new HBox(5,addButton,clockLabel);
        leftMenu.setAlignment(Pos.CENTER_LEFT);
        leftMenu.setPadding(new Insets(5, 10, 5, 10));

        HBox rightMenu = new HBox(10, libButton, settingsButton);
        rightMenu.setAlignment(Pos.CENTER_RIGHT);
        rightMenu.setPadding(new Insets(5, 10, 5, 10));

        menuPane.setLeft(rightMenu);
        menuPane.setRight(leftMenu);
        menuPane.setPadding(new Insets(5));
        Separator separator = new Separator();
        Platform.runLater(() -> {
            Node line = separator.lookup(".line");
            if (line != null) {
                line.setStyle("-fx-background-color: #244658;");
            }
        });

        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            gameListView.setPrefWidth(newVal.doubleValue() * 0.3);
            detailBox.setPrefWidth(newVal.doubleValue() * 0.6);
        });

        stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            gameListView.setPrefHeight(newVal.doubleValue() * 0.8);
            gameImageView.setFitHeight(newVal.doubleValue() * 0.5);
        });

        HBox settingsHBox = new HBox(5);
        settingsHBox.setAlignment(Pos.CENTER);

        VBox settingsPane = new VBox(settingsHBox);
        settingsPane.setAlignment(Pos.CENTER);
        settingsPane.getChildren().add(new Label("Ayarlar Ekranı"));
        settingsPane.setVisible(false);

        libButton.setOnAction(e -> {
            appIn.setVisible(true);
            settingsHBox.setVisible(false);
        });

        settingsButton.setOnAction(e -> {
            appIn.setVisible(false);
            settingsHBox.setVisible(true);
        });

        VBox root = new VBox(menuPane, separator, appIn);
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

        stage.setTitle("STEAM2.0 v31.12.23");
        stage.setScene(windowedScene);
        stage.setMinWidth(800);
        stage.setMinHeight(500);
        windowedScene.setFill(Color.DARKBLUE);
        stage.show();
    }
}