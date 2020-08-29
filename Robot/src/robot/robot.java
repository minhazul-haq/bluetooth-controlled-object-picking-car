package robot;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class robot extends JFrame implements ActionListener,MouseListener,KeyListener
{
    boolean clickCount,isKeyPressed;
    public static int mouseX1,mouseY1,mouseX2,mouseY2;
    ImageIcon image;
    JLabel lblImage;
    public static JLabel lblStatus;
    JButton btnSend,btnUp,btnDown,btnLeft,btnRight,btnExpand,btnCompress,btnExit;

    serialComm SerialComm;
    String defaultPort;

    WebCam wCam;
    
    public robot()
    {
        mouseX1 = -1;
        mouseY1 = -1;
        mouseX2 = -1;
        mouseY2 = -1;
        
        clickCount = false;
        isKeyPressed = false;

        robotUserInterfaceLayout robotLayout = new robotUserInterfaceLayout();

        setIconImage(new ImageIcon(getClass().getResource("icons/robot.png")).getImage());

        getContentPane().setFont(new Font("Calibri", Font.PLAIN, 14));
        getContentPane().setLayout(robotLayout);
        getContentPane().setBackground(Color.orange);

        getContentPane().addKeyListener(this);
        getContentPane().setFocusable(true);

        /*
        image = new ImageIcon(getClass().getResource("room/room.jpg"));
        lblImage = new JLabel("", image, JLabel.CENTER);
        lblImage.setBorder(BorderFactory.createLineBorder(Color.black));
        lblImage.addMouseListener(this);
        wCam = new WebCam(lblImage);
        lblImage.addKeyListener(this);
        getContentPane().add(lblImage);
        */
        
        lblStatus = new JLabel("Problem in serial port opening");
        lblStatus.addKeyListener(this);
        getContentPane().add(lblStatus);

        defaultPort = getDefaultPort();
        SerialComm = new serialComm(defaultPort);
        SerialComm.openSerial();
        
        /*
        btnSend = new JButton(new ImageIcon(getClass().getResource("icons/pick.png")));
        btnSend.setToolTipText("Send data");
        btnSend.setBorderPainted(false);
        btnSend.setFocusPainted(false);
        btnSend.setRolloverEnabled(true);
        btnSend.setRolloverIcon(new ImageIcon(getClass().getResource("icons/pick2.png")));
        btnSend.setContentAreaFilled(false);
        btnSend.setActionCommand("send");
        btnSend.addActionListener(this);
        btnSend.addKeyListener(this);
        getContentPane().add(btnSend);
        */

        btnUp = new JButton(new ImageIcon(getClass().getResource("icons/up.png")));
        btnUp.setBorderPainted(false);
        btnUp.setRolloverEnabled(true);
        btnUp.setRolloverIcon(new ImageIcon(getClass().getResource("icons/up1.png")));
        btnUp.setPressedIcon(new ImageIcon(getClass().getResource("icons/up2.png")));
        btnUp.setContentAreaFilled(false);
        btnUp.setActionCommand("up");
        btnUp.addActionListener(this);
        btnUp.addKeyListener(this);
        getContentPane().add(btnUp);

        btnLeft = new JButton(new ImageIcon(getClass().getResource("icons/left.png")));
        btnLeft.setBorderPainted(false);
        btnLeft.setRolloverEnabled(true);
        btnLeft.setRolloverIcon(new ImageIcon(getClass().getResource("icons/left1.png")));
        btnLeft.setPressedIcon(new ImageIcon(getClass().getResource("icons/left2.png")));
        btnLeft.setContentAreaFilled(false);
        btnLeft.setActionCommand("left");
        btnLeft.addActionListener(this);
        btnLeft.addKeyListener(this);
        getContentPane().add(btnLeft);

        btnDown = new JButton(new ImageIcon(getClass().getResource("icons/down.png")));
        btnDown.setBorderPainted(false);
        btnDown.setRolloverEnabled(true);
        btnDown.setRolloverIcon(new ImageIcon(getClass().getResource("icons/down1.png")));
        btnDown.setPressedIcon(new ImageIcon(getClass().getResource("icons/down2.png")));
        btnDown.setContentAreaFilled(false);
        btnDown.setActionCommand("down");
        btnDown.addActionListener(this);
        btnDown.addKeyListener(this);
        getContentPane().add(btnDown);

        btnRight = new JButton(new ImageIcon(getClass().getResource("icons/right.png")));
        btnRight.setBorderPainted(false);
        btnRight.setRolloverEnabled(true);
        btnRight.setRolloverIcon(new ImageIcon(getClass().getResource("icons/right1.png")));
        btnRight.setPressedIcon(new ImageIcon(getClass().getResource("icons/right2.png")));
        btnRight.setContentAreaFilled(false);
        btnRight.setActionCommand("right");
        btnRight.addActionListener(this);
        btnRight.addKeyListener(this);
        getContentPane().add(btnRight);

        btnExpand = new JButton(new ImageIcon(getClass().getResource("icons/plus.png")));
        btnExpand.setToolTipText("Expand hands");
        btnExpand.setBorderPainted(false);
        btnExpand.setRolloverEnabled(true);
        btnExpand.setRolloverIcon(new ImageIcon(getClass().getResource("icons/plus1.png")));
        btnExpand.setPressedIcon(new ImageIcon(getClass().getResource("icons/plus2.png")));
        btnExpand.setContentAreaFilled(false);
        btnExpand.setActionCommand("expand");
        btnExpand.addActionListener(this);
        btnExpand.addKeyListener(this);
        getContentPane().add(btnExpand);
        
        btnCompress = new JButton(new ImageIcon(getClass().getResource("icons/minus.png")));
        btnCompress.setToolTipText("Compress hands");
        btnCompress.setBorderPainted(false);
        btnCompress.setRolloverEnabled(true);
        btnCompress.setRolloverIcon(new ImageIcon(getClass().getResource("icons/minus1.png")));
        btnCompress.setPressedIcon(new ImageIcon(getClass().getResource("icons/minus2.png")));
        btnCompress.setContentAreaFilled(false);
        btnCompress.setActionCommand("compress");
        btnCompress.addActionListener(this);
        btnCompress.addKeyListener(this);
        getContentPane().add(btnCompress);
  
        setSize(getPreferredSize());

        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                SerialComm.closeSerial();
                System.exit(0);
            }
        });
    }

    public String getDefaultPort()
    {
        try
        {
            File commFile = new File("comm.txt");

            if (!commFile.exists())
            {
                commFile= new File("../src/robot/comm.txt");
            }

            BufferedReader br = new BufferedReader(new FileReader(commFile));
            String port = br.readLine();
            br.close();
            return port;
        }
        catch(Exception e)
        {
             lblStatus.setText("Problem in text file reading");
             return null;
        }
    }

    public void sendBytes(int loc) throws Exception
    {
        for(int i=0;i<=2;i++)
        {
            int digit = loc%10;
            
            if (digit==0) SerialComm.writeSerial((byte)(0));
            else if (digit==1) SerialComm.writeSerial((byte)(1));
            else if (digit==2) SerialComm.writeSerial((byte)(2));
            else if (digit==3) SerialComm.writeSerial((byte)(3));
            else if (digit==4) SerialComm.writeSerial((byte)(12));
            else if (digit==5) SerialComm.writeSerial((byte)(13));
            else if (digit==6) SerialComm.writeSerial((byte)(14));
            else if (digit==7) SerialComm.writeSerial((byte)(15));
            else if (digit==8) SerialComm.writeSerial((byte)(16));
            else if (digit==9) SerialComm.writeSerial((byte)(32));
            
            Thread.sleep(50);
            loc = loc/10;
        }
    }

    public void sendLocationData() throws Exception
    {
        System.out.println("mouseX1:"+mouseX1+" mouseY1:"+mouseY1+" mouseX2:"+mouseX2+" mouseY2:"+mouseY2);

        sendBytes(mouseX1);
        sendBytes(mouseY1);
        sendBytes(mouseX2);
        sendBytes(mouseY2);
    }

    void sendReleased() throws Exception
    {
        //send '0' byte as key released
        sendBytes(0);
    }
    
    public void actionPerformed(ActionEvent event)
    {
        try
        {
            /*
            if (event.getActionCommand().equals("send"))
            {
                if (mouseX1>=0 && mouseY1>=0 && mouseX2>=0 && mouseY2>=0)
                {
                    sendLocationData();
                }
                else
                {
                    lblStatus.setText("You must set initial & final points");
                }
            }
            */
            if (event.getActionCommand().equals("up"))
            {
                SerialComm.writeSerial((byte)(240));
                System.out.println("Up");
            }
            else if (event.getActionCommand().equals("left"))
            {
                SerialComm.writeSerial((byte)(241));
                System.out.println("Left");
            }
            else if (event.getActionCommand().equals("down"))
            {
                SerialComm.writeSerial((byte)(242));
                System.out.println("Down");
            }
            else if (event.getActionCommand().equals("right"))
            {
                SerialComm.writeSerial((byte)(243));
                System.out.println("Right");
            }
            else if (event.getActionCommand().equals("expand"))
            {
                SerialComm.writeSerial((byte)(1));
                System.out.println("Expand");
            }
            else if (event.getActionCommand().equals("compress"))
            {
                SerialComm.writeSerial((byte)(2));
                System.out.println("Compress");
            }
            else if (event.getActionCommand().equals("exit"))
            {
                SerialComm.closeSerial();
                System.exit(0);
            }
        }
        catch(Exception e)
        {
            lblStatus.setText("Problem in data sending");
        }
    }

    public void mouseClicked(MouseEvent event)
    {
        if (clickCount==false)
        {
            mouseX1 = event.getX();
            mouseY1 = event.getY();

            clickCount = true;
        }
        else
        {
            mouseX2 = event.getX();
            mouseY2 = event.getY();

            clickCount = false;
        }

        repaint();
    }

    public void mousePressed(MouseEvent event)
    {
    }

    public void mouseReleased(MouseEvent event)
    {
    }

    public void mouseEntered(MouseEvent event)
    {  
    }

    public void mouseExited(MouseEvent event)
    {
    }

    public void paint(Graphics g)
    {
        super.paint(g);

        g.setColor(Color.RED);

        if (mouseX1>=0 && mouseY1>=0)
        {
            g.drawImage(new ImageIcon(getClass().getResource("icons/start.png")).getImage(), mouseX1-2, mouseY1-9, null);

            g.setColor(Color.WHITE);
            g.drawString("Initial point", mouseX1-4, mouseY1-12);
        }

        g.setColor(Color.BLUE);
        
        if (mouseX2>=0 && mouseY2>=0)
        {
            g.drawImage(new ImageIcon(getClass().getResource("icons/end.png")).getImage(), mouseX2-2, mouseY2-9, null);

            g.setColor(Color.WHITE);
            g.drawString("Final point", mouseX2-4, mouseY2-12);
        }
    }

    public void keyTyped(KeyEvent e)
    {
    }

    public void keyPressed(KeyEvent e)
    {
        if (isKeyPressed==false)
        {
            isKeyPressed=true;

            if (e.getKeyCode()==37) //left
            {
                btnLeft.doClick();
                btnLeft.setIcon(new ImageIcon(getClass().getResource("icons/left2.png")));
            }
            else if (e.getKeyCode()==38) //up
            {
                btnUp.doClick();
                btnUp.setIcon(new ImageIcon(getClass().getResource("icons/up2.png")));
            }
            else if (e.getKeyCode()==39) //right
            {
                btnRight.doClick();
                btnRight.setIcon(new ImageIcon(getClass().getResource("icons/right2.png")));
            }
            else if (e.getKeyCode()==40) //down
            {
                btnDown.doClick();
                btnDown.setIcon(new ImageIcon(getClass().getResource("icons/down2.png")));
            }
            else if (e.getKeyCode()==88) //expand 'x'
            {
                btnExpand.doClick();
                btnExpand.setIcon(new ImageIcon(getClass().getResource("icons/plus2.png")));
            }
            else if (e.getKeyCode()==67) //compress 'c'
            {
                btnCompress.doClick();
                btnCompress.setIcon(new ImageIcon(getClass().getResource("icons/minus2.png")));
            }
        }
    }

    public void keyReleased(KeyEvent e)
    {
        try
        {
            if (e.getKeyCode()==37) //left
                btnLeft.setIcon(new ImageIcon(getClass().getResource("icons/left.png")));
            else if (e.getKeyCode()==38) //up
                btnUp.setIcon(new ImageIcon(getClass().getResource("icons/up.png")));
            else if (e.getKeyCode()==39) //right
                btnRight.setIcon(new ImageIcon(getClass().getResource("icons/right.png")));
            else if (e.getKeyCode()==40) //down
                btnDown.setIcon(new ImageIcon(getClass().getResource("icons/down.png")));
            else if (e.getKeyCode()==88) //expand 'x'
                btnExpand.setIcon(new ImageIcon(getClass().getResource("icons/plus.png")));
            else if (e.getKeyCode()==67) //compress 'c'
                btnCompress.setIcon(new ImageIcon(getClass().getResource("icons/minus.png")));
            
            sendReleased();
            isKeyPressed = false;
            System.out.println("Key released");
        }
        catch(Exception ecp)
        {

        }
    }
}

