package PaooGame;

import PaooGame.GameWindow.GameWindow;

public class Main
{
    public static void main(String[] args)
    {
        Game paooGame = new Game("PaooGame", 1920, 1070);
        paooGame.StartGame();
    }
}
