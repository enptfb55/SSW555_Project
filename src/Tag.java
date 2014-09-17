import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Tag
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
		
		VALID_TAGS.put(LEVEL_0, Arrays.asList(Tag.NAME_INDI
											, Tag.NAME_FAM));
		
		VALID_TAGS.put(LEVEL_1, Arrays.asList(Tag.NAME_NAME, Tag.NAME_SEX, Tag.NAME_BIRT, Tag.NAME_DEAT, Tag.NAME_FAMC, Tag.NAME_FAMS, Tag.NAME_MARR 
											, Tag.NAME_HUSB, Tag.NAME_WIFE, Tag.NAME_CHIL, Tag.NAME_DIV, Tag.NAME_TRLR, Tag.NAME_NOTE));
		
		VALID_TAGS.put(LEVEL_2, Arrays.asList(Tag.NAME_DATE));
	}
	
	private String Level;
	private String Name;
	private String Argument;
	
	public static boolean isLevelValid(String level)
	{
		return Tag.VALID_TAGS.containsKey(level);
	}
	
	public static boolean isNameValid(String level, String tag)
	{
		return isLevelValid(level) ? Tag.VALID_TAGS.get(level).contains(tag) : false;
	}
	
	public Tag(String level, String name, String argument) throws IllegalArgumentException
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
		if(!Tag.isLevelValid(level))
		{
			//perhaps create IllegalTagLevelException?
			throw new IllegalArgumentException("Specified tag level is invalid");
		}
		
		Level = level;
	}
	
	public String getName()
	{
		return Name;
	}
	
	public void setName(String name)
	{
		if(!Tag.isNameValid(this.Level, name))
		{
			//perhaps create IllegalTagNameException?
			throw new IllegalArgumentException("Specified tag name is invalid for the given tag level");
		}
		
		Name = name;
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
