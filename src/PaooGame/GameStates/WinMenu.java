package PaooGame.GameStates;

import java.awt.*;

import PaooGame.Game;
import PaooGame.GameWindow.GameWindow;

/// Clasa WinMenu reprezintă meniul care apare atunci când jucătorul câștigă jocul.
/// Aceasta conține opțiuni pentru a reveni la meniul principal.
/// Meniul este afișat pe ecran cu un fundal semi-transparent și text centrat.
/// Jucătorul poate naviga prin opțiuni folosind tastele sus și jos și poate selecta o opțiune cu tasta Enter.
/// Meniul este gestionat de către clasa Game, care îl poate afișa sau ascunde în funcție de starea jocului.
/// WinMenu-ul afișează scorul obținut și timpul total de finalizare a jocului.


public class WinMenu {
    private GameWindow window;
    private Game game;
    private boolean isVisible;
    private int selectedOption;
    private final String[] options = {"Exit"};

    public WinMenu(GameWindow window, Game game) {
        this.window = window;
        this.game = game;
        this.isVisible = false;
        this.selectedOption = 0;
    }

    public void draw(Graphics g) {
        if (!isVisible) return;

        // Fundal semi-transparent
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, window.GetWndWidth(), window.GetWndHeight());

        // Titlul "You Win!"
        g.setColor(Color.GREEN);
        g.setFont(new Font("Arial", Font.BOLD, 60));
        drawCenteredString(g, "You Win!", window.GetWndWidth(), 200);

        // Afișarea scorului
        g.setFont(new Font("Arial", Font.PLAIN, 40));
        g.setColor(Color.WHITE);
        drawCenteredString(g, "Score: " + game.getScore(), window.GetWndWidth(), 300);

        // Calcularea și afișarea timpului de finalizare
        // daca nu e cu - se buleste toata baza de date
        long elapsedTime = game.getElapsedTime(); // Milisecunde
        long seconds = -(elapsedTime / 1000) % 60;
        long minutes = -(elapsedTime / 1000) / 60;

        drawCenteredString(g, String.format("Time: %02d:%02d", minutes, seconds), window.GetWndWidth(), 360);

        // Opțiunile din meniu
        for (int i = 0; i < options.length; i++) {
            g.setColor(i == selectedOption ? Color.YELLOW : Color.WHITE);
            drawCenteredString(g, options[i], window.GetWndWidth(), 450 + i * 60);
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
            case 0: // Main Menu
                System.out.println("Switching to MainMenu..."); // Debugging
                game.getMainMenu().setVisible(true); // Afișăm meniul principal
                game.setGameActive(false); // Dezactivăm starea jocului
                this.isVisible = false; // Ascundem WinMenu
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