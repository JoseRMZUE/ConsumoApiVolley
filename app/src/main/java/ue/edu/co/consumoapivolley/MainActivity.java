package ue.edu.co.consumoapivolley;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
// Componentes de la librería Volley, para hacer peticiones HTTP
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    // ATRIBUTOS DE LA CLASE
    // Declaramos los atributos que usaremos
    private static final String URL_API = "https://jsonplaceholder.typicode.com/posts"; // Endpoint que vamos a consumir
    private static final String REQUEST_TAG = "GET_POSTS"; // "Etiqueta" para identificar esta petición dentro de la cola
    // Referencias a los elementos visuales del layout (se conectan en inicializarVistas())
    private ListView lvTodos;
    private ProgressBar progressBar;
    private TextView tvEstado;
    private TextView tvContador;
    private RequestQueue requestQueue;
    // Lista donde vamos a guardar los objetos Post ya construidos a partir del JSON.
    private final List<Post> listaPosts = new ArrayList<>();

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
        inicializarVistas(); // Conecta las variables Java con los elementos del XML
        requestQueue = Volley.newRequestQueue(getApplicationContext()); // Crea la cola de peticiones de Volley
        consumirApi(); // Dispara la petición HTTP para traer los posts
    }
    // MOSTRAR / OCULTAR CARGA
    private void mostrarCargando(boolean cargando) {
        progressBar.setVisibility(cargando ? View.VISIBLE : View.GONE); // Si cargando=true, muestra el círculo; si no, lo oculta
        lvTodos.setVisibility(cargando ? View.GONE : View.VISIBLE); // Comportamiento inverso para la lista
    }
    // MANEJO DE ERRORES
    // Metodo central para mostrar cualquier mensaje de error: actualiza el TextView,
    // lo hace visible, y además lanza un Toast para que sea más notorio.
    private void mostrarError(String mensaje) {
        mostrarCargando(false); // Deja de mostrar el círculo de carga
        tvEstado.setText(mensaje);
        tvEstado.setVisibility(View.VISIBLE);
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }
    // Se llama específicamente cuando Volley falla en la comunicación con el servidor
    // (sin internet, timeout, error 404/500, etc.)
    private void procesarError(VolleyError error) {
        String mensaje = error.getMessage();
        // Muchas veces Volley entrega un error sin mensaje de texto (null o vacío),
        // así que ponemos un mensaje genérico de respaldo
        if (mensaje == null || mensaje.isBlank()) {
            mensaje = "Verifique la conexcion a internet.";
        }
        mostrarError("Error en la solicitud: " + mensaje);
    }
    // CONSUMO DE LA API
    // Metodo principal: arma y dispara la petición GET hacia JSONPlaceholder.
    private void consumirApi() {
        mostrarCargando(true);


        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                URL_API,
                null,

                response -> {
                    listaPosts.clear();
                    try {

                        for (int i = 0; i < response.length(); i++) {
                            JSONObject item = response.getJSONObject(i);

                            Post post = Post.desdeJson(item);
                            listaPosts.add(post);
                        }

                        ArrayAdapter<Post> adapter = new ArrayAdapter<>(
                                this,
                                android.R.layout.simple_list_item_1,
                                listaPosts
                        );
                        lvTodos.setAdapter(adapter);
                        mostrarCargando(false);
                        tvEstado.setVisibility(View.GONE);

                        tvContador.setText("Se recibieron " + listaPosts.size() + " registros.");
                        tvContador.setVisibility(View.VISIBLE);
                    } catch (JSONException e) {
                        mostrarError("No fue posible procesar la respuesta");
                    }
                },
                this::procesarError
        );
        request.setTag(REQUEST_TAG);
        requestQueue.add(request);
    }
    // CONEXIÓN CON LA VISTA
    // usando los IDs (android:id) como referencia.
    private void inicializarVistas() {
        lvTodos = findViewById(R.id.lvTodos);
        progressBar = findViewById(R.id.progressBar);
        tvEstado = findViewById(R.id.tvEstado);
        tvContador = findViewById(R.id.tvContador);
    }
}