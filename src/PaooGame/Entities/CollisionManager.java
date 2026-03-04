package PaooGame.Entities;

import PaooGame.Tiles.Hazard;
import PaooGame.Tiles.Tile;
import PaooGame.Entities.Player;
import PaooGame.Entities.Golem;
import PaooGame.Entities.Enemy;

import java.awt.*;
import java.util.*;
import java.util.List;

/// Clasa CollisionManager este responsabilă pentru gestionarea coliziunilor între entități și tile-uri.
/// Aceasta include verificarea coliziunilor pentru jucător, inamici, gloanțe și obiecte speciale precum Imunosupresoare și KeyCarduri.
/// Această clasă verifică coliziunile verticale și orizontale, precum și coliziunile cu hazarduri.
/// De asemenea, gestionează coliziunile cu inamicii și verifică dacă jucătorul a colectat obiecte speciale.

public class CollisionManager {
    private final Tile[][] tileMap;
    public CollisionManager(Tile[][] tileMap) {
        this.tileMap = tileMap;
    }

    public void checkEntityCollision(MovableEntity entity) {
        checkVertical(entity);
        checkHorizontal(entity);
    }

    private void checkVertical(MovableEntity player) {
        int headX = Math.min((player.getX() + player.getWidth() / 2) / Tile.TILE_WIDTH, tileMap[0].length - 1);
        int headY = Math.min(player.getY() / Tile.TILE_HEIGHT, tileMap.length - 1);

        int footX = Math.min((player.getX() + player.getWidth() / 2) / Tile.TILE_WIDTH, tileMap[0].length - 1);
        int footY = Math.min((player.getY() + player.getHeight()) / Tile.TILE_HEIGHT, tileMap.length - 1);

        int centerY = Math.min((player.getY() + player.getHeight() / 2) / Tile.TILE_HEIGHT, tileMap.length - 1);
        int centerX = Math.min((player.getX() + player.getWidth() / 2) / Tile.TILE_WIDTH, tileMap[0].length - 1);

        headX = Math.max(0, headX);
        headY = Math.max(0, headY);
        footX = Math.max(0, footX);
        footY = Math.max(0, footY);
        centerX = Math.max(0, centerX);
        centerY = Math.max(0, centerY);

        Tile tileAtCenter = tileMap[centerY][centerX];
        Tile tileAtFeet1 = tileMap[Math.min(footY, tileMap.length - 1)][centerX];


// intri pe scară dacă tile-ul la picioare sau centru este ladder
        boolean nearLadder = (tileAtCenter != null && tileAtCenter.isLadder())
                || (tileAtFeet1 != null && tileAtFeet1.isLadder());

        // Verificare jos (podea)
        if (footY >= 0 && footY < tileMap.length && footX >= 0 && footX < tileMap[0].length) {
            Tile tileBelow = tileMap[footY][footX];
            if (tileBelow != null && tileBelow.isSolid()) {
                if (!player.isOnGround() && player.getVelocityY() > player.getSafeFallSpeed()) {
                    int damage = (int) ((player.getVelocityY() - player.getSafeFallSpeed()) * 10); // calculează damage
                    if(damage > 0)
                        player.applyFallDamage(damage);
                }

                player.setY(footY * Tile.TILE_HEIGHT - player.getHeight());
                player.setVelocityY(0);
                player.setOnGround(true);
            } else {
                player.setOnGround(false);
            }
        } else {
            player.setOnGround(false);
        }

        // Verificare sus (cap)
        if (player.getVelocityY() < 0) { // doar când se mișcă în sus
            if (headY >= 0 && headY < tileMap.length && headX >= 0 && headX < tileMap[0].length) {
                Tile tileAbove = tileMap[headY][headX];
                if (tileAbove != null && tileAbove.isSolid()) {
                    player.setY((headY + 1) * Tile.TILE_HEIGHT); // îl împinge în jos
                    player.setVelocityY(0); // oprește săritura
                }
            }
        }
        if (nearLadder) {
            player.setClimbing(true);
            player.setOnGround(false);
            player.setVelocityY(0);
        } else {
            player.setClimbing(false);
        }
    }



    private void checkHorizontal(MovableEntity player) {


        int leftX = (player.getX()) / Tile.TILE_WIDTH ;
        int rightX = (player.getX() + player.getWidth() - 1) / Tile.TILE_WIDTH;
        int topY = player.getY() / Tile.TILE_HEIGHT;
        int bottomY = (player.getY() + player.getHeight() - 1) / Tile.TILE_HEIGHT;

        if (topY < 0 || bottomY >= tileMap.length)
            return;

        // Verificare stânga
        if (player.getSpeedX() > 0 && leftX >= 0) {
            for (int y = topY; y <= bottomY; y++) {
                if (leftX < tileMap[0].length) { // asigurare în caz de bug
                    Tile t = tileMap[y][leftX];
                    if (t != null && t.isSolid()) {
                        player.setX((leftX + 1) * Tile.TILE_WIDTH);
                        return;
                    }
                }
            }
        }

        // Verificare dreapta
        if (player.getSpeedX() > 0 && rightX <= tileMap[0].length) {
            for (int y = topY; y <= bottomY; y++) {
                Tile t = tileMap[y][rightX];
                if (t != null && t.isSolid()) {
                    player.setX(rightX * Tile.TILE_WIDTH - player.getWidth());
                    return;
                }
            }
        }


    }


    public void checkBulletCollisions(Bullet bullet) {
        int bulletX = bullet.getX();
        int bulletY = bullet.getY();
        int bulletWidth = bullet.getWidth();
        int bulletHeight = bullet.getHeight();

        int startX = bulletX / Tile.TILE_WIDTH;
        int endX = (bulletX + bulletWidth - 1) / Tile.TILE_WIDTH;

        int startY = bulletY / Tile.TILE_HEIGHT;
        int endY = (bulletY + bulletHeight - 1) / Tile.TILE_HEIGHT;

        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                if (x >= 0 && x < tileMap[0].length && y >= 0 && y < tileMap.length) {
                    Tile tile = tileMap[y][x];
                    if (tile != null && tile.isSolid()) {
                        bullet.setActive(false);  // Dezactivează glonțul
                        return; // Ieșim imediat după prima coliziune
                    }
                }
            }
        }
    }

    public void checkBulletHitsEnemies(List<Enemy> enemies, Bullet bullet) {
        Rectangle bulletBounds = bullet.getBounds();

        for (Enemy enemy : enemies) {
            if (enemy.isAlive() && bulletBounds.intersects(enemy.getHitBox())) {
                int min = 10;
                int max = 15;
                int randomNum = (int)(Math.random() * (max - min + 1)) + min;
                enemy.takeDamage(randomNum);  // Poți ajusta damage-ul
                bullet.setActive(false);
                break; // Presupunem că glonțul lovește un singur inamic
            }
        }
    }

    public void checkBulletHitsPlayer(Player player, List<Bullet> bullets) {
        Rectangle playerBox = player.getBounds();

        Iterator<Bullet> it = bullets.iterator();
        while (it.hasNext()) {
            Bullet b = it.next();
            if (!b.isActive()) continue;

            if (b.getBounds().intersects(playerBox)) {
                player.takeDamage(10); // sau orice damage vrei tu
                b.setActive(false);
                it.remove(); // scoatem glonțul
            }
        }
    }


    public void checkEntityHazardCollision(Player player) {
        int tileX = (player.getX() + player.getWidth() / 2) / Tile.TILE_WIDTH;
        int tileY = (player.getY() + player.getHeight() - 1) / Tile.TILE_HEIGHT;

        Tile tileUnderPlayer = tileMap[tileY][tileX];

        if (tileUnderPlayer instanceof Hazard) {
            player.takeDamage(1);
        }
    }
    public void checkImunosupressorCollision(Player player, List<Imunosupressor> items) {
        Rectangle playerBox = new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight());

        Iterator<Imunosupressor> it = items.iterator();
        while (it.hasNext()) {
            Imunosupressor im = it.next();
            Rectangle imBox = new Rectangle(im.getX(), im.getY(), im.getWidth(), im.getHeight());

            if (playerBox.intersects(imBox)) {
                player.addImunosupressor();  // -> metoda din Player care crește count-ul
                it.remove();                 // scoatem din joc
                System.out.println("Collected Imunosupressor!");
            }
        }
    }
    public void checkKeyCardCollision(Player player, KeyCard keyCard) {
        Rectangle playerBox = new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight());
        Rectangle keyCardBox = new Rectangle(keyCard.getX(), keyCard.getY(), keyCard.getWidth(), keyCard.getHeight());
        if(playerBox.intersects(keyCardBox) && !keyCard.isCollected()) {
            keyCard.setCollected(true);
            player.keyCardSetActive(true);
        }
    }


    public void checkPlayerEnemyCollision(Player player, ArrayList<Enemy> enemies) {
        for (Enemy enemy : enemies) {
            if (!enemy.isAlive() || enemy instanceof Golem || enemy instanceof Imp) continue; // Ignorăm golemul

        }
    }
}