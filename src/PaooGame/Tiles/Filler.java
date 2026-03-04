package PaooGame.Tiles;

import PaooGame.Graphics.Assets;

import java.awt.image.BufferedImage;

abstract public class Filler extends Tile{

    public Filler(BufferedImage image, int idd) {
        super(image, idd);
        this.solid = true;
    }
}

class Filler1 extends Filler{

    public Filler1(int id) {
        super(Assets.Filler1, id);
    }
}

class Filler2 extends Filler {

    public Filler2(int id) {
        super(Assets.Filler2, id);
    }
}

class Filler3 extends Filler{

    public Filler3(int id) {
        super(Assets.Filler3, id);
    }
}

class Filler4 extends Filler{

    public Filler4(int id) {
        super(Assets.Filler4, id);
    }
}
class Filler5 extends Filler{

    public Filler5(int id) {
        super(Assets.Filler5, id);
    }
}


