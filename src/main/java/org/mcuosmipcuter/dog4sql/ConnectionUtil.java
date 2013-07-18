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
package org.mcuosmipcuter.dog4sql;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

import org.mcuosmipcuter.dog4sql.system.RuntimeClassLoader;

public class ConnectionUtil {
	static RuntimeClassLoader runtimeClassLoader = new RuntimeClassLoader();

	public static Connection getConnection(Login login)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, SQLException {

		String driverClassName = login.getDriver();
		String jdbcUrl = login.getUrl();
		String username = login.getUserName();
		String password = login.getPassWord();

		Driver d = (Driver) runtimeClassLoader.loadClass(driverClassName)
				.newInstance();
		Properties prop = new Properties();
		if (login.isUserNamePasswordSupplied()) {
			prop.put("user", username);
			prop.put("password", password);
		}

		return d.connect(jdbcUrl, prop);
	}

	public static void addJarFile(String name) {
		runtimeClassLoader.addJarFile(name);
	}

	public static void addExplDir(String name) {
		runtimeClassLoader.addExplDir(name);
	}

}
