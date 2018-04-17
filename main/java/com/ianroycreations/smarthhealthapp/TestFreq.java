package com.ianroycreations.smarthhealthapp;

import org.jtransforms.fft.DoubleFFT_1D;

public class TestFreq {

	public static void main(String[] args) throws Exception {
		/*
		double[] x= {0,0.2,0.4,0.6,0.8,1,0.8,0.6,0.4,0.2,0,-0.2,-0.4,-0.6,-0.8,-1,-0.8,-0.6,-0.4,-0.2};
		DoubleFFT_1D fft= new DoubleFFT_1D(x.length);
		double[] x2=new double[x.length*2];
		System.arraycopy(x, 0, x2, 0, x.length);
		fft.realForwardFull(x2);
		for(double d: x) {
            System.out.println(d);
		}
		*/
		
		FrequencyCalculations2.execute();
	}

}
