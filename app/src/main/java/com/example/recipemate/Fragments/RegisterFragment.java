package com.example.recipemate.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.recipemate.R;
import com.example.recipemate.Activities.MainActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	public RegisterFragment() {
		// Required empty public constructor
	}

	private Button buttonConfirmRegistration;
	private EditText registerUserEmail;
	private EditText registerUserPassword;
	private EditText registerUserPasswordConfirm;

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment RegisterFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static RegisterFragment newInstance(String param1, String param2) {
		RegisterFragment fragment = new RegisterFragment();
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
					registerUserPassword.setError("Invalid password!");
					registerUserPasswordConfirm.setError("Invalid password!");
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

		Log.d("RegisterFragment", "Registering user: " + registeredEmail + " " + registeredPassword);

		mainActivity.register(registeredEmail, registeredPassword);
	}
}