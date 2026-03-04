package PaooGame.Tiles;

import PaooGame.Graphics.Assets;

import java.awt.image.BufferedImage;

abstract class LightMetal extends Tile{
    public LightMetal(BufferedImage image, int idd) {
        super(image, idd);
        this.solid = true;
    }
}
class LightMetal1 extends LightMetal
{
    public LightMetal1(int id)
    {
        /// Apel al constructorului clasei de baza
        super(Assets.LightMetal1, id);
    }
}
class LightMetal2 extends LightMetal
{
    public LightMetal2(int id)
    {
        /// Apel al constructorului clasei de baza
        super(Assets.LightMetal2, id);
    }
}
class LightMetal3 extends LightMetal
{
    public LightMetal3(int id)
    {
        /// Apel al constructorului clasei de baza
        super(Assets.LightMetal3, id);
    }
}
class LightMetal4 extends LightMetal
{
    public LightMetal4(int id)
    {
        /// Apel al constructorului clasei de baza
        super(Assets.LightMetal4, id);
    }
}
class LightMetal5 extends LightMetal
{
    public LightMetal5(int id)
    {
        /// Apel al constructorului clasei de baza
        super(Assets.LightMetal5, id);
    }
}
class LightMetal6 extends LightMetal
{
    public LightMetal6(int id)
    {
        /// Apel al constructorului clasei de baza
        super(Assets.LightMetal6, id);
    }
}
class LightMetal7 extends LightMetal
{
    public LightMetal7(int id)
    {
        /// Apel al constructorului clasei de baza
        super(Assets.LightMetal7, id);
    }
}
class LightMetal8 extends LightMetal
{
    public LightMetal8(int id)
    {
        /// Apel al constructorului clasei de baza
        super(Assets.LightMetal8, id);
    }
}

