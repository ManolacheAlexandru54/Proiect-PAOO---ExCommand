package PaooGame.Items;

import PaooGame.Graphics.Assets;

import java.awt.*;

///Clasa CollectibleItem reprezintă un obiect colectabil în joc, cum ar fi un imunosupresor.
public class CollectibleItem {
    private int x, y;
    private boolean collected;

    public CollectibleItem(int x, int y) {
        this.x = x;
        this.y = y;
        this.collected = false;
    }

    public void draw(Graphics g) {
        if (!collected) {
            g.drawImage(Assets.imunosupressor, x, y, null);
        }
    }
    public int getX() { return x; }
    public int getY() { return y; }
}
