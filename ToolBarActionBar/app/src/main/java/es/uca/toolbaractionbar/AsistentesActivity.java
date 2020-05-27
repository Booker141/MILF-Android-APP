package es.uca.toolbaractionbar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AsistentesActivity extends AppCompatActivity {

    private RecyclerView myRecyclerView;
    private RecyclerView.Adapter myAdapter;
    private RecyclerView.LayoutManager myLayoutManager;
    private LongRunningGetIO myInvokeTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asistentes);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Asistentes");

        myLayoutManager = new LinearLayoutManager(this);
        preparaAsistentes();

        FloatingActionButton fab = findViewById(R.id.anadirAsistente);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent anadirAsistente = new Intent(view.getContext(), AnadirAsistenteActivity.class);
                view.getContext().startActivity(anadirAsistente);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //Necesario para rellenar el menu de la toolbar
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_asistentes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {   //Controla los clicks en la toolbar
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id) {
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

    private void preparaAsistentes() {
        myInvokeTask = new LongRunningGetIO();
        myInvokeTask.execute();
    }

    private class LongRunningGetIO extends AsyncTask<Void, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(Void... params) {
            /*Aparentemente necesario para una consulta correcta*/
            int SDK_INT = android.os.Build.VERSION.SDK_INT;
            if (SDK_INT > 8) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }

            /*Se realiza la consulta y se almacena el JSONArray resultante*/
            JSONArray text = null;
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url("http://10.0.2.2:8080/audience/").build();
            try {
                Response res = client.newCall(request).execute();
                text = new JSONArray(res.body().string());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return text;
        }

        @Override
        protected void onPostExecute(JSONArray result) {
            ArrayList<JSONObject> asistentes = new ArrayList<>();
            if (result != null) {
                try {
                    for (int i = 0; i < result.length(); i++) {
                        String preview = "";
                        preview += result.getJSONObject(i).getString("Nombre");
                        preview += (" " + result.getJSONObject(i).getString("Apellidos"));
                        asistentes.add(result.getJSONObject(i));
                    }
                    Toast consultaOk = Toast.makeText(getApplicationContext(), "Se han obtenido " + asistentes.size() + " resultados", Toast.LENGTH_SHORT);
                    consultaOk.show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast consultaError = Toast.makeText(getApplicationContext(), "Error en la obtención de datos", Toast.LENGTH_LONG);
                    consultaError.show();
                }
            }
            /*Inicializamos el RecyclerView con los datos obtenidos*/
            myRecyclerView = (RecyclerView)findViewById(R.id.asistentes_RecyclerView);
            myRecyclerView.setHasFixedSize(true);
            myRecyclerView.setLayoutManager(myLayoutManager);
            myAdapter = new AsistenteAdapter(asistentes);
            myRecyclerView.setAdapter(myAdapter);
        }
    }

}
