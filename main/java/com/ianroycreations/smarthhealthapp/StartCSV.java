package com.ianroycreations.smarthhealthapp;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.core.converters.ConverterUtils.DataSource;

public class StartCSV {

	public void execute() throws Exception{
		
		//Uncomment (it is commented to test faster)
		CSVmanager  csvm=new CSVmanager(new File("/storage/emulated/0/Tfg/features.csv"));
		csvm.csvToArff(new File("/storage/emulated/0/Tfg/features.arff"));
		
		DataSource source = new DataSource("/storage/emulated/0/Tfg/features.arff");
		 Instances trainingSet = source.getDataSet();
		 if (trainingSet.classIndex() == -1) {
			 trainingSet.setClassIndex(trainingSet.numAttributes() - 1);
		 }
		 createNewModel(trainingSet);
		 
		 
		 //To unlabel replace all the labeled activities for one that is included in the arff definition. Not working with csv.
		 //Unlabeled
		 DataSource source2 = new DataSource("/storage/emulated/0/Tfg/features.arff");
		 Instances classifyTest = source2.getDataSet();
		 if (classifyTest.classIndex() == -1) {
			 classifyTest.setClassIndex(classifyTest.numAttributes() - 1);
		 }
		 System.out.println(trainingSet.equalHeaders(classifyTest));
		 classifyNewCSV(classifyTest);
		 
		 
		 
		 
		 
		
		/*
		File csvFile=new File("C:\\Users\\Roy\\Documents\\tfg\\calculations\\prueba data\\S11\\features_S11.csv");
		CSVLoader loader = new CSVLoader();
	    loader.setSource(csvFile);
		Instances trainingSet=loader.getDataSet();
		trainingSet.setClassIndex(48);//classlabel is in column 0
		createNewModel(trainingSet);
		
		File csvFile2=new File("C:\\Users\\Roy\\Documents\\tfg\\calculations\\prueba data\\S11\\unlabeled\\unlabeled_features_S11.csv");
		CSVLoader loader2=new CSVLoader();
		loader2.setSource(csvFile2);
		Instances set2=loader2.getDataSet();
		classifyNewCSV(set2);//classlabel is in column 0
		*/
	    
	}
	
	
	private static void createNewModel(Instances data) throws Exception{
		 
		 
		 /*
		 //Number of trees:50
		 String[] options = new String[2];
		 options[0] = "-I";
		 options[1] = "50";
		 */
		 
		 RandomForest cls = new RandomForest(); 
		 
		 
		 //Cross validation. (Takes a lot of time).
		 /*
		 Evaluation eval = new Evaluation(data);
		 eval.crossValidateModel(cls, data, 10, new Random(1));
		 System.out.println(eval.toSummaryString("\nResults\n======\n", false));
		 */
		 
		 
		 //cls.setOptions(options);    
		 cls.buildClassifier(data);
		 weka.core.SerializationHelper.write("/storage/emulated/0/Tfg/byjavaCSV.model", cls);
		 
		 //Test model
	}
	
	private static void classifyNewCSV(Instances unlabeled) throws Exception{
		//Load model
		Classifier cls = (Classifier) weka.core.SerializationHelper.read("/storage/emulated/0/Tfg/byjavaCSV.model");
		
		unlabeled.setClassIndex(48);

		PrintWriter writer=new PrintWriter("/storage/emulated/0/Tfg/ClassificationOutputCSV.txt");
		// label instances
		for (int i = 0; i < unlabeled.numInstances(); i++) {
			double clsLabel = cls.classifyInstance(unlabeled.instance(i));
			writer.println(unlabeled.classAttribute().value((int)clsLabel));
		}
		writer.close();
	}

}
