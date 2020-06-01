package es.uca.toolbaractionbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.maps.SupportMapFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ProgramaActivity extends AppCompatActivity {

    /*private Button btnCrearPDF;
    private LinearLayout lnLayout;
    private Bitmap bitmap;
    private int MY_PERMISSIONS_REQUEST_EXTERNAL_READ = 1;
    private int MY_PERMISSIONS_REQUEST_EXTERNAL_WRITE = 2;*/
    private Button arriba;
    private ScrollView sv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programa);

        /*Carga la barra*/
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Programa");

        arriba = (Button)findViewById(R.id.boton_arriba);
        sv = (ScrollView)findViewById(R.id.scrollView);
        arriba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sv.fullScroll(ScrollView.FOCUS_UP);
            }
        });

        //Pruebas para generar PDF
        /*btnCrearPDF = findViewById(R.id.btn);
        lnLayout = findViewById(R.id.llPdf);

        btnCrearPDF.setOnClickListener(new View.OnClickListener() {
           public void onClick(View v) {
               Log.d("Tamaño", " " + lnLayout.getWidth() + " " + lnLayout.getHeight());
               bitmap = loadBitmapFromView(lnLayout, lnLayout.getWidth(), lnLayout.getHeight());
               ActivityCompat.requestPermissions(ProgramaActivity.this,
                       new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_EXTERNAL_READ);
               ActivityCompat.requestPermissions(ProgramaActivity.this,
                       new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_EXTERNAL_WRITE);
               createPdf();
           }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //Necesario para rellenar el menu de la toolbar
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_programa, menu);
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

    //Pruebas para generar PDF
    /*public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap bmpImg = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpImg);
        v.draw(c);
        return bmpImg;
    }

    private void createPdf() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float height = displaymetrics.heightPixels;
        float width = displaymetrics.widthPixels;

        int convertHighet = (int) height, convertWidth = (int) width;
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(convertWidth, convertHighet, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        canvas.drawPaint(paint);

        bitmap = Bitmap.createScaledBitmap(bitmap, convertWidth, convertHighet, true);

        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        document.finishPage(page);

        String targetPdf = Environment.DIRECTORY_DOWNLOADS + "/Programa_MILF.pdf";
        File filePath;
        filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Algo anda mal: " + e.toString(), Toast.LENGTH_LONG).show();
        }
        document.close();
        Toast.makeText(this, "PDF generado", Toast.LENGTH_SHORT).show();
        abrirPDFGenerado();
    }

    private void abrirPDFGenerado() {
        File file = new File(Environment.DIRECTORY_DOWNLOADS + "/Programa_MILF.pdf");
        if(file.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(ProgramaActivity.this, "No hay aplicación disponible para ver archivos PDF", Toast.LENGTH_LONG).show();
            }
        }
    }*/
}
