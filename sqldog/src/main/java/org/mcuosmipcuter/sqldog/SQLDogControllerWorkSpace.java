package org.mcuosmipcuter.sqldog;

import java.util.*;

public class SQLDogControllerWorkSpace implements SQLDogWorkSpace
{
	private long created;
	private java.awt.Dimension dimension;
    private boolean maximised;
    private Vector drivers;

    public SQLDogControllerWorkSpace()
    {

    }
	public SQLDogControllerWorkSpace(java.awt.Dimension dimension, boolean maximised, Vector drivers)
	{
		this.dimension = dimension;
        this.maximised = maximised;
        this.drivers = drivers;
	}

	public boolean equals(Object obj)
	{
		if(!(obj instanceof SQLDogControllerWorkSpace))
			return false;
		SQLDogControllerWorkSpace sp = (SQLDogControllerWorkSpace)obj;
		// non basic checks
		return sp.dimension.equals(dimension) && sp.maximised == maximised && drivers.equals(sp.drivers);
	}
  public java.awt.Dimension getDimension() {
    return dimension;
  }
  public void setDimension(java.awt.Dimension dimension) {
    this.dimension = dimension;
  }
    public boolean isMaximised()
    {
        return maximised;
    }
    public void setMaximised(boolean maximised)
    {
        this.maximised = maximised;
    }
  public Vector getDrivers() {
    return drivers;
  }
  public void setDrivers(Vector drivers) {
    this.drivers = drivers;
  }
}
