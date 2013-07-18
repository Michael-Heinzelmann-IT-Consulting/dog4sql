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

import java.util.Hashtable;

public class CommandPanelWorkSpace implements WorkSpace
{
	private long created;
	private Hashtable loginTable;
    private boolean autoCommit;

    public CommandPanelWorkSpace()
    {
    }

	public CommandPanelWorkSpace(Hashtable loginTable, boolean autoCommit)
	{
		this.loginTable = loginTable;
        this.autoCommit = autoCommit;
		created = System.currentTimeMillis();
	}

	public boolean equals(Object obj)
	{
		if(!(obj instanceof CommandPanelWorkSpace))
			return false;
		CommandPanelWorkSpace sp = (CommandPanelWorkSpace)obj;

		Controller.trace(5, sp.loginTable + " new table size: " + sp.loginTable.size());
		Controller.trace(5, loginTable + " old table size: " + loginTable.size());
		boolean equals = sp.loginTable.equals(loginTable);
		Controller.trace(4, "login tables are " + (equals ? "" : "not ") + "equal.");
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
