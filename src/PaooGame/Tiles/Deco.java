package PaooGame.Tiles;

import PaooGame.Graphics.Assets;

import java.awt.image.BufferedImage;

public class Deco extends Tile{
    public Deco(BufferedImage image, int idd) {
        super(image, idd);
        this.solid = false;
    }
}
class Deco1 extends Deco{

    public Deco1(int id) {
        super(Assets.Deco1, id);
    }
}

class Deco2 extends Deco {

    public Deco2(int id) {
        super(Assets.Deco2, id);
    }
}

class Deco3 extends Deco{

    public Deco3(int id) {
        super(Assets.Deco3, id);
    }
}
