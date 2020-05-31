package es.uca.toolbaractionbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AsistenteActivity extends AppCompatActivity {

    private TextView nombre, apellidos, dni, fechaNac, telefono, fechaIns;
    private CargaDatos myInvokeTask;
    private BorraDatos myInvokeTask2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*Inicia la actividad indicada*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asistente);

        /*Carga la barra*/
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Asistente");

        FloatingActionButton modificar = findViewById(R.id.modificarAsistente);
        modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = getIntent().getExtras();
                b.putString("nombre", nombre.getText().toString());
                b.putString("apellidos", apellidos.getText().toString());
                b.putString("dni", dni.getText().toString());
                b.putString("telefono", telefono.getText().toString());
                b.putString("fechaNac", fechaNac.getText().toString());
                b.putString("fechaIns", fechaIns.getText().toString());
                Intent modificarAsistente = new Intent(view.getContext(), ModificarAsistenteActivity.class);
                modificarAsistente.putExtras(b);
                view.getContext().startActivity(modificarAsistente);
            }
        });

        FloatingActionButton eliminar = findViewById(R.id.eliminarAsistente);
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Borrado directo desde AsyncTask*/
                AlertDialog.Builder adBuilder = new AlertDialog.Builder(AsistenteActivity.this);
                adBuilder.setMessage("¿Confirma el borrado de este asistente?");
                adBuilder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myInvokeTask2 = new BorraDatos(getIntent().getStringExtra("_id"));
                        myInvokeTask2.execute();
                        try {
                            Thread.sleep(500);
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                        Intent asistentes = new Intent(AsistenteActivity.this, AsistentesActivity.class);
                        AsistenteActivity.this.startActivity(asistentes);
                    }
                });
                adBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int which) {}
                });

                final AlertDialog ad = adBuilder.create();
                ad.setOnShowListener(new DialogInterface.OnShowListener() {
                   public void onShow(DialogInterface arg0) {
                       ad.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorSecondary));
                       ad.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorSecondary));
                   }
                });
                ad.show();
            }
        });

        nombre = (TextView) findViewById(R.id.asistente_nombre);
        apellidos = (TextView) findViewById(R.id.asistente_apellidos);
        dni = (TextView) findViewById(R.id.asistente_dni);
        fechaNac = (TextView) findViewById(R.id.asistente_fechaNac);
        telefono = (TextView) findViewById(R.id.asistente_telefono);
        fechaIns = (TextView) findViewById(R.id.asistente_fechaIns);

        cargaAsistenteDatos(getIntent().getStringExtra("_id"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //Necesario para rellenar el menu de la toolbar
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_asistente, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {   //Controla los clicks en la toolbar
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id) {
            case R.id.pestanaAsistentes:
                Intent asistentes = new Intent(this, AsistentesActivity.class);
                this.startActivity(asistentes);
                return true;
            case R.id.pestanaPrograma:
                Intent programa = new Intent(this, ProgramaActivity.class);
                this.startActivity(programa);
                return true;
            case R.id.pestanaFechas:
                Intent fechas = new Intent(this, FechasActivity.class);
                this.startActivity(fechas);
                return true;
            case R.id.pestanaLocalizacion:
                Intent localizacion = new Intent(this, LocalizacionActivity.class);
                this.startActivity(localizacion);
                return true;
            case R.id.pestanaInfo:
                Intent info = new Intent(this, InfoActivity.class);
                this.startActivity(info);
                return true;
            default:
                return false;
        }
    }

    private void cargaAsistenteDatos(String _id) {
        myInvokeTask = new CargaDatos(_id);
        myInvokeTask.execute();
    }

    private class CargaDatos extends AsyncTask<Void, Void, JSONObject> {

        private String _id = "";

        public CargaDatos(String _id) { this._id = _id; }

        @Override
        protected JSONObject doInBackground(Void... params) {
            /*Aparentemente necesario para una consulta correcta*/
            int SDK_INT = android.os.Build.VERSION.SDK_INT;
            if (SDK_INT > 8) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }

            /*Se realiza la consulta y se almacena el JSONObject resultante*/
            JSONObject data = null;
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url("http://"+R.string.IP_Mongo+":8080/audience/"+_id).build();
            try {
                Response res = client.newCall(request).execute();
                data = new JSONArray(res.body().string()).getJSONObject(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            if (result != null) {
                try {
                    nombre.setText(result.getString("Nombre"));
                    apellidos.setText(result.getString("Apellidos"));
                    dni.setText(result.getString("DNI"));
                    fechaNac.setText(result.getString("FechaNac"));
                    telefono.setText(result.getString("Telefono"));
                    fechaIns.setText(result.getString("FechaIns"));
                } catch (Exception e) {
                    Toast assignationError = Toast.makeText(getApplicationContext(), "Error en la obtención de datos", Toast.LENGTH_LONG);
                    assignationError.show();
                }
            } else {
                Toast noData = Toast.makeText(getApplicationContext(), "No se ha encontrado información al respecto", Toast.LENGTH_SHORT);
                noData.show();
            }
        }
    }

    private class BorraDatos extends AsyncTask<Void, Void, Boolean> {

        private String _id;

        public BorraDatos(String _id) { this._id = _id; }

        protected Boolean doInBackground(Void... params) {
            /*Aparentemente necesario para una consulta correcta*/
            int SDK_INT = android.os.Build.VERSION.SDK_INT;
            if (SDK_INT > 8) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }

            /*Se realiza la consulta y se almacena el JSONObject resultante*/
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url("http://"+R.string.IP_Mongo+":8080/audience/"+_id).delete().build();
            try {
                Response res = client.newCall(request).execute();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        protected void onPostExecute(Boolean bool) {
            if(bool) Toast.makeText(getApplicationContext(), "Asistente eliminado", Toast.LENGTH_SHORT).show();
            else Toast.makeText(getApplicationContext(), "Error al eliminar asistente", Toast.LENGTH_LONG).show();
        }
    }
}
