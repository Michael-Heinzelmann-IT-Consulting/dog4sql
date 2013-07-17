package org.mcuosmipcuter.sqldog;

import java.io.*;
import java.beans.*;

class SQLDogWorkSpacePersistenceHandler
{
	String configFileFullName;
	//SQLDogWorkSpace[] workSpaces;
	SQLDogWorkSpacePersistence[] workSpacesPersisters;

	public SQLDogWorkSpacePersistenceHandler(SQLDogWorkSpacePersistence[] workSpacesPersisters)
	{
		this.workSpacesPersisters = workSpacesPersisters;
		//workSpaces = new SQLDogWorkSpace[workSpacesPersisters.length];
		String homeDir = System.getProperties().get("user.home").toString();
		if(homeDir == null)
			homeDir = ".";
		String fileSeparator = System.getProperties().get("file.separator").toString();
		configFileFullName = homeDir + fileSeparator + SQLDogConstants.CONFIG_FILE_NAME;
	}

	public void saveWorkSpaces()
	{
         /*
		boolean unchanged = true;

		for(int i = 0; i < workSpaces.length; i++)
		{
			if(workSpaces[i] == null
				|| !workSpaces[i].equals(workSpacesPersisters[i].getWorkSpace()))
			{
				SQLDogController.trace(4, "workspace modified: " + workSpaces[i]);
				workSpaces[i] = workSpacesPersisters[i].getWorkSpace();
				unchanged = false;
			}
		}

		if(unchanged)
		{
			SQLDogController.trace(3, "workspaces unchanged.");
			return;
		}
             */

        XMLEncoder xe = null;
		try
		{
			FileOutputStream fos = new FileOutputStream(configFileFullName);

            xe = new XMLEncoder(fos);
            java.util.Hashtable h = new java.util.Hashtable();

            for(int i = 0; i < workSpacesPersisters.length; i++)
            {
                h.put(workSpacesPersisters[i].getName(), workSpacesPersisters[i].getWorkSpace());
            }
            xe.writeObject(h);
		}
		catch(Exception ex)
		{
			SQLDogController.trace(3, ex);
			SQLDogController.trace(2, "could not save configuration to " + configFileFullName);
		}
        finally
        {
            if(xe != null)
                xe.close();
        }
	}

	public void restoreWorkSpaces() throws Exception
	{
        XMLDecoder d = null;
		try
		{
			FileInputStream fis = new FileInputStream(configFileFullName);
            d = new XMLDecoder(fis);
            Object o = d.readObject();
            if(o instanceof java.util.Hashtable)
            {
                java.util.Hashtable h = (java.util.Hashtable) o;
                for (int i = 0; i < workSpacesPersisters.length; i++)
                {
                    System.out.println("ws:" + workSpacesPersisters[i]);
                    try
                    {
                        SQLDogWorkSpace ws = (SQLDogWorkSpace) h.get(
                            workSpacesPersisters[i].getName());
                        workSpacesPersisters[i].setWorkSpace(ws);
                    }
                    catch (ClassCastException ex)
                    {
                        SQLDogController.trace(1, "exception restoring workspaces: " + ex.getMessage());
                    }
                }

            }
            else
            {
                throw new Exception(o.getClass() + ", wrong class received while reading from " + configFileFullName);
            }
		}
		catch(Exception e)
		{
			e.printStackTrace();
			SQLDogController.trace(2, "could not read configuration from " + configFileFullName);
			throw e;
		}
		finally
		{
            d.close();
		}
	}

	protected static boolean workSpaceBasicCheck(Class wsClass, String version, Object compare)
	{
		if(compare == null || !(wsClass.equals(compare.getClass())))
			return false;

		return true;
	}

}
