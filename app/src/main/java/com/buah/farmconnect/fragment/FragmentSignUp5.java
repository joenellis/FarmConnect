package com.buah.farmconnect.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.buah.farmconnect.R;

public class FragmentSignUp5 extends Fragment {


    public FragmentSignUp5() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up_5, container, false);

        Spinner securityQuestion = view.findViewById(R.id.forgotPassword_securityQuestion);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.security_questions,
                android.R.layout.simple_list_item_1
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        securityQuestion.setAdapter(adapter);

        return view;
    }

}
