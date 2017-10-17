package com.mohammedabdullah3296.mychat.menuActivities;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mohammedabdullah3296.mychat.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllUsersActivity extends AppCompatActivity {

    private Toolbar mToolBar;
    private RecyclerView allUsersList;
    private DatabaseReference allDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);
        mToolBar = (Toolbar) findViewById(R.id.all_users_toolbar);
        mToolBar.setTitleTextColor(Color.WHITE);
        mToolBar.setSubtitleTextColor(Color.WHITE);

        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("All Users");
        // getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        allUsersList = (RecyclerView) findViewById(R.id.all_users_list);
        allUsersList.setHasFixedSize(true);
        allUsersList.setLayoutManager(new LinearLayoutManager(this));
        allDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<AllUsers, AllUserViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<AllUsers, AllUserViewHolder>(
                AllUsers.class,
                R.layout.all_users_display_layout,
                AllUserViewHolder.class,
                allDatabaseReference
        ) {
            @Override
            protected void populateViewHolder(AllUserViewHolder viewHolder, AllUsers model, int position) {

                viewHolder.setUser_name(model.getUser_name());
                viewHolder.setUser_status(model.getUser_status());
                viewHolder.setUser_image(getApplicationContext() , model.getUser_image());
            }
        };

        allUsersList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class AllUserViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public AllUserViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setUser_name(String user_name) {
            TextView name = (TextView) mView.findViewById(R.id.all_users_username);
            name.setText(user_name);
        }

        public void setUser_status(String user_status) {
            TextView status = (TextView) mView.findViewById(R.id.all_users_userstatus);
            status.setText(user_status);
        }

        public void setUser_image(Context con , String user_image) {
            CircleImageView image = (CircleImageView) mView.findViewById(R.id.all_users_profile_image);
            Picasso.with(con).load(user_image).into(image);
        }
    }
}
