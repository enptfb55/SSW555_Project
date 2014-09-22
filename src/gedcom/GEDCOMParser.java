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
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.Family;
import main.Individual;

/**
 * @class GEDCOMParser
 * @author samato
 *
 */
public class GEDCOMParser {
	
	static final int flags = Pattern.CASE_INSENSITIVE | Pattern.MULTILINE;
	static final String validTags = "INDI|NAME|SEX|BIRT|DEAT|" +
						"FAMC|FAMS|FAM|MARR|HUSB|WIFE|" +
					    "CHIL|DIV|DATE|TRLR|NOTE";
	
	//static final Pattern tagPattern = Pattern.compile( "([\\w|\\@]+)", flags);
	static final Pattern linePattern = Pattern.compile ("(\\s?\\d\\s[\\w|\\@]+\\s*)", flags);
	static final Pattern levelNumPattern = Pattern.compile("^\\s?(\\d)\\s*");
	static final Pattern tagPattern = Pattern.compile("\\s?\\d\\s([\\w|\\@]+)\\s*");
	static final Pattern validTagPattern = Pattern.compile("\\s?\\d\\s(" + validTags + ")\\s*");
	static final Pattern argPattern = Pattern.compile("\\s?\\d\\s[\\w|\\@]+\\s+(.*)");
	
	public Vector<GEDCOMELine> mGLines = new Vector<GEDCOMELine>();
	
	TreeMap<String, Individual> individuals = new TreeMap<String, Individual>();
	TreeMap<String, Family> families = new TreeMap<String, Family>();

	
	public GEDCOMParser () {
		
	}
	
	/**
	 * @throws IOException 
	 * @brief Constructor
	 */
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
						
						id = tag.getArgument().replace("@", ""); //should probably move this to setId methods
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
								childOfFamilyIds.add(tag.getArgument().replace("@", ""));
								iFamc.setChildOfFamilyIDs(childOfFamilyIds);
								individuals.put(lastId, iFamc);
								break;
							
							case GEDCOMTag.NAME_FAMS:
								Individual iFams = individuals.get(lastId);
								List<String> spouseOfFamilyIds = iFams.getSpouseOfFamilyIDs();
								spouseOfFamilyIds.add(tag.getArgument().replace("@", ""));
								iFams.setSpouseOfFamilyIDs(spouseOfFamilyIds);
								individuals.put(lastId, iFams);
								break;
							
							case GEDCOMTag.NAME_HUSB:
								Family fHusband = families.get(lastId);
								fHusband.setHusbandId(tag.getArgument());
								families.put(lastId, fHusband);
								break;
							
							case GEDCOMTag.NAME_WIFE:
								Family fWife = families.get(lastId);
								fWife.setWifeId(tag.getArgument());
								families.put(lastId, fWife);
								break;
								
							case GEDCOMTag.NAME_CHIL:
								Family fChild = families.get(lastId);
								fChild.setChildId(tag.getArgument());
								families.put(lastId, fChild);
								break;
						}
						
						break;
						
					case GEDCOMTag.LEVEL_2: //currently only DATE exists as valid tag name
						
						try
						{
							switch(lastTagName)
							{
								case GEDCOMTag.NAME_BIRT:
									Individual iBirt = individuals.get(lastId);
									iBirt.setBirthday(new SimpleDateFormat("d MMM yyyy").parse(tag.getArgument()));
									individuals.put(lastId,  iBirt);
									break;
								
								case GEDCOMTag.NAME_DEAT:
									Individual iDeat = individuals.get(lastId);
									iDeat.setDeath(new SimpleDateFormat("d MMM yyyy").parse(tag.getArgument()));
									individuals.put(lastId,  iDeat);
									break;
								
								case GEDCOMTag.NAME_DIV:
									Family fDiv = families.get(lastId);
									fDiv.setDivorced(new SimpleDateFormat("d MMM yyyy").parse(tag.getArgument()));
									families.put(lastId,  fDiv);
									break;
								
								case GEDCOMTag.NAME_MARR:
									Family fMarr = families.get(lastId);
									fMarr.setMarried(new SimpleDateFormat("d MMM yyyy").parse(tag.getArgument()));
									families.put(lastId,  fMarr);
									break;
							}
						}
						catch (ParseException e)
						{
							
						}
						
						break;
				}
			}
			catch (IllegalArgumentException e)
			{
				//tag is invalid
				//System.out.println(e.getMessage());
			}
			
			tagArgument = "";
		}
	}
	
	public Boolean LineIsValid (String sLine) {
		Matcher mMatcher = linePattern.matcher(sLine);
		
		if (mMatcher.find()) {
			return true;
		}
		
		return false;
	}
	
	public GEDCOMELine ParseLine (String line) {
		
		GEDCOMELine gLine = new GEDCOMELine();
		Matcher levelMatcher = levelNumPattern.matcher(line);
		Matcher tagMatcher = tagPattern.matcher(line);
		//Matcher tagMatcher = validTagPattern.matcher(line);
		Matcher argMatcher = argPattern.matcher(line);
		
		
		if (levelMatcher.find ()) {
			String sLevelNumber = new String (levelMatcher.group(1).trim());
			int iLevelNumber = Integer.parseInt( sLevelNumber );
			gLine.setLevelNumber( iLevelNumber );
		}
		
		if (tagMatcher.find()) {
			String sTag = new String(tagMatcher.group(1).trim());
			gLine.setTag(sTag);
		} else {
			gLine.setTag ("Invalid Tag");
		}
		
		if (argMatcher.find()) {
			String sArg = new String(argMatcher.group(1).trim());
			gLine.setArgs(sArg);
		}
		
		return gLine;
		
	}
	
	public TreeMap<String, Individual> getIndividuals () {
		return individuals;
	}
	
	public TreeMap<String, Family> getFamilies () {
		return families;
	}
}
