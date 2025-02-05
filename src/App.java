package src;

import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        int boardWidth=600;
        int boardHiegth=boardWidth;

        JFrame frame=new JFrame("Snake Game");
        frame.setSize(boardWidth,boardHiegth);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        GamePanel snakeGame =new GamePanel(boardWidth,boardHiegth);
        frame.add(snakeGame);
        frame.pack();
        snakeGame.requestFocus();



    }
}
