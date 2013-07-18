package org.mcuosmipcuter.dog4sql.sql;

import junit.framework.TestCase;

/**
 * unit test for the parser
 * @author Michael Heinzelmann
 */
public class TestTextToStatementsParser extends TestCase{
	
	private void genericTestAndAssertion(final String input, String ...expectedStatemens) {
		String[] result = TextToStatementsParser.getSqlStatementsFrom(input);
		assertNotNull(result);
		assertEquals(expectedStatemens.length, result.length);
		int i = 0;
		for(String s :  expectedStatemens) {
			assertEquals(s, result[i++]);
		}
		
	}
	
	public void testNull() {
		genericTestAndAssertion(null);
	}
	public void testNoStatement() {
		genericTestAndAssertion("");
		genericTestAndAssertion("select * from table");
		genericTestAndAssertion("/*qwertysfagsdb,fkfhkj*/ select * \nfrom table");
	}
	public void testNoStatementBecauseCommented() {
		genericTestAndAssertion("-- select * from table;");
		genericTestAndAssertion("/*select * from table;*/");
	}
	public void testOneStatement() {	
		String STATEMENT = ";";
		genericTestAndAssertion(STATEMENT, STATEMENT); // it is not valid but its a statement
		STATEMENT = "select * from table;";
		genericTestAndAssertion(STATEMENT, STATEMENT);
		STATEMENT = "/*qwertysfagsdb,fkfhkj*/ select * \nfrom table;";
		genericTestAndAssertion(STATEMENT, STATEMENT);
	}
	public void testOneStatementEmbeddedComment() {	
		String STATEMENT = "select * /*SQL is fun*/ from person;";
		genericTestAndAssertion(STATEMENT, STATEMENT);
	}
	public void testMultipleStatementsOneLine() {
		genericTestAndAssertion("select * from table1; select * from table2;", "select * from table1;", 
				" select * from table2;");
		genericTestAndAssertion(";select * from table1; select * from table2;", ";",
				"select * from table1;", " select * from table2;");
	}
	public void testMultipleStatementsMultiLine() {
		genericTestAndAssertion("select * from table1;\nselect * from table2;", "select * from table1;", "\nselect * from table2;");
		genericTestAndAssertion(" ;\nselect * from table1;\nselect * from table2;",
				" ;", "\nselect * from table1;", "\nselect * from table2;");
	}
	public void testMultipleStatementsMultiLineWithComments() {
		genericTestAndAssertion("--select * from table1;xxx\nselect * from table2;", "select * from table2;");
		genericTestAndAssertion("select * from table1; /* drop dabase :); */\nselect * from table2;", 
				"select * from table1;", " /* drop dabase :); */\nselect * from table2;");
		genericTestAndAssertion("--update table2 set x=y;\n  --insert trash into customer;\n"
				+ "select * from table1;\nselect * from table2;\n-- drop table1", 
				"select * from table1;", "\nselect * from table2;");
	}
}
