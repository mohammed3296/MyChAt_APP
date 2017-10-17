package com.mohammedabdullah3296.mychat.menuActivities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mohammedabdullah3296.mychat.R;
import com.mohammedabdullah3296.mychat.mainActivities.MainActivity;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private Toolbar mToolBar;
    private CircleImageView settingsDisplayImage;
    private TextView settingsDisplayUserName;
    private TextView settingsDisplayUserStatus;
    private Button settingsChangeImageButton;
    private Button settingsChangeStatusButton;
    private DatabaseReference retriveUserDefaultDataBase;
    private FirebaseAuth mAuth;
    private final static int Gallery_PIK = 1;
    private StorageReference storProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        mToolBar = (Toolbar) findViewById(R.id.settings_toolbar);
        mToolBar.setTitleTextColor(Color.WHITE);
        mToolBar.setSubtitleTextColor(Color.WHITE);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        storProfileImage = FirebaseStorage.getInstance().getReference().child("Profile_Images");

        settingsDisplayImage = (CircleImageView) findViewById(R.id.settings_profile_image);
        settingsDisplayUserName = (TextView) findViewById(R.id.settings_userName_textView);
        settingsDisplayUserStatus = (TextView) findViewById(R.id.settings_status_textView);
        settingsChangeImageButton = (Button) findViewById(R.id.settings_change_image_button);
        settingsChangeStatusButton = (Button) findViewById(R.id.settings_change_status_button);


        String online_user_id = mAuth.getCurrentUser().getUid();
        retriveUserDefaultDataBase = FirebaseDatabase.getInstance().getReference().child("Users").child(online_user_id);
        retriveUserDefaultDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String user_image = dataSnapshot.child("user_image").getValue().toString();
                String user_name = dataSnapshot.child("user_name").getValue().toString();
                String user_status = dataSnapshot.child("user_status").getValue().toString();
                String user_thumb_image = dataSnapshot.child("user_thumb_image").getValue().toString();


                settingsDisplayUserName.setText(user_name);
                settingsDisplayUserStatus.setText(user_status);
                // settingsDisplayImage.setImageDrawable();
                if (!user_image.equals("default_profile"))
                    Picasso.with(SettingsActivity.this).load(user_image).placeholder(R.drawable.profile).into(settingsDisplayImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        settingsChangeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentQalary = new Intent();
                intentQalary.setAction(Intent.ACTION_GET_CONTENT);
                intentQalary.setType("image/*");
                startActivityForResult(intentQalary, Gallery_PIK);
            }
        });

        settingsChangeStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldStatus = settingsDisplayUserStatus.getText().toString();
                Intent StatusIntent = new Intent(SettingsActivity.this, StatusActivity.class);
                StatusIntent.putExtra("user_status", oldStatus);
                startActivity(StatusIntent);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Gallery_PIK && resultCode == RESULT_OK && data != null) {

            Uri imageUri = data.getData();
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1, 1)
                    .start(this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                String user_id = mAuth.getCurrentUser().getUid();
                StorageReference filePath = storProfileImage.child(user_id + ".jpg");
                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SettingsActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                            String download = task.getResult().getDownloadUrl().toString();
                            retriveUserDefaultDataBase.child("user_image").setValue(download).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(SettingsActivity.this, "Image Updated Sccessfully", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(SettingsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intentMain = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intentMain);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
