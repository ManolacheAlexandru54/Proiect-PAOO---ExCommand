package PaooGame.Graphics;

import java.awt.image.BufferedImage;
///Clasa SpriteSheet este responsabilă pentru gestionarea unui sprite sheet, care este o imagine ce conține mai multe dale (sprite-uri) utilizate în joc.
/// Aceasta permite decuparea și extragerea unor subimagine (dale) din sprite sheet, facilitând astfel utilizarea eficientă a resurselor grafice.
public class SpriteSheet
{
    private BufferedImage       spriteSheet;              /*!< Referinta catre obiectul BufferedImage ce contine sprite sheet-ul.*/
    private static final int    tileWidth   = 32;   /*!< Latimea unei dale din sprite sheet.*/
    private static final int    tileHeight  = 32;   /*!< Inaltime unei dale din sprite sheet.*/

    public SpriteSheet(BufferedImage buffImg)
    {
        /// Retine referinta catre BufferedImage object.
        spriteSheet = buffImg;
    }
    public BufferedImage crop(int x, int y)
    {
        /// Subimaginea (dala) este regasita in sprite sheet specificad coltul stanga sus
        /// al imaginii si apoi latimea si inaltimea (totul in pixeli). Coltul din stanga sus al imaginii
        /// se obtine inmultind numarul de ordine al dalei cu dimensiunea in pixeli a unei dale.
        return spriteSheet.getSubimage(x * tileWidth, y * tileHeight, tileWidth, tileHeight);
    }
}
