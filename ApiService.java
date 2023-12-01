package com.example.uastest;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("fotologo.json")
    //Call<List<LogoModel>> getLogos();
    Call<List<Task>> getTasks();
}