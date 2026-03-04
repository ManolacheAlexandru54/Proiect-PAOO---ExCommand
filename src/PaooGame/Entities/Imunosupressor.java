package PaooGame.Entities;

import PaooGame.Graphics.Assets;
import java.awt.*;
import java.awt.image.BufferedImage;

/// Clasa Imunosupressor reprezintă un obiect colectabil în joc care poate fi adunat de jucător.
/// Aceasta conține informații despre poziția sa, dimensiuni și starea de colectare.
/// Imunosupressorul este reprezentat grafic printr-o imagine specifică.
/// Această clasă oferă metode pentru actualizarea stării obiectului, desenarea sa pe ecran și gestionarea stării de colectare.
/// Imunosupresoarele pot fi colectate de jucător pentru a-i oferi beneficii, cum ar fi reducerea daunei primite.

public class Imunosupressor {
    private int x, y;
    private int width = 20;
    private int height = 54;
    private boolean collected = false;

    public Imunosupressor(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setCollected(){
        collected = true;
    }

    public boolean isCollected(){
        return collected;
    }

    public void update() {
        // poți adă uga animație sau efect de plutire
    }

    public void draw(Graphics g) {
        if(!collected){
            g.drawImage(Assets.imunosuppressor, x, y, width, height, null);
        }
    }

    public int getX() {return x;}
    public int getY() {return y;}
    public int getWidth() {return width;}
    public int getHeight() {return height;}
}
