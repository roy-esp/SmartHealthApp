package com.ianroycreations.smarthhealthapp;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.jtransforms.fft.DoubleFFT_1D;

public class FrequencyCalculations2 {
	
	
	
	
	public static void execute() throws Exception {
		File sensor=new File("C:\\Users\\Roy\\Documents\\tfg\\calculations\\prueba data\\S01_LB.csv");
		CSVmanager csvmanagersen=new CSVmanager(sensor);
		ArrayList<String[]> arraysensor=csvmanagersen.csvSensorToArray();
		
		
		
		int n = (int) Math.pow(2,Math.round(Math.log10(arraysensor.size())/Math.log10(2)));
		double[] x=new double[n];
		for(int i=0; i<n;i++) {
			if(i<arraysensor.size()) {
				x[i]=Double.parseDouble(arraysensor.get(i)[0]);
			}
			else {
				x[i]=0;
			}
			
		}

		
		
		
		
		 
	    FreqCalc2 fft = new FreqCalc2(n);
	    double[] im = new double[n];
	    
	    for(int i=0; i<n; i++) {
	         im[i] = 0;
	       }
	    FreqCalc2.beforeAfter(fft, x, im);
		
		/*
		long size=x.length;
		DoubleFFT_1D fft= new DoubleFFT_1D(size);
		double[] x2=new double[x.length*2];
		System.arraycopy(x, 0, x2, 0, x.length);
		fft.realForwardFull(x2);
		//Wrapper class like double Double
		for(double d: x) {
            freq.println(d);
		}
		*/
	
	}
}
