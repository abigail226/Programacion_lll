package com.example.programacion_lll;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class agregarProductos extends AppCompatActivity {
    DB miDB;
    String accion = "nuevo";
    String idProducto = "0";
    ImageView imgFotoPrdro;
    String urlCompletaImg;
    Button btnProductos;
    Intent takePictureIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_productos);

        imgFotoPrdro = findViewById(R.id.imgFotoProducto);

        btnProductos = findViewById(R.id.btnMostrarProducto);
        btnProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarListaAmigos();
            }
        });

        guardarDatosAmigo();
        mostrarDatosAmigo();
        tomarFotoAmigo();

    }

    void tomarFotoAmigo(){
        imgFotoPrdro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    //guardar la imagen
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    }catch (Exception ex){}
                    if (photoFile != null) {
                        try {
                            Uri photoURI = FileProvider.getUriForFile(agregarProductos.this, "com.example.programacion_lll.fileprovider", photoFile);
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            startActivityForResult(takePictureIntent, 1);
                        }catch (Exception ex){
                            Toast.makeText(getApplicationContext(), "Error al tomar la fotografia: "+ ex.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 1 && resultCode == RESULT_OK) {
                Bitmap imageBitmap = BitmapFactory.decodeFile(urlCompletaImg);
                imgFotoPrdro.setImageBitmap(imageBitmap);
            }
        }catch (Exception ex){
            Toast.makeText(getApplicationContext(), "Error: "+ ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "imagen_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if( storageDir.exists()==false ){
            storageDir.mkdirs();
        }
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        urlCompletaImg = image.getAbsolutePath();
        return image;
    }
    void guardarDatosAmigo(){
        btnProductos = findViewById(R.id.btnGuardarProducto);
        btnProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView tempVal = findViewById(R.id.txtCodigo);
                String codigo = tempVal.getText().toString();

                tempVal = findViewById(R.id.txtDescripcion);
                String descripcion = tempVal.getText().toString();

                tempVal = findViewById(R.id.txtMarca);
                String marca = tempVal.getText().toString();

                tempVal = findViewById(R.id.txtPresentacion);
                String presentacion = tempVal.getText().toString();

                tempVal = findViewById(R.id.txtPrecio);
                String precio = tempVal.getText().toString();

                String[] data = {idProducto,codigo,descripcion,marca,presentacion,precio, urlCompletaImg};

                miDB = new DB(getApplicationContext(),"", null, 1);
                miDB.mantenimientoProducto(accion, data);

                Toast.makeText(getApplicationContext(),"El registro de productos se realizo con exito", Toast.LENGTH_LONG).show();
                mostrarListaAmigos();
            }
        });
    }
    void mostrarListaAmigos(){
        Intent mostrarProductos = new Intent(agregarProductos.this, MainActivity.class);
        startActivity(mostrarProductos);
    }
    void mostrarDatosAmigo(){
        try {
            Bundle recibirParametros = getIntent().getExtras();
            accion = recibirParametros.getString("accion");
            if (accion.equals("modificar")){
                String[] dataAmigo = recibirParametros.getStringArray("dataAmigo");

                idProducto = dataAmigo[0];

                TextView tempVal = (TextView)findViewById(R.id.txtCodigo);
                tempVal.setText(dataAmigo[1]);

                tempVal = (TextView)findViewById(R.id.txtPresentacion);
                tempVal.setText(dataAmigo[2]);

                tempVal = (TextView)findViewById(R.id.txtMarca);
                tempVal.setText(dataAmigo[3]);

                tempVal = (TextView)findViewById(R.id.txtPresentacion);
                tempVal.setText(dataAmigo[4]);

                tempVal = (TextView)findViewById(R.id.txtPrecio);
                tempVal.setText(dataAmigo[5]);

                urlCompletaImg = dataAmigo[6];
                Bitmap imageBitmap = BitmapFactory.decodeFile(urlCompletaImg);
                imgFotoPrdro.setImageBitmap(imageBitmap);
            }
        }catch (Exception ex){
            ///
        }
    }
}