package com.example.karthik.colorduneswallpaper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by karthik on 6/6/18.
 */

public class LiveTimeWallpaperService extends WallpaperService{

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
            paint.setAntiAlias(true);
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(100);
            paint.setDither(true);
            timeFormat = new SimpleDateFormat("hhmmss");
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
                    canvas.drawText(time,xPos,yPos,paint);
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
