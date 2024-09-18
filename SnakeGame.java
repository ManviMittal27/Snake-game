import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener {
    private final int GRID_SIZE = 20;
    private final int TILE_SIZE = 30;
    private final int GAME_WIDTH = GRID_SIZE * TILE_SIZE;
    private final int GAME_HEIGHT = GRID_SIZE * TILE_SIZE;
    private final int INIT_LENGTH = 3;

    private enum Direction {UP, DOWN, LEFT, RIGHT}

    private ArrayList<Point> snake;
    private Point fruit;
    private Direction direction;
    private boolean gameOver;
    private int score;
    private Timer timer;

    public SnakeGame() {
        setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyChar()) {
                    case 'w' -> {
                        if (direction != Direction.DOWN) direction = Direction.UP;
                    }
                    case 's' -> {
                        if (direction != Direction.UP) direction = Direction.DOWN;
                    }
                    case 'a' -> {
                        if (direction != Direction.RIGHT) direction = Direction.LEFT;
                    }
                    case 'd' -> {
                        if (direction != Direction.LEFT) direction = Direction.RIGHT;
                    }
                    case 'r' -> {
                        if (gameOver) restartGame();
                    }
                    case 'x' -> System.exit(0);
                }
            }
        });
        restartGame();
    }

    private void restartGame() {
        snake = new ArrayList<>();
        for (int i = INIT_LENGTH - 1; i >= 0; i--) {
            snake.add(new Point(GRID_SIZE / 2, GRID_SIZE / 2 + i));
        }
        direction = Direction.RIGHT;
        score = 0;
        gameOver = false;
        placeFruit();
        timer = new Timer(200, this);
        timer.start();
    }

    private void placeFruit() {
        Random rand = new Random();
        fruit = new Point(rand.nextInt(GRID_SIZE), rand.nextInt(GRID_SIZE));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver) {
            timer.stop();
            return;
        }

        Point head = new Point(snake.get(0));
        switch (direction) {
            case UP -> head.translate(0, -1);
            case DOWN -> head.translate(0, 1);
            case LEFT -> head.translate(-1, 0);
            case RIGHT -> head.translate(1, 0);
        }

        if (head.equals(fruit)) {
            snake.add(0, fruit);
            score++;
            placeFruit();
        } else {
            snake.add(0, head);
            snake.remove(snake.size() - 1);
        }

        if (head.x < 0 || head.x >= GRID_SIZE || head.y < 0 || head.y >= GRID_SIZE || snake.subList(1, snake.size()).contains(head)) {
            gameOver = true;
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (gameOver) {
            g.setColor(Color.RED);
            g.drawString("Game Over! Score: " + score, GAME_WIDTH / 2 - 50, GAME_HEIGHT / 2);
            g.drawString("Press R to Restart or X to Quit", GAME_WIDTH / 2 - 100, GAME_HEIGHT / 2 + 20);
            return;
        }

        // Draw boundaries
        g.setColor(Color.WHITE);
        for (int x = 0; x < GAME_WIDTH; x += TILE_SIZE) {
            g.drawRect(x, 0, TILE_SIZE, TILE_SIZE);
            g.drawRect(x, GAME_HEIGHT - TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }
        for (int y = 0; y < GAME_HEIGHT; y += TILE_SIZE) {
            g.drawRect(0, y, TILE_SIZE, TILE_SIZE);
            g.drawRect(GAME_WIDTH - TILE_SIZE, y, TILE_SIZE, TILE_SIZE);
        }

        // Draw snake
        g.setColor(Color.GREEN);
        for (int i = 0; i < snake.size(); i++) {
            Point p = snake.get(i);
            if (i == 0) {
                g.fillOval(p.x * TILE_SIZE, p.y * TILE_SIZE, TILE_SIZE, TILE_SIZE); // Snake head
            } else {
                g.fillRect(p.x * TILE_SIZE, p.y * TILE_SIZE, TILE_SIZE, TILE_SIZE); // Snake body
            }
        }

        // Draw fruit
        g.setColor(Color.RED);
        g.fillOval(fruit.x * TILE_SIZE, fruit.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);

        // Draw score
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 10, 10);
        g.drawString("Press X to Quit", 10, 25);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame game = new SnakeGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
