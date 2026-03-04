package PaooGame.GameStates;

import PaooGame.Entities.Enemy;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;

/// Clasa GameState reprezintă starea jocului, inclusiv informații despre jucător,
/// scor, timp petrecut, nivel, sănătate, statusul jocului,
/// poziția jucătorului și inamicii.
///  Această clasă oferă metode pentru a serializa și deserializa starea jocului în format JSON,
///  precum și pentru a salva și încărca starea jocului într-un format simplu.
///  Clasa GameState este esențială pentru gestionarea progresului jucătorului și a stării curente a jocului.

public class GameState implements Subject{
    private String playerName;
    private int score;
    private int timeSpent;
    private String level;
    private int health;
    private boolean gameStatus;
    private double playerX;
    private double playerY;
    private int currentMap;
    private ArrayList<Enemy> enemy1;
    private ArrayList<Enemy> enemy3;

    private List<Observer> observers = new ArrayList<>();



    // Constructor complet
    public GameState(String playerName, int score, int timeSpent, String level,
                     int health, boolean gameStatus, double playerX, double playerY,
                     int currentMap, ArrayList<Enemy> enemy1, ArrayList<Enemy> enemy3) {
        this.playerName = playerName;
        this.score = score;
        this.timeSpent = timeSpent;
        this.level = level;
        this.health = health;
        this.gameStatus = gameStatus;
        this.playerX = playerX;
        this.playerY = playerY;
        this.currentMap = currentMap;
        this.enemy1 = enemy1;
        this.enemy3 = enemy3;
    }

    // Constructor simplificat (fără inamici - de ex. folosit temporar)
    public GameState(String playerName, int score, int timeSpent, String level,
                     int health, boolean gameStatus, double playerX, double playerY,
                     int currentMap) {
        this(playerName, score, timeSpent, level, health, gameStatus, playerX, playerY, currentMap,
                new ArrayList<>(), new ArrayList<>());
    }

    // Getteri
    public String getPlayerName() { return playerName; }
    public int getScore() { return score; }
    public int getTimeSpent() { return timeSpent; }
    public String getLevel() { return level; }
    public int getHealth() { return health; }
    public boolean isGameStatus() { return gameStatus; }
    public double getPlayerX() { return playerX; }
    public double getPlayerY() { return playerY; }
    public int getCurrentMap() { return currentMap; }
    public ArrayList<Enemy> getEnemy1() { return enemy1; }
    public ArrayList<Enemy> getEnemy3() { return enemy3; }

    // Setteri
    public void setPlayerName(String playerName) { this.playerName = playerName; }
    public void setScore(int score) { this.score = score; }
    public void setTimeSpent(int timeSpent) { this.timeSpent = timeSpent; }
    public void setLevel(String level) { this.level = level; }
    public void setHealth(int health) { this.health = health; }
    public void setGameStatus(boolean gameStatus) { this.gameStatus = gameStatus; }
    public void setPlayerX(double playerX) { this.playerX = playerX; }
    public void setPlayerY(double playerY) { this.playerY = playerY; }
    public void setCurrentMap(int currentMap) { this.currentMap = currentMap; }
    public void setEnemy1(ArrayList<Enemy> enemy1) { this.enemy1 = enemy1; }
    public void setEnemy3(ArrayList<Enemy> enemy3) { this.enemy3 = enemy3; }

    // Serializare JSON
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    // Deserializare JSON
    public static GameState fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, GameState.class);
    }

    // (Opțional) dacă vrei și formate simple, cum aveai înainte
    public String toSaveFormat() {
        return String.join(",",
                playerName,
                String.valueOf(score),
                String.valueOf(timeSpent),
                level,
                String.valueOf(health),
                String.valueOf(gameStatus),
                String.valueOf(playerX),
                String.valueOf(playerY),
                String.valueOf(currentMap)
        );
    }

    public static GameState fromSaveFormat(String saveData) {
        String[] parts = saveData.split(",");
        return new GameState(
                parts[0],                          // playerName
                Integer.parseInt(parts[1]),        // score
                Integer.parseInt(parts[2]),        // timeSpent
                parts[3],                          // level
                Integer.parseInt(parts[4]),        // health
                Boolean.parseBoolean(parts[5]),    // gameStatus
                Double.parseDouble(parts[6]),      // playerX
                Double.parseDouble(parts[7]),      // playerY
                Integer.parseInt(parts[8])         // currentMap
        );
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }



}
