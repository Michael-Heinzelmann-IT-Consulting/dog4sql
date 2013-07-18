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

import java.util.Vector;

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
