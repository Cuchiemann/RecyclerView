package daniel.gil.recyclerview;

import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.PersistableBundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import daniel.gil.recyclerview.adapters.ToDoAdapter;
import daniel.gil.recyclerview.databinding.ActivityMainBinding;
import daniel.gil.recyclerview.models.ToDo;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ArrayList<ToDo> tareas;
    private ToDoAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        tareas = new ArrayList<>();
        // crearTareas();



        adapter = new ToDoAdapter(tareas, R.layout.todo_view_model, MainActivity.this);
        binding.contentMain.contenedor.setAdapter(adapter);
        layoutManager = new GridLayoutManager(MainActivity.this,2);
        binding.contentMain.contenedor.setLayoutManager(layoutManager);


        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crearToDo().show();
            }
        });
    }

    private void crearTareas() {
        for (int i = 0; i < 1000; i++) {
            tareas.add(new ToDo("T"+i, "C"+i));
        }
    }

    private AlertDialog crearToDo(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Crear ToDo");
        builder.setCancelable(false);

        View toDoAlert = LayoutInflater.from(this).inflate(R.layout.todo_model_alert, null);
        EditText txtTitulo = toDoAlert.findViewById(R.id.txtTituloToDoModelAlert);
        EditText txtContenido = toDoAlert.findViewById(R.id.txtContenidoToDoModelAlert);
        builder.setView(toDoAlert);

        builder.setNegativeButton("Cancelar", null);
        builder.setPositiveButton("Crear", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(!txtTitulo.getText().toString().isEmpty() && !txtContenido.getText().toString().isEmpty()){
                    tareas.add(new ToDo(txtTitulo.getText().toString(), txtContenido.getText().toString()));
                    adapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(MainActivity.this,"Faltan datos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return builder.create();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("LISTA",tareas);
    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        tareas.addAll((ArrayList<ToDo>) savedInstanceState.getSerializable("LISTA"));
        adapter.notifyItemRangeInserted(0,tareas.size());
    }
}