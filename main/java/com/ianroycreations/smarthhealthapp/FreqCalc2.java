package com.ianroycreations.smarthhealthapp;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class FreqCalc2 {



   int n, m;
   
   // Lookup tables.  Only need to recompute when size of FFT changes.
   double[] cos;
   double[] sin;
 
   double[] window;
   
   
   public FreqCalc2(int n) {
     this.n = n;
     this.m = (int)(Math.log(n) / Math.log(2));
 
     // Make sure n is a power of 2
     if(n != (1<<m))
       throw new RuntimeException("FFT length must be power of 2");
 
     // precompute tables
     cos = new double[n/2];
     sin = new double[n/2];
 

 
     for(int i=0; i<n/2; i++) {
       cos[i] = Math.cos(-2*Math.PI*i/n);
       sin[i] = Math.sin(-2*Math.PI*i/n);
     }
 
     makeWindow();
   }
 
   protected void makeWindow() {
     // Make a blackman window:
     // w(n)=0.42-0.5cos{(2*PI*n)/(N-1)}+0.08cos{(4*PI*n)/(N-1)};
     window = new double[n];
     for(int i = 0; i < window.length; i++)
       window[i] = 0.42 - 0.5 * Math.cos(2*Math.PI*i/(n-1)) 
         + 0.08 * Math.cos(4*Math.PI*i/(n-1));
   }
   
   public double[] getWindow() {
     return window;
   }

   public void fft(double[] x, double[] y)
   {
     int i,j,k,n1,n2,a;
     double c,s,e,t1,t2;
   
   
     // Bit-reverse
     j = 0;
     n2 = n/2;
     for (i=1; i < n - 1; i++) {
       n1 = n2;
       while ( j >= n1 ) {
         j = j - n1;
         n1 = n1/2;
       }
       j = j + n1;
     
       if (i < j) {
         t1 = x[i];
         x[i] = x[j];
         x[j] = t1;
         t1 = y[i];
         y[i] = y[j];
         y[j] = t1;
       }
     }
 
     // FFT
     n1 = 0;
     n2 = 1;
   
     for (i=0; i < m; i++) {
       n1 = n2;
       n2 = n2 + n2;
       a = 0;
     
       for (j=0; j < n1; j++) {
         c = cos[a];
         s = sin[a];
        a +=  1 << (m-i-1);

        for (k=j; k < n; k=k+n2) {
          t1 = c*x[k+n1] - s*y[k+n1];
           t2 = s*x[k+n1] + c*y[k+n1];
          x[k+n1] = x[k] - t1;
          y[k+n1] = y[k] - t2;
          x[k] = x[k] + t1;
         y[k] = y[k] + t2;
         }
      }
     }
  }                          

 
 
   // Test the FFT to make sure it's working
   public static void main(String[] args) throws FileNotFoundException {
     int N = 8;
     

 
     FreqCalc2 fft = new FreqCalc2(N);

double[] window = fft.getWindow();
     double[] re = new double[N];
     double[] im = new double[N];
 /*
     // Impulse
    re[0] = 1; im[0] = 0;
     for(int i=1; i<N; i++)
       re[i] = im[i] = 0;
    beforeAfter(fft, re, im);
 
     // Nyquist
     for(int i=0; i<N; i++) {
       re[i] = Math.pow(-1, i);
       im[i] = 0;
    }
     beforeAfter(fft, re, im);
     */
     
     // Single sin
     for(int i=0; i<N; i++) {
      re[i] = Math.cos(2*Math.PI*i /(0.5* N));
       im[i] = 0;
     }
     beforeAfter(fft, re, im);
     // Ramp
    for(int i=0; i<N; i++) {
       re[i] = i;
       im[i] = 0;
    }
     beforeAfter(fft, re, im);
 
    long time = System.currentTimeMillis();
     double iter = 30000;
     for(int i=0; i<iter; i++)
       fft.fft(re,im);
     time = System.currentTimeMillis() - time;
     System.out.println("Averaged " + (time/iter) + "ms per iteration");
   }
 
   protected static void beforeAfter(FreqCalc2 fft, double[] re, double[] im) throws FileNotFoundException {
	PrintWriter freq= new PrintWriter("C:\\Users\\Roy\\Documents\\tfg\\calculations\\freq\\freq.txt");
	freq.println("Before: ");
    printReIm(re, im,freq);
     fft.fft(re, im);
    freq.println("After: ");
    printReIm(re, im,freq);
    freq.close();
   }
   
   
   //devolver re e im despues de calcular fft:
   
   protected static double[] getFFT(FreqCalc2 fft, double[] re, double[] im) throws FileNotFoundException {
		double[] modulo=new double[re.length];
		
	     fft.fft(re, im);
	     for(int i=0; i<re.length; i++) {
	    	 modulo[i]=Math.sqrt(Math.pow(re[i],2)+Math.pow(im[i], 2));
	     }
	        
	    return modulo;
	    
	   }
 
   protected static void printReIm(double[] re, double[] im, PrintWriter freq) throws FileNotFoundException {
		
     freq.print("Re: [");
    for(int i=0; i<re.length; i++)
       freq.print(((int)(re[i]*1000)/1000.0) + " ");
 
     freq.print("]\nIm: [");
     for(int i=0; i<im.length; i++)
       freq.print(((int)(im[i]*1000)/1000.0) + " ");
 
     freq.println("]");
     
   }
}
