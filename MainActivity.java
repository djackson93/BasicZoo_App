package com.example.hm8_jackson;

import androidx.appcompat.app.AppCompatActivity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    int images[] = {R.drawable.gorilla, R.drawable.llama, R.drawable.penguins, R.drawable.toucan, R.drawable.zebra};
    int currentImage;



    private ImageView imageView;
    private Button prevButton;
    private Button infoButton;
    private Button setButton;
    private Button nextButton;

    //---SOUNDS------------------------------
    MediaPlayer mp;
    SoundPool sp;

    int sound_click;
    int sound_gorilla;
    int sound_zebra;
    int sound_toucan;
    int sound_penguin;
    int sound_llama;

    boolean sounds_loaded;
    int num_sounds_loaded;


    @Override
    protected void onCreate(Bundle inBundle) {
        super.onCreate(inBundle);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.imageView);
        prevButton = (Button) findViewById(R.id.prevButton);
        infoButton = (Button) findViewById(R.id.info);
        setButton = (Button) findViewById(R.id.pref);
        nextButton = (Button) findViewById(R.id.nextButton);

        if (inBundle != null)
            currentImage = inBundle.getInt("currentImage");
        else
            currentImage = 0;

        prevButton.setOnClickListener(this);
        infoButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        setButton.setOnClickListener(this);
        imageView.setOnClickListener(this);

        Assets.mp = null;

        num_sounds_loaded = 0;
        sounds_loaded = false;

        //Check for version being ran; If less than LOLLIPOP then use Deprecated method
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //Deprecated method for SoundPool
            sp = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);
        } else {
            //Newer, recommended way of creating SoundPool
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            SoundPool sounds = new SoundPool.Builder()
                    .setAudioAttributes(attributes).build();
        }

        //Method to check all sounds are loaded and safe to play
        sp.setOnLoadCompleteListener((soundPool, sampleID, status) -> {
            num_sounds_loaded++;
            if (num_sounds_loaded == 6)
                sounds_loaded = true;
        });

        //load sounds
        sound_click = sp.load(this, R.raw.click, 1);
        sound_gorilla = sp.load(this, R.raw.gorilla, 1);
        sound_llama = sp.load(this, R.raw.llama, 1);
        sound_zebra = sp.load(this, R.raw.zebra, 1);
        sound_toucan = sp.load(this, R.raw.toucan, 1);
        sound_penguin = sp.load(this, R.raw.penguin, 1);




    }

    //Method for onResume
    protected void onResume() {
        super.onResume();
        imageView.setImageResource(images[currentImage]);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean b = prefs.getBoolean("key_music_enabled", true);
        if (b == true) {
            //nested if
            if (Assets.mp != null) {
                Assets.mp.release();
                Assets.mp = null;
            }

            Assets.mp = MediaPlayer.create (this, R.raw.rainforest);
            Assets.mp.setLooping(true);
            Assets.mp.start();
        }

        Log.i("ProjectLogging", "Activity Resumed");
    }

    @Override
    protected void onSaveInstanceState(Bundle outBundle) {
        super.onSaveInstanceState(outBundle);
        outBundle.putInt("currentImage", currentImage);
    }

    //Method for onPause
    protected void onPause() {


        String s;

        if (isFinishing()) {
            s = "Activity Finishing";
            if (Assets.mp != null) {
                Assets.mp.stop();
                Assets.mp.release();
                Assets.mp = null;
            }
        } else {
            s = "Activity Pausing";
        }
        Log.i("ProjectLogging", s);

        super.onPause();
    }

    //Method for handling our objects being clicked
    @Override
    public void onClick(View v) {

        //Case for previous button; Will "Click" and go to the image prior in the array
        switch (v.getId()) {
            case R.id.prevButton:
                sp.play(sound_click, 1, 1, 0, 0, 2);
                if (currentImage == 0) {
                    currentImage = 4;
                    imageView.setImageResource(images[currentImage]);
                    break;
                } else if (currentImage == 1) {
                    currentImage = 0;
                    imageView.setImageResource(images[currentImage]);
                    break;
                } else if (currentImage == 2) {
                    currentImage = 1;
                    imageView.setImageResource(images[currentImage]);
                    break;
                } else if (currentImage == 3) {
                    currentImage = 2;
                    imageView.setImageResource(images[currentImage]);
                    break;
                } else {
                    currentImage = 3;
                    imageView.setImageResource(images[currentImage]);
                    break;
                }

                //Case for nextButton being pressed; Will make a "click" noise and progress to next image
            case R.id.nextButton:
                sp.play(sound_click, 1, 1, 0, 0, 2);
                if (currentImage == 0) {
                    currentImage = 1;
                    imageView.setImageResource(images[currentImage]);
                    break;
                } else if (currentImage == 1) {
                    currentImage = 2;
                    imageView.setImageResource(images[currentImage]);
                    break;
                } else if (currentImage == 2) {
                    currentImage = 3;
                    imageView.setImageResource(images[currentImage]);
                    break;
                } else if (currentImage == 3) {
                    currentImage = 4;
                    imageView.setImageResource(images[currentImage]);
                    break;
                } else {
                    currentImage = 0;
                    imageView.setImageResource(images[currentImage]);
                    break;
                }

                //Case for when the currentImage displayed is clicked; Will play appropriate animal noise
            case R.id.imageView:

                final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                boolean b2 = prefs.getBoolean("key_sfx_enabled", true);

                if (sounds_loaded) {
                    if (currentImage == 0) {
                        if (b2 != false) {
                            sp.play(sound_gorilla, 1, 1, 0, 0, 1);
                        } else {
                            Toast.makeText(this, "Grunts", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    } else if (currentImage == 1) {
                        if (b2 != false) {
                            sp.play(sound_llama, 1, 1, 0, 0, 1);
                        } else {
                            Toast.makeText(this, "Mwa", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    } else if (currentImage == 2) {
                        if (b2 != false) {
                            sp.play(sound_penguin, 1, 1, 0, 0, 1);
                        } else {
                            Toast.makeText(this, "Gakker", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    } else if (currentImage == 3) {
                        if (b2 != false) {
                            sp.play(sound_toucan, 1, 1, 0, 0, 1);
                        } else {
                            Toast.makeText(this, "Squawks", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    } else if (currentImage == 4) {
                        if (b2 != false) {
                            sp.play(sound_zebra, 1, 1, 0, 0, 1);
                        } else {
                            Toast.makeText(this, "Whinny", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                }

                //Cases for when the "info" button is pressed
                //Will send user to information about the animal on screen when pressed via webpage
            case R.id.info:
                sp.play(sound_click, 1, 1, 0, 0, 2);
                if (currentImage == 0) {
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://en.wikipedia.org/wiki/Gorilla"));
                    startActivity(myIntent);
                } else if (currentImage == 1) {
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://en.wikipedia.org/wiki/Llama"));
                    startActivity(myIntent);
                } else if (currentImage == 2) {
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://en.wikipedia.org/wiki/Penguin"));
                    startActivity(myIntent);
                } else if (currentImage == 3) {
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://en.wikipedia.org/wiki/Toucan"));
                    startActivity(myIntent);
                } else {
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://en.wikipedia.org/wiki/Zebra"));
                    startActivity(myIntent);
                }

                //Case for when our settings button is clicked
            case R.id.pref:
                sp.play(sound_click, 1, 1, 0, 0, 2);
                startActivity(new Intent(this, PrefsActivity.class));
                break;
        }
    }

    //Method for handling proper back button press for the App
    @Override
    public void onBackPressed () {
        //super.onBackPressed();

        //Creating custom Dialog box for when user hits Back Button
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.hand)
                .setTitle("Closing Application")
                .setMessage("Do you want to exit the Application?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override   //Lines following handle on click for "Yes" Button
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
