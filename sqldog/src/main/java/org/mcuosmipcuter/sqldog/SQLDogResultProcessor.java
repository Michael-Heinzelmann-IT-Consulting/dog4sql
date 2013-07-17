package org.mcuosmipcuter.sqldog;

import java.sql.*;

import javax.swing.table.*;
import javax.swing.event.*;

import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.datatransfer.*;

import javax.swing.*;

import org.mcuosmipcuter.sqldog.adt.*;
import org.mcuosmipcuter.sqldog.text.*;

import java.io.IOException;

class SQLDogResultProcessor
{
	SQLDogResultWrapper rw;
    SQLDogQueryListener ql;
    ColSizeListener cl;
    boolean findRuns = false;
    boolean saveRuns = false;
    boolean copyRuns = false;
    private ObjectCache cache;

	public SQLDogResultProcessor(SQLDogResultWrapper rw, SQLDogQueryListener ql, ColSizeListener cl)
	{
		this.rw = rw;
        this.ql = ql;
        this.cl = cl;
        cache = new ObjectCache(1000);
	}

    public void stopSave()
    {
        SQLDogController.trace(5, "stopping save");
        saveRuns = false;
    }

    public void stopCopyAll()
    {
        SQLDogController.trace(5, "stopping select all");
        copyRuns = false;
    }

    public void stopFind()
    {
        SQLDogController.trace(5, "stopping find");
        findRuns = false;
    }

    public void find(String what, java.awt.Point start, boolean regex, boolean matchCase,
                     FindListener findListener)
    {
        if(findRuns || saveRuns || copyRuns)
        {
            return;
        }
        Finder finder = new Finder(what, start, regex, matchCase, findListener);
        finder.start();
    }

    public void saveCsv(SaveListener saveListener, String seperator, File f)
    {
        if(saveRuns || findRuns || copyRuns){
            return;
        }
        CsvSaver saver = new CsvSaver(saveListener, seperator, f);
        saver.start();
    }

    public void copyAll(SaveListener saveListener, Component windowOwner )
    {
        if(copyRuns || findRuns || saveRuns){
            return;
        }
        AllSelector selector = new AllSelector(saveListener, windowOwner);
        selector.start();
    }

    public interface ColSizeListener
    {
        public abstract void newMaxSize(int colIndex, int maxSize);
    }

    public interface ProgressListener
    {
        public abstract void onProgress(float percentOfRows);
    }

    public interface FindListener extends ProgressListener
    {
        public abstract void onFindResult(java.awt.Point foundAt);
    }

    public interface SaveListener extends ProgressListener
    {
        public abstract void saveFinished();
    }

    public class CsvSaver extends Thread
    {
        SaveListener saveListener;
        String seperator;
        File f;

        public CsvSaver(SaveListener saveListener, String seperator, File f)
        {
            this.saveListener = saveListener;
            this.seperator = seperator;
            this.f = f;
        }

        public void run()
        {
            saveCsv();
        }

        public void saveCsv()
        {
            saveRuns = true;
            FileOutputStream fos = null;
            try
            {
                fos = new FileOutputStream(f);

                for (int c = 1; c <= rw.getCols(); c++)
                {
                    String colName = rw.getResultSet().getMetaData().getColumnName(c);
                    if (colName != null)
                        fos.write(colName.getBytes());
                    fos.write(seperator.getBytes());
                }
                fos.write("\n".getBytes());
                rw.getResultSet().beforeFirst();
                int progressCents = 0;
                int r = 0;
                while (saveRuns && rw.getResultSet().next())
                {
                    r++;
                    float perc =(float)r / (float)rw.getRows() * 100F;
                    if((int)perc > progressCents)
                    {
                        progressCents++;
                        saveListener.onProgress(perc);
                    }
                    for (int c = 1; c <= rw.getCols(); c++)
                    {
                        Object o = rw.getResultSet().getObject(c);
                        String v = o != null ? columnValueToString(o) : "";
                        fos.write(v.getBytes());
                        fos.write(seperator.getBytes());
                    }
                    fos.write("\n".getBytes());
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
            finally
            {
                if (fos != null)
                {
                    try
                    {
                        fos.close();
                    }
                    catch (IOException ex1){}
                }
                saveRuns = false;
                saveListener.saveFinished();
            }
        }
    }

    public class Finder extends Thread
    {
        String what;
        java.awt.Point start;
        FindListener findListener;
		boolean regex;
		boolean matchCase;

        public Finder(String what, java.awt.Point start, boolean regex, boolean matchCase, FindListener findListener)
        {
            this.what = what;
            this.start = start;
            this.findListener = findListener;
			this.regex = regex;
			this.matchCase = matchCase;
        }

        public void run()
        {
            findRuns = true;
            SQLDogController.trace(4, this + " starting");
            System.out.println();
            findInResult();
            SQLDogController.trace(4, this + " returning");
            findRuns = false;
        }

        public void findInResult()
        {
            SQLDogController.trace(4, "finding '" + what + "' from " + start);
            if(what != null && start != null && !what.equals("") && rw != null && rw.getResultSet() != null)
            {
                try
                {
                    int colStart = start.x;
                    int progressCents = 0;
                    for(int r = start.y; r <= rw.getRows(); r++)
                    {
                        float perc =(float)r / (float)rw.getRows() * 100F;
                        if((int)perc > progressCents)
                        {
                            progressCents++;
                            findListener.onProgress(perc);
                        }
                        rw.getResultSet().absolute(r);
                        for (int c = colStart; c <= rw.getCols(); c++)
                        {
                            if(!findRuns)
                            {
                                findListener.onFindResult(new java.awt.Point(0, 1));
                                return;
                            }
                            Object o = rw.getResultSet().getObject(c);
							if(o != null)
							{
								String value = SQLDogResultProcessor.columnValueToString(o);
								boolean simpleMatch = false;
								if(!regex)
									if(!matchCase)
										simpleMatch = value.toLowerCase().indexOf(what.toLowerCase()) > -1;
									else
										simpleMatch = value.indexOf(what) > -1;
								boolean matches = regex ? value.matches(what) : simpleMatch;
								if(matches)
								{
									findRuns = false;
									findListener.onFindResult(new Point(c, r));
									return;
								}
							}
                        }
                        colStart = 1;
                    }

                }
                catch (Exception ex)
                {
                    findRuns = false;
                    ex.printStackTrace();
                    findListener.onFindResult(new java.awt.Point(0, 1));
                    return;
                }
            }
            findRuns = false;
            findListener.onFindResult(new java.awt.Point(0, 1));
            return;
        }

    }

    protected class AllSelector extends Thread
    {
        SaveListener saveListener;
        Component windowOwner;
        protected AllSelector(SaveListener saveListener, Component windowOwner)
        {
            this.saveListener = saveListener;
            this.windowOwner = windowOwner;
        }
        public void run()
        {
            copyAll();
        }

        public void copyAll()
        {
            copyRuns = true;
            StringBuffer buf = new StringBuffer();
            Runtime rt = Runtime.getRuntime();
            long initFree = rt.freeMemory();
            SQLDogController.trace(5, "initial free memory:" + initFree);
            try
            {
                buf.append("\n");
                rw.getResultSet().beforeFirst();
                int progressCents = 0;
                int r = 0;
                while (copyRuns && rw.getResultSet().next())
                {
                    r++;
                    float perc =(float)r / (float)rw.getRows() * 100F;
                    if((int)perc > progressCents)
                    {
                        progressCents++;
                        saveListener.onProgress(perc);
                    }
                    for (int c = 1; c <= rw.getCols(); c++)
                    {
                        Object o = rw.getResultSet().getObject(c);
                        String v = o != null ? columnValueToString(o) : "";
                        buf.append(v);
                        buf.append("\t");
                    }
                    buf.append("\n");
                    //SQLDogController.trace(5, "free memory diff:" + (initFree - rt.freeMemory()));
                    //if (buf.length() > initFree)
                    if (initFree - rt.freeMemory() < 0 && r > 99)// at least 100 rows should be selectable
                    {
                        JOptionPane.showMessageDialog(windowOwner,
                            "only " + r + " rows of " + rw.getRows() +
                            " copied!\nuse 'save as CSV' to retrieve the complete resulset!",
                            "not enough clipboard memory",
                            JOptionPane.ERROR_MESSAGE);
                        break;
                    }

                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
            finally
            {
                try
                {
                    StringSelection s = new StringSelection(buf.toString());
                    Toolkit.getDefaultToolkit().getSystemClipboard().
                        setContents(s, s);
                }
                catch (java.lang.OutOfMemoryError err)
                {
                    err.printStackTrace();
                }

                copyRuns = false;
                saveListener.saveFinished();
                SQLDogController.trace(5, "select all finished.");
            }
        }

    }

    public static String columnValueToString(Object o)
    throws SQLException, IOException
    {
        if(o == null)
            return null;
        String colVal = null;
        if (o instanceof byte[])
        {
            colVal = new String( (byte[]) o);
        }
        else
        {
            if (o instanceof java.sql.Clob)
            {
                java.sql.Clob clob = (java.sql.Clob) o;
                char[] carr = new char[ (int) clob.length()];
                clob.getCharacterStream().read(carr);
                colVal = new String(carr);
            }
            else
            {
                colVal = o.toString();
            }
        }

        return colVal;
    }

    public class TableModel extends AbstractTableModel
    {
        public class CacheWrapper
        {
            String value;
            protected boolean isLob = false;
            protected boolean isExpanded = false;
            protected CacheWrapper(String v)
            {
                value = v;
            }
            protected CacheWrapper(String v, boolean isLob, boolean isExpanded)
            {
                value = v;
                this.isLob = isLob;
                this.isExpanded = isExpanded;
            }
            protected CacheWrapper(Integer i)
            {
                value = i == null ? null : i.toString();
            }
            public String toString()
            {
                return value;
            }
        }
       private ResultSet myRes = null;
       private Connection connection;
       private SQLDogResultWrapper wrapper = null;
       private TableModelListener tableModelListener;
       private boolean gotSQLException = false;
       private boolean moreThanOneTable = false;
       private int[] colSizes;
       private boolean expandLobs = false;

       public void setExpandLobs(boolean expand)
       {
           expandLobs = expand;
       }

       public TableModel(SQLDogResultWrapper rw)
       {
         myRes = rw.getResultSet();
         wrapper = rw;
        try
        {
            if(myRes != null)// dml commands
            {
                int cols = myRes.getMetaData().getColumnCount();
                colSizes = new int[cols + 1];
                String tableName = null;
                for (int i = 1; i <= cols; i++)
                {
                    String currTn = myRes.getMetaData().getTableName(i);

                    if (tableName != null && !tableName.equals(currTn))
                    {
                        moreThanOneTable = true;
                        break;
                    }
                    tableName = currTn;
                }
            }
        }
        catch (SQLException ex)
        {
        }
       }

       public String getColumnName(int columnIndex)
       {
         //System.out.println("getColumnName:"+columnIndex);
         String ret = "";
         if(columnIndex > 0)
         {
             try
             {
                 String prefFix = moreThanOneTable ? myRes.getMetaData().getTableName(columnIndex) + "." : "";
                 ret = prefFix + myRes.getMetaData().getColumnName(columnIndex);
             }
             catch (Exception ex)
             {
                 ex.printStackTrace();
             }
         }
         //System.out.println("getColumnName:"+ret);
         //String r = rw.headerArray[columnIndex] > 0 ? Pad.fastPad(ret, '=',rw.headerArray[columnIndex]) : ret;
         return ret;
       }

       public int getColumnCount()
       {
           if(myRes == null)
                 return 0;
           int c = 0;
           try
           {
             c = myRes.getMetaData().getColumnCount() + 1;
           }
           catch(SQLException ex)
           {
             ex.printStackTrace();
           }
           //System.out.println("getColumnCount:"+c+"rw.cols:"+wrapper.cols);
           return c;
         }

         public int getRowCount()
         {
             if(myRes == null)
                 return 0;
             return wrapper.getRows();
         }

         private int invokations = 0;
         private int inCache = 0;

         public Object getValueAt(int row, int col)
         {
           Object o = null;
           String ret = null;
           if(findRuns || saveRuns || copyRuns)
           {
               return cache.getValue(new java.awt.Point(col, row));
           }
           o = cache.getValue(new java.awt.Point(col, row));
           invokations++;
           if(invokations == 100)
           {
               SQLDogController.trace(5, "cache success factor:" + inCache + "%");
               invokations = 0;
               inCache = 0;
           }
           if(o != null)
           {
               CacheWrapper cw = (CacheWrapper)o;
               //System.out.println("in cache +++++" + row + "col:" + col);
               if(!(cw.isLob && cw.isExpanded != expandLobs))
               {
                   if (cw.value != null && cw.value.length() > colSizes[col])
                   {
                       colSizes[col] = cw.value.length();
                       cl.newMaxSize(col, colSizes[col]);
                   }
                   inCache++;
                   return o;
               }
           }
           //System.out.println("no cache -----" + row + "col:" + col);
           if(gotSQLException || col > rw.getCols() || row > rw.getRows() || myRes == null)
               return o;
           if(col == 0)
           {
               cache.putKeyValuePair(new java.awt.Point(col, row), new CacheWrapper(new Integer(row + 1)));
               return new Integer(row + 1);
           }
           try
           {
             myRes.absolute(row + 1);

             if(col > 0 && col <= wrapper.getCols())
             {
                 o = myRes.getObject(col);
             }
             if(expandLobs)
             {
                 if (o instanceof byte[])
                 {
                     if ( ( (byte[]) o).length > colSizes[col])
                     {
                         colSizes[col] = ( (byte[]) o).length;
                         cl.newMaxSize(col, colSizes[col]);
                     }
                     CacheWrapper cw = new CacheWrapper(new String( (byte[]) o).replace( (char) 10,
                         (char) 0), true, true);
                     cache.putKeyValuePair(new java.awt.Point(col, row), cw);
                     return cw;
                 }
                 else if (o instanceof java.sql.Clob)
                 {
                     java.sql.Clob clob = (java.sql.Clob) o;
                     if (clob.length() > colSizes[col])
                     {
                         colSizes[col] = (int) clob.length();
                         cl.newMaxSize(col, colSizes[col]);
                     }

                     char[] carr = new char[ (int) clob.length()];
                     try
                     {
                         clob.getCharacterStream().read(carr);
                     }
                     catch (java.io.IOException ioex)
                     {}
                     CacheWrapper cw = new CacheWrapper((new String(carr)).replace( (char) 10, (char) 0), true, true);
                     cache.putKeyValuePair(new java.awt.Point(col, row), cw);
                     return cw;
                 }
             }// end if expandLobs

             if (o != null)
             {
                 ret = o.toString();
                 if (ret.length() > colSizes[col])
                 {
                     colSizes[col] = ret.length();
                     cl.newMaxSize(col, colSizes[col]);
                 }
             }
           }
           catch(SQLException ex)
           {
               gotSQLException = true;
               ql.onQueryException(ex);
               ex.printStackTrace();
               return null;
           }
           catch(Exception e)
           {
               e.printStackTrace();
               return null;
           }
           if(o instanceof byte[] || o instanceof java.sql.Clob)
           {
               cache.putKeyValuePair(new java.awt.Point(col, row),
                                     new CacheWrapper(ret, true, false));
           }
           else
           {
               cache.putKeyValuePair(new java.awt.Point(col, row),
                                     new CacheWrapper(ret));
           }
           return ret;
         }

         public String toString()
         {
             String ret = "";
             ret += wrapper.getDbUrl();
             ret += "\nstart execution: ";
             ret += new java.util.Date(wrapper.getExecutionStart());
             ret += "\n end execution: ";
             ret += new java.util.Date(wrapper.getExecutionEnd());
             ret += "\nSQL statement: \n\n";
             ret += wrapper.getSqlString();
             ret += "\n ";

             return ret;
         }

         public void resetColSizes()
         {
             int c = colSizes.length;
             colSizes = null;
             colSizes = new int[c];
         }

     }

     public TableModel createModel()
     {
         return new TableModel(rw);
     }

}
