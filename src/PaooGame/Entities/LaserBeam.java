package PaooGame.Entities;

import java.awt.*;

/// Clasa LaserBeam reprezintă o rază de laser care poate fi trasă de către StarBoss.
/// Aceasta conține informații despre poziția sa, direcția de deplasare, durata de viață și starea de activitate.
/// Raza de laser are o lungime fixă și poate fi desenată pe ecran.
/// De asemenea, poate verifica dacă intersectează un anumit dreptunghi (de exemplu, un hitbox al jucătorului).
/// Această clasă este esențială pentru implementarea atacului cu laser al StarBoss-ului și pentru a oferi o provocare suplimentară jucătorului.

public class LaserBeam {
    private double x, y;
    private double dx, dy;
    private final int length = 5000;

    private final long spawnTime;
    private final long warningDuration = 800; // cât durează avertizarea (ms)
    private final long activeDuration = 700;  // cât durează efectiv raza

    private boolean done = false;

    public LaserBeam(int startX, int startY, double angleDegrees) {
        this.x = startX;
        this.y = startY;

        double angleRadians = Math.toRadians(angleDegrees);
        dx = Math.cos(angleRadians);
        dy = Math.sin(angleRadians);

        spawnTime = System.currentTimeMillis();
    }

    public void update(long currentTime) {
        long elapsed = currentTime - spawnTime;
        if (elapsed > warningDuration + activeDuration) {
            done = true;
        }
    }

    public void draw(Graphics g) {
        long elapsed = System.currentTimeMillis() - spawnTime;
        Graphics2D g2d = (Graphics2D) g;

        // Salvează stroke-ul original
        Stroke originalStroke = g2d.getStroke();

        int endX = (int)(x + dx * length);
        int endY = (int)(y + dy * length);

        if (elapsed < warningDuration) {
            g2d.setColor(new Color(255, 255, 0, 100)); // roșu semi-transparent
            g2d.setStroke(new BasicStroke(4f));
        } else {
            g2d.setColor(new Color(255, 0, 0)); // roșu solid
            g2d.setStroke(new BasicStroke(8f));
        }

        g2d.drawLine((int)x, (int)y, endX, endY);

        // Restaurează stroke-ul original ca să nu afecteze restul graficii
        g2d.setStroke(originalStroke);
    }

    public boolean isActive() {
        long elapsed = System.currentTimeMillis() - spawnTime;
        return elapsed >= warningDuration && elapsed < (warningDuration + activeDuration);
    }

    public boolean isDone() {
        return done;
    }

    public boolean intersects(Rectangle rect) {
        long elapsed = System.currentTimeMillis() - spawnTime;
        if (elapsed < warningDuration || elapsed > warningDuration + activeDuration)
            return false; // doar când e activă

        int endX = (int)(x + dx * length);
        int endY = (int)(y + dy * length);

        return rect.intersectsLine(x, y, endX, endY);
    }
}

