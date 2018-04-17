package com.ianroycreations.smarthhealthapp;

import java.util.List;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Calculations {

	//Take csv files calculates attributes and (a decidir) either returns array on each calculation or creates a CSV with all the results (preferred)
	//So from class start execute void generateCSV 
	
	private ArrayList<String> labels;
	private ArrayList<String[]> sensor;
	private PrintWriter writer;
	
	private static final int samplesNumber=300;//(Window length 3sec (300 samples)
	private static final double windowOverlap=0.8;
	private static final double inverseOverlap=0.2;
	
	
	public Calculations(ArrayList<String> labels, ArrayList<String[]> sensor, PrintWriter writer) {
		this.labels=labels;
		this.sensor=sensor;
		this.writer=writer;
	}
	
	public void generateCsv() throws Exception{
		//Generates a CSV file with the windows. Which includes the attributes:label, and calculated parameters
		
		
		calc();
			
		
		
		
		

		
	}
	
	public void calc() throws Exception{
		
		//X
		double xSummatory=0;
		double xSumaCuadratica=0;
		double xZcrSum=0;
		double xMax=0;
		double xMin=0;
		
		//X
		double ySummatory=0;
		double ySumaCuadratica=0;
		double yZcrSum=0;
		double yMax=0;
		double yMin=0;
		
		//X
		double zSummatory=0;
		double zSumaCuadratica=0;
		double zZcrSum=0;
		double zMax=0;
		double zMin=0;
		
		//XYZ
		double xyzSummatory=0;
		double xyzMax=0;
		
		
		//FREQ
		//X
		double xMaxAmpl=0;
		int xIndexMaxAmpl=0;
		double xFreqSummatory=0;
		double xProductsc=0;
		double xFreqSumaCuadratica=0;
		
		//Y
		double yMaxAmpl=0;
		int yIndexMaxAmpl=0;
		double yFreqSummatory=0;
		double yProductsc=0;
		double yFreqSumaCuadratica=0;
		
		//Z
		double zMaxAmpl=0;
		int zIndexMaxAmpl=0;
		double zFreqSummatory=0;
		double zProductsc=0;
		double zFreqSumaCuadratica=0;
		
		
	
		
		//Attributes names:
		String attClass="Class";
		String xAtts="xMean,xStandardDeviation,xSkewness,xZeroCrossingRate,xMeanCrossingRate,xRootMeanSquare,xEnergy,xRange";
		String yAtts="yMean,yStandardDeviation,ySkewness,yZeroCrossingRate,yMeanCrossingRate,yRootMeanSquare,yEnergy,yRange";
		String zAtts="zMean,zStandardDeviation,zSkewness,zZeroCrossingRate,zMeanCrossingRate,zRootMeanSquare,zEnergy,zRange";
		String xyzAtts="xyzMean,xyzStandardDeviation,xyzMax,xyCorrelation,xzCorrelation,yzCorrelation";
		String xFreqAtts="xFreqMean,xFreqStandardDeviation,xMaxAmpl,xFreqOfMaxAmpl,xSpectralCentroid,xSpectralEntropy";
		String yFreqAtts="yFreqMean,yFreqStandardDeviation,yMaxAmpl,yFreqOfMaxAmpl,ySpectralCentroid,ySpectralEntropy";
		String zFreqAtts="zFreqMean,zFreqStandardDeviation,zMaxAmpl,zFreqOfMaxAmpl,zSpectralCentroid,zSpectralEntropy";
		writer.println(xAtts+","+yAtts+","+zAtts+","+xyzAtts+","+xFreqAtts+","+yFreqAtts+","+zFreqAtts+","+attClass);
		
		
		for(int i=0; i<sensor.size()-samplesNumber;i+=(int)(samplesNumber*inverseOverlap)) {
			
			//Label stats
			int[] labelsStats= {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
			
			
			int n = (int) Math.pow(2,Math.round(Math.log10(samplesNumber)/Math.log10(2)));
			double[][] timearray=new double[3][n];
			for(int w=0;w<3;w++) {
				for(int k=0; k<n;k++) {
					if(k<sensor.size()) {
						timearray[w][k]=Double.parseDouble(sensor.get(i+k)[w]);
					}
					else {
						timearray[w][k]=0;
					}
					
				}
			}
			
			
			//GET FREQ(FFT)
			//X
			FreqCalc2 fft = new FreqCalc2(n);
		    double[] im = new double[n];
		    
		    for(int l=0; l<n; l++) {
		         im[l] = 0;
		       }
		    double[] freqarrayX=FreqCalc2.getFFT(fft, timearray[0], im);
		    double[] freqarrayY=FreqCalc2.getFFT(fft, timearray[1], im);
		    double[] freqarrayZ=FreqCalc2.getFFT(fft, timearray[2], im);
		    
		    for(int m=0; m<freqarrayX.length;m++) {
		    	
		    	//Mean
				xFreqSummatory+=freqarrayX[m];
				yFreqSummatory+=freqarrayY[m];
				zFreqSummatory+=freqarrayZ[m];
				
				
				//Spectral centroid
				xProductsc+=freqarrayX[m]*m;
				yProductsc+=freqarrayY[m]*m;
				zProductsc+=freqarrayZ[m]*m;
				
               
				//Suma cuadatica
				xFreqSumaCuadratica+=Math.pow(freqarrayX[m], 2);
				yFreqSumaCuadratica+=Math.pow(freqarrayY[m], 2);
				zFreqSumaCuadratica+=Math.pow(freqarrayZ[m], 2);
		    	
		    	
				//TODO:Take into account negative max. Don't start with max=0? also in the other maxs.
				//TODO: ignoring first frequency cause is the continuos value. Correct?
				//MAX AMPLITUDE
				if(m!=0) {
				 	if (freqarrayX[m]>xMaxAmpl|| m==1) {
			    		xMaxAmpl=freqarrayX[m];
			    		xIndexMaxAmpl=m;
			    	}
			    	if (freqarrayY[m]>yMaxAmpl|| m==1) {
			    		yMaxAmpl=freqarrayY[m];
			    		yIndexMaxAmpl=m;
			    	}
			    	if (freqarrayZ[m]>zMaxAmpl|| m==1) {
			    		zMaxAmpl=freqarrayZ[m];
			    		zIndexMaxAmpl=m;
			    	}
				}
		   
		    }
			
			
			for(int j=0;j<samplesNumber;j++) {
				
				//Labels
				int labelIndex=Integer.parseInt(labels.get(i+j));
				labelsStats[labelIndex]+=1;
			
		
				//Mean 
				
				//X
				double valuex=Double.parseDouble(sensor.get(i+j)[0]);
                xSummatory+=valuex;
                xSumaCuadratica+=Math.pow(valuex, 2);
                
                //Y
                double valuey=Double.parseDouble(sensor.get(i+j)[1]);
                ySummatory+=valuey;
                ySumaCuadratica+=Math.pow(valuey, 2);
                
                //Z
                double valuez=Double.parseDouble(sensor.get(i+j)[2]);
                zSummatory+=valuez;
                zSumaCuadratica+=Math.pow(valuez, 2);
                
                if(valuex>xMax || j==0) {
                	xMax=valuex;
                }
                if(valuey>yMax|| j==0) {
                	yMax=valuey;
                }
                if(valuez>zMax|| j==0) {
                	zMax=valuez;
                }
                if(valuex<xMin|| j==0) {
                	xMin=valuex;
                }
                if(valuey<yMin|| j==0) {
                	yMin=valuey;
                }
                if(valuez<zMin|| j==0) {
                	zMin=valuez;
                }
                
                if(j!=0) {
                	//Zero crossing rate
                	xZcrSum+=Math.abs(Math.signum(Double.parseDouble(sensor.get(i+j)[0]))-Math.signum(Double.parseDouble(sensor.get(i+j-1)[0])));
                	yZcrSum+=Math.abs(Math.signum(Double.parseDouble(sensor.get(i+j)[1]))-Math.signum(Double.parseDouble(sensor.get(i+j-1)[1])));
                	zZcrSum+=Math.abs(Math.signum(Double.parseDouble(sensor.get(i+j)[2]))-Math.signum(Double.parseDouble(sensor.get(i+j-1)[2])));
                	
                	
                }
                
                
                //XYZ
                double sqrtxyz=Math.sqrt(Math.pow(valuex, 2)+Math.pow(valuey, 2)+Math.pow(valuez, 2));
                xyzSummatory+=sqrtxyz;
                
                if(sqrtxyz>xyzMax|| j==0) {
                	xyzMax=sqrtxyz;
                }
                
                
                
			}
			
			
			
			
			//Mean
			double xMean=xSummatory/samplesNumber;
			double yMean=ySummatory/samplesNumber;
			double zMean=zSummatory/samplesNumber;
			double xyzMean=xyzSummatory/samplesNumber;
		
			
			//Standard deviation
			double[] xsumArray=summatory(xMean,i,0);
			double xsd=Math.sqrt(xsumArray[1]/xsumArray[0]);
			
			double[] ysumArray=summatory(yMean,i,1);
			double ysd=Math.sqrt(ysumArray[1]/ysumArray[0]);
			
			double[] zsumArray=summatory(zMean,i,2);
			double zsd=Math.sqrt(zsumArray[1]/zsumArray[0]);
			
			double[] xyzsumArray=summatory(xyzMean,i);
			double xyzsd=Math.sqrt(xyzsumArray[1]/xyzsumArray[0]);
			
			//Skewness
			double xsk=(xsumArray[2]/xsumArray[0])/(Math.pow(xsd, 3));
			double ysk=(ysumArray[2]/ysumArray[0])/(Math.pow(ysd, 3));
			double zsk=(zsumArray[2]/zsumArray[0])/(Math.pow(zsd, 3));
			
			
			//Zero crossing rate
			double xzcr=xZcrSum/(2*(samplesNumber-1));
			double yzcr=yZcrSum/(2*(samplesNumber-1));
			double zzcr=zZcrSum/(2*(samplesNumber-1));
			
			
			//Mean crossing rate
			double xmcr=xsumArray[3]/(2*(samplesNumber-1));
			double ymcr=ysumArray[3]/(2*(samplesNumber-1));
			double zmcr=zsumArray[3]/(2*(samplesNumber-1));
			
			
			//RMS
			double xrms=Math.sqrt(xSumaCuadratica/samplesNumber);
			double yrms=Math.sqrt(ySumaCuadratica/samplesNumber);
			double zrms=Math.sqrt(zSumaCuadratica/samplesNumber);
			
			
			//Energy
			double xenergy=Math.sqrt(xsumArray[1]);
			double yenergy=Math.sqrt(ysumArray[1]);
			double zenergy=Math.sqrt(zsumArray[1]);
			
			
			//Range
			double xrange=xMax-xMin;
			double yrange=yMax-yMin;
			double zrange=zMax-zMin;
			
			
			//Median
			
			
			//Interquartile range
			
			//Correlation
			double xycorrelation=correlation(i,xMean, yMean, xsd, ysd,0);
			double xzcorrelation=correlation(i,xMean, zMean, xsd, zsd,1);
			double yzcorrelation=correlation(i,yMean, zMean, ysd, zsd,2);
			
			
			//Freq
			//Mean amplitude
			double xFreqMean=xFreqSummatory/(freqarrayX.length);
			double yFreqMean=yFreqSummatory/(freqarrayY.length);
			double zFreqMean=zFreqSummatory/(freqarrayZ.length);
			
			//Standard deviation & spectral entropy
			double[] xFreqSumArray=freqSummatory(xFreqMean,freqarrayX, xFreqSumaCuadratica);
			double xFreqsd=Math.sqrt(xFreqSumArray[0]/freqarrayX.length);
			double xSpectralEntropy=xFreqSumArray[1];
			
			double[] yFreqSumArray=freqSummatory(yFreqMean,freqarrayY, yFreqSumaCuadratica);
			double yFreqsd=Math.sqrt(yFreqSumArray[0]/freqarrayY.length);
			double ySpectralEntropy=yFreqSumArray[1];
			
			double[] zFreqSumArray=freqSummatory(zFreqMean,freqarrayZ, zFreqSumaCuadratica);
			double zFreqsd=Math.sqrt(zFreqSumArray[0]/freqarrayZ.length);
			double zSpectralEntropy=zFreqSumArray[1];
			
			//Median amplitude
			//Spectral centroid
			//TODO: Check this is not working always 128. Check i+j things.
			double xSpeccentroid=xProductsc/xFreqSummatory;
			double ySpeccentroid=yProductsc/yFreqSummatory;
			double zSpeccentroid=zProductsc/zFreqSummatory;
			
		
			//Label
			int labelMaxIndex=0;
			for (int v=0;v<labelsStats.length;v++) {
				if(labelsStats[v]>labelMaxIndex) {
					labelMaxIndex=v;
				}
			}
			
			String featureClass="Zero";
			switch(labelMaxIndex) {
			case 0:
				featureClass="None or unclasified activity";
				break;
			case 1:
				featureClass="Walking";
				break;
			case 2:
				featureClass="Running";
				break;
			case 3:
				featureClass="Shuffling";
				break;
			case 4:
				featureClass="Ascending stairs";
				break;
			case 5:
				featureClass="Descending stairs";
				break;
			case 6:
				featureClass="Standing";
				break;
			case 7:
				featureClass="Sitting";
				break;
			case 8:
				featureClass="Lying";
				break;
			case 9:
				featureClass="Transition";
				break;
			case 10:
				featureClass="Bending";
				break;
			case 11:
				featureClass="Picking";
				break;
			case 12:
				featureClass="Undefined";
				break;
			case 13:
				featureClass="Cycling (sit)";
				break;
			case 14:
				featureClass="Cycling (stand)";
				break;
			case 15:
				featureClass="Heel drop";
				break;
			case 16:
				featureClass="Vigorous activity";
				break;
			case 17:
				featureClass="Non-Vigorous activity";
				break;
			case 18:
				featureClass="Transport (sitting)";
				break;
			case 19:
				featureClass="Commute (standing)";
				break;
			case 20:
				featureClass="Lying (prone)";
				break;
			case 21:
				featureClass="Lying (supine)";
				break;
			case 22:
				featureClass="Lying (left)";
				break;
			case 23:
				featureClass="Lying (right)";
				break;
			default:
				featureClass="None or unclasified activity";
				break;
				
			}
			
			
			//Write line
			//String featureClass="None";
			String xFeatures=xMean+","+xsd+","+xsk+","+xzcr+","+xmcr+","+xrms+","+xenergy+","+xrange;
			String yFeatures=yMean+","+ysd+","+ysk+","+yzcr+","+ymcr+","+yrms+","+yenergy+","+yrange;
			String zFeatures=zMean+","+zsd+","+zsk+","+zzcr+","+zmcr+","+zrms+","+zenergy+","+zrange;
			String xyzFeatures=xyzMean+","+xyzsd+","+xyzMax+","+xycorrelation+","+xzcorrelation+","+yzcorrelation;
			String freqFeatures=xFreqMean+","+xFreqsd+","+xMaxAmpl+","+xIndexMaxAmpl+","+xSpeccentroid+","+xSpectralEntropy+","+yFreqMean+","+yFreqsd+","+yMaxAmpl+","+yIndexMaxAmpl+","+ySpeccentroid+","+ySpectralEntropy+","+zFreqMean+","+zFreqsd+","+zMaxAmpl+","+zIndexMaxAmpl+","+zSpeccentroid+","+zSpectralEntropy;
			writer.println(xFeatures+","+yFeatures+","+zFeatures+","+xyzFeatures+","+freqFeatures+","+featureClass);
			
			//Put 0 variables
			xSummatory=0;
			xSumaCuadratica=0;
			xZcrSum=0;
			xMax=0;
			xMin=0;
			
			ySummatory=0;
			ySumaCuadratica=0;
			yZcrSum=0;
			yMax=0;
			yMin=0;
			
			zSummatory=0;
			zSumaCuadratica=0;
			zZcrSum=0;
			zMax=0;
			zMin=0;
			
			xyzSummatory=0;
			xyzMax=0;
			
			
			//PUT 0 FREQ VARIABLES
			//X
			xMaxAmpl=0;
			xIndexMaxAmpl=0;
			xFreqSummatory=0;
			xProductsc=0;
			xFreqSumaCuadratica=0;
			//Y
			yMaxAmpl=0;
			yIndexMaxAmpl=0;
			yFreqSummatory=0;
			yProductsc=0;
			yFreqSumaCuadratica=0;
			//Z
			zMaxAmpl=0;
			zIndexMaxAmpl=0;
			zFreqSummatory=0;
			zProductsc=0;
			zFreqSumaCuadratica=0;
			
		}
		
		writer.close();
	}
	

	
	public double[] summatory(double mean, int i, int column) {
		double[] result=new double[4];
		double summatory=0;
		double summatory2=0;
		int counter=0;
		double mcrSum=0;
		double energy=0;
		
		for(int j=0;j<samplesNumber;j++) {
			double valuex=Double.parseDouble(sensor.get(i+j)[column]);
            summatory+=Math.pow((valuex-mean),2);
            summatory2+=Math.pow((valuex-mean),3);
            
            //Mean crossing rate
            if(j!=0) {
            	mcrSum+=Math.abs(Math.signum(Double.parseDouble(sensor.get(i+j)[column])-mean)-Math.signum(Double.parseDouble(sensor.get(i+j-1)[column])-mean));
            }
        	
            counter++;
		}
		result[0]=counter;
		result[1]=summatory;
		result[2]=summatory2;
		result[3]=mcrSum;
		return result;
	}
	
	public double[] freqSummatory(double mean, double[] freqarray, double freqSumaCuadratica) {
		double[] result=new double[2];
		double summatory=0;
		double summatory2=0;

		for(int j=0;j<freqarray.length;j++) {
			double valuex=freqarray[j];
            summatory+=Math.pow((valuex-mean),2);
            double pj=(Math.pow(valuex, 2))/freqSumaCuadratica;
            summatory2+=pj*Math.log10(pj);

		}
		
		result[0]=summatory;
		result[1]=summatory2*(-1);
		
		
		return result;
	}
	
	public double[] summatory(double mean, int i) {
		double[] result=new double[4];
		double summatory=0;
		int counter=0;
		
		for(int j=0;j<samplesNumber;j++) {
			double valuex=Double.parseDouble(sensor.get(i+j)[0]);
			double valuey=Double.parseDouble(sensor.get(i+j)[1]);
			double valuez=Double.parseDouble(sensor.get(i+j)[2]);
			
			double sqrtxyz=Math.sqrt(Math.pow(valuex, 2)+Math.pow(valuey, 2)+Math.pow(valuez, 2));
			
            summatory+=Math.pow((sqrtxyz-mean),2);
            counter++;
		}
		result[0]=counter;
		result[1]=summatory;
		return result;
	}
	
	public void labelsAvg() {
		
	}
	
	public void xyzFeatures() {
		
	}
	
	public void setSensor(ArrayList<String[]> sensor) {
		this.sensor=sensor;
	}
	
	public double correlation(int i, double xMean, double yMean, double sdx, double sdy,int pair) {
		double correlation=0;
		double summatory=0;
		int column1=0;
		int column2=0;
		
		switch (pair) {
        case 0:  
        	column1 = 0;
        	column2 = 1;
            break;
        case 1:  
        	column1 = 0;
        	column2 = 2;
            break;
        case 2:  
        	column1 = 1;
        	column2 = 2;
            break;
        default:
        	column1=0;
        	column2=0;
        	break;
		}
		
		for(int j=0;j<samplesNumber;j++) {
			summatory+=((Double.parseDouble(sensor.get(i+j)[column1]))-xMean)*((Double.parseDouble(sensor.get(i+j)[column2]))-yMean);
		}
		
		correlation=summatory/((samplesNumber-1)*sdx*sdy);
		
		return correlation;
	}
	
	
}
