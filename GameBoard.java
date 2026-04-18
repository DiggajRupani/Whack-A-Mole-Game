import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class GameBoard {

    private final JFrame frame;
    private final JLabel scoreLabel;
    private final JLabel timeLabel;

    private final JButton[] buttons;
    private final HoleOccupant[] occupants;

    private int score = 0;
    private int timeRemaining = 60;
    private boolean gameOver = false;

    private Thread gameThread;
    private GameEngine engine;

    private final HighScoreManager hsm = new HighScoreManager("scores.dat");
    private final List<PlayerScore> highScores = new ArrayList<>();

    private final int ROWS = 3;
    private final int COLS = 4;

    public GameBoard() {

        frame = new JFrame("Whack-a-Mole");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel top = new JPanel();
        scoreLabel = new JLabel("Score: 0");
        timeLabel = new JLabel("Time: 60");
        top.add(scoreLabel);
        top.add(timeLabel);

        frame.add(top, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(ROWS, COLS));
        buttons = new JButton[ROWS * COLS];
        occupants = new HoleOccupant[ROWS * COLS];

        for (int i = 0; i < occupants.length; i++) {
            occupants[i] = new EmptyHole();
            JButton b = new JButton();
            final int index = i;
            b.addActionListener(e -> onHoleClicked(index));
            b.setIcon(occupants[i].getImage());
            buttons[i] = b;
            grid.add(b);
        }

        frame.add(grid, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        JButton start = new JButton("Start");
        JButton stop = new JButton("Stop");
        JButton showHS = new JButton("High Scores");
        bottom.add(start);
        bottom.add(stop);
        bottom.add(showHS);

        start.addActionListener(e -> startGame());
        stop.addActionListener(e -> endGame());
        showHS.addActionListener(e -> showScores());

        frame.add(bottom, BorderLayout.SOUTH);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (gameThread != null) gameThread.interrupt();
                exitCleanly();
            }
        });

        try {
            highScores.addAll(hsm.loadScores());
        } catch (HighScoreException ex) {
            JOptionPane.showMessageDialog(frame, "Failed to load scores.");
        }

        frame.setVisible(true);
    }

    public int getHoleCount() { return occupants.length; }

    private void startGame() {
        score = 0;
        timeRemaining = 60;
        gameOver = false;

        for (int i = 0; i < occupants.length; i++) {
            occupants[i] = new EmptyHole();
            buttons[i].setIcon(occupants[i].getImage());
        }

        engine = new GameEngine(this, 1000, 0.6);
        gameThread = new Thread(engine);
        gameThread.start();

        new Thread(() -> {
            try {
                while (!gameOver && timeRemaining > 0) {
                    Thread.sleep(1000);
                    timeRemaining--;
                    SwingUtilities.invokeLater(this::refreshUI);
                }
                if (timeRemaining <= 0)
                    SwingUtilities.invokeLater(this::onGameOver);
            } catch (InterruptedException ignored) {}
        }).start();
    }

    public boolean isGameOver() { return gameOver; }

    public void spawnOccupant(int index, HoleOccupant occ) {
        if (occupants[index].isVisible())
            throw new InvalidGameStateException("Hole already occupied");

        occupants[index] = occ;
        SwingUtilities.invokeLater(() -> buttons[index].setIcon(occ.getImage()));
    }

    public void tickAllOccupants() {
        for (int i = 0; i < occupants.length; i++) {
            HoleOccupant occ = occupants[i];
            if (occ.isVisible()) {
                occ.tick();
                if (!occ.isVisible()) {
                    occupants[i] = new EmptyHole();
                    final int idx = i;
                    SwingUtilities.invokeLater(() -> buttons[idx].setIcon(occupants[idx].getImage()));
                }
            }
        }
    }

    private void onHoleClicked(int index) {
        if (gameOver) return;
        HoleOccupant occ = occupants[index];
        if (!occ.isVisible()) return;

        score += occ.whack();

        if (occ.isGameOverOnWhack()) {
            gameOver = true;
            refreshUI();
            onGameOver();
            return;
        }

        occupants[index] = new EmptyHole();
        buttons[index].setIcon(occupants[index].getImage());
        refreshUI();
    }

    public void refreshUI() {
        scoreLabel.setText("Score: " + score);
        timeLabel.setText("Time: " + timeRemaining);
    }

    public void onEngineStopped() { refreshUI(); }

    private void onGameOver() {
        gameOver = true;
        if (engine != null) engine.shutdown();

        int ans = JOptionPane.showConfirmDialog(frame,
                "Game Over! Score = " + score + "\nSave score?",
                "Game Over", JOptionPane.YES_NO_OPTION);

        if (ans == JOptionPane.YES_OPTION) {
            String name = JOptionPane.showInputDialog("Enter name:");
            if (name != null) {
                highScores.add(new PlayerScore(name, score));
                try { hsm.saveScores(highScores); } 
                catch (HighScoreException ignored) {}
            }
        }

        int again = JOptionPane.showConfirmDialog(frame, "Play again?");
        if (again == JOptionPane.YES_OPTION) startGame();
    }

    private void showScores() {
        StringBuilder sb = new StringBuilder("High Scores:\n");
        highScores.sort((a, b) -> b.getScore() - a.getScore());
        for (PlayerScore ps : highScores) sb.append(ps).append("\n");
        JOptionPane.showMessageDialog(frame, sb.toString());
    }

    private void exitCleanly() {
        endGame();
        frame.dispose();
        System.exit(0);
    }

    private void endGame() {
        gameOver = true;
        if (engine != null) engine.shutdown();
        if (gameThread != null) gameThread.interrupt();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameBoard::new);
    }
}
