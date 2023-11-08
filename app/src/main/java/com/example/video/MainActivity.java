package com.example.video;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int STORAGE_PERMISSION_CODE = 100;
    private static final String TAG = "PERMISSION_TAG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        MyAdapter adapter = new MyAdapter(this);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        List<String> groupNames = dbHelper.getAllGroups();
        adapter.setGroups(groupNames);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }







    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        res();
    }

    public void res()
    {
        MyAdapter adapter = new MyAdapter(this);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        List<String> groupNames = dbHelper.getAllGroups();
        adapter.setGroups(groupNames);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
            int id = item.getItemId();

            if (id == R.id.loadNewGroup)
            {
                Intent intent = new Intent(this, ActivityAddGroupPage.class);
                startActivity(intent);
                return true;
            }
        return super.onOptionsItemSelected(item);
    }


        private ActivityResultLauncher<Intent> storageActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                    Log.d(TAG,"onActivityResult: ");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
                    {
                        if (Environment.isExternalStorageManager())
                        {
                            //createFolder();
                        }else {
                            Toast.makeText(MainActivity.this,"text",Toast.LENGTH_SHORT).show();
                        }
                    }else {

                    }
            }
        });

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE)
        {
            if (grantResults.length>0)
            {
                boolean  write = grantResults[0]==PackageManager.PERMISSION_GRANTED;
                boolean  read = grantResults[1]==PackageManager.PERMISSION_GRANTED;

                if (write&&read)
                {
                    Log.d(TAG,"Manifest.permission.READ_EXTERNAL_STORAGE");
                   // createFolder();
                }else {
                    Log.d(TAG,"asdadsdsadsa");
                    Toast.makeText(this,"textasdasd",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}