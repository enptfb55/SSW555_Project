package main;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Individual
{
	private static final Date DATE_EMPTY = new Date(Long.MIN_VALUE); 
	
	private String Id;
	private String Name;
	private char Sex;
	private Date Birthday;
	private Date Death;
	private List<String> childOfFamilyIDs;
	private List<String> spouseOfFamilyIDs;

	public Individual(String identifier)
	{
		this.setId(identifier);
		this.setBirthday(DATE_EMPTY);
		this.setDeath(DATE_EMPTY);
		this.setChildOfFamilyIDs(new LinkedList<String>());
		this.setSpouseOfFamilyIDs(new LinkedList<String>());
	}
	
	public static String ParseIdFromString(String s)
	{
		return s.replace("@", "");
	}
	
	public String getId()
	{
		return Id;
	}

	public void setId(String id)
	{
		if(id.isEmpty())
		{
			throw new IllegalArgumentException("An individual must have an id");
		}
		
		Id = id;
	}
	
	public String getName()
	{
		return Name;
	}
	
	public void setName(String name)
	{
		if(name.isEmpty())
		{
			throw new IllegalArgumentException("An individual must have a name");
		}

		Name = name.replace("/", "");
	}	
	
	public Date getBirthday()
	{
		return Birthday;
	}
	
	public void setBirthday(Date birthday) 
	{
		Birthday = birthday;
	}
	
	public Date getDeath() {
		return Death;
	}
	
	public void setDeath(Date death) {
		Death = death;
	}
	
	public List<String> getChildOfFamilyIDs()
	{
		return childOfFamilyIDs;
	}
	
	public void setChildOfFamilyIDs(List<String> childOfFamilyIDs)
	{
		this.childOfFamilyIDs = childOfFamilyIDs;
	}
	
	public List<String> getSpouseOfFamilyIDs()
	{
		return spouseOfFamilyIDs;
	}
	
	public void setSpouseOfFamilyIDs(List<String> spouseOfFamilyIDs)
	{
		this.spouseOfFamilyIDs = spouseOfFamilyIDs;
	}

	public char getSex() {
		return Sex;
	}
	
	public String toString()
	{
		return "Id:\t\t\t" + this.getId()
				+ "\nName:\t\t\t" + this.getName()
				+ "\nSex:\t\t\t" + this.getSex()
				+ "\nBirthday:\t\t" + (this.getBirthday() == DATE_EMPTY ? "" : new SimpleDateFormat("d MMM yyyy").format(this.getBirthday()))
				+ "\nDeath:\t\t\t" + (this.getDeath() == DATE_EMPTY ? "" : new SimpleDateFormat("d MMM yyyy").format(this.getDeath()))
				+ "\nChild of Families:\t" + this.getChildOfFamilyIDs().toString().replace("[", "").replace("]", "")
				+ "\nSpouse of Families:\t" + this.getSpouseOfFamilyIDs().toString().replace("[", "").replace("]", "")
				+ "\n";
	}

	public void setSex(char sex) {
		//should probably make these static finals or perhaps create a sex-like enum/class
		if(sex != 'M' && sex != 'F')
		{
			throw new IllegalArgumentException("The specified individual sex is invalid -- M and F only.");
		}
		
		Sex = sex;
	}
	
	
}
