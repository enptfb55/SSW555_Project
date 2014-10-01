package main;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import utils.DiffDates;

public class Family
{
	private String Id;
	private Date Married;
	private Date Divorced;
	private Individual Husband;
	private Individual Wife;
	private TreeMap <String, Individual> Children = new TreeMap <String, Individual>();

	public Family(String identifier)
	{
		this.setId(identifier);
	}
	
	public String getId() {
		return Id;
	}

	public void setId(String id)
	{
		Id = Individual.ParseIdFromString(id);
		
		if(id.isEmpty())
		{
			throw new IllegalArgumentException("A family must have an id");
		}
	}

	public Date getMarried()
	{
		return Married;
	}

	public void setMarried(Date married)
	{
		Married = married;
	}

	public Individual getHusband()
	{
		return Husband;
	}

	public void setHusband(Individual husband)
	{
		Husband = husband;
		
		if(husband.getSex() != Individual.SEX_MALE && husband.getSex() != '\u0000')
		{
			throw new IllegalArgumentException("Husband with id " + husband.getId() + " may only be of male sex type for family with id " + this.getId());
		}
		else if(husband.getDeath() != null) //should perhaps thrown a different exception as to catch the exact error type instead of determine from message
		{
			throw new IllegalArgumentException("Husband with id " + husband.getId() + " is dead and may not be married in family with id " + this.getId());
		} 
		else if (husband.getBirthday() != null && 
				Married != null)
		{
			DiffDates diff = new DiffDates (Married, husband.getBirthday());
			
			if (diff.Years() < 18) {
				throw new IllegalArgumentException("Husband with id " + husband.getId() + " is younger than 18 and may not be married in family with id " + this.getId());
			}
			
		}
		
		
		if (Children.size() > 0)
		{
			for (Map.Entry<String, Individual> childEntry : Children.entrySet()) {
				Individual child = childEntry.getValue();
				
				if (husband.getId() == child.getId()) {
					throw new IllegalArgumentException("Husband with id " + husband.getId() + " is also a child in family with id " + this.getId());
				}
			}
		}
	}

	public Individual getWife()
	{
		return Wife;
	}

	public void setWife(Individual wife)
	{
		Wife = wife;
		
		if(wife.getSex() != Individual.SEX_FEMALE && wife.getSex() != '\u0000')
		{
			throw new IllegalArgumentException("Wife with id " + wife.getId() + " may only be of female sex type for family with id " + this.getId());
		}
		else if(wife.getDeath() != null) //should perhaps thrown a different exception as to catch the exact error type instead of determine from message
		{
			throw new IllegalArgumentException("Wife with id " + wife.getId() + " is dead and may not be married in family with id " + this.getId());
		}
		else if (Children.size() > 0)
		{
			for (Map.Entry<String, Individual> childEntry : Children.entrySet()) {
				Individual child = childEntry.getValue();
				
				if (wife.getId() == child.getId()) {
					throw new IllegalArgumentException("Husband with id " + wife.getId() + " is also a child in family with id " + this.getId());
				}
			}
		}
	}

	public Individual getChild(String childName)
	{
		return Children.get(childName);
	}

	public void addChild(Individual child) 
	{
		
		if (child.getBirthday() != null &&
				Husband != null &&
				Wife != null) 
		{
			
			if (Husband.getDeath() != null &&
				Wife.getDeath() != null &&
				child.getBirthday().after(Husband.getDeath()) && 
				child.getBirthday().after(Wife.getDeath()))
			{
				throw new IllegalArgumentException("Child with id " + child.getId() + " cannot be born after both parents die");
				
			}
			else if ((Husband.getBirthday() != null && 
						child.getBirthday().before(Husband.getBirthday()) ||
					 (Wife.getBirthday() != null &&
						child.getBirthday().before(Wife.getBirthday()))))
			{
				throw new IllegalArgumentException("Child with id " + child.getId() + " cannot be born before either parent");
			}
			
		
		}
		
		
		Children.put(child.getName(), child);
	}
	
	public TreeMap <String, Individual> getChildren ()
	{
		return Children;
	}

	public Date getDivorced() 
	{
		return Divorced;
	}

	public void setDivorced(Date divorced) 
	{
		Divorced = divorced;
	}
}
