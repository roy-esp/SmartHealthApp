package com.ianroycreations.smarthhealthapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

public class CSVmanager {
	private File csvFile;
	
	public CSVmanager(File csvFile) {
		this.csvFile=csvFile;
	}

	public void csvToArff(File arffFile) throws Exception{
		// load CSV
	    CSVLoader loader = new CSVLoader();
	    loader.setSource(csvFile);
	    Instances data = loader.getDataSet();
	    data.setClassIndex(48);
	 
	    // save ARFF
	    ArffSaver saver = new ArffSaver();
	    saver.setInstances(data);
	    saver.setFile(arffFile);
	    saver.writeBatch();
	}
	
	public ArrayList<String> csvLabelToArray() throws Exception{
		ArrayList<String> lines = new ArrayList<>();
		Scanner inputStream;
		
		
		inputStream = new Scanner(csvFile);
		
		while(inputStream.hasNext()){
		   
		    lines.add(inputStream.next());
		}
		
		inputStream.close();
		
		return lines;
	}
	
	public ArrayList<String[]> csvSensorToArray() throws Exception{
		ArrayList<String[]> lines = new ArrayList<>();
        Scanner inputStream;
        
        inputStream = new Scanner(csvFile);

        while(inputStream.hasNext()){
            String line= inputStream.next();
            //Next array is critical cause is splitting as I want?
            String[] values = line.split(",");
            // this adds the currently parsed line to the 2-dimensional string array
            lines.add(values);
        }
		
		inputStream.close();
		
		return lines;
	}
	
	public void merge(File secondFile,String path) throws FileNotFoundException {
		PrintWriter writer = new PrintWriter(path);
		Scanner inputStream = new Scanner(csvFile);
		Scanner inputStream2 = new Scanner(secondFile);

        while(inputStream.hasNext()&&inputStream2.hasNext()){
            String line= inputStream.next();
            String line2=inputStream2.next();
            String outputLine=line+","+line2;
            writer.println(outputLine);
        }
        writer.close();
        inputStream.close();
        inputStream2.close();
	}

}
