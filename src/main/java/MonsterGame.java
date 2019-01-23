import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MonsterGame {


    public static void main(String[] args) throws IOException, InterruptedException {


        startGame();

    }

    private static void startGame() throws IOException, InterruptedException {
        Terminal terminal = createTerminal();

        Player player = createPlayer();

        List maze = createMaze();


        drawCharacters(terminal, player, maze);

        do {
            KeyStroke keyStroke = getUserKeyStroke(terminal);

            movePlayer(player, keyStroke, maze);

            drawCharacters(terminal, player, maze);

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

    private static void movePlayer(Player player, KeyStroke keyStroke, List maze) {
        switch (keyStroke.getKeyType()) {
            case ArrowUp:
                if (canMove(player, 0, maze)) {
                    player.moveUp();
                }
                break;
            case ArrowDown:
                if (canMove(player, 1, maze)) {
                    player.moveDown();
                }
                break;
            case ArrowLeft:
                if (canMove(player, 2, maze)) {
                    player.moveLeft();
                }
                break;
            case ArrowRight:
                if (canMove(player, 3, maze)) {
                    player.moveRight();
                }
                break;
        }
    }

    private static Boolean canMove(Player player, int dir, List maze) {
        Position pos;
        switch (dir) {
            case 0:
                pos = new Position(player.getX(), player.getY() - 1);
                if (!maze.contains(pos)) {
                    return true;
                }
                break;
            case 1:
                pos = new Position(player.getX(), player.getY() + 1);
                if (!maze.contains(pos)) {
                    return true;
                }
                break;
            case 2:
                pos = new Position(player.getX() - 1, player.getY());
                if (!maze.contains(pos)) {
                    return true;
                }
                break;
            case 3:
                pos = new Position(player.getX() + 1, player.getY());
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

        for (int row = 4; row < 10; row++) {
            for (int c = 10; c < 15; c++) {
                maze.add(new Position(c, row));
            }
        }
        return maze;
    }

    private static void drawCharacters(Terminal terminal, Player player, List<Position> maze) throws IOException {


        for (Position p : maze) {
            terminal.setCursorPosition(p.x, p.y);
            terminal.putCharacter('\u2588');
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
}