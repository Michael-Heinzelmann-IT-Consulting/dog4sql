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

/**
* string padding class
*/
public class Pad
{
	/**
	* static method to pad String str with pstr digits times
	* @param str the string to pad
	* @param pstr the pad string
	* @param digits the number of digits to pad, negative numbers pad to the left, positive to the right
	* @return the padded string
	*/
   public static String pad(String str, String pstr, int digits)
   {
       String pads = "";
       for (int i = 0; i < Math.abs(digits) - str.length(); i++)
           pads += pstr;
       if (digits < 0)
           return pads + str;
       return str + pads;
   }

    /**
   * static method to pad String str with pad character digits times
   * @param str the string to pad
   * @param padChar the pad character
   * @param digits the number of digits to pad, negative numbers pad to the left, positive to the right
   * @return the padded string
   */
    public static String fastPad(String str, char padChar, int digits)
    {
        char[] padArray = new char[Math.abs(digits) - str.length()];
        for(int i = 0; i < padArray.length; i++)
            padArray[i] = padChar;
        return digits > 0 ? str + (new String(padArray)) : (new String(padArray)) + str;
    }

	public static void main(String[] args)
	{
		if(args == null || args.length < 3)
		{
			System.err.println("usage: java Pad <str to pad> <pad string> <digits to pad>");
			System.exit(1);
		}
		 System.out.println(pad(args[0], args[1], Integer.parseInt(args[2])));
	}
}
