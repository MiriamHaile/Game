import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MonsterGame {

    static int score = 0;

    public static void main(String[] args) throws IOException, InterruptedException {

//        createLinePath(0,0, 0, 5);
        startGame();

    }

    private static void startGame() throws IOException, InterruptedException {
        Terminal terminal = createTerminal();

        Player player = createPlayer();

        List<Monster> monsters = createMonsters();

        List<Food> food = createFood();

        List<Position> maze = createMaze();

        drawCharacters(terminal, player, maze, food, monsters);

        do {
            KeyStroke keyStroke = getUserKeyStroke(terminal, monsters, player);

            movePlayer(player, keyStroke, maze, food, terminal, monsters);

            eatFood(player, food);

            drawCharacters(terminal, player, maze, food, monsters);
        } while (isPlayerAlive());


        terminal.setCursorPosition(player.getX(), player.getY());
        terminal.putCharacter(player.getSymbol());
        terminal.bell();
        terminal.flush();
    }


    private static void moveMonsters(Player player, List<Monster> monsters) {
    }

    private static void movePlayer(Player player, KeyStroke keyStroke, List maze, List food, Terminal terminal, List<Monster> monsters) throws IOException {
        switch (keyStroke.getKeyType()) {
            case ArrowUp:
                if (canMove(player, 0, maze, food, terminal)) {
                    player.moveUp();
                }
                break;
            case ArrowDown:
                if (canMove(player, 1, maze, food, terminal)) {
                    player.moveDown();
                }
                break;
            case ArrowLeft:
                if (canMove(player, 2, maze, food, terminal)) {
                    player.moveLeft();
                }
                break;
            case ArrowRight:
                if (canMove(player, 3, maze, food, terminal)) {
                    player.moveRight();
                }
                break;
        }
        for (Monster m : monsters) {
            if (m.x == player.x && m.y == player.y) {
                System.out.println("game over");
            }
        }
    }

    private static Boolean canMove(GameCharacter gameChar, int dir, List maze, List food, Terminal terminal) throws IOException {
        Position pos;
        switch (dir) {
            case 0:
                pos = new Position(gameChar.getX(), gameChar.getY() - 1);
                if (!maze.contains(pos)) {
                    return true;
                }
                break;
            case 1:
                pos = new Position(gameChar.getX(), gameChar.getY() + 1);
                if (!maze.contains(pos)) {
                    return true;
                }
                break;
            case 2:
                pos = new Position(gameChar.getX() - 1, gameChar.getY());
                if (!maze.contains(pos)) {
                    return true;
                }
                break;
            case 3:
                pos = new Position(gameChar.getX() + 1, gameChar.getY());
                if (!maze.contains(pos)) {
                    return true;
                }
                break;
            case 4:
                pos = new Position(gameChar.getX(), gameChar.getY());
                if (!maze.contains(pos)) {
                    return true;
                }
                break;
        }
        return false;
    }

    private static KeyStroke getUserKeyStroke(Terminal terminal, List<Monster> monsters, Player player) throws InterruptedException, IOException {
        KeyStroke keyStroke;
        int i = 0;
        do {
            Thread.sleep(5);
            keyStroke = terminal.pollInput();
            i++;
            if (i % 50 == 0) {
                for (Monster m : monsters) {
                    m.monsterMove(m.getPath());
                    if (m.x == player.x && m.y == player.y) {
                        System.out.println("game over");
                    }

                    terminal.setCursorPosition(m.getX(), m.getY());
                    terminal.putCharacter(m.getSymbol());
                    terminal.setCursorPosition(m.getPreviousX(), m.getPreviousY());
                    terminal.putCharacter(' ');
                }
                terminal.flush();
            }
        } while (keyStroke == null);
        return keyStroke;
    }

    private static Player createPlayer() {
        return new Player(10, 10, '\u263a');
    }

    private static List createFood() {
        List<Food> foodItems = new ArrayList<>();

        for (int i = 0; i <= 60; i++) {
            int randomY = ThreadLocalRandom.current().nextInt(0, 24);
            int randomX = ThreadLocalRandom.current().nextInt(0, 80);
            foodItems.add(new Food(randomX, randomY, '⭖'));

        }
        return foodItems;

    }

    private static Terminal createTerminal() throws IOException {
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        Terminal terminal = terminalFactory.createTerminal();
        terminal.setCursorVisible(false);
        return terminal;
    }

    private static List createMaze() {
        List<Position> maze = new ArrayList();

        maze.addAll(createCirclePath(0, 0, 79, 23));

        return maze;
    }

    private static void drawCharacters(Terminal terminal, Player player, List<Position> maze, List<Food> food, List<Monster> monsters) throws IOException {


        for (Position p : maze) {
            terminal.setCursorPosition(p.x, p.y);
            terminal.putCharacter('\u2588');
            terminal.setForegroundColor(TextColor.ANSI.BLUE);
        }
        terminal.flush();
        for (Food f : food) {
            if (canMove(f, 4, maze, food, terminal)) {
                terminal.setCursorPosition(f.getX(), f.getY());
                terminal.putCharacter(f.getFood());
                terminal.setForegroundColor(TextColor.ANSI.GREEN);
            }
        }
        terminal.flush();

        for (Monster m : monsters) {
            terminal.setCursorPosition(m.getX(), m.getY());
            terminal.putCharacter(m.getSymbol());
            terminal.setCursorPosition(m.getPreviousX(), m.getPreviousY());
            terminal.putCharacter(' ');
            terminal.setForegroundColor(TextColor.ANSI.YELLOW);
        }
        terminal.flush();

        terminal.setCursorPosition(player.getPreviousX(), player.getPreviousY());
        terminal.putCharacter(' ');

        terminal.setCursorPosition(player.getX(), player.getY());
        terminal.putCharacter(player.getSymbol());
        terminal.setForegroundColor(TextColor.ANSI.RED);


        printScore(terminal, player.getScore());
        terminal.flush();

    }

    private static boolean isPlayerAlive() {
        return true;
    }

    private static void eatFood(Player player, List<Food> food) {

        Food ff = null;
        for (Food f : food) {
            if (f.getX() == player.getX() && f.getY() == player.getY()) {
                ff = f;
                break;
            }
        }
        if (ff != null) {
            food.remove(ff);
            player.setScore(player.getScore() + 1);
        }
    }

    private static List createCirclePath(int x1, int y1, int x2, int y2) {
        List<Position> path = new ArrayList<>();

        for (int x = x1; x < x2; x++) {
            path.add(new Position(x, y1));
        }
        for (int y = y1; y < y2; y++) {
            path.add(new Position(x2, y));
        }
        for (int x = x2; x > x1; x--) {
            path.add(new Position(x, y2));
        }
        for (int y = y2; y > y1; y--) {
            path.add(new Position(x1, y));
        }
//        System.out.println(path);
        return path;
    }

    private static List createLinePath(int x1, int y1, int x2, int y2) {

        List<Position> path = new ArrayList<>();

        for (int y = y1; y <= y2; y++) {
            for (int x = x1; x <= x2; x++) {
                path.add(new Position(x, y));
            }
        }

        System.out.println(path);
        return path;
    }


    private static List createMonsters() {
        List<Monster> monsters = new ArrayList<>();

        List<Position> path1 = createCirclePath(12, 12, 14, 14);
        List<Position> path2 = createCirclePath(1, 7, 5, 9);


        Monster monster1 = new Monster(path1.get(0).x, path1.get(0).y, '⛇', path1);
        Monster monster2 = new Monster(path2.get(0).x, path2.get(0).y, '⛇', path2);


        monsters.add(monster1);
        monsters.add(monster2);
        return monsters;
    }

    public static void printScore(Terminal terminal, int score) throws IOException {
        String message = "Score: " + score;
        for (int i = 0; i < message.length(); i++) {
            terminal.setCursorPosition(50 + i, 0);
            terminal.putCharacter(message.charAt(i));
        }
        terminal.flush();
    }
}