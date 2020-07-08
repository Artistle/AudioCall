package com.example.audiocall;

import android.Manifest;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //получение разрешений
        if (ContextCompat.checkSelfPermission(LoginActivity.this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(LoginActivity.this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(LoginActivity.this,
                    new String[]{android.Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_PHONE_STATE},
                    1);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_login:
                Intent intent = new Intent(getApplicationContext(), CallsActivity.class);
                intent.putExtra("callerId", ((TextView) findViewById(R.id.your_id)).getText().toString());
                intent.putExtra("recipientId", ((TextView) findViewById(R.id.recipient_id)).getText().toString());
                startActivity(intent);

        }
    }
}
