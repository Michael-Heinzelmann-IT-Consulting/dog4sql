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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.ChangeEvent;

/**
 * <p>Title: Login window for SQLDog</p>
 * <p>Copyright: Copyright (c) 2003 Michael Heinzelmann</p>
 * @author Michael Heinzelmann
 * @ $version
 */

public class SQLDogLoginWindow
    extends JFrame
{
    /**
     * SQLDogConstants.WINDOW_TITLE_NOT_CONNECTED concatenated with "Login"
     */
    public final static String TITLE = SQLDogConstants.
        WINDOW_TITLE_NOT_CONNECTED + " Login";
    ImageIcon imageIcon = new ImageIcon(getClass().getResource(SQLDogConstants.ICON_PATH + "sqldog.gif"));
    Color col = new Color(208, 228, 220);
    ActionListener actionListener;
    JPanel panel = new JPanel();
    GridLayout gridLayout1 = new GridLayout();
    JPanel panel1 = new JPanel();
    JLabel lblName = new JLabel();
    Border border1;
    JTextField tfName = new JTextField();
    JPanel panel2 = new JPanel();
    JLabel lblDriver = new JLabel();
    JTextField tfDriver = new JTextField();
    JPanel panel3 = new JPanel();
    JLabel lblUrl = new JLabel();
    JTextField tfUrl = new JTextField();
    JPanel panel4 = new JPanel();
    JLabel lblUsername = new JLabel();
    JTextField tfUsername = new JTextField();
    JPanel panel5 = new JPanel();
    JLabel lblPassword = new JLabel();
    JPasswordField tfPassword = new JPasswordField();

    /**
         * <cvs/>string to hold state describing the task the window has (edit/connect)
     * <br/>will be set in setVisible(), should contain only SQLDogConstants
     */
    String task;

    private SQLDogLogin login;
    Border border2;
    Border border3;
    TitledBorder titledBorder1;
    Border border4;
  JPanel panel7 = new JPanel();
  JButton cancelButton = new JButton();
  JButton closeButton = new JButton();
  Border border5;
  JButton saveButton = new JButton();
  JButton connectButton = new JButton();
  FlowLayout flowLayout1 = new FlowLayout();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  GridBagLayout gridBagLayout3 = new GridBagLayout();
  JPanel panel6 = new JPanel();
  JCheckBox ckLoadMetadataOnConnect = new JCheckBox();

    public void setActionListener(ActionListener actionListener)
        throws
        HeadlessException
    {
        this.actionListener = actionListener;
    }

    public static void main(String[] args)
        throws HeadlessException
    {
        SQLDogLoginWindow SQLDogLoginWindow1 = new SQLDogLoginWindow();
        SQLDogLoginWindow1.setVisible(true);
    }

    public static Hashtable getDefaultLogins()
    {
        Hashtable hashTable = new Hashtable();
        for (int i = 0; i < SQLDogConstants.DEFAULT_LOGINS.length; i++)
        {
            hashTable.put( ( (SQLDogLogin) SQLDogConstants.DEFAULT_LOGINS[i]).
                          getName(),
                          SQLDogConstants.DEFAULT_LOGINS[i]);
        }
        return hashTable;
    }

    public SQLDogLoginWindow()
    {
        try
        {
            jbInit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        getRootPane().setDefaultButton(connectButton);
        this.setIconImage(imageIcon.getImage());
        pack();
        setTitle(TITLE);
    }

    private void jbInit()
        throws Exception
    {
        border1 = BorderFactory.createLineBorder(SystemColor.controlText, 1);
        border2 = BorderFactory.createCompoundBorder(BorderFactory.
            createLineBorder(
            Color.white, 6), BorderFactory.createEmptyBorder(6, 6, 6, 6));
        border3 = BorderFactory.createCompoundBorder(BorderFactory.
            createLineBorder(new
                             Color(208, 228, 220), 3),
            BorderFactory.createEmptyBorder(0, 10, 0, 0));
        border4 = BorderFactory.createLineBorder(new Color(69, 131, 166),2);
        border5 = BorderFactory.createEmptyBorder(2,0,0,0);
    panel.setBackground(Color.white);
        panel.setAlignmentX( (float) 0.5);
        panel.setBorder(border2);
        panel.setMinimumSize(new Dimension(80, 80));
        panel.setOpaque(true);
        panel.setLayout(gridLayout1);
        this.getContentPane().setBackground(Color.white);
        this.setContentPane(panel);
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.setIconImage(null);
        this.setLocale(java.util.Locale.getDefault());
    this.setResizable(false);
        this.setState(Frame.NORMAL);
        this.setTitle("");
        gridLayout1.setColumns(1);
    gridLayout1.setHgap(0);
        gridLayout1.setRows(7);
        gridLayout1.setVgap(2);
        panel1.setBackground(Color.white);
    panel1.setOpaque(false);
    panel1.setToolTipText("");
        panel1.setLayout(gridBagLayout1);
        lblName.setPreferredSize(new Dimension(45, 16));
        lblName.setText("name:");
        tfName.setBackground(Color.white);
        tfName.setForeground(new Color(80, 80, 160));
        tfName.setAlignmentX( (float) 0.5);
        tfName.setBorder(border3);
    tfName.setMaximumSize(new Dimension(304, 18));
    tfName.setMinimumSize(new Dimension(304, 18));
    tfName.setPreferredSize(new Dimension(304, 18));
    tfName.setToolTipText("alias name for the connection");
        tfName.setText("jTextField1");
        tfName.setColumns(38);
        tfName.setHorizontalAlignment(SwingConstants.LEFT);
        tfName.addCaretListener(new SQLDogLoginWindow_tfName_caretAdapter(this));
        panel2.setBackground(Color.white);
        panel2.setMaximumSize(new Dimension(2147483647, 2147483647));
    panel2.setOpaque(false);
        panel2.setLayout(gridBagLayout2);
        lblDriver.setPreferredSize(new Dimension(45, 16));
        lblDriver.setText("driver:");
        tfDriver.setBackground(Color.white);
        tfDriver.setForeground(new Color(80, 80, 160));
        tfDriver.setBorder(border3);
        tfDriver.setMaximumSize(new Dimension(2147483647, 2147483647));
        tfDriver.setMinimumSize(new Dimension(4, 20));
        tfDriver.setPreferredSize(new Dimension(440, 20));
    tfDriver.setToolTipText("full qualified class name of driver");
        tfDriver.setCaretColor(Color.black);
        tfDriver.setCaretPosition(0);
        tfDriver.setText("jTextField1");
        tfDriver.setColumns(38);
        tfDriver.addCaretListener(new SQLDogLoginWindow_tfDriver_caretAdapter(this));
        tfDriver.addCaretListener(new SQLDogLoginWindow_tfDriver_caretAdapter(this));
        panel3.setBackground(Color.white);
        panel3.setAlignmentX( (float) 0.5);
    panel3.setOpaque(false);
        panel3.setLayout(gridBagLayout3);
        lblUrl.setBackground(Color.white);
        lblUrl.setAlignmentY( (float) 0.5);
        lblUrl.setMaximumSize(new Dimension(45, 16));
        lblUrl.setMinimumSize(new Dimension(45, 16));
        lblUrl.setPreferredSize(new Dimension(45, 16));
        lblUrl.setText("url:");
        tfUrl.setForeground(new Color(80, 80, 160));
        tfUrl.setBorder(border3);
        tfUrl.setMaximumSize(new Dimension(440, 15));
        tfUrl.setPreferredSize(new Dimension(304, 21));
        tfUrl.setToolTipText("JDBC style URL start with jdbc:");
        tfUrl.setInputVerifier(null);
        tfUrl.setText("jTextField1");
        tfUrl.setColumns(38);
        tfUrl.setScrollOffset(0);
        tfUrl.addCaretListener(new SQLDogLoginWindow_tfUrl_caretAdapter(this));
        panel4.setBackground(Color.white);
    panel4.setOpaque(false);
        lblUsername.setText("Username:");
        tfUsername.setForeground(new Color(80, 80, 160));
        tfUsername.setBorder(border3);
        tfUsername.setCaretColor(Color.black);
        tfUsername.setText("jTextField2");
        tfUsername.setColumns(20);
        tfUsername.addCaretListener(new SQLDogLoginWindow_tfUsername_caretAdapter(this));
        panel5.setBackground(Color.white);
    panel5.setOpaque(false);
        lblPassword.setText("Password:");
        tfPassword.setForeground(new Color(80, 80, 160));
        tfPassword.setBorder(border3);
        tfPassword.setText("jPasswordField1");
        tfPassword.setColumns(20);
        tfPassword.setEchoChar('*');
        tfPassword.addCaretListener(new SQLDogLoginWindow_tfPassword_caretAdapter(this));

    panel7.setBackground(Color.white);
    panel7.setBorder(border5);
    panel7.setOpaque(false);
    panel7.setLayout(flowLayout1);
    cancelButton.setBackground(new Color(208, 228, 220));
    cancelButton.setFont(new java.awt.Font("SansSerif", 1, 12));
    cancelButton.setBorder(border4);
    cancelButton.setMaximumSize(new Dimension(83, 21));
    cancelButton.setMinimumSize(new Dimension(83, 21));
    cancelButton.setPreferredSize(new Dimension(83, 21));
    cancelButton.setActionCommand("cancel");
    cancelButton.setMnemonic('E');
    cancelButton.setText("Cancel");
    cancelButton.addActionListener(new SQLDogLoginWindow_cancelButton_actionAdapter(this));
    cancelButton.addActionListener(new SQLDogLoginWindow_cancelButton_actionAdapter(this));
    cancelButton.addActionListener(new
            SQLDogLoginWindow_cancelButton_actionAdapter(this));
    closeButton.setBackground(new Color(208, 228, 220));
    closeButton.setFont(new java.awt.Font("Dialog", 1, 12));
    closeButton.setBorder(border4);
    closeButton.setMaximumSize(new Dimension(83, 21));
    closeButton.setMinimumSize(new Dimension(83, 21));
    closeButton.setPreferredSize(new Dimension(83, 21));
    closeButton.setMnemonic('O');
    closeButton.setText("Close");
    closeButton.addActionListener(new SQLDogLoginWindow_closeButton_actionAdapter(this));
    closeButton.addActionListener(new SQLDogLoginWindow_closeButton_actionAdapter(this));
    closeButton.addActionListener(new
            SQLDogLoginWindow_closeButton_actionAdapter(this));
    saveButton.setBackground(new Color(208, 228, 220));
    saveButton.setFont(new java.awt.Font("Dialog", 1, 12));
    saveButton.setAlignmentY((float) 0.5);
    saveButton.setBorder(border4);
    saveButton.setMaximumSize(new Dimension(83, 21));
    saveButton.setMinimumSize(new Dimension(83, 21));
    saveButton.setPreferredSize(new Dimension(83, 21));
    saveButton.setActionCommand("save");
    saveButton.setMnemonic('S');
    saveButton.setText("Save");
    saveButton.setVerticalAlignment(SwingConstants.CENTER);
    saveButton.addActionListener(new SQLDogLoginWindow_saveButton_actionAdapter(this));
    saveButton.addActionListener(new SQLDogLoginWindow_saveButton_actionAdapter(this));
    saveButton.addActionListener(new SQLDogLoginWindow_saveButton_actionAdapter(this));
    saveButton.addActionListener(new
                                     SQLDogLoginWindow_saveButton_actionAdapter(this));
    connectButton.setBackground(new Color(208, 228, 220));
    connectButton.setFont(new java.awt.Font("Dialog", 1, 12));
    connectButton.setBorder(border4);
    connectButton.setMaximumSize(new Dimension(83, 21));
    connectButton.setMinimumSize(new Dimension(83, 21));
    connectButton.setPreferredSize(new Dimension(83, 21));
    connectButton.setActionCommand("connect");
    connectButton.setMnemonic('C');
    connectButton.setText("Connect");
    connectButton.addActionListener(new SQLDogLoginWindow_connectButton_actionAdapter(this));
    connectButton.addActionListener(new SQLDogLoginWindow_connectButton_actionAdapter(this));
    connectButton.addActionListener(new SQLDogLoginWindow_connectButton_actionAdapter(this));
    connectButton.addActionListener(new
            SQLDogLoginWindow_connectButton_actionAdapter(this));
    ckLoadMetadataOnConnect.setBackground(Color.white);
    ckLoadMetadataOnConnect.setOpaque(false);
    ckLoadMetadataOnConnect.setText("always load metadata on connect");
    ckLoadMetadataOnConnect.setVerticalAlignment(SwingConstants.CENTER);
    ckLoadMetadataOnConnect.addChangeListener(new SQLDogLoginWindow_ckLoadMetadataOnConnect_changeAdapter(this));
    panel6.setBackground(Color.white);
    panel6.setOpaque(false);
    panel.add(panel1, null);
        panel1.add(lblName,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 13));
        panel1.add(tfName,    new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 7, 0, 0), 0, 5));
        panel.add(panel2, null);
        panel2.add(lblDriver,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 13));
        panel2.add(tfDriver,   new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 7, 0, 0), 0, 3));
        panel.add(panel3, null);
        panel3.add(lblUrl,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 7, 13));
        panel3.add(tfUrl,     new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 2));
        panel.add(panel4, null);
        panel4.add(lblUsername, null);
        panel4.add(tfUsername, null);
        panel.add(panel5, null);
        panel5.add(lblPassword, null);
        panel5.add(tfPassword, null);
        panel7.add(connectButton, null);
    panel7.add(saveButton, null);
    panel7.add(closeButton, null);
    panel7.add(cancelButton, null);
    panel.add(panel6, null);
    panel6.add(ckLoadMetadataOnConnect, null);

    panel.add(panel7, null);

    }

    public String getTask()
    {
        return task;
    }

    public void setTask(String task)
    {
        this.task = task;
        setTitle(TITLE + " " + task);
    }

    public String getLoginName()
    {
        return tfName.getText();
    }

    public SQLDogLogin getLogin()
    {
        return new SQLDogLogin(tfName.getText(),
                               tfDriver.getText(),
                               tfUrl.getText(),
                               tfUsername.getText(),
                               new String(tfPassword.getPassword()),
                               this.ckLoadMetadataOnConnect.isSelected());
    }

    public void setLogin(SQLDogLogin login)
    {
        this.login = login;
        tfName.setText(login.getName());
        tfDriver.setText(login.getDriver());
        tfUrl.setText(login.getUrl());
        tfUsername.setText(login.getUserName());
        tfPassword.setText(login.getPassWord());
        ckLoadMetadataOnConnect.setSelected(login.isLoadMetadataOnConnect());
        saveButton.setEnabled(false);
    }

    public void setConnectEnabled(boolean connectEnabled)
    {
        connectButton.setEnabled(connectEnabled);
    }

    public void setVisible(boolean visible)
    {
        if (visible)
        {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            setLocation(screenSize.width / 2 - getWidth() / 2,
                        screenSize.height / 2 - getHeight() / 2);
        }
        super.setVisible(visible);
        if (visible)
        {
            tfUsername.requestFocus();
        }
    }

    private void enableSave()
    {
        saveButton.setEnabled(!getLogin().equals(login));
    }

    void connectButton_actionPerformed(ActionEvent e)
    {
        actionListener.actionPerformed(new ActionEvent(this,
            ActionEvent.ACTION_PERFORMED,
            SQLDogConstants.LOGIN_CONNECT));
        if (task.equals(SQLDogConstants.CONNECT))
        {
            setVisible(false);
        }
    }

    void cancelButton_actionPerformed(ActionEvent e)
    {
        setVisible(false);
    }

    void closeButton_actionPerformed(ActionEvent e)
    {
        setVisible(false);
    }

    void saveButton_actionPerformed(ActionEvent e)
    {
        actionListener.actionPerformed(new ActionEvent(this,
            ActionEvent.ACTION_PERFORMED,
            SQLDogConstants.SAVE));
    }

    void tfName_caretUpdate(CaretEvent e)
    {
       enableSave();
    }

    void tfDriver_caretUpdate(CaretEvent e)
    {
        enableSave();
    }

    void tfUrl_caretUpdate(CaretEvent e)
    {
        enableSave();
    }

    void tfUsername_caretUpdate(CaretEvent e)
    {
        enableSave();
    }

    void tfPassword_caretUpdate(CaretEvent e)
    {
        enableSave();
    }

  void ckLoadMetadataOnConnect_stateChanged(ChangeEvent e) {
      enableSave();
  }

}

class SQLDogLoginWindow_tfName_caretAdapter
    implements javax.swing.event.CaretListener
{
    SQLDogLoginWindow adaptee;

    SQLDogLoginWindow_tfName_caretAdapter(SQLDogLoginWindow adaptee)
    {
        this.adaptee = adaptee;
    }

    public void caretUpdate(CaretEvent e)
    {
        adaptee.tfName_caretUpdate(e);
    }
}

class SQLDogLoginWindow_tfDriver_caretAdapter implements javax.swing.event.CaretListener
{
    SQLDogLoginWindow adaptee;

    SQLDogLoginWindow_tfDriver_caretAdapter(SQLDogLoginWindow adaptee)
    {
        this.adaptee = adaptee;
    }
    public void caretUpdate(CaretEvent e)
    {
        adaptee.tfDriver_caretUpdate(e);
    }
}

class SQLDogLoginWindow_tfUrl_caretAdapter implements javax.swing.event.CaretListener
{
    SQLDogLoginWindow adaptee;

    SQLDogLoginWindow_tfUrl_caretAdapter(SQLDogLoginWindow adaptee)
    {
        this.adaptee = adaptee;
    }
    public void caretUpdate(CaretEvent e)
    {
        adaptee.tfUrl_caretUpdate(e);
    }
}

class SQLDogLoginWindow_tfUsername_caretAdapter implements javax.swing.event.CaretListener
{
    SQLDogLoginWindow adaptee;

    SQLDogLoginWindow_tfUsername_caretAdapter(SQLDogLoginWindow adaptee)
    {
        this.adaptee = adaptee;
    }
    public void caretUpdate(CaretEvent e)
    {
        adaptee.tfUsername_caretUpdate(e);
    }
}

class SQLDogLoginWindow_tfPassword_caretAdapter implements javax.swing.event.CaretListener
{
    SQLDogLoginWindow adaptee;

    SQLDogLoginWindow_tfPassword_caretAdapter(SQLDogLoginWindow adaptee)
    {
        this.adaptee = adaptee;
    }
    public void caretUpdate(CaretEvent e)
    {
        adaptee.tfPassword_caretUpdate(e);
    }
}

class SQLDogLoginWindow_closeButton_actionAdapter implements java.awt.event.ActionListener {
  SQLDogLoginWindow adaptee;

  SQLDogLoginWindow_closeButton_actionAdapter(SQLDogLoginWindow adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.closeButton_actionPerformed(e);
  }
}

class SQLDogLoginWindow_cancelButton_actionAdapter implements java.awt.event.ActionListener {
  SQLDogLoginWindow adaptee;

  SQLDogLoginWindow_cancelButton_actionAdapter(SQLDogLoginWindow adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.cancelButton_actionPerformed(e);
  }
}

class SQLDogLoginWindow_connectButton_actionAdapter implements java.awt.event.ActionListener {
  SQLDogLoginWindow adaptee;

  SQLDogLoginWindow_connectButton_actionAdapter(SQLDogLoginWindow adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.connectButton_actionPerformed(e);
  }
}

class SQLDogLoginWindow_saveButton_actionAdapter implements java.awt.event.ActionListener {
  SQLDogLoginWindow adaptee;

  SQLDogLoginWindow_saveButton_actionAdapter(SQLDogLoginWindow adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.saveButton_actionPerformed(e);
  }
}

class SQLDogLoginWindow_ckLoadMetadataOnConnect_changeAdapter implements javax.swing.event.ChangeListener {
  SQLDogLoginWindow adaptee;

  SQLDogLoginWindow_ckLoadMetadataOnConnect_changeAdapter(SQLDogLoginWindow adaptee) {
    this.adaptee = adaptee;
  }
  public void stateChanged(ChangeEvent e) {
    adaptee.ckLoadMetadataOnConnect_stateChanged(e);
  }
}


