import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    private final int UNIT_SIZE;
    private final int SCREEN_WIDTH;
    private final int SCREEN_HEIGHT;
    private final int GAME_UNITS;
    private final int[] x;
    private final int[] y;
    
    private int bodyParts;
    private int applesEaten;
    private int appleX;
    private int appleY;
    private char direction = 'R';
    private boolean running = false;
    private Timer timer;
    private Random random;
    private GameSettings settings;
    private boolean isPaused = false;
    private long foodSpawnTime;
    private static final long SPECIAL_FOOD_DURATION = 5000; // 5 seconds

    public GamePanel(GameSettings settings) {
        this.settings = settings;
        this.UNIT_SIZE = settings.getUnitSize();
        this.SCREEN_WIDTH = settings.getGridSize().width;
        this.SCREEN_HEIGHT = settings.getGridSize().height;
        this.GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
        this.x = new int[GAME_UNITS];
        this.y = new int[GAME_UNITS];
        this.bodyParts = settings.getInitialLength();
        
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(settings.getBackgroundColor());
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        bodyParts = settings.getInitialLength();
        applesEaten = 0;
        direction = 'R';
        // Initialize snake position
        for(int i = 0; i < bodyParts; i++) {
            x[i] = SCREEN_WIDTH/2 - i * UNIT_SIZE;
            y[i] = SCREEN_HEIGHT/2;
        }
        spawnNewFood();
        running = true;
        timer = new Timer(settings.getGameSpeed(), this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                            RenderingHints.VALUE_ANTIALIAS_ON);
        draw(g2d);
    }

    private void draw(Graphics2D g) {
        if(running) {
            // Draw grid if enabled
            if(settings.isGridLines()) {
                drawGrid(g);
            }
            
            // Draw food
            drawFood(g);
            
            // Draw snake
            drawSnake(g);
            
            // Draw score
            drawScore(g);
            
            // Draw pause indicator if paused
            if(isPaused) {
                drawPauseScreen(g);
            }
        } else {
            drawGameOver(g);
        }
    }

    private void drawGrid(Graphics2D g) {
        g.setColor(settings.getGridColor());
        for(int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++) {
            g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
            g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
        }
    }

    private void drawSnake(Graphics2D g) {
        for(int i = 0; i < bodyParts; i++) {
            if(i == 0) {
                g.setColor(settings.getSnakeColor());
            } else {
                g.setColor(settings.getSnakeColor().darker());
            }
            
            switch(settings.getSnakeShape()) {
                case "Circle":
                    g.fillOval(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                    break;
                case "Triangle":
                    int[] xPoints = {x[i], x[i] + UNIT_SIZE/2, x[i] + UNIT_SIZE};
                    int[] yPoints = {y[i] + UNIT_SIZE, y[i], y[i] + UNIT_SIZE};
                    g.fillPolygon(xPoints, yPoints, 3);
                    break;
                default: // Rectangle
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                    break;
            }
        }
    }

    private void drawFood(Graphics2D g) {
        g.setColor(settings.getFoodColor());
        
        switch(settings.getFoodShape()) {
            case "Star":
                drawStar(g, appleX, appleY);
                break;
            case "Apple":
                drawApple(g, appleX, appleY);
                break;
            default: // Circle
                g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
                break;
        }
    }

    private void drawStar(Graphics2D g, int x, int y) {
        int[] xPoints = new int[10];
        int[] yPoints = new int[10];
        int radius = UNIT_SIZE/2;
        int innerRadius = radius/2;
        
        for(int i = 0; i < 10; i++) {
            double angle = Math.PI/2 + i * Math.PI/5;
            int r = (i % 2 == 0) ? radius : innerRadius;
            xPoints[i] = x + UNIT_SIZE/2 + (int)(r * Math.cos(angle));
            yPoints[i] = y + UNIT_SIZE/2 - (int)(r * Math.sin(angle));
        }
        g.fillPolygon(xPoints, yPoints, 10);
    }

    private void drawApple(Graphics2D g, int x, int y) {
        // Main apple body
        g.setColor(settings.getFoodColor());
        g.fillOval(x, y + UNIT_SIZE/6, UNIT_SIZE, UNIT_SIZE*2/3);
        
        // Stem
        g.setColor(new Color(139, 69, 19));
        g.fillRect(x + UNIT_SIZE/2 - 2, y, 4, UNIT_SIZE/4);
        
        // Leaf
        g.setColor(Color.GREEN);
        int[] leafX = {x + UNIT_SIZE/2, x + UNIT_SIZE/2 + UNIT_SIZE/4, x + UNIT_SIZE/2};
        int[] leafY = {y + UNIT_SIZE/4, y + UNIT_SIZE/8, y};
        g.fillPolygon(leafX, leafY, 3);
    }

    private void drawScore(Graphics2D g) {
        g.setColor(settings.getTextColor());
        g.setFont(new Font("Arial", Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        String scoreText = "Score: " + applesEaten;
        g.drawString(scoreText, 
            (SCREEN_WIDTH - metrics.stringWidth(scoreText))/2, 
            g.getFont().getSize());
    }

    private void drawPauseScreen(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 128));
        g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 60));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("PAUSED", 
            (SCREEN_WIDTH - metrics.stringWidth("PAUSED"))/2, 
            SCREEN_HEIGHT/2);
    }

    private void drawGameOver(Graphics2D g) {
        g.setColor(settings.getTextColor());
        g.setFont(new Font("Arial", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        String scoreText = "Score: " + applesEaten;
        g.drawString(scoreText, 
            (SCREEN_WIDTH - metrics1.stringWidth(scoreText))/2, 
            SCREEN_HEIGHT/3);

        g.setFont(new Font("Arial", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        String gameOverText = "Game Over";
        g.drawString(gameOverText, 
            (SCREEN_WIDTH - metrics2.stringWidth(gameOverText))/2, 
            SCREEN_HEIGHT/2);

        g.setFont(new Font("Arial", Font.BOLD, 30));
        String restartText = "Press SPACE to restart";
        FontMetrics metrics3 = getFontMetrics(g.getFont());
        g.drawString(restartText, 
            (SCREEN_WIDTH - metrics3.stringWidth(restartText))/2, 
            SCREEN_HEIGHT*2/3);
    }

    private void spawnNewFood() {
        boolean validLocation;
        do {
            validLocation = true;
            appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
            appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
            
            // Check if the new food location overlaps with the snake
            for(int i = 0; i < bodyParts; i++) {
                if(x[i] == appleX && y[i] == appleY) {
                    validLocation = false;
                    break;
                }
            }
        } while(!validLocation);
        
        foodSpawnTime = System.currentTimeMillis();
        
        // Randomly choose food type
        double rand = random.nextDouble();
        if(rand < 0.1) { // 10% chance for golden food
            settings.setFoodType("Golden");
        } else if(rand < 0.3) { // 20% chance for special food
            settings.setFoodType("Special");
        } else { // 70% chance for regular food
            settings.setFoodType("Regular");
        }
    }

    private void move() {
        for(int i = bodyParts; i > 0; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch(direction) {
            case 'U': y[0] = y[0] - UNIT_SIZE; break;
            case 'D': y[0] = y[0] + UNIT_SIZE; break;
            case 'L': x[0] = x[0] - UNIT_SIZE; break;
            case 'R': x[0] = x[0] + UNIT_SIZE; break;
        }
    }

    private void checkFood() {
        Rectangle snakeHead = new Rectangle(x[0], y[0], UNIT_SIZE, UNIT_SIZE);
        Rectangle food = new Rectangle(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
        
        if(snakeHead.intersects(food)) {
            bodyParts++;
            applesEaten += settings.getScoreMultiplier();
            spawnNewFood();
        }
    }

    private void checkCollisions() {
        // Check if head collides with body
        for(int i = bodyParts; i > 0; i--) {
            if((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }
        
        // Check if head touches borders
        if(x[0] < 0 || x[0] >= SCREEN_WIDTH || 
           y[0] < 0 || y[0] >= SCREEN_HEIGHT) {
            running = false;
        }

        if(!running) {
            timer.stop();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(running && !isPaused) {
            move();
            checkFood();
            checkCollisions();
        }
        repaint();
    }

    private class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_A:
                    if(direction != 'R') direction = 'L';
                    break;
                case KeyEvent.VK_RIGHT:
                case KeyEvent.VK_D:
                    if(direction != 'L') direction = 'R';
                    break;
                case KeyEvent.VK_UP:
                case KeyEvent.VK_W:
                    if(direction != 'D') direction = 'U';
                    break;
                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_S:
                    if(direction != 'U') direction = 'D';
                    break;
                case KeyEvent.VK_SPACE:
                    if(!running) {
                        startGame();
                    } else {
                        isPaused = !isPaused;
                    }
                    break;
                case KeyEvent.VK_ESCAPE:
                    int choice = JOptionPane.showConfirmDialog(
                        null,
                        "Return to main menu?",
                        "Menu",
                        JOptionPane.YES_NO_OPTION);
                    if(choice == JOptionPane.YES_OPTION) {
                        Window window = SwingUtilities.getWindowAncestor(GamePanel.this);
                        window.dispose();
                        new GameMenu().setVisible(true);
                    }
                    break;
            }
        }
    }
}