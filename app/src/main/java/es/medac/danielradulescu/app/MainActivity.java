package es.medac.danielradulescu.app;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.chip.ChipGroup;
import com.google.android.material.progressindicator.LinearProgressIndicator;

public class MainActivity extends AppCompatActivity {

    // Cada tarea de ejemplo: su tarjeta, su casilla, su título y el chip de su categoría
    private static final int[][] TAREAS = {
            {R.id.tarjetaTarea1, R.id.casillaTarea1, R.id.tituloTarea1, R.id.chipClase},
            {R.id.tarjetaTarea2, R.id.casillaTarea2, R.id.tituloTarea2, R.id.chipTrabajo},
            {R.id.tarjetaTarea3, R.id.casillaTarea3, R.id.tituloTarea3, R.id.chipPersonal},
            {R.id.tarjetaTarea4, R.id.casillaTarea4, R.id.tituloTarea4, R.id.chipClase},
    };

    private ChipGroup grupoCategorias;
    private TextView textoPendientes;
    private TextView textoProgreso;
    private LinearProgressIndicator barraProgreso;

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

        grupoCategorias = findViewById(R.id.grupoCategorias);
        textoPendientes = findViewById(R.id.textoPendientes);
        textoProgreso = findViewById(R.id.textoProgreso);
        barraProgreso = findViewById(R.id.barraProgreso);

        for (int[] tarea : TAREAS) {
            CheckBox casilla = findViewById(tarea[1]);
            casilla.setOnCheckedChangeListener((boton, marcada) -> actualizarTareas());
            // Tocar la tarjeta entera también marca o desmarca la tarea
            findViewById(tarea[0]).setOnClickListener(v -> casilla.toggle());
        }
        grupoCategorias.setOnCheckedStateChangeListener((grupo, ids) -> actualizarTareas());

        actualizarTareas();
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Tras girar el móvil las casillas recuperan su estado: se recalcula el resumen
        actualizarTareas();
    }

    /** Recalcula el resumen del día, tacha las tareas hechas y aplica el filtro de categoría. */
    private void actualizarTareas() {
        int total = TAREAS.length;
        int completadas = 0;
        int categoria = grupoCategorias.getCheckedChipId();

        for (int[] tarea : TAREAS) {
            CheckBox casilla = findViewById(tarea[1]);
            TextView titulo = findViewById(tarea[2]);
            boolean hecha = casilla.isChecked();
            if (hecha) {
                completadas++;
            }
            int flags = titulo.getPaintFlags();
            titulo.setPaintFlags(hecha
                    ? flags | Paint.STRIKE_THRU_TEXT_FLAG
                    : flags & ~Paint.STRIKE_THRU_TEXT_FLAG);

            boolean visible = categoria == R.id.chipTodas || categoria == tarea[3];
            findViewById(tarea[0]).setVisibility(visible ? View.VISIBLE : View.GONE);
        }

        int pendientes = total - completadas;
        textoPendientes.setText(getResources().getQuantityString(
                R.plurals.resumen_pendientes, pendientes, pendientes));
        textoProgreso.setText(getString(R.string.resumen_progreso, completadas, total));
        barraProgreso.setMax(total);
        barraProgreso.setProgressCompat(completadas, true);
    }
}
