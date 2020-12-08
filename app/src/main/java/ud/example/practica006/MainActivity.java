package ud.example.practica006;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private SoundPool sPool;
    private int sound1, sound2, sound3, sound4;
    private MediaPlayer player;
    private boolean isPlaying = true;
    private AudioManager audioManager;
    private Button boton03, boton04;
    private SeekBar volumen, avance;
    private TextView total, hasonado, progreso, estado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            sPool = new SoundPool.Builder()
                    .setAudioAttributes(attributes)
                    .setMaxStreams(10)
                    .build();
        }
        else{
            sPool = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);
        }
        sound1  = sPool.load(this, R.raw.gallina, 1);
        sound2  = sPool.load(this, R.raw.perro, 1);
        sound3  = sPool.load(this, R.raw.leon, 1);
        sound4  = sPool.load(this, R.raw.serpiente, 1);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        boton03 = findViewById(R.id.button3);
        boton04 = findViewById(R.id.button4);
        total = findViewById(R.id.textView3);
        hasonado = findViewById(R.id.textView4);
        progreso = findViewById(R.id.textView5);
        volumen = findViewById(R.id.seekBar);
        avance = findViewById(R.id.seekBar2);
        player = MediaPlayer.create(this, R.raw.pharrell_williams_happy);
        avance.setMax(player.getDuration());
        volumen.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        volumen .setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,i,0);
                total.setText("El volumen del sistema es: "+i);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        Volumen();
    }//Cierre metodo OnCreate

    public void suenaboton01(View v){sPool.play(sound1,1,1,1,0,1);}
    public void suenaboton02(View v){sPool.play(sound2,1,1,1,0,1);}
    public void suenaboton05(View v){sPool.play(sound3,1,1,1,0,1);}
    public void suenaboton06(View v){sPool.play(sound4,1,1,1,0,1);}

    private Handler mSeekbarUpdateHandler = new Handler();
    private Runnable mUpdateSeekbar = new Runnable() {
        @Override
        public void run() {
           avance.setProgress(player.getCurrentPosition());
            mSeekbarUpdateHandler.postDelayed(this, 50);
        }
    };


    public void suenaboton03(View v){
        if(player != null && player.isPlaying()){
            player.pause();
            mSeekbarUpdateHandler.removeCallbacks(mUpdateSeekbar);
            boton03.setText("Reproducir MediaPlayer");
        }
        else{
            total.setText(String.valueOf(player.getDuration()));
            player.start();
            mSeekbarUpdateHandler.postDelayed(mUpdateSeekbar, 0);
            boton03.setText("Detener MediaPlayer");
        }

    }//Cierre boton03

    public void suenaboton04(View v){
        if(player != null){
            if(player.isPlaying()){
                player.stop();
                mSeekbarUpdateHandler.removeCallbacks(mUpdateSeekbar);
                boton04.setText("Reiniciar MediaPlayer");
                boton03.setText("Reproducir MediaPlayer");
                hasonado.setText(String.valueOf(player.getCurrentPosition()));
            }//if interno
            else{
                player = MediaPlayer.create(this, R.raw.pharrell_williams_happy);
                player.start();
                mSeekbarUpdateHandler.postDelayed(mUpdateSeekbar, 0);
                boton04.setText("Detener MediaPlayer");
                boton03.setText("Pausar MediaPlayer");
            }//else
        }//if externo
        else{
            player.start();
            boton04.setText("Detener MediaPlayer");
        }

    }//Cierre boton04

    @Override
    protected void onDestroy(){
        super.onDestroy();
        sPool.release();
        sPool = null;
        player.release();
        player = null;
    }

    /*@Override
    protected void onResume(){
        super.onResume();
        player.start();
    }

    @Override
    protected void onPause(){
        super.onPause();
        player.pause();
    }*/

    private void Volumen() {
        try {
            volumen = (SeekBar)findViewById(R.id.seekBar);
            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            volumen.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            volumen.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

            volumen.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                    public void onStopTrackingTouch(SeekBar arg0) {}

                @Override
                    public void onStartTrackingTouch(SeekBar arg0) {}
                @Override
                    public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                    total.setText("El volumen del sistema es: "+progress);
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }//Cierre volumen

    private void Avance() {
        try {
            avance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser)
                        player.seekTo(progress);
                        progreso.setText("El progreso de la cancion es: "+progress);
                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {progreso.setText("El progreso de la cancion es: +");}
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {progreso.setText("El progreso de la cancion es: +++");}
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }//Cierre Avance



}//Cierre de la actividad main