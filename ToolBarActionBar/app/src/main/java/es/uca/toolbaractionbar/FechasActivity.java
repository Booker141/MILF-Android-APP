package es.uca.toolbaractionbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class FechasActivity extends AppCompatActivity {

    private ImageButton fecha1, fecha2;
    private static final int NOTIF_ALERTA_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*Inicia la actividad indicada*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fechas);

        /*Carga la barra*/
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Fechas evento");

        fecha1 = (ImageButton)findViewById(R.id.fecha26);
        fecha1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "El evento del viernes 26 ya ha tenido lugar", Toast.LENGTH_LONG).show();
            }
        });

        fecha2 = (ImageButton)findViewById(R.id.fecha27);
        fecha2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), LocalizacionActivity.class);
                PendingIntent pIntent = PendingIntent.getActivity(view.getContext(), 0, intent, 0);
                NotificationCompat.Builder notification = new NotificationCompat.Builder(FechasActivity.this, "default")
                        .setSmallIcon(R.drawable.ic_logo_trans)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo))
                        .setContentTitle("¡Te estamos esperando!")
                        .setContentText("Consulta aquí la ubicación del evento para el día indicado")
                        .setContentIntent(pIntent)
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setVibrate(new long[] {1000, 1000, 1000, 1000, 1000});
                NotificationManager mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel("default", "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
                    mNotificationManager.createNotificationChannel(channel);
                }
                mNotificationManager.notify(NOTIF_ALERTA_ID, notification.build());


                Instant instant = new Timestamp(System.currentTimeMillis()).toInstant();
                ZonedDateTime zdt = instant.atZone(ZoneId.of("Europe/Madrid"));
                Date hoy = Date.from(zdt.toInstant()), fecha = null;
                try {
                    fecha = new SimpleDateFormat("yyyy/MM/dd").parse("2020/06/27");
                } catch (Exception e) { e.printStackTrace(); }
                int dias = (int)((fecha.getTime() - hoy.getTime()) / 86400000);

                Snackbar sb = Snackbar.make(view, "¡Faltan " + dias + " días!", Snackbar.LENGTH_LONG);
                sb.getView().setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.colorPrimary));
                sb.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //Necesario para rellenar el menu de la toolbar
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fechas, menu);
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

    /*private void generaNotificacion() {
        myInvokeTask = new Notificacion();
        myInvokeTask.execute();
    }

    private class Notificacion extends AsyncTask<Void, Void, Boolean> {



        protected Boolean doInBackground(Void... params) {

        }

    }*/
}
