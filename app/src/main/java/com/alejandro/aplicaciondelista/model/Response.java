package com.alejandro.aplicaciondelista.model;

public class Response {

    public interface Listener<T>{
        void onResponse(T response);
    }

    public interface ErrorListener<T>{
        void onErrorResponse(T response);
    }

}
