import javax.swing.*;

public class Bomb extends HoleOccupant {

    private static final int PENALTY = -500;
    private static final String IMAGE_PATH = "images/bomb.png";

    public Bomb(int lifeTicks) {
        super(lifeTicks);
    }

    @Override
    public int whack() {
        visible = false;
        return PENALTY;
    }

    @Override
    public ImageIcon getImage() {
        return loadScaledIcon(IMAGE_PATH, 150, 150); // RESIZED
    }

    @Override
    public boolean isGameOverOnWhack() {
        return true;
    }
}
