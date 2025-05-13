package org.example.b;

public class Game {
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
    private boolean favGame;

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
    public boolean isFavGame() { return favGame; }
    public void setFavGame(boolean favGame) { this.favGame = favGame; }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public void setGameGenre(String gameGenre) {
        this.gameGenre = gameGenre;
    }

    public void setDeveloperName(String developerName) {
        this.developerName = developerName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public void setSteamID(int SteamID) {
        this.SteamID = SteamID;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }
}
