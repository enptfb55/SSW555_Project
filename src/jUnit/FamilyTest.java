/**
 * 
 */
package jUnit;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import gedcom.GEDCOMELine;
import gedcom.GEDCOMParser;
import gedcom.GEDCOMTag;
import main.Family;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FamilyTest {
	
	final static String ID_FAMILY = "123";
	
	final static String ID_CHILD = "234";
	final static String ID_HUSBAND = "999";
	final static String ID_WIFE = "543";
	
	final static String DATE_MARRIED_STRING = "July 8, 1902";
	final static String DATE_DIVORCED_STRING = "December 17, 1934";
	
	
	final static Family FAMILY = new Family(ID_FAMILY);
	static
	{
		FAMILY.setHusbandId(ID_HUSBAND);
		FAMILY.setWifeId(ID_WIFE);
		FAMILY.setChildId(ID_CHILD);
		
		try
		{
			FAMILY.setMarried(new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).parse(DATE_MARRIED_STRING));
			FAMILY.setDivorced(new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).parse(DATE_DIVORCED_STRING));
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
	}

	@Before
	public void setUp() throws Exception {}

	@After
	public void tearDown() throws Exception {}
	
	@Test
	public void testSetIdWithNoValueThrowsException()
	{
		boolean exceptionThrown = false;
		
		try
		{
			Family f = new Family("");
			fail("Setting the id of a family to an empty value did not throw an exception when it should have");
		}
		catch (IllegalArgumentException e)
		{
			exceptionThrown = true;
		}
		
		assertTrue(exceptionThrown);
	}

	@Test
	public void testGetIdEqual()
	{
		assertEquals(ID_FAMILY, FAMILY.getId());
	}
	
	@Test
	public void testGetChildIdEqual()
	{
		assertEquals(ID_CHILD, FAMILY.getChildId());
	}
	
	@Test
	public void testGetWifeIdEqual()
	{
		assertEquals(ID_WIFE, FAMILY.getWifeId());
	}
	
	@Test
	public void testGetHusbandIdEqual()
	{
		assertEquals(ID_HUSBAND, FAMILY.getHusbandId());
	}
	
	@Test
	public void testGetMarriedEqual()
	{
		try
		{
			assertEquals(new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).parse(DATE_MARRIED_STRING), FAMILY.getMarried());
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetDivorcedEqual()
	{
		try
		{
			assertEquals(new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).parse(DATE_DIVORCED_STRING), FAMILY.getDivorced());
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
	}

}
