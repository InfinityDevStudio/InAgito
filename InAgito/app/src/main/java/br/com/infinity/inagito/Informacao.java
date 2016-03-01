package br.com.infinity.inagito;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * Created by Allison on 29/09/2015.
 */
public class Informacao extends Activity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    Cursor c = null;
    SQLiteDatabase bd;
    TextView titulo, endereco, horario, descricao;
    ImageView ivhorario, ivendereco, ivdescricao;
    private GoogleMap map2;
    String recuperado, tituloBd, informacaoBd, tipoBd, latBd, lngBd, enderecoBd, horarioBd;
    private Location location2;
    private GoogleApiClient mGoogleApicliente;

    @Override
    protected void onCreate (Bundle savedInstanceState2){
        super.onCreate(savedInstanceState2);

        setContentView(R.layout.informacao_layout);
        setUpMapIfNeeded2();
       //getActionBar().setDisplayHomeAsUpEnabled(true);

        titulo = (TextView) findViewById(R.id.tvTitulo);
        Intent intent = getIntent();
        Bundle params = intent.getExtras();
        if(params!=null) {
            recuperado = params.getString("title");
            titulo.setText(recuperado);
        }



    try {
        bd = openOrCreateDatabase("bd_inagito", Context.MODE_PRIVATE, null);
        String query = "SELECT titulo,informacao,tipo,posicaolat, posicaolng, horario, endereco FROM marcador where titulo = '"+recuperado+"'";
        c = bd.rawQuery(query, null);
        c.moveToFirst();

        tituloBd = c.getString(0);
        informacaoBd = c.getString(1);
        tipoBd = c.getString(2);
        latBd = c.getString(3);
        lngBd = c.getString(4);
        horarioBd = c.getString(5);
        enderecoBd = c.getString(6);


    } catch (Exception e) {
        ExibirMensagem("Erro abrir o Banco de dados");
    }
    //#009688 tolbar
    //letras brancas
    //#00695c





        ivdescricao = (ImageView) findViewById(R.id.ivDescricao);
        ivendereco = (ImageView) findViewById(R.id.ivEndereco);
        ivhorario = (ImageView) findViewById(R.id.ivHorario);
        ivhorario.setImageResource(R.mipmap.ic_horario);
        ivendereco.setImageResource(R.mipmap.ic_endereco);
        ivdescricao.setImageResource(R.mipmap.ic_info);

        horario = (TextView) findViewById(R.id.tvHorario);
        horario.setText( horarioBd);
        endereco = (TextView) findViewById(R.id.tvEndereco);
        endereco.setText(enderecoBd);
        descricao = (TextView) findViewById(R.id.tvDescricao);
        descricao.setText(informacaoBd);







    }




    //
    //metodos
    //



    public void setUpMapIfNeeded2() {
        if (map2 == null) {

            map2 = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapInfomacao)).getMap();

            if (map2 != null) {

                setUpMap2();

            }
        }
    }


    private void setUpMap2() {



        try {

                map2.addMarker(new MarkerOptions().position(new LatLng(c.getDouble(c.getColumnIndex(latBd)), c.getDouble(c.getColumnIndex(lngBd))))
                        .title(c.getString(c.getColumnIndex(tituloBd)))
                        .snippet(c.getString(c.getColumnIndex(informacaoBd)))
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.marcador_in)));


            LatLng ll = new LatLng(c.getColumnIndex(latBd), c.getColumnIndex(lngBd));

            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, 15);
            map2.animateCamera(update);

        } catch (Exception e) {
            ExibirMensagem("Erro ao criar marcadores");
        }



    }

    @Override
    public void onConnected(Bundle bundle) {



      }




    private void ExibirMensagem(String mensagem) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("Aviso");

        dialogo.setMessage(mensagem);
        dialogo.setNeutralButton("OK", null);
        dialogo.show();

    }



    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Id correspondente ao boto Up/Home da actionbar
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
