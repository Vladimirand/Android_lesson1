package com.nehvedovich.vladimir.pogoda.screens.screens;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nehvedovich.vladimir.pogoda.R;
import com.nehvedovich.vladimir.pogoda.screens.screens.fragments.CityInfoFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SecondActivity extends AppCompatActivity {
    public File imagePath;
    public static final int REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE = 0;
    private final String yandexHttp = "http://yandex.ru/pogoda/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        if (savedInstanceState == null) {
            CityInfoFragment details = new CityInfoFragment();
            details.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(android.R.id.content, details).commit();
        }

        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setHomeButtonEnabled(true);
            actionbar.setDisplayHomeAsUpEnabled(true);
        }
        detailsButton();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        if (item.getItemId() == R.id.action_history) {
            startActivity(new Intent(SecondActivity.this, HistoryActivity.class));
            return true;
        }
        //отправляем ссылку о состоянии погоды в городе отображенном на экране
        if (item.getItemId() == R.id.share_link) {
            Intent intent = new Intent(Intent.ACTION_SEND);

            TextView city = findViewById(R.id.cityName);
            String cityName = (String) city.getText();
            if (cityName.contains(" ")) {
                cityName = cityName.substring(0, cityName.indexOf(","));
            }

            String t = (yandexHttp + cityName);
            intent.putExtra(Intent.EXTRA_TEXT, t);
            intent.setType("text/plain");

            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(SecondActivity.this, "\n" +
                        getString(R.string.application_absent), Toast.LENGTH_SHORT).show();
            }
        }


        //Делаем скриншот экрана и отправляем его другу
        if (item.getItemId() == R.id.share_screenshot) {
            //прверяем разрешение на доступ к памяти устройства
            int permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                Bitmap bitmap = takeScreenshot();
                saveBitmap(bitmap);
                shareIt();
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE);
        }
        return super.onOptionsItemSelected(item);
    }

    //меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_for_second_activity, menu);
        return true;
    }

    private void detailsButton() {
        Button details = findViewById(R.id.moreInformation);
        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);

                TextView city = findViewById(R.id.cityNameInfo);
                String cityName = (String) city.getText();
                if (cityName.contains(" ")) {
                    cityName = cityName.substring(0, cityName.indexOf(","));
                }

                Uri uri = Uri.parse(yandexHttp + cityName);
                intent.setData(uri);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(SecondActivity.this, "\n" +
                            getString(R.string.application_absent), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public Bitmap takeScreenshot() {
        View rootView = findViewById(android.R.id.content).getRootView();
        rootView.setDrawingCacheEnabled(true);
        return rootView.getDrawingCache();
    }

    public void saveBitmap(Bitmap bitmap) {
        imagePath = new File(Environment.getExternalStorageDirectory() + "/screenshot.png");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }
    }

    private void shareIt() {
        Uri uri = Uri.fromFile(imagePath);
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("image/*");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);

        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }
}
