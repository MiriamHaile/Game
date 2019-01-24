public class Food extends GameCharacter {

    private char food;


    public Food(int x, int y, char food) {
        super(x, y);
        this.food = food;
    }


    public char getFood() {
        return food;
    }
}
