package PaooGame.Entities;

/// Clasa MovableEntity define un tip de entitate care poate fi mutată în joc.
/// Aceasta este o interfață care specifică metodele necesare pentru a gestiona poziția, dimensiunile, viteza și starea de cădere a entităților.
/// Entitățile care implementează această interfață pot fi jucători, inamici sau alte obiecte care necesită mișcare și interacțiune cu mediul înconjurător.
/// Interfața oferă metode pentru obținerea și setarea coordonatelor X și Y, dimensiunilor, vitezei de cădere, vitezei orizontale,
/// starea de pe sol (onGround), starea de escaladare (climbing) și aplicarea daunelor cauzate de cădere.

public interface MovableEntity {
    int getX();
    int getY();
    int getWidth();
    int getHeight();

    float getVelocityY();
    void setVelocityY(float velocityY);

    float getSpeedX();

    void setX(int x);
    void setY(int y);

    boolean isOnGround();
    void setOnGround(boolean onGround);

    boolean isClimbing();
    void setClimbing(boolean climbing);

    float getSafeFallSpeed();
    void applyFallDamage(int damage);
}
