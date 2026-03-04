package PaooGame.Entities;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import PaooGame.Game;
import PaooGame.Graphics.Assets;

public class StarBoss {
    private int x, y;
    private List<LaserBeam> lasers = new ArrayList<>();
    private long lastFireTime;
    private long fireCooldown = 1500; // 1.5 secunde între lasere
    private final int MIN_X = 100;
    private final int MAX_X = 700;
    private final long totalDuration = 12_0000; // 2 minute în milisecunde
    public long levelStartTime = System.currentTimeMillis();
    private boolean finished = false;
    private int laserCount = 1; // numărul de lasere pe care le trage

    public StarBoss(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update(long currentTime, Player player) {
        long timeElapsed = currentTime - levelStartTime;

        if (!finished && timeElapsed >= totalDuration) {
            finished = true;
            lasers.clear();
            System.out.println("SA TERMINAT FINISHED " + finished);
        }

        if (finished) return;

        if (timeElapsed > totalDuration) {
            return; // Steaua nu mai trage
        }

        // Determină dificultatea
        laserCount = 1;
        fireCooldown = 3000; // default

        if (timeElapsed > 30_000) {
            fireCooldown = 2200; // după 30 secunde era la 2000
            laserCount = 1;
        }
        if (timeElapsed > 60_000) {
            fireCooldown = 1800; // după 60 secunde era la 1300
            laserCount = 2;
        }
        if (timeElapsed > 90_000) {
            fireCooldown = 1800; // după 90 secunde era la 900 de schimbat inapoi
            laserCount = 3;
        }

        if (currentTime - lastFireTime > fireCooldown) {
            // Calculăm centrul jucătorului
            int playerCenterX = player.getX() + player.getWidth() / 2;
            int playerCenterY = player.getY() + player.getHeight() / 2;
            
            // Calculăm unghiul către centrul jucătorului
            double baseAngle = Math.toDegrees(Math.atan2(playerCenterY - y, playerCenterX - x));

            lasers.add(new LaserBeam(x, y, baseAngle)); // raza principală

            if (laserCount >= 2) {
                int offset1 = -15 + (int)(Math.random() * 30);
                lasers.add(new LaserBeam(x, y, baseAngle + offset1));
            }
            if (laserCount == 3) {
                int offset2 = -10 + (int)(Math.random() * 20);
                lasers.add(new LaserBeam(x, y, baseAngle + offset2));
            }

            lastFireTime = currentTime;
        }

        lasers.removeIf(LaserBeam::isDone);
        for (LaserBeam l : lasers)
            l.update(currentTime);
    }


    public void draw(Graphics g) {
       // g.setColor(Color.YELLOW);
        //g.fillOval(x - 20, y - 20, 40, 40);
        g.drawImage(Assets.StarBoss, x - 32, y - 32, 64, 64, null);
        if (finished) {
            System.out.println("SA TERMIANT FINISHED" + finished);
            return; // nu mai desenăm razele după ce s-a terminat
        }

        if (Game.getInstance().getDeathMenu().isVisible()) {
            return;
        }


        for (LaserBeam l : lasers)
            l.draw(g);
    }

    public List<LaserBeam> getLasers() {
        return lasers;
    }

    public boolean isFinished() {
        //System.out.println("SA TERMIANT FINISHED" + finished);
        return finished ;
    }

    public void reset() {
        lasers.clear();  // șterge toate razele existente
        laserCount = 1;
        lastFireTime = 0;  // resetează timpul ultimului foc
        fireCooldown = 3000;  // resetează cooldown-ul la valoarea inițială
        levelStartTime = System.currentTimeMillis();  // resetează timpul de start al nivelului
        finished = false;  // resetează starea de finalizare
    }

}