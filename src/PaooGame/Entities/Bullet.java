package PaooGame.Entities;

import java.awt.*;
import java.awt.image.BufferedImage;
import PaooGame.Graphics.Assets;

/// Clasa Bullet reprezintă un glonț în joc.
/// Aceasta este folosită pentru a trasa glonțele trase de jucător sau inamici.
/// Glonțul are o poziție, dimensiuni, viteză și o stare activă.
/// Glonțul se mișcă într-o direcție specificată (dreapta sau stânga) și poate fi desenat pe ecran.
/// De asemenea, glonțul are o imagine asociată pentru a fi afișată în timpul jocului.

public class Bullet {
    private int x, y;
    private int width = 10;   // Lățimea glonțului
    private int height = 5;   // Înălțimea glonțului
    private int speed = 10;   // Viteza glonțului
    private boolean isActive = true;
    private boolean facingRight;
    private BufferedImage bulletImage;
    private BufferedImage bulletTrailImage;

    // In Bullet.java

    private BufferedImage sprite;
    private BufferedImage trail;

    // Adaugă în constructor:
    public Bullet(int startX, int startY, boolean facingRight, BufferedImage sprite, BufferedImage trail) {
        this.x = startX;
        this.y = startY;
        this.facingRight = facingRight;
        this.sprite = sprite;
        this.trail = trail;
        this.width = 10;
        this.height = 5;
        this.speed = 8;
        this.isActive = true;
    }


    public void update() {
        if (facingRight) {
            x += speed;  // Mergi spre dreapta
        } else {
            x -= speed;  // Mergi spre stânga
        }

        // Aici adăugăm logica pentru a opri glonțul când lovește ceva
        if (x < 0 || x > 10000) {  // Limitează glonțul la lățimea totală a jocului
            isActive = false;
        }
    }

    public void draw(Graphics g) {
        if (!isActive) return;

        if (trail != null) {
            if (facingRight)
                g.drawImage(trail, x - 40, y - 3, width + 30, height, null);
            else
                g.drawImage(trail, x + 5, y - 3, width + 30, height, null);
        }

        if (sprite != null) {
            g.drawImage(sprite, x, y - 3, width, height, null);
        }
    }


    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getX() {return x;}
    public int getY() {return y;};
    public int getWidth() {return width;}
    public int getHeight() {return height;}

    public void setActive(boolean active) {this.isActive = active;}
    public boolean isActive() {return isActive;}

}
