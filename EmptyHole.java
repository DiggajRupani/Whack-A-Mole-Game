import javax.swing.*;

public class EmptyHole extends HoleOccupant {

    private static final String IMAGE_PATH = "images/empty.png";

    public EmptyHole() {
        super(0);
        this.visible = false;
    }

    @Override
    public int whack() {
        return 0;
    }

    @Override
    public ImageIcon getImage() {
        return loadScaledIcon(IMAGE_PATH, 150, 150); // RESIZED
    }

    @Override
    public boolean isVisible() {
        return false;
    }
}
