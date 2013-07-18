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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public class SQLDogResultWrapper
{
	private ResultSet resultSet;
	private int rows;
	private int cols;
	private long executionStart;
	private long executionEnd;
	private String sqlString;
	private String dbUrl;
	private Statement stmt;
	/**
	 * 
	 */
	public SQLDogResultWrapper(String sqlString, Statement stmt) {
		this.sqlString = sqlString;
		this.stmt = stmt;
	}

	public void executeSql()throws Exception{
		Connection connection = stmt.getConnection();
		dbUrl = connection.getMetaData().getURL();
		executionStart = System.currentTimeMillis();
		stmt.execute(sqlString);
		executionEnd = System.currentTimeMillis();
		resultSet = stmt.getResultSet();
		ResultSetMetaData meta = resultSet.getMetaData();
		cols = meta.getColumnCount();
	}
	public void countRows() throws Exception{
		resultSet.last();
		rows = resultSet.getRow();
	}
	/**
	 * @return
	 */
	public int getCols() {
		return cols;
	}

	/**
	 * @return
	 */
	public String getDbUrl() {
		return dbUrl;
	}

	/**
	 * @return
	 */
	public long getExecutionEnd() {
		return executionEnd;
	}

	/**
	 * @return
	 */
	public long getExecutionStart() {
		return executionStart;
	}

	/**
	 * @return
	 */
	public ResultSet getResultSet() {
		return resultSet;
	}

	/**
	 * @return
	 */
	public int getRows() {
		return rows;
	}

	/**
	 * @return
	 */
	public String getSqlString() {
		return sqlString;
	}


}
