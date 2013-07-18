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
package org.mcuosmipcuter.sqldog.editor;

import java.awt.Graphics;

import javax.swing.JButton;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

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