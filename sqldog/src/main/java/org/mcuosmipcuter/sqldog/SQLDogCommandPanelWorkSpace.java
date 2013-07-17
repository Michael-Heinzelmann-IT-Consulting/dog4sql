package org.mcuosmipcuter.sqldog;

import java.util.Hashtable;

public class SQLDogCommandPanelWorkSpace implements SQLDogWorkSpace
{
	private long created;
	private Hashtable loginTable;
    private boolean autoCommit;

    public SQLDogCommandPanelWorkSpace()
    {
    }

	public SQLDogCommandPanelWorkSpace(Hashtable loginTable, boolean autoCommit)
	{
		this.loginTable = loginTable;
        this.autoCommit = autoCommit;
		created = System.currentTimeMillis();
	}

	public boolean equals(Object obj)
	{
		if(!(obj instanceof SQLDogCommandPanelWorkSpace))
			return false;
		SQLDogCommandPanelWorkSpace sp = (SQLDogCommandPanelWorkSpace)obj;

		SQLDogController.trace(5, sp.loginTable + " new table size: " + sp.loginTable.size());
		SQLDogController.trace(5, loginTable + " old table size: " + loginTable.size());
		boolean equals = sp.loginTable.equals(loginTable);
		SQLDogController.trace(4, "login tables are " + (equals ? "" : "not ") + "equal.");
		return equals;
	}

  public Hashtable getLoginTable() {
    return loginTable;
  }
  public void setLoginTable(Hashtable loginTable) {
    this.loginTable = loginTable;
  }
  public boolean isAutoCommit() {
    return autoCommit;
  }
  public void setAutoCommit(boolean autoCommit) {
    this.autoCommit = autoCommit;
  }

}
