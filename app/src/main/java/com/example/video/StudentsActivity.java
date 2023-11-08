package com.example.video;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.video.ActivityAddGroupPage;
import com.example.video.R;

import java.util.List;
import java.util.Random;

public class StudentsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Включаем кнопку "назад" в Toolbar

        // Устанавливаем слушатель клика на кнопку "назад"
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // При клике на кнопку "назад" будет вызываться метод onBackPressed(), который закроет текущую активность
            }
        });

        Intent intent = getIntent();
        List<String> usersList = intent.getStringArrayListExtra("userList");

        TextView usersTextView = findViewById(R.id.users);

        StringBuilder userListString = new StringBuilder();
        for (String user : usersList) {
            userListString.append(user).append("\n");
        }
        usersTextView.setText(userListString.toString());

        TextView textResult = findViewById(R.id.textResult);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Random rand = new Random();
                int randomIndex = rand.nextInt(usersList.size());
                textResult.setText(usersList.get(randomIndex));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.loadNewGroup) {
            Intent intent = new Intent(this, ActivityAddGroupPage.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
