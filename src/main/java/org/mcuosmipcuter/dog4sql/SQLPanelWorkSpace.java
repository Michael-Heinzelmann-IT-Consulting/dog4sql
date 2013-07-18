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

public class SQLPanelWorkSpace implements WorkSpace
{
	private long created;
	private String sqlText;
	private int dividerPosition;
    private int dividerPosition2;

    public SQLPanelWorkSpace()
    {

    }
	public SQLPanelWorkSpace(String version, String sqlText, int dividerPosition, int dividerPosition2)
	{
		this.sqlText = sqlText;
		this.dividerPosition = dividerPosition;
        this.dividerPosition2 = dividerPosition2;
	}

	public boolean equals(Object obj)
	{
		if(!(obj instanceof SQLPanelWorkSpace))
			return false;
		SQLPanelWorkSpace sp = (SQLPanelWorkSpace)obj;
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
