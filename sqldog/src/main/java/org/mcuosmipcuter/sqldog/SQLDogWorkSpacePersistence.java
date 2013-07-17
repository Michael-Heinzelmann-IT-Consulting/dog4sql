package org.mcuosmipcuter.sqldog;

public interface SQLDogWorkSpacePersistence
{
	abstract SQLDogWorkSpace getWorkSpace();
	abstract void setWorkSpace(SQLDogWorkSpace workSpace);
    abstract String getName();
}
