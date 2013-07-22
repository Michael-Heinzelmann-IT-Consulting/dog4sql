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
package org.mcuosmipcuter.dog4sql;

import java.awt.Color;

import javax.swing.JSplitPane;

public class VeritcalSplitPane extends JSplitPane implements WorkSpacePersistence
{

	public VeritcalSplitPane(java.awt.Component upper, java.awt.Component lower)
	{
		//super(JSplitPane.VERTICAL_SPLIT, true, upper, lower);
        super(JSplitPane.VERTICAL_SPLIT);
        setBackground(Constants.COLOR_APP);
        super.setTopComponent(upper);
        super.setBottomComponent(lower);
        try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
//setOneTouchExpandable(true); //<cvs/>added one touch expand
	}

	public WorkSpace getWorkSpace()
	{
		//<cvs/>last divider pos is not to be stored, get divider gets the current pos
		return new VeritcalSplitPaneWorkSpace(getDividerLocation());
	}

	public void setWorkSpace(WorkSpace ws)
	{
		if(	ws == null
			|| !(ws instanceof VeritcalSplitPaneWorkSpace))
			return;

			setDividerLocation(((VeritcalSplitPaneWorkSpace)ws).getDividerPosition());
	}

  public VeritcalSplitPane() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public String getName(){return this.getClass().getName();}

  private void jbInit() throws Exception {
    this.setOrientation(JSplitPane.VERTICAL_SPLIT);
    this.setDividerSize(12);
    this.setOneTouchExpandable(true);

  }
}
