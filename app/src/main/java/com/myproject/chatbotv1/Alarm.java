package com.myproject.chatbotv1;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Alarm extends AppCompatActivity {

        Button start;
        EditText text;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.alarm_activity);
            start= (Button) findViewById(R.id.btnStartAlarm);

            start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startAlert();
                }
            });

            Button b2 = (Button)findViewById(R.id.btn);
            b2.setOnClickListener(new Button.OnClickListener(){
                public void onClick(View v){
                    getTime(99);
                }
            });
        }

        public void startAlert(){
            Log.i("STARTALERT","CALLED");
            int factor=0,flagg=0;

            text = (EditText) findViewById(R.id.editText);
            String res = text.getText().toString();
            Log.i("RESUL",res);

            if(res.contains("p.m.") || res.contains("p.m") || res.contains("night")|| res.contains("Night") || res.contains("evening")){
                Log.i("RESULTPREVV","hi");
                factor = 12;
                flagg = 1;
            }



            res=res.replaceAll("[^0-9:]","");
            Log.i("RESULT",res.toString());

            if(res.length()<=2){
                res+=":00";
            }

            if(res.charAt(1)==':' || res.length()==3){
                res = "0"+res;
            }

            if(res.charAt(2)!=':'){
                int l = res.length();
                String minS = Character.toString(res.charAt(l-2))+Character.toString( res.charAt(l-1));
                res = Character.toString(res.charAt(0))+Character.toString( res.charAt(1)) + ':' + minS;

            }

            Log.i("RESULT",res);

            String hrS = Character.toString(res.charAt(0))+Character.toString( res.charAt(1));
            int hr = Integer.parseInt(hrS)+factor;

            String minS = Character.toString(res.charAt(3))+Character.toString( res.charAt(4));
            int min = Integer.parseInt(minS);


            if(flagg==1 && hr == 24){
                hr = hr-12;
            }


            Log.i("HOUR",""+hrS);
            Log.i("MINUTE",""+minS);

            //current Time
            Date currentTime = Calendar.getInstance().getTime();
            Log.i("RESULT TIME",currentTime.toString());
            Calendar cur_cal = new GregorianCalendar();
            cur_cal.setTimeInMillis(System.currentTimeMillis());//set the current time and date for this calendar

            Calendar cal = new GregorianCalendar();
            cal.add(Calendar.DAY_OF_YEAR, cur_cal.get(Calendar.DAY_OF_YEAR));
            cal.set(Calendar.HOUR_OF_DAY, hr);
            cal.set(Calendar.MINUTE, min);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, cur_cal.get(Calendar.MILLISECOND));
            cal.set(Calendar.DATE, cur_cal.get(Calendar.DATE));
            cal.set(Calendar.MONTH, cur_cal.get(Calendar.MONTH));

            Log.i("TIME",cal.getTime().toString());





            //int i = Integer.parseInt(text.getText().toString());

            Intent intent = new Intent(this, AlarmBroadcastReciever.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    this.getApplicationContext(), 234324243, intent, 0);

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            if(cal.getTimeInMillis()>System.currentTimeMillis()){

                    alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                    Log.i("MESSAGE HI","huhuhu");
                    Toast.makeText(this, "Alarm set for " + hr + " hour and "+min+" minutes.",Toast.LENGTH_SHORT).show();
            } else{
                Toast.makeText(this, "Alarm set for tomorrow " + hr + " hour and "+min+" minutes.",Toast.LENGTH_SHORT).show();
            }


            //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 30*1000, pendingIntent);


        }


        public void getTime(int req_code){
            Log.i("SPEECH","CALLED");
//recognize speech
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
            startActivityForResult(intent, req_code);

        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            Log.i("ONACTIVITY","CALLED");
            if(resultCode==RESULT_OK  &&  data!=null){

                ArrayList<String> s = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String res = s.get(0);
                res = res.toLowerCase();
                //  Log.i("RESULTSPEECH", res);


                switch (requestCode){
                    case 99:
                        text = (EditText) findViewById(R.id.editText);
                        text.setText(res);
                        break;
                }
            }
        }

    }
