import javax.swing.*;

public class Mole extends HoleOccupant {

    private static final int VALUE = 100;
    private static final String IMAGE_PATH = "images/mole.png";

    public Mole(int lifeTicks) {
        super(lifeTicks);
    }

    @Override
    public int whack() {
        visible = false;
        return VALUE;
    }

    @Override
    public ImageIcon getImage() {
        return loadScaledIcon(IMAGE_PATH, 150, 150);  // RESIZED
    }
}
