package org.mcuosmipcuter.sqldog.editor;

import javax.swing.text.PlainDocument;
//import javax.swing.text.AbstractDocument$Content;
import javax.swing.text.*;
import javax.swing.event.*;
import java.util.*;

public class CStyleCommentedDocument extends PlainDocument
{
  public final static String ESCAPE = "\\";

  LineFlag[] lineFlags = new LineFlag[0]; // additional state for flag per line
  boolean allowsMultilineString = false;
  String stringDelimiters = "'\"";
  String singleLineComment = "-";

  protected interface LineFlag
  {
    public boolean isCStyleCommentOpen();
    public boolean isStringOpen();
    public String getStringOpened();
  }

  public CStyleCommentedDocument()
  {
    init();
  }

  public CStyleCommentedDocument(Content c) {
    super(c);
    init();
  }

  private void init()
  {
    addDocumentListener(new DocumentListener()
    {
      public void insertUpdate(DocumentEvent e)
      {
        parseCComments();
      }
      public void removeUpdate(DocumentEvent e)
      {
        parseCComments();
      }
      public void changedUpdate(DocumentEvent e)
      {
        parseCComments();
      }
    });
  }

  public void parseCComments()
  {
    boolean unchanged = false;

    String[] lines = null;
    try
    {
      lines = this.getText(0, this.getLength()).split("\n");
    }
    catch(BadLocationException ex)
    {}
    LineFlag[] compare = lineFlags;
    lineFlags = new LineFlag[lines.length];
    unchanged = compare != null ? lineFlags.length == compare.length : false;
    boolean oldOpen = false;
    boolean oldStringOpen = false;
    String oldStrStringOpened = null;
    int posInDocument = 0;
    for(int i = 0; i < lines.length; i++)
    {
      boolean stringOpened = allowsMultilineString ? oldStringOpen : false;
      boolean singleLineCommentStarted = false;
      boolean cStyleCommentStarted = oldOpen;
      String oldToken = null;
      String oldOldToken = null;
      String strStringOpened = allowsMultilineString ? oldStrStringOpened : null;
      int posInLine = 0;
      int cStyleCommentStartChar = 0;

      java.util.StringTokenizer strtok = new java.util.StringTokenizer(lines[i],
          "/*" + stringDelimiters + singleLineComment + ESCAPE, true);
      while(strtok.hasMoreElements())
      {
        String token = strtok.nextToken();
        posInLine += token.length();
        posInDocument += token.length();

        if(token.length() == 1
           && stringDelimiters.indexOf(token) > -1
           && !singleLineCommentStarted
           && !cStyleCommentStarted)
        {
          if(!stringOpened)
          {
            stringOpened = true;
            strStringOpened = token;
          }
          else if(strStringOpened.equals(token) && !ESCAPE.equals(oldToken))
          {
            stringOpened = false;
            strStringOpened = null;
          }
        }
        if(singleLineComment.equals(token)
           && singleLineComment.equals(oldToken)
           && !cStyleCommentStarted
           && !("/".equals(oldToken) && "*".equals(oldOldToken))
           && !stringOpened)
        {
          singleLineCommentStarted = true;
        }
        boolean unsetComments = false;
        if(!stringOpened
           && !singleLineCommentStarted
           && !cStyleCommentStarted
           && "*".equals(token)
           && "/".equals(oldToken)
           && !("*".equals(oldOldToken)))
        {
          cStyleCommentStarted = true;
          cStyleCommentStartChar = posInLine - 2;
        }
        unsetComments = (cStyleCommentStarted
                         && "/".equals(token)
                         && "*".equals(oldToken)
                         && !("/".equals(oldOldToken)
                         && ((posInLine - cStyleCommentStartChar) == 3)));

        oldOldToken = oldToken;
        oldToken = token;
        if(unsetComments)
        {
          cStyleCommentStarted = false;
        }

      } // while

      oldOpen = cStyleCommentStarted;
      oldStringOpen = stringOpened;
      oldStrStringOpened = strStringOpened;
      if(unchanged)
      {
        unchanged = compare[i].isCStyleCommentOpen() == cStyleCommentStarted;
        if(unchanged && allowsMultilineString)
          unchanged = compare[i].isStringOpen() == stringOpened;
      }
      final boolean cs = cStyleCommentStarted;
      final boolean so = stringOpened;
      final String sso = strStringOpened;
      lineFlags[i] = new LineFlag()
      {
        public boolean isCStyleCommentOpen()
        {
          return cs;
        }

        public boolean isStringOpen()
        {
          return so;
        }

        public String getStringOpened()
        {
          return sso;
        }
      };
      posInDocument += 1;
    }

    if(!unchanged)
    {
      EventListener[] el = this.listenerList.getListeners(DocumentListener.class);
      DocumentEvent de = new AbstractDocument.DefaultDocumentEvent(
          0, getLength(), DocumentEvent.EventType.CHANGE);
      for(int i = 0; i < el.length; i++)
      {
        ((DocumentListener) el[i]).changedUpdate(de);
      }
    }
  }

  public boolean isCStyleCommentOpen(int line)
  {
     return line >= 0 && line < lineFlags.length ? lineFlags[line].isCStyleCommentOpen() : false;
  }
  public boolean isStringOpen(int line)
  {
     return line >= 0 && line < lineFlags.length ? lineFlags[line].isStringOpen() : false;
  }
  public String getStringOpened(int line)
  {
     return line >= 0 && line < lineFlags.length ? lineFlags[line].getStringOpened() : null;
  }

  public void setSingleLineComment(String singleLineComment) {
    this.singleLineComment = singleLineComment;
  }
  public void setStringDelimiters(String stringDelimiters) {
    this.stringDelimiters = stringDelimiters;
  }
  public String getSingleLineComment() {
    return singleLineComment;
  }
  public String getStringDelimiters() {
    return stringDelimiters;
  }
  public boolean isAllowsMultilineString() {
    return allowsMultilineString;
  }
  public void setAllowsMultilineString(boolean allowsMultilineString) {
    this.allowsMultilineString = allowsMultilineString;
  }

}