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

public class FragmentSignUp4 extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up_4, container, false);

        TextInputEditText password = view.findViewById(R.id.signUp4_txtPassword);

        SignUp.setPassword(password.getText().toString().trim());

        return view;
    }
}
