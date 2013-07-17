/*
 * michael Created on Oct 10, 2004
*/
package org.mcuosmipcuter.sqldog;

/**
 * @author michael 2004
 */
public abstract class SQLDogWorker extends Thread
{
	private boolean hasRun = false;
	
	public SQLDogWorker()
	{
	}

	public final void run(){
		if(hasRun){
			throw new RuntimeException(this + " can only be run once!");
		}
		hasRun = true;
		SQLDogController.trace(5, "run() for: " + this + " " + getClass().getName());
		runImpl();
	}
	protected abstract void runImpl();
	
	protected void finalize()
		throws Throwable
	{
		SQLDogController.trace(5, "finalize() for: " + this + " " + getClass().getName());
		super.finalize();
	}
}
