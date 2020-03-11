package com.myproject.chatbotv1;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Locale;

public class Almond extends AppCompatActivity {

        Button b,speakbtn;
        TextView t1,t2;
        TextToSpeech tt;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.almond_activity);

            b = (Button) findViewById(R.id.btn);
            speakbtn = (Button) findViewById(R.id.speakbtn);
            t1 = (TextView) findViewById(R.id.txt1);
            t2 = (TextView) findViewById(R.id.txt2);

            b.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    promptSpeechInput(10);
                }

            });


            speakbtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    tt = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int status) {
                            if(status == TextToSpeech.SUCCESS){
                                tt.setLanguage(Locale.UK);
                                String toSpeak = t2.getText().toString();
                                int speechStatus = tt.speak(toSpeak,TextToSpeech.QUEUE_FLUSH,null);
                                if(speechStatus==TextToSpeech.ERROR){
                                    Log.e("TTS", "Error in converting Text to Speech!");
                                }
                            }
                        }
                    });
                }
            });


        }


        @Override
        public void onDestroy() {
            super.onDestroy();
            if (tt != null) {
                tt.stop();
                tt.shutdown();
            }
        }


        public void onPause(){
            if(tt !=null){
                tt.stop();
                tt.shutdown();
            }
            super.onPause();
        }

        public void promptSpeechInput(int req_code){


            //recognize speech
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "Say Something");
            startActivityForResult(intent, req_code);



        }



        @Override
        protected void onActivityResult(int requestCode, int resultCode, @android.support.annotation.Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if(resultCode==RESULT_OK  &&  data!=null){

                ArrayList<String> s = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String res = s.get(0);
                res = res.toLowerCase();
                Log.i("RESULTSPEECH", res);


                switch (requestCode){
                    case 10:
                        t1.setText(res);
                        RetrieveFeedTask task=new RetrieveFeedTask();
                        task.execute(res);


                        break;

                }

            } else{
                android.widget.Toast.makeText(getApplicationContext(),"Couldnt recognize speech :(", android.widget.Toast.LENGTH_LONG).show();
            }
        }


        // Create GetText Metod
        public String GetText(String query) throws UnsupportedEncodingException {

            String text = "";
            BufferedReader reader = null;

            // Send data
            try {

                // Defined URL  where to send data
                URL url = new URL("https://api.api.ai/v1/query?v=20150910");

                // Send POST data request

                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                conn.setDoInput(true);

                conn.setRequestProperty("Authorization", "Bearer d0f45c4082fc471f8c711825bf0cc1b7");
                conn.setRequestProperty("Content-Type", "application/json");

                //Create JSONObject here
                JSONObject jsonParam = new JSONObject();
                JSONArray queryArray = new JSONArray();
                queryArray.put(query);
                jsonParam.put("query", queryArray);
//            jsonParam.put("name", "order a medium pizza");
                jsonParam.put("lang", "en");
                jsonParam.put("sessionId", "1234567890");


                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                Log.d("karma", "after conversion is " + jsonParam.toString());
                wr.write(jsonParam.toString());
                wr.flush();
                Log.d("karma", "json is " + jsonParam);

                // Get the server response

                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;


                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    // Append server response in string
                    sb.append(line + "\n");
                }


                text = sb.toString();



                JSONObject object1 = new JSONObject(text);
                JSONObject object = object1.getJSONObject("result");
                JSONObject fulfillment = null;
                String speech = null;
//            if (object.has("fulfillment")) {
                fulfillment = object.getJSONObject("fulfillment");
//                if (fulfillment.has("speech")) {
                speech = fulfillment.optString("speech");
//                }
//            }


                Log.d("karma ", "response is " + text);
                return speech;

            } catch (Exception ex) {
                Log.d("karma", "exception at last " + ex);
            } finally {
                try {

                    reader.close();
                } catch (Exception ex) {
                }
            }

            return null;
        }

        class RetrieveFeedTask extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... voids) {
                String s = null;
                try {

                    s = GetText(voids[0]);


                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.d("karma", "Exception occurred " + e);
                }

                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                t2.setText(s);

            }
        }




    }

