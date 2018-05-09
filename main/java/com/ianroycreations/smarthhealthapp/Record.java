package com.ianroycreations.smarthhealthapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Record.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Record#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Record extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    View rootView;

    public Record() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Record.
     */
    // TODO: Rename and change types and number of parameters
    public static Record newInstance(String param1, String param2) {
        Record fragment = new Record();
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
        rootView = inflater.inflate(R.layout.fragment_record, container, false);
        // Inflate the layout for this fragment

        //Get elements
        final Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner);
        final EditText etFileNameRecording=rootView.findViewById(R.id.recordingName);
        final EditText etTimeRecordingMin=rootView.findViewById(R.id.recMin);
        final EditText etTimeRecordingSec=rootView.findViewById(R.id.recSec);
        final EditText etCountDownSec=rootView.findViewById(R.id.countDownSec);


// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.activities, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        Button startBtn= rootView.findViewById(R.id.buttonStartRecording);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:check fields
                checkFields();

                String fileNameRecording=etFileNameRecording.getText().toString();
                int timeRecordingMin =Integer.parseInt(etTimeRecordingMin.getText().toString());
                int timeRecordingSec =Integer.parseInt(etTimeRecordingSec.getText().toString());
                int countDownSec=Integer.parseInt( etCountDownSec.getText().toString());

                int timeRecording=timeRecordingMin*60+timeRecordingSec;

                String selectedActivity=spinner.getSelectedItem().toString();
                startRecording(fileNameRecording,selectedActivity,timeRecording,countDownSec);
            }
        });

        return rootView;
    }

    private void checkFields() {
    }

    private void startRecording(String filename, String activity, int timeRecording, int timeCounter) {
        if(Build.VERSION.SDK_INT>22){
            requestPermissions(new String[] {"android.permission.READ_EXTERNAL_STORAGE","android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
        }
        Fragment recording= new Recording();
        Bundle bundle = new Bundle();
        bundle.putString("filename",filename);
        bundle.putString("activity", activity);
        bundle.putInt("timeRecording", timeRecording);
        bundle.putInt("timeCounter", timeCounter);
        recording.setArguments(bundle);
        FragmentManager fragmentManager=getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.relativeLayoutMain, recording).commit();
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
