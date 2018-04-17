package com.ianroycreations.smarthhealthapp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVLoader;


//CREATES A RANDOM FOREST MODEL, SAVES IT, AND THEN LOADS IT AND CLASSIFIES AN INPUT

public class Start {

	public static void main(String[] args) throws Exception{
		Instances trainingSet = new Instances(
		         new BufferedReader(
		           new FileReader("C:\\Users\\Roy\\Documents\\tfg\\calculations\\prueba data\\features.arff")));
		trainingSet.setClassIndex(48);//classlabel is in column 0
		createNewModel(trainingSet);
		classifyNewArff(trainingSet);//classlabel is in column 0
	    
	}
	
	private void calculate() {
		//para cada par sensor-labels
		//csv-->CSVmanager-->array
		//array(array labels,array sensor)-->calculations-->(a decidir)
	}
	
	private static void createNewModel(Instances data) throws Exception{
		 
		 
		 /*
		 //Number of trees:50
		 String[] options = new String[2];
		 options[0] = "-I";
		 options[1] = "50";
		 */
		 
		 RandomForest cls = new RandomForest();   
		 
		 //cls.setOptions(options);    
		 cls.buildClassifier(data);
		 weka.core.SerializationHelper.write("C:\\Users\\Roy\\Documents\\tfg\\calculations\\prueba data\\byjava.model", cls);
	}
	
	private static void classifyNewArff(Instances unlabeled) throws Exception{
		//Load model
		Classifier cls = (Classifier) weka.core.SerializationHelper.read("C:\\Users\\Roy\\Documents\\tfg\\calculations\\prueba data\\byjava.model");
		
		unlabeled.setClassIndex(48);

		
		PrintWriter writer=new PrintWriter("C:\\Users\\Roy\\Documents\\tfg\\calculations\\prueba data\\ClassificationOutput.txt");
		// label instances
		for (int i = 0; i < unlabeled.numInstances(); i++) {
			//TODO:
			
			double clsLabel = cls.classifyInstance(unlabeled.instance(i));
			writer.println(unlabeled.classAttribute().value((int)clsLabel));
			
			/*
			double clsLabel = cls.classifyInstance(unlabeled.instance(i));
			writer.println(clsLabel);
			*/
		}
		writer.close();
	}
	
	private static void trainAndTestModelArff() throws Exception{
		//Diabetes trial
		
		//Load the model
		ObjectInputStream ois = new ObjectInputStream(
	            new FileInputStream("C:\\Users\\Roy\\Documents\\wekamodels\\modeldiabetes2.model"));
		Classifier cls = (Classifier) ois.readObject();
		ois.close();
		
		//Trains and tests from the same file
		String[] options = new String[2];
		options[0] = "-T";
		options[1] = "C:\\Users\\Roy\\Documents\\wekamodels\\diabetes_test_unlabeled.arff";
		System.out.println(Evaluation.evaluateModel(cls, options));
	
	}
	
	private static void trainAndTestCSV() throws Exception{
	
		//Load the model
		//TODO: ver si la otra manera de cargar el modelo es mas rapida
		ObjectInputStream ois = new ObjectInputStream(
			            new FileInputStream("C:\\Users\\Roy\\Documents\\wekamodels\\models01_1.model"));
		Classifier cls = (Classifier) ois.readObject();
		ois.close();

		
		
		//Trains and tests from the same CSV file
		String[] options = new String[4];
		options[0]="-c";
		options[1]="1";
		options[2] = "-t";
		options[3] = "C:\\Users\\Roy\\Documents\\wekamodels\\S01_final_test.csv";
		
		System.out.println(Evaluation.evaluateModel(cls, options));
		
		
	}
	
	
	
	private static void classifyNewArffOld() throws Exception{
		
		
		
		ObjectInputStream ois = new ObjectInputStream(
		        new FileInputStream("C:\\Users\\Roy\\Documents\\wekamodels\\modeldiabetes2.model"));
		Classifier cls = (Classifier) ois.readObject();
		ois.close();
		
		 Instances unlabeled = new Instances(
		         new BufferedReader(
		           new FileReader("C:\\Users\\Roy\\Documents\\wekamodels\\diabetes_test.arff")));

		// set class attribute
		unlabeled.setClassIndex(unlabeled.numAttributes() - 1);
		

		
		// label instances
		for (int i = 0; i < unlabeled.numInstances(); i++) {
		double clsLabel = cls.classifyInstance(unlabeled.instance(i));
		System.out.println(clsLabel);
		}
			
	}

	private static void classifyNewCSV() {
		try {
			//Classifies a activity and shows on console the classification given by the model
			
			//Load the model
			ObjectInputStream ois = new ObjectInputStream(
		            new FileInputStream("C:\\Users\\Roy\\Documents\\wekamodels\\modeldiabetes2.model"));
			Classifier cls = (Classifier) ois.readObject();
			ois.close();
			
			
			
			
			//RandomForest tree=new RandomForest();
			
			
			//TODO: CSV loader
			File csvfile= new File("C:\\Users\\Roy\\Documents\\wekamodels\\S01_final_test.csv");
			CSVLoader csvl = new CSVLoader();
			csvl.setSource(csvfile);
			Instances dataset = csvl.getDataSet();
			
			/*
			Instances dataset = new Instances(
			        new BufferedReader(
			          new FileReader("C:\\Users\\Roy\\Documents\\wekamodels\\S01_final_test.csv")));
			*/

			// set class attribute
			dataset.setClassIndex(0);
			
			// label instances
			for (int i = 0; i < dataset.numInstances(); i++) {
				Instance inst1=dataset.instance(i);
				double clsLabel = cls.classifyInstance(inst1);
				System.out.println(clsLabel);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void labelActivity() {
		//Classifies an unlabeled activity and put the labels on the file
	
		/*
		
		//TODO:Configurar
		Instances unlabeled = new Instances(
                new BufferedReader(
                  new FileReader("/some/where/unlabeled.arff")));

		// set class attribute
		unlabeled.setClassIndex(unlabeled.numAttributes() - 1);
		
		// create copy
		Instances labeled = new Instances(unlabeled);
		
		// label instances
		for (int i = 0; i < unlabeled.numInstances(); i++) {
		double clsLabel = tree.classifyInstance(unlabeled.instance(i));
		labeled.instance(i).setClassValue(clsLabel);
		}
		// save labeled data
		BufferedWriter writer = new BufferedWriter(
		                  new FileWriter("/some/where/labeled.arff"));
		writer.write(labeled.toString());
		writer.newLine();
		writer.flush();
		writer.close();
		*/
	}
}
