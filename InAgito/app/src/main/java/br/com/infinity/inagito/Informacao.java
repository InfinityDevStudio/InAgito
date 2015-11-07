package br.com.infinity.inagito;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Allison on 29/09/2015.
 */
public class Informacao extends Activity {
    Cursor c = null;
    SQLiteDatabase bd;
    TextView titulo;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.informacao_layout);
        titulo = (TextView) findViewById(R.id.tvTitulo);
        Intent intent = getIntent();
        Bundle params = intent.getExtras();
        if(params!=null)
        {
                String recuperado = params.getString("title");
            titulo.setText(recuperado);
        }


    try {
        bd = openOrCreateDatabase("bd_inagito", Context.MODE_PRIVATE, null);
    } catch (Exception e) {
        ExibirMensagem("Erro abrir o Banco de dados");
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
