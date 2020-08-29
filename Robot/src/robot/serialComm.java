package robot;

import java.io.*;
import java.util.*;
import gnu.io.*; // for rxtxSerial library

public class serialComm
{
    CommPortIdentifier portId;
    Enumeration portList;
    SerialPort serialPort;
    
    InputStream inputStream;
    OutputStream outputStream;

    public static boolean portFound;
    String defaultPort;
    int baudRate;

    public serialComm(String defaultPort)
    {
        portFound = false;
        this.defaultPort = defaultPort;
        baudRate = 9800;
    }

    public boolean openSerial()
    {
        try
        {
            //get all available ports
            portList = CommPortIdentifier.getPortIdentifiers();

            while (portList.hasMoreElements())
            {
                portId = (CommPortIdentifier) portList.nextElement();

                if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL)
                {
                    

                    if (portId.getName().equals(defaultPort))
                    {
                        portFound = true;
                        
                        robot.lblStatus.setText("Found and opened port: " + defaultPort);

                        serialPort = (SerialPort) portId.open("Robot", 2000);

                        // set port parameters
                        serialPort.setSerialPortParams(baudRate, SerialPort.DATABITS_8,
                        SerialPort.STOPBITS_1,
                        SerialPort.PARITY_NONE);

                        inputStream = serialPort.getInputStream();
                        outputStream = serialPort.getOutputStream();
                    }
                }
            }
        }
        catch(Exception e)
        {
            robot.lblStatus.setText("Problem in serial port opening");
        }

        return portFound;
    }
    
    public void writeSerial(byte data)
    {
        try
        {
            if ((portFound==true) && (outputStream!=null))
            {
                /*outputStream.write((byte)(0xFF));
                outputStream.flush();
                Thread.sleep(100);
                System.out.println((byte)(inputStream.read()));
            
                */
                /*
                for(byte b=0;b<=15;b++)
                {
                    System.out.print("Sent: "+b+"  ");
                    outputStream.write((byte)(b));
                    Thread.sleep(2000);
                    System.out.println(("Received: "+(byte)(inputStream.read())+"  "));
                }
                */

                outputStream.write((byte)(data));
            }
            else
            {
                robot.lblStatus.setText("Problem in serial port writing");
            }
        }
        catch(Exception e)
        {
            robot.lblStatus.setText("Problem in serial port writing");
        }
    }

    public void closeSerial()
    {
        try
        {
            portFound = false;
            
            inputStream.close();
            outputStream.close();
            serialPort.close();
        }
        catch(Exception e)
        {
            robot.lblStatus.setText("Problem in serial port closing");
        }
    }
}