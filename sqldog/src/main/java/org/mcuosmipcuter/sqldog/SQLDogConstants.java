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
package org.mcuosmipcuter.sqldog;

import java.awt.Font;
import java.awt.Insets;
import java.io.PrintStream;

public class SQLDogConstants
{
	public static final String EXECUTE			= "execute";
	public static final String STOP_QUERY		= "stop";
	public static final String COMMIT			= "commit";
	public static final String ROLLBACK			= "rollback";
	public static final String CONNECT			= "connect to";
	public static final String DISCONNECT		= "disconnect";
	public static final String AUTO_COMMIT		= "autocommit";
	public static final String AUTO_COMMIT_ON	= "autocommit on";
	public static final String AUTO_COMMIT_OFF	= "autocommit off";
	public static final String LOGIN_CONNECT	= "login window: connect now";
	public static final String EDIT				= "edit";
	public static final String SAVE				= "Save";
	public static final String REMOVE			= "remove";
	public static final String ASK_LOGIN		= "popup login";
	public static final String LOGIN			= "login:";
	public static final String URL				= "url:";
	public static final String WINROWDESC		= " rowbuffer:";
	public static final String FONT_SIZE_DESC	= "font size:";
	public static final String DISPLAY_ROWNUMS	= "display rownumbers";
	public static final String MESSAGE_SEP		= ": ";

	public static final char MNEMONIC_EXECUTE			= 'e';
	public static final char MNEMONIC_STOP_QUERY		= 's';
	public static final char MNEMONIC_COMMIT			= 'c';
	public static final char MNEMONIC_ROLLBACK			= 'r';
	public static final char MNEMONIC_CONNECT			= 'n';
	public static final char MNEMONIC_DISCONNECT		= 'd';
	public static final char MNEMONIC_AUTO_COMMIT		= 'a';
	public static final char MNEMONIC_SAVE				= 'S';
	public static final char MNEMONIC_REMOVE			= 'v';
	public static final char MNEMONIC_ASK_LOGIN			= 'p';
	public static final char MNEMONIC_DISPLAY_ROWNUMS	= 'w';

	public static final Font FONT_MEDIUM = new Font("Monospaced", Font.BOLD, 12);
	public static final Font FONT_SMALL = new Font("Monospaced", Font.BOLD, 8);
	public static final Insets INSETS_TIGHT = new Insets(0, 0, 0, 0);
	public static final String WINDOW_TITLE_NOT_CONNECTED	= "SQLDog";
	public static final String WINDOW_TITLE_CONNECTED		= "SQLDog connected to ";
	public static final String NULL_RESULT_REPRESENTATION	= "<<NIL>>";
	public static final java.awt.Color COLOR_BORDER = new java.awt.Color(0, 127, 197);
	//public static final java.awt.Color COLOR_ERROR	= new java.awt.Color(255, 127, 127);
    public static final java.awt.Color COLOR_ERROR	= new java.awt.Color(255, 207, 207);
	//public static final java.awt.Color COLOR_ALRIG	= new java.awt.Color(197, 255, 63);
    public static final java.awt.Color COLOR_ALRIG	= new java.awt.Color(217, 255, 163);
    public static final java.awt.Color COLOR_DISCONNECTED	= new java.awt.Color(255, 255, 63);

	public static final String INITIAL_WINDOW_ROWS	= "40";
	public static final String CONFIG_FILE_NAME		= "sqldog.xml";
	public static final String CONFIG_FILE_COMMENT	= "created by SQLDog: ";

	public static final String TRACE_PROPERTY_KEY		= "sqldog.trace.level";
	public static final PrintStream TRACE_PRINT_STREAM	= System.out;
	public static final int TRACE_DEFAULT_LEVEL			= 3;

	public static final String ICON_PATH	= "/org/mcuosmipcuter/sqldog/";

	public static final String[] FONT_SIZES  = new String[]{"6", "8", "10", "12", "14", "16" };

	public static final SQLDogLogin[] DEFAULT_LOGINS
		= new SQLDogLogin[]{new SQLDogLogin("My SQL",
											"com.mysql.jdbc.Driver",
											"jdbc:mysql://localhost/test",
											"",
											"",
                                            true),
							new SQLDogLogin("mckoi java db",
											"com.mckoi.JDBCDriver",
											"jdbc:mckoi://localhost/",
											"admin_user",
											"aupass00",
                                            true)};

	public static final String WARRANTY = "NO WARRANTY\n\n"

	+ "  11. BECAUSE THE PROGRAM IS LICENSED FREE OF CHARGE, THERE IS NO WARRANTY\n"
	+ "FOR THE PROGRAM, TO THE EXTENT PERMITTED BY APPLICABLE LAW.  EXCEPT WHEN\n"
	+ "OTHERWISE STATED IN WRITING THE COPYRIGHT HOLDERS AND/OR OTHER PARTIES\n"
	+ "PROVIDE THE PROGRAM \"AS IS\" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESSED\n"
	+ "OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF\n"
	+ "MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.  THE ENTIRE RISK AS\n"
	+ "TO THE QUALITY AND PERFORMANCE OF THE PROGRAM IS WITH YOU.  SHOULD THE\n"
	+ "PROGRAM PROVE DEFECTIVE, YOU ASSUME THE COST OF ALL NECESSARY SERVICING,\n"
	+ "REPAIR OR CORRECTION.\n\n"

	+ "  12. IN NO EVENT UNLESS REQUIRED BY APPLICABLE LAW OR AGREED TO IN WRITING\n"
	+ "WILL ANY COPYRIGHT HOLDER, OR ANY OTHER PARTY WHO MAY MODIFY AND/OR\n"
	+ "REDISTRIBUTE THE PROGRAM AS PERMITTED ABOVE, BE LIABLE TO YOU FOR DAMAGES,\n"
	+ "INCLUDING ANY GENERAL, SPECIAL, INCIDENTAL OR CONSEQUENTIAL DAMAGES ARISING\n"
	+ "OUT OF THE USE OR INABILITY TO USE THE PROGRAM (INCLUDING BUT NOT LIMITED\n"
	+ "TO LOSS OF DATA OR DATA BEING RENDERED INACCURATE OR LOSSES SUSTAINED BY\n"
	+ "YOU OR THIRD PARTIES OR A FAILURE OF THE PROGRAM TO OPERATE WITH ANY OTHER\n"
	+ "PROGRAMS), EVEN IF SUCH HOLDER OR OTHER PARTY HAS BEEN ADVISED OF THE\n"
	+ "POSSIBILITY OF SUCH DAMAGES.\n";

}
