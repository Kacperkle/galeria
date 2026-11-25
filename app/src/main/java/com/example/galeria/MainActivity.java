package com.example.galeria;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Switch swKwiaty, swZwierzeta, swSamochody;
    private LinearLayout llGaleria;
    private Zdjecie[] zdjecia;

    private static class Zdjecie {
        int id;
        String alt;
        String filename;
        int category;
        int downloads;

        Zdjecie(int id, String alt, String filename, int category, int downloads) {
            this.id = id;
            this.alt = alt;
            this.filename = filename;
            this.category = category;
            this.downloads = downloads;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        swKwiaty = findViewById(R.id.swKwiaty);
        swZwierzeta = findViewById(R.id.swZwierzeta);
        swSamochody = findViewById(R.id.swSamochody);
        llGaleria = findViewById(R.id.llGaleria);

        zdjecia = new Zdjecie[]{
                new Zdjecie(0, "Mak", "obraz1.jpg", 1, 35),
                new Zdjecie(1, "Bukiet", "obraz2.jpg", 1, 43),
                new Zdjecie(2, "Dalmatyńczyk", "obraz3.jpg", 2, 2),
                new Zdjecie(3, "Świnka morska", "obraz4.jpg", 2, 53),
                new Zdjecie(4, "Rotwailer", "obraz5.jpg", 2, 43),
                new Zdjecie(5, "Audi", "obraz6.jpg", 3, 11),
                new Zdjecie(6, "kotki", "obraz7.jpg", 2, 22),
                new Zdjecie(7, "Róża", "obraz8.jpg", 1, 33),
                new Zdjecie(8, "Świnka morska", "obraz9.jpg", 2, 123),
                new Zdjecie(9, "Foksterier", "obraz10.jpg", 2, 22),
                new Zdjecie(10, "Szczeniak", "obraz11.jpg", 2, 12),
                new Zdjecie(11, "Garbus", "obraz12.jpg", 3, 321)
        };

        CompoundButton.OnCheckedChangeListener przelacznikListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                wyswietlZdjecia();
            }
        };

        swKwiaty.setOnCheckedChangeListener(przelacznikListener);
        swZwierzeta.setOnCheckedChangeListener(przelacznikListener);
        swSamochody.setOnCheckedChangeListener(przelacznikListener);

        wyswietlZdjecia();
    }

    private void wyswietlZdjecia() {
        llGaleria.removeAllViews();

        List<Zdjecie> widoczneZdjecia = new ArrayList<>();
        for (Zdjecie z : zdjecia) {
            if ((z.category == 1 && swKwiaty.isChecked()) ||
                    (z.category == 2 && swZwierzeta.isChecked()) ||
                    (z.category == 3 && swSamochody.isChecked())) {
                widoczneZdjecia.add(z);
            }
        }

        LinearLayout aktualnyWiersz = null;

        for (int i = 0; i < widoczneZdjecia.size(); i++) {
            if (i % 2 == 0) {
                aktualnyWiersz = new LinearLayout(this);
                aktualnyWiersz.setOrientation(LinearLayout.HORIZONTAL);
                aktualnyWiersz.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                llGaleria.addView(aktualnyWiersz);
            }

            Zdjecie z = widoczneZdjecia.get(i);

            LinearLayout kartaZdjecia = new LinearLayout(this);
            kartaZdjecia.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams kartaParams = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.0f
            );
            kartaParams.setMargins(5, 5, 5, 12);
            kartaZdjecia.setLayoutParams(kartaParams);

            ImageView ivZdjecie = new ImageView(this);
            LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    320
            );
            imgParams.setMargins(5, 5, 5, 5);
            ivZdjecie.setLayoutParams(imgParams);
            ivZdjecie.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ivZdjecie.setContentDescription(z.alt);

            GradientDrawable zaokraglenie = new GradientDrawable();
            zaokraglenie.setCornerRadius(24f);
            ivZdjecie.setBackground(zaokraglenie);
            ivZdjecie.setClipToOutline(true);

            String nazwaZasobu = z.filename.substring(0, z.filename.lastIndexOf('.'));
            int resId = getResources().getIdentifier(nazwaZasobu, "drawable", getPackageName());
            ivZdjecie.setImageResource(resId);

            TextView tvPobrania = new TextView(this);
            tvPobrania.setText("Pobrań: " + z.downloads);
            tvPobrania.setTextSize(16f);
            tvPobrania.setTextColor(Color.BLACK);
            tvPobrania.setGravity(Gravity.START);

            Button btnPobierz = new Button(this);
            btnPobierz.setText("Pobierz");
            btnPobierz.setBackgroundColor(Color.parseColor("#198754"));
            btnPobierz.setTextColor(Color.WHITE);
            btnPobierz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    z.downloads++;
                    tvPobrania.setText("Pobrań: " + z.downloads);
                }
            });

            kartaZdjecia.addView(ivZdjecie);
            kartaZdjecia.addView(tvPobrania);
            kartaZdjecia.addView(btnPobierz);

            aktualnyWiersz.addView(kartaZdjecia);
        }
    }
}
