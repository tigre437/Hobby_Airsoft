
package com.example.hobby_airsoft.retrofit;


import com.example.hobby_airsoft.Usuario;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 *
 * @author Tiger
 */
public interface BorrarJugador {
    
    @GET("jugadores/borrar.php") //Usa el método GET
    Call<Usuario> borrarUsuario(@Query("id") int id);
}
