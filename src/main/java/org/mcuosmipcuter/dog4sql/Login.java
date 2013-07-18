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

public class Login implements java.io.Serializable
{
	/**
	* <cvs/>name of login, this fields acts as primary key to identify the login
	*/
	private String name;
	/**
	* full classname of the JDBC driver
	*/
	private String driver;
	/**
	* JDBC url
	*/
	private String url;
	/**
	* username
	*/
	private String userName;
	/**
	* password
	*/
	private String passWord;

    private boolean loadMetadataOnConnect;
	/**
	* gets the name
	*/
	public String getName(){ return name;}
	/**
	* gets the driver
	*/
	public String getDriver(){ return driver;}
	/**
	* gets the url
	*/
	public String getUrl(){ return url;}
	/**
	* gets the name
	*/
	public String getUserName(){ return userName;}
	/**
	* gets the password
	*/
	public String getPassWord(){ return passWord;}
	/**
	* default constructor
	*/
	public Login(){}

	/**
	* <cvs/>covenience constructor
	*/
	public Login(String name, String driver, String url, String userName, String passWord, boolean loadMetadataOnConnect)
	{
		this.name = name;
		this.driver = driver;
		this.url = url;
		this.userName = userName;
		this.passWord = passWord;
        this.loadMetadataOnConnect = loadMetadataOnConnect;
	}

	//<cvs/>userNamePasswordSupplied was flag, converted to method
	/**
	* @return true if a user/passwd combination was given, some url's don't require one
	*/
	public boolean isUserNamePasswordSupplied()
	{
		return userName != null && passWord != null
			&& !userName.equals("") && !passWord.equals("");
	}

	public boolean equals(Object o)
	{
		if(!(o instanceof Login))
			return false;
		Login login = (Login)o;
		return 	login.getName().equals(name)
				&& login.getDriver().equals(driver)
				&& login.getUrl().equals(url)
				&& login.getUserName().equals(userName)
				&& login.getPassWord().equals(passWord)
                && login.isLoadMetadataOnConnect() == loadMetadataOnConnect;
	}
  public void setDriver(String driver) {
    this.driver = driver;
  }
  public void setName(String name) {
    this.name = name;
  }
  public void setPassWord(String passWord) {
    this.passWord = passWord;
  }
  public void setUrl(String url) {
    this.url = url;
  }
  public void setUserName(String userName) {
    this.userName = userName;
  }
  public boolean isLoadMetadataOnConnect() {
    return loadMetadataOnConnect;
  }
  public void setLoadMetadataOnConnect(boolean loadMetadataOnConnect) {
    this.loadMetadataOnConnect = loadMetadataOnConnect;
  }

}
