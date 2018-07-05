package com.example.usrgam.tareasasincronas;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
private Button bEjecutar ,bCancelar;
private Button bEjecutarhilo;
private Button bAsync,bDialogoK;
private ProgressBar barraProgreso;
private TareaAsync tareaAsync;
private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bEjecutar=(Button)findViewById(R.id.btnEjecutar);
        barraProgreso=(ProgressBar)findViewById(R.id.progressBar);
        bEjecutarhilo=(Button)findViewById(R.id.bntHilo);
        bAsync=(Button)findViewById(R.id.bntAsyn);
        bCancelar=(Button)findViewById(R.id.bntCancelar);
        bDialogoK=(Button)findViewById(R.id.bntDialogo);
        bEjecutar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barraProgreso.setMax(100);
                barraProgreso.setProgress(0);
                for(int i=1;i<=100;i++){
                    //incrementa de uno en uno
                    ejecutarTarea();
                    barraProgreso.incrementProgressBy(1);
                }
                Toast.makeText(MainActivity.this,"Finalizo",Toast.LENGTH_LONG).show();
            }
        });
        ///*****ejecutar hilo****
        bEjecutarhilo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        barraProgreso.post(new Runnable() {
                            @Override
                            public void run() {
                                barraProgreso.setProgress(0);
                            }
                        });
                        for(int i=1;i<=100;i++){
                            ejecutarTarea();
                            barraProgreso.post(new Runnable() {
                                @Override
                                public void run() {
                                    barraProgreso.incrementProgressBy(1);
                                }
                            });
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this,"FinalizoHilo",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }).start();
            }
        });
        ////-----------Boton Asincrono
        bAsync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tareaAsync=new TareaAsync();
                tareaAsync.execute();
               // tareaAsync.cancel(true);
            }
        });
        bCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tareaAsync.cancel(true);
            }
        });

    }
    //******BtmnDialogo
    /*bDialogoK.setOnClickListener(new new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            barraProgreso=new ProgressDialog(MainActivity.this);
            barraProgreso.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            barraProgreso.progressDialog.setMessage("Conectando...");
            barraProgreso.setCancelable(true);
            barraProgreso.setMax(100);
            //LLamar a la tarea asuncrona;
            TareaAsyncDialogo tareaAsyncDialogo = new TareaAsyncDialogo();
            tareaAsyncDialogo.execute();

        });}*/

    public  void ejecutarTarea(){
        try {
            //se demora 12.5 seg hilo de espera
            Thread.sleep(175);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    public void ejecutarDialogo(View view) {
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("Conectando...");
        progressDialog.setCancelable(true);
        progressDialog.setMax(100);
        //LLamar a la tarea asuncrona;
        TareaAsyncDialogo tareaAsyncDialogo = new TareaAsyncDialogo();
        tareaAsyncDialogo.execute();
    }

    public class TareaAsync extends AsyncTask< Void,Integer,Boolean>{

        @Override
        protected Boolean doInBackground(Void... voids) {
            for(int i=1;i<=100;i++){
                ejecutarTarea();
                publishProgress(i);
                if(isCancelled()){
                    //aqui en vez de un break un intent o un dialogo
                    break;
                }
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int progreso=values[0].intValue();
            barraProgreso.setProgress(progreso);

        }

        @Override
        protected void onPreExecute() {
            barraProgreso.setMax(100);
            barraProgreso.setProgress(0);

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
           if(aBoolean){
               Toast.makeText(MainActivity.this,"FinalizoHiloK",Toast.LENGTH_LONG).show();
           }
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(MainActivity.this,"Se Cancelo",Toast.LENGTH_LONG).show();
        }
    }
    public class TareaAsyncDialogo extends AsyncTask< Void,Integer,Boolean>{
        protected Boolean doInBackground(Void... voids) {
            for(int i=1;i<=100;i++){
                ejecutarTarea();
                publishProgress(i);
                if(isCancelled()){
                    //aqui en vez de un break un intent o un dialogo
                    break;
                }
            }
            return true;
        }
        protected void onProgressUpdate(Integer... values) {
            int progreso=values[0].intValue();
            barraProgreso.setProgress(progreso);

        }

        @Override
        protected void onPreExecute() {
           progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
               @Override
               public void onCancel(DialogInterface dialog) {
                   TareaAsyncDialogo.this.cancel(true);
               }
           });
           barraProgreso.setProgress(0);
           progressDialog.show();

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
           if (aBoolean){
               progressDialog.dismiss();
               Toast.makeText(MainActivity.this,"FinalizoDialogo",Toast.LENGTH_LONG).show();
        }
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(MainActivity.this,"CanceloDialogo",Toast.LENGTH_LONG).show();
        }
    }

}
