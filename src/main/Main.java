package main;
import gedcom.GEDCOMParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

public class Main
{
	static Options mOptions = new Options();
	static File mInputFile = null;
	static File mOutputFile = null;
	
	public static void main(String[] args)
	{	
		
		mOptions.addOption("help", false, "prints this help message");
		mOptions.addOption("file", true, "input file path");
		mOptions.addOption("out", true, "output file path");
		
		CommandLineParser parser = new BasicParser();
		
		try {
			CommandLine cmd = parser.parse( mOptions, args);
			
			if (cmd.hasOption("help")) {
				usage();
			}
			
			if (cmd.hasOption("file")) {
				File tmpFile = new File(cmd.getOptionValue("file"));
				if (tmpFile.exists()) {
					mInputFile = tmpFile;
				}
			}
			
			if (cmd.hasOption("out")) {
				mOutputFile = new File (cmd.getOptionValue("out"));
			}
			
			
		} catch (org.apache.commons.cli.ParseException e1) {
			System.err.println( "Parsing failed.  Reason: " + e1.getMessage() );
		}
		
		
		if (mInputFile == null) {
			JFileChooser jc = new JFileChooser("data");
		
			jc.setFileFilter(new FileNameExtensionFilter("GEDCOM files", "ged"));
		
			int choice = jc.showOpenDialog(jc);
		
			if (choice != JFileChooser.APPROVE_OPTION) return;

			mInputFile = jc.getSelectedFile();
		}
		
		PrintWriter pwOutput = null;
		
		if (mOutputFile != null) {
			try {
				
				Boolean flag = mOutputFile.createNewFile();
				
				pwOutput = new PrintWriter(mOutputFile);
			} catch (FileNotFoundException e) {
				System.err.println( "Unable to create output file.  Reason: " + e.getMessage() );
			} catch (IOException e) {
				System.err.println( "Unable to create output file.  Reason: " + e.getMessage() );
			}
		}
		
		try
		{
			GEDCOMParser gParser = new GEDCOMParser (mInputFile);
			
			
			
			System.out.println("Individuals - ");
			if (pwOutput != null) {
				pwOutput.println("Individuals - ");
			}
			
			for (Map.Entry<String, Individual> entry : gParser.getIndividuals().entrySet())
			{
				System.out.println("Id:" + entry.getValue().getId() + "\tName: " + entry.getValue().getName());
				if (pwOutput != null) {
					pwOutput.println("Id:" + entry.getValue().getId() + "\tName: " + entry.getValue().getName());
				}
					
				//System.out.println(entry.getValue().toString());
			}
			
			System.out.println("");
			System.out.println("Families - ");
			
			if (pwOutput != null) {
				pwOutput.println("");
				pwOutput.println("Families - ");
			}
			
			for(Map.Entry<String, Family> entry : gParser.getFamilies().entrySet())
			{
				Family family = entry.getValue();

				System.out.println("Id:\t\t" + family.getId());
				System.out.println("Husband Name:\t" + gParser.getIndividuals().get(family.getHusbandId()).getName());
				System.out.println("Wife Name:\t" + gParser.getIndividuals().get(family.getWifeId()).getName());
				//System.out.println("\tChild Name:\t" + individuals.get(family.getChildId()).getName());
				System.out.println("");
				
				if (pwOutput != null) {
					pwOutput.println("Id:\t\t" + family.getId());
					pwOutput.println("Husband Name:\t" + gParser.getIndividuals().get(family.getHusbandId()).getName());
					pwOutput.println("Wife Name:\t" + gParser.getIndividuals().get(family.getWifeId()).getName());
					//System.out.println("\tChild Name:\t" + individuals.get(family.getChildId()).getName());
					pwOutput.println("");
				}
				
			}
			
			if (pwOutput != null) {
				pwOutput.close();
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private static void usage () {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp( "GEDCOMParser", mOptions );
		System.exit(0);
	}
	
}
