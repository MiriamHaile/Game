import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MonsterGame {


    public static void main(String[] args) throws IOException, InterruptedException {


        startGame();

    }

    private static void startGame() throws IOException, InterruptedException {
        Terminal terminal = createTerminal();

        Player player = createPlayer();

        List<Food> food = createFood();

        List<Position> maze = createMaze();


        drawCharacters(terminal, player, maze, food);

        do {
            KeyStroke keyStroke = getUserKeyStroke(terminal);

            movePlayer(player, keyStroke, maze, food, terminal);

            eatFood(player, food);

            drawCharacters(terminal, player, maze, food);

        } while (isPlayerAlive());

        terminal.setForegroundColor(TextColor.ANSI.RED);
        terminal.setCursorPosition(player.getX(), player.getY());
        terminal.putCharacter(player.getSymbol());
        terminal.bell();
        terminal.flush();
    }


    private static void moveMonsters(Player player, List<Monster> monsters) {
        for (Monster monster : monsters) {
            monster.moveTowards(player);
        }
    }

    private static void movePlayer(Player player, KeyStroke keyStroke, List maze, List food, Terminal terminal) throws IOException {
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

    private static KeyStroke getUserKeyStroke(Terminal terminal) throws InterruptedException, IOException {
        KeyStroke keyStroke;
        do {
            Thread.sleep(5);
            keyStroke = terminal.pollInput();
        } while (keyStroke == null);
        return keyStroke;
    }

    private static Player createPlayer() {
        return new Player(10, 10, '\u263a');
    }

    private static List createFood() {
        List<Food> foodItems = new ArrayList<>();

        for (int i = 0; i <= 60; i++) {
            int randomY = ThreadLocalRandom.current().nextInt(0, 25);
            int randomX = ThreadLocalRandom.current().nextInt(0, 81);
            foodItems.add(new Food(randomX, randomY, 'z'));
        }
        return foodItems;

    }

    private static List<Monster> createMonsters() {
        List<Monster> monsters = new ArrayList<>();
        monsters.add(new Monster(3, 3, 'X'));
        monsters.add(new Monster(23, 23, 'X'));
        monsters.add(new Monster(23, 3, 'X'));
        monsters.add(new Monster(3, 23, 'X'));
        return monsters;
    }

    private static Terminal createTerminal() throws IOException {
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        Terminal terminal = terminalFactory.createTerminal();
        terminal.setCursorVisible(false);
        return terminal;
    }

    private static List createMaze() {
        List<Position> maze = new ArrayList();

        for (int row = 0; row< 3; row+=2){
            for (int c = 0; c<21; c++){
                maze.add(new Position(c,row));
            }
        }
        for (int row = 10; row< 14; row+=2) {
            for (int c = 20; c < 34; c++) {
                maze.add(new Position(c, row));
            }
        }
        for (int row = 14; row< 17; row+=2) {
            for (int c = 30; c < 64; c++) {
                maze.add(new Position(c, row));
            }
        }
        for (int row = 18; row< 22; row+=2) {
            for (int c = 2; c < 24; c++) {
                maze.add(new Position(c, row));
            }
        }
        for (int row = 2; row< 6; row+=2){
            for (int c = 0; c<21; c++){
                maze.add(new Position(c,row));
            }
        }
        for (int row = 10; row< 12; row+=2){
            for (int c = 10; c<21; c++){
                maze.add(new Position(c,row));
            }
        }
        for (int row = 20; row<24 ; row+=2) {
            for (int c = 40; c < 60; c++) {
                maze.add(new Position(c, row));
            }
        }
        for (int row = 20; row<24 ; row+=2) {
            for (int c = 0; c < 20; c++) {
                maze.add(new Position(c, row));
            }
        }
        for (int row = 3; row<7 ; row+=2) {
            for (int c = 50; c < 70; c++) {
                maze.add(new Position(c, row));
            }
        }
        return maze;
    }

    private static void drawCharacters(Terminal terminal, Player player, List<Position> maze, List<Food> food) throws IOException {


        for (Position p : maze) {
            terminal.setCursorPosition(p.x, p.y);
            terminal.putCharacter('\u2588');
        }
        for (Food f : food) {
            if (canMove(f, 4, maze, food, terminal)) {
                terminal.setCursorPosition(f.getX(), f.getY());
                terminal.putCharacter(f.getFood());
            }
        }

        terminal.setCursorPosition(player.getPreviousX(), player.getPreviousY());
        terminal.putCharacter(' ');

        terminal.setCursorPosition(player.getX(), player.getY());
        terminal.putCharacter(player.getSymbol());


        terminal.flush();

    }

    private static boolean isPlayerAlive() {
        return true;
    }

    private static void eatFood(Player player, List<Food> food){

        Food ff = null;
        for (Food f : food) {
            if (f.getX() == player.getX() && f.getY() == player.getY()){
                ff = f;
                break;
            }
        }
        if (ff != null) {
            food.remove(ff);
        }
    }
}