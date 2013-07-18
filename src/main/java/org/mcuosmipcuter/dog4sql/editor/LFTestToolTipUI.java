/**
*   JDBC database client for application developers and support
*   Copyright (C) 2003 - 2013 Michael Heinzelmann,
*   Michael Heinzelmann IT-Consulting
*
*   This program is free software: you can redistribute it and/or modify
*   it under the terms of the GNU General Public License as published by
*   the Free Software Foundation, either version 3 of the License, or
*   (at your option) any later version.
*
*   This program is distributed in the hope that it will be useful,
*   but WITHOUT ANY WARRANTY; without even the implied warranty of
*   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*   GNU General Public License for more details.
*
*   You should have received a copy of the GNU General Public License
*   along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package org.mcuosmipcuter.dog4sql.editor;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicToolTipUI;

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