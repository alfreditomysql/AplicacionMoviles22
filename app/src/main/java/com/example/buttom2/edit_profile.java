package com.example.buttom2;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

public class edit_profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        // Encuentra el NestedScrollView por su ID
        NestedScrollView nestedScrollView = findViewById(R.id.nestedScrollView);

        // Configura el OnScrollChangeListener
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // Implementa el comportamiento de desplazamiento aquí
                if (scrollY > oldScrollY) {
                    // Usuario deslizó hacia abajo
                } else {
                    // Usuario deslizó hacia arriba
                }
            }
        });
    }
}
