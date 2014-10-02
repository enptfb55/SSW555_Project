package gedcom;

public class GEDCOMError
{
	private String message;

	public GEDCOMError(String m)
	{
		this.setMessage(m);
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String m) {
		message = m;
	}
}
