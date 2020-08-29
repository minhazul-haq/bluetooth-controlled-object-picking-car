package robot;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {

    private Image myimage;

    public ImagePanel() {
        setLayout(null);
        setSize(320, 240);
    }

    public void setImage(Image img) {
        this.myimage = img;
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(myimage, 0, 0, this);
    }
}
