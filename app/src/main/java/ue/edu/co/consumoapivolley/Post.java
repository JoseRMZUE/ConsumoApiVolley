package ue.edu.co.consumoapivolley;

import org.json.JSONException;
import org.json.JSONObject;

public class Post {
    // ATRIBUTOS (el "estado" de cada objeto Post)
    private final int userId; // A qué usuario pertenece el post
    private final int id; // Identificador único del post
    private final String title; // Título del post
    private final String body; // Contenido/cuerpo del post

    // CONSTRUCTOR:
    // Se ejecuta al escribir "new Post(userId, id, title, body)".
    // Guarda cada parámetro recibido en su atributo correspondiente.
    public  Post(int userId, int id, String title, String body) {
        this.userId = userId;
        this.id = id;
        this.title = title;
        this.body = body;
    }
    public static Post desdeJson(JSONObject json) throws JSONException {
        int userId = json.getInt("userId"); // Lee la clave "userId" y la interpreta como número entero
        int id = json.getInt("id"); // Lee la clave "id" como entero
        String title = json.getString("title"); // Lee la clave "title" como texto
        String body = json.getString("body"); // Lee la clave "body" como texto
        return new Post(userId, id, title, body); // Arma un objeto Post con esos datos y lo retorna
    }
    // GETTERS
    // Son la  forma de consultar los atributos privados desde fuera de la clase.
        public int getUserId() {
            return userId;
        }
        public int getId() {
            return id;
        }
        public String getTitle() {
            return title;
        }
        public String getBody() {
            return body;
        }

    // toString()
    // Java llama automáticamente este metodo cada vez que necesita "convertir"
    // el objeto a texto.
    @Override
    public String toString() {
            return "UserId: " + userId
                    + "\nID: " + id
                    + "\nTitulo: " + title
                    + "\nBody: " + body;
    }
}
