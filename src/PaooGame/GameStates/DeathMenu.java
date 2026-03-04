package PaooGame.GameStates;

import java.awt.*;

import PaooGame.Game;
import PaooGame.GameWindow.GameWindow;

/// Clasa DeathMenu reprezintă meniul care apare atunci când jucătorul moare în joc.
/// Aceasta conține opțiuni pentru a încerca din nou nivelul curent.
/// Meniul este afișat pe ecran cu un fundal semi-transparent și text centrat.
/// Jucătorul poate naviga prin opțiuni folosind tastele sus și jos și poate selecta o opțiune cu tasta Enter.
/// Meniul este gestionat de către clasa Game, care îl poate afișa sau ascunde în funcție de starea jocului.

public class DeathMenu {
    private GameWindow window;
    private Game game;
    private boolean isVisible;
    public int selectedOption;
    public final String[] options = {"Try Again"};

    public DeathMenu(GameWindow window, Game game) {
        this.window = window;
        this.game = game;
        this.isVisible = false;
        this.selectedOption = 0;
    }

    public void draw(Graphics g) {
        if (!isVisible) return;

        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, window.GetWndWidth(), window.GetWndHeight());

        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 60));
        drawCenteredString(g, "You Died!", window.GetWndWidth(), 200);

        g.setFont(new Font("Arial", Font.PLAIN, 40));
        for (int i = 0; i < options.length; i++) {
            g.setColor(i == selectedOption ? Color.YELLOW : Color.WHITE);
            drawCenteredString(g, options[i], window.GetWndWidth(), 300 + i * 60);
        }
    }

    private void drawCenteredString(Graphics g, String text, int width, int y) {
        FontMetrics fm = g.getFontMetrics();
        int x = (width - fm.stringWidth(text)) / 2;
        g.drawString(text, x, y);
    }

    public void navigateUp() {
        selectedOption = (selectedOption - 1 + options.length) % options.length;
    }

    public void navigateDown() {
        selectedOption = (selectedOption + 1) % options.length;
    }

    public void selectOption() {
        switch (selectedOption) {
            case 0: // Try Again
                game.restartCurrentLevel();
                isVisible = false;
                break;
        }
    }

    public void setVisible(boolean visible) {
        this.isVisible = visible;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public String[] getOptions() {
        return options;
    }
}
