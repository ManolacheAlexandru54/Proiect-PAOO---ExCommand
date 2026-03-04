package PaooGame.Tiles;

import PaooGame.Graphics.Assets;

import java.awt.image.BufferedImage;

abstract public class Hazard extends Tile{
    public Hazard(BufferedImage image, int idd) {
        super(image, idd);
        this.solid = false;
    }
}
class ToxinSource extends Hazard
{
    public ToxinSource(int id)
    {
        super(Assets.ToxinSource,id);
    }
}
class Toxin1 extends Hazard
{
    public Toxin1(int id)
    {
        super(Assets.Toxin1,id);
    }
}
class Toxin2 extends Hazard
{
    public Toxin2(int id)
    {
        super(Assets.Toxin2,id);
    }
}
class Toxin3 extends Hazard
{
    public Toxin3(int id)
    {
        super(Assets.Toxin3,id);
    }
}
class LavaSource extends Hazard
{
    public LavaSource(int id)
    {
        super(Assets.LavaSource,id);
    }
}
class Lava1 extends Hazard
{
    public Lava1(int id)
    {
        super(Assets.Lava1,id);
    }
}
class Lava2 extends Hazard
{
    public Lava2(int id)
    {
        super(Assets.Lava2,id);
    }
}
class Lava3 extends Hazard
{
    public Lava3(int id)
    {
        super(Assets.Lava3,id);
    }
}