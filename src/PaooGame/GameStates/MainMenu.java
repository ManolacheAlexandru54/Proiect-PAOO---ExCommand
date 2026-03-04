package PaooGame.GameStates;

import java.awt.*;
import java.util.ArrayList;

import PaooGame.DatabaseManager;
import PaooGame.Entities.Enemy;
import PaooGame.Game;
import PaooGame.GameWindow.GameWindow;

/// Clasa MainMenu reprezintă meniul principal al jocului.
/// Aceasta conține opțiuni pentru a începe un joc nou, a salva sau încărca un joc existent,
/// a accesa setările și a ieși din joc.
/// Meniul este afișat pe ecran cu un fundal semi-transparent și text centrat.
/// Jucătorul poate naviga prin opțiuni folosind tastele sus și jos și poate selecta o opțiune cu tasta Enter.
/// Meniul este gestionat de către clasa Game, care îl poate afișa sau ascunde în funcție de starea jocului.
/// De asemenea, meniul permite salvarea și încărcarea stării jocului dintr-o bază de date,
/// permițând jucătorului să continue de unde a rămas anterior.

public class MainMenu {
    private GameWindow window;
    public boolean isVisible;
    public int selectedOption;
    public String[] options = {"New Game", "Save Game", "Load Game", "Settings", "Exit"};
    private SettingsMenu settingsMenu;
    private boolean gameStarted = false;
    protected boolean fromMainMenu = false;
    private Game game;  // Add this field


    public MainMenu(GameWindow window, Game game) {
        this.window = window;
        this.game = game;
        this.isVisible = true;
        this.selectedOption = 0;
    }

    public void setSettingsMenu(SettingsMenu settingsMenu) {
        this.settingsMenu = settingsMenu;
    }

    public void toggle() {
        isVisible = !isVisible;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public void draw(Graphics g) {
        if (!isVisible) return;

        // Background
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, window.GetWndWidth(), window.GetWndHeight());

        // Title
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 60));
        drawCenteredString(g, "exCommando", window.GetWndWidth(), 150);

        // Options
        g.setFont(new Font("Arial", Font.PLAIN, 40));
        for (int i = 0; i < options.length; i++) {
            if (i == selectedOption) {
                g.setColor(Color.YELLOW);
            } else {
                g.setColor(Color.WHITE);
            }
            drawCenteredString(g, options[i], window.GetWndWidth(), 300 + i * 60);
        }
    }

    public void navigateUp() {
        selectedOption = (selectedOption - 1 + options.length) % options.length;
    }

    public void navigateDown() {
        selectedOption = (selectedOption + 1) % options.length;
    }

    public void selectOption() {
        switch (selectedOption) {
            case 0: // New Game
                gameStarted = true;
                isVisible = false;
                break;
            case 1: // Save Game
                System.out.println("Saving game...");
                GameState saveState = new GameState(
                    "Player1",
                    Game.score,
                    (int) ((System.currentTimeMillis() - Game.startTime) / 1000),
                    "LEVEL" + game.getCurrentMap(),
                    Game.getHealth(),
                    true,
                    Game.player.getX(),
                    Game.player.getY(),
                    game.getCurrentMap(),
                    Game.getEnemies1(), // Lista de inamici pentru map1
                    Game.getEnemies3()  // Lista de inamici pentru map3
                );
                DatabaseManager.saveGame(saveState); // Salvăm starea jocului în baza de date
                break;

            case 2: // Load Game
                System.out.println("Loading game...");
                GameState loadState = DatabaseManager.loadLastGame(); // Încărcăm starea jocului din baza de date
                if (loadState != null) {
                    // Restaurăm lista de inamici încărcată
                    Game.setEnemies1(loadState.getEnemy1());
                    Game.setEnemies3(loadState.getEnemy3());
                    
                    // Restaurăm și alte stări necesare
                    Game.score = loadState.getScore();
                    Game.player.setPosition(loadState.getPlayerX(), loadState.getPlayerY());
                    Game.player.setHp(loadState.getHealth());
                    game.setCurrentMap(loadState.getCurrentMap());

                    // Actualizăm timpul rămas
                    Game.startTime = System.currentTimeMillis() - (loadState.getTimeSpent() * 1000L);
                    gameStarted = true;
                    isVisible = false;
                }
                break;




            case 3: // Settings
                fromMainMenu = true;
                isVisible = false;
                if (settingsMenu != null) {
                    settingsMenu.setFromMainMenu(true);
                    settingsMenu.toggle();
                }
                break;
            case 4: // Exit
                System.exit(0);
                break;
        }
    }

    private void drawCenteredString(Graphics g, String text, int width, int y) {
        FontMetrics fm = g.getFontMetrics();
        int x = (width - fm.stringWidth(text)) / 2;
        g.drawString(text, x, y);
    }

    public void setVisible(boolean b) {
        isVisible = b;
    }

    public String[] getOptions() {
        return options;
    }

    public void setSelectedOption(int option) {
        if (option >= 0 && option < options.length) {
            this.selectedOption = option;
        }
    }
}