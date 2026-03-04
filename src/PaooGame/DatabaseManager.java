package PaooGame;

import PaooGame.Entities.Enemy;
import PaooGame.Entities.Imunosupressor;
import PaooGame.Entities.KeyCard;
import PaooGame.GameStates.GameState;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/// Clasa DatabaseManager gestionează conexiunea la baza de date SQLite
/// și oferă metode pentru inițializarea bazei de date, salvarea și încărcarea stării jocului.
/// Aceasta creează o tabelă pentru a stoca informațiile despre salvările de joc,
/// inclusiv numele jucătorului, scorul, timpul petrecut, nivelul, sănătatea, starea jocului,
/// poziția jucătorului, inamicii, starea keycard-ului și imunosupresoarele.
/// Metoda `initializeDatabase` creează tabela dacă nu există deja,
/// iar metodele `saveGame` și `loadLastGame` permit salvarea și încărcarea stării jocului.

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:game_data.db";

    public static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            String sql = """
    CREATE TABLE IF NOT EXISTS game_save (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        player_name TEXT,
        score INTEGER,
        timeScore INTEGER,
        level TEXT,
        health INTEGER,
        game_status BOOLEAN,
        player_x DOUBLE,
        player_y DOUBLE,
        current_map INTEGER,
        enemies_map1 TEXT,
        enemies_map3 TEXT,
        keycard_state TEXT,
        keycard_x DOUBLE,
        keycard_y DOUBLE,
        imunosuppressors_map1 TEXT,
        imunosuppressors_map3 TEXT,
        imunosuppressors_counter INTEGER
    );
""";
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

public static void saveGame(GameState gameState) {
    String query = """
        INSERT INTO game_save
        (player_name, score, timeScore, level, health, game_status, player_x, player_y, current_map, enemies_map1, enemies_map3, keycard_state, imunosuppressors_map1, imunosuppressors_map3, imunosuppressors_counter, keycard_x, keycard_y)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    """;

    try (Connection conn = DriverManager.getConnection(DB_URL);
         PreparedStatement pstmt = conn.prepareStatement(query)) {

        Gson gson = new Gson();

        // Salvează inamicii
        ArrayList<String> enemiesMap1JsonList = Enemy.convertEnemiesToData(Game.getEnemies1());
        ArrayList<String> enemiesMap3JsonList = Enemy.convertEnemiesToData(Game.getEnemies3());
        String enemiesMap1Json = gson.toJson(enemiesMap1JsonList);
        String enemiesMap3Json = gson.toJson(enemiesMap3JsonList);

        // Salvează KeyCard
        String keycardState = gson.toJson(Game.keyCard.isCollected()); // true dacă a fost colectat

        // Salvează Imunosupressors corect ca liste de obiecte JSON
        ArrayList<Map<String, Object>> imunosData1 = new ArrayList<>();
        for (Imunosupressor imunos : Game.imunosupressorsMap1) {
            Map<String, Object> imunosData = new HashMap<>();
            imunosData.put("x", imunos.getX());
            imunosData.put("y", imunos.getY());
            imunosData.put("collected", imunos.isCollected());
            imunosData1.add(imunosData);
        }
        String imunosMap1Json = gson.toJson(imunosData1);

        ArrayList<Map<String, Object>> imunosData3 = new ArrayList<>();
        for (Imunosupressor imunos : Game.imunosupressorsMap3) {
            Map<String, Object> imunosData = new HashMap<>();
            imunosData.put("x", imunos.getX());
            imunosData.put("y", imunos.getY());
            imunosData.put("collected", imunos.isCollected());
            imunosData3.add(imunosData);
        }
        String imunosMap3Json = gson.toJson(imunosData3);

        // Setăm datele pentru fiecare coloană
        pstmt.setString(1, gameState.getPlayerName());
        pstmt.setInt(2, gameState.getScore());
        pstmt.setInt(3, gameState.getTimeSpent());
        pstmt.setString(4, gameState.getLevel());
        pstmt.setInt(5, gameState.getHealth());
        pstmt.setBoolean(6, gameState.isGameStatus());
        pstmt.setDouble(7, gameState.getPlayerX());
        pstmt.setDouble(8, gameState.getPlayerY());
        pstmt.setInt(9, gameState.getCurrentMap());
        pstmt.setString(10, enemiesMap1Json);
        pstmt.setString(11, enemiesMap3Json);
        pstmt.setString(12, keycardState);
        pstmt.setString(13, imunosMap1Json);
        pstmt.setString(14, imunosMap3Json);
        pstmt.setInt(15, Game.player.Imunosupressor_Count);
        pstmt.setInt(16, Game.keyCard.getX()); // Coordonata X a keyCard-ului
        pstmt.setInt(17, Game.keyCard.getY()); // Coordonata Y a keyCard-ului


        pstmt.executeUpdate();
    } catch (SQLException e) {
        System.out.println("Error saving game state: " + e.getMessage());
    }
}

    public static GameState loadLastGame() {
        String query = "SELECT * FROM game_save ORDER BY id DESC LIMIT 1";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                Gson gson = new Gson();

                // Restaurăm inamicii
                String enemiesMap1Json = rs.getString("enemies_map1");
                String enemiesMap3Json = rs.getString("enemies_map3");
                Type type = new TypeToken<ArrayList<String>>() {}.getType();
                ArrayList<String> enemiesData1 = gson.fromJson(enemiesMap1Json, type);
                ArrayList<String> enemiesData3 = gson.fromJson(enemiesMap3Json, type);
                ArrayList<Enemy> enemiesMap1 = Enemy.convertDataToEnemies(enemiesData1);
                ArrayList<Enemy> enemiesMap3 = Enemy.convertDataToEnemies(enemiesData3);

                // Restaurăm KeyCard
                boolean keycardCollected = gson.fromJson(rs.getString("keycard_state"), Boolean.class);
                int keycardX = rs.getInt("keycard_x");
                int keycardY = rs.getInt("keycard_y");

// Creăm keyCard-ul cu poziția originală
                Game.keyCard = new KeyCard(keycardX, keycardY);

                Game.keyCard.setCollected(keycardCollected); // Setăm starea colectată dacă a fost colectat
                Game.player.keyCardSetActive(keycardCollected); // Sincronizăm starea în Player


                // Restaurăm Imunosupressors
                String imunosMap1Json = rs.getString("imunosuppressors_map1");
                String imunosMap3Json = rs.getString("imunosuppressors_map3");

                ArrayList<Imunosupressor> imunosMap1 = new ArrayList<>();
                ArrayList<Map<String, Object>> imunosData1 = gson.fromJson(imunosMap1Json, new TypeToken<ArrayList<Map<String, Object>>>() {}.getType());
                for (Map<String, Object> data : imunosData1) {
                    int x = ((Double) data.get("x")).intValue();
                    int y = ((Double) data.get("y")).intValue();
                    boolean collected = (Boolean) data.get("collected");
                    Imunosupressor imunos = new Imunosupressor(x, y);
                    if (collected) imunos.setCollected();
                    imunosMap1.add(imunos);
                }

                ArrayList<Imunosupressor> imunosMap3 = new ArrayList<>();
                ArrayList<Map<String, Object>> imunosData3 = gson.fromJson(imunosMap3Json, new TypeToken<ArrayList<Map<String, Object>>>() {}.getType());
                for (Map<String, Object> data : imunosData3) {
                    int x = ((Double) data.get("x")).intValue();
                    int y = ((Double) data.get("y")).intValue();
                    boolean collected = (Boolean) data.get("collected");
                    Imunosupressor imunos = new Imunosupressor(x, y);
                    if (collected) imunos.setCollected();
                    imunosMap3.add(imunos);
                }
                int collectedImunosCount = rs.getInt("imunosuppressors_counter");
                //Game.player.Imunosupressor_Count = collectedImunosCount;


                // Setăm starea restaurată
                Game.setEnemies1(enemiesMap1);
                Game.setEnemies3(enemiesMap3);
                Game.imunosupressorsMap1 = imunosMap1;
                Game.imunosupressorsMap3 = imunosMap3;

                Game.player.Imunosupressor_Count = collectedImunosCount;

                return new GameState(
                        rs.getString("player_name"),
                        rs.getInt("score"),
                        rs.getInt("timeScore"),
                        rs.getString("level"),
                        rs.getInt("health"),
                        rs.getBoolean("game_status"),
                        rs.getDouble("player_x"),
                        rs.getDouble("player_y"),
                        rs.getInt("current_map"),
                        enemiesMap1,
                        enemiesMap3
                );
            }
        } catch (SQLException e) {
            System.out.println("Error loading game state: " + e.getMessage());
        }
        return null;
    }

}


/*
    DATE DE SALVAT IN BAZA DE DATE:

        - numele jucatorului -- idk
        - timeScore
        - score
        - game state ul in sine ( tot ce trebuie sa se deseneze pe ecran atunci cand se da load )
 */