package com.buah.farmconnect.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buah.farmconnect.R;
import com.buah.farmconnect.api.SignUp;

public class FragmentSignUp3 extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up_3, container, false);

        TextInputEditText email = view.findViewById(R.id.signUp3_txtEmail);

        SignUp.setEmail(email.getText().toString());

        return view;
    }
}
