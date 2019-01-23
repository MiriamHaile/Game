
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {

        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        Terminal terminal = terminalFactory.createTerminal();
        terminal.setCursorVisible(false);
//        Position player = new Position(10,10);

        int x = 10;
        int y = 10;
        final char player1 = '\u263A';
        final char block = '\u2588';


        for (int row = 4; row < 10; row++) {
//            terminal.setCursorPosition(row, );
//            terminal.putCharacter(block);
            for (int c = 10; c < 15; c++) {
                terminal.setCursorPosition(c, row);
                terminal.putCharacter(block);
            }
        }


        for (int row = 10; row < 20; row++) {
            terminal.setCursorPosition(10, row);
            terminal.putCharacter(block);
        }

        for (int row = 8; row < 16; row++) {
            terminal.setCursorPosition(20, row);
            terminal.putCharacter(block);
        }


        }

    }

