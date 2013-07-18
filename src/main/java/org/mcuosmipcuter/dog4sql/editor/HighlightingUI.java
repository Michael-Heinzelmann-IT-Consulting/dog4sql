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

import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicTextAreaUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.PlainView;
import javax.swing.text.View;


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