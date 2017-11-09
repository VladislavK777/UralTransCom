package com.uraltranscom.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*
*
* Класс переменных для соединения с порталом
*
* @author Vladislav Klochkov
* @version 1.0
* @create 25.10.2017
*
*/

public class VaribaleForRestAPI {

    public Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://poisk-vagonov.ru")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    public SomeRestApi api = retrofit.create(SomeRestApi.class);

}
