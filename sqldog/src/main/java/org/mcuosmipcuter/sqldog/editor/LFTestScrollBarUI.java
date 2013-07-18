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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicScrollBarUI;

/**
 * @version $Revision: 1.1 $
 */

public class LFTestScrollBarUI
    extends BasicScrollBarUI
{
  protected Color myTrackColor;
  protected Color myThumbColor;

  public LFTestScrollBarUI()
  {
    myTrackColor = new Color(243, 243, 243);
    myThumbColor = new Color(233, 233, 233);
  }

  protected JButton createDecreaseButton(int orientation)
  {
    return new JButton();
  }

  protected JButton createIncreaseButton(int orientation)
  {
    return new JButton();
  }

  public static ComponentUI createUI(JComponent c)
  {
    return new LFTestScrollBarUI();
  }

  protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds)
  {
    this.thumbColor = myThumbColor;
    this.thumbDarkShadowColor = Color.white;
    this.thumbHighlightColor = Color.white;
    this.thumbLightShadowColor = Color.black;
    super.paintThumb(g, c, thumbBounds);
  }

  protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds)
  {
    g.setColor(myTrackColor);
    g.fillRect(trackBounds.x, trackBounds.y, trackBounds.x + trackBounds.width,
               trackBounds.y + trackBounds.height);
  }

}