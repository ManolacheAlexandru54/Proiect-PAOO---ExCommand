package PaooGame.Graphics;

import PaooGame.Entities.Player;
import java.awt.*;

///Clasa Camera este responsabilă pentru gestionarea poziției și transformării camerei în joc.
/// Aceasta permite centrul camerei să urmeze jucătorul și să limiteze vizibilitatea la dimensiunile hărții.
/// /// Camera poate fi utilizată pentru a începe și termina redarea graficii, asigurându-se că totul este desenat corect în raport cu poziția camerei.
public class Camera {
    private float x, y;
    private Player player;
    private int screenWidth, screenHeight;
    private int mapWidth, mapHeight;
    private Graphics2D originalGraphics;

    public Camera(Player player, int screenWidth, int screenHeight, int mapWidth, int mapHeight) {
        this.player = player;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
    }

    public void update() {
        // Centrăm camera pe jucător
        x = player.getX() - screenWidth/2 + player.getWidth()/2;
        y = player.getY() - screenHeight/2 + player.getHeight()/2;

        // Limităm camera la dimensiunile hărții
        x = Math.max(0, Math.min(x, mapWidth - screenWidth));
        y = Math.max(0, Math.min(y, mapHeight - screenHeight));
    }


    public void beginRender(Graphics g) {
        originalGraphics = (Graphics2D) ((Graphics2D) g).create();
        ((Graphics2D) g).translate(-x, -y);
    }

    public void endRender(Graphics g) {
        if (originalGraphics != null) {
            ((Graphics2D) g).setTransform(originalGraphics.getTransform());
            originalGraphics.dispose();
        }
    }

    public void resetTransform(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.translate(x, y);
    }

    public float getX() { return x; }
    public float getY() { return y; }
}