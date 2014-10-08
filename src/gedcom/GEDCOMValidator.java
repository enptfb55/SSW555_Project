package gedcom;

import java.util.Collection;

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
			if(i.getSex() == Sex.MALE)
			{
				for (Family f : families)
				{
					//to do
			    }
			}
			
			if(numValidMarriages > 1)
				return true;
		}
		
		return false;
	}
	
	public static boolean isBornBeforeAParent(Individual i, Collection<Family> families)
	{
		//depends on how second parameter is... if it is the parent families already or in the coding below, it is all families
		//if i != null && i.getBirthday() != null & parentFamilies != null
		//need double for loop
		//for each i.childOfFamilyIds
		//loop over parentFamilies
		//if parentFamilies.getId() == i.childOfFamilyIds then continue, else skip
		//if((parentFamilies.getHusband() != null && i.getBirthday().before(parent.Families.getHusband().getBirthday())
		//     || (parentFamilies.getWife() != null && i.getBirthday().before(parent.Families.getWife().getBirthday())) return true
		//
		
		return false;
	}
	
	public static boolean isMarriedToAChildOfTheirs(Individual i, Collection<Family> families)
	{
		return false;
	}
	
	public static boolean isMarriedToASibling(Individual i, Collection<Family> families)
	{
		return false;
	}
}
