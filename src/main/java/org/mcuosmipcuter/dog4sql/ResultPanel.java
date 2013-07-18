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
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

public class ResultPanel extends JPanel implements WorkSpacePersistence,
                                                            ResultProcessor.ColSizeListener,
                                                        ResultProcessor.FindListener,
                                                    ResultProcessor.SaveListener
{
	public static final String WS_VERSION	= "1.1";
    java.awt.Point findPoint = new java.awt.Point(0, 1);
	Controller controller;
    MouseListener mouseListener;
    ResultProcessor.TableModel currentTableModel;
	String windowRows = Constants.INITIAL_WINDOW_ROWS;
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel panelNorth = new JPanel();
  JPanel panelSettings = new JPanel();
  GridLayout gridLayout1 = new GridLayout();
  JSplitPane jSplitPane1 = new JSplitPane();
  JScrollPane sPaneMsg = new JScrollPane();
  JTextPane taMessage = new JTextPane();
  JPanel panelRes = new JPanel();
  BorderLayout borderLayout2 = new BorderLayout();
  Border border1;
  Border border2;
  JCheckBox ckSizeColumns = new JCheckBox();
  Border border3;
  Border border4;
  Border border5;
  Border border6;
  Border border7;
  TitledBorder titledBorder1;
  Border border8;
  JTextField tfFind = new JTextField();
  JButton btnFind = new JButton();
  TableModel defaultModel = new DefaultTableModel(new Object[0][0], new Object[0]);
  JScrollPane sPaneOut;

  MyJTable resTable = new MyJTable(defaultModel);
  JProgressBar progBar = new JProgressBar();
  JButton btnResInfo = new JButton();
  JButton btnStop = new JButton();
  JCheckBox ckExpandLobs = new JCheckBox();
  Border border9;
  JCheckBox ckRegex  = new JCheckBox();
  JCheckBox ckMatchCase = new JCheckBox();

	public ResultPanel(Controller controller, MouseListener mouseListener)
	{
        this.controller = controller;
        this.mouseListener = mouseListener;
        try
        {
            resTable.getTableHeader().setBackground(new Color(225, 225, 205));
            Font fo = resTable.getTableHeader().getFont();
            resTable.getTableHeader().setFont(fo.deriveFont(Font.BOLD));
            sPaneOut= new JScrollPane(resTable);
            jbInit();
            sPaneOut.getHorizontalScrollBar().setPreferredSize(new
              Dimension(16, 16));
          sPaneOut.getVerticalScrollBar().setPreferredSize(new
              Dimension(16, 16));
            sPaneOut.setBackground(new Color(187,221,228));
            taMessage.setName("message window");
            resTable.setName("results table");
            resTable.setBorder(null);
            //resTable.getTableHeader().setBorder(BorderFactory.createMatteBorder(0,0,1,0,new Color(80,80,160)));
            resTable.getTableHeader().setBorder(BorderFactory.createMatteBorder(0,0,1,0,new Color(80,80,160)));
            resTable.setSelectionBackground(new Color(238,231,248));
            resTable.getTableHeader().setBackground(new Color(221,221,228));
        }
        catch (Exception ex)
        {
        }
		setConnState(false);
	}

	public void setMessage(Exception e)
	{
		taMessage.setBackground(Constants.COLOR_ERROR);
        e.printStackTrace();
		appendMessage(e.getClass().toString().substring(6) + ": " + e.getMessage());
	}

	public void setMessage(String message)
	{
		taMessage.setBackground(Constants.COLOR_ALRIG);
		appendMessage(message);
	}

    public void setEnabled(boolean enabled)
    {
        resTable.setModel(defaultModel);
        resTable.getTableHeader().setEnabled(enabled);
        resTable.setEnabled(enabled);
        sPaneOut.getHorizontalScrollBar().setEnabled(enabled);
        sPaneOut.getVerticalScrollBar().setEnabled(enabled);
    }

    public void setDisconnected(String message)
    {
        setEnabled(false);
        taMessage.setBackground(Constants.COLOR_DISCONNECTED);
        appendMessage(message);
    }

	private void appendMessage(String message)
	{
		taMessage.setText(taMessage.getText() + "\n" + (new Date())
												+ Constants.MESSAGE_SEP + message);
		taMessage.setCaretPosition(taMessage.getText().length());
	}

	private void setOutWindowFont(int size)
	{
			////outWindow.setFont(new Font("Monospaced", Font.PLAIN, size));
            resTable.setFont(new Font("Monospaced", Font.PLAIN, size));
	}

    public void setConnState(boolean connected) { sPaneOut.setEnabled(connected); }
	public boolean isDisplayRownumber()	{ return ckSizeColumns.isSelected();}
    public boolean isExpandLobs()	{ return true;}
    public int getScrollBarValue() { return sPaneOut.getVerticalScrollBar().getValue(); }

    public String getName(){return this.getClass().getName();}

    public void setResult(ResultProcessor.TableModel model)
    {
        currentTableModel = model;
        currentTableModel.setExpandLobs(ckExpandLobs.isSelected());
        progBar.setValue(0);
        tfFind.setBackground(Color.white);
        resTable.setText(model.toString());
        resTable.getToolkit().beep();
        resTable.setModel(model);
        resTable.doLayout();
        sPaneOut.getHorizontalScrollBar().setEnabled(true);
        sPaneOut.getVerticalScrollBar().setEnabled(true);
        resTable.setEnabled(true);
        btnFind.setEnabled(true);

        if(model.getColumnCount() > 0)
        {
            TableCellRenderer hr = resTable.getTableHeader().getDefaultRenderer();
            resTable.getColumnModel().getColumn(0).setCellRenderer(hr);
            resTable.getColumnModel().getColumn(0).setResizable(false);
        }
    }

    public void setMinColumnWidths(boolean alsoPreferred)
    {
        if(resTable.toString() == null || resTable.toString().equals(""))
           return;
        FontMetrics fm = resTable.getTableHeader().getFontMetrics(resTable.getTableHeader().getFont());
        ResultProcessor.TableModel model = (ResultProcessor.TableModel)resTable.getModel();
        if(model.getColumnCount() > 0)
        {
            for (int i = 1; i < model.getColumnCount(); i++)
            {
                int l = fm.stringWidth(model.getColumnName(i)+"  ");
                resTable.getTableHeader().getColumnModel().getColumn(i).setMinWidth(l);
                if(alsoPreferred)
                    resTable.getTableHeader().getColumnModel().getColumn(i).setPreferredWidth(l);
            }
        }
    }

    public void setColumnWidths()
        {
            if(ckSizeColumns.isSelected())
            {
                if(resTable.getModel() instanceof ResultProcessor.TableModel)
                {
                    ResultProcessor.TableModel model = (ResultProcessor.
                        TableModel) resTable.getModel();
                    model.resetColSizes();
                }
                //resTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            }
            else
            {
                setMinColumnWidths(true);

            }

        }


	public void setWorkSpace(WorkSpace workSpace)
	{
		if(	workSpace == null
			|| !(workSpace instanceof ResultPanelWorkSpace))
			return;

		ResultPanelWorkSpace ws = (ResultPanelWorkSpace)workSpace;
        tfFind.setText(ws.getFindText());
		ckSizeColumns.setSelected(ws.isSizeColumns());
        ckExpandLobs.setSelected(ws.isExpandLobs());
		setOutWindowFont(ws.getFontSize());
		ckRegex.setSelected(ws.isRegex());
		ckMatchCase.setSelected(ws.isMatchCase());
		ckMatchCase.setEnabled(!ckRegex.isSelected());
	}

	public WorkSpace getWorkSpace()
	{
		return  new ResultPanelWorkSpace(	tfFind.getText(),
                                                ckSizeColumns.isSelected(),
                                                ckExpandLobs.isSelected(),
                                                resTable.getFont().getSize(),
												ckRegex.isSelected(), 
												ckMatchCase.isSelected());
	}

  public ResultPanel() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  protected class MyJTable extends JTable
  {
      private boolean selectRunning;

      String text = "";
      protected MyJTable(){}
      protected MyJTable(TableModel model)
      {
          super(model);
      }
      public String toString()
      {
          return text;
      }

      public void setText(String t)
      {
          text =t ;
      }
      public void setFont(Font f)
      {
          if(f.getSize() > this.getRowHeight() - 4)
          {
              this.setRowHeight(f.getSize() + 4);
          }
          else if (this.getRowHeight() != 16)
          {
              this.setRowHeight(16);
          }
          super.setFont(f);
      }


    protected void processKeyEvent(KeyEvent e)
    {
        if(e.getID() == KeyEvent.KEY_PRESSED)
        {
            if (e.isControlDown())
            {
                if (e.getKeyCode() == KeyEvent.VK_C)
                {
                    int cols = this.getColumnCount();
                    int rows = this.getRowCount();
                    if (cols == this.getSelectedColumnCount() &&
                        rows == this.getSelectedRowCount())
                    {
                        runCopyAll();
                        return;
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_LEFT)
                {
                    this.setColumnSelectionInterval(1, 1);
                    this.scrollRectToVisible(this.getCellRect(
                        getSelectedRow(), 0, true));
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT)
                {
                    this.setColumnSelectionInterval(this.getColumnCount() -
                        1, this.getColumnCount() - 1);
                    this.scrollRectToVisible(this.getCellRect(
                        getSelectedRow() - 1, this.getColumnCount() - 1, true));
                }
                if (e.getKeyCode() == KeyEvent.VK_UP)
                {
                    this.setRowSelectionInterval(0, 0);
                    this.scrollRectToVisible(this.getCellRect(0,
                        getSelectedColumn(), true));
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN)
                {
                    this.setRowSelectionInterval(this.getRowCount() - 1,
                        this.getRowCount() - 1);
                    this.scrollRectToVisible(this.getCellRect(this.
                        getRowCount() - 1, getSelectedColumn(), true));
                }

            }
            if (e.getKeyCode() == KeyEvent.VK_LEFT)
            {
                // workaround for not making cursor disappear in row number column
                if (this.getSelectedColumn() == 1)
                    return;
            }
        }
        super.processKeyEvent(e);
    }

     public void copyHeaderNames()
      {
          int cols = this.getColumnCount();
          StringBuffer buf = new StringBuffer();
          buf.append("");
          for(int c = 0; c < cols; c++)
          {
              buf.append(this.getColumnName(c));
              buf.append("\t");
          }
          java.awt.datatransfer.StringSelection s = new java.awt.datatransfer.StringSelection(buf.toString());
          Toolkit.getDefaultToolkit().getSystemClipboard().setContents(s, s);
      }

      public void saveCsv()
      {
          panelSaveCsv();
      }


  }
  private void jbInit() throws Exception {
    border1 = BorderFactory.createLineBorder(new Color(208, 228, 220),4);
    border2 = BorderFactory.createLineBorder(new Color(255, 255, 60),1);
    border3 = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.lightGray,1),BorderFactory.createEmptyBorder(0,4,0,6));
    border4 = BorderFactory.createLineBorder(new Color(187, 221, 228),2);
    border5 = BorderFactory.createLineBorder(new Color(60, 60, 160),2);
    border6 = BorderFactory.createLineBorder(new Color(60, 60, 160),2);
    border7 = BorderFactory.createLineBorder(new Color(255, 214, 42),6);
    titledBorder1 = new TitledBorder(BorderFactory.createLineBorder(new Color(0, 187, 0),1),"results");
    border8 = BorderFactory.createCompoundBorder(new TitledBorder(BorderFactory.createLineBorder(new Color(201, 69, 138),6),"results"),BorderFactory.createEmptyBorder(0,6,0,0));
    border9 = BorderFactory.createLineBorder(new Color(80, 80, 160),1);
    this.setLayout(borderLayout1);
    panelNorth.setBackground(new Color(187, 221, 228));
    panelNorth.setAlignmentX((float) 0.5);
    panelNorth.setBorder(border4);
    panelNorth.setDebugGraphicsOptions(0);
    panelNorth.setLayout(gridLayout1);
    panelSettings.setBackground(UIManager.getColor("control"));
    panelSettings.setBorder(null);
    panelSettings.setOpaque(false);
        panelSettings.setPreferredSize(new Dimension(781, 28));
    gridLayout1.setColumns(1);
    jSplitPane1.setOrientation(JSplitPane.VERTICAL_SPLIT);
    jSplitPane1.setBackground(new Color(187, 221, 228));
    jSplitPane1.setEnabled(true);
    jSplitPane1.setForeground(Color.black);
    jSplitPane1.setAlignmentY((float) 0.0);
    jSplitPane1.setBorder(null);
    jSplitPane1.setDebugGraphicsOptions(0);
    jSplitPane1.setOpaque(true);
    jSplitPane1.setRequestFocusEnabled(true);
    jSplitPane1.setContinuousLayout(false);
    jSplitPane1.setDividerSize(12);
    jSplitPane1.setOneTouchExpandable(true);
    taMessage.setText("jTextPane1");
    taMessage.setBackground(new Color(255, 255, 235));
        taMessage.setMinimumSize(new Dimension(6, 6));
    taMessage.setOpaque(true);
    taMessage.setPreferredSize(new Dimension(7, 18));
    taMessage.setEditable(false);
    taMessage.setText("");
    taMessage.addMouseListener(new Dog4SQLResultPanel_taMessage_mouseAdapter(this));
    panelRes.setLayout(borderLayout2);
    ////outWindow.setBackground(new Color(255, 255, 235));
    ////outWindow.setFont(new java.awt.Font("Monospaced", 1, 12));
    ////outWindow.setAlignmentX((float) 0.5);
    ////outWindow.setAlignmentY((float) 0.5);
    ////outWindow.setBorder(border4);
    ////outWindow.setMaximumSize(new Dimension(2147483647, 2147483647));
    ////outWindow.setMinimumSize(new Dimension(103, 25));
    ////outWindow.setRequestFocusEnabled(true);
    ////outWindow.setEditable(false);
    ////outWindow.setMargin(new Insets(4, 4, 4, 4));
    ////outWindow.setText("");
    ////outWindow.setRows(40);
    /**@todo Dog4SQLResultPanel_outWindow_mouseAdapter(this)); */
    ckSizeColumns.setForeground(new Color(80, 80, 160));
    ckSizeColumns.setAlignmentX((float) 0.0);
    ckSizeColumns.setAlignmentY((float) 0.5);
    ckSizeColumns.setBorder(BorderFactory.createLoweredBevelBorder());
    ckSizeColumns.setOpaque(false);
        ckSizeColumns.setToolTipText("adjust column widths to the max. length of a columns value found " +
    "so far");
    ckSizeColumns.setBorderPainted(false);
    ckSizeColumns.setContentAreaFilled(true);
    ckSizeColumns.setMnemonic('Z');
    ckSizeColumns.setText("size columns to values");
    ckSizeColumns.addActionListener(new Dog4SQLResultPanel_ckSizeColumns_actionAdapter(this));
    borderLayout2.setHgap(6);
    borderLayout2.setVgap(0);
    sPaneOut.getViewport().setBackground(Color.white);
    sPaneOut.setBorder(null);
    sPaneOut.setMinimumSize(new Dimension(100, 100));
    sPaneOut.setToolTipText("");
    this.setBackground(new Color(187, 221, 228));
    this.setEnabled(false);
    this.setAlignmentX((float) 0.5);
        this.setBorder(null);
    this.setDoubleBuffered(true);
    this.setMinimumSize(new Dimension(397, 112));
    this.setPreferredSize(new Dimension(397, 771));
    sPaneMsg.setViewportBorder(border9);
        sPaneMsg.getViewport().setBackground(Color.white);
        sPaneMsg.setBorder(null);
    sPaneMsg.setMinimumSize(new Dimension(56, 20));
    panelRes.setBackground(new Color(187, 221, 228));
    panelRes.setOpaque(true);
    tfFind.setBorder(BorderFactory.createLoweredBevelBorder());
	tfFind.setPreferredSize(new Dimension(106, 19));
        tfFind.setToolTipText("text to find");
    tfFind.setText("");
    tfFind.addCaretListener(new Dog4SQLResultPanel_tfFind_caretAdapter(this));
    btnFind.setEnabled(false);
    btnFind.setBorder(null);
    btnFind.setPreferredSize(new Dimension(93, 21));
        btnFind.setToolTipText("starts or continues find");
    btnFind.setMnemonic('N');
    btnFind.setText("find next");
    btnFind.addActionListener(new Dog4SQLResultPanel_btnFind_actionAdapter(this));
    /**@todo Dog4SQLResultPanel_outWindow1_mouseAdapter(this)); */
    ////resTable.setBackground(Color.red);
    /////resTable.setFont(new java.awt.Font("Monospaced", 0, 11));
    //////resTable.setModel(defaultModel);
    resTable.setBackground(new Color(255, 255, 235));
    resTable.setAutoscrolls(true);
    resTable.setDebugGraphicsOptions(0);
    resTable.setVerifyInputWhenFocusTarget(true);
    resTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    resTable.setCellSelectionEnabled(true);
    resTable.setGridColor(Color.lightGray);
    resTable.setIntercellSpacing(new Dimension(1, 1));
    resTable.addMouseListener(new Dog4SQLResultPanel_resTable_mouseAdapter(this));
    progBar.setBackground(new Color(235, 235, 235));
    progBar.setFont(new java.awt.Font("Dialog", 0, 10));
    progBar.setForeground(Color.black);
    progBar.setBorder(null);
    progBar.setPreferredSize(new Dimension(100, 12));
        progBar.setToolTipText("progress");
    progBar.setBorderPainted(true);
    progBar.setStringPainted(true);
    btnResInfo.setBorder(null);
    btnResInfo.setMaximumSize(new Dimension(103, 21));
    btnResInfo.setMinimumSize(new Dimension(103, 21));
    btnResInfo.setPreferredSize(new Dimension(103, 21));
        btnResInfo.setToolTipText("show information about the last resultset retrieved");
    btnResInfo.setMnemonic('I');
    btnResInfo.setText("result info");
    btnResInfo.addActionListener(new Dog4SQLResultPanel_btnResInfo_actionAdapter(this));
    btnStop.setBorder(null);
    btnStop.setMaximumSize(new Dimension(65, 21));
    btnStop.setMinimumSize(new Dimension(65, 21));
    btnStop.setPreferredSize(new Dimension(65, 21));
        btnStop.setToolTipText("stops the current find or save");
    btnStop.setMnemonic('P');
    btnStop.setText("stop");
    btnStop.addActionListener(new Dog4SQLResultPanel_btnStop_actionAdapter(this));
    ckExpandLobs.setForeground(new Color(80, 80, 160));
    ckExpandLobs.setOpaque(false);
        ckExpandLobs.setToolTipText("expand BLOB\'s and CLOB\'s and show their value");
    ckExpandLobs.setMnemonic('E');
        ckExpandLobs.setText("expand LOB\'s");
        ckExpandLobs.addActionListener(new Dog4SQLResultPanel_ckExpandLobs_actionAdapter(this));
	ckRegex.setOpaque(false);
	ckRegex.setMnemonic('G');
	ckRegex.setText("regex");
	ckRegex.addActionListener(new Dog4SQLResultPanel_ckRegex_actionAdapter(this));
	ckMatchCase.setOpaque(false);
	ckMatchCase.setMnemonic('T');
	ckMatchCase.setText("match case");
        this.add(panelNorth,  BorderLayout.CENTER);
    this.add(panelSettings, BorderLayout.SOUTH);
    panelNorth.add(jSplitPane1, null);
    jSplitPane1.add(sPaneMsg, JSplitPane.TOP);
    sPaneMsg.getViewport().add(taMessage, null);
    sPaneMsg.getViewport().add(taMessage, null);
    jSplitPane1.add(panelRes, JSplitPane.BOTTOM);
    panelRes.add(sPaneOut,  BorderLayout.CENTER);
        panelSettings.add(btnResInfo, null);
    panelSettings.add(ckSizeColumns, null);
        panelSettings.add(ckExpandLobs, null);
    panelSettings.add(tfFind, null);
	panelSettings.add(ckRegex, null);
	panelSettings.add(ckMatchCase, null);
    panelSettings.add(btnFind, null);
    panelSettings.add(btnStop, null);
        panelSettings.add(progBar, null);
  }

  public void runCopyAll()
  {
      sPaneOut.getVerticalScrollBar().setEnabled(false);
      sPaneOut.getHorizontalScrollBar().setEnabled(false);
      resTable.setToolTipText("copy all in progress, please wait or press stop.");
      sPaneOut.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
      resTable.setEnabled(false);

      if(controller != null && controller.resultProcessor != null)
    controller.resultProcessor.copyAll(this , this.getParent());

progBar.setForeground(new Color(155, 125, 245));
progBar.setString("copy all");
btnStop.setToolTipText("stop the current copy all");
btnFind.setEnabled(false);
resTable.repaint();

  }
  public void panelSaveCsv()
   {
       JFileChooser chooser = new JFileChooser();
       int res = chooser.showSaveDialog(this);
       if(res != JFileChooser.APPROVE_OPTION)
           return;
       File f = chooser.getSelectedFile();

       sPaneOut.getVerticalScrollBar().setEnabled(false);
       sPaneOut.getHorizontalScrollBar().setEnabled(false);
       resTable.setToolTipText("save in progress, please wait or press stop.");
       sPaneOut.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
       resTable.setEnabled(false);

       if(controller != null && controller.resultProcessor != null)
           controller.resultProcessor.saveCsv(this, ",", f);
       //tfFind.setBackground(Constants.COLOR_DISCONNECTED);
       progBar.setForeground(new Color(155, 125, 245));
       progBar.setString("save");
       btnStop.setToolTipText("stop the current save");
       btnFind.setEnabled(false);
       resTable.repaint();

   }

   public void saveFinished()
   {
       resTable.setEnabled(true);
      resTable.setToolTipText("result");
      btnFind.setEnabled(true);
      sPaneOut.getVerticalScrollBar().setEnabled(true);
      sPaneOut.getHorizontalScrollBar().setEnabled(true);
      sPaneOut.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
      btnStop.setToolTipText("stops the current find or save");
   }


  public void onFindResult(Point foundAt)
  {
      resTable.setEnabled(true);
      resTable.setToolTipText("result");
      btnFind.setEnabled(true);
      progBar.setForeground(Constants.COLOR_DISCONNECTED);
      findPoint = foundAt;
      //System.out.println("findPoint:" + findPoint);

          sPaneOut.getVerticalScrollBar().setEnabled(true);
          sPaneOut.getHorizontalScrollBar().setEnabled(true);

          if (!(findPoint.x == 0 && findPoint.y == 1))
          {
              resTable.setColumnSelectionInterval(findPoint.x , findPoint.x );
              resTable.setRowSelectionInterval(findPoint.y-1, findPoint.y-1);
              sPaneOut.getVerticalScrollBar().setValue( (findPoint.y - 1) *
                  resTable.getRowHeight());
              int colPos = 0;
              for (int i = 0; i < findPoint.x; i++)
              {
                  colPos +=
                      resTable.getTableHeader().getColumnModel().getColumn(i).
                      getWidth();
              }
              sPaneOut.getHorizontalScrollBar().setValue(colPos);
              tfFind.setBackground(Constants.COLOR_ALRIG);
            resTable.repaint();
            getToolkit().beep();
          }
          else
          {
              tfFind.setBackground(Constants.COLOR_ERROR);
          }
          // in any case restore cursor
          sPaneOut.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
          btnStop.setToolTipText("stops the current find or save");
  }

  public void onProgress(float percentOfRows)
  {
      progBar.setValue((int)percentOfRows);
  }

  void btnFind_actionPerformed(ActionEvent e)
  {
      String find = tfFind.getText();
      if(find == null || find.equals(""))
      {
         return;
      }
      
      if(ckRegex.isSelected())
      {
      try 
      {
		"".matches(find);
		}
		 catch (RuntimeException ex) {
			JOptionPane.showMessageDialog(this, ex);
			return;
		} 
      }
      sPaneOut.getVerticalScrollBar().setEnabled(false);
      sPaneOut.getHorizontalScrollBar().setEnabled(false);
      resTable.setToolTipText("find in progress, please wait or press stop.");
      sPaneOut.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
      resTable.setEnabled(false);

      if(controller != null && controller.resultProcessor != null)
          controller.resultProcessor.find(find, new Point(findPoint.x + 1, findPoint.y ),
				ckRegex.isSelected(), ckMatchCase.isSelected(), this);
      tfFind.setBackground(Constants.COLOR_DISCONNECTED);
      progBar.setForeground(new Color(155, 125, 245));
      progBar.setString("find");
      btnStop.setToolTipText("stop the current find");
      resTable.repaint();
  }

  void tfFind_caretUpdate(CaretEvent e) {
      tfFind.setBackground(Color.white);
      findPoint = new Point(0, 1);
  }

  void outWindow_mouseReleased(MouseEvent e)
  {
      mouseListener.mouseReleased(e);
  }

  void taMessage_mouseReleased(MouseEvent e)
  {
      mouseListener.mouseReleased(e);
  }

  void resTable_mouseReleased(MouseEvent e)
  {
      mouseListener.mouseReleased(e);
  }

    public void newMaxSize(int colIndex, int maxSize)
    {
        if(!ckSizeColumns.isSelected() || colIndex >= resTable.getTableHeader().getColumnModel().getColumnCount())
            return;
        int fs = resTable.getFontMetrics(resTable.getFont()).charWidth('M');
        resTable.getTableHeader().getColumnModel().getColumn(colIndex).setPreferredWidth((maxSize+1) * fs);
    }

  void btnResInfo_actionPerformed(ActionEvent e) {
      String msg = resTable.toString();
      if(msg == null || msg.equals(""))
         msg = "no result";
      JOptionPane.showMessageDialog(this, msg, "result information", JOptionPane.INFORMATION_MESSAGE);
  }

  void btnStop_actionPerformed(ActionEvent e) {
      if(controller != null && controller.resultProcessor != null)
      {
          controller.resultProcessor.stopFind();
          controller.resultProcessor.stopSave();
          controller.resultProcessor.stopCopyAll();
          btnStop.setToolTipText("stops the current find, copy all or save");
      }
      sPaneOut.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
  }

    void ckSizeColumns_actionPerformed(ActionEvent e) {
        setColumnWidths();
        resTable.repaint();
    }

    void ckExpandLobs_actionPerformed(ActionEvent e) {
        if(currentTableModel != null)
        {
            setColumnWidths();
            currentTableModel.setExpandLobs(ckExpandLobs.isSelected());
            resTable.repaint();
        }
    }

	void ckRegex_actionPerformed(ActionEvent e)
	{
		ckMatchCase.setEnabled(!ckRegex.isSelected());
	}
}



class Dog4SQLResultPanel_ckSizeColumns_actionAdapter implements java.awt.event.ActionListener {
  ResultPanel adaptee;

  Dog4SQLResultPanel_ckSizeColumns_actionAdapter(ResultPanel adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.ckSizeColumns_actionPerformed(e);
  }
}

class Dog4SQLResultPanel_btnFind_actionAdapter implements java.awt.event.ActionListener {
  ResultPanel adaptee;

  Dog4SQLResultPanel_btnFind_actionAdapter(ResultPanel adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.btnFind_actionPerformed(e);
  }
}

class Dog4SQLResultPanel_tfFind_caretAdapter implements javax.swing.event.CaretListener {
  ResultPanel adaptee;

  Dog4SQLResultPanel_tfFind_caretAdapter(ResultPanel adaptee) {
    this.adaptee = adaptee;
  }
  public void caretUpdate(CaretEvent e) {
    adaptee.tfFind_caretUpdate(e);
  }
}


class Dog4SQLResultPanel_taMessage_mouseAdapter extends java.awt.event.MouseAdapter
{
  ResultPanel adaptee;

  Dog4SQLResultPanel_taMessage_mouseAdapter(ResultPanel adaptee)
  {
    this.adaptee = adaptee;
  }
  public void mouseReleased(MouseEvent e)
  {
    adaptee.taMessage_mouseReleased(e);
  }
}

class Dog4SQLResultPanel_resTable_mouseAdapter extends java.awt.event.MouseAdapter
{
  ResultPanel adaptee;

  Dog4SQLResultPanel_resTable_mouseAdapter(ResultPanel adaptee)
  {
    this.adaptee = adaptee;
  }
  public void mouseReleased(MouseEvent e)
  {
    adaptee.resTable_mouseReleased(e);
  }
}

class Dog4SQLResultPanel_btnResInfo_actionAdapter implements java.awt.event.ActionListener {
  ResultPanel adaptee;

  Dog4SQLResultPanel_btnResInfo_actionAdapter(ResultPanel adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.btnResInfo_actionPerformed(e);
  }
}

class Dog4SQLResultPanel_btnStop_actionAdapter implements java.awt.event.ActionListener {
  ResultPanel adaptee;

  Dog4SQLResultPanel_btnStop_actionAdapter(ResultPanel adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.btnStop_actionPerformed(e);
  }
}

class Dog4SQLResultPanel_ckExpandLobs_actionAdapter implements java.awt.event.ActionListener {
  ResultPanel adaptee;

  Dog4SQLResultPanel_ckExpandLobs_actionAdapter(ResultPanel adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.ckExpandLobs_actionPerformed(e);
  }
}

class Dog4SQLResultPanel_ckRegex_actionAdapter
	implements ActionListener
{

	Dog4SQLResultPanel_ckRegex_actionAdapter(ResultPanel adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
		adaptee.ckRegex_actionPerformed(e);
	}

	ResultPanel adaptee;
}