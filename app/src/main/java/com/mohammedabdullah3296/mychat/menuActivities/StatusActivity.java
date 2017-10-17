package com.mohammedabdullah3296.mychat.menuActivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mohammedabdullah3296.mychat.R;

public class StatusActivity extends AppCompatActivity {

    private Toolbar mToolBar;
    private Button saveChangesButton;
    private TextView changeStatusTextView;
    private DatabaseReference storeUserDefaultDataBase;
    private FirebaseAuth mAuth;
    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        saveChangesButton = (Button) findViewById(R.id.save_status_button);
        changeStatusTextView = (TextView) findViewById(R.id.status_input);
        loading = new ProgressDialog(this);
        String old_status = getIntent().getExtras().get("user_status").toString();
        changeStatusTextView.setText(old_status);

        mToolBar = (Toolbar) findViewById(R.id.status_toolbar);
        mToolBar.setTitleTextColor(Color.WHITE);
        mToolBar.setSubtitleTextColor(Color.WHITE);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Status");
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        mAuth = FirebaseAuth.getInstance();
        String current_user_id = mAuth.getCurrentUser().getUid();
        storeUserDefaultDataBase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_user_id);


        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String status = changeStatusTextView.getText().toString();
                changeProfileStatus(status);
            }
        });
    }

    private void changeProfileStatus(String status) {
        if (TextUtils.isEmpty(status)) {
            Toast.makeText(StatusActivity.this, "Invalid Status Value", Toast.LENGTH_SHORT).show();
        } else {


            loading.setTitle("Change Profile Status ");
            loading.setMessage("Please Wait .... ");
            loading.show();

            storeUserDefaultDataBase.child("user_status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {

                        Intent intentSettings = new Intent(StatusActivity.this, SettingsActivity.class);
                        startActivity(intentSettings);
                        Toast.makeText(StatusActivity.this, "Profile status Updated ", Toast.LENGTH_SHORT).show();
                    } else {

                        Toast.makeText(StatusActivity.this, "Error try again", Toast.LENGTH_SHORT).show();
                    }
                    loading.dismiss();
                }
            });
        }
    }
}
