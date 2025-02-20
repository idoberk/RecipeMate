package com.example.recipemate.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.example.recipemate.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

	private NavController navController;
	private BottomNavigationView bottomNav;
	private FirebaseAuth mAuth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EdgeToEdge.enable(this);
		setContentView(R.layout.activity_main);

		NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
		navController = navHostFragment.getNavController();
		bottomNav = findViewById(R.id.bottomNavigationView);

		NavigationUI.setupWithNavController(bottomNav, navController);
		navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
			if (destination.getId() == R.id.loginFragment || destination.getId() == R.id.registerFragment || destination.getId() == R.id.welcomeFragment) {
				bottomNav.setVisibility(View.GONE);
			} else {
				bottomNav.setVisibility(View.VISIBLE);
			}
		});

		ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
			Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
			v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
			return insets;
		});

		mAuth = FirebaseAuth.getInstance();
	}

	public void login(String email, String password) {
		mAuth.signInWithEmailAndPassword(email, password)
				.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						if (task.isSuccessful()) {
							FirebaseUser user = mAuth.getCurrentUser();
							View navHostFragment = findViewById(R.id.fragmentContainerView);
							if (user != null) {
								Log.d("Login", "User logged in successfully: " + user.getEmail());
								Toast.makeText(MainActivity.this, "Login successful!",
										Toast.LENGTH_SHORT).show();
								Navigation.findNavController(navHostFragment).navigate(R.id.action_loginFragment_to_mainFragment);
							}
						} else {
							Log.e("Login", "Login failed", task.getException());
							Toast.makeText(MainActivity.this, "Login failed",
									Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	public void register(String email, String password) {
		mAuth.createUserWithEmailAndPassword(email, password)
				.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						if (task.isSuccessful()) {
							FirebaseUser user = mAuth.getCurrentUser();
							if (user != null) {
								// addData(user);
								Toast.makeText(MainActivity.this, "register ok", Toast.LENGTH_LONG).show();
							}
						} else {
							try {
								throw task.getException();
							} catch (FirebaseAuthWeakPasswordException e) {
								Toast.makeText(MainActivity.this, "Password is too weak", Toast.LENGTH_LONG).show();
							} catch (FirebaseAuthInvalidCredentialsException e) {
								Toast.makeText(MainActivity.this, "Invalid email format", Toast.LENGTH_LONG).show();
							} catch (FirebaseAuthUserCollisionException e) {
								Toast.makeText(MainActivity.this, "Email already registered", Toast.LENGTH_LONG).show();
							} catch (Exception e) {
								Toast.makeText(MainActivity.this, "Registration failed: " + e.getMessage(),
										Toast.LENGTH_LONG).show();
								Log.e("Register", "Failed to register user", e);
							}
						}
					}
				});
	}

	public void addData(FirebaseUser user) {

	}
}