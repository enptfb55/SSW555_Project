package main;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Individual
{
	public final static char SEX_MALE = 'M';
	public final static char SEX_FEMALE = 'F';
	
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
		this.setBirthday(null);
		this.setDeath(null);
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
		Id = id;
		
		if(id.isEmpty())
		{
			throw new IllegalArgumentException("An individual must have an id");
		}
	}
	
	public String getName()
	{
		return Name;
	}
	
	public void setName(String name)
	{
		Name = name.replace("/", "");
		
		if(name.isEmpty())
		{
			throw new IllegalArgumentException("An individual must have a name");
		}
	}	
	
	public Date getBirthday()
	{
		return Birthday;
	}
	
	public void setBirthday(Date birthday) 
	{
		Birthday = birthday;
		
		if(this.getDeath() != null && birthday.after(this.getDeath()))
		{
			throw new IllegalArgumentException("Specified birth date of " 
												+ new SimpleDateFormat("d MMM yyyy").format(birthday) 
												+ " is after death date of " 
												+ new SimpleDateFormat("d MMM yyyy").format(this.getDeath()) 
												+ " for individual with id " + this.getId());
		}
	}
	
	public Date getDeath()
	{
		return Death;
	}
	
	public void setDeath(Date death)
	{
		Death = death;
		
		if(this.getBirthday() != null && death.before(this.getBirthday()))
		{
			throw new IllegalArgumentException("Specified death date of " 
												+ new SimpleDateFormat("d MMM yyyy").format(death) 
												+ " is before birth date of " 
												+ new SimpleDateFormat("d MMM yyyy").format(this.getBirthday()) 
												+ " for individual with id " + this.getId());
		}
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
				+ "\nBirthday:\t\t" + (this.getBirthday() == null ? "" : new SimpleDateFormat("d MMM yyyy").format(this.getBirthday()))
				+ "\nDeath:\t\t\t" + (this.getDeath() == null ? "" : new SimpleDateFormat("d MMM yyyy").format(this.getDeath()))
				+ "\nChild of Families:\t" + this.getChildOfFamilyIDs().toString().replace("[", "").replace("]", "")
				+ "\nSpouse of Families:\t" + this.getSpouseOfFamilyIDs().toString().replace("[", "").replace("]", "")
				+ "\n";
	}

	public void setSex(char sex)
	{
		Sex = sex;
		
		//should probably make these static finals or perhaps create a sex-like enum/class
		if(sex != SEX_MALE && sex != SEX_FEMALE)
		{ 
			throw new IllegalArgumentException("The specified individual sex of " + sex + " is invalid -- " + SEX_MALE + " and " + SEX_FEMALE + " only.");
		}
	}
	
	
}
