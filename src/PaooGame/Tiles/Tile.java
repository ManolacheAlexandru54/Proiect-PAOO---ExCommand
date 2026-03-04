package PaooGame.Tiles;

import java.awt.*;
import java.awt.image.BufferedImage;

///Clasa abstractă Tile reprezintă o dale de tipul Tile din joc.
/// Aceasta este o clasă de bază pentru toate tipurile de dale care pot fi utilizate în joc.
/// Tile-urile pot fi de diferite tipuri, cum ar fi dale de pământ, metal, toxine etc.
/// Tile-urile sunt utilizate pentru a construi niveluri și medii de joc.
/// Tile-urile pot fi solide sau nu, ceea ce afectează interacțiunea jucătorului cu acestea.
/// Tile-urile sunt reprezentate grafic prin imagini și au un ID unic pentru identificare.
/// Tile-urile pot fi desenate pe ecran și actualizate în funcție de starea jocului.
public abstract class Tile
{
    protected boolean solid;
    private static final int NO_TILES   = 150;
    public static Tile[] tiles          = new Tile[NO_TILES];       /*!< Vector de referinte de tipuri de dale.*/
    /// De remarcat ca urmatoarele dale sunt statice si publice. Acest lucru imi permite sa le am incarcate
    /// o singura data in memorie
    /// Pentru Tile-urile de tip Dirt
    public static Tile Dirt1Tile = new Dirt1(1);
    public static Tile Dirt2Tile = new Dirt2(2);
    public static Tile Dirt3Tile = new Dirt3(3);
    public static Tile Dirt4Tile = new Dirt4(4);
    public static Tile Dirt5Tile = new Dirt5(5);
    public static Tile Dirt6Tile = new Dirt6(6);
    public static Tile Dirt7Tile = new Dirt7(7);
    public static Tile Dirt8Tile = new Dirt8(8);

    /// Pentru Tile-urile de tip Toxin
    public static Tile ToxinSourceTile = new ToxinSource(9);
    public static Tile Toxin1Tile = new Toxin1(10);
    public static Tile Toxin2Tile = new Toxin2(11);
    public static Tile Toxin3Tile = new Toxin3(12);

    /// Pentru Tile-urile de tip LightMetal
    public static Tile LightMetal1Tile = new LightMetal1(26);
    public static Tile LightMetal2Tile = new LightMetal2(27);
    public static Tile LightMetal3Tile = new LightMetal3(28);
    public static Tile LightMetal4Tile = new LightMetal4(29);
    public static Tile LightMetal5Tile = new LightMetal5(30);
    public static Tile LightMetal6Tile = new LightMetal6(31);
    public static Tile LightMetal7Tile = new LightMetal7(32);
    public static Tile LightMetal8Tile = new LightMetal8(33);

    /// Pentru Tile-urile de tip Lava
    public static Tile LavaSourceTile = new LavaSource(34);
    public static Tile Lava1Tile = new Lava1(35);
    public static Tile Lava2Tile = new Lava2(36);
    public static Tile Lava3Tile = new Lava3(37);

    /// Pentru Tile-urile de tip DarkMetal
    public static Tile DarkMetal1Tile = new DarkMetal1(51);
    public static Tile DarkMetal2Tile = new DarkMetal2(52);
    public static Tile DarkMetal3Tile = new DarkMetal3(53);
    public static Tile DarkMetal4Tile = new DarkMetal4(54);
    public static Tile DarkMetal5Tile = new DarkMetal5(55);
    public static Tile DarkMetal6Tile = new DarkMetal6(56);
    public static Tile DarkMetal7Tile = new DarkMetal7(57);
    public static Tile DarkMetal8Tile = new DarkMetal8(58);

    /// Pentru Tile-uril de tip Ladder
    public static Tile Ladder1Tile = new Ladder1(59);

    /// Pentru Tile-urile de tip Grass
    public static Tile Grass1Tile = new Grass1(76);
    public static Tile Grass2Tile = new Grass2(77);
    public static Tile Grass3Tile = new Grass3(78);
    public static Tile Grass4Tile = new Grass4(79);
    public static Tile Grass5Tile = new Grass5(80);
    public static Tile Grass6Tile = new Grass6(81);
    public static Tile Grass7Tile = new Grass7(82);
    public static Tile Grass8Tile = new Grass8(83);

    /// Pentru Tile-urile de tip Decoratiuni
    public static Tile Deco1Tile = new Deco1(84);
    public static Tile Deco2Tile = new Deco2(85);
    public static Tile Deco3Tile = new Deco3(86);


    /// Pentru Tile-urile de tip Filler
    public static Tile Filler1Tile = new Filler1(101);
    public static Tile Filler2Tile = new Filler2(102);
    public static Tile Filler3Tile = new Filler3(103);
    public static Tile Filler4Tile = new Filler4(104);
    public static Tile Filler5Tile = new Filler5(105);

    /// Pentru Tile-uril de tip Bridge
    public static Tile Bridge1Tile = new Bridge1(106);
    public static Tile Bridge2Tile = new Bridge2(107);
    public static Tile Bridge3Tile = new Bridge3(108);



    public static final int TILE_WIDTH  = 32;                       /*!< Latimea unei dale.*/
    public static final int TILE_HEIGHT = 32;                       /*!< Inaltimea unei dale.*/

    protected BufferedImage img;                                    /*!< Imaginea aferenta tipului de dala.*/
    protected final int id;                                         /*!< Id-ul unic aferent tipului de dala.*/

    /*! \fn public Tile(BufferedImage texture, int id)
        \brief Constructorul aferent clasei.

        \param image Imaginea corespunzatoare dalei.
        \param id Id-ul dalei.
     */
    public Tile(BufferedImage image, int idd)
    {
        img = image;
        id = idd;
        tiles[id] = this;
        solid = false;
    }
    public boolean isSolid(){
        return solid;
    }
    public boolean isLadder() { return false; }

    /*! \fn public void Update()
        \brief Actualizeaza proprietatile dalei.
     */
    public void Update()
    {
    }
    /*! \fn public void Draw(Graphics g, int x, int y)
        \brief Deseneaza in fereastra dala.

        \param g Contextul grafic in care sa se realizeze desenarea
        \param x Coordonata x in cadrul ferestrei unde sa fie desenata dala
        \param y Coordonata y in cadrul ferestrei unde sa fie desenata dala
     */
    public void Draw(Graphics g, int x, int y)
    {
        /// Desenare dala
        g.drawImage(img, x, y, TILE_WIDTH, TILE_HEIGHT, null);
    }
    public static void InitTiles() {
        // Dirt tiles
        Dirt1Tile = new Dirt1(1);
        Dirt2Tile = new Dirt2(2);
        Dirt3Tile = new Dirt3(3);
        Dirt4Tile = new Dirt4(4);
        Dirt5Tile = new Dirt5(5);
        Dirt6Tile = new Dirt6(6);
        Dirt7Tile = new Dirt7(7);
        Dirt8Tile = new Dirt8(8);

        // Toxin tiles
        ToxinSourceTile = new ToxinSource(9);
        Toxin1Tile = new Toxin1(10);
        Toxin2Tile = new Toxin2(11);
        Toxin3Tile = new Toxin3(12);

        // LightMetal tiles
        LightMetal1Tile = new LightMetal1(26);
        LightMetal2Tile = new LightMetal2(27);
        LightMetal3Tile = new LightMetal3(28);
        LightMetal4Tile = new LightMetal4(29);
        LightMetal5Tile = new LightMetal5(30);
        LightMetal6Tile = new LightMetal6(31);
        LightMetal7Tile = new LightMetal7(32);
        LightMetal8Tile = new LightMetal8(33);

        // Lava tiles
        LavaSourceTile = new LavaSource(34);
        Lava1Tile = new Lava1(35);
        Lava2Tile = new Lava2(36);
        Lava3Tile = new Lava3(37);

        // DarkMetal tiles
        DarkMetal1Tile = new DarkMetal1(51);
        DarkMetal2Tile = new DarkMetal2(52);
        DarkMetal3Tile = new DarkMetal3(53);
        DarkMetal4Tile = new DarkMetal4(54);
        DarkMetal5Tile = new DarkMetal5(55);
        DarkMetal6Tile = new DarkMetal6(56);
        DarkMetal7Tile = new DarkMetal7(57);
        DarkMetal8Tile = new DarkMetal8(58);

        // Ladder tiles
        Ladder1Tile = new Ladder1(59);
        // Grass tiles
        Grass1Tile = new Grass1(76);
        Grass2Tile = new Grass2(77);
        Grass3Tile = new Grass3(78);
        Grass4Tile = new Grass4(79);
        Grass5Tile = new Grass5(80);
        Grass6Tile = new Grass6(81);
        Grass7Tile = new Grass7(82);
        Grass8Tile = new Grass8(83);

        // Decoration tiles
        Deco1Tile = new Deco1(84);
        Deco2Tile = new Deco2(85);
        Deco3Tile = new Deco3(86);

        // Filler tiles
        Filler1Tile = new Filler1(101);
        Filler2Tile = new Filler2(102);
        Filler3Tile = new Filler3(103);
        Filler4Tile = new Filler4(104);
        Filler5Tile = new Filler5(105);

        // Bridge tiles
        Bridge1Tile = new Bridge1(106);
        Bridge2Tile = new Bridge2(107);
        Bridge3Tile = new Bridge3(108);
    }

    public int GetId()
    {
        return id;
    }
}