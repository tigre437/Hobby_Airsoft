
package com.example.hobby_airsoft.retrofit;

import com.example.hobby_airsoft.Usuario;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 *
 * @author Tiger
 */
public interface InsertarJugador {

    @POST("jugadores/insertar.php")
    Call<Usuario> insertarJugador(@Body Usuario a);

    
}



