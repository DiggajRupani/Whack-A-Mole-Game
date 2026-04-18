import java.io.*;
import java.util.*;

public class HighScoreManager {

    private final File file;

    public HighScoreManager(String filename) {
        this.file = new File(filename);
    }

    public void saveScores(List<PlayerScore> scores) throws HighScoreException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(scores);
        } catch (IOException e) {
            throw new HighScoreException("Failed to save scores", e);
        }
    }

    @SuppressWarnings("unchecked")
    public List<PlayerScore> loadScores() throws HighScoreException {
        if (!file.exists()) return new ArrayList<>();

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (List<PlayerScore>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new HighScoreException("Failed to load scores", e);
        }
    }
}
