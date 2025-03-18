package org.example.b;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.text.Font;

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

        gameList.setAll(filteredGames); // ListView'in içeriğini güncelle
    }

    public void benjaminFormatter(Button benjamin) {
        benjamin.setText("NABER LAN");
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
        // ObservableList oluştur ve ListView'e bağla
        gameList = FXCollections.observableArrayList(allGames);
        ListView<Game> gameListView = new ListView<>(gameList);
        gameListView.setMinWidth(250);


        // Arama çubuğu (Search Bar)
        TextField searchField = new TextField();
        searchField.setPromptText("Oyun ara...");
        searchField.setMinHeight(30);

        // ListView'de sadece oyun isimlerini göstermek için CellFactory kullan
        gameListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Game game, boolean empty) {
                super.updateItem(game, empty);
                if (empty || game == null) {
                    setText(null);
                } else {
                    setText(game.getGameName()); // Sadece oyun ismi gösterilecek
                }
            }
        });

        // Sağ tarafta oyun detaylarını gösterecek VBox
        VBox detailBox = new VBox(10);
        detailBox.setPadding(new Insets(10));

        // Oyun resmi göstermek için ImageView
        ImageView gameImageView = new ImageView();
        gameImageView.setFitWidth(200); // Resmin genişliği
        gameImageView.setFitHeight(300);

        HBox InfoBox = new HBox(10);

        // ListView seçimi değiştiğinde detayları güncelle
        gameListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Önce InfoBox içeriğini temizle
                InfoBox.getChildren().clear();

                // Yeni resmi yükle
                Image image = new Image(newValue.getCoverPath());
                gameImageView.setImage(image);

                // Yeni bilgileri oluştur
                Label titleLabel = new Label( newValue.getGameName());
                titleLabel.setFont(Font.font("Arial", FontWeight.BOLD,24));
                Label genreLabel = new Label("Tür: "+newValue.getGameGenre());
                Label developerLabel = new Label("Geliştirici: " + newValue.getDeveloperName());
                Label publisherLabel = new Label("Yayıncı: " + newValue.getPublisherName());
                Label yearLabel = new Label("Çıkış Yılı: " + newValue.getReleaseYear());
                Label hoursPlayedLabel = new Label("Oynama süresi: " + newValue.getHoursPlayed()+" saat");
                Label ratingLabel = new Label("Puan: " + newValue.getPublicRating()+"/100");
                Label descriptionLabel = new Label("Açıklama: " + newValue.getGameInfo());
                Label steamIDLabel = new Label("SteamID: " + newValue.getSteamID());

                // Detayları güncelle
                detailBox.getChildren().setAll(titleLabel, genreLabel,hoursPlayedLabel, developerLabel, publisherLabel, yearLabel, ratingLabel, descriptionLabel, steamIDLabel);
                InfoBox.getChildren().addAll(gameImageView, detailBox);
            }
        });

        // Arama çubuğu için dinleyici ekle
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterGameList(newValue);
        });


        // Sol tarafta arama çubuğu ve liste
        VBox leftBox = new VBox(10, searchField, gameListView);

        // Ana layout olan HBox
        HBox appIn = new HBox(20, leftBox, InfoBox);
        appIn.setPadding(new Insets(10));
        appIn.setFillHeight(true);

        Label clockLabel = new Label();
        Button libButton = new Button("Kütüphane");
        Button settingsButton = new Button("Ayarlar");
        Button addButton = new Button("+");
        BorderPane menuPane = new BorderPane();

        HBox leftMenu = new HBox(5,addButton,clockLabel);
        leftMenu.setAlignment(Pos.CENTER_LEFT);
        leftMenu.setPadding(new Insets(5, 10, 5, 10));

        HBox rightMenu = new HBox(10, libButton, settingsButton);
        rightMenu.setAlignment(Pos.CENTER_RIGHT);
        rightMenu.setPadding(new Insets(5, 10, 5, 10));


        Timeline clock = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            clockLabel.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        }));
        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();
        clockLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5px;");

        menuPane.setLeft(rightMenu);
        menuPane.setRight(leftMenu);
        menuPane.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        menuPane.setPadding(new Insets(5));

        VBox root = new VBox(menuPane, appIn);

        Scene windowedScene = new Scene(root);
        Scene fullscreenScene = new Scene(new VBox(new Label("Full Screen Mode")));

        stage.setTitle("STEAM2.0 v31.12.23");


        stage.setScene(windowedScene);
        stage.setMinWidth(800);
        stage.setMinHeight(500);
        windowedScene.setFill(Color.DEEPSKYBLUE);
        stage.show();
    }
}
class Game {
    private String gameName;
    private String gameGenre;
    private String developerName;
    private String publisherName;
    private int releaseYear;
    private float hoursPlayed;
    private String gameInfo;
    private int SteamID;
    private float publicRating;
    private String coverPath;

    public Game(String gameName,String gameGenre, String developerName, String publisherName, int releaseYear, float hoursPlayed, String gameInfo, int SteamID, float publicRating, String coverPath) {
        this.gameInfo = gameInfo;
        this.gameName = gameName;
        this.gameGenre = gameGenre;
        this.hoursPlayed = hoursPlayed;
        this.publisherName = publisherName;
        this.releaseYear = releaseYear;
        this.developerName = developerName;
        this.SteamID = SteamID;
        this.publicRating = publicRating;
        if (coverPath != null && new java.io.File(coverPath.replace("file:", "")).exists()) {
            this.coverPath = coverPath;
        } else {
            this.coverPath = "file:src/main/Cover Arts/NemaFoto.jpg";
        }
    }

    public float getHoursPlayed() { return hoursPlayed; }
    public String getGameInfo() { return gameInfo; }
    public String getGameName() { return gameName; }
    public String getPublisherName() { return publisherName; }
    public int getReleaseYear() { return releaseYear; }
    public String getDeveloperName() { return developerName; }
    public int getSteamID() { return SteamID; }
    public float getPublicRating() { return publicRating; }
    public String getCoverPath() { return coverPath; }
    public String getGameGenre() { return gameGenre; }
    public String getYearString(){
        return String.valueOf(getReleaseYear());
    }
}