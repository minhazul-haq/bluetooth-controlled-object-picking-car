package robot;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.*;
import javax.media.control.FormatControl;
import javax.media.control.FrameGrabbingControl;
import javax.media.format.VideoFormat;
import javax.media.protocol.*;
import javax.media.util.BufferToImage;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class WebCam implements ActionListener {

    public javax.swing.Timer timer = new javax.swing.Timer(200, this);
    private JLabel jLabel;
    private MediaLocator ml;
    private DataSource ds;
    private Player player;
    private ImagePanel imgpanel;
    private JButton buttonCaptureTask;
    private JFrame frame;    
    private Buffer BUF;
    private BufferToImage BtoI;
    private Image img;

    /*Constructor*/
    public WebCam(JLabel j) {
        jLabel = j;

        getWebCam();

        createGUI();
    }

    /* Grab the default web cam*/
    private void getWebCam() {
        try {
            /* Grab the default web cam*/
            ml = new MediaLocator("vfw://0");

            /* Create my data source */
            ds = Manager.createDataSource(ml);
            requestFormatResolution(ds);

            /* Create & start my player */
            player = Manager.createRealizedPlayer(ds);
            player.start();

        } catch (NoPlayerException ex) {
            Logger.getLogger(WebCam.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CannotRealizeException ex) {
            Logger.getLogger(WebCam.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(WebCam.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoDataSourceException ex) {
            Logger.getLogger(WebCam.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*Creates GUI*/
    private void createGUI() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        frame = new JFrame(" WebCam Capture Manager ");

        if (player.getVisualComponent() != null) {
            frame.getContentPane().add(player.getVisualComponent(), BorderLayout.SOUTH);
        }
        if (player.getControlPanelComponent() != null) {
            frame.getContentPane().add(player.getControlPanelComponent(), BorderLayout.SOUTH);
        }

        frame.setContentPane(createContentPane() );
        frame.setDefaultCloseOperation(playerclose());
        frame.pack();
        frame.setVisible(true);
    }

    /*request Format Resolution*/
    public boolean requestFormatResolution(DataSource ds) {

        if (ds instanceof CaptureDevice) {
            FormatControl[] fcs = ((CaptureDevice) ds).getFormatControls();
            for (FormatControl fc : fcs) {
                Format[] formats = ((FormatControl) fc).getSupportedFormats();
                for (Format format : formats) {
                    if ((format instanceof VideoFormat)
                            && (((VideoFormat) format).getSize().getHeight() <= 240)
                            && (((VideoFormat) format).getSize().getWidth() <= 320)) {
                        ((FormatControl) fc).setFormat(format);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JComponent) {
            JComponent c = (JComponent) e.getSource();
            if (c == buttonCaptureTask) {
                actionCapture();  // maoved every thing to new method action()
            }
        } else if (e.getSource() instanceof javax.swing.Timer) {
            action();       // timer event , call action() again
        }
    }
/**************************Actions Starts********************************/
    private void actionCapture() {
        // Grab a frame
        FrameGrabbingControl fgc = (FrameGrabbingControl) player.getControl("javax.media.control.FrameGrabbingControl");

        BUF = fgc.grabFrame();

        // Convert it to an image
        BtoI = new BufferToImage((VideoFormat) BUF.getFormat());
        img = BtoI.createImage(BUF);

        // show the image
        imgpanel.setImage(img);

        // save image

        String fileName = System.currentTimeMillis() + ".jpg";

        String path = System.getProperty("user.home") + "/" + fileName;

        // save image
        saveJPG(img, path);
    }

    private void action() {
        
        // Grab a frame
        FrameGrabbingControl fgc = (FrameGrabbingControl) player.getControl("javax.media.control.FrameGrabbingControl");
        BUF = fgc.grabFrame();

        // Convert it to an image
        BtoI = new BufferToImage((VideoFormat) BUF.getFormat());
        img = BtoI.createImage(BUF);

        // show the image
        imgpanel.setImage(img);
        
        jLabel = new JLabel(new ImageIcon(img) );
    }
/**************************Actions Finished********************************/
    
    private Container createContentPane() {
        Container container = new Container();
        
        imgpanel = new ImagePanel();
        buttonCaptureTask = new JButton("Capture");
        buttonCaptureTask.addActionListener(this);
        
        container.add(imgpanel);
        container.add(buttonCaptureTask);
        
        return container;
    }

    public int playerclose() {
        player.close();
        player.deallocate();
        
        return JFrame.EXIT_ON_CLOSE;
    }

    private void saveJPG(Image img, String path) {

        BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = bi.createGraphics();
        g2.drawImage(img, null, null);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path);
        } catch (java.io.FileNotFoundException io) {
            System.out.println("File Not Found");
        }

        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
        JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bi);
        param.setQuality(0.5f, false);
        encoder.setJPEGEncodeParam(param);

        try {
            encoder.encode(bi);
            out.close();
        } catch (java.io.IOException io) {
            System.out.println("IOException");
        }
    }
}
