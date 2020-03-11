package com.myproject.chatbotv1;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class Music extends AppCompatActivity {


    Button b,pause,resume,audiob;
    MediaPlayer mp;
    //public static abstract void func();
    public void func(int req_code){

        //recognize speech
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
        startActivityForResult(intent, req_code);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i("ONWORKSCREATE","?");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_activity);

        b = (Button) findViewById(R.id.button);
        pause = (Button)findViewById(R.id.b2);
        resume = (Button) findViewById(R.id.button3);
        audiob = (Button)findViewById(R.id.button4);

        b.setOnClickListener( new Button.OnClickListener(){
            public void onClick(View v){

                func(10);

            }
        });

        audiob.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                func(20);
            }
        });

        pause.setOnClickListener( new Button.OnClickListener(){
            public void onClick(View v){
                mp.pause();
            }
        });

        resume.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                mp.start();
            }
        });



    }

    //actually get the data in recognition
    void func1(String res){
        if(res.equals("beethoven")){
            Log.i("resinside","loop");

            mp = MediaPlayer.create(this,R.raw.beethoven);
            mp.start();

        }

        if(res.equals("ganapati") || res.equals("ganapathi")){

            mp = MediaPlayer.create(this,R.raw.ganapati);
            mp.start();
        }

        if(res.equals("muruga")||res.equals("murga")){
            mp = MediaPlayer.create(this,R.raw.muruga);
            mp.start();

        }
        if(res.equals("unnai") || res.equals("unai")||res.equals("unae")){
            mp = MediaPlayer.create(this,R.raw.unnai);
            mp.start();

        }

        if(res.equals("maiya maiya") || res.equals("maya maya")||res.equals("maya")){
            mp = MediaPlayer.create(this,R.raw.maya_maya);
            mp.start();

        }

        if(res.equals(" sakthi kodu") || res.contains("shakthi")||res.equals("shakti")){
            mp = MediaPlayer.create(this,R.raw.sakthi);
            mp.start();

        }


    }


    public void func2(String res){
        if(res.equals("secret sharer")){
            mp = MediaPlayer.create(this,R.raw.secret_sharer);
            mp.start();
        }
        if(res.equals("raven") || res.equals("braven")){
            mp =  MediaPlayer.create(this,R.raw.raven_poe);
            mp.start();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK  &&  data!=null){

            ArrayList<String> s = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String res = s.get(0);
            res = res.toLowerCase();
            Log.i("RESULTSPEECH", res);


            switch (requestCode){
                case 10:
                    func1(res);
                    break;
                case 20:
                    func2(res);

                    break;
            }
        } else{
            Toast.makeText(getApplicationContext(),"Couldnt recognize speech :(", Toast.LENGTH_LONG).show();
        }
    }
}
