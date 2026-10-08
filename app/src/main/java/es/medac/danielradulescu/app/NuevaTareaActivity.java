package es.medac.danielradulescu.app;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class NuevaTareaActivity extends AppCompatActivity {

    private TextInputLayout capaTitulo;
    private TextInputEditText campoTitulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_nueva_tarea);
        // También se deja hueco para el teclado, así los botones no quedan tapados
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets barras = insets.getInsets(
                    WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.ime());
            v.setPadding(barras.left, barras.top, barras.right, barras.bottom);
            return insets;
        });

        capaTitulo = findViewById(R.id.capaTitulo);
        campoTitulo = findViewById(R.id.campoTitulo);

        MaterialToolbar barraHerramientas = findViewById(R.id.barraHerramientas);
        barraHerramientas.setNavigationOnClickListener(v -> finish());
        findViewById(R.id.botonCancelar).setOnClickListener(v -> finish());
        findViewById(R.id.botonGuardar).setOnClickListener(v -> guardarTarea());

        // El aviso de "título vacío" desaparece en cuanto el usuario empieza a escribir
        campoTitulo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                capaTitulo.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /** Comprueba que la tarea tiene título, muestra la confirmación y vuelve a la lista. */
    private void guardarTarea() {
        Editable texto = campoTitulo.getText();
        String titulo = texto == null ? "" : texto.toString().trim();
        if (titulo.isEmpty()) {
            capaTitulo.setError(getString(R.string.error_titulo_vacio));
            return;
        }
        Toast.makeText(this, getString(R.string.mensaje_tarea_guardada, titulo),
                Toast.LENGTH_SHORT).show();
        finish();
    }
}
