package org.mcuosmipcuter.sqldog;

public class SQLDogSQLPanelWorkSpace implements SQLDogWorkSpace
{
	private long created;
	private String sqlText;
	private int dividerPosition;
    private int dividerPosition2;

    public SQLDogSQLPanelWorkSpace()
    {

    }
	public SQLDogSQLPanelWorkSpace(String version, String sqlText, int dividerPosition, int dividerPosition2)
	{
		this.sqlText = sqlText;
		this.dividerPosition = dividerPosition;
        this.dividerPosition2 = dividerPosition2;
	}

	public boolean equals(Object obj)
	{
		if(!(obj instanceof SQLDogSQLPanelWorkSpace))
			return false;
		SQLDogSQLPanelWorkSpace sp = (SQLDogSQLPanelWorkSpace)obj;
		// non basic checks
		return sp.sqlText.equals(sqlText)
            && sp.dividerPosition == dividerPosition
            && sp.dividerPosition2 == dividerPosition2;
	}

  public String getSqlText() {
    return sqlText;
  }
  public void setSqlText(String sqlText) {
    this.sqlText = sqlText;
  }
  public int getDividerPosition() {
    return dividerPosition;
  }
  public void setDividerPosition(int dividerPosition) {
    this.dividerPosition = dividerPosition;
  }
  public int getDividerPosition2() {
    return dividerPosition2;
  }
  public void setDividerPosition2(int dividerPosition2) {
    this.dividerPosition2 = dividerPosition2;
  }
}
