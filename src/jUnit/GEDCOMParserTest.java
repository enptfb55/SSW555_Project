/**
 * 
 */
package jUnit;

import static org.junit.Assert.*;
import gedcom.GEDCOMELine;
import gedcom.GEDCOMParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author samato
 *
 */
public class GEDCOMParserTest {
	
	GEDCOMParser mGParser = null;
	final static String mGedcomLine = "1 NAME Bob";

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		mGParser = new GEDCOMParser();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		
		GEDCOMELine gLine = mGParser.ParseLine(mGedcomLine);
		
		assertEquals (1, gLine.getLevelNumber());
		assertEquals("NAME", gLine.getTag());
		assertEquals("Bob", gLine.getArgs());
		
	}

}
