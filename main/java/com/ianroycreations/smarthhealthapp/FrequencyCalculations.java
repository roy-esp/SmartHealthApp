package com.ianroycreations.smarthhealthapp;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.jtransforms.fft.*;

public class FrequencyCalculations {

	public static void main(String[] args) throws Exception {
		execute();

	}
	
	public static void execute() throws Exception {
		File sensor=new File("C:\\Users\\Roy\\Documents\\tfg\\calculations\\prueba data\\S01_LB.csv");
		CSVmanager csvmanagersen=new CSVmanager(sensor);
		ArrayList<String[]> arraysensor=csvmanagersen.csvSensorToArray();
		PrintWriter time = new PrintWriter("C:\\Users\\Roy\\Documents\\tfg\\calculations\\freq\\time.txt");
		PrintWriter freq= new PrintWriter("C:\\Users\\Roy\\Documents\\tfg\\calculations\\freq\\freq.txt");
		
		double[] x=new double[arraysensor.size()];
		for(int i=0; i<arraysensor.size();i++) {
			x[i]=Double.parseDouble(arraysensor.get(i)[0]);
			time.println(x[i]);
		}
		time.close();
		
		long size=x.length;
		DoubleFFT_1D fft= new DoubleFFT_1D(size);
		double[] x2=new double[x.length*2];
		System.arraycopy(x, 0, x2, 0, x.length);
		fft.realForwardFull(x2);
		//Wrapper class like double Double
		for(double d: x) {
            freq.println(d);
		}
	}

}
