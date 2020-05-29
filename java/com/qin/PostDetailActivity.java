package com.qin;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.qin.lafapplication.R;

public class PostDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        int postID=getIntent().getIntExtra("postID",0);
    }
}
