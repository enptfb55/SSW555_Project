package main;
import gedcom.GEDCOMError;
import gedcom.GEDCOMParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
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

	
	public static void main(String[] args)
	{	
		File inputFile = null;
		File outputFile = null;
		File inputDir = null;
		File outputDir = null;
		
		mOptions.addOption("help", false, "prints this help message");
		mOptions.addOption("file", true, "input file path");
		mOptions.addOption("out", true, "output file path");
		mOptions.addOption("in_dir", true, "input dir file path");
		mOptions.addOption("out_dir", true, "input dir file path");
		
		CommandLineParser parser = new BasicParser();
		
		try {
			CommandLine cmd = parser.parse( mOptions, args);
			
			if (cmd.hasOption("help")) {
				usage();
			}
			
			if (cmd.hasOption("file")) {
				File tmpFile = new File(cmd.getOptionValue("file"));
				if (tmpFile.exists()) {
					inputFile = tmpFile;
				}
			}
			
			if (cmd.hasOption("out")) {
				outputFile = new File (cmd.getOptionValue("out"));
			}
			
			if (cmd.hasOption("in_dir")) {
				inputDir = new File (cmd.getOptionValue("in_dir"));
			}
			
			if (cmd.hasOption("out_dir")) {
				outputDir = new File (cmd.getOptionValue("out_dir"));
			}
				
			
			
		} catch (org.apache.commons.cli.ParseException e1) {
			System.err.println( "Parsing failed.  Reason: " + e1.getMessage() );
		}
		
		if (inputDir != null && 
			inputDir.isDirectory() ) 
		{
			File [] test = inputDir.listFiles();
			
			for (File file : inputDir.listFiles()) {
				File outFile = null;
				if (outputDir != null && outputDir.isDirectory()) {
					outFile = new File (outputDir.getPath() + '/' + file.getName().replace(".ged", ".txt") );
				}
				
				run (file, outFile);
			}	
			
			return;
			
		} else if (inputFile == null) {
			JFileChooser jc = new JFileChooser("data");
		
			jc.setFileFilter(new FileNameExtensionFilter("GEDCOM files", "ged"));
		
			int choice = jc.showOpenDialog(jc);
		
			if (choice != JFileChooser.APPROVE_OPTION) return;

			inputFile = jc.getSelectedFile();
		}
		
		
		run (inputFile, outputFile);
		
		
	}
	
	private static void run (File inputFile, File outputFile) {
		PrintWriter pwOutput = null;
		
		
		
		if (outputFile != null) {
			try {
				
				Boolean flag = outputFile.createNewFile();
				
				pwOutput = new PrintWriter(outputFile);
			} catch (FileNotFoundException e) {
				System.err.println( "Unable to create output file.  Reason: " + e.getMessage() );
			} catch (IOException e) {
				System.err.println( "Unable to create output file.  Reason: " + e.getMessage() );
			}
		}
		
		try
		{
			GEDCOMParser gParser = new GEDCOMParser (inputFile);
			
			String outputText = "";
			
			outputText = "Individuals - \n";
						
			for (Map.Entry<String, Individual> entry : gParser.getIndividuals().entrySet())
			{
				outputText += entry.getValue().toString() + "\n";
			}
			
			outputText += "\nFamilies - \n";
			
			for(Map.Entry<String, Family> entry : gParser.getFamilies().entrySet())
			{
				outputText += entry.getValue().toString()+ "\n";
			}
			
			LinkedList<GEDCOMError> errors = gParser.getErrors();
			
			outputText += "\nIssues detected - \n";
			
			if(errors.size() == 0)
			{
				outputText += "None found";
			}
			else
			{
				for(int i = 0; i < errors.size(); i++)
				{
					outputText += (i+1) + ". " + errors.get(i).getMessage() + "\n";
				}
			}
			
			System.out.print(outputText);
			
			if (pwOutput != null) {
				pwOutput.print(outputText);
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
