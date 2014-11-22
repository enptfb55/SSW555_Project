/**
 * @file GEDCOMParser.java
 */

package gedcom;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import main.Family;
import main.Individual;

public class GEDCOMParser {
	

	TreeMap<String, Individual> individuals = new TreeMap<String, Individual>();
	TreeMap<String, Family> families = new TreeMap<String, Family>();
	List<GEDCOMError> errors = new LinkedList<GEDCOMError>();
	
	public GEDCOMParser ()
	{
		
	}
	
	public GEDCOMParser (String sFilePath) throws IOException {
		ParseFile(sFilePath);
	}
	
	public GEDCOMParser(File fFile) throws IOException {
		ParseFile(fFile.getPath());
	}

	public void ParseFile (String sFilePath) throws IOException {
		FileReader frFileReader = new FileReader( sFilePath );
		BufferedReader brBufferedReader = new BufferedReader( frFileReader );
		
		ParseBuffer (brBufferedReader);
		
	}
	
	public void ParseBuffer (BufferedReader br) throws IOException {
		
		String line, level, lastTopLevelTagName, tagName, id, lastId = "", tagArgument = "", lastTagName = "";
		int secondSpaceIndex;
		GEDCOMTag tag;
	
		
		while ((line = br.readLine()) != null)
		{
			level = line.substring(0, 1);
			secondSpaceIndex = line.indexOf(" ", 2);
			
			// we should use the int value and compare to a enum
			if(level.equals(GEDCOMTag.LEVEL_0))
			{
				tagName = line.substring(secondSpaceIndex + 1);

				if(secondSpaceIndex > 0)
				{
					tagArgument = line.substring(2, secondSpaceIndex);
				}
			}
			else if(secondSpaceIndex < 0)
			{
				tagName = line.substring(2);
			}
			else
			{
				tagName = line.substring(2, secondSpaceIndex);
				tagArgument = line.substring(secondSpaceIndex + 1);
			}
					
			try
			{
				tag = new GEDCOMTag(level, tagName, tagArgument);
				
				switch (tag.getLevel())
				{
					case GEDCOMTag.LEVEL_0:
						lastTopLevelTagName = tag.getName();
						
						id = Individual.ParseIdFromString(tag.getArgument());
						lastId = id;

						switch(lastTopLevelTagName)
						{
							case GEDCOMTag.NAME_INDI:
								individuals.put(id, new Individual(id));
								break;
							
							case GEDCOMTag.NAME_FAM:
								families.put(id, new Family(id));
								break;
						}
						
						break;
					
					
					case GEDCOMTag.LEVEL_1:
						lastTagName = tag.getName();
						
						switch(tag.getName())
						{
							case GEDCOMTag.NAME_NAME:
								Individual iName = individuals.get(lastId);
								iName.setName(tag.getArgument());
								individuals.put(lastId, iName);
								break;
								
							case GEDCOMTag.NAME_SEX:
								Individual iSex = individuals.get(lastId);
								iSex.setSex(tag.getArgument().charAt(0));
								individuals.put(lastId, iSex);
								break;
								
							case GEDCOMTag.NAME_FAMC:
								Individual iFamc = individuals.get(lastId);
								List<String> childOfFamilyIds = iFamc.getChildOfFamilyIDs();
								childOfFamilyIds.add(Individual.ParseIdFromString(tag.getArgument()));
								iFamc.setChildOfFamilyIDs(childOfFamilyIds);
								individuals.put(lastId, iFamc);
								break;
							
							case GEDCOMTag.NAME_FAMS:
								Individual iFams = individuals.get(lastId);
								List<String> spouseOfFamilyIds = iFams.getSpouseOfFamilyIDs();
								spouseOfFamilyIds.add(Individual.ParseIdFromString(tag.getArgument()));
								iFams.setSpouseOfFamilyIDs(spouseOfFamilyIds);
								individuals.put(lastId, iFams);
								break;
							
							case GEDCOMTag.NAME_HUSB:
								Family fHusband = families.get(lastId);
								fHusband.setHusband(new Individual(Individual.ParseIdFromString(tag.getArgument())));
								families.put(lastId, fHusband);
								break;
							
							case GEDCOMTag.NAME_WIFE:
								Family fWife = families.get(lastId);
								fWife.setWife(new Individual(Individual.ParseIdFromString(tag.getArgument())));
								families.put(lastId, fWife);
								break;
								
							case GEDCOMTag.NAME_CHIL:
								Family fChild = families.get(lastId);
								fChild.setChild(new Individual(Individual.ParseIdFromString(tag.getArgument())));
								families.put(lastId, fChild);
								break;
						}
						
						break;
						
					case GEDCOMTag.LEVEL_2: //currently only DATE exists as valid tag name
						
						try
						{
							Date d = new SimpleDateFormat("d MMM yyyy").parse(tag.getArgument());
							
							switch(lastTagName)
							{
								case GEDCOMTag.NAME_BIRT:
									Individual iBirt = individuals.get(lastId);
									iBirt.setBirthday(d);
									individuals.put(lastId,  iBirt);
									break;
								
								case GEDCOMTag.NAME_DEAT:
									Individual iDeat = individuals.get(lastId);
									iDeat.setDeath(d);
									individuals.put(lastId,  iDeat);
									break;
								
								case GEDCOMTag.NAME_DIV:
									Family fDiv = families.get(lastId);
									fDiv.setDivorced(d);
									families.put(lastId,  fDiv);
									break;
								
								case GEDCOMTag.NAME_MARR:
									Family fMarr = families.get(lastId);
									fMarr.setMarried(d);
									families.put(lastId,  fMarr);
									break;
							}
						}
						catch (ParseException e)
						{
							errors.add(new GEDCOMError(e.getMessage()));
						}
						
						break;
				}
			}
			catch (IllegalArgumentException e)
			{
				errors.add(new GEDCOMError(e.getMessage()));
			}
			
			tagArgument = "";
		}
		
		//should probably do this more elegantly because we're doing 2 passes, 1 for this, second for validation
		for (Map.Entry<String, Family> entry : families.entrySet())
		{
			Family f = entry.getValue();
			
			//doing this because we only initially saved the ids due to uncertain order of records from input file
			if(f.getHusband() != null) { f.setHusband(individuals.get(f.getHusband().getId())); }
			if(f.getWife() != null) { f.setWife(individuals.get(f.getWife().getId())); }
			if(f.getChild() != null) { f.setChild(individuals.get(f.getChild().getId())); }
			
			families.put(f.getId(), f); //would fail if a family has no id
		}
		
		//should do this validation more elegantly using design patterns
		for (Map.Entry<String, Individual> entry : individuals.entrySet())
		{
			Individual i = entry.getValue();
					
			if(GEDCOMValidator.isIndividualWithNoId(i))
			{
				errors.add(new GEDCOMError("An individual was found with no identifier"));
			}
			
			if(GEDCOMValidator.isBirthdayAfterDateOfDeath(i))
			{
				errors.add(new GEDCOMError("Individual " + i.getId() + " has a birth date of " 
											+ new SimpleDateFormat("d MMM yyyy").format(i.getBirthday()) + " which is >= their death date of " 
											+ new SimpleDateFormat("d MMM yyyy").format(i.getDeath())));
			}
			
			if(GEDCOMValidator.isDeathDateBeforeBirthday(i))
			{
				errors.add(new GEDCOMError("Individual " + i.getId() + " has a death date of " 
											+ new SimpleDateFormat("d MMM yyyy").format(i.getBirthday()) + " which is <= their birthdate of " 
											+ new SimpleDateFormat("d MMM yyyy").format(i.getDeath())));
			}
			
			if(GEDCOMValidator.isSexInvalid(i))
			{
				errors.add(new GEDCOMError("Individual " + i.getId() + " has an invalid sex assignment of " + i.getSex()));
			}
			
			if(GEDCOMValidator.isDeathListedWithoutBirthday(i))
			{
				errors.add(new GEDCOMError("Individual " + i.getId() + " has a death date listed without a corresponding birthday"));
			}
			
			if(GEDCOMValidator.hasMoreThanOneSpouse(individuals,families,i))
			{	
				errors.add(new GEDCOMError("Individual " + i.getId() + " was found invalidly married more than once"));
			}
			
			if(GEDCOMValidator.isChildBornBeforeParent(individuals,families,i))
			{
				errors.add(new GEDCOMError("Individual " + i.getId() + " was found to be born before a parent"));
			}
			
			if (GEDCOMValidator.isBornAfterParentsDeath (individuals, families, i)) 
			{
				errors.add(new GEDCOMError("Individual " + i.getId() + " was found to be born after both parents were deceased"));
			}
			
			if (GEDCOMValidator.isMarriedBefore18(individuals, families, i)) 
			{
				errors.add(new GEDCOMError("Individual " + i.getId() + " was found to be married before they were 18"));
			}			

			if(GEDCOMValidator.isMarriedToSibling(families,individuals,i))
			{
				errors.add(new GEDCOMError("Individual " + i.getId() + " was found to be married to a sibling"));
			}
			
			if(GEDCOMValidator.isMarriedtoParent(families,i))
			{
				errors.add(new GEDCOMError("Individual " + i.getId() + " was found to be married to a parent"));
			}
			
			if(GEDCOMValidator.hasMoreThanOneDeathDate(i))
			{
				errors.add(new GEDCOMError("Individual " + i.getId() + " was found to have more than one death date"));
			}
			
			if(GEDCOMValidator.isBirthdayInFuture(i))
			{
				errors.add(new GEDCOMError("Individual " + i.getId() + " was found to have a birthday in the future "));
			}
			
			if(GEDCOMValidator.childIsHisOwnParent(i,families))
			{
				errors.add(new GEDCOMError("Individual " + i.getId() + " is listed as their own parent "));
			}
			
			if(GEDCOMValidator.isBornOutOfWedlock(i,families))
			{
				errors.add(new GEDCOMError("Individual " + i.getId() + " is born out of wedlock "));
			}
			
			if(GEDCOMValidator.isOlderThanInYears(i, 120))
			{
				errors.add(new GEDCOMError("Individual " + i.getId() + " is found to be older than 120 years "));
			}
		}
		
		for (Map.Entry<String, Family> entry : families.entrySet())
		{
			Family f = entry.getValue();
			
			if(GEDCOMValidator.isFamilyWithNoId(f))
			{
				errors.add(new GEDCOMError("A family was found with no identifier"));
			}
			
			if(GEDCOMValidator.isHusbandSexNonMale(f))
			{
				errors.add(new GEDCOMError("Family " + f.getId() + " has a non-male husband listed with id " + f.getHusband().getId()));
			}
			
			if(GEDCOMValidator.isWifeSexNonFemale(f))
			{
				errors.add(new GEDCOMError("Family " + f.getId() + " has a non-female wife listed with id " + f.getWife().getId()));
			}
			
			if(GEDCOMValidator.isHusbandDeadAndMarried(f))
			{
				errors.add(new GEDCOMError("Family " + f.getId() + " has a has a dead wife listed with id " + f.getWife().getId()));
			}
			
			if(GEDCOMValidator.isWifeDeadAndMarried(f))
			{
				errors.add(new GEDCOMError("Family " + f.getId() + " has a dead wife listed with id " + f.getWife().getId()));
			}
			
			if(GEDCOMValidator.isDivorceListedWithoutDateOfMarriage(f))
			{
				errors.add(new GEDCOMError("Family " + f.getId() + " has a date of divorce listed without a corresponding day of marriage"));
			}
			
			if(GEDCOMValidator.isMarriagedateInFuture(f))
			{
				errors.add(new GEDCOMError("Family " +f.getId()  + " has marriage date in future"));
			}
			
			if(GEDCOMValidator.isMarriedLongerThan120Years(f))
			{
				errors.add(new GEDCOMError("Family " +f.getId()  + " has been married for longer than 120 years"));
			}
		}
	}
	
	public TreeMap<String, Individual> getIndividuals () {
		return individuals;
	}
	
	public TreeMap<String, Family> getFamilies () {
		return families;
	}
	
	public LinkedList<GEDCOMError> getErrors()
	{
		return (LinkedList<GEDCOMError>) errors;
	}
}
