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
* factory to generate random words to be used as passwords etc.
*/
public class RandomWordFactory
{
	/**
	* the seperator for disallowed phrases, value ","
	*/
	public final static String SEPERATOR = ",";

	private final static int maxRandomCount = 10000;
	private static String characterHolder = 	"abcdefghijklmnopqrstuvwxyz"
											+	"ABCDEFGHIJKLMNOPQRSTUVWXYZ"
											+	"0123456789";
	/**
	* @param length of the random word to be generated<br>
	* the allowed characters are the alphabet lower and uppercase plus 0-9
	* @return the random word
	*/
	public static String getRandomWord(int len)
	{
		try
		{
			return getRandomWord(len, characterHolder, null);
		}
		catch(Exception e) { }	
		return null;
	}

	/**
	* @param length of the random word to be generated
	* @param string of allowed characters
	* @return the random word
	* @throws if the allowed characters are null or empty string
	*/
	public static String getRandomWord(int len, String allowedChars)
	throws Exception
	{
		return getRandomWord(len, allowedChars, null);
	}

	/**
	* @param length of the random word to be generated
	* @param string of allowed characters
	* @param string of disallowed phrases, seperated by comma<br>
	* the generated word will not contain those phrases
	* @return the random word
	* @throws if the allowed characters are null or empty string<br>
	* or the random word could not be constructed if the disallowed phrases<br>
	* are too restrictiv semantically to the allowed characters
	*/
	public static String getRandomWord(int len, String allowedChars, String disallowedPhrases)
	throws Exception
	{
		if(allowedChars == null || allowedChars.length() == 0)
			throw new Exception("invalid allowedChars String");
		Vector v = new Vector();
		if(disallowedPhrases != null)
		{
			StringTokenizer strtok = new StringTokenizer(disallowedPhrases, SEPERATOR);
			while(strtok.hasMoreTokens())
				v.add(strtok.nextToken());
		}
		String randomWord;
		int randomCount = 0;
		while(randomCount < maxRandomCount)
		{
			randomWord = "";
			randomCount++;
			boolean rejected = false;
			for(int i = 0; i < len; i++)
			{
				int pos = (int)(Math.random() * (double)(allowedChars.length()));
				if(pos == allowedChars.length())
					pos = 0;
				randomWord += allowedChars.charAt(pos);
			}
			if(v.size() > 0)
			{
				for(int i = 0; i < v.size(); i++)
				{
					if(randomWord.indexOf((String)v.elementAt(i)) > -1)
						rejected = true;
				}
			}
			if(rejected)
				continue;
			return randomWord;
		}
		throw new Exception("could not create random word");
	}

	/**
	* method to use factory from commandline<br>
	* usage: java org.mcuosmipcuter.sqldog.text.RandomWordFactory <br><length> [allowed] [disallowed]
	*/
	public static void main(String[] args)
	{
		if(args == null || args.length < 1)
		{
			System.err.println
	("usage: java org.mcuosmipcuter.sqldog.text.RandomWordFactory <length> [allowed] [disallowed]");	
			System.exit(1);
		}
		String rw = "";
		try
		{
			if(args.length == 1)
				rw = getRandomWord(Integer.parseInt(args[0]));
			if(args.length == 2)
				rw = getRandomWord(Integer.parseInt(args[0]), args[1]);
			if(args.length == 3)
				rw = getRandomWord(Integer.parseInt(args[0]), args[1], args[2]);
		}
		catch(Exception e)
		{
			e.printStackTrace();			
		}	
		System.out.println("random word: " + rw);	
	}	

}
