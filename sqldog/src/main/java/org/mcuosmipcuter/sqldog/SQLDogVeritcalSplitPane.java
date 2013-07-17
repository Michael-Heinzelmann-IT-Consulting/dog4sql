package org.mcuosmipcuter.sqldog;

import javax.swing.*;
import java.awt.Color;

public class SQLDogVeritcalSplitPane extends JSplitPane implements SQLDogWorkSpacePersistence
{

	public SQLDogVeritcalSplitPane(java.awt.Component upper, java.awt.Component lower)
	{
		//super(JSplitPane.VERTICAL_SPLIT, true, upper, lower);
        super(JSplitPane.VERTICAL_SPLIT);
        setBackground(new Color(187, 221, 228));
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

	public SQLDogWorkSpace getWorkSpace()
	{
		//<cvs/>last divider pos is not to be stored, get divider gets the current pos
		return new SQLDogVeritcalSplitPaneWorkSpace(getDividerLocation());
	}

	public void setWorkSpace(SQLDogWorkSpace ws)
	{
		if(	ws == null
			|| !(ws instanceof SQLDogVeritcalSplitPaneWorkSpace))
			return;

			setDividerLocation(((SQLDogVeritcalSplitPaneWorkSpace)ws).getDividerPosition());
	}

  public SQLDogVeritcalSplitPane() {
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
