package com.example.karthik.colorduneswallpaper;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by karthik on 6/6/18.
 */

public class LiveTimeWallpaperService extends WallpaperService{

    static boolean numberSign = true,format24 = false;
    SharedPreferences preferences;
    static Typeface[] typefaceText = new Typeface[5];
    static int fontIndex = 0;

    @Override
    public Engine onCreateEngine(){
        return new WallpaperEngine();
    }

    private class WallpaperEngine extends  Engine{

        Calendar calendar;
        SimpleDateFormat timeFormat;

        private final Handler handler = new Handler();

        private final Runnable drawRunner  = new Runnable() {
            @Override
            public void run() {
                draw();
            }
        };

        private Paint paint = new Paint();
        private  int width;
        private int height;
        private boolean visible = true;



        public WallpaperEngine(){
            preferences = PreferenceManager.getDefaultSharedPreferences(LiveTimeWallpaperService.this);

            numberSign = preferences.getBoolean("number_sign",true);
            format24 = preferences.getBoolean("24_hr",false);
            
            paint.setAntiAlias(true);
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(100);
            paint.setDither(true);

            typefaceText[0] = Typeface.createFromAsset(getAssets(),"Cinzel-Regular.ttf");
            typefaceText[1] = Typeface.createFromAsset(getAssets(),"GloriaHallelujah.ttf");
            typefaceText[2] = Typeface.createFromAsset(getAssets(),"Nexa-Light.otf");
            typefaceText[3] = Typeface.createFromAsset(getAssets(),"Raleway-Light.ttf");
            typefaceText[4] = Typeface.createFromAsset(getAssets(),"Roboto-Light.ttf");

            handler.post(drawRunner);
        }


        @Override
        public void onVisibilityChanged(boolean visible){
            this.visible = visible;
            if(visible){
                handler.post(drawRunner);
            }else {
                handler.removeCallbacks(drawRunner);
            }
        }

        @Override
        public  void onSurfaceDestroyed(SurfaceHolder holder){
            super.onSurfaceDestroyed(holder);
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder,int format,int width,int height){
            this.width = width;
            this.height = height;
            super.onSurfaceChanged(holder,format,width,height);
        }

        private void draw(){

            numberSign = preferences.getBoolean("number_sign",true);
            format24 = preferences.getBoolean("24_hr",false);
            fontIndex = Integer.parseInt(preferences.getString("font","1"));
            paint.setTypeface(typefaceText[fontIndex]);
            if(format24) {
                timeFormat = new SimpleDateFormat("HHmmss");
            }else{timeFormat = new SimpleDateFormat("hhmmss");}
            calendar = Calendar.getInstance();
            String time = "#"+timeFormat.format(calendar.getTime());
            int color = Color.parseColor(time);


            SurfaceHolder holder = getSurfaceHolder();
            Canvas canvas = null;
            try{
                canvas = holder.lockCanvas();
                if(canvas != null){

                    canvas.drawColor(color);
                    int xPos = (canvas.getWidth()/2) - (int)(paint.measureText(time)/2);
                    int yPos = (int)((canvas.getHeight()/2)-((paint.ascent()+paint.descent())/2));
                    if(numberSign) {
                        canvas.drawText(time, xPos, yPos, paint);
                    }else {canvas.drawText(time.replaceAll("#",""), xPos, yPos, paint);}
                }
            }finally {
                if(canvas != null){
                    holder.unlockCanvasAndPost(canvas);
                }
            }

            handler.removeCallbacks(drawRunner);
            if(visible){
                handler.postDelayed(drawRunner,1000);
            }

        }

    }

}
