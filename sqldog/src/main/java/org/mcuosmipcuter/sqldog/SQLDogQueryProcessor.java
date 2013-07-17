package org.mcuosmipcuter.sqldog;

import java.sql.*;

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
