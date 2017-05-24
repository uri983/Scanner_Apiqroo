package com.scann.apiqroo.scannerapiqroo;

/**
 * Created by Usuario on 17/05/2017.
 */

import java.util.List;

public class ListaPost {
    // Encapsulamiento de Posts
    private List<Codigos> items;

    public ListaPost(List<Codigos> items) {
        this.items = items;
    }

    public void setItems(List<Codigos> items) {
        this.items = items;
    }

    public List<Codigos> getItems() {
        return items;
    }
}