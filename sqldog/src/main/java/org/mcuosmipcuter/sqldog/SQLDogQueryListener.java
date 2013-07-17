package org.mcuosmipcuter.sqldog;

public interface SQLDogQueryListener
{
	public abstract void onQueryMessage(String message);
	public abstract void onQueryException(Exception ex);
	public abstract void onResult(SQLDogResultWrapper rw);
}
