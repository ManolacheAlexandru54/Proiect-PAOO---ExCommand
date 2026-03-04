package PaooGame.Inputs;

import PaooGame.Game;
import PaooGame.GameWindow.GameWindow;
import PaooGame.GameStates.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import PaooGame.Entities.*;

///Clasa MouseInputs este responsabilă pentru gestionarea intrărilor de la mouse în joc.
/// Aceasta implementează interfețele MouseListener și MouseMotionListener pentru a răspunde la evenimentele de mouse, cum ar fi click-uri și mișcări.
public class MouseInputs implements MouseListener, MouseMotionListener {
    private GameWindow window;
    private PauseMenu pauseMenu;
    private SettingsMenu settingsMenu;
    private Player player;

    public MouseInputs() {
    }

    public void setWindow(GameWindow window) {
        this.window = window;
    }

    public void setMenu(PauseMenu pauseMenu) {
        this.pauseMenu = pauseMenu;
    }

    public void setSettingsMenu(SettingsMenu settingsMenu) {
        this.settingsMenu = settingsMenu;
    }

    public void setPlayer(Player player) { this.player = player; }

    @Override
    public void mouseClicked(MouseEvent e) {
        // Verificăm dacă meniul de victorie (WinMenu) este vizibil
        if (Game.getInstance().getWinMenu() != null && Game.getInstance().getWinMenu().isVisible()) {
            //Game.getInstance().getWinMenu().click(e);
            return; // Oprire procesare ulterioară dacă WinMenu este activ
        }

        // Verificăm meniul principal (MainMenu)
        MainMenu mainMenu = Game.getInstance().getMainMenu();
        if (mainMenu != null && mainMenu.isVisible()) {
            handleMainMenuClick(e, mainMenu);
            return;
        }

        // Verificăm meniul de pauză (PauseMenu)
        if (pauseMenu != null && pauseMenu.isVisible()) {
            handlePauseMenuClick(e);
            return;
        }

        // Verificăm meniul de setări (SettingsMenu)
        if (settingsMenu != null && settingsMenu.isVisible()) {
            handleSettingsMenuClick(e);
            return;
        }

        // Verificăm meniul de moarte (DeathMenu)
        DeathMenu deathMenu = Game.getInstance().getDeathMenu();
        if (deathMenu != null && deathMenu.isVisible()) {
            handleDeathMenuClick(e, deathMenu);
        }
    }

    private void handlePauseMenuClick(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        int buttonWidth = 200;
        int buttonHeight = 40;
        int startY = 300; // Coordonatele verticale pentru primul buton
        int spacing = 50;

        for (int i = 0; i < pauseMenu.getOptions().length; i++) {
            int buttonX = (window.GetWndWidth() - buttonWidth) / 2;
            int buttonY = startY + i * spacing;

            // Verificăm dacă click-ul se află pe unul dintre butoane
            if (mouseX >= buttonX && mouseX <= buttonX + buttonWidth &&
                    mouseY >= buttonY && mouseY <= buttonY + buttonHeight) {
                pauseMenu.setSelectedOption(i);
                pauseMenu.selectOption();
                break;
            }
        }
    }


    private void handleMainMenuClick(MouseEvent e, MainMenu mainMenu) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        int buttonWidth = 300;
        int buttonHeight = 50;
        int startY = 300; // Coordonatele verticale pentru primul buton
        int spacing = 60;

        for (int i = 0; i < mainMenu.getOptions().length; i++) {
            int buttonX = (window.GetWndWidth() - buttonWidth) / 2;
            int buttonY = startY + i * spacing;

            // Verificăm dacă click-ul se află pe unul dintre butoane
            if (mouseX >= buttonX && mouseX <= buttonX + buttonWidth &&
                    mouseY >= buttonY && mouseY <= buttonY + buttonHeight) {
                mainMenu.setSelectedOption(i);
                mainMenu.selectOption();
                break;
            }
        }
    }


    private void handleDeathMenuClick(MouseEvent e, DeathMenu deathMenu) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        int buttonWidth = 200;
        int buttonHeight = 50;
        int startY = 300; // Coordonatele verticale pentru primul buton
        int spacing = 60;

        for (int i = 0; i < deathMenu.getOptions().length; i++) {
            int buttonX = (window.GetWndWidth() - buttonWidth) / 2;
            int buttonY = startY + i * spacing;

            // Verificăm dacă click-ul se află pe unul dintre butoane
            if (mouseX >= buttonX && mouseX <= buttonX + buttonWidth &&
                    mouseY >= buttonY && mouseY <= buttonY + buttonHeight) {
                deathMenu.selectOption();
                break;
            }
        }
    }
    private void handleSettingsMenuClick(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        int buttonWidth = 300;
        int buttonHeight = 50;
        int startY = 200; // Coordonatele verticale pentru primul buton
        int spacing = 60;

        for (int i = 0; i < settingsMenu.getSoundOptions().length; i++) {
            int buttonX = (window.GetWndWidth() - buttonWidth) / 2;
            int buttonY = startY + i * spacing;

            // Verificăm dacă click-ul se află pe unul dintre butoane
            if (mouseX >= buttonX && mouseX <= buttonX + buttonWidth &&
                    mouseY >= buttonY && mouseY <= buttonY + buttonHeight) {
                settingsMenu.setSelectedOption(i);
                settingsMenu.selectOption();
                break;
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (player != null) {
                player.shoot();
                player.setSpeedX(1);
                player.handleMousePressed(e);
            }
        }
        
        if (e.getButton() == MouseEvent.BUTTON3) {
            if (player != null) {
                player.activateMetalJacket();
                player.setSpeedX(1);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1 || e.getButton() == MouseEvent.BUTTON3){
            if(player != null)
            {
                player.setSpeedX(4);
            }
        }

    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {
        if (pauseMenu != null && pauseMenu.isVisible()) {
            handleMenuHover(e);
        } else if (settingsMenu != null && settingsMenu.isVisible()) {
            handleSettingsHover(e);
        }
    }

    private void handleMenuHover(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        int buttonWidth = 200;
        int buttonHeight = 40;
        int startY = 200;
        int spacing = 50;

        for (int i = 0; i < pauseMenu.getOptions().length; i++) {
            int buttonX = (window.GetWndWidth() - buttonWidth) / 2;
            int buttonY = startY + i * spacing;

            if (mouseX >= buttonX && mouseX <= buttonX + buttonWidth &&
                    mouseY >= buttonY - 30 && mouseY <= buttonY + 10) {
                pauseMenu.setSelectedOption(i);
                break;
            }
        }
    }

    private void handleSettingsHover(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        int buttonWidth = 300;
        int buttonHeight = 40;
        int startY = 200;
        int spacing = 50;

        for (int i = 0; i < settingsMenu.getSoundOptions().length; i++) {
            int buttonX = (window.GetWndWidth() - buttonWidth) / 2;
            int buttonY = startY + i * spacing;

            if (mouseX >= buttonX && mouseX <= buttonX + buttonWidth &&
                    mouseY >= buttonY - 30 && mouseY <= buttonY + 10) {
                settingsMenu.setSelectedOption(i);
                break;
            }
        }
    }
}