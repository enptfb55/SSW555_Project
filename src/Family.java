import java.util.Date;

public class Family
{
	private String Id;
	private Date Married;
	private Date Divorced;
	private String HusbandId;
	private String WifeId;
	private String ChildId;

	public Family(String identifier)
	{
		this.setId(identifier);
	}
	
	public String getId() {
		return Id;
	}

	public void setId(String id) {
		if(id.isEmpty())
		{
			throw new IllegalArgumentException("A family must have an id");
		}
		
		Id = id.replace("@", "");
	}

	public Date getMarried() {
		return Married;
	}

	public void setMarried(Date married) {
		Married = married;
	}

	public String getHusbandId() {
		return HusbandId;
	}

	public void setHusbandId(String husbandId) {
		HusbandId = husbandId.replace("@", "");
	}

	public String getWifeId() {
		return WifeId;
	}

	public void setWifeId(String wifeId) {
		WifeId = wifeId.replace("@", "");
	}

	public String getChildId() {
		return ChildId;
	}

	public void setChildId(String childId) {
		ChildId = childId.replace("@", "");
	}

	public Date getDivorced() {
		return Divorced;
	}

	public void setDivorced(Date divorced) {
		Divorced = divorced;
	}
}
