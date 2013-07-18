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
/*
 * michael Created on Oct 10, 2004
*/
package org.mcuosmipcuter.dog4sql;

/**
 * @author michael 2004
 */
public abstract class Worker extends Thread
{
	private boolean hasRun = false;
	
	public Worker()
	{
	}

	public final void run(){
		if(hasRun){
			throw new RuntimeException(this + " can only be run once!");
		}
		hasRun = true;
		Controller.trace(5, "run() for: " + this + " " + getClass().getName());
		runImpl();
	}
	protected abstract void runImpl();
	
	protected void finalize()
		throws Throwable
	{
		Controller.trace(5, "finalize() for: " + this + " " + getClass().getName());
		super.finalize();
	}
}
