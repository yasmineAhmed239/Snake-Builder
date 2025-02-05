package src;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.ArrayList;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if ((e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) || (e.getKeyCode() == KeyEvent.VK_W && velocityY != 1)) {
            velocityX = 0;
            velocityY = -1;
        } else if ((e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) || (e.getKeyCode() == KeyEvent.VK_S && velocityY != -1)) {
            velocityX = 0;
            velocityY = 1;
        } else if ((e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) || (e.getKeyCode() == KeyEvent.VK_D && velocityX != -1)) {
            velocityX = 1;
            velocityY = 0;
        } else if ((e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1) || (e.getKeyCode() == KeyEvent.VK_D && velocityX != 1)) {
            velocityX = -1;
            velocityY = 0;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    private class Tile {
        int x;
        int y;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    int boardWidth;
    int boardHieght;
    int tileSize = 25;

    //snakeBody
    Tile snakeHead;
    ArrayList<Tile> snakeBody;

    //food
    Tile food;
    Random random;

    //game logic
    Timer gameLoop;
    int velocityX;
    int velocityY;
    boolean gameOver = false;


    GamePanel(int boardWidth, int boardHieght) {
        this.boardWidth = boardWidth;
        this.boardHieght = boardHieght;
        setPreferredSize(new Dimension(boardWidth, boardHieght));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);


        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<>();

        food = new Tile(10, 10);
        random = new Random();
        placeFood();

        velocityX = 0;
        velocityY = 1;

        gameLoop=new Timer(100,this);
        gameLoop.start();

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        //grids
//        for (int i = 0; i < boardWidth / tileSize; i++) {
//            g.drawLine(i * tileSize, 0, i * tileSize, boardHieght);
//            g.drawLine(0, i * tileSize, boardHieght, i * tileSize);

            //snakeHead
            g.setColor(Color.green);
            g.fillRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize);

            //snakeBody
            for (int i =0 ; i<snakeBody.size();i++){
                Tile snakePart=snakeBody.get(i);
                g.fillRect(snakePart.x*tileSize,snakePart.y*tileSize,tileSize,tileSize);
            }

            //food
            g.setColor(Color.red);
            g.fillRect(food.x*tileSize,food.y*tileSize,tileSize,tileSize);

            //score
        g.setFont((new Font("Arial",Font.PLAIN , 22)));
        if(gameOver){
            g.setColor(Color.white);
            g.drawString("Game Over!",tileSize*9,tileSize*12);
            g.setColor(Color.white);
            g.drawString("Score: "+String.valueOf(snakeBody.size()),tileSize*10,tileSize*13);
        }
        else {
            g.drawString("Score: "+String.valueOf(snakeBody.size()),tileSize-16,tileSize);
        }

    }



    public void placeFood() {
        food.x = random.nextInt(boardHieght / tileSize);
        food.y = random.nextInt(boardHieght / tileSize);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver){
            gameLoop.stop();
        }
    }

    public void move() {
        //eat food
        if (collisions(snakeHead,food)){
            snakeBody.add(new Tile(food.x,food.y));
            placeFood();
        }

        //snakeBody
        for (int i =snakeBody.size()-1 ; i>=0 ; i--){
            Tile snakePart = snakeBody.get(i);
            if (i==0){
                snakePart.x= snakeHead.x;
                snakePart.y=snakeHead.y;
            }     else {
                Tile prevSnakeBody = snakeBody.get(i-1) ;
                snakePart.x=prevSnakeBody.x;
                snakePart.y=prevSnakeBody.y;
            }
        }

        //snake Head
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        //game over conditions
        for (int i=0; i<snakeBody.size();i++){
            Tile snakePart = snakeBody.get(i);
            //collision with the snake body
            if(collisions(snakeHead,snakePart)) {
                gameOver = true;
            }
                //collision with the wall
                if (snakeHead.x*tileSize<0 || snakeHead.x*tileSize>boardWidth ||
                        snakeHead.y*tileSize<0 || snakeHead.y*tileSize>boardHieght){
                    gameOver= true;
                }

        }


    }
    public boolean collisions(Tile tile1 , Tile tile2){
        return tile1.x==tile2.x && tile1.y==tile2.y ;
    }

}


