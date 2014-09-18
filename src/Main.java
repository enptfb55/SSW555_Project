import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JFileChooser;

import javafx.stage.FileChooser;

public class Main
{
	public static void main(String[] args)
	{
		BufferedReader br;
		TreeMap<String, Individual> individuals = new TreeMap<String, Individual>();
		TreeMap<String, Family> families = new TreeMap<String, Family>();
		
		JFileChooser jc = new JFileChooser();
		
		int choice = jc.showOpenDialog(jc);
		
		if (choice != JFileChooser.APPROVE_OPTION) return;

		File chosenFile = jc.getSelectedFile();
		
		try
		{
			br = new BufferedReader(new FileReader(chosenFile.getPath()));
			
			

			//should really do this parsing logic somewhere else
			String line, level, lastTopLevelTagName, tagName, id, lastId = "", tagArgument = "", lastTagName = "";
			int secondSpaceIndex;
			Tag tag;
			
			while ((line = br.readLine()) != null)
			{
				level = line.substring(0, 1);
				secondSpaceIndex = line.indexOf(" ", 2);
				
				if(level.equals(Tag.LEVEL_0))
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
					tag = new Tag(level, tagName, tagArgument);
					
					switch (tag.getLevel())
					{
						case Tag.LEVEL_0:
							lastTopLevelTagName = tag.getName();
							
							id = tag.getArgument().replace("@", ""); //should probably move this to setId methods
							lastId = id;

							switch(lastTopLevelTagName)
							{
								case Tag.NAME_INDI:
									individuals.put(id, new Individual(id));
									break;
								
								case Tag.NAME_FAM:
									families.put(id, new Family(id));
									break;
							}
							
							break;
						
						
						case Tag.LEVEL_1:
							lastTagName = tag.getName();
							
							switch(tag.getName())
							{
								case Tag.NAME_NAME:
									Individual iName = individuals.get(lastId);
									iName.setName(tag.getArgument());
									individuals.put(lastId, iName);
									break;
									
								case Tag.NAME_SEX:
									Individual iSex = individuals.get(lastId);
									iSex.setSex(tag.getArgument().charAt(0));
									individuals.put(lastId, iSex);
									break;
									
								case Tag.NAME_FAMC:
									Individual iFamc = individuals.get(lastId);
									List<String> childOfFamilyIds = iFamc.getChildOfFamilyIDs();
									childOfFamilyIds.add(tag.getArgument().replace("@", ""));
									iFamc.setChildOfFamilyIDs(childOfFamilyIds);
									individuals.put(lastId, iFamc);
									break;
								
								case Tag.NAME_FAMS:
									Individual iFams = individuals.get(lastId);
									List<String> spouseOfFamilyIds = iFams.getSpouseOfFamilyIDs();
									spouseOfFamilyIds.add(tag.getArgument().replace("@", ""));
									iFams.setSpouseOfFamilyIDs(spouseOfFamilyIds);
									individuals.put(lastId, iFams);
									break;
								
								case Tag.NAME_HUSB:
									Family fHusband = families.get(lastId);
									fHusband.setHusbandId(tag.getArgument());
									families.put(lastId, fHusband);
									break;
								
								case Tag.NAME_WIFE:
									Family fWife = families.get(lastId);
									fWife.setWifeId(tag.getArgument());
									families.put(lastId, fWife);
									break;
									
								case Tag.NAME_CHIL:
									Family fChild = families.get(lastId);
									fChild.setChildId(tag.getArgument());
									families.put(lastId, fChild);
									break;
							}
							
							break;
							
						case Tag.LEVEL_2: //currently only DATE exists as valid tag name
							
							try
							{
								switch(lastTagName)
								{
									case Tag.NAME_BIRT:
										Individual iBirt = individuals.get(lastId);
										iBirt.setBirthday(new SimpleDateFormat("d MMM yyyy").parse(tag.getArgument()));
										individuals.put(lastId,  iBirt);
										break;
									
									case Tag.NAME_DEAT:
										Individual iDeat = individuals.get(lastId);
										iDeat.setDeath(new SimpleDateFormat("d MMM yyyy").parse(tag.getArgument()));
										individuals.put(lastId,  iDeat);
										break;
									
									case Tag.NAME_DIV:
										Family fDiv = families.get(lastId);
										fDiv.setDivorced(new SimpleDateFormat("d MMM yyyy").parse(tag.getArgument()));
										families.put(lastId,  fDiv);
										break;
									
									case Tag.NAME_MARR:
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
			
			br.close();
			
			System.out.println("Individuals - ");
			
			for(Map.Entry<String, Individual> entry : individuals.entrySet())
			{
				System.out.println("Id:" + entry.getValue().getId() + "\tName: " + entry.getValue().getName());
				//System.out.println(entry.getValue().toString());
			}
			
			System.out.println("");
			System.out.println("Families - ");
			
			for(Map.Entry<String, Family> entry : families.entrySet())
			{
				Family family = entry.getValue();

				System.out.println("Id:\t\t" + family.getId());
				System.out.println("Husband Name:\t" + individuals.get(family.getHusbandId()).getName());
				System.out.println("Wife Name:\t" + individuals.get(family.getWifeId()).getName());
				//System.out.println("\tChild Name:\t" + individuals.get(family.getChildId()).getName());
				System.out.println("");
				
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
