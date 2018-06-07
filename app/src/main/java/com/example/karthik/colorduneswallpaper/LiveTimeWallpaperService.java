package com.example.karthik.colorduneswallpaper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.AlarmClock;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by karthik on 6/6/18.
 */

public class LiveTimeWallpaperService extends WallpaperService{

    static boolean numberSign = true,format24 = false,touchEnabled =false;
    static Typeface[] typefaceText = new Typeface[5];
    static int fontIndex = 0,overlayIndex = 0,dividerIndex = 0;
    int xPos = 0,yPos = 0 ;
    static Bitmap overlayBitmap[] = new Bitmap[5];
    Paint alphaPaint;
    SharedPreferences preferences;
    String time = "";

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

            alphaPaint = new Paint();
            alphaPaint.setAlpha(80);

            overlayBitmap[0] = BitmapFactory.decodeResource(getResources(),R.mipmap.dots);
            overlayBitmap[1] = BitmapFactory.decodeResource(getResources(),R.mipmap.honeycomb);
            overlayBitmap[2] = BitmapFactory.decodeResource(getResources(),R.mipmap.square);
            overlayBitmap[3] = BitmapFactory.decodeResource(getResources(),R.mipmap.triangle);
            overlayBitmap[4] = BitmapFactory.decodeResource(getResources(),R.mipmap.teapot);

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

        @Override
        public void onTouchEvent(MotionEvent event){
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                if (touchEnabled) {
                    float x = event.getX();
                    float y = event.getY();

                            Rect rect = new Rect();
                            paint.getTextBounds(time, 0, time.length(), rect);
                            if (rect.contains((int)(x-xPos),(int)(y-yPos))) {
                                Intent openClockIntent = new Intent(AlarmClock.ACTION_SET_ALARM);
                                openClockIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(openClockIntent);
                            }
                        }

                    }
                    super.onTouchEvent(event);
                }



        private void draw() {

            numberSign = preferences.getBoolean("number_sign", true);
            format24 = preferences.getBoolean("24_hr", false);
            fontIndex = Integer.parseInt(preferences.getString("font", "0"));
            paint.setTypeface(typefaceText[fontIndex]);
            overlayIndex = Integer.parseInt(preferences.getString("overlay", "0"));
            dividerIndex = Integer.parseInt(preferences.getString("divider", "0"));
            touchEnabled = preferences.getBoolean("openClock", false);

            calendar = Calendar.getInstance();

            int color = 0;

            if (format24) {
                color = Color.parseColor("#" + ((new SimpleDateFormat("HHmmss")).format(calendar.getTime())));
                if (dividerIndex == 0) {
                    timeFormat = new SimpleDateFormat("HHmmss");
                } else if (dividerIndex == 1) {
                    timeFormat = new SimpleDateFormat("HH.mm.ss");
                } else if (dividerIndex == 2) {
                    timeFormat = new SimpleDateFormat("HH:mm:ss");
                } else if (dividerIndex == 3) {
                    timeFormat = new SimpleDateFormat("HH mm ss");
                } else if (dividerIndex == 4) {
                    timeFormat = new SimpleDateFormat("HH|mm|ss");
                } else if (dividerIndex == 5) {
                    timeFormat = new SimpleDateFormat("HH/mm/ss");
                }
            } else {
                color = Color.parseColor("#" + ((new SimpleDateFormat("hhmmss")).format(calendar.getTime())));
                if (dividerIndex == 0) {
                    timeFormat = new SimpleDateFormat("hhmmss");
                } else if (dividerIndex == 1) {
                    timeFormat = new SimpleDateFormat("hh.mm.ss");
                } else if (dividerIndex == 2) {
                    timeFormat = new SimpleDateFormat("hh:mm:ss");
                } else if (dividerIndex == 3) {
                    timeFormat = new SimpleDateFormat("hh mm ss");
                } else if (dividerIndex == 4) {
                    timeFormat = new SimpleDateFormat("hh|mm|ss");
                } else if (dividerIndex == 5) {
                    timeFormat = new SimpleDateFormat("hh/mm/ss");
                }
            }

            time = "#" + timeFormat.format(calendar.getTime());


            SurfaceHolder holder = getSurfaceHolder();
            Canvas canvas = null;

            try {
                canvas = holder.lockCanvas();
                if (canvas != null) {

                    canvas.drawColor(color);

                    if (overlayIndex > 0) {
                        canvas.drawBitmap(overlayBitmap[overlayIndex - 1], 0, 0, alphaPaint);
                    }

                    if (numberSign) {
                        xPos = (canvas.getWidth() / 2) - (int) (paint.measureText(time) / 2);
                        yPos = (int) ((canvas.getHeight() / 2) - ((paint.ascent() + paint.descent()) / 2));
                        canvas.drawText(time, xPos, yPos, paint);
                    } else {
                        time = time.replaceAll("#", "");
                        xPos = (canvas.getWidth() / 2) - (int) (paint.measureText(time.replaceAll("#", "")) / 2);
                        yPos = (int) ((canvas.getHeight() / 2) - ((paint.ascent() + paint.descent()) / 2));
                        canvas.drawText(time, xPos, yPos, paint);
                    }

                }
            } finally {
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas);
                }
            }

            handler.removeCallbacks(drawRunner);
            if (visible) {
                handler.postDelayed(drawRunner, 1000);
            }
        }

        }

    }




