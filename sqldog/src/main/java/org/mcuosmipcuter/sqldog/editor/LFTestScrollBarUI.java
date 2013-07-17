package org.mcuosmipcuter.sqldog.editor;

import javax.swing.plaf.basic.*;
import javax.swing.plaf.*;
import java.awt.*;
import javax.swing.*;

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