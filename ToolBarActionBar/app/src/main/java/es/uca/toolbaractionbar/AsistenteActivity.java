package es.uca.toolbaractionbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

    private TextView nombre_apellidos, dni, fechaNac, telefono, fechaIns;
    private LongRunningGetIO myInvokeTask;

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
        /*modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent modificarAsistente = new Intent(view.getContext(), ModificarAsistenteActivity.class);
                view.getContext().startActivity(modificarAsistente);
            }
        });*/

        FloatingActionButton eliminar = findViewById(R.id.eliminarAsistente);
        /*eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent eliminarAsistente = new Intent(view.getContext(), EliminarAsistenteActivity.class);
                view.getContext().startActivity(eliminarAsistente);
            }
        });*/

        nombre_apellidos = (TextView) findViewById(R.id.asistente_nombre_apellidos);
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
            /*case R.id.pestanaPrograma:
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
                return true;*/
            default:
                return false;
        }
    }

    private void cargaAsistenteDatos(String _id) {
        myInvokeTask = new LongRunningGetIO(_id);
        myInvokeTask.execute();
    }

    private class LongRunningGetIO extends AsyncTask<Void, Void, JSONObject> {

        private String _id = "";

        public LongRunningGetIO(String _id) { this._id = _id; }

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
            Request request = new Request.Builder().url("http://10.0.2.2:8080/audience/"+_id).build();
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
                    String nombreCompleto = result.getString("Nombre") + " " + result.getString("Apellidos");
                    nombre_apellidos.setText(nombreCompleto);
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
}
