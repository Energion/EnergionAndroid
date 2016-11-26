package com.github.energion.energionandroid;

import com.github.energion.energionandroid.model.Day;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BackendService {
  @GET("get-dataset")
  Call<List<Day>> listDays();
}