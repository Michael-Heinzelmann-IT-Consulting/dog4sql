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
package org.mcuosmipcuter.sqldog.system;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Vector;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class RuntimeClassLoader
    extends ClassLoader
{
    protected class BiasableVector
        extends Vector
    {
        protected void setBias(Object preferred)
        {
            if (this.contains(preferred) && this.size() > 1)
            {
                this.remove(preferred);
                Object tempObj = preferred;
                for (int i = 0; i < this.size(); i++)
                {
                    Object holdObj = this.elementAt(i);
                    this.setElementAt(tempObj, i);
                    tempObj = holdObj;
                }
                this.add(tempObj);
            }
        }
    }

    private BiasableVector jarFiles = new BiasableVector();
    private Vector explDirs = new Vector();
    private static final String FS = System.getProperty("file.separator");

    public RuntimeClassLoader()
    {
    }

    public RuntimeClassLoader(ClassLoader classLoader)
    {
        super(classLoader);
    }

    public void setJarFiles(String[] fileNames)
    {
        for (int i = 0; i < fileNames.length; i++)
        {
            try
            {
                jarFiles.add(new JarFile(fileNames[i]));
            }
            catch (IOException ex)
            {
            }
        }
    }

    public boolean addJarFile(String fileName)
    {
        boolean ret = true;
        try
        {
            jarFiles.add(new JarFile(fileName));
        }
        catch (IOException ex)
        {
            ret = false;
        }
        return ret;
    }

    public void setExplDirs(String[] dirNames)
    {
        for (int i = 0; i < dirNames.length; i++)
        {
            explDirs.add(dirNames[i]);
        }
    }

    public void addExplDir(String dirName)
    {
        explDirs.add(dirName);
    }

    protected Class findClass(String name)
        throws java.lang.ClassNotFoundException
    {
        Class returnClass = null;
        try
        {
            //System.out.println("findClass ----->  :" + name);
            returnClass = this.getClass().getClassLoader().loadClass(name);
            //System.out.println("00000> class found:" + name);
        }
        catch (ClassNotFoundException ex)
        {
            String pathName = name.replace('.', '/') + ".class";

            // jar file
            InputStream is = null;
            try
            {
                JarFile jar = null;
                ZipEntry ze = null;

                Enumeration enumeration = jarFiles.elements();
                boolean firstElement = true;
                while (enumeration.hasMoreElements() && ze == null)
                {
                    jar = (JarFile) enumeration.nextElement();
                    //System.out.println("searching for " + pathName + " in " + jar.getName());
                    ze = jar.getEntry(pathName);
                    if (ze != null && !firstElement)
                    {
                        //System.out.println("setting bias for:" + jar.getName());
                        jarFiles.setBias(jar);
                    }
                    firstElement = false;
                }
                if (ze != null)
                {
                    System.out.println("found [" + name + "] in [" + jar.getName() + "]");
                    is = jar.getInputStream(ze);
                    byte[] jbarr = new byte[ (int) ze.getSize()];

                    for (int i = 0; i < jbarr.length; i++)
                    {
                        jbarr[i] = (byte) is.read();
                    }
                    String xx = new String(jbarr);
                    returnClass = this.defineClass(name, jbarr, 0, jbarr.length);
                    //System.out.println("jjjjar> class loaded:" + name);
                }
            }
            catch (IOException ex2)
            {
            }
            finally
            {
                if (is != null)
                {
                    try
                    {
                        is.close();
                    }
                    catch (IOException ign)
                    {}
                }
            }
            if (returnClass != null)
            {
                return returnClass;
            }

            // exploded
            String path = null;
            File f = null;
            Enumeration enumeration = explDirs.elements();
            while (enumeration.hasMoreElements() && (f == null || !f.exists()))
            {
                path = (String) enumeration.nextElement();
                if (!path.endsWith(FS))
                {
                    path += FS;
                }
                f = new File(path + pathName);
            }

            if (f != null && f.exists())
            {
                byte[] barr = new byte[ (int) f.length()];
                FileInputStream fis = null;
                try
                {
                    fis = new FileInputStream(f);
                    fis.read(barr);
                    returnClass = this.defineClass(name, barr, 0, barr.length);
                }
                catch (IOException ex1)
                {
                    System.out.println(ex1.getMessage());
                }
                finally
                {
                    try
                    {
                        if (fis != null)
                        {
                            fis.close();
                        }
                    }
                    catch (IOException ign)
                    {}

                    f = null;
                }
            }

            if (returnClass == null)
            {
                throw new ClassNotFoundException(name);
            }

        }
        return returnClass;
    }

  protected URL findResource(String name)
  {
    System.out.println("======================= findResource =======================" + name);
    URL ret = super.findResource(name);
    return ret;
  }
  public InputStream getResourceAsStream(String name)
  {
    System.out.println("getResourceAsStream >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    InputStream ret = super.getResourceAsStream(name);
    if(ret == null)
    {
        JarFile jar = null;
        ZipEntry ze = null;
        Enumeration enumeration = jarFiles.elements();
        while (enumeration.hasMoreElements() && ze == null)
        {
            jar = (JarFile) enumeration.nextElement();
            //System.out.println("searching for " + pathName + " in " + jar.getName());
            ze = jar.getEntry(name);
            if (ze != null)
            {
                System.out.println("found:" + name + " in " + jar.getName());
                try
                {
                    ret = jar.getInputStream(ze);
                    System.out.println("ret:" + ret);
                }
                catch (IOException ex)
                {
                    ex.printStackTrace();
                }
            }
        }

    }
    return ret;

  }



}
