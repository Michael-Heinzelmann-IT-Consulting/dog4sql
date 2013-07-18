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

import javax.swing.UIDefaults;
import javax.swing.plaf.metal.MetalLookAndFeel;

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