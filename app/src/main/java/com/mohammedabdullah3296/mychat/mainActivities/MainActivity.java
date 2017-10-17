package com.mohammedabdullah3296.mychat.mainActivities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mohammedabdullah3296.mychat.R;
import com.mohammedabdullah3296.mychat.menuActivities.AllUsersActivity;
import com.mohammedabdullah3296.mychat.menuActivities.SettingsActivity;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Toolbar mToolBar;
    private TabLayout myTabLayout;
    private ViewPager myViewPager;
    private TabsPagerAdapter tabsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myViewPager = (ViewPager) findViewById(R.id.main_tabs_pager);
        mAuth = FirebaseAuth.getInstance();

        mToolBar = (Toolbar) findViewById(R.id.main_page_toolbar);
        mToolBar.setTitleTextColor(Color.WHITE);
        mToolBar.setSubtitleTextColor(Color.WHITE);

        tabsPagerAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        myViewPager.setAdapter(tabsPagerAdapter);
        myTabLayout = (TabLayout) findViewById(R.id.main_tabs);
        myTabLayout.setupWithViewPager(myViewPager);
        setSupportActionBar(mToolBar);
        myTabLayout.setTabTextColors(Color.BLACK, Color.WHITE);
        /*
        getSupportActionBar().setTitle("myChAt");
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        */

    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentuser = mAuth.getCurrentUser();
        if (currentuser == null) {
            logoutUser();
        }
    }

    private void logoutUser() {

        Intent startPageIntent = new Intent(MainActivity.this, StartPageActivity.class);
        startPageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(startPageIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                onBackPressed();
                return true;
            case R.id.main_logout_button:
                mAuth.signOut();
                logoutUser();
                return true;
            case R.id.main_Settings_button:

                Intent settingsntent = new Intent(MainActivity.this, SettingsActivity.class);
                //settingsntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(settingsntent);

                return true;
            case R.id.main_all_users_button:

                Intent allUsersIntent = new Intent(MainActivity.this, AllUsersActivity.class);
                //settingsntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(allUsersIntent);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
