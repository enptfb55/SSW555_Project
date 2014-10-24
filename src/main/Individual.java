package main;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

public class Individual
{
	private String id;
	private String name;
	private char sex;
	private Date birthday;
	private Date death;
	private List<String> childOfFamilyIDs;
	private List<String> spouseOfFamilyIDs;
	private int numDeathDates;

	public Individual(String identifier)
	{
		this.setId(identifier);
		this.setBirthday(null);
		this.setDeath(null);
		this.setChildOfFamilyIDs(new LinkedList<String>());
		this.setSpouseOfFamilyIDs(new LinkedList<String>());
		this.setNumDeathDates(0);
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
		if(d != null)
		{
			if(this.getDeath() == null || !this.getDeath().equals(d))
				this.setNumDeathDates(this.getNumDeathDates() + 1);
		}
		
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
	
	public ArrayList<String> getAllSpousesIDs(TreeMap<String, Family> family)
	{
		ArrayList<String> spouses = new ArrayList<String>();
		
		Iterator<String> i = getSpouseOfFamilyIDs().iterator();
	
		while(i.hasNext())
		{
			String s = i.next();
			if(family.containsKey(s))
			{
				if(family.get(s).getHusband()!=null && family.get(s).getWife()!=null) {
		
					
				if(family.get(s).getHusband().getId().equals(id))
					spouses.add(family.get(s).getWife().getId());
				if(family.get(s).getWife().getId().equals(id))
					spouses.add(family.get(s).getHusband().getId());
				}
			}			
		}
		
		return spouses;
	}

	public int getNumDeathDates() {
		return numDeathDates;
	}

	public void setNumDeathDates(int numDeathDates) {
		this.numDeathDates = numDeathDates;
	}
	
}
