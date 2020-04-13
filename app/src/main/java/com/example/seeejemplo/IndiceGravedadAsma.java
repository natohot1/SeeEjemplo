package com.example.seeejemplo;

import android.animation.Animator;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.github.chrisbanes.photoview.PhotoView;

public class IndiceGravedadAsma extends AppCompatActivity {
    Bundle datos;
    String medicamen_final;
    private Animator currentAnimator;
    private int shortAnimationDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indice_gravedad_asma);

        PhotoView photoView = (PhotoView) findViewById(R.id.photo_view);
        photoView.setImageResource(R.drawable.asma_gravedad2);


//
//    public void onCli2(MenuItem item) {
//        Intent intent = new Intent(IndiceGravedadAsma.this, Asma.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("medcinas", medicamen_final);
//        intent.putExtras(bundle);
//        startActivity(intent);
//
//
   }

}
