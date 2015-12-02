package br.com.infinity.inagito;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;



/**
 * Created by Allison on 24/09/2015.
 */
public class CriaBD extends Activity{
    SQLiteDatabase bd;
    Cursor c;

    @Override
    protected void onCreate (Bundle savedInstanceState){
      super.onCreate(savedInstanceState);


      try {
          bd = openOrCreateDatabase("bd_inagito", Context.MODE_PRIVATE, null);

          bd.execSQL("create table if not exists "
                  + "marcador(id_mar integer primary key autoincrement, "
                  + "titulo text not null, informacao text not null, horario text not null, endereco text not null, "
                  + "tipo text not null, posicaolat double not null, posicaolng double not null)");

          c = bd.query("marcador", new String[] { "id_mar" }, null, null, null, null, null);
          c.moveToFirst();
          Integer r = c.getCount();
          if (r == 0) {
              bd.execSQL("insert into marcador(titulo,informacao,tipo,posicaolat, posicaolng, horario, endereco) " +
                      "values('Casa Allison','Onde tudo começou a ter forma', '1', -10.8880173, -61.9244513, '7hs ás 20hs de segunda a sexta, sabado das 10hs ás 19hs', 'av principal,n 999')");

              bd.execSQL("insert into marcador(titulo,informacao,tipo,posicaolat, posicaolng, horario, endereco) " +
                      "values('Teste Topcom','Loja de informatica e telefonia, com assitencia tecnica', '2', -10.8876565, -61.923152, '7hs ás 20hs de segunda a sexta, sabado das 10hs ás 19hs', 'av principal,n 999')");
              bd.execSQL("insert into marcador(titulo,informacao,tipo,posicaolat, posicaolng, horario, endereco) " +
                      "values('Teste Bar do miodo','Bar de esquena', '2', -10.8923144, -61.9250011, '7hs ás 20hs de segunda a sexta, sabado das 10hs ás 19hs', 'av principal,n 999')");
              bd.execSQL("insert into marcador(titulo,informacao,tipo,posicaolat, posicaolng, horario, endereco) " +
                      "values('Teste ulbra','Centro Universitario Luterano de Ji-Paraná', '3', -10.8642139, -61.9606871, '7hs ás 20hs de segunda a sexta, sabado das 10hs ás 19hs', 'av principal,n 999')");
              bd.execSQL("insert into marcador(titulo,informacao,tipo,posicaolat, posicaolng, horario, endereco) " +
                      "values('Teste Igreja','Teste testado para testar /n pular linha', '3', -10.8870266, -61.9289728, '7hs ás 20hs de segunda a sexta, sabado das 10hs ás 19hs', 'av principal,n 999')");

          }

          if (true) {

              Intent intent = new Intent(this,
                      PrincipalActivity.class);
              startActivity(intent);
              onDestroy();

          }





      }catch (Exception e) {
          ExibirMensagem("Erro ao criar ou abrir o Banco de dados" + e);
      }
    }

    protected void onPause() {
        super.onPause();
        finish();
    }

    private void ExibirMensagem(String mensagem) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("Aviso");

        dialogo.setMessage(mensagem);
        dialogo.setNeutralButton("OK", null);
        dialogo.show();

    }



}
