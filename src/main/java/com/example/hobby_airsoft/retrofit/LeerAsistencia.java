/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.hobby_airsoft.retrofit;

import com.example.hobby_airsoft.Jugador;
import com.example.hobby_airsoft.Partida;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 *
 * @author Tiger
 */
public interface LeerAsistencia {
    
    @GET("asistencia/leer.php") //Usa el método GET
    Call<List<Jugador>> getAsistencias(@Query("id_partida") int id);
    
}
