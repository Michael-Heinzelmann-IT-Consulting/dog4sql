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
