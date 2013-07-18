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

import java.awt.Color;
import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.event.DocumentEvent;
import javax.swing.text.JTextComponent;
import javax.swing.text.TabExpander;

public class SyntaxPainter
{
  public final static String ESCAPE = "\\";

  protected String delimiters = "\t " + ESCAPE;
  protected String singleLineCommentStart = "";
  protected String stringDelimiters = "'\"";
  protected String decimalSeperator = ".";

  protected ResourceBundle reservedWords;
  protected String[] keyWords1 = new String[0];
  protected String[] keyWords2 = new String[0];
  protected Vector myMessageListeners = new Vector();
  protected Vector myChangeListeners = new Vector();

  protected boolean caseSensitiveKeyWords1;
  protected boolean caseSensitiveKeyWords2;
  protected boolean supportsCStyleComments;
  protected boolean allowsMultilineString;

  protected Color colorStrings = new Color(165, 145, 55);
  protected Color colorComment = new Color(63, 166, 63);
  protected Color colorKeyWord1 = Color.blue;
  protected Color colorKeyWord2 = new Color(235, 85, 25);
  protected Color colorDelimiters = Color.red;
  protected Color colorNumbers = Color.magenta;

  Vector drillDownListeners = new Vector();
  public interface DrillDownInitListener
  {
    public void tryDrillDown(String token, int position);
  }
  public void addDrillDownInitListener(DrillDownInitListener ddl)
  {
    this.drillDownListeners.add(ddl);
  }
  public void removeDrillDownInitListener(DrillDownInitListener ddl)
  {
    this.drillDownListeners.remove(ddl);
  }

  protected class Token
  {
    protected String string;
    protected boolean number;
    protected int pos;

    protected void setAll(Token token)
    {
      string = token.string;
      number = token.number;
      pos = token.pos;
    }

  }

  public SyntaxPainter() {
  }

  public SyntaxPainter(ResourceBundle resBun)
  {
    this();
    setReservedWords(resBun);
  }

  public void setKeyWords1(Vector kw1)
  {
    if(kw1 == null)
      return;
    Object old = this.keyWords1;
    keyWords1 = new String[kw1.size()];
    for(int i = 0; i < kw1.size(); i++)
    {
      keyWords1[i] = kw1.elementAt(i).toString();
    }
    this.notifyChange("keyWords1", old, this.keyWords1);
  }

  public void setKeyWords2(Vector kw2)
  {
    if(kw2 == null)
      return;
    Object old = this.keyWords2;
    keyWords2 = new String[kw2.size()];
    for(int i = 0; i < kw2.size(); i++)
    {
      keyWords2[i] = kw2.elementAt(i).toString();
    }
    this.notifyChange("keyWords2", old, this.keyWords2);
  }

  public void addPropertyChangeListener(PropertyChangeListener aListener)
  {
    if(!myChangeListeners.contains(aListener))
      myChangeListeners.add(aListener);
  }

  public void removePropertyChangeListener(PropertyChangeListener aListener)
  {
    myChangeListeners.remove(aListener);
  }

  public int paintLine(JTextComponent tcomp,
                       int tabSize,
                       Graphics g,
                       String text,
                       TabExpander tabex,
                       int x,
                       int y,
                       boolean cStyleCommentStarted,
                       boolean stringOpened,
                       String strStringOpened,
                       DocumentEvent e,
                       int p0)
  {
    String allDelimiters
        = supportsCStyleComments ? delimiters + " */\n" : delimiters;
    java.util.StringTokenizer strtok = new java.util.StringTokenizer(text,
        allDelimiters + stringDelimiters, true);

    int pos = x;
    int posInLine = 0;
    int cStyleCommentStartChar = 0;
    if(!allowsMultilineString)
    {
      stringOpened = false;
      strStringOpened = null;
    }
    boolean singleLineCommentStarted = false;
    Token oldToken = new Token();
    Token oldOldToken = new Token();

    while(strtok.hasMoreElements())
    {
      Token token = new Token();
      token.pos = pos;
      token.string = strtok.nextToken();
      posInLine += token.string.length();

      if(token.string.equals("\t"))
      {
        pos = (int)tabex.nextTabStop(pos, tabSize);
      }

      boolean keyWord1 = false;
      for(int i = 0; i < keyWords1.length; i++)
      {
        boolean equal = caseSensitiveKeyWords1 ? token.string.equals(keyWords1[i])
            : token.string.equalsIgnoreCase(keyWords1[i]);
        if(equal)
        {
          keyWord1 = true;
          break;
        }
      }

      boolean keyWord2 = false;
      if(!keyWord1 && keyWords2 != null)
      {
        for(int i = 0; i < keyWords2.length; i++)
        {
          boolean equal = caseSensitiveKeyWords2 ? token.string.equals(keyWords2[i])
              : token.string.equalsIgnoreCase(keyWords2[i]);
          if(equal)
          {
            keyWord2 = true;
            break;
          }
        }
      }

      boolean number = true;
      for(int i = 0; i < token.string.length(); i++)
      {
        if(token.string.charAt(i) < '0' || token.string.charAt(i) > '9')
        {
          number = false;
          break;
        }
      }

      boolean closeString = false;
      if(token.string.length() == 1
         && stringDelimiters.indexOf(token.string) > -1
         && !singleLineCommentStarted
         && !cStyleCommentStarted)
      {
        if(!stringOpened)
        {
          stringOpened = true;
          strStringOpened = token.string;
        }
        else if(strStringOpened.equals(token.string) && !ESCAPE.equals(oldToken.string))
        {
          closeString = true;
        }
      }

      if(!stringOpened
         && singleLineCommentStart.equals(token.string)
         && singleLineCommentStart.equals(oldToken.string)
         && !(supportsCStyleComments && "/".equals(oldToken.string) && "*".equals(oldOldToken.string)))
      {
        singleLineCommentStarted = true;
        g.setColor(colorComment);
        g.drawString(singleLineCommentStart, oldToken.pos, y);
                     //pos - g.getFontMetrics().stringWidth(singleLineCommentStart), y);
      }

      boolean unsetComments = false;
      if(supportsCStyleComments)
      {
        if(!stringOpened
           && !singleLineCommentStarted
           && "*".equals(token.string)
           && "/".equals(oldToken.string)
           && !("*".equals(oldOldToken.string)))
        {
          cStyleCommentStarted = true;
          cStyleCommentStartChar = posInLine - 2;
          // redraw
          g.setColor(colorComment);
          g.drawString(oldToken.string, oldToken.pos, y); // pos - g.getFontMetrics().stringWidth(oldToken)
        }

        unsetComments = (cStyleCommentStarted
                         && "/".equals(token.string)
                         && "*".equals(oldToken.string)
                         && !("/".equals(oldOldToken.string)
                         && ((posInLine - cStyleCommentStartChar) == 3)));

        if(cStyleCommentStarted)
        {
          singleLineCommentStarted = true;
          g.setColor(colorComment);
        }
      }
      boolean delimiter = delimiters.indexOf(token.string, 1) > -1;
      if(stringOpened)
        g.setColor(colorStrings);
      if(!stringOpened && !singleLineCommentStarted)
      {
        if(!cStyleCommentStarted)
        {
          if(((number || "\n".equals(token.string))
             && decimalSeperator.equals(oldToken.string)
             && oldOldToken.number))
          {
            g.setColor(colorNumbers);
            g.drawString(decimalSeperator, oldToken.pos, y);
          }
          if(decimalSeperator.equals(token.string)
             && oldToken.number
             && !strtok.hasMoreElements())
          {
            number = true;
            delimiter = false;
          }

        }
        if(keyWord1)
        {
          g.setColor(colorKeyWord1);
        }
        else if(keyWord2)
        {
          g.setColor(colorKeyWord2);
        }
        else if(delimiter)
        {
          g.setColor(colorDelimiters);
        }
        else if(number)
        {
          g.setColor(colorNumbers);
        }
        else
        {
          g.setColor(tcomp.getForeground());
        }
      }

      g.drawString(token.string, pos, y);
      ////
      if(e != null
         && ".".equals(token.string)
         && !stringOpened
         && !singleLineCommentStarted
         && !cStyleCommentStarted
         && e.getType() == DocumentEvent.EventType.INSERT)
      {
        System.out.println("drill >>>>>>>>>>>>>>>>>" + oldToken.string + "<<<<<<<<<<<<<<<<" +
                           (p0+posInLine));
        Enumeration enumeration = this.drillDownListeners.elements();
        while(enumeration.hasMoreElements())
        {
          ((DrillDownInitListener) enumeration.nextElement()).tryDrillDown(oldToken.
              string,
              p0+posInLine);
        }
      }

      ////
      pos += g.getFontMetrics().stringWidth(token.string);
      token.number = number;
      oldOldToken = oldToken;
      oldToken = token;
      if(closeString)
      {
        stringOpened = false;
      }

      if(unsetComments)
      {
        cStyleCommentStarted = false;
        singleLineCommentStarted = false;
      }

    } // end while strtok

    return pos;

  }

  protected void notifyChange(String propertyName, Object oldValue, Object newValue)
  {
    PropertyChangeEvent pce = new PropertyChangeEvent(this, propertyName, oldValue, newValue);
    Enumeration enumeration = myChangeListeners.elements();
    while(enumeration.hasMoreElements())
    {
      ((PropertyChangeListener)enumeration.nextElement()).propertyChange(pce);
    }
  }

  ///// get / set /////

  public boolean isCaseSensitiveKeyWords1() {
    return caseSensitiveKeyWords1;
  }
  public void setCaseSensitiveKeyWords1(boolean caseSensitiveKeyWords1) {
    Boolean old = new Boolean(this.caseSensitiveKeyWords1);
    this.caseSensitiveKeyWords1 = caseSensitiveKeyWords1;
    this.notifyChange("caseSensitiveKeyWords1", old, new Boolean(caseSensitiveKeyWords1));
  }
  public boolean isCaseSensitiveKeyWords2() {
    return caseSensitiveKeyWords2;
  }
  public void setCaseSensitiveKeyWords2(boolean caseSensitiveKeyWords2) {
    Boolean old = new Boolean(this.caseSensitiveKeyWords2);
    this.caseSensitiveKeyWords2 = caseSensitiveKeyWords2;
    this.notifyChange("caseSensitiveKeyWords2", old, new Boolean(caseSensitiveKeyWords2));
  }
  public String getStringDelimiters() {
    return stringDelimiters;
  }
  public void setStringDelimiters(String stringDelimiters) {
    String old = this.stringDelimiters;
    this.stringDelimiters = stringDelimiters;
    this.notifyChange("stringDelimiters", old, stringDelimiters);
  }
  public Color getColorComment() {
    return colorComment;
  }
  public void setColorComment(Color colorComment) {
    Color old = this.colorComment;
    this.colorComment = colorComment;
    this.notifyChange("colorComment", old, colorComment);
  }
  public Color getColorDelimiters() {
    return colorDelimiters;
  }
  public void setColorDelimiters(Color colorDelimiters) {
    Color old = this.colorDelimiters;
    this.colorDelimiters = colorDelimiters;
    this.notifyChange("colorDelimiters", old, colorDelimiters);
  }
  public Color getColorKeyWord1() {
    return colorKeyWord1;
  }
  public void setColorKeyWord1(Color colorKeyWord1) {
    Color old = this.colorKeyWord1;
    this.colorKeyWord1 = colorKeyWord1;
    this.notifyChange("colorKeyWord1", old, colorKeyWord1);
  }
  public Color getColorKeyWord2() {
    return colorKeyWord2;
  }
  public void setColorKeyWord2(Color colorKeyWord2) {
    Color old = this.colorKeyWord2;
    this.colorKeyWord2 = colorKeyWord2;
    this.notifyChange("colorKeyWord2", old, colorKeyWord2);
  }
  public Color getColorNumbers() {
    return colorNumbers;
  }
  public void setColorNumbers(Color colorNumbers) {
    Color old = this.colorNumbers;
    this.colorNumbers = colorNumbers;
    this.notifyChange("colorNumbers", old, colorNumbers);
  }
  public Color getColorStrings() {
    return colorStrings;
  }
  public void setColorStrings(Color colorStrings) {
    Color old = this.colorStrings;
    this.colorStrings = colorStrings;
    this.notifyChange("colorStrings", old, colorStrings);
  }
  public ResourceBundle getReservedWords() {
    return reservedWords;
  }
  public void setReservedWords(ResourceBundle reservedWords) {
    ResourceBundle old = this.reservedWords;
    this.reservedWords = reservedWords;
    Enumeration enumeration = reservedWords.getKeys();
    Vector vKeyWords1 = new Vector();
    Vector vKeyWords2 = new Vector();
    while(enumeration.hasMoreElements())
    {
      Object o = enumeration.nextElement();
      if(reservedWords.getString(o.toString()).equals("keyWord1"))
        vKeyWords1.add(o);
      if(reservedWords.getString(o.toString()).equals("keyWord2"))
        vKeyWords2.add(o);
    }
    keyWords1 = new String[vKeyWords1.size()];
    vKeyWords1.copyInto(keyWords1);
    keyWords2 = new String[vKeyWords2.size()];
    vKeyWords2.copyInto(keyWords2);

    delimiters += reservedWords.getString("syntax.delimiters");
    setSingleLineCommentStart(reservedWords.getString("syntax.comment.singleline.start"));

    this.notifyChange("reservedWords", old, reservedWords);
  }
  public boolean isSupportsCStyleComments() {
    return supportsCStyleComments;
  }
  public void setSupportsCStyleComments(boolean supportsCStyleComments) {
    Boolean old = new Boolean(this.supportsCStyleComments);
    this.supportsCStyleComments = supportsCStyleComments;
    this.notifyChange("supportsCStyleComments", old, new Boolean(supportsCStyleComments));
  }
  public String getSingleLineCommentStart() {
    return singleLineCommentStart;
  }
  public void setSingleLineCommentStart(String singleLineCommentStart) {
    if(singleLineCommentStart.length() > 1)
      throw new IllegalArgumentException("length must be 1");
    String old = this.singleLineCommentStart;
    this.singleLineCommentStart = singleLineCommentStart;
    this.notifyChange("singleLineCommentStart", old, singleLineCommentStart);
  }
  public boolean isAllowsMultilineString() {
    return allowsMultilineString;
  }
  public void setAllowsMultilineString(boolean allowsMultilineString) {
    Boolean old = new Boolean(this.allowsMultilineString);
    this.allowsMultilineString = allowsMultilineString;
    this.notifyChange("allowsMultilineString", old, new Boolean(allowsMultilineString));
  }
  public String getDecimalSeperator() {
    return decimalSeperator;
  }
  public void setDecimalSeperator(String decimalSeperator) {
    if(decimalSeperator.length() > 1)
      throw new IllegalArgumentException("length must be 1");
    String old = this.decimalSeperator;
    this.decimalSeperator = decimalSeperator;
    this.notifyChange("decimalSeperator", old, decimalSeperator);
  }

}