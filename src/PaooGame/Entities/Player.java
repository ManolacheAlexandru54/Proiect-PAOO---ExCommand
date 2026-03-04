package PaooGame.Entities;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import PaooGame.Audio.AudioPlayer;

import PaooGame.GameStates.Observer;
import PaooGame.Graphics.Assets;
import PaooGame.Game;

import javax.swing.*;
import PaooGame.GameStates.Subject;
import PaooGame.GameStates.Observer;

/// Clasa pentru jucător
/// Aceasta clasa implementează interfața MovableEntity și definește comportamentul jucătorului în joc.
/// Aceasta include mișcarea, animațiile, săritura, dash-ul, tragerea și alte acțiuni.
/// Aceasta clasa gestionează și starea jucătorului, cum ar fi sănătatea, colectarea obiectelor și activarea abilităților speciale.
public class Player implements MovableEntity, Observer {
    private int x, y;
    private int width = 30;
    private int height = 32;
    private float speedX = 4;
    private float speedY = 4;
    private int prevX, prevY;

    /// animatii
    private int frameIndex = 0;
    private int animationTick = 0;
    private int animationSpeed = 6;// invers propotional
    private boolean moving = false;
    private String state = "idle";
    private String prevState = "idle";
    private boolean facingRight = true;
    private boolean onGround = false;
    private float velocityY = 0f;
    private final float gravity = 0.5f;
    private final float jumpStrength = -10f;
    private boolean jumping = false;
    private boolean climbing = false;

    /// Shooting
    private ArrayList<Bullet> bullets = new ArrayList<>();
    public boolean shooting = false;
    private int shooting_timer = 0;
    private final int shooting_duration = 10;
    /// MetalJacket
    private boolean metalJacketActive = false;
    private int metalJacketCooldown = 0;
    private final int metalJacketCooldownMax = 300; // 5 secunde
    private final int metalJacketDuration = 30; // cât durează animația sau efectul (ex: 0.5s)
    private int metalJacketTimer = 0;

    /// Dash
    private boolean dashing = false;
    private int dashTimer = 0;
    private final int dashDuration = 10; // frames
    private final float dashSpeed = 10f;
    private final int dashCooldownMax = 60;
    private int dashCooldown = 0;

    /// SupressiveFire
    private boolean suppressing = false;
    private int suppressTimer = 0;
    private final int suppressDuration = 120; // 2 secunde la 60 FPS
    private final int suppressFireRate = 12; // un glonț la fiecare 12 frame-uri (~5 pe direcție în 2 secunde)
    private int suppressTick = 0;
    private boolean suppressFireRight = true;
    private final int suppressCooldownMax = 600; // 5 secunde la 60 FPS
    private int suppressCooldown = 0;

    ///HP
    private int hp = 100;
    protected final float safeFallSpeed = 17.0f;
    public int Imunosupressor_Count = 0;
    private boolean isKeyCardCollected = false;
    private boolean ePressedLastFrame = false;


    private AudioPlayer audioPlayer;


    ///Constructorul claselei Player
    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        this.audioPlayer = new AudioPlayer();
    }
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public void setX(int x)
    {
        this.x = x;
    }
    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
    public float getSpeedX(){
        return speedX;
    }

    public boolean isClimbing() {
        return climbing;
    }

    public void setClimbing(boolean climbing) {
        this.climbing = climbing;
    }

    public float getSpeedY(){
        return speedY;
    }
    public void setSpeedX(float speedX){
        this.speedX = speedX;
    }
    public void setSpeedY(float speedY){
        this.speedY = speedY;
    }

    public boolean isOnGround(){
        return onGround;
    }
    public void setOnGround(boolean onGround){
        this.onGround = onGround;
    }
    public float getVelocityY() {
        return velocityY;
    }
    public void setVelocityY(float velocityY){
        this.velocityY = velocityY;
    }
    public void updatePrevPosition() {
        this.prevX = x;
        this.prevY = y;
    }

    public void addImunosupressor() {
        Imunosupressor_Count++;
    }

    public void useImunosupressor() {
        if (Imunosupressor_Count > 0 && hp < 100) {
            Imunosupressor_Count--;
            hp += 50; // sau hp = 100 pentru heal complet
            if (hp > 100) hp = 100;
        }
    }
    public float getSafeFallSpeed(){ return safeFallSpeed;}

    ///Metoda care actualizează starea jucătorului
    public void update(boolean[] keys) {
        updatePrevPosition();
        moving = false;
        shooting = false;

        if (keys[KeyEvent.VK_Q]) {
            activateSuppressiveFire();
        }
        if (keys[KeyEvent.VK_E] && !ePressedLastFrame) {
            useImunosupressor();
            ePressedLastFrame = true;
        } else if (!keys[KeyEvent.VK_E]) {
            ePressedLastFrame = false;
        }

        if (metalJacketCooldown > 0) {
            metalJacketCooldown--;
        }

        if (metalJacketActive) {
            metalJacketTimer++;
            if (metalJacketTimer >= metalJacketDuration) {
                metalJacketActive = false;
                metalJacketTimer = 0;
            }
        }
        if (suppressing) {
            suppressTimer--;
            suppressTick++;

            if (suppressTick % suppressFireRate == 0) {
                if (suppressFireRight) {
                    audioPlayer.playSoundEffect("/sounds/gunshot.wav");
                    bullets.add(new Bullet(x + 15, y + 15, true, Assets.bullet, Assets.bullet_trace));
                } else {
                    audioPlayer.playSoundEffect("/sounds/gunshot.wav");
                    bullets.add(new Bullet(x - 10, y + 15, false, Assets.bullet, Assets.bullet_trace));
                }
                suppressFireRight = !suppressFireRight;
            }

            if (suppressTimer <= 0) {
                suppressing = false;
                suppressTick = 0;
                suppressFireRight = true;
            }

        } else {
            if (keys[KeyEvent.VK_A]) {
                x -= (int) speedX;
                moving = true;
                facingRight = false;
            }
            if (keys[KeyEvent.VK_D]) {
                x += (int) speedX;
                moving = true;
                facingRight = true;
            }
            if (keys[KeyEvent.VK_SPACE]) {
                jump();
            }
            dash(keys);

            if (climbing) {
                velocityY = 0;
                if (keys[KeyEvent.VK_W]) y -= (int) speedY;
                else if (keys[KeyEvent.VK_S]) y += (int) speedY;
            }
        }
        if (!onGround) {
            velocityY += gravity;
            y += velocityY;
        }


        ///Setarea stării
        if (suppressing) {
            state = "suppressing";
        } else if(metalJacketActive) {
            state = "metaljacket";
        } else if (shooting_timer > 0) {
            shooting_timer--;
            state = "shooting";
            if (shooting_timer == 0) shooting = false;
        } else if (dashing) {
            state = "dashing";
        } else if (isClimbing() && (keys[KeyEvent.VK_W] || keys[KeyEvent.VK_S])) {
            state = "climb";
        } else if (keys[KeyEvent.VK_A] || keys[KeyEvent.VK_D]) {
            state = "walk";
        } else {
            state = "idle";
        }

        // Reset animație dacă s-a schimbat starea
        if (!state.equals(prevState)) {
            frameIndex = 0;
            animationTick = 0;
            prevState = state;
        }

        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            bullet.update();
            if (!bullet.isActive()) {
                bullets.remove(i);
                i--;
            }
        }
        if(suppressCooldown > 0)
            suppressCooldown--;
        updateAnimationTick();
    }

    /// Metoda care actualizează tick-ul de animație
    private void updateAnimationTick() {
        animationTick++;
        if (animationTick >= animationSpeed) {
            animationTick = 0;
            frameIndex++;

            switch (state) {
                case "suppressing":
                    if (frameIndex >= Assets.playerSupressiveFire.length)
                        frameIndex = 0;
                    break;
                case "metaljacket":
                    int metalLength = facingRight ? Assets.playerMetalJacket.length : Assets.playerMetalJacketLeft.length;
                    if (frameIndex >= metalLength)
                        frameIndex = 0;
                    break;
                case "walk":
                    int walkLength = facingRight ? Assets.playerWalkRight.length : Assets.playerWalkLeft.length;
                    if (frameIndex >= walkLength)
                        frameIndex = 0;
                    break;
                case "climb":
                    if (frameIndex >= Assets.playerClimb.length)
                        frameIndex = 0;
                    break;
                case "shooting":
                    int shootLength = facingRight ? Assets.playerShoot.length : Assets.playerShootLeft.length;
                    if (frameIndex >= shootLength)
                        frameIndex = 0;
                    break;
                case "dashing":
                    int dashLength = facingRight ? Assets.playerDash.length : Assets.playerDashLeft.length;
                    if (frameIndex >= dashLength)
                        frameIndex = 0;
                    break;
                default:
                    frameIndex = 0;
                    break;
            }
        }
    }

    ///Jump
    /// Această metodă permite jucătorului să sară dacă se află pe sol.
    public void jump() {
        if (onGround) {
            velocityY = jumpStrength;
            onGround = false;
            jumping = true;
        }
    }
    ///Dash
    /// Această metodă permite jucătorului să execute un dash dacă este apăsat tasta Shift și cooldown-ul este gata.
    public void dash(boolean[] keys) {
        // Handle cooldown
        if (dashCooldown > 0) {
            dashCooldown--;
        }

        if (keys[KeyEvent.VK_SHIFT] && !dashing && dashCooldown == 0) {
            dashing = true;
            dashTimer = dashDuration;
            dashCooldown = dashCooldownMax;
        }

        // Perform dash movement
        if (dashing) {
            int direction = facingRight ? 1 : -1;
            x += direction * dashSpeed;
            dashTimer--;

            if (dashTimer <= 0) {
                dashing = false;
            }

            moving = true; // still counts as movement
        }
    }
    ///Draw
    /// Această metodă desenează jucătorul pe ecran, în funcție de starea sa curentă și de animațiile asociate.
    public void draw(Graphics g) {
        BufferedImage currentFrame;
        int drawX = x;
        int drawY = y;
        switch (state) {
            case "suppressing":
                drawX = x - 60;
                drawY = y - 10;
                currentFrame = Assets.playerSupressiveFire[frameIndex];
                break;
            case "metaljacket":
                if (facingRight) {
                    drawX = x;
                    drawY = y;
                    currentFrame = Assets.playerMetalJacket[frameIndex];
                } else {
                    drawX = x - 60;   // ← offset mai mic pentru stânga
                    drawY = y;
                    currentFrame = Assets.playerMetalJacketLeft[frameIndex];
                }
                break;
            case "walk":
                currentFrame = facingRight
                        ? Assets.playerWalkRight[frameIndex]
                        : Assets.playerWalkLeft[frameIndex];
                System.out.println(x);
                System.out.println(y);
                break;
            case "climb":
                currentFrame = Assets.playerClimb[frameIndex];
                break;
            case "shooting":
                currentFrame = facingRight
                        ? Assets.playerShoot[frameIndex]
                        : Assets.playerShootLeft[frameIndex];
                break;
            case "dashing":
                currentFrame = facingRight
                        ? Assets.playerDash[frameIndex]
                        : Assets.playerDashLeft[frameIndex];
                break;
            default:
                currentFrame = facingRight
                        ? Assets.playerIdleRight
                        : Assets.playerIdleLeft;
                break;
        }

        g.drawImage(currentFrame, drawX, drawY, null);

        for (Bullet bullet : new ArrayList<>(bullets)) {
            bullet.draw(g);
        }
    }
/// Metoda care permite jucătorului să tragă
    public void shoot() {
        Bullet bullet;
        //speedX = 2;
        shooting = true;
        shooting_timer = shooting_duration;

        if (audioPlayer != null && Game.getInstance().getSettingsMenu().isSoundEnabled()) {
            audioPlayer.playSoundEffect("/sounds/gunshot.wav");
        }

        if (facingRight) {
            bullet = new Bullet(x + 15, y + 15, true, Assets.bullet, Assets.bullet_trace);  // Lansează glonț spre dreapta
        } else {
            bullet = new Bullet(x - 10, y + 15, false, Assets.bullet, Assets.bullet_trace); // Lansează glonț spre stânga
        }
        bullets.add(bullet);
    }
    public void handleMousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3 && !metalJacketActive && metalJacketCooldown == 0) {
            activateMetalJacket();
        }
    }
    public void MetalJacket() {
        Bullet bullet1;

        audioPlayer.playSoundEffect("/sounds/gunshot.wav");

        if (facingRight) {
            bullet1 = new Bullet(x + 15, y + 15, true, Assets.bullet, Assets.bullet_trace);
        } else {
            bullet1 = new Bullet(x - 10, y + 15, false, Assets.bullet, Assets.bullet_trace);
        }
        bullets.add(bullet1);

        // Trage al doilea glonț după 100ms
        Timer timer = new Timer(100, e -> {
            Bullet bullet2;

            audioPlayer.playSoundEffect("/sounds/gunshot.wav");

            if (facingRight) {
                bullet2 = new Bullet(x + 15, y + 15, true, Assets.bullet, Assets.bullet_trace);
            } else {
                bullet2 = new Bullet(x - 10, y + 15, false, Assets.bullet, Assets.bullet_trace);
            }
            bullets.add(bullet2);
        });
        timer.setRepeats(false); // rulează o singură dată
        timer.start();
    }
    public void activateMetalJacket() {
        if (
                metalJacketCooldown == 0 && !metalJacketActive) {
            metalJacketActive = true;
            metalJacketCooldown = metalJacketCooldownMax;
            metalJacketTimer = 0;
            MetalJacket();

        }
    }


//    public void bidirectional_shoot() {
//        Bullet bullet1;
//        Bullet bullet2;
//        if (facingRight) {
//            bullet1 = new Bullet(x + 15, y + 15, true);
//            bullet2 = new Bullet(x - 10, y + 15, false);// Lansează glonț spre dreapta
//        } else {
//            bullet1 = new Bullet(x + 15, y + 15, true);
//            bullet2 = new Bullet(x - 10, y + 15, false); // Lansează glonț spre stânga
//        }
//        bullets.add(bullet1);
//        bullets.add(bullet2);
//    }

    public void activateSuppressiveFire() {
        if (!suppressing && suppressCooldown == 0) {

            suppressing = true;
            suppressTimer = suppressDuration;
            suppressTick = 0;
            suppressFireRight = true;
            suppressCooldown= suppressCooldownMax;
        }
    }



    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    public void removeBullet(Bullet bullet) {
        bullets.remove(bullet);
    }

    public void drawHpBar(Graphics g) {
        int barWidth = 230;
        int barHeight = 25;

        int barOverheat_Width = 200;
        int barOverheat_Height = 10;


        int x = 150;
        int y = 65;

        float hpPercent = Math.max(0, hp) / 100f; // presupunem că hp max e 100

        // Fundal roșu
        g.setColor(Color.ORANGE);
        g.fillRect(150, 98, barOverheat_Width, barOverheat_Height);

        //Imunosupressor fundal gri inchis + chenare
        g.setColor(Color.DARK_GRAY);
        g.fillRect(150, 140, 110, 35);

        g.setColor(Color.RED);
        g.fillRect(x, y, barWidth, barHeight);

        g.setColor(Color.YELLOW);
        g.fillRect(150, 140, 35 * Imunosupressor_Count, 35 );


        // Suprapunere verde (HP actual)
        g.setColor(Color.GREEN);
        g.fillRect(x, y, (int)(barWidth * hpPercent), barHeight);

        // Chenar
        g.setColor(Color.BLACK);
        g.drawRect(x, y, barWidth, barHeight);
        int iconX = 20;
        int iconY = 20;
        g.drawImage(Assets.StatusIcon[0],iconX, iconY, 396, 180, null );
    }

    public void drawSupress(Graphics g){

        int iconX = 20;
        int iconY = 950;
        int iconSize = 80;
        g.drawImage(Assets.SupressiveFireIcon[0], iconX,iconY, iconSize, iconSize, null);
        if(suppressCooldown > 0)
        {
            float ratio = (float) suppressCooldown / suppressCooldownMax;
            int overlay = (int)(iconSize * ratio);
            g.setColor(new Color(0,0,0,150));
            g.fillRect(iconX, iconY + (iconSize - overlay), iconSize, overlay);
        }
    }

    public void drawMetalJacket(Graphics g){
        int iconX = 220;
        int iconY = 950;
        int iconSize = 80;
        g.drawImage(Assets.MetalJacketIcon[0], iconX, iconY, iconSize, iconSize, null);
        if(metalJacketCooldown > 0)
        {
            float ratio = (float) metalJacketCooldown / metalJacketCooldownMax;
            int overlay = (int)(iconSize * ratio);
            g.setColor(new Color(0,0,0,150));
            g.fillRect(iconX, iconY + (iconSize - overlay), iconSize, overlay);
        }
    }

    public void drawDash(Graphics g){
        int iconX = 120;
        int iconY = 947;
        int iconSize = 85;
        g.drawImage(Assets.DashIcon[0], iconX, iconY, iconSize, iconSize + 10, null);
        if(dashCooldown > 0)
        {
            float ratio = (float) dashCooldown / dashCooldownMax;
            int overlay = (int)(iconSize * ratio);
            g.setColor(new Color(0,0,0,150));
            g.fillRect(iconX, iconY + (iconSize - overlay), iconSize, overlay);
        }
    }

    public void drawKeyCard(Graphics g){
        int iconX = 280;
        int iconY = 125;
        if(isKeyCardCollected) {
            g.drawImage(Assets.keycard, iconX, iconY, 43, 31, null);}
    }


    public void applyFallDamage(int damage) {
        hp -= damage;
        if (hp < 0)
            die();
        System.out.println("Fall damage: -" + damage + " HP. Current HP: " + hp);
    }


    public void takeDamage(int i) {
        hp -= i;
    }

    public void die() {
        hp = 0; //de finisat
    }

    public int getHp(){
        return hp;
    }

    public void setPosition(double playerX, double playerY) {
        this.x = (int) playerX;
        this.y = (int) playerY;
    }

    public void setHp(int health) {
        this.hp = health;
    }

    public void increaseScore(int amount) {
        Game.score += amount;
    }

    public void keyCardSetActive(boolean b) {
        isKeyCardCollected = b;
    }

    public boolean isKeyCardActive() {
        return isKeyCardCollected;
    }

    @Override
    public void update(String eventType, Object data) {
        switch (eventType) {
            case "healthChanged":
                // Actualizează sănătatea dacă jocul notifică o schimbare (ex.: power-up sau damage global)
                this.hp = (int) data;
                break;

            case "scoreChanged":
                // Poate declanșa un comportament specific dacă scorul crește (ex.: declanșează un power-up)
                System.out.println("Score notification received in Player: " + data);
                break;

            // Adaugă alte evenimente dacă trebuie să reacționeze la ele.
        }
    }

}