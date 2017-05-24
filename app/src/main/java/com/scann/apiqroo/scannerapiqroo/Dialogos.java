package com.scann.apiqroo.scannerapiqroo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Uriel Velasquez on 19/05/2017.
 */

public class Dialogos  extends AppCompatActivity {

    Activity actividad;

    public Dialogos (Activity actividad){

       this.actividad  = actividad;
    }

    public void showDialogo(String titulo, String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(actividad);

        builder.setTitle(titulo)
                .setMessage(mensaje)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });


        Dialog dialog = builder.create();
        dialog.show();
    }

    public void show_logout_dialog(String titulo, String mensaje , final Activity actividad) {
        AlertDialog.Builder builder = new AlertDialog.Builder(actividad);

        builder.setTitle(titulo)
                .setMessage(mensaje)
                .setPositiveButton("Aceptar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SessionPrefs.get(actividad).logOut();
                                actividad.finish();
                            }
                        })
                .setNegativeButton("Cancelar", null);//sin listener;


        Dialog dialog = builder.create();
        dialog.show();
    }


}
