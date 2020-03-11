package com.myproject.chatbotv1;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private static final int SOS_REQUEST_CODE = 1;
    private static final int SMS_REQUEST_CODE = 101;
    private static final int CALL_REQUEST_CODE = 202;
    private static final int REMINDER_REQUEST_CODE = 303;
    private static ListView wordsList;
    private static Button sosButton, smsButton, callButton, reminderButton, stopButton;
    private Timer myTimer;
    MediaPlayer mp;

    public void setAlarm(View v){
        Intent i = new Intent(this, Alarm.class);
        startActivity(i);
    }

    public void talkAlmond(View v){
        Intent it = new Intent(this, Almond.class);
        startActivity(it);

    }


    public void onClick(View v){
        Intent i = new Intent(this, Music.class);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSmsPermission()) {
                Log.e("permission", "Permission already granted.");
            } else {
                requestSmsPermission();
            }
        }
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkCallPermission()) {
                Log.e("permission", "Permission already granted.");
            } else {
                requestCallPermission();
            }
        }
        myTimer = new Timer();
        mp=MediaPlayer.create(getApplicationContext(),R.raw.a);

        init();
    }

    private void init() {
        sosButton = (Button) findViewById(R.id.sosButton);
        smsButton = (Button) findViewById(R.id.smsButton);
        callButton = (Button) findViewById(R.id.callButton);
     //   reminderButton = (Button) findViewById(R.id.reminderButton);
      //  stopButton = (Button) findViewById(R.id.stopButton);

        // Disable button if no recognition service is present
        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);


        sosButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                startSosActivity();

            }
        });

        smsButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                startSmsActivity();

            }
        });

        callButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                startCallActivity();
            }
        });
//        reminderButton.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                startReminderActivity();
//
//            }
//        });
//        stopButton.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                mp.stop();
//
//            }
//        });
    }
    int Clock;

    private void startSosActivity() {
        smsmessage = "Emergency Come Immediately";
        if(checkSmsPermission()) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("9500925735", null, smsmessage, null, null);
        }else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    private void startSmsActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Voice Recognition for SMS!!!");
        startActivityForResult(intent, SMS_REQUEST_CODE);
    }

    private void startCallActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Voice Recognition for Call!!!");
        startActivityForResult(intent, CALL_REQUEST_CODE);
    }

    private void startReminderActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Voice Recognition for Call!!!");
        startActivityForResult(intent, REMINDER_REQUEST_CODE);
    }
    String smsmessage="";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("MyStatus","1234");

        if (requestCode == SMS_REQUEST_CODE && resultCode == RESULT_OK) {
            smsmessage = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);

            String smsNumber = "9500925735";

            if(smsmessage.toLowerCase().contains("father"))
                smsNumber = "9500925735";

            if(smsmessage.toLowerCase().contains("mother"))
                smsNumber = "9003294197";

            if(smsmessage.toLowerCase().contains("police"))
                smsNumber = "100";

            if(smsmessage.toLowerCase().contains("hod")||smsmessage.toLowerCase().contains("renjith sir")||smsmessage.toLowerCase().contains("ranjeet"))
                smsNumber= "9884311369";

            if(smsmessage.toLowerCase().contains("jerosha"))
                smsNumber= "9500012438";

            if(smsmessage.toLowerCase().contains("gauthaman")||smsmessage.toLowerCase().contains("gautam")||smsmessage.toLowerCase().contains("gautaman sir")||smsmessage.toLowerCase().contains("gautham"))
                smsNumber= "9442417016";


            if(checkSmsPermission()) {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(smsNumber, null, smsmessage, null, null);
            }else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }



        if (requestCode == CALL_REQUEST_CODE && resultCode == RESULT_OK) {

            String callmessage = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);

            String dialNumber = "9500925735";


            if(callmessage.toLowerCase().contains("father")||callmessage.toLowerCase().contains("dad"))
                dialNumber = "9500925735";

            if(callmessage.toLowerCase().contains("mother")||callmessage.toLowerCase().contains("mom")||callmessage.toLowerCase().contains("mum"))
                dialNumber = "9003294197";

            if(callmessage.toLowerCase().contains("police"))
                dialNumber = "100";

            if(callmessage.toLowerCase().contains("jerosha"))
                dialNumber ="9500012438";

            if(callmessage.toLowerCase().contains("anitha")||callmessage.toLowerCase().contains("anita"))
                dialNumber="9677141211";

            if(callmessage.toLowerCase().contains("hod")||callmessage.toLowerCase().contains("renjith")||callmessage.toLowerCase().contains("ranjeet")||callmessage.toLowerCase().contains("ranjit"))
                dialNumber="9884311369";
            if(callmessage.toLowerCase().contains("gautam")||callmessage.toLowerCase().contains("gaudhaman")||callmessage.toLowerCase().contains("gautham")||callmessage.toLowerCase().contains("gautaman"))
                dialNumber = "9442417016";

            String dial = "tel:" + dialNumber;

            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }else {
            Toast.makeText(MainActivity.this, "Please enter a valid telephone number", Toast.LENGTH_SHORT).show();
        }



        if (requestCode == REMINDER_REQUEST_CODE && resultCode == RESULT_OK) {

            String remindermessage = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);

            if (remindermessage.matches("[0-9]+") && remindermessage.length() < 3) {
                int hour = Integer.parseInt(remindermessage);
                if (hour <= 23){
                    Clock = hour;
                }
                mp.start();
                Date startDate = new Date();
                int day = startDate.getDay();
                if (hour < startDate.getHours())
                    day = day + 1;
                Date endDate   = new Date(startDate.getYear(), startDate.getMonth(), startDate.getDay(), hour, 0,0);

                long duration  = endDate.getTime() - startDate.getTime();
                if(duration < 0){
                    duration = 1000 * 60;
                }
                long diffInSeconds = TimeUnit.NANOSECONDS.toMillis(duration);
                myTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        mp.start();
                    }

                }, diffInSeconds, 1000 * 60 *60 * 24);
            }

        }else {
            Toast.makeText(MainActivity.this, "Please enter a valid telephone number", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private boolean checkSmsPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestSmsPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, PERMISSION_SMS_REQUEST_CODE);

    }

    public boolean checkCallPermission() {

        int CallPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE);

        return CallPermissionResult == PackageManager.PERMISSION_GRANTED;

    }

    private void requestCallPermission() {

        ActivityCompat.requestPermissions(MainActivity.this, new String[]
                {
                        Manifest.permission.CALL_PHONE
                }, PERMISSION_CALL_REQUEST_CODE);

    }

    private static final int PERMISSION_SOS_REQUEST_CODE = 0;

    private static final int PERMISSION_SMS_REQUEST_CODE = 1;

    private static final int PERMISSION_CALL_REQUEST_CODE = 2;

    private static final int PERMISSION_REMINDER_REQUEST_CODE = 3;

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_SOS_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this,
                            "SOS Permission accepted", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(this,
                            "SOS Permission denied", Toast.LENGTH_LONG).show();
                    ;

                }
                break;
            case PERMISSION_SMS_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this,
                            "SMS Permission accepted", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(this,
                            "SMS Permission denied", Toast.LENGTH_LONG).show();
                    ;

                }
                break;
            case PERMISSION_CALL_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this,
                            "Call Permission accepted", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(this,
                            "Call Permission denied", Toast.LENGTH_LONG).show();
                    ;

                }
                break;
            case PERMISSION_REMINDER_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this,
                            "Reminder Permission accepted", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(this,
                            "Reminder Permission denied", Toast.LENGTH_LONG).show();
                    ;

                }
                break;
        }
    }


}
