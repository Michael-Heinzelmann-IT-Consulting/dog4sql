package org.mcuosmipcuter.sqldog.sql;

public class ResultProcessorConstants
{
	/**
	* the key to retrieve the string for processing instruction from the resource file
	* <br/>value: org.mcuosmipcuter.sqldog.sql.xml.pi
	*/
	public static final String XML_KEY_PI = "org.mcuosmipcuter.sqldog.sql.xml.pi";
	/**
	* the default value for key org.mcuosmipcuter.sqldog.sql.xml.pi
	* value: &lt;?xml version="1.0" encoding="UTF-8"?&gt;
	*/
	public static final String XML_DEFAULT_PI = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	/**
	* the key to retrieve the string for doctype instruction from the resource file
	* <br/>value: org.mcuosmipcuter.sqldog.sql.xml.doctype
	*/
	public static final String XML_KEY_DOCTYPE = "org.mcuosmipcuter.sqldog.sql.xml.doctype";
	/**
	* the default value for key org.mcuosmipcuter.sqldog.sql.xml.doctype
	* value: &lt;!DOCTYPE resultset SYSTEM "resultset.dtd"&gt;
	*/
	public static final String XML_DEFAULT_DOCTYPE = "<!DOCTYPE resultset SYSTEM \"resultset.dtd\">";

	/**
	* the key to retrieve the string for the resultset element from the resource file
	* <br/>value: org.mcuosmipcuter.sqldog.sql.xml.resultset
	*/
	public static final String XML_KEY_RESULTSET = "org.mcuosmipcuter.sqldog.sql.xml.resultset";
	/**
	* the default value for key org.mcuosmipcuter.sqldog.sql.xml.resultset
	* value: resultset
	*/
	public static final String XML_DEFAULT_RESULTSET = "resultset";
	/**
	* the key to retrieve the string for the attribute rows of the resultset element from the resource file
	* <br/>value: org.mcuosmipcuter.sqldog.sql.xml.resultset.attribute.rows
	*/
	public static final String XML_KEY_RESULTSET_ATTR_ROWS = "org.mcuosmipcuter.sqldog.sql.xml.resultset.attribute.rows";
	/**
	* the default value for key org.mcuosmipcuter.sqldog.sql.xml.resultset.attribute.rows
	* value: rows
	*/
	public static final String XML_DEFAULT_RESULTSET_ATTR_ROWS = "rows";
	/**
	* the key to retrieve the string for the attribute columns of the resultset element from the resource file
	* <br/>value: org.mcuosmipcuter.sqldog.sql.xml.resultset.attribute.columns
	*/
	public static final String XML_KEY_RESULTSET_ATTR_COLUMNS = "org.mcuosmipcuter.sqldog.sql.xml.resultset.attribute.columns";
	/**
	* the default value for key org.mcuosmipcuter.sqldog.sql.xml.resultset.attribute.columns
	* value: columns
	*/
	public static final String XML_DEFAULT_RESULTSET_ATTR_COLUMNS = "columns";
	/**
	* the key to retrieve the string for the element header from the resource file
	* <br/>value: org.mcuosmipcuter.sqldog.sql.xml.header
	*/
	public static final String XML_KEY_HEADER = "org.mcuosmipcuter.sqldog.sql.xml.header";
	/**
	* the default value for key org.mcuosmipcuter.sqldog.sql.xml.header
	* value: header
	*/
	public static final String XML_DEFAULT_HEADER = "header";
	/**
	* the key to retrieve the string for the element sql from the resource file
	* <br/>value: org.mcuosmipcuter.sqldog.sql.xml.sql
	*/
	public static final String XML_KEY_SQL = "org.mcuosmipcuter.sqldog.sql.xml.sql";
	/**
	* the default value for key org.mcuosmipcuter.sqldog.sql.xml.sql
	* value: sql
	*/
	public static final String XML_DEFAULT_SQL = "sql";
	/**
	* the key to retrieve the string for the element timestamp from the resource file
	* <br/>value: org.mcuosmipcuter.sqldog.sql.xml.timestamp
	*/
	public static final String XML_KEY_TIMESTAMP = "org.mcuosmipcuter.sqldog.sql.xml.timestamp";
	/**
	* the default value for key org.mcuosmipcuter.sqldog.sql.xml.timestamp
	* value: timestamp
	*/
	public static final String XML_DEFAULT_TIMESTAMP = "timestamp";
	/**
	* the key to retrieve the string for the element result from the resource file
	* <br/>value: org.mcuosmipcuter.sqldog.sql.xml.result
	*/
	public static final String XML_KEY_RESULT = "org.mcuosmipcuter.sqldog.sql.xml.result";
	/**
	* the default value for key org.mcuosmipcuter.sqldog.sql.xml.result
	* value: result
	*/
	public static final String XML_DEFAULT_RESULT = "result";
	/**
	* the key to retrieve the string for the attribute startrow of the element result from the resource file
	* <br/>value: org.mcuosmipcuter.sqldog.sql.xml.result.attribute.startrow
	*/
	public static final String XML_KEY_RESULT_ATTR_STARTROW = "org.mcuosmipcuter.sqldog.sql.xml.result.attribute.startrow";
	/**
	* the default value for key org.mcuosmipcuter.sqldog.sql.xml.result.attribute.startrow
	* value: startrow
	*/
	public static final String XML_DEFAULT_RESULT_ATTR_STARTROW = "startrow";
	/**
	* the key to retrieve the string for the attribute endrow of the element result from the resource file
	* <br/>value: org.mcuosmipcuter.sqldog.sql.xml.result.attribute.endrow
	*/
	public static final String XML_KEY_RESULT_ATTR_ENDROW = "org.mcuosmipcuter.sqldog.sql.xml.result.attribute.endrow";
	/**
	* the default value for key org.mcuosmipcuter.sqldog.sql.xml.result.attribute.endrow
	* value: endrow
	*/
	public static final String XML_DEFAULT_RESULT_ATTR_ENDROW = "endrow";
	/**
	* the key to retrieve the string for the element row from the resource file
	* <br/>value: org.mcuosmipcuter.sqldog.sql.xml.row
	*/
	public static final String XML_KEY_ROW = "org.mcuosmipcuter.sqldog.sql.xml.row";
	/**
	* the default value for key org.mcuosmipcuter.sqldog.sql.xml.row
	* value: row
	*/
	public static final String XML_DEFAULT_ROW = "row";
	/**
	* the key to retrieve the string for the attribute number of the element row from the resource file
	* <br/>value: org.mcuosmipcuter.sqldog.sql.xml.row.attribute.number
	*/
	public static final String XML_KEY_ROW_ATTR_NUMBER = "org.mcuosmipcuter.sqldog.sql.xml.row.attribute.number";
	/**
	* the default value for key org.mcuosmipcuter.sqldog.sql.xml.row.attribute.number
	* value: n
	*/
	public static final String XML_DEFAULT_ROW_ATTR_NUMBER = "n";
	/**
	* the key to retrieve the string for the element column from the resource file
	* <br/>value: org.mcuosmipcuter.sqldog.sql.xml.column
	*/
	public static final String XML_KEY_COLUMN = "org.mcuosmipcuter.sqldog.sql.xml.column";
	/**
	* the default value for key org.mcuosmipcuter.sqldog.sql.xml.column
	* value: column
	*/
	public static final String XML_DEFAULT_COLUMN = "column";
	/**
	* the key to retrieve the string for the attribute number of the element column from the resource file
	* <br/>value: org.mcuosmipcuter.sqldog.sql.xml.column.attribute.number
	*/
	public static final String XML_KEY_COLUMN_ATTR_NUMBER = "org.mcuosmipcuter.sqldog.sql.xml.column.attribute.number";
	/**
	* the default value for key org.mcuosmipcuter.sqldog.sql.xml.column.attribute.number
	* value: n
	*/
	public static final String XML_DEFAULT_COLUMN_ATTR_NUMBER = "n";
	/**
	* the key to retrieve the string for attribute class of the element column from the resource file
	* <br/>value: org.mcuosmipcuter.sqldog.sql.xml.column.attribute.class
	*/
	public static final String XML_KEY_COLUMN_ATTR_CLASS = "org.mcuosmipcuter.sqldog.sql.xml.column.attribute.class";
	/**
	* the default value for key org.mcuosmipcuter.sqldog.sql.xml.column.attribute.class
	* value: class
	*/
	public static final String XML_DEFAULT_COLUMN_ATTR_CLASS = "class";
}
