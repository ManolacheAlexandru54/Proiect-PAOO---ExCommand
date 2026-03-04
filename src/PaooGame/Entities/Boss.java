package PaooGame.Entities;

import PaooGame.Graphics.Assets;
import PaooGame.Game;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/// Clasa facuta pentru a desena Bossul final al jocului.
/// Aceasta extinde clasa Enemy și implementează interfața MovableEntity pentru a gestiona mișcarea și animațiile.
/// Bossul are mai multe stări (IDLE, ATTACK, WALK) și poate ataca jucătorul dacă acesta se află în raza de atac.
/// Bossul are și o logică de patrulare, unde se mișcă stânga-dreapta într-un interval specificat.
/// De asemenea, Bossul are o logică de gravitație și poate aplica damage jucătorului.

public class Boss extends Enemy implements MovableEntity{

    private BufferedImage[] idleFrames;
    private BufferedImage[] attackFrames;
    private BufferedImage[] walkFrames;

    private int frameIndex = 0;
    private int animationSpeed = 6; // cu cât mai mare, cu atât mai lentă
    private int animationTick = 0;
    private boolean facingRight = true;

    private boolean onGround = false;
    private float velocityY = 0f;
    private final float gravity = 0.4f;

    private enum State { IDLE, ATTACK, WALK }
    private State currentState = State.IDLE;

    /// detectie
    private final float Detection_Radius = 100;
    private final float Attack_Radius = 50;
    private int Attack_Cooldown = 50;
    private boolean Cycle_Attack = false;
    private Rectangle debugAttackBox = null;
    private int attackBoxWidth = 60; // Lățimea box-ului de atac
    private int attackBoxHeight = 150; // Poți modifica dacă e necesar
    private int attackOffsetX = 0;
    private double speed = 3.2;

    private boolean hasAttackedDuringAnimation = false; // Utilizat pentru a da damage doar o dată


    /// patrol
    private int patrolRange = 100; // cât merge stânga/dreapta
    private int patrolOriginX;     // punctul de unde a început
    private boolean movingRight = true; // direcția

    private float speedX = 0; // ✅ adăugat pentru getSpeedX()

    private boolean climbing = false; // ✅ adăugat pentru climbing logic

    private final float safeFallSpeed = 10.0f; // ✅ adăugat pentru damage logic


    public Boss(int x, int y) {
        super(x, y, 192, 124, 1000);

        this.idleFrames = new BufferedImage[]{Assets.bossIdle}; // dacă e doar un frame
        this.attackFrames = Assets.bossAttack;
        this.walkFrames = Assets.bossWalk;

        this.patrolOriginX = x;
        this.speedX = 1.0f;
        this.currentState = State.WALK;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public float getVelocityY() {
        return velocityY;
    }

    @Override
    public void setVelocityY(float velocityY) {
        this.velocityY = velocityY;
    }

    @Override
    public float getSpeedX() {
        return speedX;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public boolean isOnGround() {
        return onGround;
    }

    @Override
    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    @Override
    public boolean isClimbing() {
        return climbing;
    }

    @Override
    public void setClimbing(boolean climbing) {
        this.climbing = climbing;
    }

    @Override
    public float getSafeFallSpeed() {
        return safeFallSpeed;
    }

    @Override
    public void applyFallDamage(int damage) {
        this.hp -= damage;
        if (hp <= 0) {
            this.alive = false;
        }
    }
    @Override
    public Rectangle getHitBox() {
        return new Rectangle(x + 45, y + 45, width - 90, height - 50);
    }


    @Override
    public void update() {
        if (Attack_Cooldown > 0) {
            Attack_Cooldown--;
        }

        updateAI(Game.player); // Actualizăm logica AI

        animationTick++;

        if (animationTick >= animationSpeed) {
            animationTick = 0;
            frameIndex++;

            int maxFrames = switch (currentState) {
                case IDLE -> idleFrames.length;
                case ATTACK -> attackFrames.length;
                case WALK -> walkFrames.length;
            };

            if (frameIndex >= maxFrames) {
                frameIndex = 0;

                if (currentState == State.ATTACK) {
                    currentState = State.IDLE; // După atac, Bossul revine la IDLE
                    hasAttackedDuringAnimation = false; // Resetăm flag-ul de damage
                }
            }

            // Aplicăm damage la cadrul 10 din animația de atac
            if (currentState == State.ATTACK && frameIndex == 8 && !hasAttackedDuringAnimation) {
                checkDamage(Game.player); // Verificăm și aplicăm damage jucătorului
                hasAttackedDuringAnimation = true; // Marcăm că damage-ul a fost aplicat
            }
        }

        // Gravitația
        if (!onGround) {
            velocityY += gravity;
            y += velocityY;
        }

        if (Game.currentMap == 1) {
            Game.collisionManager1.checkEntityCollision(this);
        } else if (Game.currentMap == 2) {
            Game.collisionManager2.checkEntityCollision(this);
        } else if (Game.currentMap == 3) {
            Game.collisionManager3.checkEntityCollision(this);
        }
        // Coliziuni
    }

    private void updateAI(Player player) {
        int attackWidth = 220; // Lățimea pătratului albastru pentru atac
        int attackHeight = 100; // Înălțimea pătratului albastru pentru atac

        // Calculăm zona de atac în funcție de poziția Bossului
        int attackX = x + (width / 2) - (attackWidth / 2);
        int attackY = y + (height / 2) - (attackHeight / 2);
        debugAttackBox = new Rectangle(attackX, attackY, attackWidth, attackHeight);

        Rectangle attackBox = new Rectangle(attackX, attackY, attackWidth, attackHeight);
        Rectangle playerBox = new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight());

        // Verificăm dacă jucătorul se află în raza de detectare
        if (attackBox.intersects(playerBox)) {
            if (currentState != State.ATTACK) {
                currentState = State.ATTACK; // Trecem la starea de atac
                frameIndex = 0; // Resetăm animația
                hasAttackedDuringAnimation = false; // Resetăm flag-ul de damage
            }
        } else {
            // Dacă jucătorul NU mai este în raza de atac, mergi spre el
            if (player.getX() < x) {
                x -= speed; // Deplasare spre stânga
                facingRight = false;
            } else {
                x += speed; // Deplasare spre dreapta
                facingRight = true;
            }

            currentState = State.WALK; // Trecem la mers
        }


    }

    private Rectangle createAttackBox() {
        int boxX = facingRight ? (x + width - 20) : (x - attackBoxWidth + 20); // Ajustăm poziția în funcție de dimensiuni
        int boxY = y + 40; // Lăsăm un offset vertical pentru precizie
        return new Rectangle(boxX, boxY, 80, 80); // Micșorăm zona de atac la dimensiuni mai realiste
    }



    private void moveToward(Player player) {
        float dx = player.getX() - this.x;
        int direction = dx > 0 ? 1 : -1; // Determinăm direcția

        // Creștem viteza de mișcare pentru a reacționa mai rapid
        this.x += direction * 3; // Viteză ajustată direct
        facingRight = direction > 0; // Actualizăm direcția sprite-ului
    }


    private void attack(Player player) {
        int attackWidth = 180; // Lățimea pătratului albastru
        int attackHeight = 64; // Înălțimea fixă a pătratului albastru

        // Calculăm pătratul albastru pentru intersecția de atac
        Rectangle attackBox;
        if (facingRight) {
            // Dacă Bossul e orientat spre dreapta, pătratul albastru este la dreapta sa
            attackBox = new Rectangle(x + width, y + height / 2 - attackHeight / 2, attackWidth, attackHeight);
        } else {
            // Dacă Bossul e orientat spre stânga, pătratul albastru este la stânga sa
            attackBox = new Rectangle(x - attackWidth, y + height / 2 - attackHeight / 2, attackWidth, attackHeight);
        }

        debugAttackBox = attackBox; // Salvăm hitbox-ul pentru debugging vizual

        // Hitbox-ul jucătorului
        Rectangle playerBox = new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight());

        // Verificăm dacă pătratul albastru intersectează jucătorul
        if (attackBox.intersects(playerBox)) {
            player.takeDamage(25); // Aplicăm damage
        }
    }


    private void patrol(){
        if (movingRight) {
            x += 1;
            if (x > patrolOriginX + patrolRange) movingRight = false;
        } else {
            x -= 1;
            if (x < patrolOriginX - patrolRange) movingRight = true;
        }

    }

    @Override
    public void draw(Graphics g) {
        if (!alive) return;

        BufferedImage currentFrame = switch (currentState) {
            case IDLE -> idleFrames[Math.min(frameIndex, idleFrames.length - 1)];
            case ATTACK -> attackFrames[Math.min(frameIndex, attackFrames.length - 1)];
            case WALK -> walkFrames[Math.min(frameIndex, walkFrames.length - 1)];
        };

        Graphics2D g2d = (Graphics2D) g;
        AffineTransform original = g2d.getTransform();

        if (!facingRight) {
            g2d.drawImage(currentFrame, x, y, width, height, null);

        } else {

            g2d.translate(x + width, y);
            g2d.scale(-1, 1);
            g2d.drawImage(currentFrame, 0, 0, width, height, null);
        }

        Player player = Game.player;  // Obține poziția actuală a jucătorului
        int detectionSize = (int) Detection_Radius;  // Dimensiunea pătratului verde

    }

    private void checkDamage(Player player) {
        int attackWidth = 180;
        int attackHeight = 64;

        Rectangle attackBox;
        if (facingRight) {
            attackBox = new Rectangle(x + width, y + height / 2 - attackHeight / 2, attackWidth, attackHeight);
        } else {
            attackBox = new Rectangle(x - attackWidth, y + height / 2 - attackHeight / 2, attackWidth, attackHeight);
        }

        debugAttackBox = attackBox; // opțional, pt. desen

        Rectangle playerBox = new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight());

        if (attackBox.intersects(playerBox)) {
            player.takeDamage(25);
        }
    }


}