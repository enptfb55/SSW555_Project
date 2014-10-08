package jUnit;

import static org.junit.Assert.*;
import gedcom.GEDCOMValidator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import main.Family;
import main.Individual;
import main.Sex;

import org.junit.Test;

public class GEDCOMValidatorTest {
	
	final static String INDIVIDUAL_ID = "123";
	final static String INDIVIDUAL_BIRTHDAY_DATE = "July 8, 1902";
	final static String INDIVIDUAL_DEATH_DATE = "December 17, 1934";
	
	final static String FAMILY_ID = "234";
	final static String FAMILY_DATE_MARRIED_STRING = "July 8, 1902";
	final static String FAMILY_DATE_DIVORCED_STRING = "December 17, 1934";


	@Test
	public final void testIsIndividualWithNoId() {
		assertTrue(GEDCOMValidator.isIndividualWithNoId(new Individual("")));
	}
	
	@Test
	public final void testIsFamilyWithNoId() {
		assertTrue(GEDCOMValidator.isFamilyWithNoId(new Family("")));
	}
	
	@Test
	public final void testIsDeathListedWithoutBirthday() {
		boolean validationResult = false;
		
		try
		{
			Individual i = new Individual(INDIVIDUAL_ID);
			i.setDeath(new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).parse(INDIVIDUAL_DEATH_DATE));
			validationResult = GEDCOMValidator.isDeathListedWithoutBirthday(i);
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
	
		assertTrue(validationResult);
	}

	@Test
	public final void testIsBirthdayAfterDateOfDeath() {
		boolean validationResult = false;
		
		try
		{
			Date d = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).parse(INDIVIDUAL_DEATH_DATE);
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			cal.add(Calendar.DATE, +20); //20 days after death date
			
			Individual i = new Individual(INDIVIDUAL_ID);
			i.setDeath(d);
			i.setBirthday(cal.getTime());
			validationResult = GEDCOMValidator.isBirthdayAfterDateOfDeath(i);
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		
		assertTrue(validationResult);
	}

	@Test
	public final void testIsDeathDateBeforeBirthday() {
		boolean validationResult = false;

		try
		{
			Date d = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).parse(INDIVIDUAL_BIRTHDAY_DATE);
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			cal.add(Calendar.DATE, -20); //20 days before birth date
			
			Individual i = new Individual(INDIVIDUAL_ID);
			i.setBirthday(d);
			i.setDeath(cal.getTime());
			validationResult = GEDCOMValidator.isBirthdayAfterDateOfDeath(i);
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		
		assertTrue(validationResult);
	}

	@Test
	public final void testIsSexInvalid() {
		Individual i = new Individual(INDIVIDUAL_ID);
		i.setSex('A');
		assertTrue(GEDCOMValidator.isSexInvalid(i));
	}

	@Test
	public final void testIsHusbandSexNonMale() {
		Family f = new Family(FAMILY_ID);
		
		Individual female = new Individual(INDIVIDUAL_ID);
		female.setSex(Sex.FEMALE);
		
		f.setHusband(female);
		assertTrue(GEDCOMValidator.isHusbandSexNonMale(f));
	}

	@Test
	public final void testIsWifeSexNonFemale() {
		Family f = new Family(FAMILY_ID);
		
		Individual male = new Individual(INDIVIDUAL_ID);
		male.setSex(Sex.MALE);
		
		f.setWife(male);
		assertTrue(GEDCOMValidator.isWifeSexNonFemale(f));
	}

	@Test
	public final void testIsHusbandDeadAndMarried() {
		boolean validationResult = false;
		
		try
		{
			Date d = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).parse(FAMILY_DATE_MARRIED_STRING);
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			cal.add(Calendar.DATE, -20); //20 days before marriage date -- should probably check same-day marriage and death too
			
			Individual male = new Individual(INDIVIDUAL_ID);
			male.setDeath(cal.getTime());
			
			Family f = new Family(FAMILY_ID);
			f.setMarried(d);
			f.setHusband(male);
			
			validationResult = GEDCOMValidator.isHusbandDeadAndMarried(f);
		}
		catch (ParseException e)
		{
			
		}
		
		assertTrue(validationResult);
	}

	@Test
	public final void testIsWifeDeadAndMarried() {
		boolean validationResult = false;
		
		try
		{
			Date d = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).parse(FAMILY_DATE_MARRIED_STRING);
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			cal.add(Calendar.DATE, -20); //20 days before marriage date -- should probably check same-day marriage and death too
			
			Individual female = new Individual(INDIVIDUAL_ID);
			female.setDeath(cal.getTime());
			
			Family f = new Family(FAMILY_ID);
			f.setMarried(d);
			f.setWife(female);
			
			validationResult = GEDCOMValidator.isWifeDeadAndMarried(f);
		}
		catch (ParseException e)
		{
			
		}
		
		assertTrue(validationResult);
	}

	@Test
	public final void testIsDivorceListedWithoutDateOfMarriage() {
		boolean validationResult = false;
		
		try
		{
			Family f = new Family(FAMILY_ID);
			
			f.setDivorced(new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).parse(FAMILY_DATE_DIVORCED_STRING));
			
			validationResult = GEDCOMValidator.isDivorceListedWithoutDateOfMarriage(f);
		}
		catch (ParseException e)
		{
			
		}
		
		assertTrue(validationResult);
	}

}
