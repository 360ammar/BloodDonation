package com.example.ammar.blooddonation;

import android.app.FragmentManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ammar.blooddonation.Modals.RequestforBlood;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.Map;


import static android.R.layout.simple_spinner_item;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RequiredBlood.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RequiredBlood#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RequiredBlood extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Spinner spinner;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    FirebaseUser userID;
    Button btn;


    private OnFragmentInteractionListener mListener;

    public RequiredBlood() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RequiredBlood.
     */
    // TODO: Rename and change types and number of parameters
    public static RequiredBlood newInstance(String param1, String param2) {
        RequiredBlood fragment = new RequiredBlood();
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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_required_blood, container, false);
        spinner = (Spinner) v.findViewById(R.id.spinner2);

        String[] arraySpinner = new String[] {
                "A+", "O+", "B+", "AB+", "A-","O-","B-","AB-"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(RequiredBlood.this.getActivity(), android.R.layout.simple_spinner_item, arraySpinner);
        spinner.setAdapter(adapter);
        btn= (Button) v.findViewById(R.id.btnRequest);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userID = FirebaseAuth.getInstance().getCurrentUser();
                RequestforBlood requestforBlood = new RequestforBlood();
                Map timestamp = ServerValue.TIMESTAMP;
                requestforBlood.setBloodGroup(spinner.getSelectedItem().toString());
                requestforBlood.setDaterequested(timestamp);
                requestforBlood.setUserID(userID.getUid());
                DatabaseReference newDatabaseReference = mDatabase.child("recipient").push();
                String key =  newDatabaseReference.getKey();
                mDatabase.child("recipient").child(key).setValue(requestforBlood);
                Toast.makeText(RequiredBlood.this.getActivity(),spinner.getSelectedItem().toString()+" Blood Requested!",Toast.LENGTH_LONG).show();
            }
        }
        );
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onRequiredFragmentInteraction(uri);
        }
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
        void onRequiredFragmentInteraction(Uri uri);
    }
}
