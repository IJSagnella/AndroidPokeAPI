package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PokeActivity extends AppCompatActivity {

    Pokemon pokemon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poke);

        Intent intent = getIntent();

        Button atras = findViewById(R.id.back);
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para iniciar PokeActivity
                Intent intent = new Intent(PokeActivity.this, MainActivity.class);

                // Iniciar MainActivity
                startActivity(intent);
            }
        });

        // Obtener el Bundle del Intent
        Bundle bundle = intent.getExtras();

        // Verificar si el Bundle no es nulo y obtener el String
        if (bundle != null) {
            String receivedURL = bundle.getString("url");

            getPoke(receivedURL);

        }

    }

    private void getPoke(String url){
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    int id = jsonObject.getInt("id");
                    String name = jsonObject.getString("name");

                    JSONArray stats = jsonObject.getJSONArray("stats");
                    int atk = stats.getJSONObject(1).getInt("base_stat");
                    int def = stats.getJSONObject(2).getInt("base_stat");
                    int vel = stats.getJSONObject(5).getInt("base_stat");

                    completarInfo(new Pokemon(
                            id,
                            name,
                            atk,
                            def,
                            vel));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.getMessage());
            }
        });
        Volley.newRequestQueue(this).add(request);
    }

    private void completarInfo(Pokemon pokemon){
        TextView textName = findViewById(R.id.textNombre);
        TextView textNum = findViewById(R.id.textNumero);
        TextView textAtk = findViewById(R.id.textAtk);
        TextView textDef = findViewById(R.id.textDef);
        TextView textVel = findViewById(R.id.textVel);

        textName.setText("Nombre: "+pokemon.getNombre());
        textNum.setText("Numero: "+pokemon.getId());
        textAtk.setText("Ataque: "+pokemon.getAtaque());
        textDef.setText("Defensa: "+pokemon.getDefensa());
        textVel.setText("Velocidad: "+pokemon.getVelocidad());

        ImageView imgView = findViewById(R.id.imageView);

        String imageURL = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/"+ pokemon.getId() +".png";

        Glide.with(this)
                .asBitmap()
                .load(imageURL)
                .into(imgView);

    }
}