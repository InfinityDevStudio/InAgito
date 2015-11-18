package br.com.infinity.inagito;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class PrincipalActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private GoogleMap map;

    private GoogleApiClient mGoogleApicliente;
    private Cursor c = null;
    private Location location;
    private SQLiteDatabase bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //abri o bd
        try {
            bd = openOrCreateDatabase("bd_inagito", Context.MODE_PRIVATE, null);
        } catch (Exception e) {
            ExibirMensagem("Erro abrir o Banco de dados");
        }

        callConnection();

        setUpMapIfNeeded();

        map.setOnMarkerClickListener(new OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                Intent intent = new Intent(PrincipalActivity.this, Informacao.class);
                Bundle params = new Bundle();
                String titulo = marker.getTitle();
                params.putString("title", titulo );
                intent.putExtras(params);
                startActivity(intent);
                return true;
            }
        });
    }

    public synchronized void callConnection(){
        //conectando com a Google Play Servicos
        mGoogleApicliente = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApicliente.connect();

    }

    @Override
    protected void onStart(){
        super.onStart();
        if  ( mGoogleApicliente !=  null )  {
            mGoogleApicliente.connect ();
        }
    }


    @Override
    protected void onResume(){
        super.onResume();
        if (mGoogleApicliente==null) {
           callConnection();
        }

    }


    @Override
    protected void onPause(){

        super.onPause();
        if (mGoogleApicliente.isConnected()) {
            mGoogleApicliente.disconnect();
        }
    }

    @Override
    protected void onStop(){
        mGoogleApicliente.disconnect();
        super.onStop();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_frag_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(map!=null ){
            if(item.getItemId()==R.id.action_mapa_hibrido){
                map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            }else if (item.getItemId()==R.id.action_mapa_normal){
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }else if (item.getItemId()==R.id.action_mapa_satelite){
                map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            }else if (item.getItemId()==R.id.action_mapa_terreno){
                map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            }else if (item.getItemId()==R.id.action_zoom_in){
                map.animateCamera(CameraUpdateFactory.zoomIn());
            }else if (item.getItemId()==R.id.action_zoom_out){
                map.animateCamera(CameraUpdateFactory.zoomOut());
            };

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onConnected(Bundle bundle) {

        location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApicliente);

        if (location != null) {
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());

            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, 15);
            map.animateCamera(update);

        }
        else {
            handleNewLocation(location);
        };



    }

    private void handleNewLocation(Location location) {
        Log.d("TAG", location.toString());
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("Teste2","Coneccao ao google play services interrropida"+i );
    }


    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i("TAG", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("Teste","Conectado ao google play services" );
        this.map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }


    //
    //metodos
    //



    public void setUpMapIfNeeded() {
           if (map == null) {

            map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                    .getMap();

            if (map != null) {

                setUpMap();

            }
        }
    }


    private void setUpMap() {

        map.setMyLocationEnabled(true);

        try {
            String query = "SELECT * FROM marcador ";
            c = null;
            c = bd.rawQuery(query, null);

            c.moveToFirst();
            Integer r = c.getCount();
            Integer i = 0;
            Integer icon=null;
            while (i < r) {

                int j = c.getInt(c.getColumnIndex("tipo"));

                if (j == 1){
                    icon = R.mipmap.marcador_in;

                }else if (j == 2){
                    icon = R.mipmap.marcador_in;

                }else if (j== 3){
                    icon = R.mipmap.marcador_in;

                }
                map.addMarker(new MarkerOptions().position(new LatLng(c.getDouble(c.getColumnIndex("posicaolat")), c.getDouble(c.getColumnIndex("posicaolng"))))
                        .title(c.getString(c.getColumnIndex("titulo")))
                        .snippet(c.getString(c.getColumnIndex("informacao")))
                        .icon(BitmapDescriptorFactory.fromResource(icon)));

                c.moveToNext();
                i++;
            }
        } catch (Exception e) {
            ExibirMensagem("Erro ao criar marcadores");
        }
    }

    private void ExibirMensagem(String mensagem) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("Aviso");
        dialogo.setMessage(mensagem);
        dialogo.setNeutralButton("OK", null);
        dialogo.show();
    }


}
