package com.example.tamz2_pop_projekt;


import android.graphics.Bitmap;
import android.graphics.Canvas;

public abstract class GameObject {
    protected Bitmap[][] images;

    protected final int rowCount;
    protected final int colCount;

    protected final int[][] widths;
    protected final int[][] heights;

    protected int x;
    protected int y;
    protected int type;

    public GameObject(Bitmap[][] images, int rowCount, int colCount, int x, int y, int type)  {

        this.images = images;
        this.rowCount= rowCount;
        this.colCount= colCount;

        this.x= x;
        this.y= y;
        this.type = type;

        this.widths = new int[rowCount][colCount];
        this.heights = new int[rowCount][colCount];

        for (int i = 0;i<rowCount;i++){
            for (int j = 0;j<colCount;j++ ){
                this.widths[i][j] = images[i][j].getWidth();
                this.heights[i][j] = images[i][j].getHeight();
            }
        }

    }

    public void draw(Canvas canvas) {

    }

    public int getX()  {
        return this.x;
    }

    public int getY()  {
        return this.y;
    }

    public int getType(){
        return this.type;
    }


    public int getHeight(int row, int col) {
        return heights[row][col];
    }

    public int getWidth(int row, int col) {
        return widths[row][col];
    }
}

