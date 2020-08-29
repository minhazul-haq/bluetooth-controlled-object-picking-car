package robot;

import java.awt.*;

public class robotUserInterfaceLayout implements LayoutManager
{
    public robotUserInterfaceLayout()
    {
    }

    public void addLayoutComponent(String name, Component comp)
    {
    }

    public void removeLayoutComponent(Component comp)
    {
    }

    public Dimension preferredLayoutSize(Container parent)
    {
        Dimension dim = new Dimension(0, 0);

        Insets insets = parent.getInsets();
        dim.width = 340 + insets.left + insets.right;
        dim.height = 200 + insets.top + insets.bottom;

        return dim;
    }

    public Dimension minimumLayoutSize(Container parent)
    {
        Dimension dim = new Dimension(0, 0);
        return dim;
    }

    public void layoutContainer(Container parent)
    {
        Insets insets = parent.getInsets();

        parent.getComponent(0).setBounds(insets.left+90,insets.top+145,200,24);

        parent.getComponent(1).setBounds(insets.left+55,insets.top+20,44,44);
        parent.getComponent(2).setBounds(insets.left+15,insets.top+56,44,44);
        parent.getComponent(3).setBounds(insets.left+55,insets.top+90,44,44);
        parent.getComponent(4).setBounds(insets.left+95,insets.top+56,44,44);
        
        parent.getComponent(5).setBounds(insets.left+180,insets.top+50,59,49);
        parent.getComponent(6).setBounds(insets.left+270,insets.top+50,50,49);
    }
}