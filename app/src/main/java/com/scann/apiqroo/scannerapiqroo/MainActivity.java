package com.scann.apiqroo.scannerapiqroo;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.Result;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , ZXingScannerView.ResultHandler  {

    private  ZXingScannerView viewScanner;
    Dialogos dialog = new Dialogos(this);
    ImageButton boton_scan;
    TextView user_name;
    //Button scanear ;

    JSONObject obj = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        boton_scan = (ImageButton) this.findViewById(R.id.imageButton);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {Manifest.permission.CAMERA}, 1);
            }
        }

        if (!SessionPrefs.get(this).isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }else{

            setUser_menu();
        }




        boton_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startScanner();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



    }

    public void setUser_menu(){

        SharedPreferences settings = getSharedPreferences("SALUDMOCK_PREFS", MODE_PRIVATE);
        String nombre = settings.getString("PREFS_USER_MAIL", "apiqroo");
        Integer first_log  = settings.getInt("first_log",0);
        //Toast.makeText(this, nombre, Toast.LENGTH_SHORT).show();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        user_name = (TextView) headerView.findViewById(R.id.user_name);
        user_name.setText(nombre);

        if(first_log == 1){

            Dialogos dialog = new Dialogos(this);
            dialog.showDialogo("Inicio de sesión","¡Bienvenido!");
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("first_log", 0);
            editor.apply();




        }





    }


    public void startScanner(){
        try {
            viewScanner = new ZXingScannerView(this);
            setContentView(viewScanner);
            viewScanner.setResultHandler(this);
            viewScanner.startCamera();
        }catch(Exception e){}

    }

    @Override
    protected void onPause() {
        try { super.onPause();

            viewScanner.stopCamera();
        }catch(Exception e){}
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

         if (id == R.id.nav_logout) {

            Dialogos dialog = new Dialogos(this);
            dialog.show_logout_dialog("Cierre de Sesión","¿ Deseas cerrar tu sesión en este dispositivo?",this);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

         try {
             if (keyCode == KeyEvent.KEYCODE_BACK) {

                 DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

                 if (drawer.isDrawerOpen(GravityCompat.START)) {
                     drawer.closeDrawer(GravityCompat.START);
                 } else {
                     //super.onBackPressed();
                     new AlertDialog.Builder(this)
                             .setIcon(android.R.drawable.ic_dialog_info)
                             .setTitle("Estas saliendo de la aplicación")
                             .setMessage("¿Seguro que quieres salir de la aplicación?")
                             .setNegativeButton("Cancelar", null)//sin listener
                             .setPositiveButton("Salir", new DialogInterface.OnClickListener() {//un listener que al pulsar, cierre la aplicacion
                                 @Override
                                 public void onClick(DialogInterface dialog, int which) {
                                     //Salir
                                     try {
                                         MainActivity.this.finish();
                                     }catch(Exception e){

                                         MainActivity.this.finish();}
                                 }
                             })
                             .show();
                     // Si el listener devuelve true, significa que el evento esta procesado, y nadie debe hacer nada mas
                     return true;
                 }


             }
         }catch(Exception e){

             startActivity(new Intent(getBaseContext(), MainActivity.class));
             finish();
         }
        //para las demas cosas, se reenvia el evento al listener habitual
        return super.onKeyDown(keyCode, event);

    }


    public void saveData(final String codigo , final ZXingScannerView.ResultHandler result){

        //Toast.makeText(getBaseContext(), " "+ codigo , Toast.LENGTH_LONG).show();

        RequestQueue queue = Volley.newRequestQueue(this);


        StringRequest sr = new StringRequest(Request.Method.POST,"http://servicios.apiqroo.com.mx/app_scaner/home/addCode", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //mPostCommentResponse.requestCompleted();

                try{
                    obj = new JSONObject(response);
                    if( Integer.parseInt( obj.getString("action")) == 1) {
                        show_scan_confirmation("Guardado con éxito", "el código " + codigo + " se guardo con exito", result);
                    } else if (Integer.parseInt( obj.getString("action")) == 2){

                        show_scan_confirmation("Codigo repetido", "El codigo " + codigo + " se registro anteriormente y no puede ser usado", result);

                    } else if (Integer.parseInt( obj.getString("action")) == 0){

                        show_scan_confirmation("Error de guardado", "Ocurrio un error, no se registraron cambios", result);

                    } else{

                        show_scan_confirmation("Error de guardado", "Ocurrio un error, no se registraron cambios", result);
                    }


                    //Toast.makeText(getBaseContext(), "guardado " , Toast.LENGTH_LONG).show();

                }catch(Exception e){
                    show_scan_confirmation("Error de guardado",""+ e.getMessage() , result);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mPostCommentResponse.requestEndedWithError(error);
                show_scan_confirmation("Error de guardado", "Ocurrio un error, no se registraron cambios", result);
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("code","" + codigo);


                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };

        //GlobalSingleton.getInstance(getBaseContext()).addToRequestQueue(ArrayRequest);

        queue.add(sr);


    }


    @Override
    public void handleResult(Result result) {
        Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
        vib.vibrate(100);
        saveData(result.getText(),this);



    }


    public void show_scan_confirmation(String titulo, String mensaje, final ZXingScannerView.ResultHandler respuesta) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(titulo)
                .setMessage(mensaje)
                .setPositiveButton("Seguir Capturando", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        viewScanner.resumeCameraPreview(respuesta);
                    }
                })
                .setNegativeButton("Terminar Capturas",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(getBaseContext(), MainActivity.class));
                                finish();
                            }
                        } );


        Dialog dialog = builder.create();
        dialog.show();
    }
}










