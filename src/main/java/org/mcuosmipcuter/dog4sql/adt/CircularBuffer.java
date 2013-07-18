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
/**
* @author Michael Heinzelmann
* a circular buffer for various uses
*/
public class CircularBuffer
{
	protected int bufSize;
	protected int rwPointer = 0;
	protected int currSize = 0;
	protected Object[] bufferArray;

	/**
	* default constructor with buffer size 10
	*/
	public CircularBuffer()
	{
		this(10);
	}

	/**
	* construct cirular buffer of objects
	* @param the buffer size
	*/
	public CircularBuffer(int bufSize)
	{
		this.bufSize = bufSize;
		bufferArray = new Object[bufSize];
	}
	
	/**
	* adds an element, by means of a circular buffer :<br>
	* if the buffer is not yet full the element is just added at the end<br>
	* if the buffer is full the oldest element is removed to make room for this element
	* @param the Object to insert
	*/
	public void addElement(Object obj)
	{
		bufferArray[rwPointer] = null;
		bufferArray[rwPointer] = obj;
		if(rwPointer < bufSize - 1)
			rwPointer++;
		else
			rwPointer = 0;

		if(currSize < bufSize)
			currSize++;
	}

	/**
	* looks up if an Object is contained in this buffer using Object.equals(Object obj)
	* @param the object
	* @return true if the object is found 
	*/
	public boolean isContained(Object obj)
	{
		for(int i = 0; i < currSize; i++)
			if(bufferArray[i].equals(obj))
				return true;
		return false;
	}

	/**
	* @return the elements in the order they have been added
	*/
	public Object[] getAllElements()
	{
		final int cs = currSize;
		Object[] retArray = new Object[cs];
		if(cs < bufSize)
		{
			for(int i = 0; i < cs; i++)
				retArray[i] = bufferArray[i];
		}
		else
		{
			int j = 0;
			for(int i = rwPointer; i < bufSize; i++)
				 retArray[j++] = bufferArray[i];
			for(int i = 0; i < rwPointer; i++)
				 retArray[j++] = bufferArray[i];
		}
		return retArray;
	}


	/**
	* @return the elements in the order they are stored in the buffer<br>
	* provided for applications which are interested in the underlaying buffer<br>
	* sequence of the elements e.g. graphics drawing
	*/
	public Object[] getBuffer()
	{
		if(currSize < bufSize)
			return getAllElements();		
		else
			return bufferArray;
	}
	
	/**
	* @return the pointer position
	*/
	public int getPointerPosition()
	{
		return rwPointer;
	}
	
	/**
	* main method for testing	
	*/
	public static void main(String[] args)
	{
		CircularBuffer cb = new CircularBuffer(10);		
		for(int i = 0; i < 11; i++)
			cb.addElement(new Integer(i));
		Object[] S = cb.getAllElements();
		for(int i = 0; i < S.length; i++)
			System.out.println("element " + i + ": " + S[i]);
		
	}

}
