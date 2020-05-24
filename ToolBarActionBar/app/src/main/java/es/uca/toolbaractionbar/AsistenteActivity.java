package es.uca.toolbaractionbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class AsistenteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*Inicia la actividad indicada*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asistente);

        /*Carga la barra*/
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
}
