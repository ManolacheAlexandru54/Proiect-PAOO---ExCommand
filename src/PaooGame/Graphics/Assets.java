package PaooGame.Graphics;

import java.awt.image.BufferedImage;

///Clasa Assets reprezintă o colecție de referințe către elementele grafice (dale, sprite-uri, iconițe) utilizate în joc.
/// Aceasta este responsabilă pentru încărcarea și stocarea imaginilor necesare pentru a fi utilizate în diferite aspecte ale jocului, cum ar fi fundalurile, personajele, inamicii și obiectele colectabile.
/// Această clasă oferă o metodă statică Init() care este apelată la începutul jocului pentru a încărca toate resursele grafice necesare.
public class Assets
{
    /// Referinte catre elementele grafice (dale) utilizate in joc.
    //public static BufferedImage background;
    public static BufferedImage Dirt1;
    public static BufferedImage Dirt2;
    public static BufferedImage Dirt3;
    public static BufferedImage Dirt4;
    public static BufferedImage Dirt5;
    public static BufferedImage Dirt6;
    public static BufferedImage Dirt7;
    public static BufferedImage Dirt8;
    public static BufferedImage LightMetal1;
    public static BufferedImage LightMetal2;
    public static BufferedImage LightMetal3;
    public static BufferedImage LightMetal4;
    public static BufferedImage LightMetal5;
    public static BufferedImage LightMetal6;
    public static BufferedImage LightMetal7;
    public static BufferedImage LightMetal8;
    public static BufferedImage DarkMetal1;
    public static BufferedImage DarkMetal2;
    public static BufferedImage DarkMetal3;
    public static BufferedImage DarkMetal4;
    public static BufferedImage DarkMetal5;
    public static BufferedImage DarkMetal6;
    public static BufferedImage DarkMetal7;
    public static BufferedImage DarkMetal8;
    public static BufferedImage Grass1;
    public static BufferedImage Grass2;
    public static BufferedImage Grass3;
    public static BufferedImage Grass4;
    public static BufferedImage Grass5;
    public static BufferedImage Grass6;
    public static BufferedImage Grass7;
    public static BufferedImage Grass8;
    public static BufferedImage ToxinSource;
    public static BufferedImage Toxin1;
    public static BufferedImage Toxin2;
    public static BufferedImage Toxin3;
    public static BufferedImage LavaSource;
    public static BufferedImage Lava1;
    public static BufferedImage Lava2;
    public static BufferedImage Lava3;
    public static BufferedImage Filler1;
    public static BufferedImage Filler2;
    public static BufferedImage Filler3;
    public static BufferedImage Filler4;
    public static BufferedImage Filler5;
    public static BufferedImage Bridge1;
    public static BufferedImage Bridge2;
    public static BufferedImage Bridge3;
    public static BufferedImage Ladder1;
    public static BufferedImage Deco1;
    public static BufferedImage Deco2;
    public static BufferedImage Deco3;
    /// Player
    public static BufferedImage playerIdleRight;
    public static BufferedImage playerIdleLeft;
    public static BufferedImage[] playerWalkRight;
    public static BufferedImage[] playerWalkLeft;
    public static BufferedImage[] playerClimb;
    public static BufferedImage[] playerShoot;
    public static BufferedImage[] playerShootLeft;
    public static BufferedImage[] playerDash;
    public static BufferedImage[] playerDashLeft;
    public static BufferedImage[] playerSupressiveFire;
    public static BufferedImage[] playerMetalJacket;
    public static BufferedImage[] playerMetalJacketLeft;
    /// Icons
    public static BufferedImage[] SupressiveFireIcon;
    public static BufferedImage[] DashIcon;
    public static BufferedImage[] StatusIcon;
    public static BufferedImage[] MetalJacketIcon;
    /// Background
    public static BufferedImage background;
    public static BufferedImage Clouds;
    public static BufferedImage bullet;
    public static BufferedImage Moon;
    public static BufferedImage bullet_trace;
    /// Golem
    public static BufferedImage[] golemWalkRight;
    public static BufferedImage[] golemIdleRight;
    public static BufferedImage[] golemAttackRight;
    /// Imp
    public static BufferedImage impIdle;
    public static BufferedImage[] impAttack;
    public static BufferedImage[] impWalk;
    public static BufferedImage impBullet;
    public static BufferedImage impBulletTrace;
    /// Boss
    public static BufferedImage bossIdle;
    public static BufferedImage[] bossWalk;
    public static BufferedImage[] bossAttack;
    ///Collectables
    public static BufferedImage imunosuppressor;
    public static BufferedImage keycard;

    public static BufferedImage imunosupressor;

    public static BufferedImage npcSprite;
    public static BufferedImage StarBoss;



    /*! \fn public static void Init()
        \brief Functia initializaza referintele catre elementele grafice utilizate.

        Aceasta functie poate fi rescrisa astfel incat elementele grafice incarcate/utilizate
        sa fie parametrizate. Din acest motiv referintele nu sunt finale.
     */
    public static void Init()
    {

        /// Se creaza temporar un obiect SpriteSheet initializat prin intermediul clasei ImageLoader
        SpriteSheet sheet = new SpriteSheet(ImageLoader.LoadImage("/textures/Map/TilesFinal.png"));
        /// Se obtin subimaginile corespunzatoare elementelor necesare.
        bullet = ImageLoader.LoadImage("/textures/Map/bullet.png");
        Moon = ImageLoader.LoadImage("/textures/Map/PlanetaDebian.png");
        Clouds = ImageLoader.LoadImage("/textures/Map/Clouds.png");
        background = ImageLoader.LoadImage("/textures/Map/Munti.png");
        bullet_trace = ImageLoader.LoadImage("/textures/Map/BulletTrace.png");
        impBullet = ImageLoader.LoadImage("/textures/Imp/ImpBullet.png");
        impBulletTrace = ImageLoader.LoadImage("/textures/Imp/ImpBulletTrail.png");
        /// TIle
        Dirt1 = sheet.crop(0,0);
        Dirt2 = sheet.crop(1, 0);
        Dirt3 = sheet.crop(2, 0);
        Dirt4 = sheet.crop(3, 0);
        Dirt5 = sheet.crop(4,0);
        Dirt6 = sheet.crop(5, 0);
        Dirt7 = sheet.crop(6, 0);
        Dirt8 = sheet.crop(7, 0);
        LightMetal1 = sheet.crop(0,1);
        LightMetal2 = sheet.crop(1,1);
        LightMetal3 = sheet.crop(2,1);
        LightMetal4 = sheet.crop(3,1);
        LightMetal5 = sheet.crop(4,1);
        LightMetal6 = sheet.crop(5,1);
        LightMetal7 = sheet.crop(6,1);
        LightMetal8 = sheet.crop(7,1);
        DarkMetal1 = sheet.crop(0,2);
        DarkMetal2 = sheet.crop(1,2);
        DarkMetal3 = sheet.crop(2,2);
        DarkMetal4 = sheet.crop(3,2);
        DarkMetal5 = sheet.crop(4,2);
        DarkMetal6 = sheet.crop(5,2);
        DarkMetal7 = sheet.crop(6,2);
        DarkMetal8 = sheet.crop(7,2);
        Grass1 = sheet.crop(0,3);
        Grass2 = sheet.crop(1, 3);
        Grass3 = sheet.crop(2, 3);
        Grass4 = sheet.crop(3, 3);
        Grass5 = sheet.crop(4,3);
        Grass6 = sheet.crop(5, 3);
        Grass7 = sheet.crop(6, 3);
        Grass8 = sheet.crop(7, 3);
        ToxinSource = sheet.crop(8, 0);
        Toxin1 = sheet.crop(9,0);
        Toxin2 = sheet.crop(10, 0);
        Toxin3 = sheet.crop(11, 0);
        LavaSource = sheet.crop(8,1);
        Lava1 = sheet.crop(9, 1);
        Lava2 = sheet.crop(10, 1);
        Lava3 = sheet.crop(11, 1);
        Filler1 = sheet.crop(0,4);
        Filler2 = sheet.crop(1, 4);
        Filler3 = sheet.crop(2, 4);
        Filler4 = sheet.crop(3, 4);
        Filler5 = sheet.crop(4,4);
        Bridge1 = sheet.crop(5,4);
        Bridge2 = sheet.crop(6, 4);
        Bridge3 = sheet.crop(7, 4);
        Ladder1 = sheet.crop(8,2);
        Deco1 = sheet.crop(8,3);
        Deco2 = sheet.crop(9, 3);
        Deco3 = sheet.crop(10, 3);


        int frameWidth = 30;
        int frameHeight = 36;
        // PlayerSheets
        BufferedImage rightSheet = ImageLoader.LoadImage("/textures/Player/MoveRight.png");
        BufferedImage leftSheet = ImageLoader.LoadImage("/textures/Player/MoveLeft.png");
        BufferedImage IdleSheet = ImageLoader.LoadImage("/textures/Player/Idle.png");
        BufferedImage climbSheet = ImageLoader.LoadImage("/textures/Player/Climb.png");
        BufferedImage ShootSheet = ImageLoader.LoadImage("/textures/Player/AttackMC.png");
        BufferedImage ShootSheetLeft = ImageLoader.LoadImage("/textures/Player/AttackMCLeft.png");
        BufferedImage DashSheet = ImageLoader.LoadImage("/textures/Player/Dash.png");
        BufferedImage DashLeftSheet = ImageLoader.LoadImage("/textures/Player/DashLeft.png");
        BufferedImage SupressiveFireSheet = ImageLoader.LoadImage("/textures/Player/SupressiveFire.png");
        // Golem Sheets
        int frameWidthGolem = 180;
        int frameHeightGolem = 128;
        BufferedImage IdleSheetGolem = ImageLoader.LoadImage("/textures/Golem/Golem_1_idle_x2.png");
        BufferedImage AttackSheetGolem = ImageLoader.LoadImage("/textures/Golem/Golem_1_attack_x2.png");
        BufferedImage WalkSheetGolem = ImageLoader.LoadImage("/textures/Golem/Golem_1_walk_x2.png");
        //Imp Sheets
        BufferedImage IdleSheetImp = ImageLoader.LoadImage("/textures/Imp/ImpIdle.png");
        BufferedImage AttackSheetImp = ImageLoader.LoadImage("/textures/Imp/ImpAttack.png");
        BufferedImage WalkSheetImp = ImageLoader.LoadImage("/textures/Imp/ImpWalk.png");
        //Boss Sheets
        BufferedImage BossIdleSheet = ImageLoader.LoadImage("/textures/Boss/BossIdle.png");
        BufferedImage BossAttackSheet = ImageLoader.LoadImage("/textures/Boss/BossAttack13.png");
        BufferedImage BossWalkSheet = ImageLoader.LoadImage("/textures/Boss/BossWalk7.png");

        //Collectables sheets
        BufferedImage imunosuppressorSheet = ImageLoader.LoadImage("/textures/Collectables/imunosupressor.png");
        BufferedImage keycardSheet = ImageLoader.LoadImage("/textures/Collectables/KeyCard.png");
        BufferedImage metalJacketSheet = ImageLoader.LoadImage("/textures/Player/MetalJacket.png");
        BufferedImage metalJacketSheetLeft = ImageLoader.LoadImage("/textures/Player/MetalJacketLeft.png");

        ///de facut si pentru stanga la golem cand baga manolache gaming sprite uri si pentru stanga

        imunosupressor = ImageLoader.LoadImage("/textures/imunosupressor.png");

        npcSprite = ImageLoader.LoadImage("/textures/bro.png");
        StarBoss = ImageLoader.LoadImage("/textures/StarBoss.png");


// GolemIdle
        golemIdleRight = new BufferedImage[8];

        for (int i = 0; i < 8; i++){
            golemIdleRight[i] = IdleSheetGolem.getSubimage(i*frameWidthGolem,0,frameWidthGolem,frameHeightGolem);
        }

// GolemAttack
        golemAttackRight = new BufferedImage[11];
        for (int i = 0; i < 11; i++){
            golemAttackRight[i] = AttackSheetGolem.getSubimage(i*frameWidthGolem,0,frameWidthGolem,frameHeightGolem);
        }
// GolemWalk
        golemWalkRight = new BufferedImage[10];
        for (int i = 0; i < 10; i++){
            golemWalkRight[i] = WalkSheetGolem.getSubimage(i*180,0,180,128);
        }
//ImpIdle
        impIdle = IdleSheetImp.getSubimage(0,0,34,42);
//ImpWalk
        impWalk = new BufferedImage[6];
        for (int i = 0; i < 6; i++){
            impWalk[i] = WalkSheetImp.getSubimage(i*36,0,36, 50);
        }
//ImpAttack
        impAttack = new BufferedImage[4];
        for (int i = 0; i < 4; i++){
            impAttack[i] = AttackSheetImp.getSubimage(i*66,0,66, 42);
        }
        //BossIdle
        bossIdle = BossIdleSheet.getSubimage(0,0,576,278);
        //BossAttack
        bossAttack = new BufferedImage[13];
        for (int i = 0; i < 13; i++){
            bossAttack[i] = BossAttackSheet.getSubimage(i*576,106,576,278);
        }
        //BossWalk
        bossWalk = new BufferedImage[7];
        for (int i = 0; i < 7; i++){
            bossWalk[i] = BossWalkSheet.getSubimage(i*576,0,576,278);
        }


/// Player
        playerIdleRight = IdleSheet.getSubimage(0, 0, frameWidth, frameHeight);
        playerIdleLeft = IdleSheet.getSubimage(frameWidth, 0, frameWidth, frameHeight);
//Walk 8 frameuri
        playerWalkRight = new BufferedImage[8];
        playerWalkLeft = new BufferedImage[8];

        for (int i = 0; i < 8; i++) {
            playerWalkRight[i] = rightSheet.getSubimage(i * frameWidth, 0, frameWidth, frameHeight);
        }
        playerWalkLeft = new BufferedImage[8];
        for (int i = 0; i < 8; i++) {
            playerWalkLeft[i] = leftSheet.getSubimage(i * frameWidth, 0, frameWidth, frameHeight);
        }//
//Dash
        playerDash = new BufferedImage[2];
        for(int i = 0; i < 2; i ++)
        {
            playerDash[i] = DashSheet.getSubimage(i * (frameWidth + 10), 0, (frameWidth + 2), frameHeight - 2);
        }
        playerDashLeft = new BufferedImage[2];
        for(int i = 0; i < 2; i ++)
        {
            playerDashLeft[i] = DashLeftSheet.getSubimage(i * (frameWidth + 10), 0, (frameWidth + 2), frameHeight - 2);
        }
        playerShootLeft = new BufferedImage[2];




// Shoot 2 frame-uri
        playerShoot = new BufferedImage[2];
        for(int i = 0; i < 2; i ++){
            playerShoot[i] = ShootSheet.getSubimage(i * (frameWidth * 2), 23, frameWidth * 2, frameHeight);
        }
        playerShootLeft = new BufferedImage[2];
        //for(int i = 0; i < 2; i ++){
        playerShootLeft[0] = ShootSheetLeft.getSubimage(0 * frameWidth, 23, frameWidth +7, frameHeight);
        playerShootLeft[1] = ShootSheetLeft.getSubimage(1 * frameWidth + 7, 23, frameWidth +7, frameHeight);
        //}
//SupressiveFire
        int supFrameWidth = SupressiveFireSheet.getWidth() / 11;
        int supFrameHeight = SupressiveFireSheet.getHeight() / 2;

        playerSupressiveFire = new BufferedImage[22];
        int contor = 0;
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 11; col++) {
                playerSupressiveFire[contor++] = SupressiveFireSheet.getSubimage(
                        col * supFrameWidth,
                        row * supFrameHeight,
                        supFrameWidth,
                        supFrameHeight
                );
            }
        }
        //MetalJacket
        playerMetalJacket = new BufferedImage[3];
        for(int i = 0; i < 3; i ++){
            playerMetalJacket[i] = metalJacketSheet.getSubimage(i * 96, 27,96,33);
        }
        playerMetalJacketLeft = new BufferedImage[3];
        for(int i = 0; i < 3; i ++){
            playerMetalJacketLeft[i] = metalJacketSheetLeft.getSubimage(i * 96, 0,96,33);
        }


/// Colectabile
        imunosuppressor = imunosuppressorSheet.getSubimage(0,0,20,54);
        keycard = keycardSheet.getSubimage(0,0,43,31);
/// Iconite Abilitati
        SupressiveFireIcon = new BufferedImage[1];
        SupressiveFireIcon[0] = ImageLoader.LoadImage("/textures/Player/SupressiveFireIcon.png");

        DashIcon = new BufferedImage[1];
        DashIcon[0] = ImageLoader.LoadImage("/textures/Player/DashIcon.png");

        StatusIcon = new BufferedImage[1];
        StatusIcon[0] = ImageLoader.LoadImage("/textures/Player/Icon/StatusBar.png");

        MetalJacketIcon = new BufferedImage[1];
        MetalJacketIcon[0] = ImageLoader.LoadImage("/textures/Player/Icon/MetalJacketIcon.png");


// Climb 2 frameuri
        playerClimb = new BufferedImage[2];
        for (int i = 0; i < 2; i++) {
            playerClimb[i] = climbSheet.getSubimage( i * frameWidth, 0, frameWidth, frameHeight);
        }
    }
}