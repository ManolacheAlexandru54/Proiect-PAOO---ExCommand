package PaooGame.Entities;

import java.awt.*;
import PaooGame.Graphics.Assets;

/// Clasa care reprezintă un NPC (Non-Playable Character) în joc.
/// Aceasta conține informații despre poziția sa, dimensiuni, zona de coliziune și mesajul de interacțiune.
/// NPC-ul poate fi interacționat de către jucător prin apăsarea unei taste.
/// Când jucătorul se află în apropierea NPC-ului și apasă tasta "F", un mesaj cu instrucțiuni este afișat.
/// Această clasă este esențială pentru a adăuga interacțiuni cu NPC-uri în joc, oferind jucătorului informații sau misiuni.
/// NPC-ul se numeste Bro

public class Bro {
    private int x, y;          // Coordonatele NPC-ului pe hartă
    private int width, height; // Dimensiunile sprite-ului NPC
    private Rectangle bounds;  // Zona de coliziune/interacțiune
    private boolean playerNearby = false; // Indică dacă player-ul este în zona de interacțiune
    private boolean isMessageToggled = false; // Control dacă mesajul cu instrucțiuni este afișat
    private String message;    // Mesajul de afișat când player-ul interacționează

    public Bro(int x, int y, String message) {
        this.x = x;
        this.y = y;
        this.message = message;
        this.width = 64;  // Dimensiunile sprite-ului (modifică după asset)
        this.height = 64;

        // Zona de interacțiune
        this.bounds = new Rectangle(x, y, width, height);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    private boolean fWasPressed = false; // adaugă această variabilă în clasa NPC

    public void checkPlayerCollision(Rectangle playerBounds, boolean fKeyPressed) {
        // Verifică dacă player-ul este în apropiere
        playerNearby = playerBounds.intersects(bounds);

        // Toggle mesajul doar când F este apăsat (o singură dată)
        if (playerNearby && fKeyPressed && !fWasPressed) {
            isMessageToggled = !isMessageToggled;
        }
        fWasPressed = fKeyPressed;
    }

    public boolean isPlayerNearby() {
        return playerNearby;
    }

    public boolean isMessageToggled() {
        return isMessageToggled;
    }

    public void draw(Graphics g) {
        // Desenează NPC-ul pe hartă
        g.drawImage(Assets.npcSprite, x, y  + 10, width, height, null);

        // Dacă player-ul este aproape, desenează mesajul "Press [F] to toggle"
        if (playerNearby) {
            g.setColor(new Color(0, 0, 0, 150)); // Fundal semitransparent
            g.fillRect(x - 70, y - 50, 200, 50);

            g.setColor(Color.WHITE);
            g.drawString("Press [F] to speak with Bro", x - 40, y - 30);
        }
    }

    public void drawMessage(Graphics g) {
        if (isMessageToggled) {
            Font messageFont = new Font("Arial", Font.PLAIN, 16);
            g.setFont(messageFont);
            FontMetrics metrics = g.getFontMetrics(messageFont);

            int maxLineWidth = 400; // lățimea maximă a mesajului
            String[] words = message.split(" ");
            java.util.List<String> lines = new java.util.ArrayList<>();

            StringBuilder currentLine = new StringBuilder();

            for (String word : words) {
                String testLine = currentLine + (currentLine.length() > 0 ? " " : "") + word;
                int lineWidth = metrics.stringWidth(testLine);

                if (lineWidth > maxLineWidth) {
                    lines.add(currentLine.toString());
                    currentLine = new StringBuilder(word);
                } else {
                    currentLine.append((currentLine.length() > 0 ? " " : "")).append(word);
                }
            }
            if (currentLine.length() > 0) {
                lines.add(currentLine.toString());
            }

            int lineHeight = metrics.getHeight();
            int padding = 20;
            int boxWidth = maxLineWidth + padding * 2;
            int boxHeight = lineHeight * lines.size() + padding * 2;

            int messageX = x - (boxWidth / 2);
            int messageY = y - boxHeight - 70;

            // fundal
            g.setColor(new Color(0, 0, 0, 180));
            g.fillRect(messageX, messageY, boxWidth, boxHeight);

            // chenar
            g.setColor(Color.WHITE);
            g.drawRect(messageX, messageY, boxWidth, boxHeight);

            // text
            g.setColor(Color.WHITE);
            for (int i = 0; i < lines.size(); i++) {
                g.drawString(lines.get(i), messageX + padding, messageY + padding + (i + 1) * lineHeight - 5);
            }
        }
    }


}