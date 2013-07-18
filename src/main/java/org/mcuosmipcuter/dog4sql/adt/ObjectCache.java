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
package org.mcuosmipcuter.dog4sql.adt;

import org.mcuosmipcuter.dog4sql.text.Pad;

/**
* wraps a CircularBuffer by means of spezialisation to store and retrieve<br>
* key-value pairs of Objects. The keys are treated unique as opposed to<br>
* CircularBuffer which just dumps the new objects. As with CircularBuffer<br>
* once the capacity limit is reached and a new element is added the oldest<br>
* element is automatically discarded and lost.<br>
* This class can be used as an accessible cache with limited size
*/
public class ObjectCache
{
	private CircularBuffer buf;

	/**
	* default constructor with cachesize 10
	*/
	public ObjectCache()
	{
		this(10);
	}

	/**
	* constructor
	* @param cachesize
	*/
	public ObjectCache(int size)
	{
		buf = new CircularBuffer(size);
	}

	/**
	* puts the key value pair into the cache only if the key is not already<br>
	* contained, else only the value of that key is refreshed.
	* @param key
	* @param value
	*/
	public synchronized void putKeyValuePair(Object key, Object value)
	{
		Object[] arr = buf.getAllElements();
		for(int i = 0; i < arr.length; i++)
		{
			Object[] oA = (Object[])arr[i];
			if(oA[0].equals(key))
			{
				oA[1] = null;
				oA[1] = value;
				return ;
			}
		}
		buf.addElement(new Object[] { key, value});
	}

	/**
	* gets the value corresponding to a key
	* @param key
	* @return the value if key is contained, else null
	*/
	public synchronized Object getValue(Object key)
	{
		Object[] arr = buf.getAllElements();
		for(int i = 0; i < arr.length; i++)
		{
			Object[] oA = (Object[])arr[i];
			if(oA[0].equals(key))
				return oA[1];
		}
		return null;
	}

	/**
	* retrieves all key-value pairs in the cache
	* @return 2 dimensional array cachesize * 2
	*/
	public synchronized Object[][] getAllKeyValuePairs()
	{
		Object[] arr = buf.getAllElements();
		Object[][] ret = new Object[arr.length][2];
		for(int i = 0; i < arr.length; i++)
		{
			Object[] oA = (Object[])arr[i];
			ret[i][0] = oA[0];
			ret[i][1] = oA[1];
		}
		return ret;
	}

	/**
	* demo method
	*/
	public static void main(String[] args)
	{
		System.out.println("creaing ObjectCache with size 5");
		ObjectCache oc = new ObjectCache(5);
		try
		{
			String k = "";
			String v = "";
			for(int i = 0; i < 16; i++)
			{
				if(i == 3|| i == 4 || i == 8 || i == 9)
				{
					k = "key" + Pad.pad("" + (i - 2), "0", -3);
					v = "val" + Pad.pad("" + i, "#", -3);
					System.out.print("now using an contained key:" + k + ", " + "value:" + v);
					System.out.print(" watch for hashmarks");
				}
				else
				{
					k = "key" + Pad.pad("" + i, "0", -3);
					v = "val" + Pad.pad("" + i, "0", -3);
					System.out.print("putting key:" + k + ", " + "value:" + v);
				}
				oc.putKeyValuePair(k, v);
				System.out.print("\ncache contents: ");
				Object[][] cont = oc.getAllKeyValuePairs();
				for(int j = 0; j < cont.length; j++)
					System.out.print(" " + cont[j][0] + "=" + cont[j][1] + "  ");
				System.out.println("");
				Thread.sleep(2000);
			}
		}
		catch(Exception e)
		{
			System.err.println("exception: " + e);
			e.printStackTrace();
		}
	}

}
