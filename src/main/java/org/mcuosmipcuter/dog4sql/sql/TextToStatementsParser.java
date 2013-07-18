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
package org.mcuosmipcuter.dog4sql.sql;

import java.util.ArrayList;
import java.util.List;


/**
 * Class for parsing text to SQL statements
 * @author Michael Heinzelmann
 */
public class TextToStatementsParser {
	
	
	/**
	 * Parses any text to SQL statements, no validation is performed
	 * @param input any text including null
	 * @return an array of statements, never null
	 */
	public static String[] getSqlStatementsFrom(final String input) {
		if(input == null) {
			return new String[0];
		}
		List<String> reslultList = new ArrayList<String>();
		boolean singleLineCommentInProgress = false;
		boolean cStyleInProgress = false;
		char prev = ' ';
		StringBuilder statement = new StringBuilder();
		
		for(char curr : input.toCharArray()) {
			if(curr == '-' && prev == '-') {
				singleLineCommentInProgress = true;
			}
			if(singleLineCommentInProgress && curr == '\n') {
				singleLineCommentInProgress = false;
				statement = new StringBuilder();
				continue;
			}
			if(prev == '/' && curr == '*') {
				cStyleInProgress = true;
			}
			if(cStyleInProgress && prev == '*' && curr == '/' ) {
				cStyleInProgress = false;
			}
			statement.append(curr);
			if(curr == ';') {
				if(!singleLineCommentInProgress && !cStyleInProgress) {
					reslultList.add(statement.toString());
					statement = new StringBuilder();
				}
			}
			prev = curr;
		}
		
		return reslultList.toArray(new String[0]);
	}
}
