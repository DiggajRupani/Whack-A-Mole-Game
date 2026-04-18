import javax.swing.*;
import java.util.Random;

public class GameEngine implements Runnable {

    private final GameBoard board;
    private boolean running = true;
    private final Random rand = new Random();

    private final int tickMillis;
    private final double spawnChance;

    public GameEngine(GameBoard board, int tickMillis, double spawnChance) {
        this.board = board;
        this.tickMillis = tickMillis;
        this.spawnChance = spawnChance;
    }

    public void shutdown() { running = false; }

    public void run() {
        try {
            while (running && !board.isGameOver()) {

                board.tickAllOccupants();

                if (rand.nextDouble() < spawnChance) {
                    int hole = rand.nextInt(board.getHoleCount());
                    double r = rand.nextDouble();

                    HoleOccupant occ;
                    int life = 3 + rand.nextInt(4);

                    if (r < 0.75) occ = new Mole(life);
                    else if (r < 0.95) occ = new BonusMole(life);
                    else occ = new Bomb(life);

                    board.spawnOccupant(hole, occ);
                }

                SwingUtilities.invokeLater(() -> board.refreshUI());
                Thread.sleep(tickMillis);
            }
        } catch (InterruptedException e) {
            running = false;
            SwingUtilities.invokeLater(board::onEngineStopped);
        }

        SwingUtilities.invokeLater(board::onEngineStopped);
    }
}
