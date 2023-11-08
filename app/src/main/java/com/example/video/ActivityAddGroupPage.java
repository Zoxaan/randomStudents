package com.example.video;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;



import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.InputStream;

public class ActivityAddGroupPage extends AppCompatActivity {

 private  int REQUEST_CODE = 1;
    private static final int STORAGE_PERMISSION_CODE = 100;
    private String fileName;
    private String fileContent;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group_page);
        databaseHelper = new DatabaseHelper(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); // Установите ToolBar в качестве ActionBar

        // Устанавливаем слушатель клика на кнопку "назад"
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // При клике на кнопку "назад" будет вызываться метод onBackPressed(), который закроет текущую активность
            }
        });

        Button loadNewGroupButton = findViewById(R.id.loadNewGroup);
        loadNewGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");

                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            // Обработка нажатия кнопки "назад"
            onBackPressed(); // Этот метод закроет текущую активность
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    public boolean checkPermission()
    {
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.R)
        {
            return Environment.isExternalStorageManager();
        }else{
            int write = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int read = ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE);

            return write == PackageManager.PERMISSION_GRANTED && read ==  PackageManager.PERMISSION_GRANTED;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Uri selectedFileUri = data.getData();
            if (checkPermission())
                {
                    readFileFromUri(selectedFileUri);
                }else {
                    requetsPermission();
                }
        }
    }


    private void readFileFromUri(Uri fileUri) {
        try {
            // Найдите TextView по идентификатору
            TextView groupResultTextView = findViewById(R.id.groupResult);
            InputStream inputStream = getContentResolver().openInputStream(fileUri);
            if (inputStream != null) {
                StringBuilder stringBuilder = new StringBuilder();
                int content;
                while ((content = inputStream.read()) != -1) {
                    stringBuilder.append((char) content);
                }
                inputStream.close();
                 fileContent = stringBuilder.toString();
                getFileNameFromUri(fileUri);
                groupResultTextView.setText("result"+fileContent);
                long groupId = databaseHelper.insertGroup(fileName);
                Toast.makeText(this, "id"+groupId, Toast.LENGTH_LONG).show();
                String[] users = fileContent.split("\n");
                for (String user : users) {
                    databaseHelper.insertUser(user, (int) groupId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void requetsPermission()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        {
            try {

                Log.d(TAG,"requestPermission : try");
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package",this.getPackageName(),null);
                intent.setData(uri);
                storageActivityResultLauncher.launch(intent);
            }catch (Exception e)
            {
                Log.e(TAG,"requestPermission : catch",e);
                Intent intent =  new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                storageActivityResultLauncher.launch(intent);
            }

        }else {
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
        }
    }

    private String getFileNameFromUri(Uri fileUri) {


        String uriString = fileUri.toString();
        File myFile = new File(uriString);

        if (uriString.startsWith("content://")) {
            Cursor cursor = null;
            try {
                cursor = getContentResolver().query(fileUri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } else if (uriString.startsWith("file://")) {
            fileName = myFile.getName();
        }
        int lastIndex = fileName.lastIndexOf(".");
        if (lastIndex > 0) {
            fileName = fileName.substring(0, lastIndex);
        }

        return fileName;
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
                            Log.d(TAG,"aAAAAAAA");
                           // createFolder();
                        }else {
                            Log.d(TAG,"Bbbbbbb");
                            Toast.makeText(ActivityAddGroupPage.this,"text",Toast.LENGTH_SHORT).show();
                        }
                    }else {

                    }
                }
            });


}
