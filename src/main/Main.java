/**
 * 
 */
package main;

import java.io.IOException;

import gedcom.GEDCOMParser;

/**
 * @author samato
 *
 */
public class Main {
	
	public static String filePath = null;
	
	public static void usage () {
		System.out.println("Usage for GEDCOM Reader");
		
		System.out.println ("GEDCOMReader [-f|--file][-h|--help]");
		
		System.exit(0);
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		for (int i = 0; i < args.length ; i++) {
			if (args[i].equals("-f")
					|| args[i].equals("--file"))
			{
				if (args.length < ++i) {
					usage();
				}
				
				filePath = args[i];
				
			} else if (args[i].equals("-h")
					|| args[i].equals("--help"))
			{
				usage();
			} else {
				usage();
			}
		}
		
		GEDCOMParser gParser = new GEDCOMParser();
		
		try {
			gParser.ParseFile (filePath);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Line # | Level # |     Tag      | Arguments ");
		
		for (int idx = 0 ; idx < gParser.mGLines.size() ; idx++) {
			System.out.printf( "  %d\t%6s\t    %s    \t   %s\n",
					idx, 
					gParser.mGLines.elementAt(idx).getmLevelNumber(),
					gParser.mGLines.elementAt(idx).getmTag().trim (),
					gParser.mGLines.elementAt(idx).getmArgs().trim());
		}
		
	}

}
