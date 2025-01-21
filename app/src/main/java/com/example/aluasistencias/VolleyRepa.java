package com.example.aluasistencias;

import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class VolleyRepa {

    private static RequestQueue requestQueue;
    private Context context;

    public VolleyRepa(Context context) {
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(context);
    }

    // Método para obtener la cola de solicitudes
    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    // Método para obtener datos del usuario
    public void obtenerUsuario(String IdUsuario, final VolleyCallback callback) {
        String url = "http://192.168.212.7/BackAlu/Backend/Repartidor.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("VolleyResponse", response);

                    try {
                        // Intentamos analizar la respuesta como un JSONObject
                        JSONObject jsonResponse = new JSONObject(response);

                        // Verificamos si hay un campo "message" en la respuesta
                        if (jsonResponse.has("message")) {
                            // Si la respuesta contiene un mensaje de error
                            String message = jsonResponse.getString("message");
                            callback.onError(message);
                        } else {
                            // Si la respuesta contiene los datos del usuario
                            String nick = jsonResponse.getString("nick");
                            String estado = jsonResponse.getString("estado");
                            String nombreCompleto = jsonResponse.getString("Nombre_completo");
                            String rol = jsonResponse.getString("rol");
                            String email = jsonResponse.getString("email");

                            // Crear una respuesta con los datos del usuario
                            String usuarioInfo = "{\"nick\":\"" + nick + "\", \"estado\":\"" + estado + "\", "
                                    + "\"nombreCompleto\":\"" + nombreCompleto + "\", "
                                    + "\"rol\":\"" + rol + "\", "
                                    + "\"email\":\"" + email + "\"}";

                            callback.onSuccess(usuarioInfo);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        callback.onError("Error al procesar los datos.");
                    }
                },
                error -> callback.onError("Error en la conexión: " + error.getMessage())
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("IdUsuario", IdUsuario);  // Asegúrate de usar el nombre correcto del parámetro
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    // Método para obtener los repartos con fechas
    public static void obtenerRepartos(String IdUsuario, final VolleyCallback callback) {
        String url = "http://192.168.212.7/BackAlu/Backend/Repartos.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("VolleyResponse", response);

                        try {
                            // Verificar si la respuesta es un mensaje de error
                            if (response.contains("No se encontraron repartos")) {
                                callback.onSuccess("{\"message\":\"No se encontraron repartos\"}");
                                return;
                            }

                            // Aquí manejamos la respuesta como un JSONArray
                            JSONArray jsonArray = new JSONArray(response);

                            // Crear un StringBuilder para almacenar los datos formateados
                            StringBuilder repartos = new StringBuilder();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                // Extraemos los datos del JSON
                                String direccion = jsonArray.getJSONObject(i).getString("Direccion_entrega");
                                String estatus = jsonArray.getJSONObject(i).getString("Estatus");
                                String anotaciones = jsonArray.getJSONObject(i).getString("Anotaciones");
                                String fechaInicio = jsonArray.getJSONObject(i).getString("FechaInicio");
                                String fechaEntrega = jsonArray.getJSONObject(i).getString("FechaEntrega");

                                // Formateamos la respuesta con los datos completos
                                repartos.append("Reparto " + (i + 1) + ": " + direccion + " - " + estatus + " - " + anotaciones
                                        + " - Fecha Inicio: " + fechaInicio + " - Fecha Entrega: " + fechaEntrega + "\n");
                            }

                            // Devolvemos la cadena con los repartos
                            callback.onSuccess(repartos.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onError("Error al procesar los datos.");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError("Error en la conexión: " + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("IdUsuario", IdUsuario);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }


    public void actualizarEstadoReparto(String IdReparto, String nuevoEstado, final VolleyCallback callback) {
        String url = "http://192.168.212.7/BackAlu/Backend/NuevoEstado.php";

        // Crear el JSON para enviar
        JSONObject params = new JSONObject();
        try {
            params.put("IdReparto", IdReparto);
            params.put("estado", nuevoEstado);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Realizar la solicitud POST
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Verifica si la respuesta contiene un mensaje de éxito
                            if (response.has("message")) {
                                String message = response.getString("message");
                                callback.onSuccess(message);  // Llamar onSuccess con el mensaje
                            } else {
                                callback.onError("Error: No se pudo actualizar el estado.");
                            }
                        } catch (JSONException e) {
                            callback.onError("Error al procesar la respuesta.");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error.toString());
                    }
                });

        // Agregar la solicitud a la cola de Volley
        Volley.newRequestQueue(context).add(request);
    }



    // Interfaz para manejar el resultado o error de la solicitud
    public interface VolleyCallback {
        void onSuccess(String response);
        void onError(String error);
    }
}