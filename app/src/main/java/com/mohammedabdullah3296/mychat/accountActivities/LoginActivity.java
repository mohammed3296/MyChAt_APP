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
import com.mohammedabdullah3296.mychat.R;
import com.mohammedabdullah3296.mychat.mainActivities.MainActivity;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    // UI references.
    private Toolbar mToolBar;
    private Button loginButton;
    private AutoCompleteTextView email;
    private EditText password;
    private ProgressDialog  loading;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mToolBar = (Toolbar) findViewById(R.id.login_toolbar);
        mToolBar.setTitleTextColor(Color.WHITE);
        mToolBar.setSubtitleTextColor(Color.WHITE);
        loading = new ProgressDialog(this);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Sign in");
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        loginButton = (Button) findViewById(R.id.email_sign_in_button);
        email = (AutoCompleteTextView) findViewById(R.id.login_email);
        password = (EditText) findViewById(R.id.login_password);
        mAuth = FirebaseAuth.getInstance();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String stringEmail = email.getText().toString();
                String stringPassword = password.getText().toString();
                loginAccount( stringEmail, stringPassword);
            }
        });
    }

    private void loginAccount(String stringEmail, String stringPassword) {

        if (TextUtils.isEmpty(stringEmail)) {
            Toast.makeText(LoginActivity.this, "invalid email", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(stringPassword)) {
            Toast.makeText(LoginActivity.this, "invalid password", Toast.LENGTH_LONG).show();
        } else {

            loading.setTitle("Login Account ");
            loading.setMessage("Please Wait .... ");
            loading.show();

            mAuth.signInWithEmailAndPassword(stringEmail, stringPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(mainIntent);
                        finish();
                    } else {

                        Toast.makeText(LoginActivity.this, "Error Occure Try Again !", Toast.LENGTH_SHORT).show();
                        FirebaseAuthException e = (FirebaseAuthException) task.getException();
                        Toast.makeText(LoginActivity.this, "Failed Registration: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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