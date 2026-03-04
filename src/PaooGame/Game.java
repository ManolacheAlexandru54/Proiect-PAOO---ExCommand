package PaooGame;

import PaooGame.Audio.AudioPlayer;
import PaooGame.Entities.*;
import PaooGame.GameStates.*;
import PaooGame.GameWindow.GameWindow;
import PaooGame.Graphics.Assets;
import PaooGame.Graphics.Camera;
import PaooGame.Inputs.KeyboardInputs;
import PaooGame.Inputs.MouseInputs;
import PaooGame.Maps.Map1;
import PaooGame.Maps.Map2;
import PaooGame.Maps.Map3;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Iterator;

import static PaooGame.Tiles.Tile.InitTiles;

/*! \class Game
    \brief Clasa principala a intregului proiect. Implementeaza Game - Loop (Update -> Draw)

                ------------
                |           |
                |     ------------
    60 times/s  |     |  Update  |  -->{ actualizeaza variabile, stari, pozitii ale elementelor grafice etc.
        =       |     ------------
     16.7 ms    |           |
                |     ------------
                |     |   Draw   |  -->{ deseneaza totul pe ecran
                |     ------------
                |           |
                -------------
    Implementeaza interfata Runnable:

        public interface Runnable {
            public void run();
        }

    Interfata este utilizata pentru a crea un nou fir de executie avand ca argument clasa Game.
    Clasa Game trebuie sa aiba definita metoda "public void run()", metoda ce va fi apelata
    in noul thread(fir de executie). Mai multe explicatii veti primi la curs.

    In mod obisnuit aceasta clasa trebuie sa contina urmatoarele:
        - public Game();            //constructor
        - private void init();      //metoda privata de initializare
        - private void update();    //metoda privata de actualizare a elementelor jocului
        - private void draw();      //metoda privata de desenare a tablei de joc
        - public run();             //metoda publica ce va fi apelata de noul fir de executie
        - public synchronized void start(); //metoda publica de pornire a jocului
        - public synchronized void stop()   //metoda publica de oprire a jocului
 */
public class Game implements Runnable
{
    private GameWindow      wnd;        /*!< Fereastra in care se va desena tabla jocului*/
    private boolean         runState;   /*!< Flag ce starea firului de executie.*/
    private Thread          gameThread; /*!< Referinta catre thread-ul de update si draw al ferestrei*/
    private BufferStrategy  bs;         /*!< Referinta catre un mecanism cu care se organizeaza memoria complexa pentru un canvas.*/
    private boolean escPressed = false;
    /// Sunt cateva tipuri de "complex buffer strategies", scopul fiind acela de a elimina fenomenul de
    /// flickering (palpaire) a ferestrei.
    /// Modul in care va fi implementata aceasta strategie in cadrul proiectului curent va fi triplu buffer-at

    ///                         |------------------------------------------------>|
    ///                         |                                                 |
    ///                 ****************          *****************        ***************
    ///                 *              *   Show   *               *        *             *
    /// [ //Ecran ] <---- * Front Buffer *  <------ * Middle Buffer * <----- * Back Buffer * <---- Draw()
    ///                 *              *          *               *        *             *
    ///                 ****************          *****************        ***************

    private Graphics        g;          /*!< Referinta catre un context grafic.*/

    private Map1 map1;
    private Map2 map2;
    private Map3 map3;
    public static int currentMap = 1;
    private PauseMenu pauseMenu;
    private DeathMenu deathMenu;
    private SettingsMenu settingsMenu;
    private KeyboardInputs keyboardInputs;
    private MouseInputs mouseInputs;
    public static Player player;
    public static CollisionManager collisionManager1;
    public static CollisionManager collisionManager2;
    public static CollisionManager collisionManager3;
    private MainMenu mainMenu;
    private WinMenu winMenu;
    private GameState gameState;
    private boolean gameActive = false;
    private StarBoss star;

    private Rectangle exitZone;
    private Rectangle exitZone2;
    private static Game instance;
    public static ArrayList<Bullet> enemyBullets = new ArrayList<>();

    public WinMenu getWinMenu() {
        return winMenu;
    }

    public int getScore() {
        return score;
    }

    public long getElapsedTime() {
        return timeScore;
    }


    private enum GameStatus {
        MAIN_MENU,
        PLAYING
    }

    public static int getHealth()
    {
        return player.getHp();
    }

    private GameStatus currentState = GameStatus.MAIN_MENU;
    private Camera camera; /// pentru mapa 1
    private Camera camera2; /// pentru mapa 2
    private Camera camera3; /// pentru mapa 3
    static ArrayList<Enemy> enemiesMap1;
    static ArrayList<Enemy> enemiesMap3;
    private Bro npc;


    static ArrayList<Imunosupressor> imunosupressorsMap1;
    static ArrayList<Imunosupressor> imunosupressorsMap3;

    static KeyCard keyCard;

    public static long startTime;   // Timpul de start al jocului
    public static long timeScore;
    public static int score;        // Scorul jucătorului

    private AudioPlayer backgroundMusicPlayer;




    /*! \fn public Game(String title, int width, int height)
        \brief Constructor de initializare al clasei Game.

        Acest constructor primeste ca parametri titlul ferestrei, latimea si inaltimea
        acesteia avand in vedere ca fereastra va fi construita/creata in cadrul clasei Game.

        \param title Titlul ferestrei.
        \param width Latimea ferestrei in pixeli.
        \param height Inaltimea ferestrei in pixeli.
     */
    public Game(String title, int width, int height) {
        instance = this;
        wnd = new GameWindow(title, width, height);
        runState = false;
        Assets.Init();
        InitTiles();

        DatabaseManager.initializeDatabase();

        map1 = new Map1(wnd); // Transmitem GameWindow la construcție
        map2 = new Map2(wnd); // Transmitem GameWindow la construcție
        map3 = new Map3(wnd);
        int mapPixelWidth1 = map1.getWidthInTiles() * 32;
        int mapPixelHeight1 = map1.getHeightInTiles() * 32;

        int mapPixelWidth2 = map2.getWidthInTiles() * 32;
        int mapPixelHeight2 = map2.getHeightInTiles() * 32;

        int mapPixelWindth3 = map3.getWidthInTiles() * 32;
        int mapPixelHeight3 = map3.getHeightInTiles() * 32;

        pauseMenu = new PauseMenu(wnd);
        mainMenu = new MainMenu(wnd,this);
        winMenu = new WinMenu(wnd,this);

        player = new Player(320,1000);
        collisionManager1 = new CollisionManager(map1.getTileMap());
        collisionManager2 = new CollisionManager(map2.getTileMap());
        collisionManager3 = new CollisionManager(map3.getTileMap());

        backgroundMusicPlayer = new AudioPlayer();
        backgroundMusicPlayer.playBackgroundMusic("/sounds/mama.wav"); // Pornește muzica jocului

        settingsMenu = new SettingsMenu(wnd, backgroundMusicPlayer);
        pauseMenu.setSettingsMenu(settingsMenu);
        mainMenu.setSettingsMenu(settingsMenu);
        pauseMenu.setMainMenu(mainMenu);
        settingsMenu.setMainMenu(mainMenu);
        settingsMenu.setPauseMenu(pauseMenu);
        deathMenu = new DeathMenu(wnd, this);
        gameState = new GameState("Player", 0, 0, "Level 1", 100, true, player.getX(), player.getY(), currentMap);

        gameState.addObserver(player);

        camera = new Camera(player, width, height, mapPixelWidth1, mapPixelHeight1);
        camera2 = new Camera(player, 1920, 1080, mapPixelWidth2, mapPixelHeight2);
        camera3 = new Camera(player, width, height, mapPixelWindth3, mapPixelHeight3);

        keyboardInputs = new KeyboardInputs(wnd);
        keyboardInputs.setPlayer(player);
        keyboardInputs.setGame(this);
        mouseInputs = new MouseInputs();
        mouseInputs.setWindow(wnd);
        mouseInputs.setMenu(pauseMenu);
        mouseInputs.setSettingsMenu(settingsMenu);
        mouseInputs.setPlayer(player);

        wnd.BuildGameWindow();
        wnd.GetCanvas().addMouseListener(mouseInputs);
        wnd.GetCanvas().addMouseMotionListener(mouseInputs);

        enemiesMap1 = new ArrayList<>();
        enemiesMap3 = new ArrayList<>();
        enemiesMap3.add(new Boss(145*32, 43 * 32));
        //Imp
        enemiesMap1.add(new Imp(2144, 768));
        enemiesMap1.add(new Imp(1664, 992));
        enemiesMap1.add(new Imp(2016, 1120));
        enemiesMap1.add(new Imp(2592, 1024));
        enemiesMap1.add(new Imp(4224, 1152));
        enemiesMap1.add(new Imp(5184, 896));
        enemiesMap1.add(new Imp(6144, 768));
        enemiesMap1.add(new Imp(4864, 160));
        enemiesMap1.add(new Imp(5568, 320));
        enemiesMap1.add(new Imp(5888, 224));
        enemiesMap1.add(new Imp(6048, 1152));
        enemiesMap1.add(new Imp(5632, 1152));
        //Golemii
        enemiesMap1.add(new Golem(2816, 1120));
        enemiesMap1.add(new Golem(3584, 1120));
        enemiesMap1.add(new Golem(5600, 540));
        enemiesMap1.add(new Golem(2048, 416));
        enemiesMap1.add(new Golem(1792, 300));



        //enemiesMap1.add(new Imp(400, 1100));
        star = new StarBoss(1120, 300); // centru sus
        //enemiesMap2.add(new )

        //Imunosupressor spawn
        imunosupressorsMap1 = new ArrayList<>();
        imunosupressorsMap3 = new ArrayList<>();
        imunosupressorsMap1.add(new Imunosupressor(576, 320));
        imunosupressorsMap1.add(new Imunosupressor(1792, 640));
        imunosupressorsMap1.add(new Imunosupressor(2624, 1024));
        imunosupressorsMap1.add(new Imunosupressor(4256, 352));
        imunosupressorsMap1.add(new Imunosupressor(5216, 896));
        imunosupressorsMap1.add(new Imunosupressor(32, 704));
        //keycard
        keyCard = new KeyCard(480, 256);

        exitZone = new Rectangle(6272, 352, 64, 64);
        exitZone2 = new Rectangle(67 * 32, 29 * 32, 128, 128);

        npc = new Bro(834, 990, "Use W, A, S, D, SPACE and SHIFT to move around.\n Defeat all the enemies with the abilities in your arsenal and collect the keycard. Good luck! \n"
        +"And remember do, NOT use $rm -rf / !!!");
    }

    /*! \fn private void init()
        \brief  Metoda construieste fereastra jocului, initializeaza aseturile, listenerul de tastatura etc.

        Fereastra jocului va fi construita prin apelul functiei BuildGameWindow();
        Sunt construite elementele grafice (assets): dale, player, elemente active si pasive.

     */
    /*! \fn public void run()
        \brief Functia ce va rula in thread-ul creat.

        Aceasta functie va actualiza starea jocului si va redesena tabla de joc (va actualiza fereastra grafica)
     */
    public void InitGame(){
        //MouseInputs mouseInputs = new MouseInputs();
        wnd.GetCanvas().addMouseListener(mouseInputs);
        wnd.GetCanvas().addMouseMotionListener(mouseInputs);
        wnd.GetCanvas().addKeyListener(keyboardInputs);
        wnd.GetCanvas().setFocusable(true);
        wnd.GetCanvas().requestFocus();


        gameState.setHealth(100);

    }
    // public void PaintComponent
    public void run()
    {
        InitGame();
        /// Initializeaza obiectul game
        long oldTime = System.nanoTime();   /*!< Retine timpul in nanosecunde aferent frame-ului anterior.*/
        long currentTime;                    /*!< Retine timpul curent de executie.*/

        /// Apelul functiilor Update() & Draw() trebuie realizat la fiecare 16.7 ms
        /// sau mai bine spus de 60 ori pe secunda.

        final int framesPerSecond   = 60; /*!< Constanta intreaga initializata cu numarul de frame-uri pe secunda.*/
        final double timeFrame      = 1000000000 / framesPerSecond; /*!< Durata unui frame in nanosecunde.*/

        /// Atat timp timp cat threadul este pornit Update() & Draw()
        while (runState == true)
        {
            /// Se obtine timpul curent
            currentTime = System.nanoTime();
            if((currentTime - oldTime) > timeFrame) {
                Update();
                Draw();
                oldTime = currentTime;
            }
        }

    }

    /*! \fn public synchronized void start()
        \brief Creaza si starteaza firul separat de executie (thread).

        Metoda trebuie sa fie declarata synchronized pentru ca apelul acesteia sa fie semaforizat.
     */
    public synchronized void StartGame()
    {
        if (runState == false) {
            runState = true;
            gameThread = new Thread(this);
            gameThread.start();

            // Setăm startTime când jocul începe
            //startTime = System.currentTimeMillis();
            score = 0; // Resetează scorul la începutul jocului
        }
        else
        {
            /// Thread-ul este creat si pornit deja
            return;
        }
    }

    /*! \fn public synchronized void stop()
        \brief Opreste executie thread-ului.

        Metoda trebuie sa fie declarata synchronized pentru ca apelul acesteia sa fie semaforizat.
     */
    public synchronized void StopGame()
    {
        if(runState == true)
        {
            /// Actualizare stare thread
            runState = false;
            /// Metoda join() arunca exceptii motiv pentru care trebuie incadrata intr-un block try - catch.
            try
            {
                /// Metoda join() pune un thread in asteptare panca cand un altul isi termina executie.
                /// Totusi, in situatia de fata efectul apelului este de oprire a threadului.
                gameThread.join();
            }
            catch(InterruptedException ex)
            {
                /// In situatia in care apare o exceptie pe ecran vor fi afisate informatii utile pentru depanare.
                ex.printStackTrace();
            }
        }
        else
        {
            /// Thread-ul este oprit deja.
            return;
        }


    }

    /*! \fn private void Update()
        \brief Actualizeaza starea elementelor din joc.

        Metoda este declarata privat deoarece trebuie apelata doar in metoda run()
     */
    private void Update() {
        boolean[] keys = keyboardInputs.getKeys();

        // Handle ESC key for pause menu (only when game is active)
        if (keys[KeyEvent.VK_ESCAPE] && !escPressed && gameActive) {
            if(settingsMenu.isVisible()) {
                settingsMenu.toggle();
            }
            else {
                pauseMenu.toggle();
            }
            if (pauseMenu.isVisible()) {
                backgroundMusicPlayer.stopBackgroundMusic();
            } else {
                backgroundMusicPlayer.resumeBackgroundMusic();
            }


            escPressed = true;
        }
        else if (!keys[KeyEvent.VK_ESCAPE]) {
            escPressed = false;
        }
        if (mainMenu.isVisible()) {
            gameActive = false;
            currentState = GameStatus.MAIN_MENU;
        }



        // Handle main menu input
        if (mainMenu.isVisible()) {
            if (keys[KeyEvent.VK_UP]) {
                mainMenu.navigateUp();
                keyboardInputs.resetKey(KeyEvent.VK_UP);
            }
            else if (keys[KeyEvent.VK_DOWN]) {
                mainMenu.navigateDown();
                keyboardInputs.resetKey(KeyEvent.VK_DOWN);
            }
            else if (keys[KeyEvent.VK_ENTER]) {
                mainMenu.selectOption();
                if (!mainMenu.isVisible()) {
                    gameActive = true;  // Game starts when main menu disappears
                    currentState = GameStatus.PLAYING;

                    startTime = System.currentTimeMillis();

                }
                keyboardInputs.resetKey(KeyEvent.VK_ENTER);
            }
        }
        else if (pauseMenu.isVisible()) {
            if (keys[KeyEvent.VK_UP]) {
                pauseMenu.navigateUp();
                keyboardInputs.resetKey(KeyEvent.VK_UP);
            }
            else if (keys[KeyEvent.VK_DOWN]) {
                pauseMenu.navigateDown();
                keyboardInputs.resetKey(KeyEvent.VK_DOWN);
            }
            else if (keys[KeyEvent.VK_ENTER]) {
                pauseMenu.selectOption();
                if (!pauseMenu.isVisible() && !settingsMenu.isVisible()) {
                    gameActive = true;
                }
                keyboardInputs.resetKey(KeyEvent.VK_ENTER);
            }
        }
        else if (settingsMenu.isVisible()) {
            if (keys[KeyEvent.VK_ENTER] && settingsMenu.getSelectedOption() == 4) {
                settingsMenu.toggle();
                if(!pauseMenu.isVisible()) {
                    pauseMenu.toggle();
                }
            }
            if (keys[KeyEvent.VK_UP]) {
                settingsMenu.navigateUp();
                keyboardInputs.resetKey(KeyEvent.VK_UP);
            }
            else if (keys[KeyEvent.VK_DOWN]) {
                settingsMenu.navigateDown();
                keyboardInputs.resetKey(KeyEvent.VK_DOWN);
            }
            else if (keys[KeyEvent.VK_ENTER]) {
                settingsMenu.selectOption();
                keyboardInputs.resetKey(KeyEvent.VK_ENTER);
            }
            else if (keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_RIGHT]) {
                keyboardInputs.resetKey(KeyEvent.VK_LEFT);
                keyboardInputs.resetKey(KeyEvent.VK_RIGHT);
            }
        }

        // Update game elements only when game is active and no menus are visible

        if (gameActive && currentState == GameStatus.PLAYING
                && !pauseMenu.isVisible() && !settingsMenu.isVisible() && !mainMenu.isVisible() && !deathMenu.isVisible()) {
            player.update(keyboardInputs.getKeys());

            if (currentMap == 1) {
                camera.update();
                collisionManager1.checkEntityCollision(player);
                collisionManager1.checkEntityHazardCollision(player);
                collisionManager1.checkPlayerEnemyCollision(player, enemiesMap1);

                boolean fKeyPressed = keys[KeyEvent.VK_F];
                npc.checkPlayerCollision(player.getBounds(), fKeyPressed);


                collisionManager1.checkImunosupressorCollision(player, imunosupressorsMap1);
                collisionManager1.checkKeyCardCollision(player, keyCard);
                collisionManager1.checkBulletHitsPlayer(player, Game.enemyBullets);

                ArrayList<Bullet> bullets = player.getBullets();




                Iterator<Enemy> enemyIterator = enemiesMap1.iterator();
                while (enemyIterator.hasNext()) {
                    Enemy enemy = enemyIterator.next();
                    if (enemy.getHp() <= 0) { // Dacă viața este zero sau mai mică
                        enemy.setAlive(false); // Setezi alive pe false
                        enemy.setHp(0);        // Să te asiguri că viața este zero
                    } else {
                        enemy.update(); // Menții actualizarea numai pentru cei "vii"
                    }

                }

                for (int i = 0; i < bullets.size(); i++) {
                    Bullet bullet = bullets.get(i);
                    bullet.update();
                    collisionManager1.checkBulletCollisions(bullet);
                    collisionManager1.checkBulletHitsEnemies(enemiesMap1, bullet);

                    if (!bullet.isActive()) {
                        bullets.remove(i);
                        i--; // IMPORTANT: scazi indexul ca să nu sari peste un element
                    }
                }
                boolean allDead = true;
                for(Enemy e : enemiesMap1) {
                    if (e.isAlive()) {
                        allDead = false;
                    }
                }

                if (player.isKeyCardActive() &&
                        allDead &&
                        player.getBounds().intersects(exitZone)) {

                    System.out.println("Toate condițiile îndeplinite - trecere la nivelul 2!");
                    transitionToNextLevel(2);
                }

                for (int i = 0; i < Game.enemyBullets.size(); i++) {
                    Bullet b = Game.enemyBullets.get(i);
                    b.update();
                    collisionManager1.checkBulletCollisions(b);

                    if (!b.isActive()) {
                        Game.enemyBullets.remove(i);
                        i--;
                    }
                }


            } else if(currentMap == 2){
                camera2.update();
                collisionManager2.checkEntityCollision(player);
                collisionManager2.checkEntityHazardCollision(player); // ← AICI
                collisionManager2.checkKeyCardCollision(player, keyCard);

                ArrayList<Bullet> bullets = player.getBullets();
                for (int i = 0; i < bullets.size(); i++) {
                    Bullet bullet = bullets.get(i);
                    bullet.update();
                    collisionManager2.checkBulletCollisions(bullet);
                    collisionManager2.checkBulletHitsEnemies(enemiesMap1, bullet);

                    if (!bullet.isActive()) {
                        bullets.remove(i);
                        i--;
                    }
                }
                player.keyCardSetActive(false);
                if (star.isFinished()) {
                    // Afișează rectangle-ul pentru tranzit (dreapta jos)
                    if (player.getBounds().intersects(exitZone2)) {
                        System.out.println("All conditions met. Transitioning to level 3...");
                        transitionToNextLevel(3);
                    } else {
                        System.out.println("Boss defeated! Head to the exit zone to transition.");
                    }
                }




            } else {
                camera3.update();
                collisionManager3.checkEntityCollision(player);
                collisionManager3.checkEntityHazardCollision(player);
                collisionManager3.checkImunosupressorCollision(player, imunosupressorsMap3);
                collisionManager3.checkPlayerEnemyCollision(player, enemiesMap3);

                Iterator<Enemy> enemyIterator = enemiesMap3.iterator();
                while (enemyIterator.hasNext()) {
                    Enemy enemy = enemyIterator.next();
                    if (enemy.getHp() <= 0) { // Dacă viața este zero sau mai mică
                        enemy.setAlive(false); // Setezi alive pe false
                        enemy.setHp(0);        // Să te asiguri că viața este zero
                    } else {
                        enemy.update(); // Menții actualizarea numai pentru cei "vii"
                    }

                }

                ArrayList<Bullet> bullets = player.getBullets();
                for (int i = 0; i < bullets.size(); i++) {
                    Bullet bullet = bullets.get(i);
                    bullet.update();
                    collisionManager3.checkBulletCollisions(bullet);
                    collisionManager3.checkBulletHitsEnemies(enemiesMap3, bullet);

                    if (!bullet.isActive()) {
                        bullets.remove(i);
                        i--;
                    }
                }
                boolean allEnemiesDefeated = enemiesMap3.stream().noneMatch(Enemy::isAlive);
                if (allEnemiesDefeated) {
                    long endTime = System.currentTimeMillis();
                    Game.timeScore = startTime - endTime;
                    winMenu.setVisible(true);
                    gameActive = false; // Oprirea jocului
                }


            }
        }



        if(currentMap == 2) {
            star.update(System.currentTimeMillis(), player);

            for (LaserBeam l : star.getLasers()) {
                if (l.isActive() && l.intersects(player.getBounds())) {
                    player.die();
                }
            }
        }

        if (player.getHp() <= 0 && !deathMenu.isVisible()) {
            deathMenu.setVisible(true);
        }



    }

    /*! \fn private void Draw()
        \brief Deseneaza elementele grafice in fereastra coresponzator starilor actualizate ale elementelor.

        Metoda este declarata privat deoarece trebuie apelata doar in metoda run()
     */

    private void Draw() {
        bs = wnd.GetCanvas().getBufferStrategy();
        if (bs == null) {
            wnd.GetCanvas().createBufferStrategy(3);
            return;
        }

        g = bs.getDrawGraphics();
        g.clearRect(0, 0, wnd.GetWndWidth(), wnd.GetWndHeight());

        Graphics2D g2d = (Graphics2D)g;

        // 1. Salvăm transformarea originală
        AffineTransform oldTransform = g2d.getTransform();

        // 2. Aplicăm transformarea camerei pentru harta și player
        if (currentMap == 1) {
            g2d.translate(-camera.getX(), -camera.getY());
            g.drawImage(Assets.background, (int)(camera.getX()), (int)(camera.getY()), 1920, 1240, null);
            g.drawImage(Assets.Moon, (int)(camera.getX() * 0.9), (int)(camera.getY() - 100), 1920, 1080, null);
            g.drawImage(Assets.Clouds, (int)(camera.getX() * 0.75), (int)(camera.getY()), 1920, 1240, null);
            if(!keyCard.isCollected())
            {
                keyCard.draw(g);
            }
            boolean allDead = true;
            for(Enemy e : enemiesMap1) {
                if (e.isAlive()) {
                    allDead = false;
                }
            }
            if (player.isKeyCardActive() && allDead){
                // Verde când toate condițiile sunt îndeplinite
                g.setColor(new Color(0, 255, 0, 100));
            } else {
                // Roșu când mai sunt condiții de îndeplinit
                g.setColor(new Color(255, 0, 0, 100));
            }
            g.fillRect(exitZone.x, exitZone.y, exitZone.width, exitZone.height);

            // Desenează NPC-ul
            npc.draw(g);  // pentru a desena NPC-ul
            npc.drawMessage(g);


            for(Imunosupressor i : imunosupressorsMap1) {
                i.draw(g);
            }
            map1.Draw(g);
        } else if (currentMap == 2) {
            g2d.translate(-camera2.getX(), -camera2.getY());

            g.drawImage(Assets.background, (int)(camera2.getX()), (int)(camera2.getY()), 1920, 1240, null);
            g.drawImage(Assets.Moon, (int)(camera2.getX() * 0.9), (int)(camera2.getY() - 100), 1920, 1080, null);
            g.drawImage(Assets.Clouds, (int)(camera2.getX() * 0.75), (int)(camera2.getY()), 1920, 1240, null);

            if (star.isFinished()) {
                g.setColor(new Color(0, 255, 0, 100)); // Verde semi-transparent
            } else
            {
                g.setColor(new Color(255, 0, 0, 100));

            }
            g.fillRect(exitZone2.x, exitZone2.y, exitZone2.width, exitZone2.height);

            map2.Draw(g);
        }
        else if(currentMap == 3){
            g2d.translate(-camera3.getX(), -camera3.getY());
            g.drawImage(Assets.background, (int)(camera3.getX()), (int)(camera3.getY()), 1920, 1240, null);
            g.drawImage(Assets.Moon, (int)(camera3.getX() * 0.9), (int)(camera3.getY() - 100), 1920, 1080, null);
            g.drawImage(Assets.Clouds, (int)(camera3.getX() * 0.75), (int)(camera3.getY()), 1920, 1240, null);
            for(Imunosupressor i : imunosupressorsMap3){
                i.draw(g);
            }
            map3.Draw(g);
        }

        // 3. Desenăm harta și playerul

        if (gameActive) {
            player.draw(g);
            for (Bullet b : Game.enemyBullets) {
                b.draw(g);
            }

        }
        if(currentMap == 2)
            star.draw(g);

        if ((currentMap == 1)) {
            for (Enemy e : enemiesMap1) {
                if (e.isAlive()) {
                    e.draw(g);
                }
            }
        } else if (currentMap == 3) {
            for (Enemy e : enemiesMap3) {
                if (e.isAlive()) {
                    e.draw(g);
                }
            }
        }


        // 4. Restaurăm transformarea pentru meniuri (nu vrem să fie afectate de cameră)
        g2d.setTransform(oldTransform);

        if(gameActive){
            player.drawHpBar(g);
            if(player.isKeyCardActive()) {
                player.drawKeyCard(g);
            }
            player.drawSupress(g);
            player.drawDash(g);
            player.drawKeyCard(g);
            player.drawMetalJacket(g);

            // Desenează iconița KeyCard în UI
            //player.drawKeyCard(g);

        }



        // 5. Desenăm meniurile
        if (mainMenu.isVisible()) {
            mainMenu.draw(g);
            //System.out.println("desenez main menu");
        }
        if (pauseMenu.isVisible() || settingsMenu.isVisible()) {
            // fundal semi-transparent pe tot ecranul
            g.setColor(new Color(0, 0, 0, 180));
            g.fillRect(0, 0, wnd.GetWndWidth(), wnd.GetWndHeight());

            if(pauseMenu.isVisible()) {
                if (mainMenu.isVisible()) {
                    mainMenu.draw(g);
                }
            }

            if (pauseMenu.isVisible()) {
                pauseMenu.draw(g); // acum e desenat pe centrul ecranului
            }
            if (settingsMenu.isVisible()) {
                settingsMenu.draw(g);
            }
        }

        if (deathMenu.isVisible()) {
            deathMenu.draw(g);
        }

        if (winMenu.isVisible()) {
            winMenu.draw(g);
        }


        bs.show();
        g.dispose();
    }



    public int getCurrentMap() {
        return currentMap;
    }

    public void setCurrentMap(int map) {
        currentMap = map;
    }

    public static ArrayList<Enemy> getEnemies1() {
        return enemiesMap1;
    }

    public static ArrayList<Enemy> getEnemies3() {
        return enemiesMap3;
    }

    public static void setEnemies1(ArrayList<Enemy> enemies1) {
        enemiesMap1 = enemies1;
    }

    public static void setEnemies3(ArrayList<Enemy> enemies3) {
        enemiesMap3 = enemies3;
    }

    public DeathMenu getDeathMenu() {
        return deathMenu;
    }

    public void restartCurrentLevel() {
    // Resetare player
    player.setHp(100);
    player.setPosition(300, 1000); // Exemplu poziție de start

    // Resetează lista actuală de inamici folosind starea inițială
    //enemiesMap1.clear();
    for(Enemy enemy : enemiesMap1) {
            enemy.setAlive(true); // Reînviem inamicii
        if (enemy instanceof Imp) {
            enemy.setHp(50); // Exemplu: Imp are 50 HP
        } else if (enemy instanceof Golem) {
            enemy.setHp(150); // Exemplu: Golem are 150 HP
        }

    }

    // Resetează alte elemente (dacă e necesar)
    //startTime = System.currentTimeMillis(); // Resetare timp
    gameActive = true; // Jocul este activ
    deathMenu.setVisible(false); // Ascunde meniul de moarte
}


    public MainMenu getMainMenu() {
        return mainMenu;
    }

    public void setGameActive(boolean active) {
        this.gameActive = active;
    }

    private void transitionToNextLevel(int level) {
        if (level == 2 ) {
            System.out.println("Tranziție la nivelul 2!");
            currentMap = 2;
            player.setPosition(200, 900);
            star.reset();
        }
        else if (level == 3){
            System.out.println("Tranziție la nivelul 3!");
            currentMap = 3;
            player.setPosition(200, 1500);
            enemiesMap3.clear();
        }
    }

    public static Game getInstance(){
        return instance;
    }

    public SettingsMenu getSettingsMenu() {return settingsMenu;}



}