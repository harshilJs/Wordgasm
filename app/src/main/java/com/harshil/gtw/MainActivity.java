package com.harshil.gtw;


import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView tv_word, tv_score, score, timer, tv_hint;
    TextInputEditText edt_guess;
    TextInputLayout til_guess;
    Button bt_check, bt_new;
    CountDownTimer count;
    FloatingActionButton fab;

    String[] dictionary = {
            "wrong", "able", "acid", "automatic", "certain", "broken",
            "yellow", "angry", "common", "dark", "electric", "flat", "awake",
            "mouse", "bad", "long", "young", "bent", "fixed", "important", "gray", "natural", "different", "green", "first", "dry",
            "bird", "beautiful", "complete", "loose", "cold", "ill", "male", "parallel", "present", "private", "medical", "hanging", "dirty",
            "black", "dependent", "bitter", "hard", "false", "clear", "quiet", "public", "quick", "ready", "red", "loud", "high",
            "blue", "female", "brown", "complex", "foolish", "probable", "same", "responsible", "clean", "round", "right", "second", "military",
            "boiling", "good", "future", "married", "physical", "regular", "cruel", "safe", "simple", "shut", "chief", "short", "secret",
            "bright", "hollow", "general", "new", "political", "sad", "solid", "conscious", "smooth", "small", "slow", "chemical", "sharp",
            "apple", "able", "kind", "poor", "rough", "soft", "special", "sticky", "strange", "strong", "straight", "cut", "stiff", "cheap",
            "deep", "fat", "last", "possible", "sudden", "sweet", "tall", "true", "violent", "", "", "white", "wet", "warm", "waiting", "thin", "dear", "opposite",
            "delicate", "foolish", "serious", "tired", "wide", "dead", "elastic", "free", "late", "equal", "frequent", "left", "thick", "narrow", "great",
            "early", "full", "happy", "like", "mixed", "past", "material", "low", "necessary", "healthy", "fertile", "feeble",
            "normal", "tight",
            "open", "wise",
            "old"

    };

    Random r;
    String currentWord;
    private int counter = 0;

    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, "ca-app-pub-8836857050636822~8139421031");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-8836857050636822/8972068661");




        TextView tx = (TextView) findViewById(R.id.tool_title);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/MontserratMedium.ttf");
        tx.setTypeface(custom_font);

        tv_word = (TextView) findViewById(R.id.tv_word);
        edt_guess = (TextInputEditText) findViewById(R.id.edt_guess);
        bt_check = (Button) findViewById(R.id.bt_check);
        bt_new = (Button) findViewById(R.id.bt_new);
        tv_score = (TextView) findViewById(R.id.tv_score);
        score = (TextView) findViewById(R.id.score);
        til_guess = (TextInputLayout) findViewById(R.id.til_guess);
        timer = (TextView) findViewById(R.id.timer);
        tv_hint = (TextView) findViewById(R.id.hint);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        tv_word.setTypeface(custom_font);
        edt_guess.setTypeface(custom_font);
        bt_check.setTypeface(custom_font);
        bt_new.setTypeface(custom_font);
        tv_score.setTypeface(custom_font);
        score.setTypeface(custom_font);
        timer.setTypeface(custom_font);


        r = new Random();
        newGame();

        bt_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_guess.getText().toString().equalsIgnoreCase(currentWord)) {
                    TastyToast.makeText(getApplicationContext(), "+1", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                    counter++;
                    int s = counter;
                    score.setText(Integer.toString(s));
                    count.cancel();
                    newGame();


                } else {
                    counter = 0;
                    count.cancel();
                    TastyToast.makeText(getApplicationContext(), "Game over!", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                    edt_guess.setText("");
                    score.setText("0");
                    edt_guess.setEnabled(false);
                    til_guess.setError("Incorrect word!");
                    bt_check.setVisibility(View.GONE);
                    bt_new.setVisibility(View.VISIBLE);

                }
            }
        });

        bt_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count.cancel();
                til_guess.setError(null);
                newGame();
                fab.show();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdRequest adRequest = new AdRequest.Builder().build();

                // Load ads into Interstitial Ads
                mInterstitialAd.loadAd(adRequest);

                mInterstitialAd.setAdListener(new AdListener() {
                    public void onAdLoaded() {
                        showInterstitial();
                    }
                });

            }
        });

    }

    private String shuffleWord(String word) {
        List<String> letters = Arrays.asList(word.split(""));
        Collections.shuffle(letters);
        String shuffled = "";
        for (String letter : letters) {
            shuffled += letter;
        }
        return shuffled;
    }

    public void newGame() {


        currentWord = dictionary[r.nextInt(dictionary.length)];
        tv_word.setText(shuffleWord(currentWord));
        til_guess.setError(null);
        edt_guess.setEnabled(true);
        edt_guess.setText("");
        bt_new.setVisibility(View.GONE);
        bt_check.setVisibility(View.VISIBLE);

        count = new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                timer.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                TastyToast.makeText(getApplicationContext(), "Oops!", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                timer.setText("");
                score.setText("0");
                edt_guess.setText("");
                edt_guess.setEnabled(false);
                bt_check.setVisibility(View.GONE);
                bt_new.setVisibility(View.VISIBLE);
                til_guess.setError("Timeout");
            }
        }.start();
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {


                edt_guess.setText(currentWord);
                TastyToast.makeText(getApplicationContext(), "Hint Applied", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);


            }
        });
    }
}
