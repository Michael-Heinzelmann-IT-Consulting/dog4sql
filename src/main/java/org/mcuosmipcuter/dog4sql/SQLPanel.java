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
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.undo.UndoManager;

import org.mcuosmipcuter.dog4sql.editor.CStyleCommentedDocument;
import org.mcuosmipcuter.dog4sql.editor.HighlightingUI;
import org.mcuosmipcuter.dog4sql.editor.SyntaxPainter;
import org.mcuosmipcuter.dog4sql.sql.TextToStatementsParser;

public class SQLPanel extends JPanel implements WorkSpacePersistence,
//FocusListener,
AdjustmentListener
{
	public static final String WS_VERSION	= "1.1";
	//public static final int WINROWS	= 10;
    MouseListener mouseListener;
    Frame frame;
	//JTextArea sqlWindow = new JTextArea();
	//JTextArea scratchWindow = new JTextArea();
	//JSplitPane splitPane;
	int selectionStart;
	int selectionEnd;
	boolean textSelected;
    public static final String SELECTED_FONT	= "12";

	//<cvs/>state, used to determine autoselection, set on selection and unset on scrolling
	boolean autoSelect = false;
  BorderLayout borderLayout1 = new BorderLayout();
  JSplitPane splitPane = new JSplitPane();
  JSplitPane splitPane2 = new JSplitPane();
  JScrollPane scrollPaneSQL = new JScrollPane();
  JScrollPane scrollPaneScratch = new JScrollPane();
  JScrollPane scrollPaneTree = new JScrollPane();
  //org.mcuosmipcuter.dog4sql.editor.TestTextArea sqlWindow;
  /////
  SyntaxPainter syntaxPainter = new SyntaxPainter();
  JTextArea sqlWindow = new JTextArea()
  {
    protected UndoManager undoManager = null;
    boolean oldCanUndo;
    boolean oldCanRedo;
    String oldUndoText = "";
    String oldRedoText = "";

    void init()
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
      syntaxPainter.addDrillDownInitListener(new
          SyntaxPainter.DrillDownInitListener()
      {
          public void tryDrillDown(String token, int pos)
          {
              System.out.println("getCaretPosition()"+getCaretPosition()+ "/" +pos+"|"+token);
              if(getCaretPosition() == pos)
              {
                  System.out.println(
                      "((((((((((((((((((((((((((((((((((((((matches caret:" +
                      pos + "|" + token);

                  if (objTree.getModel()instanceof DBMetaObjects)
                  {
                      System.out.println("__ instanceof DBMetaObjects __");
                      DBMetaObjects mo = (DBMetaObjects)
                          objTree.getModel();
                      String[] s = mo.getTableColumns(token);
                      if(s != null)
                      {
                          for(int i = 0; i < s.length; i++)
                              System.out.print(s[i]+", ");
                              frame.showDrilldown(token, s, sqlWindow);

                        //JOptionPane.showInputDialog(null, token, "drilldown",0, null, s, "");
                      }
                      System.out.println("found:" + s);
                  }
              }
          }


      });

  }
  /*
  public void setCaretPosition(int cp)
  {
      System.out.println("setCaretPosition: " + cp);
      super.setCaretPosition(cp);
  }
*/

    protected void processKeyEvent(KeyEvent e)
    {
      if(undoManager == null)
        init();
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
                        undoManager.getUndoPresentationName() + "CTRL+Z" : "can't undo");
      }
      if(oldCanRedo != undoManager.canRedo()
         || !(oldRedoText.equals(undoManager.getRedoPresentationName())))
      {
        stsRedo.setText(undoManager.canRedo() ?
                        undoManager.getRedoPresentationName() + "CTRL+Y" : "can't redo");
      }
      oldCanUndo = undoManager.canUndo();
      oldCanRedo = undoManager.canRedo();
      oldUndoText = undoManager.getUndoPresentationName();
      oldRedoText = undoManager.getRedoPresentationName();
      /*
      if(e.getKeyChar() == '.' && e.getID() == KeyEvent.KEY_RELEASED)
      {
          //((CStyleCommentedDocument)getDocument()).isCStyleCommentOpen()
          int pos = this.getCaretPosition();
          System.out.println("pos: "+pos);

        try
        {
            String text = getDocument().getText(0, pos);
            int l = text.lastIndexOf("\n");
            String line = text.substring(l);

            System.out.println("line: "+ line);
        }
        catch (BadLocationException ex){}
          getToolkit().beep();
      }
                */
      super.processKeyEvent(e);
    }
  };

  /////
  JTextArea scratchWindow = new JTextArea();
  Border border1;
  TitledBorder titledBorder1;
  Border border2;
  TitledBorder titledBorder2;
  Border border3;
  Border border4;
  Border border5;
  Border border6;
  Border border7;
  Border border8;
  Border border9;
    Border border10;
  JTree objLoadTree = new JTree(new Object[] {"loading DB objects ..."});
  protected class MyJTree extends JTree
  {
      private boolean reloadState;
      public boolean getReloadState(){ return reloadState;}
      public void setReloadState(boolean reloadState){ this.reloadState = reloadState;}
      public MyJTree(){ super();}
      public MyJTree(Object[] arr){ super(arr);}
  }
   MyJTree objTree = new MyJTree(new Object[] {"no metadata"});

  //Border border11;
  TreeModel emptyTree = objTree.getModel();
  TreeModel loadingTree = objLoadTree.getModel();
  JPanel pnlStatusBar = new JPanel();
  JLabel stsUndo = new JLabel();
  Border border12;
  JLabel stsRedo = new JLabel();
  FlowLayout flowLayout1 = new FlowLayout();
  JLabel sep1 = new JLabel();
  JLabel sep2 = new JLabel();
  Border border13;
  JPanel pnlEdit = new JPanel();
  JLabel lblEmpty = new JLabel("");
  BorderLayout borderLayout2 = new BorderLayout();
  Border border14;
  JPanel pnlTree = new JPanel();
  JPanel pnlStatusTree = new JPanel();
  BorderLayout borderLayout3 = new BorderLayout();
  JLabel stsTree = new JLabel();
  FlowLayout flowLayout2 = new FlowLayout();
  JPanel pnlScratch = new JPanel();
  JPanel pnlStatusScratch = new JPanel();
  BorderLayout borderLayout4 = new BorderLayout();
  BorderLayout borderLayout6 = new BorderLayout();
  JLabel stsHighlight = new JLabel();
  Border border15;
  Border border16;
  Border border17;

  public SQLPanel(MouseListener mouseListener, Frame frame)
  {
    this.mouseListener = mouseListener;
	this.frame = frame;
      ResourceBundle bundle = ResourceBundle.getBundle("org.mcuosmipcuter.dog4sql.editor/sql92",
          Locale.ENGLISH, this.getClass().getClassLoader());

      try
      {
          jbInit();
      }
      catch (Exception e)
      {
          e.printStackTrace();
      }

      CStyleCommentedDocument document = new CStyleCommentedDocument();
      sqlWindow.setDocument(document);
      sqlWindow.setUI(new HighlightingUI(syntaxPainter, document));
      syntaxPainter.setReservedWords(bundle);

      scrollPaneSQL.setBackground(new Color(187, 221, 228));
      scrollPaneSQL.getHorizontalScrollBar().setPreferredSize(new Dimension(
          14, 14));
      scrollPaneSQL.getVerticalScrollBar().setPreferredSize(new Dimension(17,
          17));
      scrollPaneScratch.setBackground(new Color(187, 221, 228));
      scrollPaneScratch.getHorizontalScrollBar().setPreferredSize(new
          Dimension(12, 12));
      scrollPaneScratch.getVerticalScrollBar().setPreferredSize(new Dimension(
          15, 15));
      scrollPaneTree.setBackground(new Color(187, 221, 228));
      scrollPaneTree.getHorizontalScrollBar().setPreferredSize(new
          Dimension(12, 12));
      scrollPaneTree.getVerticalScrollBar().setPreferredSize(new Dimension(
          15, 15));
      JPanel corner = new JPanel();
      corner.setBackground(new Color(233, 233, 233));
      scrollPaneSQL.setCorner(JScrollPane.LOWER_RIGHT_CORNER, corner);
      JPanel corner2 = new JPanel();
      corner2.setBackground(new Color(233, 233, 233));
      scrollPaneTree.setCorner(JScrollPane.LOWER_RIGHT_CORNER, corner2);
      JPanel corner3 = new JPanel();
      corner3.setBackground(new Color(233, 233, 233));
      scrollPaneScratch.setCorner(JScrollPane.LOWER_RIGHT_CORNER, corner3);
      objTree.setBackground(new Color(255, 253, 255));
      objTree.setRootVisible(false);
      objTree.setShowsRootHandles(false);
      objTree.setBorder(border9);
      objTree.setCellRenderer(new MyRenderer("nodb"));
      sqlWindow.setName("SQL window");
      scratchWindow.setName("scratch pad");
      objTree.setName("database metaobjects");
      stsUndo.setName("stsUndo");
      stsRedo.setName("stsRedo");

  }

	public String[] getSql()
	{
		textSelected = sqlWindow.getSelectedText() != null
						&& !sqlWindow.getSelectedText().equals("");
		if(textSelected)
		{
			selectionStart = sqlWindow.getSelectionStart();
			selectionEnd = sqlWindow.getSelectionEnd();
			textSelected = true;
			return TextToStatementsParser.getSqlStatementsFrom(sqlWindow.getSelectedText());
		}
		return TextToStatementsParser.getSqlStatementsFrom(sqlWindow.getText());
	}

	public void restoreSelection()
	{
		sqlWindow.grabFocus();
		if(textSelected)
			sqlWindow.select(selectionStart, selectionEnd);
	}
/*
	public void focusGained(FocusEvent e)
	{
		if(autoSelect)
			sqlWindow.select(selectionStart, selectionEnd);
	}

	public void focusLost(FocusEvent e)
	{
		selectionStart = sqlWindow.getSelectionStart();
		selectionEnd = sqlWindow.getSelectionEnd();
		//<cvs>enable autoselect here</cvs>, if no scrolling occurs selection will be restored
		autoSelect = true;
	}
*/
	public void adjustmentValueChanged(AdjustmentEvent e)
	{
		//user scrolled before entering SQL window, so autoselect is not desirable
		//<cvs/>disable autoselect here
		autoSelect = false;
	}

	public void setWorkSpace(WorkSpace workSpace)
	{
		if(	workSpace == null
			|| !(workSpace instanceof SQLPanelWorkSpace))
			return;

		SQLPanelWorkSpace ws = (SQLPanelWorkSpace)workSpace;
		sqlWindow.setText(ws.getSqlText());
        ////sqlWindow.activateUndo();
		splitPane.setDividerLocation(ws.getDividerPosition());
        splitPane2.setDividerLocation(ws.getDividerPosition2());
	}

	public WorkSpace getWorkSpace()
	{
		//<cvs/>last divider pos is not to be stored, get divider gets the current pos
		return  new SQLPanelWorkSpace(WS_VERSION, sqlWindow.getText(),
                                            splitPane.getDividerLocation(),
                                            splitPane2.getDividerLocation());
	}
    public void setObjTreeModel(DBMetaObjects mo)
    {
        objTree.setModel(mo);
        objTree.setRootVisible(true);
        objTree.setCellRenderer(mo.createCellRenderer());
        syntaxPainter.setKeyWords2(mo.getAllTables());
        java.text.SimpleDateFormat stf = new java.text.SimpleDateFormat("HH:mm:ss");
        stsTree.setText("updated " + (stf.format(new java.util.Date())));
        stsHighlight.setText("table highlighting on");
    }
    public void setLoadingTreeModel()
    {
        objTree.setModel(loadingTree);
        objTree.setCellRenderer(new MyRenderer("attention"));
        objTree.setRootVisible(false);
        syntaxPainter.setKeyWords2(new java.util.Vector());
        stsHighlight.setText("no table highlighting");
    }

    public void setReLoadingTreeModel()
    {
        objTree.setModel(loadingTree);
        objTree.setCellRenderer(new MyRenderer("reload"));
        objTree.setRootVisible(false);
    }

    public void setEmptyTreeModel()
    {
        objTree.setModel(emptyTree);
        objTree.setCellRenderer(new MyRenderer("nodb"));
        objTree.setRootVisible(false);
        stsHighlight.setText("no table highlighting");
    }

    public void setReloadState(boolean reloadEnabled)
    {
        objTree.setReloadState(reloadEnabled);
    }

    public String getName(){return this.getClass().getName();}

  private void jbInit() throws Exception {
    border1 = BorderFactory.createLineBorder(Color.red,2);
    titledBorder1 = new TitledBorder(BorderFactory.createLineBorder(new Color(201, 69, 138),6),"SQL");
    border2 = BorderFactory.createMatteBorder(6,6,6,6,new Color(201, 69, 138));
    titledBorder2 = new TitledBorder(BorderFactory.createLineBorder(new Color(201, 69, 138),6),"scratch pad");
    border3 = BorderFactory.createCompoundBorder(new TitledBorder(BorderFactory.createLineBorder(new Color(82, 162, 182),6),"scratch pad"),BorderFactory.createEmptyBorder(6,6,6,6));

    border4 = BorderFactory.createLineBorder(new Color(235, 255, 235),6);
    border5 = BorderFactory.createLineBorder(new Color(255, 255, 225),6);
    border6 = BorderFactory.createCompoundBorder(titledBorder1,BorderFactory.createEmptyBorder(6,6,6,6));
    border8 = BorderFactory.createEmptyBorder(4,6,4,4);
    border9 = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.white,1),BorderFactory.createEmptyBorder(10,10,0,0));
    border10 = BorderFactory.createBevelBorder(BevelBorder.RAISED,Color.white,Color.white,new Color(115, 114, 105),new Color(165, 163, 151));
    //    border11 = BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(Color.white,Color.red),BorderFactory.createEmptyBorder(6,6,0,0));
    border12 = BorderFactory.createLineBorder(SystemColor.controlText,1);
        border13 = BorderFactory.createBevelBorder(BevelBorder.RAISED,Color.white,Color.white,Color.lightGray,Color.lightGray);
        border14 = BorderFactory.createBevelBorder(BevelBorder.LOWERED,new Color(235, 235, 255),new Color(235, 235, 255),new Color(114, 114, 114),new Color(163, 163, 163));
    border15 = BorderFactory.createMatteBorder(1,1,0,1,new Color(80, 80, 160));
    border16 = BorderFactory.createLineBorder(new Color(80, 80, 160),1);
    border17 = BorderFactory.createMatteBorder(0,1,0,1,new Color(80, 80, 160));
    this.setLayout(borderLayout1);
    splitPane.setBackground(new Color(187, 221, 228));
    splitPane.setBorder(null);
    splitPane.setDoubleBuffered(false);
    splitPane.setContinuousLayout(false);
    splitPane.setDividerSize(12);
    splitPane.setOneTouchExpandable(true);
    sqlWindow.setBackground(new Color(255, 255, 225));
    sqlWindow.setFont(new java.awt.Font("Dialog", 0, 12));
    sqlWindow.setBorder(border8);
    sqlWindow.setMaximumSize(new Dimension(2147483647, 2147483647));
    sqlWindow.setToolTipText("SQL statement window");
    sqlWindow.setSelectedTextColor(Color.white);
        sqlWindow.setSelectionColor(new Color(49, 55, 208));
    sqlWindow.setText("jTextArea1");
    sqlWindow.setRows(0);
    sqlWindow.addMouseListener(new Dog4SQLSQLPanel_sqlWindow_mouseAdapter(this));
    sqlWindow.addFocusListener(new Dog4SQLSQLPanel_sqlWindow_focusAdapter(this));
    scratchWindow.setBackground(new Color(255, 255, 225));
    scratchWindow.setForeground(Color.black);
    scratchWindow.setAlignmentY((float) 0.5);
    scratchWindow.setBorder(border5);
    //scratchWindow.setMaximumSize(new Dimension(2147483647, 2147483647));
    //scratchWindow.setMinimumSize(new Dimension(67, 24));
    //scratchWindow.setPreferredSize(new Dimension(67, 24));
    scratchWindow.setRequestFocusEnabled(true);
    scratchWindow.setToolTipText("scratch panel");
    scratchWindow.setMargin(new Insets(0, 0, 0, 0));
    scratchWindow.setSelectionStart(3);
    scratchWindow.setRows(0);
    scratchWindow.setTabSize(8);
    scratchWindow.addMouseListener(new Dog4SQLSQLPanel_scratchWindow_mouseAdapter(this));
    scrollPaneScratch.setViewportBorder(border15);
    scrollPaneScratch.setAutoscrolls(false);
    scrollPaneScratch.setBorder(null);
    scrollPaneScratch.setMinimumSize(new Dimension(100, 50));
    scrollPaneScratch.setPreferredSize(new Dimension(75, 32));
    scrollPaneSQL.setViewportBorder(border15);
    scrollPaneSQL.getViewport().setBackground(new Color(187, 221, 228));
    scrollPaneSQL.setAlignmentY((float) 0.5);
    scrollPaneSQL.setBorder(null);
    scrollPaneSQL.setPreferredSize(new Dimension(75, 32));
    scrollPaneSQL.getVerticalScrollBar().addAdjustmentListener(this);
    splitPane2.setBackground(new Color(187, 221, 228));
    splitPane2.setBorder(null);
    splitPane2.setDividerSize(12);
    splitPane2.setOneTouchExpandable(true);
    scrollPaneTree.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scrollPaneTree.setViewportBorder(border15);
    //jScrollPane3.setViewport(null);
    scrollPaneTree.setBackground(new Color(187, 221, 228));
    //jScrollPane3.getViewport().setBackground(new Color(187, 221, 228));
    scrollPaneTree.setAlignmentY((float) 0.5);
    scrollPaneTree.setAutoscrolls(false);
        scrollPaneTree.setBorder(null);
    scrollPaneTree.setMinimumSize(new Dimension(100, 25));
    scrollPaneTree.setOpaque(true);
    scrollPaneTree.setPreferredSize(new Dimension(95, 32));
    scrollPaneTree.setToolTipText("displays database metadata tree");
    scrollPaneTree.setVerifyInputWhenFocusTarget(true);

    this.setBackground(new Color(187, 221, 228));
    this.setAlignmentY((float) 0.5);
    this.setBorder(null);
    this.setPreferredSize(new Dimension(520, 342));
    pnlStatusBar.setBackground(new Color(221, 221, 228));
    pnlStatusBar.setForeground(new Color(80, 80, 160));
        pnlStatusBar.setAlignmentX((float) 0.5);
    pnlStatusBar.setBorder(border16);
        pnlStatusBar.setDebugGraphicsOptions(0);
        pnlStatusBar.setMaximumSize(new Dimension(16, 14));
        pnlStatusBar.setMinimumSize(new Dimension(16, 14));
    pnlStatusBar.setOpaque(true);
        pnlStatusBar.setPreferredSize(new Dimension(16, 14));
        pnlStatusBar.setLayout(flowLayout1);
        stsUndo.setFont(new java.awt.Font("Dialog", 0, 9));
        stsUndo.setForeground(new Color(120, 120, 160));
        stsUndo.setAlignmentY((float) 0.0);
        stsUndo.setMaximumSize(new Dimension(120, 14));
        stsUndo.setMinimumSize(new Dimension(96, 14));
        stsUndo.setPreferredSize(new Dimension(96, 12));
        stsUndo.setHorizontalAlignment(SwingConstants.LEFT);
        stsUndo.setHorizontalTextPosition(SwingConstants.LEFT);
        stsUndo.setIconTextGap(1);
        stsUndo.setText(" ");
        stsUndo.setVerticalAlignment(SwingConstants.TOP);
    stsUndo.setVerticalTextPosition(SwingConstants.CENTER);
        stsRedo.setFont(new java.awt.Font("Dialog", 0, 9));
        stsRedo.setForeground(new Color(120, 120, 160));
        stsRedo.setAlignmentY((float) 0.5);
    stsRedo.setBorder(null);
        stsRedo.setMaximumSize(new Dimension(120, 14));
        stsRedo.setMinimumSize(new Dimension(96, 14));
        stsRedo.setPreferredSize(new Dimension(100, 12));
    stsRedo.setDisplayedMnemonic('0');
        stsRedo.setHorizontalAlignment(SwingConstants.LEFT);
        stsRedo.setHorizontalTextPosition(SwingConstants.LEFT);
        stsRedo.setText(" ");
        stsRedo.setVerticalAlignment(SwingConstants.TOP);
    stsRedo.setVerticalTextPosition(SwingConstants.CENTER);
        flowLayout1.setAlignment(FlowLayout.LEFT);
        flowLayout1.setHgap(6);
        flowLayout1.setVgap(0);
        sep1.setBorder(border17);
    sep1.setDebugGraphicsOptions(0);
        sep1.setMaximumSize(new Dimension(1, 16));
        sep1.setMinimumSize(new Dimension(1, 16));
        sep1.setPreferredSize(new Dimension(3, 12));
        sep1.setToolTipText("");
        sep1.setText("");
        sep1.setVerticalTextPosition(SwingConstants.TOP);
        sep2.setBorder(border17);
        sep2.setDebugGraphicsOptions(0);
        sep2.setMaximumSize(new Dimension(1, 16));
        sep2.setMinimumSize(new Dimension(1, 16));
        sep2.setPreferredSize(new Dimension(3, 12));
        sep2.setRequestFocusEnabled(true);
        sep2.setText("");
        pnlEdit.setLayout(borderLayout2);
    pnlEdit.setMaximumSize(new Dimension(2147483647, 2147483647));
    pnlEdit.setMinimumSize(new Dimension(80, 80));
    pnlEdit.setOpaque(false);
    pnlEdit.setPreferredSize(new Dimension(120, 120));
    lblEmpty.setBorder(BorderFactory.createLoweredBevelBorder());
    lblEmpty.setMaximumSize(new Dimension(600, 14));
    lblEmpty.setPreferredSize(new Dimension(60, 14));
    pnlTree.setLayout(borderLayout3);
    pnlStatusTree.setBackground(new Color(221, 221, 228));
    pnlStatusTree.setBorder(border16);
    pnlStatusTree.setMaximumSize(new Dimension(16, 14));
    pnlStatusTree.setMinimumSize(new Dimension(16, 14));
    pnlStatusTree.setPreferredSize(new Dimension(16, 14));
    pnlStatusTree.setLayout(flowLayout2);

    stsTree.setFont(new java.awt.Font("Dialog", 0, 9));
    stsTree.setForeground(new Color(120, 120, 160));
    stsTree.setMaximumSize(new Dimension(55, 14));
    stsTree.setMinimumSize(new Dimension(225, 14));
    stsTree.setPreferredSize(new Dimension(85, 14));
    stsTree.setText("no metadata loaded");
    stsTree.setVerticalAlignment(SwingConstants.TOP);
    flowLayout2.setAlignment(FlowLayout.LEFT);
    flowLayout2.setHgap(6);
    flowLayout2.setVgap(0);

    objTree.setAutoscrolls(false);
    objTree.setBorder(null);
    objTree.setDebugGraphicsOptions(0);
    objTree.setToolTipText("");
    //objTree.setMaximumSize(new Dimension(2147483647, 2147483647));
    //objTree.setMinimumSize(new Dimension(20, 20));
    //objTree.setPreferredSize(new Dimension(0, 0));
    objTree.setAnchorSelectionPath(null);
    objTree.setScrollsOnExpand(true);
        objTree.addMouseListener(new Dog4SQLSQLPanel_objTree_mouseAdapter(this));
    pnlStatusScratch.setBackground(new Color(221, 221, 228));
    pnlStatusScratch.setBorder(border16);
    pnlStatusScratch.setMaximumSize(new Dimension(16, 14));
    pnlStatusScratch.setMinimumSize(new Dimension(16, 14));
    pnlStatusScratch.setPreferredSize(new Dimension(16, 14));
    pnlScratch.setLayout(borderLayout6);
    pnlScratch.setPreferredSize(new Dimension(120, 120));
    pnlTree.setMinimumSize(new Dimension(80, 80));
    pnlTree.setPreferredSize(new Dimension(120, 120));


        stsHighlight.setFont(new java.awt.Font("Dialog", 0, 9));
    stsHighlight.setForeground(new Color(120, 120, 160));
    stsHighlight.setPreferredSize(new Dimension(106, 12));
    stsHighlight.setText("no table highlighting");
    syntaxPainter.setSupportsCStyleComments(true);
        syntaxPainter.setAllowsMultilineString(true);
        pnlTree.add(scrollPaneTree,  BorderLayout.CENTER);
    pnlTree.add(pnlStatusTree,  BorderLayout.SOUTH);
    pnlStatusTree.add(stsTree, null);
    scrollPaneTree.getViewport().add(objTree, null);
    splitPane2.add(pnlTree, JSplitPane.LEFT);

   //splitPane2.add(scrollPaneTree, JSplitPane.LEFT);//??
   //scrollPaneTree.getViewport().add(objTree, null);

    splitPane2.add(scrollPaneScratch,JSplitPane.RIGHT);
       // this.add(pnlStatusBar,  BorderLayout.SOUTH);
    //splitPane.add(scrollPaneSQL, JSplitPane.LEFT);
    pnlEdit.add(scrollPaneSQL, BorderLayout.CENTER);
    pnlEdit.add(pnlStatusBar, BorderLayout.SOUTH);
    splitPane.add(pnlEdit, JSplitPane.LEFT);
    scrollPaneSQL.getViewport().add(sqlWindow, null);

    pnlScratch.add(scrollPaneScratch, BorderLayout.CENTER);
    pnlScratch.add(pnlStatusScratch, BorderLayout.SOUTH);
    splitPane2.add(pnlScratch,JSplitPane.RIGHT);
    //splitPane2.add(scrollPaneScratch,JSplitPane.RIGHT);
    scrollPaneScratch.getViewport().add(scratchWindow, null);

    splitPane.add(splitPane2, JSplitPane.RIGHT);

    this.add(splitPane, BorderLayout.CENTER);
        pnlStatusBar.add(stsHighlight, null);
        pnlStatusBar.add(sep1, null);
        pnlStatusBar.add(stsRedo, null);
        pnlStatusBar.add(sep2, null);
    pnlStatusBar.add(stsUndo, null);
    splitPane.setDividerLocation(350);
    splitPane2.setDividerLocation(180);


  }

  void sqlWindow_focusGained(FocusEvent e) {
      //if(autoSelect)
			//sqlWindow.select(selectionStart, selectionEnd);
  }

  void sqlWindow_focusLost(FocusEvent e) {
      selectionStart = sqlWindow.getSelectionStart();
      selectionEnd = sqlWindow.getSelectionEnd();
      autoSelect = true;

  }

  void sqlWindow_mouseReleased(MouseEvent e)
  {
      mouseListener.mouseReleased(e);
  }

  void scratchWindow_mouseReleased(MouseEvent e)
  {
      mouseListener.mouseReleased(e);
  }

///// ----------------
  /*
    public void updateUndoRedoDisplay(boolean canUndo, String undoText, boolean canRedo, String redoText)
    {
        //System.out.println("canUndo:" + canUndo + " canRedo:" + canRedo);
        for(int i = 0; i < pnlStatusBar.getComponentCount(); i++)
        {
            if("stsUndo".equals(pnlStatusBar.getComponent(i).getName()))
            {
                //pnlStatusBar.getComponent(i).setForeground(canUndo ? new Color(80, 80, 160) : new Color(160, 160, 160));
                try{
                    ((JLabel)pnlStatusBar.getComponent(i)).setText(canUndo ? undoText + " CTRL+Z" : "can't undo");
                }catch(ClassCastException ce){}
            }
            if("stsRedo".equals(pnlStatusBar.getComponent(i).getName()))
            {
                //pnlStatusBar.getComponent(i).setForeground(canRedo ? new Color(80, 80, 160) : new Color(160, 160, 160));
                try{
                    ((JLabel)pnlStatusBar.getComponent(i)).setText(canRedo ? redoText + " CTRL+Y" : "can't redo");
                }catch(ClassCastException ce){}

            }

        }
    }
        */
    class MyRenderer extends DefaultTreeCellRenderer
    {
        String gifBaseName = "default";
      public MyRenderer(String gifBaseName)
      {
          this.gifBaseName = gifBaseName;
      }

      public Component getTreeCellRendererComponent(
                          JTree tree,
                          Object value,
                          boolean sel,
                          boolean expanded,
                          boolean leaf,
                          int row,
                          boolean hasFocus) {

          super.getTreeCellRendererComponent(
                          tree, value, sel,
                          expanded, leaf, row,
                          hasFocus);

          java.net.URL url = getClass().getResource(Constants.ICON_PATH + gifBaseName + ".gif");
          if(url != null)
          {
            setIcon(new ImageIcon(url));
          }
              setToolTipText("" + tree.getPathForRow(row));
          return this;
      }
    }

    void objTree_mouseReleased(MouseEvent e)
    {
        mouseListener.mouseReleased(e);
    }
  public JScrollPane getScrollPaneSQL() {
    return scrollPaneSQL;
  }

}

class Dog4SQLSQLPanel_sqlWindow_focusAdapter extends java.awt.event.FocusAdapter {
  SQLPanel adaptee;

  Dog4SQLSQLPanel_sqlWindow_focusAdapter(SQLPanel adaptee) {
    this.adaptee = adaptee;
  }
  public void focusGained(FocusEvent e) {
    adaptee.sqlWindow_focusGained(e);
  }
  public void focusLost(FocusEvent e) {
    adaptee.sqlWindow_focusLost(e);
  }
}

class Dog4SQLSQLPanel_sqlWindow_mouseAdapter extends java.awt.event.MouseAdapter
{
  SQLPanel adaptee;

  Dog4SQLSQLPanel_sqlWindow_mouseAdapter(SQLPanel adaptee)
  {
    this.adaptee = adaptee;
  }
  public void mouseReleased(MouseEvent e)
  {
    adaptee.sqlWindow_mouseReleased(e);
  }
}

class Dog4SQLSQLPanel_scratchWindow_mouseAdapter extends java.awt.event.MouseAdapter
{
  SQLPanel adaptee;

  Dog4SQLSQLPanel_scratchWindow_mouseAdapter(SQLPanel adaptee)
  {
    this.adaptee = adaptee;
  }
  public void mouseReleased(MouseEvent e)
  {
    adaptee.scratchWindow_mouseReleased(e);
  }

}

class Dog4SQLSQLPanel_objTree_mouseAdapter extends java.awt.event.MouseAdapter
{
    SQLPanel adaptee;

    Dog4SQLSQLPanel_objTree_mouseAdapter(SQLPanel adaptee)
    {
        this.adaptee = adaptee;
    }
    public void mouseReleased(MouseEvent e)
    {
        adaptee.objTree_mouseReleased(e);
    }
}
