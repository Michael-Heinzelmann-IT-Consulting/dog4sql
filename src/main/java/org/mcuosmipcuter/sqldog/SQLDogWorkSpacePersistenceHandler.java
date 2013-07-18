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
package org.mcuosmipcuter.sqldog;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileOutputStream;

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
