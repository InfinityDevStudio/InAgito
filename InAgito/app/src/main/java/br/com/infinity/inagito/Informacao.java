package br.com.infinity.inagito;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

/**
 * Created by Allison on 29/09/2015.
 */
public class Informacao extends Activity {
    Cursor c = null;
    SQLiteDatabase bd;


    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.informacao_layout);


        Intent intent = getIntent();
        String recuperado = (String) intent.getSerializableExtra("title");

    try {
        bd = openOrCreateDatabase("bd_inagito", Context.MODE_PRIVATE, null);

            String query = "SELECT * FROM marcador where title = " + recuperado;
            c = bd.rawQuery(query, null);
            c.moveToFirst();

        ExibirMensagem("Vc abril mais informações de " + c.getColumnIndex("titulo"));


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
