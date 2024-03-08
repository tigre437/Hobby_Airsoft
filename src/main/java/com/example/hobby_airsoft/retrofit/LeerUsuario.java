
package com.example.hobby_airsoft.retrofit;

import com.example.hobby_airsoft.Usuario;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 *
 * @author Tiger
 */
public interface LeerUsuario {

    @GET("jugadores/leer.php") //Usa el método GET
    Call<List<Usuario>> getUsuario();
    
}



