package es.uca.toolbaractionbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AnadirAsistenteActivity extends AppCompatActivity {

    private EditText nombre, apellidos, dni, telefono, fechaNac;
    private TextView error;
    private DatePickerDialog picker;
    private Button enviar;
    private LongRunningGetIO myInvokeTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_asistente);

        /*Carga la barra*/
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Añade asist.");

        /*Formulario*/
        nombre = (EditText)findViewById(R.id.nombre_formulario);
        apellidos = (EditText)findViewById(R.id.apellidos_formulario);
        dni = (EditText)findViewById(R.id.dni_formulario);
        telefono = (EditText)findViewById(R.id.telefono_formulario);

        fechaNac = (EditText)findViewById(R.id.fechanac_formulario);
        fechaNac.setInputType(InputType.TYPE_NULL);
        fechaNac.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int dia = cldr.get(Calendar.DAY_OF_MONTH);
                int mes = cldr.get(Calendar.MONTH);
                int ano = cldr.get(Calendar.YEAR);
                picker = new DatePickerDialog(AnadirAsistenteActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int ano, int mes, int dia) {
                        String birth = ano + "-";
                        if(mes < 10) birth += '0';
                        birth += (mes+1) + "-";
                        if(dia < 10) birth += '0';
                        birth += dia;
                        fechaNac.setText(birth);
                    }
                }, 2000, mes, dia);
                cldr.set(Calendar.YEAR, ano-100);
                picker.getDatePicker().setMinDate(cldr.getTimeInMillis());
                cldr.set(Calendar.YEAR, ano-6);
                picker.getDatePicker().setMaxDate(cldr.getTimeInMillis());
                picker.show();
            }
        });

        enviar = (Button)findViewById(R.id.boton_formulario);
        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(datosOk(nombre.getText().toString(), apellidos.getText().toString(), dni.getText().toString(), telefono.getText().toString(), fechaNac.getText().toString())) {
                    anadeAsistente(nombre.getText().toString(), apellidos.getText().toString(), dni.getText().toString(), telefono.getText().toString(), fechaNac.getText().toString());

                    Intent asistentes = new Intent(view.getContext(), AsistentesActivity.class);
                    view.getContext().startActivity(asistentes);
                }
            }
        });

        error = (TextView)findViewById(R.id.error_formulario);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //Necesario para rellenar el menu de la toolbar
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_anadir_asistente, menu);
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

    private boolean datosOk(String nombre, String apellidos, String dni, String telefono, String fechaNac) {
        boolean ok = true;
        String errorMsg = "";
        if(nombre.trim().isEmpty()) {
            errorMsg += "Debe introducir un nombre\n";
            ok = false;
        }
        if(apellidos.trim().isEmpty()) {
            errorMsg += "Debe introducir apellidos\n";
            ok = false;
        }
        if(dni.trim().length() != 9) {
            errorMsg += "El DNI debe estar formado por 8 números y 1 letra\n";
            ok = false;
        }
        if(telefono.trim().isEmpty()) {
            errorMsg += "Debe introducir un teléfono\n";
            ok = false;
        }
        if(fechaNac.trim().isEmpty()) {
            errorMsg += "Debe introducir una fecha de nacimiento";
        }
        error.setText(errorMsg);
        return ok;
    }

    private void anadeAsistente(String nombre, String apellidos, String dni, String telefono, String fechaNac) {
        myInvokeTask = new LongRunningGetIO(nombre, apellidos, dni, telefono, fechaNac);
        myInvokeTask.execute();
    }

    private class LongRunningGetIO extends AsyncTask<Void, Void, Boolean> {

        private String nombre, apellidos, dni, telefono, fechaNac;

        protected LongRunningGetIO(String nombre, String apellidos, String dni, String telefono, String fechaNac) {
            this.nombre = nombre;
            this.apellidos = apellidos;
            this.dni = dni;
            this.telefono = telefono;
            this.fechaNac = fechaNac;
        }

        protected Boolean doInBackground(Void... params) {
            /*Aparentemente necesario para una consulta correcta*/
            int SDK_INT = android.os.Build.VERSION.SDK_INT;
            if (SDK_INT > 8) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }

            /*Se realiza la inserción*/
            OkHttpClient client = new OkHttpClient();
            Instant instant = new Timestamp(System.currentTimeMillis()).toInstant();
            ZonedDateTime zdt = instant.atZone(ZoneId.of("Europe/Madrid"));
            RequestBody formBody = new FormBody.Builder()
                    .add("Nombre", nombre)
                    .add("Apellidos", apellidos)
                    .add("DNI", dni)
                    .add("FechaNac", fechaNac)
                    .add("Telefono", telefono)
                    .add("FechaIns", zdt.toString().substring(0, 10) + " " + zdt.toString().substring(11, 19))
                    .build();
            Request request = new Request.Builder().url(getString(R.string.MongoURL)).post(formBody).build();

            try {
                Response res = client.newCall(request).execute();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        protected void onPostExecute(Boolean bool) {
            if(bool) Toast.makeText(getApplicationContext(), "Asistente insertado", Toast.LENGTH_SHORT).show();
            else Toast.makeText(getApplicationContext(), "Error al insertar asistente", Toast.LENGTH_LONG).show();
        }
    }
}
