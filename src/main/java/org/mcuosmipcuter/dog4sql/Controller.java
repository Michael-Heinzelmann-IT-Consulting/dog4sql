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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Stack;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class Controller
    implements ActionListener,
    QueryListener,
    WorkSpacePersistence,
    DBMetaObjects.BuildListener
{
    MouseListener mouseListener;
	CommandPanel commandPanel;
	SQLPanel sqlPanel;
	ResultPanel resPanel;
	WorkSpacePersistenceHandler workSpacePersistenceHandler;
	QueryProcessor queryProcessor;
	ResultProcessor resultProcessor;
	ResultWrapper resultWrapper;
	VeritcalSplitPane splitPane;
    DBMetaObjects metaObjects;
	Login login;
    Stack sqlStack = new Stack();
    Frame frame;
	JPanel mainPanel;

	//<cvs/>added member variable to store workspace to use for storin dimension on behalf
	ControllerWorkSpace myWorkSpace;

	Statement statement = null;

	private Connection connection;
	//<cvs/>added state to hold trace level read from system properties
	private static int applicationTraceLevel = Constants.TRACE_DEFAULT_LEVEL;

	public Controller(MouseListener mouseListener, Frame frame)
	{
		final MouseListener ml = mouseListener;
		MouseAdapter mouseAdapter = new MouseAdapter(){
			public void mouseReleased(MouseEvent e){
					ml.mouseReleased(e);
				}
			};
		
		mainPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createLineBorder(appBackGround()));
        
		commandPanel = new CommandPanel(this);
		sqlPanel = new SQLPanel(mouseAdapter, frame);
		this.frame = frame;

        sqlPanel.setReloadState(false);
		resPanel = new ResultPanel(this, mouseAdapter);
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add("North", commandPanel);
		splitPane = new VeritcalSplitPane(sqlPanel, resPanel);
        splitPane.setBackground(appBackGround());
        splitPane.setDividerSize(10);
        splitPane.setBorder(null);
		mainPanel.add("Center", splitPane);
		workSpacePersistenceHandler = new WorkSpacePersistenceHandler(
		new  WorkSpacePersistence[]{ commandPanel, sqlPanel, resPanel, splitPane, this});
		try
		{
			Object o = System.getProperties().get(Constants.TRACE_PROPERTY_KEY);
			if(o != null)// if property not set trace level stays at default
				applicationTraceLevel = Integer.parseInt(o.toString());
		}
		catch(NumberFormatException ex)
		{
			ex.printStackTrace();
		}
	}
	
	static Color appBackGround(){
		return new Color(187, 221, 228);
	}

	//<cvs/>added trace method
	static void trace(int localTraceLevel, String message)
	{
		if(applicationTraceLevel >= localTraceLevel)
			Constants.TRACE_PRINT_STREAM.println(message);
	}

	//<cvs/>added overloaded trace method to take exceptions
	static void trace(int localTraceLevel, Throwable throwable)
	{
		if(applicationTraceLevel >= localTraceLevel)
			throwable.printStackTrace(Constants.TRACE_PRINT_STREAM);
	}

	public void actionPerformed(ActionEvent e)
	{

		String action = e.getActionCommand();

		try
		{
			if(connection != null)
			{
				if(action.equals(Constants.AUTO_COMMIT_ON))
					connection.setAutoCommit(true);
				if(action.equals(Constants.AUTO_COMMIT_OFF))
					connection.setAutoCommit(false);
			}
		}
		catch(Exception e0)
		{
			trace(2, e0);
			resPanel.setMessage(e0);
		}

		if(action.equals(Constants.CONNECT))
		{
			try
			{
				login = commandPanel.getLogin();
                resPanel.setMessage("connecting ...");
                try
                {
                    connection = ConnectionUtil.getConnection(login);
                }
                catch(ClassNotFoundException cnf)
                {
                    int res = JOptionPane.showConfirmDialog(mainPanel.getParent(),
                        "oops class not found for '" + login.getDriver() +
                        "' do you want to locate and load the driver now?",
                        "connect",
                        JOptionPane.YES_NO_OPTION);

                    if (res == JOptionPane.YES_OPTION)
                    {
                    	frame.showDriverWindow();
                    	resPanel.setMessage("no connection, driver has to be located first.");
                    	return;
                    }
                    else
                    {
                        throw cnf;
                    }
                }
                if(connection == null)
                    throw new Exception("connection is null!");

                connection.setAutoCommit(commandPanel.getAutoCommit());
				setConnState(true);
                if(login.isLoadMetadataOnConnect())
                {
                    resPanel.setMessage("retrieving database metadata ...");
                    metaObjects = new DBMetaObjects(login, this);
                    sqlPanel.setLoadingTreeModel();
                    metaObjects.start();
                }
                else
                {
                    sqlPanel.setEmptyTreeModel();
                    sqlPanel.setReloadState(true);
                }
				resPanel.setMessage("connected to "  + login.getUrl());
			}
			catch(Exception e1)
			{
				trace(2, e1);
				resPanel.setMessage(e1);
			}
		}

		if(action.equals(Constants.DISCONNECT))
		{
			try
			{
                resPanel.setEnabled(false);
                resPanel.setMessage("disconnecting ...");
                if(metaObjects != null)
                {
                    metaObjects.stopBuild();
                    if (!metaObjects.isBuildCompleted())
                        sqlPanel.setEmptyTreeModel();
                }

                if(connection.isClosed())
                {
                    resPanel.setDisconnected("connection already closed by server " + login.getUrl());
                }
                else
                {
                    connection.close();
                    resPanel.setDisconnected("disconnected from " +
                                             login.getUrl());
                }
			}
			catch(Exception e2)
			{
				trace(2, e2);
				resPanel.setMessage(e2);
			}
			finally
			{
				connection = null;
				resultWrapper = null;//<cvs/>fix bug on autoscroll after disconnect
				setConnState(false);
                sqlPanel.setReloadState(false);
			}
		}

		if(action.equals(Constants.COMMIT) || action.equals(Constants.ROLLBACK))
		{
			try
			{
				if(action.equals(Constants.COMMIT))
					connection.commit();
				if(action.equals(Constants.ROLLBACK))
					connection.rollback();
			}
			catch(Exception ex)
			{
				trace(2, ex);
				resPanel.setMessage(ex);
			}
		}

        if (action.equals(Constants.EXECUTE))
        {
            String[] sqlStatements = sqlPanel.getSql();
            sqlStack.clear();
            for (int i = sqlStatements.length-1; i >= 0; i--)
            {
                String s = sqlStatements[i].trim();
                if(!s.equals(""))
                {
                    sqlStack.push(s);
                    trace(5, "pushing: " + s);
                }
            }

            if (sqlStack.size() > 1)
           {
               int res = JOptionPane.showConfirmDialog(mainPanel.getParent(),
                   "selection contains " + sqlStack.size()  +
                   " SQL statements, are you sure to execute them all?",
                   "execute",
                   JOptionPane.YES_NO_OPTION);
               if (res != JOptionPane.YES_OPTION)
                   return;
           }

            executeStatementFromStack();

        }

        if (action.equals(Constants.STOP_QUERY))
		{
			try
			{
				statement.cancel();
				if(queryProcessor != null)
				{
					queryProcessor.stopQuery();
					queryProcessor.interrupt();
				}
			}
			catch(Exception ex)
			{
				resPanel.setMessage(ex);
			}
		}
        if(action.equals("reload metadata"))
        {
            if(connection == null)
                return;
            if(metaObjects != null)
                metaObjects.stopBuild();

            resPanel.setMessage("reloading database metadata ...");
            try
            {
                metaObjects = new DBMetaObjects(login, this);
            }
            catch (Exception ex1)
            {
            }
            sqlPanel.setReLoadingTreeModel();
            sqlPanel.setReloadState(false);
            metaObjects.start();

        }
	}

    private void executeStatementFromStack()
    {
        Object o = sqlStack.pop();
        if(o == null || o.toString().equals(""))
            return;

        try
        {
            if(connection == null)
                throw new Exception("not connected!");
            if(connection.isClosed())
            {
                setConnState(false);
                throw new Exception("connection was closed!");
            }

            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                    ResultSet.CONCUR_READ_ONLY);
                                                    
			queryProcessor = new QueryProcessor(this, o.toString(), statement);

            commandPanel.setExecState(true);
            queryProcessor.start();
        }
        catch(InterruptedException ie)
        {
            resPanel.setMessage("stopped");
        }
        catch(Exception e3)
        {
            trace(2, e3);
            resPanel.setMessage(e3);
        }
        finally
        {
            sqlPanel.restoreSelection();
        }


    }
	public void onResult(ResultWrapper rw)
	{
		resultWrapper = rw;
		resultProcessor = new ResultProcessor(resultWrapper, this, resPanel);
        resPanel.setResult(resultProcessor.createModel());
		//adjustmentValueChanged(null);
		queryProcessor = null;


        resPanel.setMinColumnWidths(false);
        if(!sqlStack.isEmpty())
        {
            executeStatementFromStack();
        }
        else
        {
            commandPanel.setExecState(false);
        }
	}

	public void onQueryMessage(String message)
	{
		resPanel.setMessage(message);
	}

	public void onQueryException(Exception qex)
	{
		resPanel.setMessage(qex);
        sqlStack.clear();
        resPanel.setEnabled(false);
        try
        {
            if (connection != null && connection.isClosed())
            {
                resPanel.setDisconnected(qex.getMessage());
            }
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }

		commandPanel.setExecState(false);
		queryProcessor = null;
	}

	public void saveWorkSpaces()
	{
		trace(2, "saving workspaces...");
		workSpacePersistenceHandler.saveWorkSpaces();
	}

	public boolean restoreWorkSpaces()
	{
		trace(2, "restoring workspaces...");
		try
		{
			workSpacePersistenceHandler.restoreWorkSpaces();
		}
		catch(Exception e)
		{
			return false;
		}
		return true;
	}

	public WorkSpace getWorkSpace()
	{
		return new ControllerWorkSpace(frame.getSize(), 
							frame.getExtendedState() == JFrame.MAXIMIZED_BOTH,
							frame.getDriverLocations());
	}

	public void setWorkSpace(WorkSpace ws)
	{
		if(!(ws instanceof ControllerWorkSpace))
			return;
		myWorkSpace = (ControllerWorkSpace)ws;
		mainPanel.setSize(myWorkSpace.getDimension());
        frame.setMaximised(myWorkSpace.isMaximised());
        frame.setDriverLocations(myWorkSpace.getDrivers());
	}

	private void setConnState(boolean connected)
	{
		commandPanel.setConnState(connected);
		resPanel.setConnState(connected);
	}

	/**
	* <cvs>overloaded method to set the workspace on behalf of a containing window frame</cvs>
	* <cvs>should be called from containing main program before calling saveWorkSpaces()</cvs>
	* @param the dimension to store instead of this controllers size
	*/
	public void setWorkSpace(Dimension dimension, boolean maximised, Vector drivers)
	{
		myWorkSpace =  new ControllerWorkSpace(dimension, maximised, drivers);
        frame.setDriverLocations(drivers);
	}

    public Controller()
    {
      try
      {
        jbInit();
      }
      catch(Exception e)
      {
        e.printStackTrace();
      }
    }
    private void jbInit() throws Exception
    {
		mainPanel.setBackground(appBackGround());
		mainPanel.setOpaque(true);
		mainPanel.setRequestFocusEnabled(true);
		splitPane.setBackground(appBackGround());
    }

    public String getName(){return this.getClass().getName();}

    public void treeFinished(DBMetaObjects model)
    {
        resPanel.setMessage("database metadata loaded.");
        sqlPanel.setObjTreeModel(model);
        sqlPanel.setReloadState(true);
    }
	/**
	 * @return
	 */
	public JPanel getMainPanel() {
		return mainPanel;
	}

}
