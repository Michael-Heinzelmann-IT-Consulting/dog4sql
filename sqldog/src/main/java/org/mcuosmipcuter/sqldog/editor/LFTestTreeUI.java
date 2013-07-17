package org.mcuosmipcuter.sqldog.editor;

import javax.swing.plaf.basic.*;
import java.awt.*;
import javax.swing.tree.*;
import javax.swing.plaf.*;
import javax.swing.*;
import java.awt.event.*;

/**
 * @version $Revision: 1.1 $
 */

public class LFTestTreeUI
    extends BasicTreeUI
{

  public static ComponentUI createUI(JComponent c)
  {
    return new LFTestTreeUI();
  }

  public LFTestTreeUI()
  {
  }

  protected void drawCentered(Component c, Graphics g, Icon icon, int x, int y)
  {
    g.setColor(Color.white);
    g.fillRect(x - 4, y - 4, 8, 8);
    g.setColor(new Color(205, 205, 255));
    g.drawRect(x - 4, y - 4, 8, 8);
    if(icon.equals(this.collapsedIcon))
    {
      g.drawLine(x, y - 2, x, y + 2);
    }
    g.drawLine(x - 2, y, x + 2, y);
  }

}