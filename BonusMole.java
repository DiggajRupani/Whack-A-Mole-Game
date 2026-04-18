import javax.swing.*;

public class BonusMole extends HoleOccupant {

    private static final int VALUE = 1000;
    private static final String IMAGE_PATH = "images/bonus_mole.png";

    public BonusMole(int lifeTicks) {
        super(lifeTicks);
    }

    @Override
    public int whack() {
        visible = false;
        return VALUE;
    }

    @Override
    public ImageIcon getImage() {
        return loadScaledIcon(IMAGE_PATH, 150, 150); // RESIZED
    }
}
