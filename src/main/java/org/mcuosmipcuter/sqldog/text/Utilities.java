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
package org.mcuosmipcuter.sqldog.text;

import java.util.StringTokenizer;
import java.util.Vector;

/**
* Utilities class for text operations
*/
public class Utilities
{

	/**
	* converts string containing tokens separated by delim to string array
	* @param the whole string
	* @param the delimiter used in the srtring to separate the tokens
	* @return an array representing the tokeized string
	*/
	public static String[] tokenString2stringArray(String tokenString, String delim)
	{
		Vector v = new Vector();
		StringTokenizer strtok = new StringTokenizer(tokenString, delim);
		while(strtok.hasMoreTokens())
			v.add(strtok.nextToken());
		String[] retArr = new String[v.size()];
		for(int i = 0; i < v.size(); i++)
			retArr[i] = (String)v.elementAt(i);
		return retArr;
	}
	
	/**
	* main method for testing
	*/
	public static void main(String[] args)
	{
		try
		{
			String[]arr = tokenString2stringArray(args[0], args[1]);
			 for(int i = 0; i < arr.length; i++)
				System.out.println("token" + i + ":" + arr[i]);	
		}
		catch(Exception e)
		{
			System.err.println("usage: java org.mcuosmipcuter.sqldog.text.Utilities tokenstring delim");	
		}	
	}	
}
