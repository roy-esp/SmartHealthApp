package com.ianroycreations.smarthhealthapp;

import android.app.Fragment;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Recording.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Recording#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Recording extends Fragment implements SensorEventListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String fileNameRecording;
    private String activityName;
    private int activityNumber;
    private int timeRecording;
    private int countDownSec;


    private SensorManager mSensorManager;
    private Sensor mSensor;

    private float accelerationX;
    private float accelerationY;
    private float accelerationZ;


    int counter=5;

    private OnFragmentInteractionListener mListener;

    View rootView;

    public Recording() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Recording.
     */
    // TODO: Rename and change types and number of parameters
    public static Recording newInstance(String param1, String param2) {
        Recording fragment = new Recording();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Default atts
        timeRecording=60;//Seconds
        countDownSec=7;
        activityNumber=0;

        Bundle bundle = getArguments();
        if (bundle != null) {
            fileNameRecording=bundle.getString("filename");
            activityName=bundle.getString("activity");
            timeRecording=bundle.getInt("timeRecording");
            countDownSec=bundle.getInt("timeCounter");

        }
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        accelerationX=0;
        accelerationY=0;
        accelerationZ=0;


        //Translate activity name to int
        switch (activityName){
            case "Walking":
                activityNumber=1;
                break;
            case "Running":
                activityNumber=2;
                break;
            case "Shuffling":
                activityNumber=3;
                break;
            case "Ascending stairs":
                activityNumber=4;
                break;
            case "Descending stairs":
                activityNumber=5;
                break;
            case "Standing":
                activityNumber=6;
                break;
            case "Sitting":
                activityNumber=7;
                break;
            case "Lying":
                activityNumber=8;
                break;
            case "Transition":
                activityNumber=9;
                break;
            case "Bending":
                activityNumber=10;
                break;
            case "Picking":
                activityNumber=11;
                break;
            case "Undefined":
                activityNumber=12;
                break;
            case "Cycling (sit)":
                activityNumber=13;
                break;
            case "Cycling (stand)":
                activityNumber=14;
                break;
            case "Heel drop":
                activityNumber=15;
                break;
            default:
                activityNumber=0;
                break;
        }

        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mSensor , SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_recording, container, false);
        // Inflate the layout for this fragment

        //Get elements

        final TextView tvConfigs=rootView.findViewById(R.id.textViewConfigs);
        tvConfigs.setText("Filename: "+fileNameRecording+" Activity: "+activityName+" Time Recording: "+timeRecording+" Countdown: "+countDownSec);

        final TextView tvCountingDown=rootView.findViewById(R.id.tvCountingDown);
        final TextView tvInstruct=rootView.findViewById(R.id.tvInstruct);

        //Counter
        counter=countDownSec;
        new CountDownTimer(counter*1000,1000){
            public void onTick(long millisUntilFinished){
                counter--;
                if(counter==2){
                    //TODO: add vibration
                    try {
                        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                        Uri uri2=Uri.parse("android.resource://"
                                + getContext().getPackageName() + "/" + R.raw.beep1);

                        Ringtone r = RingtoneManager.getRingtone(getContext(), uri2);
                        r.play();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                tvCountingDown.setText(String.valueOf(counter));
            }
            public void onFinish(){
                tvCountingDown.setText("0");//change for ""
                tvInstruct.setText("Recording... If you are watching this, then the recorded data will not give a precise measure, so come on! Stop it and do it again placing your mobile phone on your pocket when said so.");

                //start taking measures of accelerations
                //Executing in background with no params ()
                new ExecBackgroundMeasures().execute();
            }
        }.start();

        return rootView;
    }


    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent event) {
        //Right in here is where you put code to read the current sensor values and
        //update any views you might have that are displaying the sensor information
        //You'd get accelerometer values like this:

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            accelerationX=event.values[0];
            accelerationY=event.values[1];
            accelerationZ=event.values[2];
        }


        /*
        float mSensorX, mSensorY;
        switch (mDisplay.getRotation()) {
            case Surface.ROTATION_0:
                mSensorX = event.values[0];
                mSensorY = event.values[1];
                break;
            case Surface.ROTATION_90:
                mSensorX = -event.values[1];
                mSensorY = event.values[0];
                break;
            case Surface.ROTATION_180:
                mSensorX = -event.values[0];
                mSensorY = -event.values[1];
                break;
            case Surface.ROTATION_270:
                mSensorX = event.values[1];
                mSensorY = -event.values[0];
        }
        */

    }

    private void execMeasures() throws IOException {

        //Open file if exists, else create one.
        File plainfile=new File(Environment.getExternalStorageDirectory(), "/Tfg/csv/"+fileNameRecording+".csv");
        try(FileWriter fw = new FileWriter(plainfile, true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw)){
            out.println(accelerationX+","+accelerationY+","+accelerationZ);
        }

        File plainfilelabels=new File(Environment.getExternalStorageDirectory(), "/Tfg/csv/"+fileNameRecording+"_labels.csv");
        try(FileWriter fw2 = new FileWriter(plainfilelabels, true);
            BufferedWriter bw2 = new BufferedWriter(fw2);
            PrintWriter out2 = new PrintWriter(bw2)){
            out2.println(activityNumber);
        }





    }

    private class ExecBackgroundMeasures extends AsyncTask<Void, Integer, Void> {
        protected Void doInBackground(Void... voids) {
            for (int i = 0; i < timeRecording*100; i++) {
                try {
                    execMeasures();
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                publishProgress((int) ((i / (float) timeRecording) * 100));
                // Escape early if cancel() is called
                if (isCancelled()) break;
            }
            return null;
        }

        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        protected void onPostExecute(Long result) {
            //todo: not working the sound
            try {
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                Uri uri2=Uri.parse("android.resource://"
                        + getContext().getPackageName() + "/" + R.raw.beep1);

                Ringtone r = RingtoneManager.getRingtone(getContext(), uri2);
                r.play();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
