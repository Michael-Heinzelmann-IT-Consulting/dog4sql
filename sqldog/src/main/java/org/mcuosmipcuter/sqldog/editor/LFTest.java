package org.mcuosmipcuter.sqldog.editor;

import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.basic.*;
import javax.swing.*;

/**
 * @version $Revision: 1.1 $
 */

public class LFTest
    extends MetalLookAndFeel
{
  public LFTest()
  {
  }

  public static void main(String[] args)
  {
    LFTest LFTest1 = new LFTest();
  }

  protected void initClassDefaults(UIDefaults table)
  {
    super.initClassDefaults(table);
    Object[] uiDefaults =
        {
        "ButtonUI", "org.mcuosmipcuter.sqldog.editor.LFTestButtonUI",
        "SplitPaneUI", "org.mcuosmipcuter.sqldog.editor.LFTestSplitPaneUI",
        "ScrollBarUI", "org.mcuosmipcuter.sqldog.editor.LFTestScrollBarUI",
        "TreeUI", "org.mcuosmipcuter.sqldog.editor.LFTestTreeUI"
    };

    table.putDefaults(uiDefaults);
  }

}