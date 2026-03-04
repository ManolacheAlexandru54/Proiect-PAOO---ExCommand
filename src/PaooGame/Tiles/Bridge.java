package PaooGame.Tiles;

import PaooGame.Graphics.Assets;

import java.awt.image.BufferedImage;

public class Bridge extends Tile{
    public Bridge(BufferedImage image, int idd) {
        super(image, idd);
        this.solid = true;
    }
}
class Bridge1 extends Bridge{

    public Bridge1(int id) {
        super(Assets.Bridge1, id);
    }
}

class Bridge2 extends Bridge {

    public Bridge2(int id) {
        super(Assets.Bridge2, id);
    }
}

class Bridge3 extends Bridge{

    public Bridge3(int id) {
        super(Assets.Bridge3, id);
    }
}
class Ladder1 extends Bridge{

    public Ladder1(int id) {

        super(Assets.Ladder1, id);
        this.solid = false;
    }

    @Override
    public boolean isLadder() {
        return true; // Tile-ul este considerat scară
    }

}



