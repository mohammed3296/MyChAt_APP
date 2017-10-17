package com.mohammedabdullah3296.mychat.mainActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mohammedabdullah3296.mychat.accountActivities.LoginActivity;
import com.mohammedabdullah3296.mychat.R;
import com.mohammedabdullah3296.mychat.accountActivities.RegisterActivity;

public class StartPageActivity extends AppCompatActivity {

    private Button needNewAccountButton ;
    private Button alreadyHaveAccount ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);
        alreadyHaveAccount = (Button)findViewById(R.id.already_have_acount_button);
        needNewAccountButton = (Button) findViewById(R.id.new_account_button);

        alreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent alradyHaveAccountIntent = new Intent(StartPageActivity.this , LoginActivity.class);
                startActivity(alradyHaveAccountIntent);
            }
        });

        needNewAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newAccountIntent = new Intent(StartPageActivity.this , RegisterActivity.class);
                startActivity(newAccountIntent);
            }
        });
    }
}
