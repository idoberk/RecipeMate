package com.example.recipemate.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.recipemate.Activities.MainActivity;
import com.example.recipemate.R;

public class LoginFragment extends Fragment {
	
	public LoginFragment() {
		// Required empty public constructor
	}

	private Button createAnAccountButton;
	private Button loginButton;
	private EditText userEmail;
	private EditText userPassword;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_login, container, false);
		MainActivity mainActivity = (MainActivity) getActivity();
		createAnAccountButton = view.findViewById(R.id.buttonRegisterFromLogin);
		loginButton = view.findViewById(R.id.buttonConfirmLoginButton);
		userEmail = view.findViewById(R.id.loginEmailTextInput);
		userPassword = view.findViewById(R.id.loginPasswordTextInput);

		createAnAccountButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment);
			 }
		});

		loginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (userEmail.getText().toString().isEmpty()) {
					userEmail.setError("Email is required");
				} else if (userPassword.getText().toString().isEmpty()) {
					userPassword.setError("Password is required");
				} else {
					loginUser(mainActivity);
				}
			}
		});

		return view;
	}

	private void loginUser(MainActivity mainActivity) {
		String email = userEmail.getText().toString().trim();
		String password = userPassword.getText().toString().trim();

		mainActivity.login(email, password);
	}
}