package com.in.den.android.text2speechapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.in.den.android.text2speechapp.data.Recepie;
import com.in.den.android.text2speechapp.data.RecepieContent;

public class MainActivity extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public void onListFragmentInteraction(Recepie item) {

        Intent i = new Intent(getApplicationContext(), RecepieActivity.class);
        i.putExtra("filename", item.filename);
        i.putExtra("order", item.order);

        startActivity(i);
    }
}
