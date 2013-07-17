package org.mcuosmipcuter.sqldog.editor;

import javax.swing.plaf.basic.BasicToolTipUI;
import javax.swing.plaf.*;
import javax.swing.*;
import java.awt.*;

public class LFTestToolTipUI extends BasicToolTipUI
{
  public LFTestToolTipUI()
  {
    System.out.println("LFTestToolTipUI constructor<.:.> ");
  }
  public static ComponentUI createUI(JComponent c){
    System.out.println("LFTestToolTipUI createUI ");
    return new LFTestToolTipUI() ;
  }
  public void paint(Graphics g, JComponent c) {
    //System.out.println("paint --:x:"+c.getLocationOnScreen().x+" y:"+c.getLocationOnScreen().y);
    //g.setColor(Color.red);
    //g.drawRect(c.getLocationOnScreen().x,c.getLocationOnScreen().y,40,40);

    c.setBackground(Color.yellow);

    super.paint(g, c);
  }
  public void update(Graphics g, JComponent c) {
    System.out.println("update>>>>"+c);
    //g.setColor(Color.blue);
    //c.setBounds(new Rectangle(0,0,40,40));
    //c.setFont(c.getFont().deriveFont(18.0F));
    //g.drawRect(0,0,40,40);
    super.update(g, c);
  }

}