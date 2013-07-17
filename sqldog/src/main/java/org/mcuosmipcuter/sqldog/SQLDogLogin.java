package org.mcuosmipcuter.sqldog;

public class SQLDogLogin implements java.io.Serializable
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
	public SQLDogLogin(){}

	/**
	* <cvs/>covenience constructor
	*/
	public SQLDogLogin(String name, String driver, String url, String userName, String passWord, boolean loadMetadataOnConnect)
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
		if(!(o instanceof SQLDogLogin))
			return false;
		SQLDogLogin login = (SQLDogLogin)o;
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
