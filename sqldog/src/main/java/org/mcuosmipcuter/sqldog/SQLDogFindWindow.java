package org.mcuosmipcuter.sqldog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SQLDogFindWindow extends JFrame
{
    ActionListener actionListener;
    JPanel jPanel1 = new JPanel();
  JTextField tfFind = new JTextField();
  JButton jButtonFind = new JButton();
  JLabel jLabel1 = new JLabel();
  FlowLayout flowLayout1 = new FlowLayout();
    public SQLDogFindWindow() throws HeadlessException
    {
        try {
          jbInit();
          getRootPane().setDefaultButton(jButtonFind);
        }
        catch(Exception e) {
          e.printStackTrace();
        }
    }
    private void jbInit() throws Exception {
      tfFind.setPreferredSize(new Dimension(160, 21));
    tfFind.setText("");
      jButtonFind.setMnemonic('F');
    jButtonFind.setText("Find");
    jButtonFind.addActionListener(new SQLDogFindWindow_jButtonFind_actionAdapter(this));
    jLabel1.setText("text to find:");
    jPanel1.setMinimumSize(new Dimension(335, 40));
    this.setTitle("find");
    jPanel1.add(jLabel1, null);
    jPanel1.add(tfFind, null);
    jPanel1.add(jButtonFind, null);
    this.getContentPane().add(jPanel1, BorderLayout.NORTH);
      jPanel1.setLayout(flowLayout1);
    }

    public String getFindText()
    {
        return tfFind.getText();
    }

    public void addActionListener(ActionListener actionListener)
    {
        this.actionListener = actionListener;
    }

  void jButtonFind_actionPerformed(ActionEvent e) {
      actionListener.actionPerformed(e);
  }
}

class SQLDogFindWindow_jButtonFind_actionAdapter implements java.awt.event.ActionListener {
  SQLDogFindWindow adaptee;

  SQLDogFindWindow_jButtonFind_actionAdapter(SQLDogFindWindow adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jButtonFind_actionPerformed(e);
  }
}