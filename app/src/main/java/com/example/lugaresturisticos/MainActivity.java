package com.example.lugaresturisticos;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import WebServices.Asynchtask;
import WebServices.WebService;

public class MainActivity extends AppCompatActivity implements Asynchtask {

    private Spinner spCategoria;
    private Spinner spSubCategoria;

    private List<Categoria> categorias;
    private Map<String, List<String>> subCategoriasMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spCategoria = findViewById(R.id.spCategoria);
        spSubCategoria = findViewById(R.id.spSubCategoria);

        categorias = new ArrayList<>();
        subCategoriasMap = new HashMap<>();

        Map<String, String> datos = new HashMap<>();
        WebService ws = new WebService("https://uealecpeterson.net/turismo/categoria/getlistadoCB",
                datos, MainActivity.this, MainActivity.this);
        ws.execute("GET");
    }

    @Override
    public void processFinish(String result) throws JSONException {
        categorias.clear();
        subCategoriasMap.clear();

        JSONArray jsonArray = new JSONArray(result);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String id = jsonObject.getString("id");
            String descripcion = jsonObject.getString("descripcion");

            Categoria categoria = new Categoria(id, descripcion);
            categorias.add(categoria);

            List<String> subCategorias = new ArrayList<>();
            JSONArray subCategoriaArray = jsonObject.getJSONArray("subcategorias");
            for (int j = 0; j < subCategoriaArray.length(); j++) {
                String subCategoria = subCategoriaArray.getString(j);
                subCategorias.add(subCategoria);
            }
            subCategoriasMap.put(descripcion, subCategorias);
        }

        ArrayAdapter<Categoria> categoriaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categorias);
        categoriaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoria.setAdapter(categoriaAdapter);

        spCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Categoria categoriaSeleccionada = categorias.get(position);
                List<String> subCategorias = subCategoriasMap.get(categoriaSeleccionada.getDescripcion());

                ArrayAdapter<String> subCategoriaAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, subCategorias);
                subCategoriaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spSubCategoria.setAdapter(subCategoriaAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }


    private static class Categoria {
        private String id;
        private String descripcion;

        public Categoria(String id, String descripcion) {
            this.id = id;
            this.descripcion = descripcion;
        }

        public String getId() {
            return id;
        }

        public String getDescripcion() {
            return descripcion;
        }

        @Override
        public String toString() {
            return descripcion; // Displayed in the spinner
        }
    }
}
