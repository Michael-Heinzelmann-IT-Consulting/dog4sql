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

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

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