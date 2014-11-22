package jUnit;

import static org.junit.Assert.*;
import gedcom.GEDCOMValidator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.*;

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
	public final void testIsHusbandDeadAndMarried()
	{
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
	
	@Test
	public void testChildBornBeforeParent()
	{
		
		TreeMap<String, Family> familyIndex = new TreeMap<String, Family>();
		TreeMap<String, Individual> personIndex = new TreeMap<String, Individual>();
		
		Individual ind1 = new Individual("1");
		Individual ind2 = new Individual("2");
		Individual ind3 = new Individual("3");
		Individual ind4 = new Individual("4");
		Individual ind5 = new Individual("5");
		Individual ind6 = new Individual("6");
		
		Family testf1 = new Family("1");
		Family testf2 = new Family("2");
		
		
	
		
		Date older = new Date(1970, 10, 10);
		Date mid = new Date(1980, 10, 10);
		
		Date young = new Date(1997, 10, 10);

		
		List<String> SpouseFamilyId = new 	ArrayList<String>();
		SpouseFamilyId.add("1");
		ind1.setSpouseOfFamilyIDs(SpouseFamilyId);
		ind1.setBirthday(older);
		
		//List<String> SpouseFamilyId1 = new 	ArrayList<String>();
		SpouseFamilyId.add("1");
		ind2.setSpouseOfFamilyIDs(SpouseFamilyId);
		ind2.setBirthday(mid);
		
		List<String> childId = new 	ArrayList<String>();
		childId.add("1");
		ind3.setChildOfFamilyIDs(childId);
		ind3.setBirthday(young);
		
		//List<String> SpouseFamilyId2 = new 	ArrayList<String>();
		SpouseFamilyId.add("2");
		ind4.setSpouseOfFamilyIDs(SpouseFamilyId);
		ind4.setBirthday(mid);
		
		//List<String> SpouseFamilyId3 = new 	ArrayList<String>();
		SpouseFamilyId.add("2");
		ind5.setSpouseOfFamilyIDs(SpouseFamilyId);
		ind5.setBirthday(young);

		
		childId.add("2");
		ind6.setChildOfFamilyIDs(childId);
		ind6.setBirthday(mid);
		
		ind1.setSex('M');
		ind2.setSex('F');
		ind3.setSex('F');
		ind4.setSex('M');
		ind5.setSex('F');
		
		
		testf1.setHusband(ind1);
		testf1.setWife(ind2);
		testf2.setHusband(ind4);
		testf2.setWife(ind5);
		testf1.setChild(ind3);
		testf2.setChild(ind6);
		
		
		personIndex.put(ind1.getId(), ind1);
		personIndex.put(ind2.getId(), ind2);
		personIndex.put(ind3.getId(), ind3);
		personIndex.put(ind4.getId(), ind4);
		personIndex.put(ind5.getId(), ind5);
		personIndex.put(ind6.getId(), ind6);
		familyIndex.put(testf1.getId(), testf1);
		familyIndex.put(testf2.getId(), testf2);
		
			
	//	assertTrue(fa.bornBeforeParents(personIndex,familyIndex,ind6));
		assertTrue(!GEDCOMValidator.isChildBornBeforeParent(personIndex,familyIndex,ind3));
	}
	
	@Test
	public void testChildBornAfterParentsDeath()
	{
		
		TreeMap<String, Family> familyIndex = new TreeMap<String, Family>();
		TreeMap<String, Individual> personIndex = new TreeMap<String, Individual>();
		
		Individual ind1 = new Individual("1");
		Individual ind2 = new Individual("2");
		Individual ind3 = new Individual("3");
		Individual ind4 = new Individual("4");
		Individual ind5 = new Individual("5");
		Individual ind6 = new Individual("6");
		
		Family testf1 = new Family("1");
		Family testf2 = new Family("2");
		
		Date older = new Date(1970, 10, 10);
		Date mid = new Date(1980, 10, 10);
		
		Date young = new Date(1997, 10, 10);

		
		List<String> SpouseFamilyId = new 	ArrayList<String>();
		SpouseFamilyId.add("1");
		ind1.setSpouseOfFamilyIDs(SpouseFamilyId);
		ind1.setDeath(young);
		
		SpouseFamilyId.add("1");
		ind2.setSpouseOfFamilyIDs(SpouseFamilyId);
		ind2.setDeath(mid);
		
		List<String> childId = new 	ArrayList<String>();
		childId.add("1");
		ind3.setChildOfFamilyIDs(childId);
		ind3.setBirthday(older);
		
		SpouseFamilyId.add("2");
		ind4.setSpouseOfFamilyIDs(SpouseFamilyId);
		ind4.setBirthday(mid);
		
		SpouseFamilyId.add("2");
		ind5.setSpouseOfFamilyIDs(SpouseFamilyId);
		ind5.setBirthday(young);

		
		childId.add("2");
		ind6.setChildOfFamilyIDs(childId);
		ind6.setBirthday(mid);
		
		ind1.setSex('M');
		ind2.setSex('F');
		ind3.setSex('F');
		ind4.setSex('M');
		ind5.setSex('F');
		
		
		testf1.setHusband(ind1);
		testf1.setWife(ind2);
		testf2.setHusband(ind4);
		testf2.setWife(ind5);
		testf1.setChild(ind3);
		testf2.setChild(ind6);
		
		
		personIndex.put(ind1.getId(), ind1);
		personIndex.put(ind2.getId(), ind2);
		personIndex.put(ind3.getId(), ind3);
		personIndex.put(ind4.getId(), ind4);
		personIndex.put(ind5.getId(), ind5);
		personIndex.put(ind6.getId(), ind6);
		familyIndex.put(testf1.getId(), testf1);
		familyIndex.put(testf2.getId(), testf2);
		
		assertTrue(!GEDCOMValidator.isBornAfterParentsDeath(personIndex,familyIndex,ind3));
	}
	
	@Test
	public void testisMarriedBefore18() 
	{
		Individual i1 = new Individual("1");
		Individual i2 = new Individual("2");
		Family f1 = new Family("1");
		
		f1.setHusband( i1 );
		f1.setWife( i2 );
		
		List<String> SpouseFamilyId = new 	ArrayList<String>();
		SpouseFamilyId.add(f1.getId());
		i1.setSpouseOfFamilyIDs( SpouseFamilyId );
		i2.setSpouseOfFamilyIDs( SpouseFamilyId );
		
		f1.setMarried( new Date(2010, 1, 1) );
		i1.setBirthday( new Date(1995, 1, 1) );
		i2.setBirthday(new Date(1990, 1, 1) );
		
		TreeMap<String, Family> famTable = new TreeMap<String, Family>();
		famTable.put(f1.getId(), f1);
		
		TreeMap<String, Individual> indTable = new TreeMap<String, Individual>();
		indTable.put(i1.getId(), i1);
		indTable.put(i2.getId(), i2);
		
		assertTrue( GEDCOMValidator.isMarriedBefore18( indTable, famTable, i1 ) );
		
	}
	
	@Test
	public void testhasmorethanonespouse() {
		Individual i1 = new Individual("1");
		Individual i2 = new Individual("2");
		Individual i3 = new Individual("3");
		Family f1 = new Family("1");
		Family f2 = new Family("2");
		
		TreeMap<String, Individual> indTable = new TreeMap<String, Individual>();
		
		f1.setHusband(i1);
		f1.setWife(i2);
		List<String> SpouseFamilyId = new 	ArrayList<String>();
		
		SpouseFamilyId.add(f1.getId());
		i1.setSpouseOfFamilyIDs(SpouseFamilyId);
		i2.setSpouseOfFamilyIDs(SpouseFamilyId);
		
		f1.setMarried(new Date(2002, 6, 5));
		f1.setMarried(new Date(2012, 12, 12));
		
		f2.setHusband(i1);
		f2.setWife(i3);
		SpouseFamilyId.add(f2.getId());
		i1.setSpouseOfFamilyIDs(SpouseFamilyId);
		i2.setSpouseOfFamilyIDs(SpouseFamilyId);
		f2.setMarried(new Date(2012, 6, 5));
		
		TreeMap<String, Family> famTable = new TreeMap<String, Family>();
		famTable.put(f1.getId(), f1);
		famTable.put(f2.getId(), f2);
		
		indTable.put(i1.getId(), i1);
		indTable.put(i2.getId(), i2);
		indTable.put(i3.getId(), i3);
		
		assertTrue(GEDCOMValidator.hasMoreThanOneSpouse(indTable, famTable, i1) );
	}
	
	
	
	
	@Test
	public void testisMarriedToSibling()
	{
		Family fam1 = new Family("1");
		Family fam2 = new Family("2");
		
		Individual ind1 = new Individual("1");
		Individual ind2 = new Individual("2");
		
		ind1.setSex('M');
		ind2.setSex('F');
		
		List<String> spouseOfFamilyIDs = new ArrayList<String>();
		spouseOfFamilyIDs.add("1");
		ind1.setSpouseOfFamilyIDs(spouseOfFamilyIDs);
		ind2.setSpouseOfFamilyIDs(spouseOfFamilyIDs);
		
		
		List<String> ChildOfFamilyIDs = new ArrayList<String>();
		ChildOfFamilyIDs.add("2");
		ind1.setChildOfFamilyIDs(ChildOfFamilyIDs);
		ind2.setChildOfFamilyIDs(ChildOfFamilyIDs);
		
		fam1.setHusband(ind1);
		fam1.setWife(ind2);
		
		fam2.setChild(ind1);
		fam2.setChild(ind2);
		
		TreeMap<String, Individual> indTable = new TreeMap<String, Individual>();
		indTable.put(ind1.getId(), ind1);
		indTable.put(ind2.getId(), ind2);
		
		TreeMap<String, Family> famTable = new TreeMap<String, Family>();
		famTable.put(fam1.getId(), fam1);
		famTable.put(fam2.getId(), fam2);
		
		assertTrue(GEDCOMValidator.isMarriedToSibling(famTable, indTable, ind1));	
	}
	
	
	@Test
	public void testIsMarriedToParent(){
		
		TreeMap<String, Family> familyIndex = new TreeMap<String, Family>();
		TreeMap<String, Individual> personIndex = new TreeMap<String, Individual>();

		Individual test1 = new Individual("5");
		Individual test2 = new Individual("6");
		Individual test3 = new Individual("7");
		Individual test4 = new Individual("8");
		Individual test5 = new Individual("9");
		Family testf1 = new Family("1");
		Family testf2 = new Family("2");
		Family testf3 = new Family("3");

		List<String> spouseOfFamilyIDs = new ArrayList<String>();
		spouseOfFamilyIDs.add("1");
		test1.setSpouseOfFamilyIDs(spouseOfFamilyIDs);

		spouseOfFamilyIDs.add("2");
		test1.setSpouseOfFamilyIDs(spouseOfFamilyIDs);

		List<String> spouseOfFamilyIDs1 = new ArrayList<String>();
		spouseOfFamilyIDs1.add("1");
		test2.setSpouseOfFamilyIDs(spouseOfFamilyIDs1);

		List<String> ChildOfFamilyIDs = new ArrayList<String>();
		ChildOfFamilyIDs.add("1");
		test3.setChildOfFamilyIDs(ChildOfFamilyIDs);

		List<String> spouseOfFamilyIDs2 = new ArrayList<String>();
		spouseOfFamilyIDs2.add("1");
		test3.setSpouseOfFamilyIDs(spouseOfFamilyIDs2);

		List<String> spouseOfFamilyIDs4 = new ArrayList<String>();
		spouseOfFamilyIDs4.add("3");
		test4.setSpouseOfFamilyIDs(spouseOfFamilyIDs4);

		List<String> spouseOfFamilyIDs5 = new ArrayList<String>();
		spouseOfFamilyIDs5.add("3");
		test5.setSpouseOfFamilyIDs(spouseOfFamilyIDs5);

		test1.setSex('M');
		test2.setSex('F');
		test3.setSex('F');
		test4.setSex('M');
		test5.setSex('F');
		testf1.setHusband(test1);
		testf1.setWife(test2);
		testf2.setHusband(test1);
		testf2.setWife(test3);
		testf3.setHusband(test4);
		testf3.setWife(test5);
		testf1.setChild(test3);

		personIndex.put(test1.getId(), test1);
		personIndex.put(test2.getId(), test2);
		personIndex.put(test3.getId(), test3);
		personIndex.put(test4.getId(), test4);
		personIndex.put(test5.getId(), test5);
		familyIndex.put(testf1.getId(), testf1);
		familyIndex.put(testf2.getId(), testf2);
		familyIndex.put(testf3.getId(), testf3);

		assertTrue(!GEDCOMValidator.isMarriedtoParent(familyIndex, test3)); // Daughter
		// married
		// to
		// father.
		// Should
		// not
		// be
		// true
		// because
		// the
		// function
		// checks
		// if
		// the
		// parent
		// was
		// married
		// to
		// their
		// child,
		// not
		// the
		// other
		// way
		// around.
		assertTrue(GEDCOMValidator.isMarriedtoParent(familyIndex, test1)); // Father
		// married
		// to
		// daughter.
		assertTrue(!GEDCOMValidator.isMarriedtoParent(familyIndex, test4)); // Normal
		assertTrue(!GEDCOMValidator.isMarriedtoParent(familyIndex, test2)); // step
		// father
		// married
		// to
		// step
		// daughter
		// -
		// is
		// allowed
	}
	
	@Test
	public final void testIsHusbandDeadAndMarried1()
	{
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
	public final void hasMoreThanOneDeathDate()
	{
		try
		{
			Individual i = new Individual(INDIVIDUAL_ID);
			assertFalse(GEDCOMValidator.hasMoreThanOneDeathDate(i));

			i.setDeath(new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).parse(INDIVIDUAL_DEATH_DATE));
			assertFalse(GEDCOMValidator.hasMoreThanOneDeathDate(i));

			i.setDeath(i.getDeath());
			assertFalse(GEDCOMValidator.hasMoreThanOneDeathDate(i));

			i.setDeath(new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).parse(FAMILY_DATE_MARRIED_STRING));
			assertTrue(GEDCOMValidator.hasMoreThanOneDeathDate(i));

			i.setDeath(i.getDeath());
			assertTrue(GEDCOMValidator.hasMoreThanOneDeathDate(i));

			Individual i2 = new Individual(INDIVIDUAL_ID);
			
			i2.setDeath(new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).parse(INDIVIDUAL_DEATH_DATE));
			i2.setDeath(null);
			assertFalse(GEDCOMValidator.hasMoreThanOneDeathDate(i2));
			
			i2.setDeath(new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).parse(FAMILY_DATE_MARRIED_STRING));
			assertTrue(GEDCOMValidator.hasMoreThanOneDeathDate(i2));
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
	}
	
	@Test
	public final void isBirthdayInFuture()
	{
		try
		{
			Individual i = new Individual(INDIVIDUAL_ID);

			i.setBirthday(new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).parse(INDIVIDUAL_BIRTHDAY_DATE));
			assertFalse(GEDCOMValidator.isBirthdayInFuture(i));

			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.DATE, +20); //20 days after now
			
			i.setBirthday(cal.getTime());
			assertTrue(GEDCOMValidator.isBirthdayInFuture(i));
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
	}

	@Test
	public final void childIsHisOwnParent() {
		
		TreeMap<String, Family> familyIndex = new TreeMap<String, Family>();
		Individual test1 = new Individual("1");
		Individual test2 = new Individual("2");

		Family testf1 = new Family("1");
		
		testf1.setHusband(test1);
		testf1.setWife(test2);
		testf1.setChild(test1);

		List<String> ChildOfFamilyIDs = new ArrayList<String>();
		ChildOfFamilyIDs.add("1");
		test1.setChildOfFamilyIDs(ChildOfFamilyIDs);
		
		familyIndex.put(test1.getId(), testf1);
		
		assertTrue(GEDCOMValidator.childIsHisOwnParent(test1,familyIndex));
	}
	
	@Test
	public final void testIsBornOutOfWedlock() {
		
		TreeMap<String, Family> familyIndex = new TreeMap<String, Family>();
		Individual father = new Individual("1");
		Individual mother = new Individual("2");
		Individual child = new Individual("2");

		Family testf1 = new Family("1");
		
		testf1.setHusband(father);
		testf1.setWife(mother);
		testf1.setChild(child);

		List<String> ChildOfFamilyIDs = new ArrayList<String>();
		ChildOfFamilyIDs.add("1");
		child.setChildOfFamilyIDs(ChildOfFamilyIDs);
		
		familyIndex.put(father.getId(), testf1);
		
		assertTrue(GEDCOMValidator.isBornOutOfWedlock(child,familyIndex));
	}
	
	@Test
	public void testMarriedDateFuture()
	{
		TreeMap<String, Family> familyIndex = new TreeMap<String, Family>();
	
		Family f1 = new Family("1");
		f1.setMarried(new Date(2015, 4, 15));
		
		Family f2 = new Family("4");
		f2.setMarried(new Date(1994, 6, 4));
		
		
		
		familyIndex.put(f1.getId(), f1);
		familyIndex.put(f2.getId(), f2);
	
		
	
		assertTrue(GEDCOMValidator.isMarriagedateInFuture(f1));
		assertTrue(GEDCOMValidator.isMarriagedateInFuture(f2));
		
	}
	
	@Test
	public void isMarriedLongerThan120Years()
	{
		try
		{
			Date d = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).parse(FAMILY_DATE_MARRIED_STRING);
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			cal.add(Calendar.DATE, (int) (119*365.25));

			Family f = new Family(FAMILY_ID);
			
			f.setMarried(d);
			f.setDivorced(cal.getTime());
			assertFalse(GEDCOMValidator.isMarriedLongerThan120Years(f));
			
			cal.setTime(d);
			cal.add(Calendar.DATE, (int) (120*365.25));
			f.setDivorced(cal.getTime());
			assertFalse(GEDCOMValidator.isMarriedLongerThan120Years(f));
			
			cal.setTime(d);
			cal.add(Calendar.DATE, (int) (121*365.25));
			f.setDivorced(cal.getTime());
			assertTrue(GEDCOMValidator.isMarriedLongerThan120Years(f));
		}
		catch (ParseException e)
		{
			
		}
		
	}
	
	@Test
	public void isOlderThanInYears()
	{
		try
		{
			Date d = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).parse(FAMILY_DATE_MARRIED_STRING);
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			cal.add(Calendar.DATE, (int) (119*365.25));

			Individual i = new Individual(INDIVIDUAL_ID);
			
			i.setBirthday(d);
			i.setDeath(cal.getTime());
			assertFalse(GEDCOMValidator.isOlderThanInYears(i, 120));
			
			cal.setTime(d);
			cal.add(Calendar.DATE, (int) (120*365.25));
			i.setDeath(cal.getTime());
			assertFalse(GEDCOMValidator.isOlderThanInYears(i, 120));
			
			cal.setTime(d);
			cal.add(Calendar.DATE, (int) (121*365.25));
			i.setDeath(cal.getTime());
			assertTrue(GEDCOMValidator.isOlderThanInYears(i, 120));
		}
		catch (ParseException e)
		{
			
		}
		
	}
	
	
}
