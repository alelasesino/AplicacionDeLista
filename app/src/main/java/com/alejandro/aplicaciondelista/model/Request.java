package com.alejandro.aplicaciondelista.model;

import java.io.IOException;

public interface Request<T>  {

    T sendRequest(String params) throws IOException;

}
