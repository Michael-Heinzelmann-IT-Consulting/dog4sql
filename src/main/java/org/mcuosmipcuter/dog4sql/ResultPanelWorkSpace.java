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

public class ResultPanelWorkSpace implements WorkSpace
{
	private long created;
	private String findText;
	private boolean sizeColumns;
    private boolean expandLobs;
	private int fontSize;
	private boolean regex;
	private boolean matchCase;

    public ResultPanelWorkSpace()
    {

    }
	public ResultPanelWorkSpace(	String findText,
										boolean sizeColumns,
                                        boolean expandLobs,
										int fontSize,boolean regex, boolean matchCase)
	{
		this.findText = findText;
		this.sizeColumns = sizeColumns;
        this.expandLobs = expandLobs;
		this.fontSize = fontSize;
		this.regex = regex;
		this.matchCase = matchCase;
	}

	public boolean equals(Object obj)
	{
		if(!(obj instanceof ResultPanelWorkSpace))
			return false;
		ResultPanelWorkSpace sp = (ResultPanelWorkSpace)obj;
		// non basic checks
		return sp.findText.equals(findText) 
				&& sp.sizeColumns == sizeColumns 
				&& sp.expandLobs == expandLobs 
				&& sp.fontSize == fontSize
				&& sp.regex == regex
				&& sp.matchCase == matchCase;
	}

  public String getFindText() {
    return findText;
  }
  public void setFindText(String findText) {
    this.findText = findText;
  }
  public int getFontSize() {
    return fontSize;
  }
  public void setFontSize(int fontSize) {
    this.fontSize = fontSize;
  }
  public boolean isSizeColumns() {
    return sizeColumns;
  }
  public void setSizeColumns(boolean sizeColumns) {
    this.sizeColumns = sizeColumns;
  }
    public boolean isExpandLobs()
    {
        return expandLobs;
    }
    public void setExpandLobs(boolean expandLobs)
    {
        this.expandLobs = expandLobs;
    }

	public boolean isRegex()
	{
		return regex;
	}

	public void setRegex(boolean regex)
	{
		this.regex = regex;
	}

	public boolean isMatchCase()
	{
		return matchCase;
	}

	public void setMatchCase(boolean matchCase)
	{
		this.matchCase = matchCase;
	}
}
