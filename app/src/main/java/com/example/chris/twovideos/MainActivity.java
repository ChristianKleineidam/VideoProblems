package com.example.chris.twovideos;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button mButton = findViewById(R.id.button1);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewFragment();
            }
        });
    }

    private void createNewFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.contentFragment, new VideoFragment());
        ft.commit();
    }
}
