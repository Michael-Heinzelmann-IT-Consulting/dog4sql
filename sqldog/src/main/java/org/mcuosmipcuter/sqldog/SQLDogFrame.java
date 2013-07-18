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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;

class SQLDogFrame extends JFrame implements  ActionListener
{

    ImageIcon imageIcon = new ImageIcon(getClass().getResource(SQLDogConstants.ICON_PATH + "sqldog.gif"));
	SQLDogController controller;
    javax.swing.JComponent popper;
    String currentFindText = "";
    int findPos = -1;
    javax.swing.JComponent findPopper;
    boolean maximised;

	SQLDogFindWindow findWindow = new SQLDogFindWindow();
    Point popupPoint;

    JMenuItem[] fontSizes;
	JMenuItem jMenuItem1 = new JMenuItem();
 	JRadioButtonMenuItem mniPlain = new JRadioButtonMenuItem();
	JRadioButtonMenuItem mniBold = new JRadioButtonMenuItem();
	JRadioButtonMenuItem mniItalic = new JRadioButtonMenuItem();
	JMenu jMenuStyle = new JMenu();
	JPopupMenu jPopupMenu1 = new JPopupMenu();
	ButtonGroup buttonGroup1 = new ButtonGroup();
	ButtonGroup buttonGroup2 = new ButtonGroup();

  JMenu jMenu2 = new JMenu();
  JRadioButtonMenuItem rbm6 = new JRadioButtonMenuItem();
  JRadioButtonMenuItem rbm8 = new JRadioButtonMenuItem();
  JRadioButtonMenuItem rbm10 = new JRadioButtonMenuItem();
  JRadioButtonMenuItem rbm12 = new JRadioButtonMenuItem();
  JRadioButtonMenuItem rbm14 = new JRadioButtonMenuItem();
  JRadioButtonMenuItem rbm16 = new JRadioButtonMenuItem();
  JRadioButtonMenuItem rbm18 = new JRadioButtonMenuItem();
    JMenuItem jMenuItemSelAll = new JMenuItem();
  JMenuItem menuItemFind = new JMenuItem();
  JMenuItem menuItemFindNext = new JMenuItem();
  JMenu menuItemResultTable = new JMenu();
  JMenuItem sbmCopyHeaderNames = new JMenuItem();
    JMenuItem sbmSaveAsCsv = new JMenuItem();
    JMenuItem menuItemReload = new JMenuItem();
    private SQLDogDriverWindow driverWindow;
  JMenuBar jMenuBar1 = new JMenuBar();
  JMenu jMenuFile = new JMenu();
  JMenuItem jMenuItemExit = new JMenuItem();
  JMenu jMenuSettings = new JMenu();
  JMenuItem jMenuItemGeneral = new JMenuItem();
  JMenuItem jMenuItemDrivers = new JMenuItem();
  JMenu jMenuHelp = new JMenu();
  JMenuItem jMenuItemAbout = new JMenuItem();

	public SQLDogFrame()
	{
        driverWindow = new SQLDogDriverWindow();
        driverWindow.pack();
		MouseAdapter mouseAdapter = new MouseAdapter(){
			public void mouseReleased(MouseEvent e){
					popUp(e);
				}
			};
		controller = new SQLDogController(mouseAdapter, this);
		boolean restored = controller.restoreWorkSpaces();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle(SQLDogConstants.WINDOW_TITLE_NOT_CONNECTED);
        this.setIconImage(imageIcon.getImage());
		Container container = getContentPane();
		container.add(controller.getMainPanel());
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e)
			{
				// controller can now store this JFrame's size
				controller.setWorkSpace(getSize(), maximised, getDriverLocations());
				controller.saveWorkSpaces();
			}
		});
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        if (restored)
        {
            setSize(controller.getMainPanel().getSize().width, controller.getMainPanel().getSize().height);
            setLocation(screenSize.width / 2 - getWidth() / 2,
                        screenSize.height / 2 - getHeight() / 2);
        }
        else
            pack();

		//setVisible(true);
    try
    {
      jbInit();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
	}

    public boolean getMaximised(){ return maximised;}
    public void setMaximised(boolean maximised){this.maximised = maximised;}
    
    public void showDriverWindow()
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        driverWindow.setLocation(screenSize.width / 2 - driverWindow.getWidth() / 2,
                    screenSize.height / 2 - driverWindow.getHeight() / 2);

       driverWindow.setVisible(true);
    }
    public Vector getDriverLocations()
    {
        return driverWindow.getDriverLocations();
    }
    public void setDriverLocations(Vector driverLocations)
    {
        driverWindow.setDriverLocations(driverLocations);
    }
	public void actionPerformed(ActionEvent e)
	{
        if (e.getActionCommand().equals("Find"))
        {
            if(findWindow != null)
            {
                currentFindText = findWindow.getFindText();
                find();
                findWindow.setVisible(false);
            }
        }

    }
/////
    public void showDrilldown(String table, String[] columns, JTextArea sqlWindow)
     {

         // final int pos = sqlWindow.getCaretPosition();
       try
       {
           Rectangle r = sqlWindow.getUI().modelToView(sqlWindow, sqlWindow.getCaretPosition());
           final JPanel p = new JPanel(new BorderLayout());
           final JTextArea ta = sqlWindow;

           JList l = new JList(columns)
           {
               boolean updated;
               public void processFocusEvent(FocusEvent e)
               {
                   if(e.getID() == FocusEvent.FOCUS_LOST && !updated)
                       updateTa();
               }
               protected void updateTa()
               {
                   Object v = this.getSelectedValue();
                   String text = v != null ? v.toString() : "";
                   int pos = ta.getCaretPosition();
                   ta.requestFocus();
                   ta.remove(p);
                   ta.repaint();
                   ta.requestFocus();
                   ta.insert(text, pos);
                   updated = true;
               }

               public void processKeyEvent(KeyEvent e)
               {
                   if (e.getKeyCode() == KeyEvent.VK_ENTER)
                   {
                       //System.out.println("ENTER ....");
                       updateTa();
                       return;
                   }
                   super.processKeyEvent(e);
               }
               public void processMouseEvent(MouseEvent e)
               {
                   if(e.getClickCount() == 2)
                   {
                       updateTa();
                       return;
                   }
                   super.processMouseEvent(e);
               }
           };

           //l.getFontMetrics(l.getFont()).getHeight();l.getPreferredScrollableViewportSize().width
           l.setFont(sqlWindow.getFont());
           int fh = l.getFontMetrics(l.getFont()).getHeight();
           l.setFixedCellHeight(fh);

           //sqlWindow.getVisibleRect().height
           //System.out.println("\ndiff:"+(r.y % sqlWindow);
           JScrollPane scrollPane = new JScrollPane(l);
           int scrVal = controller.sqlPanel.getScrollPaneSQL().getVerticalScrollBar().getValue();
           int vh = sqlWindow.getVisibleRect().height - r.y + scrVal;

           System.out.println("+++++++++++++++" + scrVal);
           int h = fh * columns.length > vh ? vh : fh * columns.length;
           if(h > 40)
           {
               p.setSize(l.getPreferredScrollableViewportSize().width + 20, h);
               p.setLocation(r.x , r.y);
           }
           else
           {
               int diff = sqlWindow.getVisibleRect().height - r.y + scrVal;
               int hup = fh * columns.length > sqlWindow.getVisibleRect().height - diff? sqlWindow.getVisibleRect().height -diff: fh * columns.length;
               //int sh = scrollPane.getHorizontalScrollBar().getHeight();
               //hup += sqlWindow.getFont().getSize();
               p.setSize(l.getPreferredScrollableViewportSize().width + 20, hup);
               p.setLocation(r.x , r.y - hup + sqlWindow.getFontMetrics(sqlWindow.getFont()).getHeight());
           }

           p.add(BorderLayout.CENTER, scrollPane);

           sqlWindow.add(p, 0);
           l.requestFocus();
           sqlWindow.repaint();
           //
           //p.setVisible(true);


           //JOptionPane.showInputDialog(this, "table: ", "drilldown",0, null, columns, "");
           //mouseListener.mousePressed(new MouseEvent(pop, 0, 0, 0, r.x + sqlWindow.getLocationOnScreen().x, r.y+sqlWindow.getLocationOnScreen().y, 0, true));

       }
       catch (BadLocationException ex)
       {
           ex.printStackTrace();
       }
         //Object v = JOptionPane.showInputDialog(this, table, "drilldown", JOptionPane.QUESTION_MESSAGE, null, columns, "");
         //repaint();
     }
     protected void restore(JTextArea ta, int pos)
     {
         ta.requestFocus();
         ta.setCaretPosition(pos);
         ta.repaint();
     }
/////
  private void jbInit() throws Exception
  {
    mniPlain.addActionListener(new Frame1_mniPlain_actionAdapter(this));
    mniPlain.setText("plain");
    jMenuItem1.setOpaque(false);
    jMenuItem1.setText("font");
    mniBold.setText("bold");
    mniBold.addActionListener(new Frame1_mniBold_actionAdapter(this));
    mniItalic.setText("italic");
    mniItalic.addActionListener(new Frame1_mniItalic_actionAdapter(this));
    mniItalic.addActionListener(new Frame1_mniItalic_actionAdapter(this));
    jPopupMenu1.setBackground(new Color(187, 221, 228));
    jPopupMenu1.setOpaque(true);
    jPopupMenu1.setRequestFocusEnabled(true);
    jPopupMenu1.setInvoker(this);
    jPopupMenu1.setLightWeightPopupEnabled(true);
    mniItalic.addActionListener(new Frame1_mniItalic_actionAdapter(this));
    mniBold.addActionListener(new Frame1_mniBold_actionAdapter(this));
    mniPlain.addActionListener(new Frame1_mniPlain_actionAdapter(this));
    mniPlain.setBackground(new Color(187, 221, 228));
    mniPlain.setOpaque(true);
    mniPlain.setText("plain");
    mniBold.setBackground(new Color(187, 221, 228));
    mniBold.setOpaque(true);
    mniBold.setText("bold");
    mniItalic.setBackground(new Color(187, 221, 228));
    mniItalic.setOpaque(true);
    mniItalic.setText("italic");
    jMenuStyle.setBackground(new Color(187, 221, 228));
    jMenuStyle.setOpaque(false);
    jMenuStyle.setActionCommand("style");
        jMenuStyle.setMnemonic('0');
    jMenuStyle.setText("font style");
    jMenuStyle.setArmed(false);
    this.getContentPane().setBackground(new Color(187, 221, 228));
    this.setJMenuBar(jMenuBar1);
    jMenu2.setBackground(new Color(187, 221, 228));
        jMenu2.setMnemonic('0');
    jMenu2.setText("font size");
    rbm6.setBackground(new Color(187, 221, 228));
    rbm6.setOpaque(true);
    rbm6.setText("6");
    rbm6.addActionListener(new SQLDogFrame_rbm6_actionAdapter(this));
    rbm8.setBackground(new Color(187, 221, 228));
    rbm8.setText("8");
    rbm8.addActionListener(new SQLDogFrame_rbm8_actionAdapter(this));
    rbm10.setBackground(new Color(187, 221, 228));
    rbm10.setText("10");
    rbm10.addActionListener(new SQLDogFrame_rbm10_actionAdapter(this));
    rbm12.setBackground(new Color(187, 221, 228));
    rbm12.setText("12");
    rbm12.addActionListener(new SQLDogFrame_rbm12_actionAdapter(this));
    rbm14.setBackground(new Color(187, 221, 228));
    rbm14.setText("14");
    rbm14.addActionListener(new SQLDogFrame_rbm14_actionAdapter(this));
    rbm16.setBackground(new Color(187, 221, 228));
    rbm16.setText("16");
    rbm16.addActionListener(new SQLDogFrame_rbm16_actionAdapter(this));
    jMenuItemSelAll.setOpaque(false);
        jMenuItemSelAll.setPreferredSize(new Dimension(75, 19));
    jMenuItemSelAll.setMnemonic('0');
        jMenuItemSelAll.setText("select all");
        jMenuItemSelAll.addMouseListener(new SQLDogFrame_jMenuItemSelAll_mouseAdapter(this));
        menuItemFind.setOpaque(false);
        menuItemFind.setMnemonic('0');
    menuItemFind.setText("find");
    menuItemFind.addMouseListener(new SQLDogFrame_menuItemFind_mouseAdapter(this));
    menuItemFindNext.setOpaque(false);
        menuItemFindNext.setMnemonic('0');
    menuItemFindNext.setText("find next");
    menuItemFindNext.addMouseListener(new SQLDogFrame_menuItemFindNext_mouseAdapter(this));
        menuItemResultTable.setOpaque(false);
    menuItemResultTable.setText("SQL result");
    sbmCopyHeaderNames.setBackground(new Color(187, 221, 228));
    sbmCopyHeaderNames.setOpaque(true);
    sbmCopyHeaderNames.setText("copy header names");
    sbmCopyHeaderNames.addMouseListener(new SQLDogFrame_sbmCopyHeaderNames_mouseAdapter(this));
    sbmSaveAsCsv.setBackground(new Color(187, 221, 228));
        sbmSaveAsCsv.setText("save as CSV");
        sbmSaveAsCsv.addMouseListener(new SQLDogFrame_sbmSaveAsCsv_mouseAdapter(this));
        menuItemReload.setOpaque(false);
    menuItemReload.setText("reload metadata");
        menuItemReload.addMouseListener(new SQLDogFrame_menuItemReload_mouseAdapter(this));
    jMenuFile.setOpaque(false);
    jMenuFile.setMnemonic('F');
    jMenuFile.setText("File");
    jMenuItemExit.setBackground(new Color(187, 221, 228));
    jMenuItemExit.setMnemonic('E');
    jMenuItemExit.setText("Exit");
    jMenuItemExit.addActionListener(new SQLDogFrame_jMenuItemExit_actionAdapter(this));
    jMenuSettings.setOpaque(false);
    jMenuSettings.setMnemonic('S');
    jMenuSettings.setText("Settings");
    jMenuItemGeneral.setBackground(new Color(187, 221, 228));
    jMenuItemGeneral.setMnemonic('G');
    jMenuItemGeneral.setText("general");
    jMenuItemDrivers.setBackground(new Color(187, 221, 228));
    jMenuItemDrivers.setMnemonic('J');
    jMenuItemDrivers.setText("JDBC drivers");
    jMenuItemDrivers.addActionListener(new SQLDogFrame_jMenuItemDrivers_actionAdapter(this));
    jMenuHelp.setOpaque(false);
    jMenuHelp.setMnemonic('H');
    jMenuHelp.setText("Help");
    jMenuItemAbout.setBackground(new Color(187, 221, 228));
    jMenuItemAbout.setMnemonic('A');
    jMenuItemAbout.setText("about");
    jMenuItemAbout.addActionListener(new SQLDogFrame_jMenuItemAbout_actionAdapter(this));
    jMenuBar1.setBackground(new Color(187, 221, 228));
    jMenuBar1.setOpaque(true);
        rbm18.setBackground(new Color(187, 221, 228));
    rbm18.setText("18");
    rbm18.addActionListener(new SQLDogFrame_rbm18_actionAdapter(this));
        jMenuStyle.add(mniPlain);
    jMenuStyle.add(mniBold);
    jMenuStyle.add(mniItalic);
    jPopupMenu1.add(jMenuItem1);
    jPopupMenu1.addSeparator();
        jPopupMenu1.add(jMenuItemSelAll);
    jPopupMenu1.addSeparator();
    jPopupMenu1.add(jMenuStyle);
    jPopupMenu1.add(jMenu2);
    jPopupMenu1.addSeparator();
    jPopupMenu1.add(menuItemFind);
    jPopupMenu1.add(menuItemFindNext);
    jPopupMenu1.add(menuItemResultTable);
        jPopupMenu1.add(menuItemReload);
    jMenu2.add(rbm6);
    jMenu2.add(rbm8);
    jMenu2.add(rbm10);
    jMenu2.add(rbm12);
    jMenu2.add(rbm14);
    jMenu2.add(rbm16);
    jMenu2.add(rbm18);

    buttonGroup1.add(rbm6);
    buttonGroup1.add(rbm8);
    buttonGroup1.add(rbm10);
    buttonGroup1.add(rbm12);
    buttonGroup1.add(rbm14);
    buttonGroup1.add(rbm16);
    buttonGroup1.add(rbm18);
    buttonGroup2.add(mniPlain);
    buttonGroup2.add(mniBold);
    buttonGroup2.add(mniItalic);
    menuItemResultTable.add(sbmCopyHeaderNames);
        menuItemResultTable.add(sbmSaveAsCsv);
    jMenuBar1.add(jMenuFile);
    jMenuBar1.add(jMenuSettings);
    jMenuBar1.add(jMenuHelp);
    jMenuFile.add(jMenuItemExit);
    jMenuSettings.add(jMenuItemGeneral);
    jMenuSettings.add(jMenuItemDrivers);
    jMenuHelp.add(jMenuItemAbout);
    fontSizes = new JMenuItem[] {rbm6, rbm8, rbm10, rbm12, rbm14, rbm16, rbm18};
  }

  private void popUp(MouseEvent e)
  {
    if(e.getButton() != MouseEvent.BUTTON3)
      return;

    this.popper = (javax.swing.JComponent)e.getComponent();
    int s = (popper.getFont().getSize() - 6 ) / 2;
    JMenuItem item = fontSizes[s];
    item.setSelected(true);
    s = popper.getFont().getStyle();
    switch(s)
    {
        case Font.PLAIN:
            mniPlain.setSelected(true); break;
        case Font.BOLD:
          mniBold.setSelected(true); break;
        case Font.ITALIC:
          mniItalic.setSelected(true); break;
    }

    int x = popper.getLocationOnScreen().x;
    int y =popper.getLocationOnScreen().y;
    popupPoint = new Point(x + e.getX(), y + e.getY());
    jPopupMenu1.setLocation(popupPoint);
   String title = "'" + popper.getName() + "'";
   menuItemFind.setVisible(popper instanceof javax.swing.text.JTextComponent);
   menuItemFindNext.setVisible(popper instanceof javax.swing.text.JTextComponent);
   //menuItemResultTable.setEnabled(popper instanceof SQLDogResultPanel.MyJTable);
   if(popper instanceof SQLDogResultPanel.MyJTable)
   {
       menuItemResultTable.setVisible(true);
       menuItemResultTable.setEnabled(popper.isEnabled());
   }
   else
       menuItemResultTable.setVisible(false);

    menuItemReload.setVisible("database metaobjects".equals(popper.getName()));
    if(popper instanceof SQLDogSQLPanel.MyJTree)
        menuItemReload.setEnabled(((SQLDogSQLPanel.MyJTree)popper).getReloadState());
    menuItemFindNext.setEnabled(findPos != -1 && findPopper == popper);
    jMenuItem1.setText(title);
    jPopupMenu1.setVisible(true);

  }
  private void setSizeFont(int size)
  {
    String family = popper.getFont().getFamily();
    int style = popper.getFont().getStyle();
    popper.setFont(new Font(family, style, size));
  }
  private void setStyleFont(int style)
  {
    String family = popper.getFont().getFamily();
    int size = popper.getFont().getSize();
    popper.setFont(new Font(family, style, size));
  }

  void mniItalic_actionPerformed(ActionEvent e)
  {
      setStyleFont(Font.ITALIC);
  }

  void mniBold_actionPerformed(ActionEvent e)
  {
      setStyleFont(Font.BOLD);
  }

  void mniPlain_actionPerformed(ActionEvent e)
  {
      setStyleFont(Font.PLAIN);
  }

  void rbm10_actionPerformed(ActionEvent e)
  {
      setSizeFont(10);
  }

  void rbm12_actionPerformed(ActionEvent e)
  {
      setSizeFont(12);
  }

  void rbm14_actionPerformed(ActionEvent e)
  {
      setSizeFont(14);
  }

  void rbm6_actionPerformed(ActionEvent e)
  {
      setSizeFont(6);
  }

  void rbm8_actionPerformed(ActionEvent e)
  {
      setSizeFont(8);
  }

  void rbm16_actionPerformed(ActionEvent e)
  {
      setSizeFont(16);
  }

  void rbm18_actionPerformed(ActionEvent e)
  {
      setSizeFont(18);
  }

    void jMenuItemSelAll_mouseReleased(MouseEvent e)
    {
        popper.requestFocus();
        if(popper instanceof javax.swing.text.JTextComponent)
            ((javax.swing.text.JTextComponent)popper).selectAll();
        if(popper instanceof javax.swing.JTable)
            ((javax.swing.JTable)popper).selectAll();
        if(popper instanceof javax.swing.JTree)
        {
            int[] rows = new int[((javax.swing.JTree) popper).getRowCount()];
            for(int i = 0; i < rows.length; i++)
                rows[i] = i;
            ((javax.swing.JTree) popper).setSelectionRows(rows);
        }

    }

    void menuItemFind_mouseReleased(MouseEvent e)
    {
        if (popper instanceof javax.swing.text.JTextComponent)
        {
            findPos = ((javax.swing.text.JTextComponent) popper).
                getCaretPosition();
            findPopper = popper;
            findWindow.addActionListener(this);
            findWindow.setIconImage(imageIcon.getImage());
            findWindow.pack();

            if(popupPoint.x + findWindow.getWidth() >
               Toolkit.getDefaultToolkit().getScreenSize().width)
            {
                findWindow.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width - findWindow.getWidth(), popupPoint.y);
            }
            else
            {
                findWindow.setLocation(popupPoint);
            }
            findWindow.setVisible(true);
            findWindow.setFocusable(false);
        }
    }

  void menuItemFindNext_mouseReleased(MouseEvent e)
  {
      find();
  }

  private void find()
  {
      if(popper instanceof javax.swing.text.JTextComponent)
      {
          String text = ( (javax.swing.text.JTextComponent) popper).getText();
          if( !currentFindText.equals(""))
          {
              int fpos = text.indexOf(currentFindText, findPos);
              if(fpos != -1)
              {
                  popper.addFocusListener(new FocusListener()
                  {
                      public void focusGained(FocusEvent e)
                      {
                          ((javax.swing.text.JTextComponent)popper).select(findPos-currentFindText.length(), findPos);
                          popper.removeFocusListener(this);
                      }
                      public void focusLost(FocusEvent e)
                      {
                      }
                  });

                  popper.requestFocus();
                  findPos = fpos + currentFindText.length();
                  ((javax.swing.text.JTextComponent)popper).select(fpos, findPos);
                  popper.repaint();
              }
              else
              {
                  int res = JOptionPane.showConfirmDialog(popper.getParent(),"'" +currentFindText+ "' not found, find from beginning ?", "find",JOptionPane.YES_NO_OPTION);
                  if(res == JOptionPane.YES_OPTION)
                  {
                      findPos = 0;
                      find();
                  }
              }

          }
      }


  }

  void sbmCopyHeaderNames_mouseReleased(MouseEvent e) {
      ((SQLDogResultPanel.MyJTable)popper).copyHeaderNames();
  }

    void sbmSaveAsCsv_mouseReleased(MouseEvent e) {
        ((SQLDogResultPanel.MyJTable)popper).saveCsv();
    }

    void menuItemReload_mouseReleased(MouseEvent e) {
        controller.actionPerformed(new ActionEvent(this, 0, "reload metadata"));
    }

  void jMenuItemExit_actionPerformed(ActionEvent e) {
      // see windowClosing
      controller.setWorkSpace(getSize(), maximised, this.getDriverLocations());
      controller.saveWorkSpaces();
      System.exit(0);
  }

  void jMenuItemDrivers_actionPerformed(ActionEvent e) {
      showDriverWindow();
  }

  void jMenuItemAbout_actionPerformed(ActionEvent e) {
      int m = JOptionPane.INFORMATION_MESSAGE;
      JOptionPane.showMessageDialog(this, "SQL fetcher, author M. Heinzelmann\n\n"
                                    + SQLDogConstants.WARRANTY
                                    , "about", m, imageIcon);
  }






}

class Frame1_mniItalic_actionAdapter implements java.awt.event.ActionListener
{
  SQLDogFrame adaptee;

  Frame1_mniItalic_actionAdapter(SQLDogFrame adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.mniItalic_actionPerformed(e);
  }
}

class Frame1_mniBold_actionAdapter implements java.awt.event.ActionListener
{
  SQLDogFrame adaptee;

  Frame1_mniBold_actionAdapter(SQLDogFrame adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.mniBold_actionPerformed(e);
  }
}

class Frame1_mniPlain_actionAdapter implements java.awt.event.ActionListener
{
  SQLDogFrame adaptee;

  Frame1_mniPlain_actionAdapter(SQLDogFrame adaptee)
  {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e)
  {
    adaptee.mniPlain_actionPerformed(e);
  }
}

class SQLDogFrame_rbm6_actionAdapter implements java.awt.event.ActionListener {
  SQLDogFrame adaptee;

  SQLDogFrame_rbm6_actionAdapter(SQLDogFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.rbm6_actionPerformed(e);
  }
}

class SQLDogFrame_rbm8_actionAdapter implements java.awt.event.ActionListener {
  SQLDogFrame adaptee;

  SQLDogFrame_rbm8_actionAdapter(SQLDogFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.rbm8_actionPerformed(e);
  }
}

class SQLDogFrame_rbm10_actionAdapter implements java.awt.event.ActionListener {
  SQLDogFrame adaptee;

  SQLDogFrame_rbm10_actionAdapter(SQLDogFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.rbm10_actionPerformed(e);
  }
}

class SQLDogFrame_rbm12_actionAdapter implements java.awt.event.ActionListener {
  SQLDogFrame adaptee;

  SQLDogFrame_rbm12_actionAdapter(SQLDogFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.rbm12_actionPerformed(e);
  }
}

class SQLDogFrame_rbm14_actionAdapter implements java.awt.event.ActionListener {
  SQLDogFrame adaptee;

  SQLDogFrame_rbm14_actionAdapter(SQLDogFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.rbm14_actionPerformed(e);
  }
}

class SQLDogFrame_rbm16_actionAdapter implements java.awt.event.ActionListener {
  SQLDogFrame adaptee;

  SQLDogFrame_rbm16_actionAdapter(SQLDogFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.rbm16_actionPerformed(e);
  }
}

class SQLDogFrame_rbm18_actionAdapter implements java.awt.event.ActionListener {
  SQLDogFrame adaptee;

  SQLDogFrame_rbm18_actionAdapter(SQLDogFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.rbm18_actionPerformed(e);
  }
}

class SQLDogFrame_jMenuItemSelAll_mouseAdapter extends java.awt.event.MouseAdapter
{
    SQLDogFrame adaptee;

    SQLDogFrame_jMenuItemSelAll_mouseAdapter(SQLDogFrame adaptee)
    {
        this.adaptee = adaptee;
    }
    public void mouseReleased(MouseEvent e)
    {
        adaptee.jMenuItemSelAll_mouseReleased(e);
    }
}

class SQLDogFrame_menuItemFind_mouseAdapter extends java.awt.event.MouseAdapter {
  SQLDogFrame adaptee;

  SQLDogFrame_menuItemFind_mouseAdapter(SQLDogFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void mouseReleased(MouseEvent e) {
    adaptee.menuItemFind_mouseReleased(e);
  }
}

class SQLDogFrame_menuItemFindNext_mouseAdapter extends java.awt.event.MouseAdapter {
  SQLDogFrame adaptee;

  SQLDogFrame_menuItemFindNext_mouseAdapter(SQLDogFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void mouseReleased(MouseEvent e) {
    adaptee.menuItemFindNext_mouseReleased(e);
  }
}

class SQLDogFrame_sbmCopyHeaderNames_mouseAdapter extends java.awt.event.MouseAdapter {
  SQLDogFrame adaptee;

  SQLDogFrame_sbmCopyHeaderNames_mouseAdapter(SQLDogFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void mouseReleased(MouseEvent e) {
    adaptee.sbmCopyHeaderNames_mouseReleased(e);
  }
}

class SQLDogFrame_sbmSaveAsCsv_mouseAdapter extends java.awt.event.MouseAdapter {
  SQLDogFrame adaptee;

  SQLDogFrame_sbmSaveAsCsv_mouseAdapter(SQLDogFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void mouseReleased(MouseEvent e) {
    adaptee.sbmSaveAsCsv_mouseReleased(e);
  }
}

class SQLDogFrame_menuItemReload_mouseAdapter extends java.awt.event.MouseAdapter {
  SQLDogFrame adaptee;

  SQLDogFrame_menuItemReload_mouseAdapter(SQLDogFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void mouseReleased(MouseEvent e) {
    adaptee.menuItemReload_mouseReleased(e);
  }
}

class SQLDogFrame_jMenuItemExit_actionAdapter implements java.awt.event.ActionListener {
  SQLDogFrame adaptee;

  SQLDogFrame_jMenuItemExit_actionAdapter(SQLDogFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jMenuItemExit_actionPerformed(e);
  }
}

class SQLDogFrame_jMenuItemDrivers_actionAdapter implements java.awt.event.ActionListener {
  SQLDogFrame adaptee;

  SQLDogFrame_jMenuItemDrivers_actionAdapter(SQLDogFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jMenuItemDrivers_actionPerformed(e);
  }
}

class SQLDogFrame_jMenuItemAbout_actionAdapter implements java.awt.event.ActionListener {
  SQLDogFrame adaptee;

  SQLDogFrame_jMenuItemAbout_actionAdapter(SQLDogFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jMenuItemAbout_actionPerformed(e);
  }
}
