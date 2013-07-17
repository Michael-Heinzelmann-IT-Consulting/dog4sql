package org.mcuosmipcuter.sqldog.adt;
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
