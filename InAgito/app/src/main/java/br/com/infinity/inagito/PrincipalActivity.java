package br.com.infinity.inagito;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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


public class PrincipalActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    private GoogleMap map;
    private Location location;
    private GoogleApiClient mGoogleApicliente;
    private LocationManager locationManager;
    Cursor c = null;
    SQLiteDatabase bd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        //abri o bd
        try {
            bd = openOrCreateDatabase("bd_inagito", Context.MODE_PRIVATE, null);
        } catch (Exception e) {
            ExibirMensagem("Erro abrir o Banco de dados");
        }

        setUpMapIfNeeded();

        mGoogleApicliente = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        //.apiClientBuilder






       map.setOnMarkerClickListener(new OnMarkerClickListener() {
           @Override
           public boolean onMarkerClick(Marker marker) {


               if (marker.getTitle() != null) {
                   Intent intent = new Intent(PrincipalActivity.this, Informacao.class);
                   intent.putExtra("title", marker.getTitle());
                   startActivity(intent);
               }
               return false;
           }
       } );

    }



    private void setUpMapIfNeeded() {



        // Do a null check to confirm that we have not already instantiated the map.
        if (map == null) {
            // Try to obtain the map from the SupportMapFragment.
            map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (map != null) {

                setUpMap();
            }
        }
    }



    private void setUpMap() {

        map.setMyLocationEnabled(true);
        // colocar localizaao atual do usuario
        if(mGoogleApicliente!=null) {
            location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApicliente);
            ExibirMensagem("teste" + Double.toString(location.getLatitude()) + Double.toString(location.getLongitude()));
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            LatLng ll = new LatLng(lat, lng);


            map.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 10));
        }else if (mGoogleApicliente == null){
            LatLng ll = new LatLng(  -10.8880173, -61.9244513);

            map.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 10));
        }



        map.animateCamera(CameraUpdateFactory.zoomTo(15), 5000, null);
        map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marcacao teste"));

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
                        .title(c.getString(c.getColumnIndex("titulo"))).snippet(c.getString(c.getColumnIndex("informacao")))
                        .icon(BitmapDescriptorFactory.fromResource(icon)));

                c.moveToNext();
                i++;
            }
        } catch (Exception e) {
            ExibirMensagem("Erro ao criar marcadores");
        }


        }



    @Override
    protected void onStart(){
        super.onStart();
        mGoogleApicliente.connect();

    }


    @Override
    protected void onResume(){
        super.onResume();
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this.onLocationChanged());
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) this);
    }

    @Override
    protected void onPause(){
        super.onPause();
        locationManager.removeUpdates((LocationListener) this);
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

    private void ExibirMensagem(String mensagem) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("Aviso");

        dialogo.setMessage(mensagem);
        dialogo.setNeutralButton("OK", null);
        dialogo.show();

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
        Log.d("Teste","Conectado ao google play services" );
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("Teste2","Coneccao ao google play services interrropida" );
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("Teste","Conectado ao google play services"+connectionResult );

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("Teste","Conectado ao google play services" );
       // this.googleMap = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

    }
}
