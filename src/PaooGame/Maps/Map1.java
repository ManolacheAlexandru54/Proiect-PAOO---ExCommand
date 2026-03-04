package PaooGame.Maps;

import PaooGame.GameWindow.GameWindow;
import PaooGame.Graphics.Assets;
import PaooGame.Tiles.Tile;
import PaooGame.Tiles.XmlToMatrix;
import PaooGame.Items.CollectibleItem;
import java.util.ArrayList;


import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.File;
/// Clasa Map1 reprezintă o hartă specifică a jocului, care conține un aranjament de tile-uri.
/// Această hartă este definită printr-un fișier XML care specifică layout-ul tile-urilor.
/// fiecare mapa o sa aiba cate o clasa
/// constructorul claselor se va apela in caz de trecere la urmatorul nivel in metoda update() din Game

public class Map1 {
    private GameWindow     wnd;        /*!< Fereastra in care se va desena tabla jocului*/
    private BufferStrategy bs;         /*!< Referinta catre un mecanism cu care se organizeaza memoria complexa pentru un canvas.*/
    private Graphics g;/*!< Referinta catre un context grafic.*/
    private Tile[][] tileMap;
    private final int height = 40;
    private final int width = 200;
    File xmlFile = new File("res/textures/Map/MapLayout/Map1.xml");


    int[][] idMatrix = XmlToMatrix.GetIdMatrix(xmlFile,height, width);
    public Map1(GameWindow wnd){
        this.wnd = wnd;

        tileMap = new Tile[idMatrix.length][idMatrix[0].length];

        for (int i = 0; i < idMatrix.length; i++) {
            for (int j = 0; j < idMatrix[0].length; j++) {
                int id = idMatrix[i][j];
                if (id >= 0 && Tile.tiles[id] != null) {
                    tileMap[i][j] = Tile.tiles[id];
                } else {
                    tileMap[i][j] = null;
                }
            }
        }
        for (int i = 0; i < idMatrix.length; i++) {
            for (int j = 0; j < idMatrix[0].length; j++) {
                System.out.printf(idMatrix[i][j] + " ");
            }
            System.out.printf("\n");
        }

    }

    public Tile[][] getTileMap(){
        return tileMap;
    }

    public void Draw(Graphics g) {
        for (int y = 0; y < tileMap.length; y++) {
            for (int x = 0; x < tileMap[0].length; x++) {
                if (tileMap[y][x] != null) {
                    tileMap[y][x].Draw(g, x * Tile.TILE_WIDTH, y * Tile.TILE_HEIGHT);
                }
            }
        }
    }

    public int getWidth() {
        return tileMap[0].length * Tile.TILE_WIDTH; // sau dimensiunea reală a hărții
    }

    public int getHeight() {
        return tileMap.length * Tile.TILE_HEIGHT; // sau dimensiunea reală a hărții
    }

    public Tile getTile(int x, int y) {
        // Verifică dacă coordonatele sunt în limitele hărții
        if (x >= 0 && x < tileMap[0].length && y >= 0 && y < tileMap.length) {
            return tileMap[y][x];
        }
        // Returnează un tile default (de exemplu un tile gol) dacă e în afara limitelor
        return Tile.Dirt1Tile; // sau alt tile default
    }

    public int getWidthInTiles() {
        return tileMap[0].length;
    }

    public int getHeightInTiles() {
        return tileMap.length;
    }
}
