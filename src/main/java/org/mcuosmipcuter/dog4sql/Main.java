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

import javax.swing.UIManager;

class Main {
	public static void main(String[] args) throws Exception
	{
        Controller.trace(3, "claspath_bef:" + System.getProperty("java.class.path"));
        UIManager.setLookAndFeel("org.mcuosmipcuter.dog4sql.editor.LFTest");

		Frame s = new Frame();
        s.setVisible(true);
        if(s.getMaximised()) {
            s.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        }

	}

}
