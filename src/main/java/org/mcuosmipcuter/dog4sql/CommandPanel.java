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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.TreeSet;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

public class CommandPanel extends JPanel implements 	ActionListener,
															WorkSpacePersistence
{
	LoginWindow loginWindow = new LoginWindow();

    class MyCellRenderer extends JLabel implements ListCellRenderer {
        public MyCellRenderer() {
            setOpaque(true);
        }
        public Component getListCellRendererComponent(
            JList list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus)
        {
            setText(" " + value.toString());

            if(value.toString().equals(currentConnected))
            {
                setBackground(cboLogins.getBackground());
                setForeground(Color.black);
            }
            else
            {
                setBackground(isSelected ? new Color(200, 225, 235): new Color(220, 245, 255));
                setForeground(isSelected ? Color.blue : Color.black);
            }
            if(index ==-1)
            {
                if(!value.toString().equals(currentConnected) && !currentConnected.equals(""))
                    this.setBorder(new LineBorder(Color.red));
            }
            else
                this.setBorder(null);

            return this;
        }
    }


	java.awt.event.ActionListener actionListener;
	//<cvs/>now keeping a hashtable of logins instead of one login field
	private Hashtable loginTable = new Hashtable();
	// state
	boolean connected = false;
	boolean executing = false;
	// gui
    ImageIcon imageIcon = new ImageIcon(ClassLoader.getSystemResource("org/mcuosmipcuter/dog4sql/dog4sql.gif"));
  	JButton remLoginButton = new JButton(Constants.REMOVE);
  	JButton addLoginButton = new JButton(Constants.EDIT);
  	JButton stopButton = new JButton(Constants.STOP_QUERY);
  	JToolBar jToolBar = new JToolBar();
  	JButton executeButton = new JButton(Constants.EXECUTE);
  	JComboBox cboLogins = new JComboBox();
  	JButton connectButton = new JButton(Constants.CONNECT);
  	JCheckBox ckAutoCommit = new JCheckBox(Constants.AUTO_COMMIT);
  	JButton rollbackButton = new JButton(Constants.ROLLBACK);
  	JButton commitButton = new JButton(Constants.COMMIT);
  	String currentConnected = "";
  	String connectTo = "";
  	BorderLayout borderLayout = new BorderLayout();
  	JPanel jPanel = new JPanel();

	public CommandPanel(java.awt.event.ActionListener actionListener)
	{
		this();
		this.actionListener = actionListener;
		executeButton.addActionListener(actionListener);
        addLoginButton.addActionListener(this);
		remLoginButton.addActionListener(this);
		stopButton.addActionListener(actionListener);
		commitButton.addActionListener(actionListener);
        connectButton.addActionListener(this);
        rollbackButton.addActionListener(actionListener);
        ckAutoCommit.addActionListener(this);

        commitButton.setActionCommand(Constants.COMMIT);
		commitButton.setEnabled(false);
        rollbackButton.setActionCommand(Constants.ROLLBACK);
		rollbackButton.setEnabled(false);
		cboLogins.setEditable(false);

		ckAutoCommit.setSelected(true);
		loginWindow.setVisible(false);
		setConnState(false);
		setExecState(false);
        loginWindow.setActionListener(this);
	}

	// <cvs/> Login returns now:
	/**
	* @return <cvs/>either the hash table lookup or, on edit, the (changed) fields
	*/
	public Login getLogin()
	{
		if(loginWindow.getTask().equals(Constants.CONNECT))
			return (Login)loginTable.get(cboLogins.getSelectedItem());
		return loginWindow.getLogin();
	}

	protected String[] getComboBoxItems(JComboBox box)
	{
		String[] arr = new String[box.getItemCount()];
		for(int i = 0; i < box.getItemCount(); i ++)
			arr[i] = (String)box.getItemAt(i);
		return arr;
	}

	protected void updateComboBox(JComboBox box, Object[] arr)
	{
		box.removeAllItems();
        TreeSet ts = new TreeSet(new Comparator()
        {
            public int compare(Object o1, Object o2)
            {
                return ((String)o1).compareToIgnoreCase((String)o2);
            }
            public boolean equals(Object object)
            {
                return false;
            }
        });

		for(int i = 0; i < arr.length; i++)
        {
            ts.add(arr[i]);
        }

        Iterator iter = ts.iterator();
        while(iter.hasNext())
        {
            box.addItem(iter.next());
        }

	}

	protected boolean comboBoxContains(JComboBox box, Object obj)
	{
		for(int i = 0; i < box.getItemCount(); i ++)
		{
			if(box.getItemAt(i) != null && box.getItemAt(i).equals(obj))
				return true;
		}
		return false;
	}

	public void setWorkSpace(WorkSpace workSpace)
	{
        if (workSpace == null
            || ! (workSpace instanceof CommandPanelWorkSpace) ||
            ( (CommandPanelWorkSpace) workSpace).getLoginTable() == null ||
            ( (CommandPanelWorkSpace) workSpace).getLoginTable().size() ==
            0)
        {
            loginTable = LoginWindow.getDefaultLogins();
            updateComboBox(cboLogins, loginTable.keySet().toArray());
            return;
        }
        CommandPanelWorkSpace ws = (CommandPanelWorkSpace)
            workSpace;
		//<cvs/>added clone() - bug in calling the equals() method if same obj
		loginTable = (Hashtable)ws.getLoginTable().clone();
		updateComboBox(cboLogins, loginTable.keySet().toArray());
		ckAutoCommit.setSelected(ws.isAutoCommit());
	}

	public WorkSpace getWorkSpace()
	{
		return  new CommandPanelWorkSpace(loginTable, ckAutoCommit.isSelected());
	}

	public void setConnState(boolean connected)
	{
		this.connected = connected;
        currentConnected = connected ? connectTo : "";
		connectButton.setText(connected ? Constants.DISCONNECT : Constants.CONNECT);
        connectButton.setToolTipText(connected ? "disconnect from " + currentConnected : "connect to selected login");
		setExecState(false);
		setAutoCommitState();
        cboLogins.setRenderer(new MyCellRenderer());
        cboLogins.repaint();
	}

	public void setExecState(boolean executing)
	{
		this.executing = executing;
		executeButton.setEnabled(!executing && connected);
		stopButton.setEnabled(executing);
	}

    public boolean getAutoCommit(){ return ckAutoCommit.isSelected();}

	public void setAutoCommitState()
	{
		commitButton.setEnabled(!ckAutoCommit.isSelected()  && connected);
		rollbackButton.setEnabled(!ckAutoCommit.isSelected()  && connected);
	}

	public void actionPerformed(ActionEvent e)
	{

		if(e.getSource() == ckAutoCommit)
		{
			setAutoCommitState();
			actionListener.actionPerformed(new ActionEvent(	this,
															ActionEvent.ACTION_PERFORMED,
								ckAutoCommit.isSelected() ? Constants.AUTO_COMMIT_ON :
															Constants.AUTO_COMMIT_OFF ));
		}

		String action = e.getActionCommand();

		if(action.equals(Constants.DISCONNECT))
		{
			actionListener.actionPerformed(e);
		}

		if(action.equals(Constants.CONNECT))
		{
			loginWindow.setTask(Constants.CONNECT); // task is connect in any case
            connectTo = cboLogins.getSelectedItem().toString();
			actionListener.actionPerformed(e);// pass on
		}

		if(action.equals(Constants.LOGIN_CONNECT))
		{
			if(connected)// avoid multiple connections
				return;

			loginTable.remove(loginWindow.getLoginName());
			loginTable.put(loginWindow.getLoginName(), loginWindow.getLogin());

			// convert event
			actionListener.actionPerformed(new ActionEvent(	this,
															ActionEvent.ACTION_PERFORMED,
															Constants.CONNECT ));
		}

		if(action.equals(Constants.EDIT))
		{
			if(e.getSource() == addLoginButton)
			{
				loginWindow.setLogin((Login)loginTable.get(cboLogins.getSelectedItem()));
				loginWindow.setTask(Constants.EDIT);
                loginWindow.setConnectEnabled(!connected);
				loginWindow.setVisible(true);// show login
			}
		}

		if(action.equals(Constants.REMOVE))
		{
			int res = JOptionPane.showConfirmDialog(	null,
					"are you sure you want to remove " + cboLogins.getSelectedItem() + " ?",
														"remove login",
														JOptionPane.YES_NO_OPTION);
			if(res != JOptionPane.OK_OPTION)
				return;

			cboLogins.removeItem(cboLogins.getSelectedItem());
			loginTable.remove(loginWindow.getLoginName());
            updateComboBox(cboLogins, loginTable.keySet().toArray());
			Controller.trace(3, "removed '" + loginWindow.getLoginName() + "'");
		}
		if(action.equals(Constants.SAVE))
		{
			Controller.trace(3, "saving '" + loginWindow.getLoginName() + "'");
			if(!comboBoxContains(cboLogins, loginWindow.getLoginName()))
            {
				cboLogins.addItem(loginWindow.getLoginName());
            }
			loginTable.remove(loginWindow.getLoginName());
			Controller.trace(5, "loginTable.size() after remove :" + loginTable.size());
			loginTable.put(loginWindow.getLoginName(), loginWindow.getLogin());
            updateComboBox(cboLogins, loginTable.keySet().toArray());
            cboLogins.setSelectedItem(loginWindow.getLoginName());
			Controller.trace(5, "loginTable.size() after put :" + loginTable.size());
		}
	}

    public String getName(){return this.getClass().getName();}

  public CommandPanel() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {

    this.setBackground(Constants.COLOR_APP);
    this.setFont(new java.awt.Font("MS Sans Serif", 0, 11));
    this.setForeground(new Color(40, 118, 173));
    this.setBorder(null);
    this.setMaximumSize(new Dimension(2147483647, 2147483647));
    this.setMinimumSize(new Dimension(275, 34));
    this.setOpaque(true);
    this.setPreferredSize(new Dimension(261, 44));
    this.setLayout(borderLayout);
    remLoginButton.setBorder(null);
    remLoginButton.setMaximumSize(new Dimension(49, 27));
    remLoginButton.setMinimumSize(new Dimension(49, 27));
    remLoginButton.setPreferredSize(new Dimension(49, 27));
    remLoginButton.setToolTipText("remove selected connection");
    remLoginButton.setMnemonic('V');
    jToolBar.setBackground(UIManager.getColor("control"));
    jToolBar.setForeground(Color.black);
    jToolBar.setAlignmentX((float) 0.0);
    jToolBar.setAlignmentY((float) 0.4814815);
    jToolBar.setBorder(null);
    jToolBar.setOpaque(false);
    jToolBar.setToolTipText("");
    jToolBar.setBorderPainted(true);
    jToolBar.setMargin(new Insets(3, 6, 3, 3));
    jToolBar.setFloatable(false);
    executeButton.setAlignmentY((float) 0.5);
    executeButton.setBorder(null);
    executeButton.setMaximumSize(new Dimension(151, 27));
    executeButton.setMinimumSize(new Dimension(151, 27));
    executeButton.setPreferredSize(new Dimension(151, 27));
    executeButton.setToolTipText("execute the SQL statement currently selected");
    executeButton.setBorderPainted(true);
    executeButton.setMnemonic('X');
    stopButton.setBorder(null);
    stopButton.setMaximumSize(new Dimension(41, 27));
    stopButton.setMinimumSize(new Dimension(41, 27));
    stopButton.setPreferredSize(new Dimension(41, 27));
    stopButton.setToolTipText("stop execution of SQL statement");
    stopButton.setBorderPainted(true);
    stopButton.setMnemonic('T');
    connectButton.setBorder(null);
    connectButton.setMaximumSize(new Dimension(73, 27));
    connectButton.setMinimumSize(new Dimension(73, 27));
    connectButton.setPreferredSize(new Dimension(73, 27));
    connectButton.setToolTipText("connect to selected login");
    connectButton.setMnemonic('C');
    cboLogins.setBackground(new Color(255, 255, 225));
    cboLogins.setAutoscrolls(false);
    cboLogins.setBorder(null);
    cboLogins.setMaximumSize(new Dimension(166, 27));
    cboLogins.setMinimumSize(new Dimension(26, 27));
    cboLogins.setOpaque(false);
    cboLogins.setPreferredSize(new Dimension(26, 28));
    cboLogins.setToolTipText("select connection");
    cboLogins.setPopupVisible(false);
    addLoginButton.setBorder(null);
    addLoginButton.setMaximumSize(new Dimension(31, 27));
    addLoginButton.setMinimumSize(new Dimension(31, 27));
    addLoginButton.setPreferredSize(new Dimension(31, 27));
    addLoginButton.setToolTipText("edit connection details");
    addLoginButton.setMnemonic('D');
    ckAutoCommit.setMaximumSize(new Dimension(101, 27));
    ckAutoCommit.setMinimumSize(new Dimension(101, 27));
    ckAutoCommit.setOpaque(false);
    ckAutoCommit.setPreferredSize(new Dimension(101, 27));
    ckAutoCommit.setToolTipText("autocommit off is not supported by all JDBC drivers");
    ckAutoCommit.setMargin(new Insets(2, 2, 2, 2));
    ckAutoCommit.setMnemonic('A');
    rollbackButton.setBorder(null);
    rollbackButton.setMaximumSize(new Dimension(53, 27));
    rollbackButton.setMinimumSize(new Dimension(53, 27));
    rollbackButton.setPreferredSize(new Dimension(53, 27));
    rollbackButton.setToolTipText("roll back current transaction");
    rollbackButton.setContentAreaFilled(true);
    rollbackButton.setMnemonic('R');
    commitButton.setBorder(null);
    commitButton.setMaximumSize(new Dimension(53, 27));
    commitButton.setMinimumSize(new Dimension(53, 27));
    commitButton.setPreferredSize(new Dimension(53, 27));
    commitButton.setToolTipText("commit current transaction");
    commitButton.setMnemonic('M');
    jPanel.setOpaque(false);
    jPanel.setPreferredSize(new Dimension(5, 5));
    this.add(jToolBar, BorderLayout.CENTER);
    jToolBar.add(commitButton, null);
    jToolBar.add(rollbackButton, null);
    jToolBar.add(ckAutoCommit, null);
    jToolBar.add(executeButton, null);
    jToolBar.add(stopButton, null);
    jToolBar.add(connectButton, null);
    jToolBar.add(cboLogins, null);
    jToolBar.add(addLoginButton, null);
    jToolBar.add(remLoginButton, null);
    this.add(jPanel, BorderLayout.SOUTH);

  }


}

