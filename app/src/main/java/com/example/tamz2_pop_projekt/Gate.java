package com.example.tamz2_pop_projekt;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Gate extends GameObject{
    private Bitmap block;

    private GameSurface gameSurface;

    public Gate(GameSurface gameSurface, Bitmap[][] images, int x, int y) {
        super(images, 1, 1, x, y, 0);

        this.block = images[0][0];

        this.gameSurface = gameSurface;

    }

    public Bitmap getBitmap()  {
        return this.block;
    }

    public void draw(Canvas canvas)  {
        Bitmap bitmap = this.getBitmap();
        canvas.drawBitmap(bitmap,x, y, null);
    }
}


