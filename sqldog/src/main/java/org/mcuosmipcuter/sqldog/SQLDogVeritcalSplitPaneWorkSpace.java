package org.mcuosmipcuter.sqldog;

public class SQLDogVeritcalSplitPaneWorkSpace implements SQLDogWorkSpace
{
	private long created;
	private int dividerPosition;

    public SQLDogVeritcalSplitPaneWorkSpace()
    {

    }
	public SQLDogVeritcalSplitPaneWorkSpace(int dividerPosition)
	{
		this.dividerPosition = dividerPosition;
	}


	public boolean equals(Object obj)
	{
		if(!(obj instanceof SQLDogVeritcalSplitPaneWorkSpace))
			return false;
		SQLDogVeritcalSplitPaneWorkSpace sp = (SQLDogVeritcalSplitPaneWorkSpace)obj;
		// non basic checks
		return sp.dividerPosition == dividerPosition;
	}

  public int getDividerPosition() {
    return dividerPosition;
  }
  public void setDividerPosition(int dividerPosition) {
    this.dividerPosition = dividerPosition;
  }
}
