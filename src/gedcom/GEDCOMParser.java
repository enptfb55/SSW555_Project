/**
 * @file GEDCOMParser.java
 */

package gedcom;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @class GEDCOMParser
 * @author samato
 *
 */
public class GEDCOMParser {
	
	static final int flags = Pattern.CASE_INSENSITIVE | Pattern.MULTILINE;
	static final String validTags = "INDI|NAME|SEX|BIRT|DEAT|" +
						"FAMC|FAMS|FAM|MARR|HUSB|WIFE|" +
					    "CHIL|DIV|DATE|TRLR|NOTE";
	
	//static final Pattern tagPattern = Pattern.compile( "([\\w|\\@]+)", flags);
	static final Pattern linePattern = Pattern.compile ("(\\s?\\d\\s[\\w|\\@]+\\s*)", flags);
	static final Pattern levelNumPattern = Pattern.compile("^\\s?(\\d)\\s*");
	static final Pattern tagPattern = Pattern.compile("\\s?\\d\\s([\\w|\\@]+)\\s*");
	static final Pattern validTagPattern = Pattern.compile("\\s?\\d\\s(" + validTags + ")\\s*");
	static final Pattern argPattern = Pattern.compile("\\s?\\d\\s[\\w|\\@]+\\s+(.*)");
	
	public Vector<GEDCOMELine> mGLines = new Vector<GEDCOMELine>();
	

	/**
	 * @brief Constructor
	 */
	public GEDCOMParser () {
		
	}
	
	public void ParseFile (String sFilePath) throws IOException {
		FileReader frFileReader = new FileReader( sFilePath );
		BufferedReader brBufferedReader = new BufferedReader( frFileReader );
		
		ParseBuffer (brBufferedReader);
		
	}
	
	public void ParseBuffer (BufferedReader brBuffer) throws IOException {
		String sLine;
		
		while ((sLine = brBuffer.readLine()) != null) {
			
			if (LineIsValid (sLine)) {
				mGLines.add(ParseLine(sLine));
			}
			
		}
	}
	
	public Boolean LineIsValid (String sLine) {
		Matcher mMatcher = linePattern.matcher(sLine);
		
		if (mMatcher.find()) {
			return true;
		}
		
		return false;
	}
	
	public GEDCOMELine ParseLine (String line) {
		
		GEDCOMELine gLine = new GEDCOMELine();
		Matcher levelMatcher = levelNumPattern.matcher(line);
		Matcher tagMatcher = tagPattern.matcher(line);
		//Matcher tagMatcher = validTagPattern.matcher(line);
		Matcher argMatcher = argPattern.matcher(line);
		
		
		if (levelMatcher.find ()) {
			String sLevelNumber = new String (levelMatcher.group(1).trim());
			int iLevelNumber = Integer.parseInt( sLevelNumber );
			gLine.setLevelNumber( iLevelNumber );
		}
		
		if (tagMatcher.find()) {
			String sTag = new String(tagMatcher.group(1).trim());
			gLine.setTag(sTag);
		} else {
			gLine.setTag ("Invalid Tag");
		}
		
		if (argMatcher.find()) {
			String sArg = new String(argMatcher.group(1).trim());
			gLine.setArgs(sArg);
		}
		
		return gLine;
		
	}
}
