package gedcom;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class GEDCOMTag
{
	public static final String LEVEL_0 = "0";
	public static final String LEVEL_1 = "1";
	public static final String LEVEL_2 = "2";
	
	public static final String NAME_INDI = "INDI";
	public static final String NAME_NAME = "NAME";
	public static final String NAME_SEX  = "SEX";
	public static final String NAME_BIRT = "BIRT";
	public static final String NAME_DEAT = "DEAT";
	public static final String NAME_FAMC = "FAMC";
	public static final String NAME_FAMS = "FAMS";
	public static final String NAME_FAM  = "FAM";
	public static final String NAME_MARR = "MARR";
	public static final String NAME_HUSB = "HUSB";
	public static final String NAME_WIFE = "WIFE";
	public static final String NAME_CHIL = "CHIL";
	public static final String NAME_DIV  = "DIV";
	public static final String NAME_DATE = "DATE";
	public static final String NAME_TRLR = "TRLR";
	public static final String NAME_NOTE = "NOTE";
	
	public static final HashMap<String, List<String>> VALID_TAGS;
	static
	{
		VALID_TAGS = new HashMap<String, List<String>>();
		
		VALID_TAGS.put(LEVEL_0, Arrays.asList(GEDCOMTag.NAME_INDI
											, GEDCOMTag.NAME_FAM));
		
		VALID_TAGS.put(LEVEL_1, Arrays.asList(GEDCOMTag.NAME_NAME, GEDCOMTag.NAME_SEX, GEDCOMTag.NAME_BIRT, GEDCOMTag.NAME_DEAT, GEDCOMTag.NAME_FAMC, GEDCOMTag.NAME_FAMS, GEDCOMTag.NAME_MARR 
											, GEDCOMTag.NAME_HUSB, GEDCOMTag.NAME_WIFE, GEDCOMTag.NAME_CHIL, GEDCOMTag.NAME_DIV, GEDCOMTag.NAME_TRLR, GEDCOMTag.NAME_NOTE));
		
		VALID_TAGS.put(LEVEL_2, Arrays.asList(GEDCOMTag.NAME_DATE));
	}
	
	private String Level;
	private String Name;
	private String Argument;
	
	public static boolean isLevelValid(String level)
	{
		return GEDCOMTag.VALID_TAGS.containsKey(level);
	}
	
	public static boolean isNameValid(String level, String tag)
	{
		return isLevelValid(level) ? GEDCOMTag.VALID_TAGS.get(level).contains(tag) : false;
	}
	
	public GEDCOMTag(String level, String name, String argument) throws IllegalArgumentException
	{
		this.setLevel(level);
		this.setName(name);
		this.setArgument(argument);
	}
	
	public String getLevel()
	{
		return Level;
	}
	
	public void setLevel(String level)
	{
		Level = level;

		if(!GEDCOMTag.isLevelValid(level))
		{
			throw new IllegalArgumentException("Specified tag level of " + level + " is invalid");
		}
	}
	
	public String getName()
	{
		return Name;
	}
	
	public void setName(String name)
	{
		Name = name;
		
		if(!GEDCOMTag.isNameValid(this.Level, name))
		{
			throw new IllegalArgumentException("Specified tag name of "+ name + " is invalid for the given tag level of " + name);
		}
	}
	
	public String getArgument()
	{
		return Argument;
	}
	
	public void setArgument(String argument)
	{
		Argument = argument;
	}	
}
