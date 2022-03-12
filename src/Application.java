
public class Application {

    Tetris tetris;

    public Application() {
        tetris = new Tetris(this);
        tetris.setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        new Application();
    }

}
