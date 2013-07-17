package org.mcuosmipcuter.sqldog.editor;

import javax.swing.plaf.basic.*;
import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

/**
 * @version $Revision: 1.1 $
 */

public class LFTestSplitPaneUI
    extends BasicSplitPaneUI
{
  public LFTestSplitPaneUI()
  {
  }

  public BasicSplitPaneDivider createDefaultDivider()
  {
    return new LFTestSplitPaneDivider(this);
  }

  protected Component createDefaultNonContinuousLayoutDivider()
  {
    return new LFTestSplitPaneDivider(this);
  }

  public static ComponentUI createUI()
  {
    return new LFTestSplitPaneUI();
  }

  public static ComponentUI createUI(JComponent c)
  {
    return new LFTestSplitPaneUI();
  }

  public BasicSplitPaneDivider getDivider()
  {
    return new LFTestSplitPaneDivider(this);
  }
}