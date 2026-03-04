package PaooGame.Entities;

import PaooGame.Graphics.Assets;
import PaooGame.Game;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/// Clasa Imp reprezintă un inamic care poate fi controlat de AI.
///  Aceasta extinde clasa Enemy și implementează interfața MovableEntity.
/// Imp are diferite stări (IDLE, ATTACK, WALK) și poate să se miște, să atace și să verifice coliziunile.
/// De asemenea, are o logică de detecție a jucătorului și poate trasa gloanțe.
/// Impul are animații pentru stările sale și poate să se miște în stânga și dreapta.

public class Imp extends Enemy implements MovableEntity{

    private BufferedImage[] idleFrames;
    private BufferedImage[] attackFrames;
    private BufferedImage[] walkFrames;

    private int frameIndex = 0;
    private int animationSpeed = 8; // cu cât mai mare, cu atât mai lentă
    private int animationTick = 0;
    private boolean facingRight = true;

    private boolean onGround = false;
    private float velocityY = 0f;
    private final float gravity = 0.2f;


    //hiotbox

    private enum State { IDLE, ATTACK, WALK }
    private State currentState = State.IDLE;

    /// detectie
    private final float Detection_RadiusX = 640;
    private final float Detection_RadiusY = 20;
    private final float Attack_Radius = 2000;
    private int Attack_Cooldown = 45;
    private boolean Cycle_Attack = false;
    private Rectangle debugAttackBox = null;
    private int attackBoxWidth = 60; // Lățimea box-ului de atac
    private int attackBoxHeight = 120; // Poți modifica dacă e necesar
    private int attackOffsetX = 0;
    private int speed = 2;

    private boolean hasAttackedDuringAnimation = false; // Utilizat pentru a da damage doar o dată


    /// patrol
    private int patrolRange = 100; // cât merge stânga/dreapta
    private int patrolOriginX;     // punctul de unde a început
    private boolean movingRight = true; // direcția

    private float speedX = 0; // ✅ adăugat pentru getSpeedX()

    private boolean climbing = false; // ✅ adăugat pentru climbing logic

    private final float safeFallSpeed = 10.0f; // ✅ adăugat pentru damage logic


    public Imp(int x, int y) {
        super(x, y, 50, 50, 120);

        this.idleFrames = new BufferedImage[]{Assets.impIdle}; // dacă e doar un frame
        this.attackFrames = Assets.impAttack;
        this.walkFrames = Assets.impWalk;

        this.patrolOriginX = x;
        this.speedX = 1.0f;
        this.currentState = State.WALK;
    }

    private void shootBullet() {
        int bulletX = facingRight ? x + width : x - 10;
        int bulletY = y + height / 2;

        Bullet bullet = new Bullet(
                bulletX,
                bulletY,
                facingRight,
                Assets.impBullet,
                Assets.impBulletTrace
        );

        Game.enemyBullets.add(bullet);
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
        return new Rectangle(x + 10, y + 10, width - 20, height - 20);
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
                    currentState = State.IDLE; // După atac, Impul revine la IDLE
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

        Game.collisionManager1.checkEntityCollision(this); // Coliziuni
    }

    private void updateAI(Player player) {
        float dx = Math.abs(player.getX() - x); // Distanța pe axa X
        float dy = Math.abs(player.getY() - y); // Distanța pe axa Y

        // 🔄 Se uită mereu după jucător
        facingRight = player.getX() >= x;

        // Verificăm dacă player-ul este în zona de detecție dreptunghiulară
        if (dx <= Detection_RadiusX && dy <= Detection_RadiusY) { // 🔥 În raza de detecție

            if (dx <= Attack_Radius) { // Dacă este și mai aproape pentru atac
                if (currentState != State.ATTACK) {
                    currentState = State.ATTACK;
                    frameIndex = 0;
                    hasAttackedDuringAnimation = false;
                }

                if (Attack_Cooldown <= 0) {
                    shootBullet();
                    Attack_Cooldown = 60;
                }
            } else {
                // E în raza de detecție, dar prea departe pentru atac — doar merge spre player
                if (player.getX() < x) {
                    x -= speed;
                } else {
                    x += speed;
                }

                currentState = State.WALK;
            }

        } else {
            currentState = State.IDLE; // Prea departe → rămâne pe loc
        }
    }




    private Rectangle createAttackBox() {
        int boxX = facingRight ? (x + width - 20) : (x - attackBoxWidth + 20); // Ajustăm poziția în funcție de dimensiuni
        int boxY = y + 20; // Lăsăm un offset vertical pentru precizie
        return new Rectangle(boxX, boxY, 80, 60); // Micșorăm zona de atac la dimensiuni mai realiste
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
            // Dacă Impul e orientat spre dreapta, pătratul albastru este la dreapta sa
            attackBox = new Rectangle(x + width, y + height / 2 - attackHeight / 2, attackWidth, attackHeight);
        } else {
            // Dacă Impul e orientat spre stânga, pătratul albastru este la stânga sa
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

        if (debugAttackBox != null) {
            g2d.setColor(Color.GREEN);
            g2d.drawRect(debugAttackBox.x, debugAttackBox.y, debugAttackBox.width, debugAttackBox.height);
        }

        int frameW = currentFrame.getWidth();
        int frameH = currentFrame.getHeight();

        if (!facingRight) {
            g2d.translate(x + frameW, y);
            g2d.scale(-1, 1);
            g2d.drawImage(currentFrame, 0, 0, frameW, frameH, null);
            g2d.setTransform(original); // reset transformare după flip
        } else {
            g2d.drawImage(currentFrame, x, y, frameW, frameH, null);
        }



        // DEBUG: Pătratul verde care urmărește jucătorul (zona de DETECTIE)
        Player player = Game.player;  // Obține poziția actuală a jucătorului
        int detectionSize = (int) Detection_RadiusX;  // Dimensiunea pătratului verde

        // DEBUG: Desenează hitbox-ul real (cel folosit pentru coliziune cu gloanțele)

    }

    private void checkDamage(Player player) {
        int attackWidth = 200; // Dimensiunea zonei de atac
        int attackHeight = 64;

        // Calculăm zona de atac
        int attackX = x + (width / 2) - (attackWidth / 2);
        int attackY = y + (height / 2) - (attackHeight / 2);

        Rectangle attackBox = new Rectangle(attackX, attackY, attackWidth, attackHeight);
        Rectangle playerBox = new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight());

        // Dacă zona de atac intersectează hitbox-ul jucătorului, aplicăm damage
        if (attackBox.intersects(playerBox)) {
            player.takeDamage(25); // Aplicăm damage-ul
        }
    }
}