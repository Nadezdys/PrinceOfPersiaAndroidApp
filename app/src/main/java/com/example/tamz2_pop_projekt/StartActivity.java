package com.example.tamz2_pop_projekt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class StartActivity extends AppCompatActivity {
    ImageView imageView;
    ImageView imageView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_start);

        imageView = findViewById(R.id.imageView);
        imageView2 = findViewById(R.id.imageView2);


        imageView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), GameActivity.class);
            Bundle b = new Bundle();
            b.putInt("level", 1);
            intent.putExtras(b);
            startActivity(intent);
        }
    });
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), GameActivity.class);
                Bundle b = new Bundle();
                b.putInt("level", 2);
                intent.putExtras(b);
                startActivity(intent);
            }
        });

    }
}