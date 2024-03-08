/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.hobby_airsoft.retrofit;

import com.example.hobby_airsoft.Jugador;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 *
 * @author Tiger
 */
public interface InsertarAsistencia {
    
    @POST("asistencia/insertar.php")
    Call<Jugador> insertarDato(@Body Jugador a);
    
}