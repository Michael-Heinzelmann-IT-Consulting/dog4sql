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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class FindWindow extends JFrame
{
    ActionListener actionListener;
    JPanel jPanel1 = new JPanel();
  JTextField tfFind = new JTextField();
  JButton jButtonFind = new JButton();
  JLabel jLabel1 = new JLabel();
  FlowLayout flowLayout1 = new FlowLayout();
    public FindWindow() throws HeadlessException
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
    jButtonFind.addActionListener(new Dog4SQLFindWindow_jButtonFind_actionAdapter(this));
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

class Dog4SQLFindWindow_jButtonFind_actionAdapter implements java.awt.event.ActionListener {
  FindWindow adaptee;

  Dog4SQLFindWindow_jButtonFind_actionAdapter(FindWindow adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jButtonFind_actionPerformed(e);
  }
}