package com.alejandro.aplicaciondelista;

import com.alejandro.aplicaciondelista.model.ItemContent;
import com.alejandro.aplicaciondelista.model.JsonObjectRequest;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {

        JsonObjectRequest request = new JsonObjectRequest("GET", ItemContent.URL_IMAGES_BASE + ItemContent.URL_PRODUCTS, null);
        request.execute();

        assertEquals(4, 2 + 2);
    }
}