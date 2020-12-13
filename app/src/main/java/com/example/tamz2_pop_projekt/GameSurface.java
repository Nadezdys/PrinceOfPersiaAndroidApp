package com.example.tamz2_pop_projekt;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.ArrayList;
import java.util.List;

public class GameSurface extends SurfaceView implements SurfaceHolder.Callback {
    private GameThread gameThread;
    private List<MainCharacter> character;
    private List<List<GameObject>> objects;
    private Gate gate;
    private LevelBuilder lvlBuilder;
    private Bitmap scaled;

    private int screen = 0;

    private int level;
    private Context context;

    private String[] levels;

    public GameSurface(Context context, int lvl)  {
        super(context);
        this.context = context;

        this.setFocusable(true);

        this.getHolder().addCallback(this);

        level = lvl;

        levels = new String[20];
        levels[0] = "level1.xml";
        levels[1] = "level2.xml";
    }

    public void update()  {
        if(this.character.get(this.screen).IsEnd(this.gate) && this.screen == 2)
            ((Activity)this.context).finish();
        if(!this.character.get(this.screen).IsOnScreen())
            this.screen++;

        this.character.get(this.screen).IsOnGround(this.objects.get(screen));
        this.character.get(this.screen).CheckCollision(this.objects.get(this.screen));
        this.character.get(this.screen).update();
    }

    @Override
    public void draw(Canvas canvas)  {
        super.draw(canvas);

        canvas.drawBitmap(scaled, 0, 0, null);

        this.character.get(this.screen).draw(canvas);

        for(GameObject b : this.objects.get(this.screen)){
            b.draw(canvas);
        }
        if(screen == 2 && gate != null)
            this.gate.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if(event.getY() > 700){
                this.character.get(this.screen).move();
                return true;

            }else if(event.getY() < 700){
                this.character.get(this.screen).jump();
                return true;
            }
        }
        return false;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        float scale = (float) background.getHeight() / (float) getHeight();
        int newWidth = Math.round(background.getWidth() / scale);
        int newHeight = Math.round(background.getHeight() / scale);
        scaled = Bitmap.createScaledBitmap(background, newWidth, newHeight, true);

        Bitmap[][] charBitmaps = new Bitmap[5][5];

        charBitmaps[0][0] = BitmapFactory.decodeResource(this.getResources(), R.drawable.right1);
        charBitmaps[0][1] = BitmapFactory.decodeResource(this.getResources(), R.drawable.right2);
        charBitmaps[0][2] = BitmapFactory.decodeResource(this.getResources(), R.drawable.right3);
        charBitmaps[0][3] = BitmapFactory.decodeResource(this.getResources(), R.drawable.right4);
        charBitmaps[0][4] = BitmapFactory.decodeResource(this.getResources(), R.drawable.right5);

        charBitmaps[1][0] = BitmapFactory.decodeResource(this.getResources(), R.drawable.jumpright);
        charBitmaps[1][1] = BitmapFactory.decodeResource(this.getResources(), R.drawable.jumpright);
        charBitmaps[1][2] = BitmapFactory.decodeResource(this.getResources(), R.drawable.jumpright);
        charBitmaps[1][3] = BitmapFactory.decodeResource(this.getResources(), R.drawable.jumpright);
        charBitmaps[1][4] = BitmapFactory.decodeResource(this.getResources(), R.drawable.jumpright);

        charBitmaps[2][0] = BitmapFactory.decodeResource(this.getResources(), R.drawable.stand);
        charBitmaps[2][1] = BitmapFactory.decodeResource(this.getResources(), R.drawable.stand);
        charBitmaps[2][2] = BitmapFactory.decodeResource(this.getResources(), R.drawable.stand);
        charBitmaps[2][3] = BitmapFactory.decodeResource(this.getResources(), R.drawable.stand);
        charBitmaps[2][4] = BitmapFactory.decodeResource(this.getResources(), R.drawable.stand);

        charBitmaps[3][0] = BitmapFactory.decodeResource(this.getResources(), R.drawable.fall);
        charBitmaps[3][1] = BitmapFactory.decodeResource(this.getResources(), R.drawable.fall);
        charBitmaps[3][2] = BitmapFactory.decodeResource(this.getResources(), R.drawable.fall);
        charBitmaps[3][3] = BitmapFactory.decodeResource(this.getResources(), R.drawable.fall);
        charBitmaps[3][4] = BitmapFactory.decodeResource(this.getResources(), R.drawable.fall);

        charBitmaps[4][0] = BitmapFactory.decodeResource(this.getResources(), R.drawable.death);
        charBitmaps[4][1] = BitmapFactory.decodeResource(this.getResources(), R.drawable.death);
        charBitmaps[4][2] = BitmapFactory.decodeResource(this.getResources(), R.drawable.death);
        charBitmaps[4][3] = BitmapFactory.decodeResource(this.getResources(), R.drawable.death);
        charBitmaps[4][4] = BitmapFactory.decodeResource(this.getResources(), R.drawable.death);

        Bitmap[][] blockBitmap = new Bitmap[1][1];
        blockBitmap[0][0] = BitmapFactory.decodeResource(this.getResources(), R.drawable.blocksprite);


        Bitmap[][] spikeBitmap = new Bitmap[1][1];
        spikeBitmap[0][0] = BitmapFactory.decodeResource(this.getResources(), R.drawable.spike);

        Bitmap[][] gateBitmap = new Bitmap[1][1];
        gateBitmap[0][0] = BitmapFactory.decodeResource(this.getResources(), R.drawable.gate);

        this.lvlBuilder = new LevelBuilder(this.context, blockBitmap,spikeBitmap,charBitmaps,gateBitmap, this);

        this.objects = new ArrayList<>();
        this.objects = this.lvlBuilder.XmlParseLevel(this.levels[level-1]);

        this.character = new ArrayList<MainCharacter>();
        this.character = this.lvlBuilder.getCharacter();

        this.gate = this.lvlBuilder.getGate();

        this.gameThread = new GameThread(this,holder);
        this.gameThread.setRunning(true);
        this.gameThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry= true;
        while(retry) {
            try {
                this.gameThread.setRunning(false);

                this.gameThread.join();
            }catch(InterruptedException e)  {
                e.printStackTrace();
            }
            retry= true;
        }
    }

    public void sleep() throws InterruptedException {
        this.gameThread.sleep(1000);
    }

}

