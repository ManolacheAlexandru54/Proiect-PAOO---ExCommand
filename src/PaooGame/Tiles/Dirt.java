package PaooGame.Tiles;

import PaooGame.Graphics.Assets;

import java.awt.image.BufferedImage;

abstract class Dirt extends Tile{
    public Dirt(BufferedImage image, int idd) {
        super(image, idd);
        this.solid = true;
    }
}
class Dirt1 extends Dirt
{
    public Dirt1(int id)
    {
        /// Apel al constructorului clasei de baza
        super(Assets.Dirt1, id);
    }

}
class Dirt2 extends Dirt
{
    public Dirt2(int id)
    {
        /// Apel al constructorului clasei de baza
        super(Assets.Dirt2, id);
    }
}

class Dirt3 extends Dirt
{
    public Dirt3(int id)
    {
        /// Apel al constructorului clasei de baza
        super(Assets.Dirt3, id);
    }
}

class Dirt4 extends Dirt
{
    public Dirt4(int id)
    {
        /// Apel al constructorului clasei de baza
        super(Assets.Dirt4, id);
    }
}
class Dirt5 extends Dirt
{
    public Dirt5(int id)
    {
        /// Apel al constructorului clasei de baza
        super(Assets.Dirt5, id);
    }
}
class Dirt6 extends Dirt
{
    public Dirt6(int id)
    {
        /// Apel al constructorului clasei de baza
        super(Assets.Dirt6, id);
    }
}
class Dirt7 extends Dirt
{
    public Dirt7(int id)
    {
        /// Apel al constructorului clasei de baza
        super(Assets.Dirt7, id);
    }
}
class Dirt8 extends Dirt
{
    public Dirt8(int id)
    {
        /// Apel al constructorului clasei de baza
        super(Assets.Dirt8, id);
    }
}
