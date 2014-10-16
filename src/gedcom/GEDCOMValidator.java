package gedcom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import main.Family;
import main.Individual;
import main.Sex;

public class GEDCOMValidator
{
	public static boolean isIndividualWithNoId(Individual i)
	{
		if(i.getId().isEmpty())
			return true;
		
		return false;
	}
	
	public static boolean isFamilyWithNoId(Family f)
	{
		if(f.getId().isEmpty())
			return true;
		
		return false;
	}
	
	public static boolean isDeathListedWithoutBirthday(Individual i)
	{
		if(i.getDeath() != null && i.getBirthday() == null)
			return true;
		
		return false;
	}
	
	public static boolean isBirthdayAfterDateOfDeath(Individual i)
	{
		if(i.getDeath() != null && i.getBirthday() != null && i.getBirthday().after(i.getDeath()))
			return true;
		
		return false;
	}
	
	public static boolean isDeathDateBeforeBirthday(Individual i)
	{
		if(i.getDeath() != null && i.getBirthday() != null && i.getDeath().before(i.getBirthday()))
			return true;
		
		return false;
	}
	
	//should perhaps change this to IsSexValid since the domain of values is smaller 
	public static boolean isSexInvalid(Individual i)
	{
		//first condition is null character
		if(i.getSex() != '\u0000' && i.getSex() != Sex.MALE && i.getSex() != Sex.FEMALE)
			return true;
		
		return false;
	}
	
	public static boolean isHusbandSexNonMale(Family f)
	{
		if(f.getHusband() != null && f.getHusband().getSex() != Sex.MALE)
			return true;
		
		return false;
	}
	
	public static boolean isWifeSexNonFemale(Family f)
	{
		if(f.getWife() != null && f.getWife().getSex() != Sex.FEMALE)
			return true;
		
		return false;
	}
	
	public static boolean isHusbandDeadAndMarried(Family f)
	{
		//same day marriage and death should probably be checked too
		if(f.getHusband() != null && f.getHusband().getDeath() != null && f.getMarried() != null && f.getHusband().getDeath().before(f.getMarried()))
		{
			return true;
		}
		
		return false;
	}
	
	public static boolean isWifeDeadAndMarried(Family f)
	{
		//same day marriage and death should probably be checked too
		if(f.getWife() != null && f.getWife().getDeath() != null && f.getMarried() != null && f.getWife().getDeath().before(f.getMarried()))
		{
			return true;
		}
		
		return false;
	}
	
	public static boolean isDivorceListedWithoutDateOfMarriage(Family f)
	{
		if(f.getMarried() == null && f.getDivorced() != null)
		{
			return true;
		}
		
		return false;
	}
	
	public static boolean isMarriedMoreThanOnceAtATime(Individual i, Collection<Family> families)
	{
		int numValidMarriages = 0;
		
		if(i != null && families != null)
		{
//			switch(i.getSex())
//			{
//				case Sex.MALE:
//					for (Family f : families)
//					{
//						//doesn't consider marriage/divorce dates and it should
//						if(f.getHusband() != null && f.getWife() != null && f.getHusband().getId() == i.getId() && f.getDivorced() == null) 
//							numValidMarriages++;
//				    }
//					break;
//					
//				case Sex.FEMALE:
//					for (Family f : families)
//					{
//						//doesn't consider marriage/divorce dates and it should
//						if(f.getHusband() != null && f.getWife() != null && f.getWife().getId() == i.getId() && f.getDivorced() != null) 
//							numValidMarriages++;
//				    }
//					break;
//		}
			
			if(numValidMarriages > 1)
				return true;
		}
		
		return false;
	}
	

	public static boolean hasmorethanonespouse( TreeMap<String, Individual> i,
											    TreeMap<String, Family> fam, 
											    Individual indi ) 
	{
		
		for (String s : indi.getSpouseOfFamilyIDs()) {
			for (String t : indi.getSpouseOfFamilyIDs()) {

				if (fam.get(s) != null && fam.get(t) != null) {

					if (!fam.get(s).equals(fam.get(t))) {
						// System.out.println(" s = " + fam.get(s));
						// System.out.println(" t = " + fam.get(t));

						Family s_test = fam.get(s);
						Family t_test = fam.get(t);
						
						Date spouseDeathDate = null;
						
						if ( fam.get(t).getHusband() == indi &&
						     fam.get(t).getWife() != null ) 
						{
							spouseDeathDate = fam.get(t).getWife().getDeath();
						} 
						else if ( fam.get(t).getHusband() != null )
						{
							spouseDeathDate = fam.get(t).getHusband().getDeath();
						}
						

						Date md = fam.get(s).getMarried();
						Date dd = fam.get(t).getDivorced();
						
						if (spouseDeathDate != null) {
							if (md.before(spouseDeathDate)) {
								System.out
								.println(indi.getName()
										+ " Married to another person before spouse in previous marriage passed away");
								return true;
							}
						}
						
						if (dd != null) {
							if (md.before(dd)) {
								System.out
										.println(indi.getName()
												+ " Married to another person before taking divorce");
								
								return true;
							}
							

						} else {
							System.out
									.println(indi.getName()
											+ "  is married more than person at same time");

							return true;
						}

					}
				}
			}
		}
		
		return false;
	}

	public static boolean ChildBornBeforeParent(TreeMap<String, Individual> indi,TreeMap<String, Family> famI,Individual i)
	{
		Individual father;
		Individual mother;
		String fam;
		
		List<String> famC = i.getChildOfFamilyIDs();
		Iterator<String> famID = famC.iterator();
		
		if(famID.hasNext())
		{
			fam = famID.next();
			System.out.println("fam = " +fam);
			if(famI.get(fam)!=null)
			{
				father = famI.get(fam).getHusband();
				mother = famI.get(fam).getWife();
				if(father!=null && mother != null) {
					//System.out.println("father baday = " + father.getBirthday());
			
					//System.out.println("Son's birthday = " + i.getBirthday());
			
					if( i.getBirthday() != null && 
						( i.getBirthday().before(father.getBirthday()) || 
					      i.getBirthday().before(mother.getBirthday()) ) )
					{
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public static boolean isMarriedToSibling(TreeMap<String, Family> familyIndex, TreeMap<String, Individual> indIndex, Individual ind)
	{
		
		ArrayList<String> spouses = ind.getAllSpousesIDs(familyIndex);
	
		ArrayList<String> familyIDsSpouseChildFamily = new ArrayList<String>();
		for(String spouseID: spouses)
		{
			if(indIndex.get(spouseID)!=null)
			{
			familyIDsSpouseChildFamily.addAll(indIndex.get(spouseID).getChildOfFamilyIDs());
			//familyIDsSpouseChildFamily.addAll(indIndex.get(spouseID).getChildOfFamilyIDs());
			//System.out.println("i am here 1 = " + familyIDsSpouseChildFamily.get(0));
			}
		}
		Iterator<String> i = ind.getChildOfFamilyIDs().iterator();
		while(i.hasNext())
		{
			if(familyIDsSpouseChildFamily.contains(i.next()))
				return true;
		}
		
		return false;
	}
	
	public static boolean isMarriedtoParent(Family f)
	{
		
		if(f.getHusband() != null && f.getWife() != null && f.getChild() != null
			&& (f.getHusband().getId().equals(f.getChild().getId()) 
				|| f.getWife().getId().equals(f.getChild().getId())))
			return true;

		return false;
	}
	

	
}
