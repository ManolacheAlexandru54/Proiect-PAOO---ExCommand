package PaooGame.Entities;

import PaooGame.Graphics.Assets;
import java.awt.*;

/// Clasa KeyCard reprezintă un obiect de tip card de acces în joc
/// care poate fi colectat de jucător.
/// Aceasta conține informații despre poziția sa, dimensiuni și starea de colectare.
/// KeyCard-ul este reprezentat grafic printr-o imagine specifică.
/// Această clasă oferă metode pentru actualizarea stării obiectului,
/// desenarea sa pe ecran și gestionarea stării de colectare.
/// KeyCard-urile pot fi colectate de jucător pentru a debloca noi niveluri in joc
/// și pentru a progresa în povestea jocului.

public class KeyCard {
    private int x, y;
    private int width = 43;
    private int height = 31;
    private boolean collected = false;

    public KeyCard(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setX(int x) {this.x = x;}
    public void setY(int y) {this.y = y;}

    public void update() {
        // poți adăuga animație sau efect de plutire
    }

    public void draw(Graphics g) {
        g.drawImage(Assets.keycard, x, y, width, height, null);
    }

    public void setCollected(boolean b){
        collected = b;
    }
    public boolean isCollected(){
        return collected;
    }

    public int getX() {return x;}
    public int getY() {return y;}
    public int getWidth() {return width;}
    public int getHeight() {return height;}
}
