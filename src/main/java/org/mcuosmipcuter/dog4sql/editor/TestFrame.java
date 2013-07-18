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
package org.mcuosmipcuter.dog4sql.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;

public class TestFrame extends JFrame  implements PropertyChangeListener
{
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel jPanel1 = new JPanel();
  JScrollPane jScrollPane1 = new JScrollPane();
  SyntaxPainter sp1 = new SyntaxPainter();
  SyntaxPainter sp2 = new SyntaxPainter();
  JPanel jPanel2 = new JPanel();
  JTextArea ed = new JTextArea()
  {
    protected UndoManager undoManager = null;
    boolean oldCanUndo;
    boolean oldCanRedo;
    String oldUndoText = "";
    String oldRedoText = "";

    void addUndoManager()
    {
      undoManager = new UndoManager();
      getDocument().addUndoableEditListener(
          new UndoableEditListener()
      {
        public void undoableEditHappened(UndoableEditEvent e)
        {
          undoManager.addEdit(e.getEdit());
        }
      });
    }

    protected void processKeyEvent(KeyEvent e)
    {
      if(undoManager == null)
        addUndoManager();
      if(e.getID() == KeyEvent.KEY_PRESSED && e.isControlDown())
      {
        if(e.getKeyCode() == KeyEvent.VK_Z)
        {
          if(undoManager.canUndo())
          {
            undoManager.undo();
            this.revalidate();
          }
          else
            getToolkit().beep();
        }
        else if(e.getKeyCode() == KeyEvent.VK_Y)
        {
          if(undoManager.canRedo())
            undoManager.redo();
          else
            getToolkit().beep();
        }
      }
      if((oldCanUndo != undoManager.canUndo())
         || !(oldUndoText.equals(undoManager.getUndoPresentationName())))
      {
        stsUndo.setText(undoManager.canUndo() ?
                        undoManager.getUndoPresentationName() : "nop");
      }
      if(oldCanRedo != undoManager.canRedo()
         || !(oldRedoText.equals(undoManager.getRedoPresentationName())))
      {
        stsRedo.setText(undoManager.canRedo() ?
                        undoManager.getRedoPresentationName() : "nop");
      }
      oldCanUndo = undoManager.canUndo();
      oldCanRedo = undoManager.canRedo();
      oldUndoText = undoManager.getUndoPresentationName();
      oldRedoText = undoManager.getRedoPresentationName();

      super.processKeyEvent(e);
    }
  };

  JColorChooser cc = new JColorChooser(Color.BLACK);
  JMenuBar jMenuBar1 = new JMenuBar();
  JMenu jMenu1 = new JMenu();
  JMenuItem jMenuItem1 = new JMenuItem();
  JMenu jMenu2 = new JMenu();
  JMenuItem jMenuItemKw1 = new JMenuItem();
  JMenuItem jMenuItemKw2 = new JMenuItem();
  JMenuItem jMenuItemDelim = new JMenuItem();
  JMenuItem jMenuItemStrings = new JMenuItem();
  JMenuItem jMenuItemComments = new JMenuItem();
  JMenuItem jMenuItemNumbers = new JMenuItem();
  JMenu jMenu3 = new JMenu();
  JCheckBoxMenuItem jCheckBoxMenuItemKW1 = new JCheckBoxMenuItem();
  JCheckBoxMenuItem jCheckBoxMenuItemCStyle = new JCheckBoxMenuItem();
  JLabel stsUndo = new JLabel();
  JMenuItem jMenuItemBackGround = new JMenuItem();
  JMenuItem jMenuItemForeGround = new JMenuItem();
  JLabel stsRedo = new JLabel();
  JOptionPane jopFont = new JOptionPane();
  GridLayout gridLayout1 = new GridLayout();
  JComboBox jComboBox1 = new JComboBox();
  JComboBox jComboBox2 = new JComboBox();
  JComboBox jComboBox3 = new JComboBox();
  JLabel jLabel1 = new JLabel();
  JLabel jLabel2 = new JLabel();
  JLabel jLabel3 = new JLabel();
  JCheckBoxMenuItem jCheckBoxMenuItemAMS = new JCheckBoxMenuItem();
  JMenuItem jMenuItemFont = new JMenuItem();
  public TestFrame()
  {
    try
    {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    ResourceBundle bundle1 =  ResourceBundle.getBundle("org.mcuosmipcuter.dog4sql.editor/java2", Locale.ENGLISH,this.getClass().getClassLoader());
    ResourceBundle bundle2 =  ResourceBundle.getBundle("org.mcuosmipcuter.dog4sql.editor/sql92", Locale.ENGLISH,this.getClass().getClassLoader());

    sp2.setReservedWords(bundle2);

    CStyleCommentedDocument doc = new CStyleCommentedDocument();
    ed.setDocument(doc);
    ed.setUI(new HighlightingUI(sp1, doc));
    sp1.setReservedWords(bundle2);

  }
  public static void main(String[] args)
  {
    JFrame frame = new TestFrame();
    frame.pack();
    frame.setVisible(true);
  }
  private void jbInit() throws Exception {
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.setEnabled(true);
    this.setJMenuBar(jMenuBar1);
    this.getContentPane().setLayout(borderLayout1);
    jPanel1.setBorder(BorderFactory.createEtchedBorder());
    jMenu1.setText("File");
    jMenuItem1.setText("exit");
    jMenu2.setText("select color");
    jMenuItemKw1.setText("keywords 1");
    jMenuItemKw1.addActionListener(new TestFrame_jMenuItemKw1_actionAdapter(this));
    jMenuItemKw2.setText("keywords 2");
    jMenuItemDelim.setText("delimiters");
    jMenuItemStrings.setText("strings");
    jMenuItemComments.setText("comments");
    jMenuItemNumbers.setText("numbers");
    sp1.setCaseSensitiveKeyWords1(true);
    sp1.setCaseSensitiveKeyWords2(true);
    ed.setFont(new java.awt.Font("Monospaced", 0, 12));
    ed.setRows(8);
    jMenu3.setText("settings");
    jCheckBoxMenuItemKW1.setText("kw 1 case sensitive");
    jCheckBoxMenuItemKW1.addActionListener(new TestFrame_jCheckBoxMenuItemKW1_actionAdapter(this));
    jCheckBoxMenuItemCStyle.setText("C style comments");
    jCheckBoxMenuItemCStyle.addActionListener(new TestFrame_jCheckBoxMenuItemCStyle_actionAdapter(this));
    stsUndo.setFont(new java.awt.Font("Dialog", 0, 10));
    stsUndo.setPreferredSize(new Dimension(100, 12));
    stsUndo.setText("undo");
    jPanel2.setFont(new java.awt.Font("Dialog", 0, 20));
    jPanel2.setPreferredSize(new Dimension(30, 30));
    jMenuItemBackGround.setText("background");
    jMenuItemBackGround.addActionListener(new TestFrame_jMenuItemBackGround_actionAdapter(this));
    jMenuItemForeGround.setText("foreground");
    jMenuItemForeGround.addActionListener(new TestFrame_jMenuItemForeGround_actionAdapter(this));
    stsUndo.setText("undo");
    stsRedo.setPreferredSize(new Dimension(100, 12));
    stsRedo.setText("redo");
    jopFont.setLayout(gridLayout1);
    gridLayout1.setColumns(2);
    gridLayout1.setRows(4);
    jLabel1.setText("jLabel1");
    jLabel2.setText("jLabel2");
    jLabel3.setText("jLabel3");
    jopFont.setPreferredSize(new Dimension(108, 108));
    jComboBox3.setPreferredSize(new Dimension(26, 12));
    jComboBox1.setPreferredSize(new Dimension(26, 12));
    jComboBox2.setPreferredSize(new Dimension(26, 12));
    jCheckBoxMenuItemAMS.setText("allow multiline strings");
    jCheckBoxMenuItemAMS.addActionListener(new TestFrame_jCheckBoxMenuItemAMS_actionAdapter(this));
    jMenuItemFont.setText("font");
    jMenuItemFont.addActionListener(new TestFrame_jMenuItemFont_actionAdapter(this));
    jMenuItemFont.addActionListener(new TestFrame_jMenuItemFont_actionAdapter(this));
    this.getContentPane().add(jPanel1, BorderLayout.SOUTH);
    jPanel1.add(stsUndo, null);
    jPanel1.add(stsRedo, null);
    this.getContentPane().add(jScrollPane1, BorderLayout.CENTER);
    jScrollPane1.getViewport().add(ed, null);
    this.getContentPane().add(jPanel2, BorderLayout.NORTH);
    jMenuBar1.add(jMenu1);
    jMenuBar1.add(jMenu2);
    jMenuBar1.add(jMenu3);
    jMenu1.add(jMenuItem1);
    jMenu2.add(jMenuItemKw1);
    jMenu2.add(jMenuItemKw2);
    jMenu2.add(jMenuItemDelim);
    jMenu2.add(jMenuItemStrings);
    jMenu2.add(jMenuItemComments);
    jMenu2.add(jMenuItemNumbers);
    jMenu2.add(jMenuItemBackGround);
    jMenu2.add(jMenuItemForeGround);
    jMenu3.add(jCheckBoxMenuItemCStyle);
    jMenu3.add(jCheckBoxMenuItemKW1);
    jMenu3.add(jCheckBoxMenuItemAMS);
    jMenu3.add(jMenuItemFont);
    jopFont.add(jComboBox3, null);
    jopFont.add(jLabel2, null);
    jopFont.add(jComboBox1, null);
    jopFont.add(jLabel3, null);
    jopFont.add(jComboBox2, null);
    jopFont.add(jLabel1, null);
  }

  void jMenuItemKw1_actionPerformed(ActionEvent e) {
    Color newColor = cc.showDialog(this, "key words 1", sp1.getColorKeyWord1());
    if(newColor != null)
    {
      sp1.setColorKeyWord1(newColor);
    }
  }

  void jCheckBoxMenuItemCStyle_actionPerformed(ActionEvent e) {
    sp1.setSupportsCStyleComments(jCheckBoxMenuItemCStyle.isSelected());
  }

  public void updateUndoRedoDisplay(boolean canUndo, String undoText,
                                    boolean canRedo, String redoText)
  {
  }

/*
  public void setWarningMessage(WarningMessage[] messageKeys)
  {
    String msg = "";
    for(int i = 0; i < messageKeys.length; i++)
    {
      msg += (messageKeys[i].getMessageKey() + " :" + messageKeys[i].getLineNumber() +
              ", " + messageKeys[i].getColumnNumber() + "  ");
    }
    jLabelMessages.setText(msg);
  }
      */

  public void propertyChange(PropertyChangeEvent propertyChangeEvent)
  {
    ed.repaint();
  }

  void jMenuItemBackGround_actionPerformed(ActionEvent e) {
    Color newColor = cc.showDialog(this, "back ground", ed.getBackground());
    ed.setBackground(newColor == null ? ed.getBackground() : newColor);
  }

  void jMenuItemForeGround_actionPerformed(ActionEvent e) {
    Color newColor = cc.showDialog(this, "fore ground", ed.getForeground());
    ed.setForeground(newColor == null ? ed.getForeground() : newColor);
  }

  void jMenuItemFont_actionPerformed(ActionEvent e) {
    jopFont.showInputDialog("font size?");
  }

  void jCheckBoxMenuItemKW1_actionPerformed(ActionEvent e) {
    sp1.setCaseSensitiveKeyWords1(jCheckBoxMenuItemKW1.isSelected());
  }

  void jCheckBoxMenuItemAMS_actionPerformed(ActionEvent e) {
    sp1.setAllowsMultilineString(jCheckBoxMenuItemAMS.isSelected());
  }
}

class TestFrame_jMenuItemKw1_actionAdapter implements java.awt.event.ActionListener {
  TestFrame adaptee;

  TestFrame_jMenuItemKw1_actionAdapter(TestFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jMenuItemKw1_actionPerformed(e);
  }
}

class TestFrame_jCheckBoxMenuItemCStyle_actionAdapter implements java.awt.event.ActionListener {
  TestFrame adaptee;

  TestFrame_jCheckBoxMenuItemCStyle_actionAdapter(TestFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jCheckBoxMenuItemCStyle_actionPerformed(e);
  }
}

class TestFrame_jMenuItemBackGround_actionAdapter implements java.awt.event.ActionListener {
  TestFrame adaptee;

  TestFrame_jMenuItemBackGround_actionAdapter(TestFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jMenuItemBackGround_actionPerformed(e);
  }
}

class TestFrame_jMenuItemForeGround_actionAdapter implements java.awt.event.ActionListener {
  TestFrame adaptee;

  TestFrame_jMenuItemForeGround_actionAdapter(TestFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jMenuItemForeGround_actionPerformed(e);
  }
}

class TestFrame_jCheckBoxMenuItemKW1_actionAdapter implements java.awt.event.ActionListener {
  TestFrame adaptee;

  TestFrame_jCheckBoxMenuItemKW1_actionAdapter(TestFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jCheckBoxMenuItemKW1_actionPerformed(e);
  }
}

class TestFrame_jMenuItemFont_actionAdapter implements java.awt.event.ActionListener {
  TestFrame adaptee;

  TestFrame_jMenuItemFont_actionAdapter(TestFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jMenuItemFont_actionPerformed(e);
  }
}

class TestFrame_jCheckBoxMenuItemAMS_actionAdapter implements java.awt.event.ActionListener {
  TestFrame adaptee;

  TestFrame_jCheckBoxMenuItemAMS_actionAdapter(TestFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jCheckBoxMenuItemAMS_actionPerformed(e);
  }
}