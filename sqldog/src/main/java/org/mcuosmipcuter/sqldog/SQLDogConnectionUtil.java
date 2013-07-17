package org.mcuosmipcuter.sqldog;

import java.sql.*;
import java.util.*;

import org.mcuosmipcuter.sqldog.system.*;

public class SQLDogConnectionUtil
{
    static RuntimeClassLoader runtimeClassLoader = new RuntimeClassLoader();

	public static Connection getConnection(SQLDogLogin login)
	throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException
	{

        String driverClassName = login.getDriver();
        String jdbcUrl = login.getUrl();
        String username = login.getUserName();
        String password = login.getPassWord();

        Driver d = (Driver)runtimeClassLoader.loadClass(driverClassName).newInstance();
        Properties prop = new Properties();
		if(login.isUserNamePasswordSupplied())
        {
            prop.put("user", username);
            prop.put("password", password);
        }

    	return  d.connect(jdbcUrl, prop);
	}

	public static void addJarFile(String name)
	{
        runtimeClassLoader.addJarFile(name);
	}

    public static void addExplDir(String name)
    {
        runtimeClassLoader.addExplDir(name);
    }

}
