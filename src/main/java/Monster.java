import java.util.List;

public class Monster extends GameCharacter{
    private char symbol;
    private int previousX;
    private int previousY;
    private List<Position> path;

    public Monster(int x, int y, char symbol, List<Position> path) {
        super(path.get(0).x, path.get(0).y);
        this.symbol = symbol;
        this.previousX = x;
        this.previousY = y;
        this.path = path;
    }

    public void monsterMove (List<Position> path) {
        for (int i = 0; i < path.size(); i++) {
            if (i == path.size() - 1) {
                this.x = path.get(0).x;
                this.y = path.get(0).y;
            }
            if (path.get(i).x == this.x && path.get(i).y == this.y) {
                this.x = path.get(i + 1).x;
                this.y = path.get(i + 1).y;
                break;
            }
        }


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

}