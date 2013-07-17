package org.mcuosmipcuter.sqldog.editor;

import javax.swing.plaf.basic.*;
import javax.swing.text.*;
import java.awt.*;
import javax.swing.event.*;
import javax.swing.event.DocumentEvent;
import java.beans.*;
import java.beans.PropertyChangeEvent;
import javax.swing.*;


public class HighlightingUI extends BasicTextAreaUI {
  SyntaxPainter syntaxPainter;
  CStyleCommentedDocument cStyleCommentedDocument;
  DocumentEvent currEvent;

  public HighlightingUI(SyntaxPainter sp, CStyleCommentedDocument doc)
  {
    syntaxPainter = sp;
    cStyleCommentedDocument = doc;
    cStyleCommentedDocument.setSingleLineComment(syntaxPainter.getSingleLineCommentStart());
    cStyleCommentedDocument.setStringDelimiters(syntaxPainter.getStringDelimiters());
    cStyleCommentedDocument.setAllowsMultilineString(syntaxPainter.isAllowsMultilineString());
    syntaxPainter.addPropertyChangeListener(new PropertyChangeListener()
    {
      public void propertyChange(PropertyChangeEvent e)
      {
        if(e.getPropertyName().equals("singleLineCommentStart"))
          cStyleCommentedDocument.setSingleLineComment((String)e.getNewValue());
        if(e.getPropertyName().equals("stringDelimiters"))
          cStyleCommentedDocument.setStringDelimiters((String)e.getNewValue());
        if(e.getPropertyName().equals("allowsMultilineString"))
          cStyleCommentedDocument.setAllowsMultilineString(((Boolean)e.getNewValue()).booleanValue());
        getComponent().repaint();
      }
    });
    cStyleCommentedDocument.addDocumentListener(new DocumentListener()
    {
      public void insertUpdate(DocumentEvent e){currEvent = e;}
      public void removeUpdate(DocumentEvent e){currEvent = e;}
      public void changedUpdate(DocumentEvent e)
      {
        currEvent = e;
        getComponent().repaint();
      }
    });
  }

  public View create(Element element)
  {
    return new PlainView(element)
    {
      int currLine;

      public void drawLine(int lineIndex, Graphics g, int x, int y)
      {
        currLine = lineIndex;
        super.drawLine(lineIndex, g, x, y);
      }

      protected int drawUnselectedText(Graphics g, int x, int y, int p0, int p1)
          throws BadLocationException
      {

        //System.out.println("___________ :" + p0+"|"+p1+" curr: "+currEvent);
        int tabSize = getComponent() instanceof JTextArea ? ((JTextArea)getComponent()).getTabSize(): 8;
        int ret = p1 - p0 != 0 ?
           syntaxPainter.paintLine(getComponent(), tabSize, g, getComponent().getText(p0, p1 - p0),
                        this, x, y, cStyleCommentedDocument.isCStyleCommentOpen(currLine - 1),
                        cStyleCommentedDocument.isStringOpen(currLine - 1),
                        cStyleCommentedDocument.getStringOpened(currLine - 1), currEvent, p0)

           : x;
       currEvent = null;
       return ret;
      }
    };

  }

}