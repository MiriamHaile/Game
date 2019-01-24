public class Player extends GameCharacter {
    private char symbol;
    private int previousX;
    private int previousY;

    public Player(int x, int y, char symbol) {
        super(x, y);
        this.symbol = symbol;
        this.previousX = x;
        this.previousY = y;
    }

    public char getSymbol() {
        return symbol;
    }

    public int getPreviousX() {
        return previousX;
    }

    public int getPreviousY() {
        return previousY;
    }

    public void moveUp() {
        previousX = x;
        previousY = y;
        y--;
    }

    public void moveDown() {
        previousX = x;
        previousY = y;
        y++;
    }

    public void moveLeft() {
        previousX = x;
        previousY = y;
        x--;
    }

    public void moveRight() {
        previousX = x;
        previousY = y;
        x++;
    }

    @Override
    public String toString() {
        return "Player{" +
                "x=" + x +
                ", y=" + y +
                ", symbol=" + symbol +
                ", previousX=" + previousX +
                ", previousY=" + previousY +
                '}';
    }
}