/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.hobby_airsoft.retrofit;

import com.example.hobby_airsoft.Jugador;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 *
 * @author Tiger
 */
public interface BorrarAsistencia {
    
    @GET("asistencia/borrar.php") //Usa el método GET
    Call<List<Jugador>> borrarPartida(@Query("Id_Jugador") int id_jugador, @Query("Id_Partida") int id_partida);
    
}
