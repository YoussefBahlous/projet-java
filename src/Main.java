import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


import static javax.swing.WindowConstants.EXIT_ON_CLOSE;


public class Main {
    JFrame displayZoneFrame;
    RenderEngine renderEngine;
    GameEngine gameEngine;
    PhysicEngine physicEngine;
    JPanel startScreen;
    JPanel gameOverScreen;
    Timer gameTimer;
    boolean gameRunning = false;
    private FPS fpsCounter; // Ajout du compteur FPS

    public Main() throws Exception {
        displayZoneFrame = new JFrame("Java Labs");
        displayZoneFrame.setSize(400, 600);
        displayZoneFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        startScreen = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.BLACK);
                g.drawString("Appuyez sur Entrée pour commencer le jeu", 50, 250);
            }
        };
        startScreen.setBackground(Color.LIGHT_GRAY);
        displayZoneFrame.add(startScreen);

        startScreen.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    startGame();
                }
            }
        });
        startScreen.setFocusable(true);
        startScreen.requestFocusInWindow();

        displayZoneFrame.setVisible(true);
    }

    private void startGame() {
        try {
            displayZoneFrame.getContentPane().removeAll();
            displayZoneFrame.revalidate();
            displayZoneFrame.repaint();

            fpsCounter = new FPS(); // Initialisation du compteur FPS

            DynamicSprite hero = new DynamicSprite(200, 300,
                    ImageIO.read(new File("./img/heroTileSheetLowRes.png")), 48, 50);

            renderEngine = new RenderEngine(displayZoneFrame);
            physicEngine = new PhysicEngine();
            gameEngine = new GameEngine(hero);

            // Création d'un JLabel pour afficher le FPS
            JLabel fpsLabel = new JLabel("FPS: 0");
            fpsLabel.setForeground(Color.GREEN);
            fpsLabel.setBounds(10, 10, 100, 20);
            renderEngine.setLayout(null);
            renderEngine.add(fpsLabel);

            Timer renderTimer = new Timer(16, (time) -> { // ~60 FPS (1000ms/60 ≈ 16.67ms)
                renderEngine.update();
                renderEngine.repaint();
                fpsCounter.update();
                fpsLabel.setText("FPS: " + fpsCounter.getFPS());
            });
            gameTimer = new Timer(50, (time) -> gameEngine.update());
            Timer physicTimer = new Timer(50, (time) -> physicEngine.update());

            renderTimer.start();
            gameTimer.start();
            physicTimer.start();

            displayZoneFrame.getContentPane().add(renderEngine);
            displayZoneFrame.setVisible(true);

            Playground level = new Playground("./data/level1.txt");
            renderEngine.addToRenderList(level.getSpriteList());
            renderEngine.addToRenderList(hero);
            physicEngine.addToMovingSpriteList(hero);
            physicEngine.setEnvironment(level.getSolidSpriteList());

            displayZoneFrame.addKeyListener(gameEngine);
            gameRunning = true;

            new Timer(30000, e -> timeOver()).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void timeOver() {
        System.out.println("Time Over appelé");
        if (gameTimer != null) {
            gameTimer.stop();
        }
        gameRunning = false;

        gameOverScreen = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.RED);
                g.drawString("Time Over!", 150, 250);
                g.drawString("Appuyez sur R pour recommencer", 100, 300);
            }
        };
        gameOverScreen.setBackground(Color.BLACK);
        displayZoneFrame.getContentPane().remove(renderEngine);
        displayZoneFrame.getContentPane().add(gameOverScreen);
        displayZoneFrame.revalidate();
        displayZoneFrame.repaint();

        gameOverScreen.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_R) {
                    try {
                        startGame();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        gameOverScreen.setFocusable(true);
        gameOverScreen.requestFocusInWindow();
    }

    public static void main(String[] args) throws Exception {
        Main main = new Main();
    }
}