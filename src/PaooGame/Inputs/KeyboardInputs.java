package PaooGame.Inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import PaooGame.Entities.*;

import PaooGame.GameWindow.GameWindow;

///Clasa KeyboardInputs este responsabilă pentru gestionarea intrărilor de la tastatură în joc.
/// Aceasta implementează interfața KeyListener pentru a răspunde la evenimentele de tastatură, cum ar fi apăsarea și eliberarea tastelor.
public class KeyboardInputs implements KeyListener {
    private GameWindow gameWindow;
    private Player player;
    private boolean[] keys = new boolean[256];
    private PaooGame.Game game;

    public void setGame(PaooGame.Game game) {
        this.game = game;
    }

    public KeyboardInputs(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
    }

    public void setPlayer(Player player) { this.player = player; }

    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;

        int key = e.getKeyCode();
        keys[key] = true;

        if (game != null && game.getDeathMenu() != null && game.getDeathMenu().isVisible()) {
            switch (key) {
                case KeyEvent.VK_UP:
                    game.getDeathMenu().navigateUp();
                    break;
                case KeyEvent.VK_DOWN:
                    game.getDeathMenu().navigateDown();
                    break;
                case KeyEvent.VK_ENTER:
                    game.getDeathMenu().selectOption();
                    break;
            }
        }

        if (game != null && game.getWinMenu() != null && game.getWinMenu().isVisible()) {
            if (key == KeyEvent.VK_ENTER) { // Verificare pe KeyCode
                System.exit(0);
            }
        }



    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;

    }

    @Override
    public void keyTyped(KeyEvent e) {} // Neutilizată


    public boolean[] getKeys() {
        return keys.clone();
    }

    public void resetKey(int key) {
        if(key >=0 && key < keys.length)
            keys[key] = false;
    }


    public boolean isKeyPressed(int keyCode) {
        return keyCode >= 0 && keyCode < keys.length && keys[keyCode];
    }

    public void resetAllKeys() {
        Arrays.fill(keys, false);
    }
}