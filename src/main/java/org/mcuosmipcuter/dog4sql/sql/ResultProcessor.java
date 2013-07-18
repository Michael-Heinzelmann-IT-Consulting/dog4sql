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
package org.mcuosmipcuter.dog4sql.sql;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.SimpleDateFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
* class to process resultsets retrieved vi SQL
*/
public class ResultProcessor 
{
	protected String processingInstruction 
								= ResultProcessorConstants.XML_DEFAULT_PI;
	protected String docType
								= ResultProcessorConstants.XML_DEFAULT_DOCTYPE;

	protected String resultset 
								= ResultProcessorConstants.XML_DEFAULT_RESULTSET;
	protected String resultSetAtrrRows 
								= ResultProcessorConstants.XML_DEFAULT_RESULTSET_ATTR_ROWS;
	protected String resultSetAtrrColumns 
								= ResultProcessorConstants.XML_DEFAULT_RESULTSET_ATTR_COLUMNS;
	protected String header 
								= ResultProcessorConstants.XML_DEFAULT_HEADER;
	protected String sql 
								= ResultProcessorConstants.XML_DEFAULT_SQL;
	protected String timestamp 
								= ResultProcessorConstants.XML_DEFAULT_TIMESTAMP;
	protected String result 
								= ResultProcessorConstants.XML_DEFAULT_RESULT;
	protected String resultAttrStartrow 
								= ResultProcessorConstants.XML_DEFAULT_RESULT_ATTR_STARTROW;
	protected String resultAttrEndrow 
								= ResultProcessorConstants.XML_DEFAULT_RESULT_ATTR_ENDROW;

	protected String row 
								= ResultProcessorConstants.XML_DEFAULT_ROW;
	protected String rowAttrNumber 
								= ResultProcessorConstants.XML_DEFAULT_ROW_ATTR_NUMBER;
	protected String column 
								= ResultProcessorConstants.XML_DEFAULT_COLUMN;
	protected String columnAttrNumber 
								= ResultProcessorConstants.XML_DEFAULT_COLUMN_ATTR_NUMBER;
	protected String columnAttrClass 
								= ResultProcessorConstants.XML_DEFAULT_COLUMN_ATTR_CLASS;

	protected final byte[] endElementNewLine  = ">\n".getBytes();
	protected final byte[] tab = "\t".getBytes();
	protected final byte[] cdataStart = "<![CDATA[".getBytes(); 
	protected final byte[] cdataEnd  = "]]>".getBytes(); 
	protected final byte[] startEndElement  = "</".getBytes(); 
	protected final byte[] startAttrVal  = "=\"".getBytes(); 
	protected final byte[] afterAttrValBefNextAttr  = "\" ".getBytes(); 

	protected byte[] rowStartElement;
	protected byte[] rowEndElement; 
	protected byte[] colStartElement;
	protected byte[] colEndElement; 

	private void init()
	{
		rowStartElement = ("\t<" + row + " " + rowAttrNumber + "=\"").getBytes();
		rowEndElement = "\">".getBytes(); 
		colStartElement = ("\t\t<" + column + " " + columnAttrNumber + "=\"").getBytes();
		colEndElement = "\">".getBytes(); 
	}
	/**
	* default constructor<br/>
	* all values are inititialized to defaults set in ResultProcessorConstants
	*/
	public ResultProcessor()
	{
		init();
	}
	
	/**
	* constructor using settings from a provided resource file<br/>
	* the keys from ResultProcessorConstants have to be used<br/>
	* if a key/value pair is not given it falls back to the defaults
	* @param resourceFile the resource file basename
	* @throws MissingResourceException when the file is not found 
	*/
	public ResultProcessor(String resourceFile)
	throws MissingResourceException
	{
		ResourceBundle resourceBundle = ResourceBundle.getBundle(resourceFile);
		try {
			processingInstruction 
			= resourceBundle.getString(ResultProcessorConstants.XML_KEY_PI);
		} catch(MissingResourceException ex) { }	
		try {
			docType 
			= resourceBundle.getString(ResultProcessorConstants.XML_KEY_DOCTYPE);
		} catch(MissingResourceException ex) { }	
		try {
			resultset 
			= resourceBundle.getString(ResultProcessorConstants.XML_KEY_RESULTSET);
		} catch(MissingResourceException ex) { }	
		try {
			resultSetAtrrRows 
			= resourceBundle.getString(ResultProcessorConstants.XML_KEY_RESULTSET_ATTR_ROWS);
		} catch(MissingResourceException ex) { }	
		try {
			resultSetAtrrColumns 
			= resourceBundle.getString(ResultProcessorConstants.XML_KEY_RESULTSET_ATTR_COLUMNS);
		} catch(MissingResourceException ex) { }	
		try {
			header 
			= resourceBundle.getString(ResultProcessorConstants.XML_KEY_HEADER);
		} catch(MissingResourceException ex) { }	
		try {
			sql 
			= resourceBundle.getString(ResultProcessorConstants.XML_KEY_SQL);
		} catch(MissingResourceException ex) { }	
		try {
			timestamp 
			= resourceBundle.getString(ResultProcessorConstants.XML_KEY_TIMESTAMP);
		} catch(MissingResourceException ex) { }	
		try {
			result
			= resourceBundle.getString(ResultProcessorConstants.XML_KEY_RESULT);
		} catch(MissingResourceException ex) { }	
		try {
			resultAttrStartrow 
			= resourceBundle.getString(ResultProcessorConstants.XML_KEY_RESULT_ATTR_STARTROW);
		} catch(MissingResourceException ex) { }	
		try {
			resultAttrEndrow 
			= resourceBundle.getString(ResultProcessorConstants.XML_KEY_RESULT_ATTR_STARTROW);
		} catch(MissingResourceException ex) { }	
		try {
			row
			= resourceBundle.getString(ResultProcessorConstants.XML_KEY_RESULT_ATTR_ENDROW);
		} catch(MissingResourceException ex) { }	
		try {
			rowAttrNumber 
			= resourceBundle.getString(ResultProcessorConstants.XML_KEY_ROW);
		} catch(MissingResourceException ex) { }	
		try {
			column 
			= resourceBundle.getString(ResultProcessorConstants.XML_KEY_ROW_ATTR_NUMBER);
		} catch(MissingResourceException ex) { }	
		try {
			columnAttrNumber 
			= resourceBundle.getString(ResultProcessorConstants.XML_KEY_COLUMN_ATTR_NUMBER);
		} catch(MissingResourceException ex) { }	

		init();
	}

	/**
	* writes the XML data contained in the resulset to an output stream
	* @param resultSet the input
	* @param includeDoctype set to true if the doc type instruction should be in the output
	* @param sqlString the SQL used to produce the resultset, can be null
	* @param out the output stream to write the XML data
	* @throws java.sql.SQLException on problems with the resultset
	* @throws java.io.IOException on problems with the output stream
	*/
	public void getResultsetAsXml(	ResultSet resultSet,
									boolean includeDoctype,
									String sqlString,
									OutputStream out)
	throws java.sql.SQLException, java.io.IOException
	{
		int startrow = 0;
		resultSet.beforeFirst();
		if(resultSet.next())
			startrow = 1;
		getResultsetAsXml(resultSet, includeDoctype, startrow, 0, sqlString, out);
	}

	/**
	* writes the XML data contained in the resulset to an output stream<br/>
	* writes the part of the resultset limited by startRow and endRow
	* @param resultSet the input
	* @param includeDoctype set to true if the doc type instruction should be in the output
	* @param startRow the row to start writing
	* @param endRow the row to stop writing
	* @param sqlString the SQL used to produce the resultset, can be null
	* @param out the output stream to write the XML data
	* @throws java.sql.SQLException on problems with the resultset
	* @throws java.io.IOException on problems with the output stream
	*/
	public void getResultsetAsXml(	ResultSet resultSet,
									boolean includeDoctype,
									int startRow,
									int endRow,
									String sqlString,
									OutputStream out)
	throws java.sql.SQLException, java.io.IOException
	{
		ResultSetMetaData meta = resultSet.getMetaData();
		int cols = meta.getColumnCount();
		String[] classNames = new String[cols + 1];
		for(int i = 1; i <= cols; i++)	
			classNames[i] = meta.getColumnClassName(i);
		out.write(processingInstruction.getBytes());
		out.write("\n".getBytes());
		if(includeDoctype)
		{
			out.write(docType.getBytes());
			out.write("\n".getBytes());
		}
		out.write('<');
		out.write(resultset.getBytes());
		out.write(' ');
		out.write(resultSetAtrrRows.getBytes());
		out.write(startAttrVal);
		out.write(Integer.toString(endRow - startRow + 1).getBytes());
		out.write(afterAttrValBefNextAttr);
		out.write(resultSetAtrrColumns.getBytes());
		out.write(startAttrVal);
		out.write(Integer.toString(cols).getBytes());
		out.write('"');
		out.write(endElementNewLine);
		out.write('<');
		out.write(header.getBytes());
		out.write(endElementNewLine);
		for(int i = 1; i <= cols; i++)	
		{
			out.write(colStartElement);
			out.write(Integer.toString(i).getBytes());
			out.write(afterAttrValBefNextAttr);
			out.write(columnAttrClass.getBytes());
			out.write(startAttrVal);
			out.write(classNames[i].getBytes());
			out.write(colEndElement);
			out.write(meta.getColumnName(i).getBytes());
			out.write(startEndElement);
			out.write(column.getBytes());
			out.write(endElementNewLine);
		}
		out.write(startEndElement);
		out.write(header.getBytes());
		out.write(endElementNewLine);
		out.write('<');
		out.write(sql.getBytes());
		out.write('>');
		out.write(cdataStart);
		out.write(sqlString.getBytes());
		out.write(cdataEnd);
		out.write(startEndElement);
		out.write(sql.getBytes());
		out.write(endElementNewLine);
		out.write('<');
		out.write(timestamp.getBytes());
		out.write('>');
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		out.write(sdf.format(new java.util.Date()).toString().getBytes());
		out.write(startEndElement);
		out.write(timestamp.getBytes());
		out.write(endElementNewLine);
		out.write('<');
		out.write(result.getBytes());
		out.write(' ');
		out.write(resultAttrStartrow.getBytes());
		out.write(startAttrVal);
		out.write(Integer.toString(startRow).getBytes());
		out.write(afterAttrValBefNextAttr);
		out.write(resultAttrEndrow.getBytes());
		out.write(startAttrVal);
		out.write(Integer.toString(endRow).getBytes());
		out.write('"');
		out.write(endElementNewLine);
		if(startRow > 0)
		{
			int rownum = startRow;
			boolean beforeLimit = true;
			do
			{
				out.write(rowStartElement);
				out.write(Integer.toString(rownum++).getBytes());
				out.write(rowEndElement);
				out.write("\n".getBytes());
				for(int i = 1; i <= cols; i++)	
				{
					out.write(colStartElement);
					out.write(Integer.toString(i).getBytes());
					out.write(colEndElement);
					Object o = resultSet.getObject(i);	
					if(o != null)
						out.write(o.toString().getBytes());
					out.write(startEndElement);
					out.write(column.getBytes());
					out.write(endElementNewLine);
				}
				out.write(tab);
				out.write(startEndElement);
				out.write(row.getBytes());
				out.write(endElementNewLine);
				if(endRow > 0)
					beforeLimit = rownum <= endRow;
			} while(resultSet.next() && beforeLimit);
		}
		out.write(startEndElement);
		out.write(result.getBytes());
		out.write(endElementNewLine);
		out.write(startEndElement);
		out.write(resultset.getBytes());
		out.write(endElementNewLine);
	}

	/**
	* main method for testing
	*/
	public static void main(String[] args)
	{
		PreparedStatement s = null;
		ResultSet r = null;
		Connection conn = null;

		try
		{
        	Class.forName("com.mysql.jdbc.Driver").newInstance();

	        conn = java.sql.DriverManager.getConnection("jdbc:mysql://localhost/test");

			String sql = args[0];
			System.out.println(sql);	
			
			s = conn.prepareStatement(sql);
			s.execute();
			r = s.getResultSet();

			File f = new File("test.out");
			FileOutputStream fos = new FileOutputStream(f);
			
			ResultProcessor processor = new ResultProcessor();
			System.out.println("getting XML");	
			long startTime = System.currentTimeMillis();
			processor.getResultsetAsXml(r, true, sql, fos);
			System.out.println("time n ms needed " + (System.currentTimeMillis() - startTime));	

			fos.close();
			r.close();

			if(conn != null)
				conn.close();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}	

}
