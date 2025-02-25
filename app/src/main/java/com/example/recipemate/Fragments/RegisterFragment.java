package com.example.recipemate.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.recipemate.R;
import com.example.recipemate.Activities.MainActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterFragment extends Fragment {

	public RegisterFragment() {
		// Required empty public constructor
	}

	private Button buttonConfirmRegistration;
	private EditText registerUserEmail;
	private EditText registerUserPassword;
	private EditText registerUserPasswordConfirm;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_register, container, false);
		buttonConfirmRegistration = view.findViewById(R.id.registerConfirmButton);
		registerUserEmail = view.findViewById(R.id.registerEmailTextInput);
		registerUserPassword = view.findViewById(R.id.registerPasswordTextInput);
		registerUserPasswordConfirm = view.findViewById(R.id.registerConfirmPasswordTextInput);


		buttonConfirmRegistration.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MainActivity mainActivity = (MainActivity) getActivity();
				String registeredEmail = registerUserEmail.getText().toString().trim();
				String registeredPassword = registerUserPassword.getText().toString().trim();
				String registeredPasswordConfirm = registerUserPasswordConfirm.getText().toString().trim();
				boolean isValid = true;

				if (!isValidEmail(registeredEmail)) {
					registerUserEmail.setError("Invalid email!");
					isValid = false;
				}

				if (!isValidPassword(registeredPassword, registeredPasswordConfirm)) {
					registerUserPassword.setError("Invalid password! Must contain at least 8 characters, one letter and one number.");
					registerUserPasswordConfirm.setError("Invalid password! Must contain at least 8 characters, one letter and one number.");
					isValid = false;
				}

				if (isValid) {
					registerUser(mainActivity);
					Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment);
				}
			}
		});

		return view;
	}

	private boolean isValidEmail(String email) {
		if (email == null || email.isEmpty()) {
			return false;
		}

		String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
		Pattern pattern = Pattern.compile(emailPattern);
		Matcher matcher = pattern.matcher(email);

		return matcher.matches();
	}

	private boolean isValidPassword(String password1, String password2) {
		String passwordPattern = "^(?=.*[a-zA-Z])(?=.*[0-9]).{8,}$";

		if (password1 == null || password2 == null) {
			return false;
		}

		if (!password1.equals(password2)) {
			return false;
		}

		return password1.matches(passwordPattern);
	}

	private void registerUser(MainActivity mainActivity) {
		String registeredEmail = registerUserEmail.getText().toString().trim();
		String registeredPassword = registerUserPassword.getText().toString().trim();

		mainActivity.register(registeredEmail, registeredPassword);
	}
}