package org.mcuosmipcuter.sqldog;

import java.sql.*;

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
