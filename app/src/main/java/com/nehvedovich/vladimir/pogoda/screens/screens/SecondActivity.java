package com.nehvedovich.vladimir.pogoda.screens.screens;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

//    File imagePath = new File(context.getFilesDir(), "screenshot");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        if (savedInstanceState == null) {
            CityInfoFragment details = new CityInfoFragment();
            details.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(android.R.id.content, details).commit();
        }

        Button info = findViewById(R.id.infoTemperature);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SecondActivity.this, InfoActivity.class));
            }
        });

        ActionBar actionbar = getSupportActionBar();
        actionbar.setHomeButtonEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);

        Button details = findViewById(R.id.moreInformation);
        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);

                TextView city = findViewById(R.id.cityName);
                String cityName = (String) city.getText();
                if(cityName.contains(" ")){
                    cityName= cityName.substring(0, cityName.indexOf(","));}



                Uri uri = Uri.parse("http://yandex.ru/pogoda/" + cityName);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        //отправляем ссылку о состоянии погоды в городе отображенном на экране
        if (item.getItemId() == R.id.share_link){
            Intent intent = new Intent(Intent.ACTION_SEND);
//            TextView city = findViewById(R.id.cityNameInfo);
//            String cityName = (String) city.getText();

            TextView city = findViewById(R.id.cityName);
            String cityName = (String) city.getText();
            if(cityName.contains(" ")){
                cityName= cityName.substring(0, cityName.indexOf(","));}

            String t = ("http://yandex.ru/pogoda/" + cityName);
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
        if (item.getItemId() == R.id.share_screenshot){
            Bitmap bitmap = takeScreenshot();
            saveBitmap(bitmap);
            shareIt();

        }

        //noinspection SimplifiableIfStatement
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    //меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_for_second_activity, menu);
        return true;
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
//        String shareBody = "In Tweecher, My highest score with screen shot";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "My Tweecher score");
//        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);

        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

}
