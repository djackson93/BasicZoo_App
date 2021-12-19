package com.example.hm8_jackson;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Handler;
import android.os.Bundle;
import android.content.Intent;

//Main method
public class TitleScreen extends AppCompatActivity {
    private Handler mHandler = new Handler();
    boolean quit;

    /*onCreate method that sets the layout to fullscreen title screen for 5 seconds
    before passing intent onto next activity.
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.title_screen);
        //Checks to see if the user has quit by hitting the backbutton
        quit = false;
        mHandler.postDelayed(() -> {
            if (!quit)
                activityProgression();
        }, 5000);
    }

    //Method used to progress past title screen after 5 seconds
    private void activityProgression() {
        Intent intent = new Intent(TitleScreen.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    //Method for handling the user quitting the application
    @Override
    public void onBackPressed() {
        //If this isn't here, then even after pressing back the user will get the app
        //to pop up anyways
        quit = true;
        super.onBackPressed();
    }
}
