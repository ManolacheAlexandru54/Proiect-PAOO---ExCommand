package PaooGame.Entities;

import PaooGame.Game;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/// Clasa abstractă Enemy reprezintă un inamic în joc.
/// Aceasta este o clasă de bază pentru diferite tipuri de inamici.
/// Conține metode abstracte pentru actualizare, desenare și obținerea hitbox-ului.
/// De asemenea, gestionează viața inamicului, coliziunile și scorul jucătorului.
/// Clasa Enemy este extinsă de clase specifice precum Golem și Imp.
/// Clasa Enemy include metode pentru a lua daune, verifica dacă inamicul este în viață,
/// Serializeaza și deserializeaza inamicii în format JSON.
/// De asemenea, include metode pentru a converti o listă de inamici în date și invers.
/// Această clasă este esențială pentru gestionarea comportamentului și stării inamicilor în joc.

public abstract class Enemy {
    protected int x, y;
    protected int width, height;
    protected int hp;
    protected boolean alive;

    public Enemy(int x, int y, int width, int height, int hp) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.hp = hp;
        this.alive = true;
    }

    public abstract void update();
    public abstract void draw(Graphics g);
    public abstract Rectangle getHitBox();

    public void takeDamage(int amount) {
        hp -= amount;
        if (hp <= 0) {
            alive = false;
            hp = 0;
            Game.player.increaseScore(100); // Add score when enemy dies
            System.out.println("Enemy defeated! Score increased.");
        }
    }

    public boolean isAlive() {
        return alive;
    }


    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public void setX(int i) {
        this.x = i;
    }

    public void setY(int i) {
        this.y = i;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getHp() {
        return hp;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    // Metodă pentru deserializarea din JSON (opțional)
    public static Enemy fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Enemy.class);
    }

    public static ArrayList<String> convertEnemiesToData(ArrayList<Enemy> enemies) {
        ArrayList<String> enemyDataList = new ArrayList<>();
        Gson gson = new Gson(); // Utilizăm Gson pentru serializare

        for (Enemy enemy : enemies) {
            // Creăm o hartă pentru x, y și hp
            Map<String, Object> enemyData = new HashMap<>();
            enemyData.put("x", enemy.getX());
            enemyData.put("y", enemy.getY());
            enemyData.put("hp", enemy.getHp());
            enemyData.put("type", enemy.getClass().getSimpleName());

            // Convertim în JSON și adăugăm în lista finală
            enemyDataList.add(gson.toJson(enemyData));
        }

        System.out.println("Enemies before saving: ");
        for (Enemy enemy : enemies) {
            System.out.println("x=" + enemy.getX() + ", y=" + enemy.getY() + ", hp=" + enemy.getHp());
        }

        return enemyDataList;
    }

    public static ArrayList<Enemy> convertDataToEnemies(ArrayList<String> enemiesData) {
        ArrayList<Enemy> enemies = new ArrayList<>();
        Gson gson = new Gson(); // Utilizăm Gson pentru deserializare

        for (String enemyDataJson : enemiesData) {
            // Mapăm JSON-ul în datele inamicului
            Map<String, Object> enemyData = gson.fromJson(enemyDataJson, new TypeToken<Map<String, Object>>() {}.getType());
        
            // Extragem datele inamicului
            int x = ((Double) enemyData.get("x")).intValue();
            int y = ((Double) enemyData.get("y")).intValue();
            int hp = ((Double) enemyData.get("hp")).intValue();
            String type = (String) enemyData.get("type");


            // Construim un inamic pe baza acestor date
            Enemy enemy = null;
            if ("Golem".equals(type)) {
                enemy = new Golem(x, y);
            } else if ("Imp".equals(type)) {
                enemy = new Imp(x, y);
            }
            if (enemy != null) {
                enemy.setHp(hp);
                enemies.add(enemy);
            }

        }

        System.out.println("Enemies after loading: ");
        for (Enemy enemy : enemies) {
            System.out.println("x=" + enemy.getX() + ", y=" + enemy.getY() + ", hp=" + enemy.getHp());
        }

        return enemies;
    }

    public void setAlive(boolean b) {
        alive = b;
    }
}