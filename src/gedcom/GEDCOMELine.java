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



	public int getLevelNumber() {
		return mLevelNumber;
	}
	
	public String getLevelNumberAsString () {
		return Integer.toString(mLevelNumber);
	}

	public void setLevelNumber(int mLevelNumber) {
		this.mLevelNumber = mLevelNumber;
	}

	public String getTag() {
		return mTag;
	}

	public void setTag(String mTag) {
		this.mTag = mTag;
	}

	public String getArgs() {
		return mArgs;
	}

	public void setArgs(String mArgs) {
		this.mArgs = mArgs;
	}
}
