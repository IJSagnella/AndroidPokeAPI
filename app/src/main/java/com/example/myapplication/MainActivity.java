package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    LinearLayout linear1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linear1 = findViewById(R.id.linear1);

        getPokeList();;
    }


    private void getPokeList(){
        String url = "https://pokeapi.co/api/v2/pokemon/";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    // Obtener el JSONArray de la respuesta
                    JSONArray result = jsonObject.getJSONArray("results");

                    Button newButton;

                    // Recorrer el JSONArray (No uso ForEach por el tipo de dato)
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject item = result.getJSONObject(i);
                        // Aquí puedes procesar cada objeto JSON individualmente
                        Log.d("MainActivity", item.toString());

                        newButton = new Button(MainActivity.this);

                        newButton.setText(item.getString("name"));

                        newButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v){
                                // Crear un Intent para iniciar PokeActivity
                                Intent intent = new Intent(MainActivity.this, PokeActivity.class);

                                // Crear un Bundle y agregar el String
                                Bundle bundle = new Bundle();
                                try {
                                    bundle.putString("url", item.getString("url"));
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }

                                // Agregar el Bundle al Intent
                                intent.putExtras(bundle);

                                // Iniciar PokeActivity
                                startActivity(intent);
                            }
                        });

                        linear1.addView(newButton);

                    }


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
}