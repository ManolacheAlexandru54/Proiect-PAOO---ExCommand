package PaooGame.GameStates;

import java.awt.*;
import PaooGame.GameWindow.GameWindow;

/// Clasa PauseMenu reprezintă meniul de pauză al jocului.
/// Aceasta permite jucătorului să reia jocul, să acceseze setările,
/// să salveze progresul sau să iasă înapoi la meniul principal.
/// Meniul este afișat pe ecran cu un fundal semi-transparent și text centrat.
/// Jucătorul poate naviga prin opțiuni folosind tastele sus și jos și poate selecta o opțiune cu tasta Enter.
/// Meniul de pauză este gestionat de către clasa Game, care îl poate afișa sau ascunde în funcție de starea jocului.

public class PauseMenu {
    private GameWindow window;
    private boolean isVisible;
    public int selectedOption;
    public String[] options = {"Resume", "Settings", "Save Game", "Exit to Menu"};
    private SettingsMenu settingsMenu;
    private MainMenu mainMenu;
    private boolean fromMainMenu = false;
    private int playerX;
    private int playerY;

    public PauseMenu(GameWindow window) {
        this.window = window;
        this.isVisible = false;
        this.selectedOption = 0;
    }

    public void setSettingsMenu(SettingsMenu settingsMenu) {
        this.settingsMenu = settingsMenu;
    }
    public void setMainMenu(MainMenu mainMenu) { this.mainMenu = mainMenu; }

    public boolean isFromMainMenu() {
        return fromMainMenu;
    }

    public void toggle() {
        isVisible = !isVisible;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // Dimensiuni fereastră
        int width = window.GetWndWidth();
        int height = window.GetWndHeight();

        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, window.GetWndWidth(), window.GetWndHeight());

        // ---------- Stiluri ----------
        Font titleFont = new Font("Arial", Font.BOLD, 60);
        Font optionFont = new Font("Arial", Font.PLAIN, 40);

        // ---------- Titlu ----------
        String title = "PAUSE";
        g.setFont(titleFont);
        g.setColor(Color.WHITE);
        int titleWidth = g.getFontMetrics().stringWidth(title);
        int titleX = (width - titleWidth) / 2;
        int titleY = height / 2 - 300; // sus deasupra opțiunilor

        FontMetrics fm = g.getFontMetrics();

        g.drawString(title, titleX, titleY);

        // ---------- Opțiuni ----------
        g.setFont(optionFont);
        int optionHeight = 40;
        int spacing = 20;

        int totalOptionsHeight = options.length * optionHeight + (options.length - 1) * spacing;
        int startY = titleY + 100; // puțin mai jos de titlu

        for (int i = 0; i < options.length; i++) {
            String text = options[i];
            int textWidth = g.getFontMetrics().stringWidth(text);
            int x = (width - textWidth) / 2;
            int y = startY + i * (optionHeight + spacing);

            if (i == selectedOption) {
                int padding = 10;
                int rectX = x - padding;
                int rectY = y - fm.getAscent() + 1;
                int rectWidth = textWidth + 2 * padding;
                int rectHeight = fm.getHeight();

                g2d.setColor(new Color(255, 255, 0, 80)); // galben semi-transparent
                g2d.fillRoundRect(rectX, rectY, rectWidth, rectHeight, 15, 15);
            } else {
                g.setColor(Color.WHITE);
            }

            g2d.setColor(Color.BLACK);
            g2d.drawString(text, x + 2, y + 2); // umbră ușoară

            // ---------- Textul principal ----------
            g2d.setColor(Color.WHITE);
            g2d.drawString(text, x, y);
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
            case 0: // Resume
                isVisible = false;
                // Adaugă această linie:
                // game.setCurrentState(GameState.PLAYING); // sau cum ai acces la instanța Game
                break;
            case 1: // Settings
                isVisible = false;
                fromMainMenu = false;
                if (settingsMenu != null) {
                    settingsMenu.setFromMainMenu(false);
                    settingsMenu.toggle();
                }
                break;
            case 2: // Save Game
                System.out.println("Game saved!");
                break;
            case 3: // Exit to Menu
                //fromMainMenu = true;
                if (mainMenu != null) {
                    mainMenu.setVisible(true);  // safer with method
                }
                isVisible = false;
                // You'll need to handle returning to main menu in your Game class
                break;
        }
    }

    private void drawCenteredString(Graphics g, String text, int x, int width, int y) {
        FontMetrics fm = g.getFontMetrics();
        int textX = x + (width - fm.stringWidth(text)) / 2;
        g.drawString(text, textX, y);
    }

    public String[] getOptions() {
        return options;
    }

    public void setSelectedOption(int option) {
        if (option >= 0 && option < options.length) {
            this.selectedOption = option;
        }
    }

    public void setVisible(boolean b) {
        this.isVisible = b;
    }
}