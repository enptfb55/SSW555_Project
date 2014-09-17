/**
 * 
 */
package gedcom;

/**
 * @author samato
 *
 */
public class GEDCOMELine {

	private int mLevelNumber = 0;
	private String mTag = "";
	private String mArgs = "";
	
	/**
	 * 
	 */
	public GEDCOMELine() {
		// TODO Auto-generated constructor stub
	}



	public int getmLevelNumber() {
		return mLevelNumber;
	}

	public void setmLevelNumber(int mLevelNumber) {
		this.mLevelNumber = mLevelNumber;
	}

	public String getmTag() {
		return mTag;
	}

	public void setmTag(String mTag) {
		this.mTag = mTag;
	}

	public String getmArgs() {
		return mArgs;
	}

	public void setmArgs(String mArgs) {
		this.mArgs = mArgs;
	}
}
