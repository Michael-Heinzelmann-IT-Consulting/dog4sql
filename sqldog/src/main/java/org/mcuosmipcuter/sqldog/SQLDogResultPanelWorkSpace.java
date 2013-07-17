package org.mcuosmipcuter.sqldog;

public class SQLDogResultPanelWorkSpace implements SQLDogWorkSpace
{
	private long created;
	private String findText;
	private boolean sizeColumns;
    private boolean expandLobs;
	private int fontSize;
	private boolean regex;
	private boolean matchCase;

    public SQLDogResultPanelWorkSpace()
    {

    }
	public SQLDogResultPanelWorkSpace(	String findText,
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
		if(!(obj instanceof SQLDogResultPanelWorkSpace))
			return false;
		SQLDogResultPanelWorkSpace sp = (SQLDogResultPanelWorkSpace)obj;
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
