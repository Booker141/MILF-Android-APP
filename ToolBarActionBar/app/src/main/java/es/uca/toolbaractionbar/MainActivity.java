package es.uca.toolbaractionbar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> aLAsistentes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*Inicia la actividad indicada*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Carga la barra*/
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //Necesario para rellenar el menu de la toolbar
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
                preparaAsistentes();
                Intent asistentes = new Intent(this, AsistentesActivity.class);
                Bundle b = new Bundle();
                b.putStringArrayList("asistentes", aLAsistentes);
                asistentes.putExtras(b);
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

    private void preparaAsistentes() {
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
        } catch (Exception e) {}

        /*Se pasan los DNI resultantes a un ArrayList*/
        if (text != null) {
            try {
                for (int i = 0; i < text.length(); i++) {
                    aLAsistentes.add(text.getJSONObject(i).getString("DNI"));
                }
            } catch(Exception e) {}
        }
    }

}
