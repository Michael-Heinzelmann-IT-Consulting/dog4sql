package org.mcuosmipcuter.sqldog.editor;

import javax.swing.plaf.basic.*;
import java.awt.*;
import javax.swing.JButton;
import javax.swing.border.*;

/**
 * @version $Revision: 1.1 $
 */

public class LFTestSplitPaneDivider
    extends BasicSplitPaneDivider
{
  public LFTestSplitPaneDivider(BasicSplitPaneUI ui)
  {
    super(ui);
  }

  public void paint(Graphics g)
  {
    g.setColor(splitPane.getBackground());
    g.fillRect(0, 0, getBounds().x + getBounds().width,
               getBounds().y + getBounds().height);
    super.paint(g);
  }

  protected JButton createLeftOneTouchButton()
  {
    JButton b = super.createLeftOneTouchButton();
    b.setName("dividerButton");
    b.setBackground(splitPane.getBackground());
    return b;
  }

  protected JButton createRightOneTouchButton()
  {
    JButton b = super.createRightOneTouchButton();
    b.setName("dividerButton");
    b.setBackground(splitPane.getBackground());
    return b;
  }

  public Border getBorder()
  {
    return null;
  }

  public void setBorder(Border b)
  {
    super.setBorder(null);
  }

}