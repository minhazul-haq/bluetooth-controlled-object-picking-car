package robot;

import javax.swing.*;
import javax.swing.UIManager.*;


public class Main
{
    public static void main(String[] args) 
    {
        try
        {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
            {
                if ("Nimbus".equals(info.getName()))
                {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }

            robot robot = new robot();
            robot.setResizable(false);
            robot.setTitle("RoboPick");
            robot.setLocationRelativeTo(null);
            robot.setVisible(true);
        }
        catch(Exception e)
        {
        }
    }
}
