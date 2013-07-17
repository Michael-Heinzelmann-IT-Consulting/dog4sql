package org.mcuosmipcuter.sqldog.text;

import java.util.*;

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
