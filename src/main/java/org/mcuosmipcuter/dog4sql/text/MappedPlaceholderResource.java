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



import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.Vector;



/**

* @author Michael Heinzelmann

* takes a resource file as templates and replaces placeholders in there with mapped values

*/

public class MappedPlaceholderResource

{

/**

* string to setup a value in resource file followed by a number, value "value."

*/

	public static final String VALUE 	= "value.";

/**

* string to setup a separator in resource file followed by a number, value "separator."

*/

	public static final String SEP 	= "separator.";

/**

* string to setup a line in resource file followed by a number, value "line."

*/

	public static final String LIN 	= "line.";

/**

* string to setup a wordreplace in resource file followed by a number, value "wordreplace."

* wordreplace placeholders are only replaced if surrounded by separators

*/

	public static final String WORD 	= "wordreplace.";

/**

* string to denote a key of a value in resource file, value ".key"

*/

	public static final String KEY	= ".key";

/** 

* string to denote a member of a value in resource file followed by

* a number, value ".member."  

*/

	public static final String MEM	= ".member.";

/** 

* string to denote a sysvalue of a value in resource file followed

* by a number, value ".sysval."  

*/

	public static final String SYSVAL	= ".sysval.";

/** 

* string to denote a mapvalue of a value in resource file followed

* by a number, value ".mapval."  

*/

	public static final String MAPVAL	= ".mapval.";

/** 

* string to denote a default value in resource file, value ".default" 

*/

	public static final String DEFVAL	= ".default";

/**

* string to denote the number of members, value ".numberOfMembers"

*/

	public static final String NUMMEM 	= ".numberOfMembers";

/**

* string to denote the number of values, value "numberOfValues"

*/

	public static final String NUMVAL 	= "numberOfValues";

/**

* string to denote the number of lines, value "numberOfLines"

*/

	public static final String NUMLIN 	= "numberOfLines";

/**

* string to denote the number of seperators, value "numberOfSeperators"

*/

	public static final String NUMSEP 	= "numberOfSeparators";

/**

* string to denote the number of wordreplaces, value "numberOfWordReplace"

*/

	public static final String NUMWORD	= "numberOfWordReplace";



	private ResourceBundle resbun 		= null;

	private Hashtable hashtablesTable 	= null;

	private Hashtable defaultValTable 	= null;

	private Vector separators 			= null;

	private Vector wordreplace 			= null;



	protected int numberOfSeparators 	= 0;

	protected int numberOfWordReplace 	= 0;

	protected int numberOfValues 		= 0;

	protected int numberOfLines 		= 0;



	private boolean separatorsOk 	= false;

	private boolean wordreplaceOk 	= false;

	private boolean valuesOk 		= false;

	private boolean linesOk 		= false;



	private String templateBody = null;

	private String loadErrors = "";



/**

* constructor

* @param resource file base name

*/

	public MappedPlaceholderResource(String strResourceFile) throws Exception

	{

		initMappedPlaceholderResource(strResourceFile);

	}



/**

* this returns the body consisting of all lines in the lines section of the resourcefile <br>

* note that it is not required to use a body from the resourcefile<br>

* applications can also use a body containing placeholders retrieved external <br>

* e.g a from a file

* <pre>

* <br>typical usage in a main method:<br>

* public static void main( String[] args){

*   MappedPlaceholderResource mpr = new MappedPlaceholderResource(args[0]);

*   Hashtable h = new Hashtable();

*   for(int i = 1; i < args.length - 1; i += 2)

*       h.put(args[i], args[i + 1]);

*   System.out.println(mpr.mapPlaceholders( <b>mpr.getTemplateBody()</b>, h));<br>

* }<br>

* </pre>

* @return the template body constructed from the resource file

*/

	public String getTemplateBody()

	{

		return templateBody;

	}



/**

* returns a summary of all load errors, important for checking the resourcefiles syntax

* @return the load errors

* @see #main(String[] args)

*/

	public String getLoadErrors()

	{

		return loadErrors;

	}



/**

* applications can retrieve the major keys and iterate through the enumeration<br>

* so they dont 'forget' to provide a value which is set in the resource template

* <pre>

* ...

* Enumeration enum = <b>mpr.getMajorKeys()</b>;

* Hashtable h = new Hashtable();

* while(enum.hasMoreElements())

* {

* 	Object e = enum.nextElement();

* 	h.put(e.toString(), getMyValue(e)); // gets the runtime value for element

* }

* LoadedFile = mpr.mapPlaceholders(LoadedFile, h);

* ...

* </pre>

* @return an enumeration of the major keys

*/

	public Enumeration getMajorKeys()

	{

		return hashtablesTable.keys();

	}

/**

* @return the number of values in this MappedPlaceholoderResource

*/

	public int getNumberOfValues() {return numberOfValues;}



/**

* @return the number of lines of the body in this MappedPlaceholoderResource

*/

	public int getNumberOfLines() {return numberOfLines;}



/**

* @return the number of separators in this MappedPlaceholoderResource

*/

	public int getNumberOfSeparators() {return numberOfSeparators;}



/**

* @return the number of word replace in this MappedPlaceholoderResource

*/

	public int getNumberOfWordReplace() {return numberOfWordReplace;}



/**

* @return true if section separators is at all loaded from the resource file

* @see #main(String[] args)

*/

	public boolean isSectionSeparatorsLoaded() {return (separators != null);}



/**

* @return true if section wordreplace is at all loaded from the resource file

* @see #main(String[] args)

*/

	public boolean isSectionWordReplaceLoaded() {return (wordreplace != null);}



/**

* @return true if section values is at all loaded from the resource file

* @see #main(String[] args)

*/

	public boolean isSectionValuesLoaded() {return (hashtablesTable != null);}



/**

* @return true if section lines is at all loaded from the resource file

* @see #main(String[] args)

*/

	public boolean isSectionLinesLoaded() {return (templateBody != null);}



/**

* @return true if section separators is loaded without errors from the resource file

* @see #main(String[] args)

*/

	public boolean isSectionSeparatorsLoadedOk() {return separatorsOk;}



/**

* @return true if section wordreplace is loaded without errors from the resource file

* @see #main(String[] args)

*/

	public boolean isSectionWordReplaceLoadedOk() {return wordreplaceOk;}



/**

* @return true if section values is loaded without errors from the resource file

* @see #main(String[] args)

*/

	public boolean isSectionValuesLoadedOk() {return valuesOk;}



/**

* @return true if section lines is loaded without errors from the resource file

* @see #main(String[] args)

*/

	public boolean isSectionLinesLoadedOk() {return linesOk;}



/**

* initialises the resource and the hashtable of hashtables

* @param basename of the resource file template

* @return void

*/

	private void initMappedPlaceholderResource(String strResourceFile)

	throws java.util.MissingResourceException, Exception

	{

		// load resourcefile

		resbun = ResourceBundle.getBundle(strResourceFile);



		try

		{

			numberOfSeparators = Integer.parseInt(resbun.getString(NUMSEP));

			separators = new Vector();

			for(int i = 0; i < numberOfSeparators; i++)

			{

				separators.add(resbun.getString(SEP + i));

			}

			separatorsOk = true;

		}

		catch(java.util.MissingResourceException mrexSep)

		{

			loadErrors += mrexSep.getMessage() + "\n";

		}



		try

		{

			numberOfWordReplace = Integer.parseInt(resbun.getString(NUMWORD));

			wordreplace = new Vector();

			for(int i = 0; i < numberOfWordReplace; i++)

			{

				wordreplace.add(resbun.getString(WORD + i));

			}

			wordreplaceOk = true;

		}

		catch(java.util.MissingResourceException mrexWord)

		{

			loadErrors += mrexWord.getMessage() + "\n";

		}



		try

		{

			numberOfValues = Integer.parseInt(resbun.getString(NUMVAL));

			hashtablesTable = new Hashtable();

			defaultValTable = new Hashtable();



			for(int i = 0; i < numberOfValues; i++)

			{

				int m = Integer.parseInt(resbun.getString(VALUE + i + NUMMEM));

				String key = resbun.getString(VALUE + i + KEY);

				int n =Integer.parseInt(resbun.getString(key + "." + NUMVAL));

				for(int l = 0; l < m; l++)

				{

					String memkey = resbun.getString(key + MEM + l);

					Hashtable h = new Hashtable();

					for(int j = 0; j < n; j++)

					{

						String sv = resbun.getString(key + SYSVAL + j);

						String mv = resbun.getString(key + MAPVAL + j);

						String dv = resbun.getString(key + DEFVAL);

						defaultValTable.put(memkey, dv);

						mv = Replace.replaceAll(mv, key, memkey);

						h.put( sv, mv);

					}

					hashtablesTable.put(memkey, h);

				}

			}

			valuesOk = true;

		}

		catch(java.util.MissingResourceException mrexVal)

		{

			loadErrors += mrexVal.getMessage() + "\n";

		}



		try

		{

			numberOfLines = Integer.parseInt(resbun.getString(NUMLIN));

			templateBody = "";

			for(int i = 0; i < numberOfLines; i++)

			{

				templateBody += resbun.getString(LIN + i ) + "\n";

			}

			linesOk = true;

		}

		catch(java.util.MissingResourceException mrexLin)

		{

			loadErrors += mrexLin.getMessage() + "\n";

		}

		resbun = null;



	}



/**

* map the values from the 2 dimensional hashtable

* @param key

* @param value

* @return String

*/

	private String mapValue(String key, String value)

	{

		try

		{

			String rs = (String)((Hashtable)hashtablesTable.get(key)).get(value);

			if(rs == null)

				return value;



			return rs;

		}

		catch(Exception e)

		{

			return value;

		}

	}



/**

* map placeholder in String<br>

* <b><a name="warn1">Warning:</a><br>

* Use this method only if you want to control the replace process yourself

* as subsequent calls to the method providing the same body string with values

* that are substrings of former ones can result in unexpected replacements or

* even endless loops. In all other cases use<br>

* mapPlaceholders(String inputBody,  Hashtable inputHash)<br>

* instead. This method takes automatically care of the correct replacement of

* multiple name value pairs.</b>

* @param string to replace and map

* @param key (placeholder)

* @param value which will be mapped and replaces the placeholder if found

* @return String

* @see #mapPlaceholders(String inputBody,  Hashtable inputHash)

*/

	public String mapPlaceholder(String S, String key, String value)

	{

		return Replace.replaceAll(S, key, mapValue(key, value));

	}



/**

* map placeholder in String

* <b><a href="#warn1">same caveats as for mapPlaceholder(String S, String key, String value)</a></b>

* @param string to replace and map

* @param key (placeholder)

* @param value which will be mapped and replaces the placeholder if found

* @param match whole word only

* @return String

* @see #mapPlaceholders(String inputBody,  Hashtable inputHash)

*/

	public String mapPlaceholder(String S, String key, String value, boolean matchWord)

	{

		if(matchWord)

			return replaceAllWords(S, key, mapValue(key, value));

		else

			return Replace.replaceAll(S, key, mapValue(key, value));

	}



/**

* compare with loaded separator strings

* @param the string to compare

* @return true if the string equals one of the given separators else false

*/

	private boolean equalsSeparator(String s)

	{

		if(separators == null)

			return false;



		return separators.contains(s);

	}



/**

* replaces all occurences of what with with in string only if surrounded by whitespace

* @param string to make replacements in

* @param what

* @param with

* @return String all occurences replaced or original string

*/

	protected String replaceAllWords(String S, String strWhat, String strWith)

	{



		if(separators == null || S == null || strWhat.equals(strWith))

			return S;



		int pos;

		int begin = 0;

		String returnS = S;

		String before;

		String after;



		while((pos = S.indexOf(strWhat, begin)) != -1)

		{

			before = " ";

			after = " ";

			if(pos > 0 && pos < S.length() -1)

			{

				before = S.substring(pos - 1, pos);

				after = S.substring(pos + strWhat.length(), pos + strWhat.length() + 1);

			}

			// compare to whitespace character combinations

			if( !(equalsSeparator(before) && equalsSeparator(after)) )

			{

				begin = pos + strWhat.length();

				continue;// is not surrounded by whitespace - no replace

			}

			returnS = S.substring(0, pos) + strWith + S.substring(pos + strWhat.length());

			begin = pos + strWith.length();

			S = returnS;

		}

		return returnS;

	}



/**

* replaces and maps all occurences of keys of the hashtable with the values of the hashtable 

* @param string to make replacements in

* @param hashtable input key-value pairs

* @return String all occurences replaced or original string

*/

	public String mapPlaceholders(String inputBody,  Hashtable inputHash)

	{

		if(inputBody == null)

			return null;

		if(inputHash == null)

			return inputBody; 

		Enumeration enumeration = inputHash.keys();

		int maxlength = 0;

		int numkeys = 0;

		while(enumeration.hasMoreElements())

		{

			int l = ((String)(enumeration.nextElement())).length();

			numkeys++;

			if(l > maxlength)

				maxlength = l;

		}



		// order by stringlength

		Vector orderedKeys = new Vector();

		int j = 0;

		for(int i = maxlength; i > 0; i--)

		{

			enumeration = inputHash.keys();

			while(enumeration.hasMoreElements())

			{

				String s = enumeration.nextElement().toString();

				if(s.length() == i)

				{

					orderedKeys.add(s);

					j++;

				}

			}



		}



		int delta = 0;

		int begin = 0;

		int pos = 0;

		int holdpos = 0;

		int nextBegin = 0;

		boolean found = false;

		boolean marked = false;

		boolean replace = false;

		String before = null;

		String after = null;

		String strWhat = null;

		String strWith = null;

		String s = null;

		Object objVal = null;



		do

		{

			nextBegin = inputBody.length();// last possible index + 1

			found = false;

			marked = false;

			delta = 0;

			int i = 0; 

			while( i < orderedKeys.size())

			{

				s = orderedKeys.elementAt(i).toString();



				pos = inputBody.indexOf(s, begin);

				if(pos != -1)

				{

					found = true;

				}

				else

				{

					orderedKeys.removeElementAt(i);

					i--;

				}

				if(pos != begin)

				{

					if(pos != -1 && pos < nextBegin)

						nextBegin = pos;

				}

				else{

					if(!marked)

					{

						holdpos = pos;

						objVal = inputHash.get(s);

						if(objVal == null || objVal.toString().equals(""))

							objVal = defaultValTable.get(s);

						if(objVal == null)

							objVal = new String("");

						strWith = mapValue(s, objVal.toString());

						strWhat = s;

						marked = true;

						pos = inputBody.indexOf(s, begin + s.length());

						if(pos != -1 && pos < nextBegin)

							nextBegin = pos;

					}

				}

				i++;

			}

			if(wordreplace != null && wordreplace.contains(strWhat))

			{

				if(holdpos > 0)

					before = inputBody.substring(holdpos - 1, holdpos);

				if(holdpos < inputBody.length() - 2)

					after = inputBody.substring(holdpos + strWhat.length(),

								 holdpos + strWhat.length() + 1);

				replace  = (equalsSeparator(before) && equalsSeparator(after));

			}

			else {replace = true;}



			if(replace && marked)

			{

			inputBody = inputBody.substring(0, holdpos) 

							+ strWith

							+ inputBody.substring(holdpos + strWhat.length());

			delta = strWith.length() - strWhat.length();

			}

			begin = nextBegin + delta;



		}while(found);



		return inputBody;

	}



/**

* main method to use class from commandline or syntaxchecking of the resourcefile

* prints the loaderrors typos etc. <br><b>Make sure to examine the messages carefully.<br>

* Edit the resourcefile if possible in an advanced text org.mcuosmipcuter.dog4sql.editor where you can

* check non-printable characters and trailing spaces</b><br>

* sections are all optional, these are: separators, wordreplace, values, lines

* if there are sections missing in the  resourcefile it is displayed in the load errors 

* but you can ignore the errors if you don't need those sections<br>

* An application can check if the sections are provided at runtime by calling the methods

* @see #isSectionSeparatorsLoaded()

* @see #isSectionWordReplaceLoaded()

* @see #isSectionValuesLoaded()

* @see #isSectionLinesLoaded()

* @see #isSectionSeparatorsLoadedOk()

* @see #isSectionWordReplaceLoadedOk()

* @see #isSectionValuesLoadedOk()

* @see #isSectionLinesLoadedOk()

* @see #getLoadErrors()

* @param resourcefile basename [[key] [value]] ...

*/

	public static void main( String[] args)

	{

		if(args == null || args.length == 0)

		{

			System.err.println(

			"usage: java MappedPlaceholderResource <resourcefile basename> [[key] [value]] ...");

			System.exit(1);

		}

		try

		{

			MappedPlaceholderResource mpr = new MappedPlaceholderResource(args[0]);

			Hashtable h = new Hashtable();

			for(int i = 1; i < args.length - 1; i += 2)

			{

				h.put(args[i], args[i + 1]);

			}

			if(mpr.isSectionSeparatorsLoaded())

				System.out.println("section separators loaded correctly: " + 

									mpr.isSectionSeparatorsLoadedOk());

			if(mpr.isSectionWordReplaceLoaded())

				System.out.println("section wordreplace loaded correctly: " + 

									mpr.isSectionWordReplaceLoadedOk());

			if(mpr.isSectionValuesLoaded())

				System.out.println("section values loaded correctly: " + 

									mpr.isSectionValuesLoadedOk());

			if(mpr.isSectionLinesLoaded())

				System.out.println("section lines loaded correctly: " + 

									mpr.isSectionLinesLoadedOk());

			System.out.println("load errors:\n" + mpr.getLoadErrors());

			System.out.println(mpr.mapPlaceholders(mpr.getTemplateBody(), h));

		}

		catch(java.util.MissingResourceException mre)

		{

			System.err.println("Missing Resource Exception: " + mre.getMessage());

		}

		catch(Exception e)

		{

			System.err.println("Exception: " + e.getMessage());

			e.printStackTrace();

		}

	}





}



