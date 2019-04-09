package com.harshil.gtw;


import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
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

    private TextView tv_word, tv_score, score, timer, tv_hint, hscore, tv_hscore;
    private TextInputEditText edt_guess;
    private TextInputLayout til_guess;
    private FloatingActionButton fab;
    private Button bt_check, bt_new;

    private InterstitialAd mInterstitialAd;
    private SharedPreferences preferences;

    private CountDownTimer count;
    private Integer counter = 0;
    private String currentWord;
    private Random r;


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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Admob
        MobileAds.initialize(this, "ca-app-pub-8836857050636822~8139421031");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-8836857050636822/8972068661");

        //Toolbar
        TextView tx = findViewById(R.id.tool_title);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/MontserratMedium.ttf");
        tx.setTypeface(custom_font);

        //Ids
        tv_word = findViewById(R.id.tv_word);
        edt_guess = findViewById(R.id.edt_guess);
        bt_check = findViewById(R.id.bt_check);
        bt_new = findViewById(R.id.bt_new);
        tv_score = findViewById(R.id.tv_score);
        score = findViewById(R.id.score);
        tv_hscore = findViewById(R.id.tv_hscore);
        hscore = findViewById(R.id.hscore);
        til_guess = findViewById(R.id.til_guess);
        timer = findViewById(R.id.timer);
        tv_hint = findViewById(R.id.hint);
        fab = findViewById(R.id.fab);

        //Font
        tv_word.setTypeface(custom_font);
        edt_guess.setTypeface(custom_font);
        bt_check.setTypeface(custom_font);
        bt_new.setTypeface(custom_font);
        tv_score.setTypeface(custom_font);
        tv_hscore.setTypeface(custom_font);
        hscore.setTypeface(custom_font);
        score.setTypeface(custom_font);
        timer.setTypeface(custom_font);

        hscore.setText(String.valueOf(getHs()));
        score.setText("0");
        r = new Random();
        newGame();

        bt_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_guess.getText().toString().equalsIgnoreCase(currentWord)) {
                    TastyToast.makeText(getApplicationContext(), "+1", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                    counter++;
                    score.setText(String.valueOf(counter));
                    count.cancel();
                    newGame();

                    if (getHs() < counter) {
                        setHs(counter);
                        hscore.setText(String.valueOf(counter));
                    }
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
                til_guess.setError(null);
                fab.show();
                newGame();
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


    //Ad Events
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        count.cancel();
    }

    private boolean setHs(int hs) {
        preferences = getApplicationContext().getSharedPreferences(getPackageName(), MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("High", hs);
        return editor.commit();
    }

    private Integer getHs() {
        preferences = getApplicationContext().getSharedPreferences(getPackageName(), MODE_PRIVATE);
        return preferences.getInt("High", 0);
    }
}
