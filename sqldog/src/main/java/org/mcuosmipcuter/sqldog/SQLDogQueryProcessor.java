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

import java.sql.Statement;

public class SQLDogQueryProcessor extends SQLDogWorker
{
	private String strSQL;
	SQLDogQueryListener queryListener;
	Statement stmt = null;

	boolean process = false;

	public SQLDogQueryProcessor(SQLDogQueryListener queryListener, String strSQL, Statement stmt)
	throws Exception
	{
		this.queryListener = queryListener;
		if(stmt.getConnection() == null)
			throw new Exception("not connected!");
		this.strSQL = strSQL;
		this.stmt = stmt;	
	}

	public void runImpl()
	{
		SQLDogResultWrapper rw = new SQLDogResultWrapper(strSQL, stmt);

		process = true;

		try
		{
			queryListener.onQueryMessage("executing ...");

			if(process){
				rw.executeSql();
				queryListener.onQueryMessage("statement executed");
			}		
			
			int rownum = 0;
			if(rw.getResultSet() != null)
			{
				if(process){
					queryListener.onQueryMessage("resultset received, converting ...");
					SQLDogController.trace(3, "counting rows ...");
					rw.countRows();
					queryListener.onResult(rw);
				}
			}
			else {
				queryListener.onResult(rw);
			}

			queryListener.onQueryMessage(rw.getRows() + " rows returned, done.");
		}
		catch(Exception ex)
		{
			SQLDogController.trace(1, ex);
			queryListener.onQueryException(ex);
		}
		finally
		{
			stmt = null;
			rw = null;
			process = false;
			SQLDogController.trace(4, this + " resources released.");
		}

	}

	public void stopQuery()
	{
		try
		{
			queryListener.onQueryMessage("stopping query ...");
		 	process	= false;
			stmt.close();
			stmt = null;
			queryListener.onQueryMessage("statement cancelled.");
		}
		catch(Exception e)
		{
			queryListener.onQueryException(e);
		}
	}

}
