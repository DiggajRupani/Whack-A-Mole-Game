import javax.swing.*;
import java.awt.*;

/**
 * Abstract base class for hole occupants.
 */
public abstract class HoleOccupant {
    protected ImageIcon loadScaledIcon(String path, int width, int height) {
        Image img = new ImageIcon(path).getImage();
        Image scaled = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    protected boolean visible = true;
    protected int timeRemaining;

    public HoleOccupant(int lifeTicks) {
        this.timeRemaining = lifeTicks;
    }

    public abstract int whack();
    public abstract javax.swing.ImageIcon getImage();

    public void tick() {
        timeRemaining--;
        if (timeRemaining <= 0) visible = false;
    }

    public boolean isVisible() {
        return visible && timeRemaining > 0;
    }

    public boolean isGameOverOnWhack() { return false; }
}
