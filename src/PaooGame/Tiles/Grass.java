package PaooGame.Tiles;

import PaooGame.Graphics.Assets;

import java.awt.image.BufferedImage;

abstract public class Grass extends Tile {
    public Grass(BufferedImage image, int idd) {
        super(image, idd);
        this.solid = true;
    }
}

class Grass1 extends Grass
{
    public Grass1(int id)
    {
        /// Apel al constructorului clasei de baza
        super(Assets.Grass1, id);
    }

}
class Grass2 extends Grass
{
    public Grass2(int id)
    {
        /// Apel al constructorului clasei de baza
        super(Assets.Grass2, id);
    }
}

class Grass3 extends Grass
{
    public Grass3(int id)
    {
        /// Apel al constructorului clasei de baza
        super(Assets.Grass3, id);
    }
}

class Grass4 extends Grass
{
    public Grass4(int id)
    {
        /// Apel al constructorului clasei de baza
        super(Assets.Grass4, id);
    }
}
class Grass5 extends Grass
{
    public Grass5(int id)
    {
        /// Apel al constructorului clasei de baza
        super(Assets.Grass5, id);
    }
}
class Grass6 extends Grass
{
    public Grass6(int id)
    {
        /// Apel al constructorului clasei de baza
        super(Assets.Grass6, id);
    }
}
class Grass7 extends Grass
{
    public Grass7(int id)
    {
        /// Apel al constructorului clasei de baza
        super(Assets.Grass7, id);
    }
}
class Grass8 extends Grass
{
    public Grass8(int id)
    {
        /// Apel al constructorului clasei de baza
        super(Assets.Grass8, id);
    }
}
