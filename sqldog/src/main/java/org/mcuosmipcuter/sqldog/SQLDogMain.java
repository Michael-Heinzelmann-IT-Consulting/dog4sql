package org.mcuosmipcuter.sqldog;

import javax.swing.UIManager;

class SQLDogMain
{
	public static void main(String[] args)
	{
        try {
            System.out.println("claspath_bef:" + System.getProperty("java.class.path"));
            //System.load("C:\\bea\\weblogic81\\common\\eval\\pointbase\\lib\\pbclient44.jar");

            //C:\bea\weblogic81\common\eval\pointbase\lib\pbclient44.jar
            //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
            UIManager.setLookAndFeel("org.mcuosmipcuter.sqldog.editor.LFTest");
            //UIManager.setLookAndFeel("com.incors.plaf.kunststoff.KunststoffLookAndFeel");
        }
        catch(Exception e) {
        }
		SQLDogFrame s = new SQLDogFrame();
        s.setVisible(true);
        if(s.getMaximised())
            s.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);

	}

}
