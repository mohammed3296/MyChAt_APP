package com.mohammedabdullah3296.mychat.accountActivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mohammedabdullah3296.mychat.R;
import com.mohammedabdullah3296.mychat.mainActivities.MainActivity;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Toolbar mToolBar;
    private AutoCompleteTextView registerUserName;
    private AutoCompleteTextView registerUserEmail;
    private EditText registerUserPassword;
    private Button createAccountButton;
    private ProgressDialog loading;
    private DatabaseReference storeUserDefaultDataBase ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        mToolBar = (Toolbar) findViewById(R.id.register_toolbar);
        mToolBar.setTitleTextColor(Color.WHITE);
        mToolBar.setSubtitleTextColor(Color.WHITE);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Sign Up");
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        registerUserName = (AutoCompleteTextView) findViewById(R.id.register_name);
        registerUserEmail = (AutoCompleteTextView) findViewById(R.id.register_email);
        registerUserPassword = (EditText) findViewById(R.id.register_password);
        createAccountButton = (Button) findViewById(R.id.email_sign_up_button);
        loading = new ProgressDialog(this);
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = registerUserName.getText().toString();
                String email = registerUserEmail.getText().toString();
                String password = registerUserPassword.getText().toString();
                registerAccount(name, email, password);
            }


        });
    }

    private void registerAccount(final String name, String email, String password) {
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(RegisterActivity.this, "invalid name", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(RegisterActivity.this, "invalid email", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(RegisterActivity.this, "invalid password", Toast.LENGTH_LONG).show();
        } else {


            loading.setTitle("Creating Account ");
            loading.setMessage("Please Wait .... ");
            loading.show();

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        String current_user_id = mAuth.getCurrentUser().getUid();
                        storeUserDefaultDataBase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_user_id);
                        storeUserDefaultDataBase.child("user_name").setValue(name);
                        storeUserDefaultDataBase.child("user_status").setValue("Hey there , I am using MyChAt App");
                        storeUserDefaultDataBase.child("user_image").setValue("default_profile");
                        storeUserDefaultDataBase.child("user_thumb_image").setValue("default_profile").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(mainIntent);
                                    finish();
                                }
                            }
                        });

                    } else {

                        Toast.makeText(RegisterActivity.this, "Error Occure Try Again !", Toast.LENGTH_SHORT).show();
                        FirebaseAuthException e = (FirebaseAuthException) task.getException();
                        Toast.makeText(RegisterActivity.this, "Failed Registration: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        loading.hide();
                        Log.e("LoginActivity", "Failed Registration", e);
                        return;
                    }

                    loading.dismiss();
                }
            });

        }
    }

    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }*/
}
