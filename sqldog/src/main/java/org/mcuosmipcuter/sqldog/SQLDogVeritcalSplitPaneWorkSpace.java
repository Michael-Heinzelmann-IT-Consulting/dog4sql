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
