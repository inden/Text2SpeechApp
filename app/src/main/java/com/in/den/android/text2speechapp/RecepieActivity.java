package com.in.den.android.text2speechapp;


import android.speech.tts.TextToSpeech;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;

import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStreamReader;

public class RecepieActivity extends AppCompatActivity {

    private final static String TAG = "RecepieActivity";
    private ImageButton button;
    TextToSpeech textToSpeech = null;
    WebView webView;
    String htmlAsString = "";
    private final RecetteParser recetteParser = new RecetteParser();
    private boolean speaking;

    /*
    Free icon downloaded from
    http://www.flaticon.es/icono-gratis/pac-man-hablando_14733
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recepie);
        webView = (WebView) findViewById(R.id.webview);
        button = (ImageButton) findViewById(R.id.button);
        speaking = false;

        Bundle extras = getIntent().getExtras();
        String filename = extras.getString("filename");
        int order = extras.getInt("order");

        try {
            htmlAsString = recetteParser.getHtmlRecette(
                    new InputStreamReader(getAssets().open("xml/"+filename)), order);

            webView.loadDataWithBaseURL(null, htmlAsString, "text/html", "utf-8", null);

        }catch (Exception e) {
            Log.e(TAG, e.getMessage());
            finish();
        }

        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    if(!speaking) {
                        button.setImageResource(R.drawable.pac_man_hablando_stop);
                        recetteParser.readRecette(htmlAsString, textToSpeech);
                        speaking = true;
                    }
                    else {
                        button.setImageResource(R.drawable.pac_man_hablando);
                        textToSpeech.stop();
                        speaking = false;
                    }

                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        textToSpeech.shutdown();
    }


}
