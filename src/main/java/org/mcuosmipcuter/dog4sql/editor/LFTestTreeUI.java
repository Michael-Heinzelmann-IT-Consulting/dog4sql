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
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTreeUI;

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