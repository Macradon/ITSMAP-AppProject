package com.au564065.plantswap.activities.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.au564065.plantswap.R;
import com.au564065.plantswap.database.Repository;
import com.au564065.plantswap.models.PlantSwapUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    //Debug logging tag
    private static final String TAG = "RegisterActivity";

    //UI Widgets
    private Button btnRegister, btnCancel;
    private EditText edtName, edtAddress, edtZipCode, edtCity,
            edtEmail, edtPhone, edtPass, edtConfirm;

    //Auxiliary
    private FirebaseAuth firebaseAuthentication = FirebaseAuth.getInstance();
    private Repository repo;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        repo = new Repository(getApplicationContext());
        
        initiateUI();
    }

    private void initiateUI() {
        edtName = findViewById(R.id.Profile_Update_edtTxtName);
        edtAddress = findViewById(R.id.Profile_Update_edtTxtAddress);
        edtZipCode = findViewById(R.id.Profile_Update_edtTxtZipCode);
        edtCity = findViewById(R.id.Profile_Update_edtTxtCity);
        edtEmail = findViewById(R.id.Profile_Update_edtTxtEmail);
        edtPhone = findViewById(R.id.Profile_Update_edtTxtPhone);
        edtPass = findViewById(R.id.Profile_Update_edtTxtPassword);
        edtConfirm = findViewById(R.id.Profile_Update_edtTxtConfirm);
        
        btnRegister = findViewById(R.id.Profile_Window_EditButton);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRegisterPressed();
            }
        });
        
        btnCancel = findViewById(R.id.Profile_Window_BackButton);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCancelPressed();
            }
        });
    }

    private void onCancelPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    private void onRegisterPressed() {
        if (edtName.getText()!=null && edtAddress.getText()!=null && edtZipCode.getText()!=null
                && edtCity.getText()!=null && edtEmail.getText()!=null && edtPhone.getText()!=null
                && edtPass.getText()!=null){
            if (edtPass.getText().toString().equals(edtConfirm.getText().toString())) {
                firebaseAuth.createUserWithEmailAndPassword(edtEmail.getText().toString(), edtPass.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "onComplete: User Creation Succcess");
                                    Toast.makeText(RegisterActivity.this,
                                            "You have successfully been registered",
                                            Toast.LENGTH_SHORT).show();
                                            login();
                                }else {
                                    Log.w(TAG, "onComplete: ", task.getException());
                                    Toast.makeText(RegisterActivity.this,
                                            "Something went wrong, you have not been registered",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        }
    }

    private void login() {
        firebaseAuth.signInWithEmailAndPassword(edtEmail.getText().toString(),
                edtPass.getText().toString())
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: SignInWithEmail: Success");
                            FirebaseUser user = firebaseAuthentication.getCurrentUser();
                            Log.d(TAG, "onComplete: Person logged in: " + user.getUid());
                            PlantSwapUser newUser = new PlantSwapUser(edtName.getText().toString(),
                                    edtAddress.getText().toString(),
                                    edtZipCode.getText().toString(),
                                    edtCity.getText().toString(),
                                    edtEmail.getText().toString(),
                                    edtPhone.getText().toString());

                            repo.addNewUserToCloudDatabase(newUser, user.getUid());

                            finishRegister();
                        } else {
                            Log.w(TAG, "signIn failure", task.getException());
                            Toast.makeText(RegisterActivity.this,
                                    "Login was unsuccessful after registering.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void finishRegister() {
        setResult(RESULT_OK);
        finish();
    }
}