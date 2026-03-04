package PaooGame.Tiles;

import PaooGame.Graphics.Assets;

import java.awt.image.BufferedImage;

abstract class DarkMetal extends Tile{
    public DarkMetal(BufferedImage image, int idd) {
        super(image, idd);
        this.solid = true;
    }
}
class DarkMetal1 extends DarkMetal
{
    public DarkMetal1(int id)
    {
        /// Apel al constructorului clasei de baza
        super(Assets.DarkMetal1, id);
    }
}
class DarkMetal2 extends DarkMetal
{
    public DarkMetal2(int id)
    {
        /// Apel al constructorului clasei de baza
        super(Assets.DarkMetal2, id);
    }
}
class DarkMetal3 extends DarkMetal
{
    public DarkMetal3(int id)
    {
        /// Apel al constructorului clasei de baza
        super(Assets.DarkMetal3, id);
    }
}
class DarkMetal4 extends DarkMetal
{
    public DarkMetal4(int id)
    {
        /// Apel al constructorului clasei de baza
        super(Assets.DarkMetal4, id);
    }
}
class DarkMetal5 extends DarkMetal
{
    public DarkMetal5(int id)
    {
        /// Apel al constructorului clasei de baza
        super(Assets.DarkMetal5, id);
    }
}
class DarkMetal6 extends DarkMetal
{
    public DarkMetal6(int id)
    {
        /// Apel al constructorului clasei de baza
        super(Assets.DarkMetal6, id);
    }
}
class DarkMetal7 extends DarkMetal
{
    public DarkMetal7(int id)
    {
        /// Apel al constructorului clasei de baza
        super(Assets.DarkMetal7, id);
    }
}
class DarkMetal8 extends DarkMetal
{
    public DarkMetal8(int id)
    {
        /// Apel al constructorului clasei de baza
        super(Assets.DarkMetal8, id);
    }
}

