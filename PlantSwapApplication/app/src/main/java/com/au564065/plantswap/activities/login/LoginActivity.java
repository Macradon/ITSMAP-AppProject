package com.au564065.plantswap.activities.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.au564065.plantswap.GlobalConstants;
import com.au564065.plantswap.R;
import com.au564065.plantswap.database.Repository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    //Debug logging tag
    private static final String TAG = "LoginActivity";

    //UI Widgets
    private Button btnLogin, btnRegister;
    private EditText edtMail, edtPassword;

    //Auxiliary
    private FirebaseAuth firebaseAuthentication = FirebaseAuth.getInstance();
    private Repository repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        repo = Repository.getInstance(getApplicationContext());

        initiateUI();
    }

    private void initiateUI() {
        edtMail = findViewById(R.id.login_edtTxtEmail);
        edtPassword = findViewById(R.id.login_edtTxtPassword);

        btnLogin = findViewById(R.id.login_btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLoginClicked();
            }
        });

        btnRegister = findViewById(R.id.login_btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRegisterClicked();
            }
        });
    }

    private void onRegisterClicked() {
        Intent registerIntent = new Intent(this, RegisterActivity.class);
        startActivityForResult(registerIntent, GlobalConstants.RegisterRequestCode);
    }

    private void onLoginClicked() {
        firebaseAuthentication.signInWithEmailAndPassword(edtMail.getText().toString(),
                                                            edtPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: SignInWithEmail: Success");
                            FirebaseUser user = firebaseAuthentication.getCurrentUser();
                            Log.d(TAG, "onComplete: Person logged in: " + user.getUid());
                            repo.setCurrentUser(user.getUid());
                            finishLogin();
                        } else {
                            Log.w(TAG, "signIn failure", task.getException());
                            Toast.makeText(LoginActivity.this,
                                    "Login was unsuccessful.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GlobalConstants.RegisterRequestCode) {
            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user == null) {
                    Toast.makeText(LoginActivity.this,
                            "Login unsuccessful, try manual login.",
                            Toast.LENGTH_SHORT).show();
                } else  {
                    finishLogin();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(LoginActivity.this,
                        "Canceled registration.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void finishLogin() {
        setResult(RESULT_OK);
        finish();
    }
}