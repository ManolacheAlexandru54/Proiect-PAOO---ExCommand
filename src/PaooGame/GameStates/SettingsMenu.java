package PaooGame.GameStates;

import java.awt.*;
import PaooGame.GameWindow.GameWindow;
import PaooGame.Main;
import PaooGame.Audio.AudioPlayer;

/// Clasa SettingsMenu reprezintă meniul de setări al jocului.
/// Aceasta permite jucătorului să activeze sau să dezactiveze sunetul și muzica,
/// să ajusteze volumul muzicii și să revină la meniul principal sau la meniul de pauză.
/// Meniul este afișat pe ecran cu un fundal semi-transparent și text centrat.
/// Jucătorul poate naviga prin opțiuni folosind tastele sus și jos și poate selecta o opțiune cu tasta Enter.
/// Setările sunt gestionate prin intermediul clasei AudioPlayer, care controlează redarea sunetelor și muzicii.

public class SettingsMenu {
    private GameWindow window;
    private boolean isVisible;
    public int selectedOption;
    public String[] options;
    //private boolean fromMainMenu;
    public boolean conditieMain = false;
    private MainMenu mainMenu;
    private PauseMenu pauseMenu;

    // Settings variables
    private boolean soundEnabled = true;
    private boolean musicEnabled = true;
    private float soundVolume = 1.0f;
    private float musicVolume = 0.8f;
    private AudioPlayer audioPlayer;

    public SettingsMenu(GameWindow window, AudioPlayer audioPlayer) {
        this.window = window;
        this.audioPlayer = audioPlayer;
        this.isVisible = false;
        this.selectedOption = 0;
        updateOptionsText();
    }

    public void setFromMainMenu(boolean fromMain) {
        mainMenu.fromMainMenu = fromMain;
    }
    public void setMainMenu(MainMenu mainMenu) { this.mainMenu = mainMenu; }
    public void setPauseMenu(PauseMenu pauseMenu) { this.pauseMenu = pauseMenu; }

    public void toggle() {
        isVisible = !isVisible;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public int getSelectedOption() {
        return selectedOption;
    }



    private void updateOptionsText() {
        this.options = new String[]{
                "Sound: " + (soundEnabled ? "ON" : "OFF"),
                "Music: " + (musicEnabled ? "ON" : "OFF"),
                "Music Vol: " + (int)(musicVolume * 100) + "%",
                "Back"
        };
    }

    public void draw(Graphics g) {
        if (!isVisible) return;

        // Semi-transparent overlay
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, window.GetWndWidth(), window.GetWndHeight());

        // Title
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        drawCenteredString(g, "SETTINGS", window.GetWndWidth(), 100);

        // Options
        g.setFont(new Font("Arial", Font.PLAIN, 36));
        for (int i = 0; i < options.length; i++) {
            if (i == selectedOption) {
                g.setColor(Color.YELLOW);
            } else {
                g.setColor(Color.WHITE);
            }
            drawCenteredString(g, options[i], window.GetWndWidth(), 200 + i * 50);
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
            case 0: // Toggle Sound
                soundEnabled = !soundEnabled;
                updateOptionsText();
                // Oprește/redă toate sunetele în funcție de opțiune
                if (!soundEnabled) {
                    audioPlayer.setSoundEffectVolume(0.0f, null); // Mută efectele sonore (volum 0)
                } else {
                    audioPlayer.setSoundEffectVolume(soundVolume, null); // Restorează volumul selectat
                }


                break;
            case 1: // Toggle Music
                musicEnabled = !musicEnabled;
                updateOptionsText();

                if (!musicEnabled) {
                    audioPlayer.stopBackgroundMusic();
                } else {
                    audioPlayer.resumeBackgroundMusic();
                }
                break;
            case 2: // Music Volume
                musicVolume = (musicVolume >= 1.0f) ? 0.0f : (float) Math.round((musicVolume + 0.1f) * 10f) / 10f;
                musicVolume = Math.min(musicVolume, 1.0f); // Siguranță: limitează la 1.0
                audioPlayer.setVolume(musicVolume); // Aplică volumul pentru muzica de fundal
                updateOptionsText();
                break;
            case 3: // Back
                isVisible = false;
                if (this.isFromMainMenu() && mainMenu != null) {
                    mainMenu.setVisible(true);
                } else if (!this.isFromMainMenu() && pauseMenu != null) {
                    pauseMenu.setVisible(true);
                }
                break;
        }
    }

    private void drawCenteredString(Graphics g, String text, int width, int y) {
        FontMetrics fm = g.getFontMetrics();
        int x = (width - fm.stringWidth(text)) / 2;
        g.drawString(text, x, y);
    }

    public String[] getSoundOptions() {
        return options;
    }

    public void setSelectedOption(int option) {
        if (option >= 0 && option < options.length) {
            this.selectedOption = option;
        }
    }

    public boolean isFromMainMenu() {
        return mainMenu.fromMainMenu;
    }

    public void setVisible(boolean b) {
        isVisible = b;
    }
    public boolean isSoundEnabled() {
        return soundEnabled;
    }


}