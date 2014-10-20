package main;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Family
{
	private String id;
	private Date married;
	private Date divorced;
	private Individual husband;
	private Individual wife;
	private Individual child;

	public Family(String identifier)
	{
		this.setId(identifier);
	}
	
	public String getId()
	{
		return id;
	}

	public void setId(String identifier)
	{
		id = Individual.ParseIdFromString(identifier);
	}

	public Date getMarried()
	{
		return married;
	}

	public void setMarried(Date d)
	{
		married = d;
	}

	public Individual getHusband()
	{
		return husband;
	}

	public void setHusband(Individual i)
	{
		husband = i;
	}

	public Individual getWife()
	{
		return wife;
	}

	public void setWife(Individual i)
	{
		wife = i;
	}

	public Individual getChild()
	{
		return child;
	}

	public void setChild(Individual i) 
	{
		child = i;
	}

	public Date getDivorced() 
	{
		return divorced;
	}

	public void setDivorced(Date d) 
	{
		divorced = d;
	}
	
	public String toString()
	{
		return "Id:\t\t" + this.getId()
				+ "\nHusband Name:\t" + (this.getHusband() != null ? this.getHusband().getName() : "")
				+ "\nWife Name:\t" + (this.getWife() != null ? this.getWife().getName() : "")
				+ "\nChild Name:\t" + (this.getChild() != null ? this.getChild().getName() : "")
				+ "\nMarried:\t" + (this.getMarried() == null ? "" : new SimpleDateFormat("d MMM yyyy").format(this.getMarried()))
				+ "\nDivorced:\t" + (this.getDivorced() == null ? "" : new SimpleDateFormat("d MMM yyyy").format(this.getDivorced()))
				+ "\n";
	}
}
