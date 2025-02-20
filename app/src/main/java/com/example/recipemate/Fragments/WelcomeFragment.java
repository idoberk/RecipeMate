package com.example.recipemate.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.recipemate.R;

public class WelcomeFragment extends Fragment {

	public WelcomeFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		 View view = inflater.inflate(R.layout.fragment_welcome, container, false);

		 Button buttonHomeLogin = view.findViewById(R.id.buttonHomePageLogin);
		 Button buttonHomeRegister = view.findViewById(R.id.buttonHomePageRegister);

		 buttonHomeLogin.setOnClickListener(new View.OnClickListener() {
			 @Override
			 public void onClick(View v) {
				Navigation.findNavController(view).navigate(R.id.action_homePageFragment_to_loginFragment);
			 }
		 });

		 buttonHomeRegister.setOnClickListener(new View.OnClickListener() {
			 @Override
			 public void onClick(View v) {
				Navigation.findNavController(view).navigate(R.id.action_homePageFragment_to_registerFragment);
			 }
		 });

		 return view;
	}
}