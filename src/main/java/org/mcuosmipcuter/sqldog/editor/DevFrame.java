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
package org.mcuosmipcuter.sqldog.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;

public class DevFrame extends JFrame
{
  BorderLayout borderLayout1 = new BorderLayout();
  JScrollPane jScrollPane1 = new JScrollPane();
  //JEditorPane editorPane = new JEditorPane();
  JTextArea editorPane = new JTextArea()
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


  SyntaxPainter sp1 = new SyntaxPainter();
  TitledBorder titledBorder1;
  JPanel jPanel1 = new JPanel();
  JLabel stsUndo = new JLabel();
  JLabel stsRedo = new JLabel();


  public DevFrame()
  {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }

    CStyleCommentedDocument doc = new CStyleCommentedDocument();
    editorPane.setDocument(doc);
    editorPane.setUI(new HighlightingUI(sp1, doc));
    ResourceBundle bundle1 =  ResourceBundle.getBundle("org.mcuosmipcuter.sqldog.editor/java2", Locale.ENGLISH,this.getClass().getClassLoader());
    sp1.setReservedWords(bundle1);
    editorPane.setTabSize(4);



    /*
    editorPane.setUI(new BasicTextAreaUI()
    {
      public View create(Element e)
      {
        System.out.println("|" + e.getStartOffset());
        System.out.println("c" + e.getElementCount());
        //View v = super.create(e);
        View v = new PlainView(e)
        {
          int currLine;int pos;
          public  View breakView(int axis, int p0, float pos, float len)
          {
            System.out.println("p0:" + p0);
            return super.breakView(axis, p0,pos,len);
          }

          public void drawLine(int lineIndex, Graphics g, int x, int y)
          {
            //System.out.println("lineIndex:" + lineIndex);
            currLine = lineIndex;
           super.drawLine(lineIndex,g,x,y);
          }
          protected int drawSelectedText(Graphics g, int x, int y, int p0, int p1)
          throws BadLocationException
      {
        int r = super.drawSelectedText(g,x,y,p0,p1);
        //sp1.paintLine(editorPane,g,editorPane.getText(p0,p1-p0),8,x,currLine);
        System.out.println(">>>"+p0+"|"+p1);
        return r;

      }
          protected int drawUnselectedText(Graphics g, int x, int y, int p0, int p1)
          throws BadLocationException
      {
        //Segment s = new Segment();
        //getDocument().getText(p0,p1,s);
        //StringTokenizer strtok = new StringTokenizer(editorPane.getText(p0,p1-p0));
        //System.out.println("p0:" + p0 + "p1:" + p1 + "currLine:" + currLine + "|"  + editorPane.getText(p0,p1-p0) + "|" );
        int r1 = 0;//super.drawUnselectedText(g, x, y, p0, p1);
        //boolean openC = currLine > 0 && currLine < cCommentOpen.length ? cCommentOpen[currLine - 1] : false;
        boolean openC = doc.cCommentOpened(currLine -1);
        int tab = (int)this.nextTabStop(0, 8);
        int r2 = p1 - p0 != 0 ? sp1.paintLine(editorPane,g,editorPane.getText(p0,p1-p0),this,x,y, openC) : x;
        //System.out.println("y:"+y+"x:"+x+"p0:"+p0+"p1:"+p1);
        pos = 0;
        return r2;
      }

        };
        System.out.println("v" + v);
        System.out.println("getPropertyPrefix:" + this.getPropertyPrefix());
        return v;
      }
    });
        */
    //System.out.println("document: " + editorPane.getEditorKit());

  }
  public static void main(String[] args)
  {
    DevFrame devFrame = new DevFrame();
    devFrame.pack();
    devFrame.setVisible(true);
  }
  private void jbInit() throws Exception {
    titledBorder1 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(178, 178, 178)),"test");
    this.getContentPane().setLayout(borderLayout1);
    editorPane.setBackground(new Color(222, 232, 249));
    editorPane.setFont(new java.awt.Font("Monospaced", 0, 72));
    editorPane.setBorder(titledBorder1);
    editorPane.setMargin(new Insets(10, 10, 10, 10));
    editorPane.setSelectedTextColor(new Color(249, 255, 255));
    editorPane.setSelectionColor(new Color(49, 55, 208));
    editorPane.setText("test text");
    editorPane.setLineWrap(false);
    editorPane.setRows(8);
    editorPane.setTabSize(2);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    sp1.setColorDelimiters(new Color(152, 42, 0));
    sp1.setColorStrings(new Color(0, 201, 187));
    sp1.setSupportsCStyleComments(true);
    sp1.setAllowsMultilineString(true);
    sp1.setDecimalSeperator(",");
    stsUndo.setText("undo");
    stsRedo.setText("redo");
    this.getContentPane().add(jScrollPane1, BorderLayout.CENTER);
    this.getContentPane().add(jPanel1,  BorderLayout.SOUTH);
    jPanel1.add(stsUndo, null);
    jPanel1.add(stsRedo, null);
    jScrollPane1.getViewport().add(editorPane, null);
  }

}