package com.ianroycreations.smarthhealthapp;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;


//CALCULATES FEATURES AND OUTPUTS TO CSV

public class TestCalc {

	public void execute(String pathLabels, String pathSensor) throws Exception {
		
		//Do this for each sensor, for each person.
		//File labels=new File("C:\\Users\\Roy\\Documents\\tfg\\calculations\\prueba data\\S11\\S11_labels.csv");
		//File sensor=new File("C:\\Users\\Roy\\Documents\\tfg\\calculations\\prueba data\\S11\\S11_LB.csv");

		File labels=new File(pathLabels);
		File sensor=new File(pathSensor);
		CSVmanager csvmanagersen=new CSVmanager(sensor);
		ArrayList<String[]> arraysensor=csvmanagersen.csvSensorToArray();
		CSVmanager csvmanagerlab=new CSVmanager(labels);
		ArrayList<String> arraylabel=csvmanagerlab.csvLabelToArray();
		
		//PrintWriter writer = new PrintWriter("C:\\Users\\Roy\\Documents\\tfg\\calculations\\prueba data\\S11\\features_S11.csv");
		PrintWriter writer = new PrintWriter("/storage/emulated/0/Tfg/features.csv");

		//Generate CSV files x,y,z separated feature csv files
		Calculations calculations=new Calculations(arraylabel,arraysensor,writer);
		calculations.generateCsv();
		
		
	}

}
