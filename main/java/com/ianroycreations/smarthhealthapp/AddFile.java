package com.ianroycreations.smarthhealthapp;

import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import junit.framework.Test;

import java.net.URISyntaxException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddFile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddFile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFile extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String pathLabels;
    private String pathsensor;

    private int flagPath;

    private OnFragmentInteractionListener mListener;
    private static final String TAG = "Addfilefragment";
    View rootView;

    public AddFile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddFile.
     */
    // TODO: Rename and change types and number of parameters
    public static AddFile newInstance(String param1, String param2) {
        AddFile fragment = new AddFile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_add_file, container, false);
        rootView.setTag(TAG);

        flagPath=0;

        final Button buttonSelectLabels = rootView.findViewById(R.id.button_select_labels);
        buttonSelectLabels.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                flagPath=1;
                fileManager();
            }
        });

        final Button buttonSelectSensor = rootView.findViewById(R.id.button_select_sensor);
        buttonSelectSensor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                flagPath=2;
                fileManager();
            }
        });

        final Button buttonAnalyse = rootView.findViewById(R.id.button_analyse);
        buttonAnalyse.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    analyse();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    // Inflate the layout for this fragment
        return rootView;
    }

    private void analyse() throws Exception{
        //TODO:Check whether a file was selected before analysing
        Toast toast = Toast.makeText(getContext(), "Starting", Toast.LENGTH_SHORT);
        toast.show();
        TestCalc testCalc=new TestCalc();
        testCalc.execute(pathLabels,pathsensor);
        toast = Toast.makeText(getContext(), "Features Calculated", Toast.LENGTH_SHORT);
        toast.show();
        StartCSV startCSV=new StartCSV();
        startCSV.execute();
        toast = Toast.makeText(getContext(), "Created a model and classified out to output.txt the result. Analysis done.", Toast.LENGTH_SHORT);
    }

    private void fileManager() {
        //Grant permissions in addition to manifest
        if(Build.VERSION.SDK_INT>22){
            requestPermissions(new String[] {"android.permission.READ_EXTERNAL_STORAGE","android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
        }


        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        // Filter to only show results that can be "opened", such as a
        // file (as opposed to a list of contacts or timezones)
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Filter to show only images, using the image MIME data type.
        // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
        // To search for all documents available via installed storage providers,
        // it would be "*/*".
        intent.setType("*/*");

        startActivityForResult(intent, 42);

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
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.

        if (requestCode == 42 && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            Uri uri = null;
            if (resultData != null) {
                //TODO: save uri as filepath
                uri = resultData.getData();
                try {

                    if(flagPath==1){
                        pathLabels = PathUtil.getPath(getContext(),uri);
                    }
                    if(flagPath==2){
                        pathsensor = PathUtil.getPath(getContext(),uri);
                    }
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }


                //showImage(uri);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (!(grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(getContext(),"Permission denied to access your location.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }




}
