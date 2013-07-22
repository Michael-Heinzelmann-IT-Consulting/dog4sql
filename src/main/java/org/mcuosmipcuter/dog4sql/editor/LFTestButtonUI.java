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
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 * @version $Revision: 1.1 $
 */

public class LFTestButtonUI
    extends BasicButtonUI
{
  private boolean mouseOver = false;
  private int lastMouseX = 0;
  private int lastMouseY = 0;
  private boolean toRight;
  private boolean lastMouseOver;

  public LFTestButtonUI()
  {
  }

  public static ComponentUI createUI(JComponent c)
  {
    return new LFTestButtonUI();
  }

  protected void paintButtonPressed(Graphics g, AbstractButton b)
  {
    boolean divider = b.getName() == null ? false :
        b.getName().equals("dividerButton");
    if(divider)
    {
      return;
    }
    int lenText = b.getFontMetrics(b.getFont()).stringWidth(b.getText() == null ? "" : b.getText());
    int startText = (b.getWidth() - lenText) / 2;
    int endText = startText + lenText;
    int heightText = b.getFontMetrics(b.getFont()).getHeight();
    int topText = (b.getHeight() - heightText) / 2;
    int bottomText = topText + heightText;
    int j = 0;
    int rim = 2;
    for(int i = rim; i < b.getWidth() - rim - 1; i++)
    {
      if(i % 2 == 0)
      {
        g.setColor(new Color(255, 255, 255));
        j = i < b.getWidth() / 2 ? j + 1 : j - 1;
      }
      else
      {
        g.setColor(new Color(55, 25, 163));
      }
      if(i < startText - 1 || i > endText + 1)
      {
        g.drawLine(i, rim, i, b.getHeight() - rim - 1);
      }
      else
      {
        g.drawLine(i, rim, i, topText - 1);
        g.drawLine(i, bottomText + 1, i, b.getHeight() - rim - 1);
      }
    }

  }

  public void update(Graphics g, JComponent c)
  {
    boolean divider = c.getName() == null ? false :
        c.getName().equals("dividerButton");
    if(divider)
    {
      return;
    }

    if(c.getHeight() > 255 * 2)
    {
      return; // exceedingly high component not supported
    }
    Border bo = null;
    int rim = 1;
    int left = rim;
    int top = rim;
    int right = rim;
    int bottom = rim;
    if(bo != null)
    {
      Insets ins = bo.getBorderInsets(c);
      left = ins.left;
      top = ins.top;
      right = ins.right;
      bottom = ins.bottom;
    }
    g.clearRect(0, 0, c.getSize().width, c.getSize().height);
    for(int i = top; i < c.getHeight() - bottom; i++)
    {
      int val = 255 - i * 2;
      if(val < 0)
        val = 0;
      if(val > 255)
        val = 255;
      g.setColor(new Color(val, val, val));
      g.drawLine(left, i, c.getWidth() - right, i);
    }
    if(bo == null)
    {
      drawBorder(g, c);
    }
    else
    {
      bo.paintBorder(c, g, 0, 0, c.getWidth(), c.getHeight());
    }
    paint(g, c);
  }

  protected void paintFocus(Graphics g, AbstractButton b,
                            Rectangle viewRect, Rectangle textRect,
                            Rectangle iconRect)
  {
    boolean divider = b.getName() == null ? false :
        b.getName().equals("dividerButton");
    if(b.getModel().isPressed() || mouseOver || divider)
    {
      return;
    }
    g.setColor(Color.white);
    g.setColor(new Color(55, 55, 255));
    g.drawString(b.getText() == null ? "" : b.getText(), textRect.x,
                 textRect.y + g.getFontMetrics().getAscent());
    if(b.getBorder() == null)
    {
      drawBorder(g, b);
    }
  }

  public boolean contains(JComponent c, int x, int y)
  {
    boolean divider = c.getName() == null ? false :
        c.getName().equals("dividerButton");
    if(divider||!c.hasFocus())
    {
      return super.contains(c, x, y);
    }

    Object par = c.getParent();
    boolean cbox = par == null ? false :
        par.getClass().getName().equals("javax.swing.JComboBox");
    mouseOver = x > 0 && x < c.getWidth() - 4 && y > 0 && y < c.getHeight() - 4;
    if(lastMouseOver && !mouseOver && !cbox)
    {
      update(c.getGraphics(), c);
    }
    if(mouseOver && !cbox && c.isEnabled())
    {
      if(x < lastMouseX)
      {
        if(toRight)
        {
          shadeToLeft(c.getGraphics(), c);
          paint(c.getGraphics(), c);
          toRight = false;
        }
      }
      else if(x != lastMouseX)
      {
        if(!toRight)
        {
          shadeToRight(c.getGraphics(), c);
          paint(c.getGraphics(), c);
          toRight = true;
        }
      }

      lastMouseX = x;
    }
    lastMouseOver = mouseOver;

    return super.contains(c, x, y);
  }

  protected void shadeToRight(Graphics g, JComponent c)
  {
    horizontalShade(g, c, true);
  }

  protected void shadeToLeft(Graphics g, JComponent c)
  {
    horizontalShade(g, c, false);
  }

  private void horizontalShade(Graphics g, JComponent c, boolean darkerToRight)
  {
    int width = c.getWidth();
    int height = c.getHeight();
    if(width > 255 * 2)
    {
      return; // exceedingly wide component not supported
    }
    Border bo = c.getBorder();
    int rim = 1;
    int left = rim;
    int top = rim;
    int right = rim;
    int bottom = rim;
    int d = 2;
    if(bo != null)
    {
      Insets ins = bo.getBorderInsets(c);
      left = ins.left;
      top = ins.top;
      right = ins.right;
      bottom = ins.bottom;
      d = 1;
    }

    int j = 0;
    int start = 255 - width / 2 > 0 ? 255 - width / 2 : 0;
    for(int i = left; i < width - right; i++)
    {
      if(true)
      {
        if(i % 2 == 0)
        {
          j++;
        }
        int s = darkerToRight ? 255 - j : start + j;
        g.setColor(new Color(s, s, s));
      }
      g.drawLine(i, top, i, height - bottom - d);

    }
  }

  protected static void drawBorder(Graphics g, JComponent c) {
    g.setColor(new Color(25, 25, 124));
    g.drawRect(0, 0, c.getWidth() - 1, c.getHeight() - 1);
  }

}