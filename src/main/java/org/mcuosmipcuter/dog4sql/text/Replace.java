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
package org.mcuosmipcuter.dog4sql.text;


/**
* string replace class with undo stack supports quasi unlimited number of undo's
*/
public class Replace implements java.io.Serializable
{
	private int numberOfReplacements;
	private int position;
	private boolean lastSuccess;
	private String currentString;
	private java.util.Stack replaceDataStack;

	/**
	* default constructor
	*/
	public Replace()
	{
		currentString = "";
		replaceDataStack = new java.util.Stack();
	}

	/**
	* constructs a Replace object with S as currentString
	*/
	public Replace(String S)
	{
		currentString = S;
		replaceDataStack = new java.util.Stack();
	}

	/**
	* static method to replace in String S what with with
	* @param String the string
	* @param String what
	* @param String with
	* @return String first occurrence replaced or original input string if not found
	*/
	public static String replaceFirst(String S, String strWhat, String strWith)
	{
		return replaceString(null, S, strWhat, strWith, 1, 0);
	}

	/**
	* method to replace in currentString what with with
	* @param String what
	* @param String with
	* @return String first occurrence replaced or original input string if not found
	*/
	public synchronized String replaceFirst(String strWhat, String strWith)
	{
		return (currentString = replaceString(this, currentString, strWhat, strWith, 1, 0));
	}

	/**
	* method to replace in currentString what with with
	* @param String what
	* @param String with
	* @param int start
	* @return String first occurrence replaced or original input string if not found
	*/
	public synchronized String replaceFirst(String strWhat, String strWith, int start)
	{
		return (currentString = replaceString(this, currentString, strWhat, strWith, 1, start));
	}

	/**
	* static method to replace in currentString what with with
	* @param String the string
	* @param String what
	* @param String with
	* @param int start
	* @return String first occurrence replaced or original input string if not found
	*/
	public static String replaceFirst(String S, String strWhat, String strWith, int start)
	{
		return replaceString(null, S, strWhat, strWith, 1, start);
	}

	/**
	* static method to replace in String S what with with
	* @param String the string
	* @param String what
	* @param String with
	* @return String last occurrence replaced or original input string if not found
	*/
	public static String replaceLast(String S, String strWhat, String strWith)
	{
		return replaceString(null, S, strWhat, strWith, 1, S.lastIndexOf(strWhat));
	}

	/**
	* method to replace in currentString what with with
	* @param String what
	* @param String with
	* @return String last occurrence replaced or original input string if not found
	*/
	public synchronized String replaceLast(String strWhat, String strWith)
	{
		return (currentString = replaceString(this, currentString, 
									strWhat, strWith, 1, currentString.lastIndexOf(strWhat)));
	}

	/**
	* static method to replace in String S what with with
	* @param String the string
	* @param String what
	* @param String with
	* @return String all occurrences replaced or original input string if not found
	*/
	public static String replaceAll(String S, String strWhat, String strWith)
	{
		return replaceString(null, S, strWhat, strWith, -1, 0);
	}

	/**
	* method to replace in currentString what with with
	* @param String what
	* @param String with
	* @return String all occurrences replaced or original input string if not found
	*/
	public synchronized String replaceAll(String strWhat, String strWith)
	{
		return (currentString = replaceString(this, currentString, strWhat, strWith, -1, 0));
	}

	/**
	* private static method to replace in String S what with with to limit of occurences
	* if limit < 0 unlimited occurrences are replaced
	* @param Replace object
	* @param String the string
	* @param String what
	* @param String with
	* @return String occurrences up to limit replaced or original input string if not found
	* @param  start of replacements
	*/
	private static synchronized String replaceString(Replace r, String S, 
								String strWhat, String strWith, int limit, int start)
	{
		boolean undos = false;
		// this method doesnt throw exceptions - all erreonous parameters just cause 'not found'
		if(	S == null || strWhat == null || strWith == null ||
			strWhat.equals(strWith) || (limit < 1 && strWhat.equals("")))
			return S;
		// enable undo for sp. case empty 
		if(strWhat.equals("") && start >= S.length()) 
		{
			S += "-";
			undos = true;
		}

		String retString = S;// holds temporary string
		int pos;// holds position
		int begin = start;
		if(r != null)
		{
			begin = r.position;
			r.numberOfReplacements = 0;
			r.lastSuccess = false;
		}
		while((pos = S.indexOf(strWhat, begin)) != -1 && limit != 0)
		{
			retString = S.substring(0, pos) + strWith + S.substring(pos + strWhat.length());
			begin = pos + strWith.length();
			S = retString;
			if(r != null)
			{
				r.numberOfReplacements++;
				r.replaceDataStack.push(new Object[] {new Integer(pos), strWhat, strWith});
				if(pos == -1)
					r.position = 0;
				else
					r.position = begin;
				r.lastSuccess = true;
			}
			limit--;
		}
		if(undos)
			return(S.substring(0, S.length() - 1));
		else
			return retString;
	}

	/**
	* getter method
	* @return the currentString
	*/
	public boolean isLastSuccess()
	{
		return lastSuccess;
	}

	/**
	* getter method
	* @return the currentString
	*/
	public synchronized String getCurrentString()
	{
		return currentString;
	}

	/**
	* getter method
	* @return the total number of replacements
	*/
	public int getTotalNumberOfReplacements()
	{
		return replaceDataStack.size();
	}

	/**
	* getter method
	* @return the number of replacements done in the last replace
	*/
	public int getNumberOfReplacements()
	{
		return numberOfReplacements;
	}

	/**
	* getter method
	* @return the current position in the replace
	*/
	public int getPosition()
	{
		return position;
	}

	/**
	* setter method
	* @param the new position in the replace
	*/
	public void setPosition(int position)
	{
		this.position = position;
	}

	/**
	* undo all replacements
	* @return true if undo was done false if nothing to undo
	*/
	public synchronized int unDoAll()
	{
		int s = replaceDataStack.size();
		while(unDoLast()){;}
		return s;
	}

	/**
	* undo last replacement
	* @return true if undo was done false if nothing to undo
	*/
	public synchronized boolean unDoLast()
	{
		Object o[] = null;

		if(replaceDataStack == null)
			return false;
		try
		{
			o = (Object[])replaceDataStack.pop();
		}
		catch(java.util.EmptyStackException ese)
		{
			return false;
		}
		currentString = replaceString(null, currentString, 
								(String)o[2], (String)o[1], 1, ((Integer)o[0]).intValue());	
		position = ((Integer)o[0]).intValue(); // move position back, not done in prev. meth.call
		return true;
	}

	/**
	* method to test from commandline
	*/
	public static void main(String[] args)
	{
		if(args == null || args.length !=3)
		{
			System.err.println("usage: java Replace <instring> <what> <with>");
			System.exit(1);
		}

		System.out.println("creating Replace object r ...");
		Replace r = new Replace(args[0]);
		System.out.println("r after method replaceFirst: \t" + r.replaceFirst(args[1], args[2]));
		System.out.println("r has number replacements: \t" + r.getTotalNumberOfReplacements());

		System.out.println("static method replaceFirst: \t" + replaceFirst(args[0],args[1], args[2]));
		System.out.println("r has number replacements: \t" + r.getTotalNumberOfReplacements());

		System.out.println("r after method replaceLast: \t" + r.replaceLast(args[1], args[2]));
		System.out.println("r has number replacements: \t" + r.getTotalNumberOfReplacements());

		System.out.println("r after method replaceAll: \t" + r.replaceAll(args[1], args[2]));
		System.out.println("r has number replacements: \t" + r.getTotalNumberOfReplacements());

		System.out.println("================= history =================");
		
		for(int i = 0; i < r.replaceDataStack.size(); i++)
		{
			  System.out.print("at position ");
			  System.out.print(((Object[])(r.replaceDataStack.elementAt(i)))[0].toString());
			  System.out.print("\t what: ");
			  System.out.print(((Object[])(r.replaceDataStack.elementAt(i)))[1].toString());
			  System.out.print(" with: ");
			System.out.println(((Object[])(r.replaceDataStack.elementAt(i)))[2].toString());
		}
		System.out.println("================= undo =================");

		while(r.unDoLast())
		{
			System.out.println("after method unDoLast: \t" + r.currentString);
			System.out.println("r has number replacements:\t" + r.getTotalNumberOfReplacements());
		}
	}

}
