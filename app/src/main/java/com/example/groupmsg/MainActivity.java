package com.example.groupmsg;

import android.os.Bundle;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    private EditText phoneNumbersEditText, messageEditText;
    private Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phoneNumbersEditText = findViewById(R.id.phone_numbers_edit_text);
        messageEditText = findViewById(R.id.message_edit_text);
        sendButton = findViewById(R.id.send_button);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumbers = phoneNumbersEditText.getText().toString();
                String message = messageEditText.getText().toString();

                if (!phoneNumbers.isEmpty() && !message.isEmpty()) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
                    } else {
                        sendGroupSMS(phoneNumbers, message);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Please enter phone numbers and message", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendGroupSMS(String phoneNumbers, String message) {
        SmsManager smsManager = SmsManager.getDefault();
        String[] numbers = phoneNumbers.split(";");
        for (String number : numbers) {
            smsManager.sendTextMessage(number, null, message, null, null);
        }
        Toast.makeText(MainActivity.this, "Group SMS sent successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    String phoneNumbers = phoneNumbersEditText.getText().toString();
                    String message = messageEditText.getText().toString();
                    sendGroupSMS(phoneNumbers, message);
                } else {
                    Toast.makeText(MainActivity.this, "SMS permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}

