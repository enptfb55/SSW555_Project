package main;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Individual
{
	private String id;
	private String name;
	private char sex;
	private Date birthday;
	private Date death;
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
		return id;
	}

	public void setId(String identifier)
	{
		id = identifier;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String n)
	{
		name = n.replace("/", "");
	}	
	
	public Date getBirthday()
	{
		return birthday;
	}
	
	public void setBirthday(Date b) 
	{
		birthday = b;
	}
	
	public Date getDeath()
	{
		return death;
	}
	
	public void setDeath(Date d)
	{
		death = d;
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
		return sex;
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

	public void setSex(char c)
	{
		sex = c;
	}
	
	
}
