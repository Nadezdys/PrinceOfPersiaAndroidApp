package com.example.tamz2_pop_projekt;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Collection;

public class MainCharacter extends GameObject {
    private static final int RIGHT = 0;
    private static final int JUMP = 1;
    private static final int STAND = 2;
    private static final int FALL = 3;
    private static final int DEAD = 4;

    private int rowUsing = STAND;

    private int colUsing;

    private int updateCounter;

    private Bitmap[][] images;


    public static final float VELOCITY = 0.1f;

    private int movingVectorX = 2;
    private int movingVectorY = 2;

    private int originX;
    private int originY;

    private long lastDrawNanoTime =-1;

    private GameSurface gameSurface;

    private boolean IsJumping = false;
    private boolean IsFallingFromJump = false;
    private boolean IsFalling = false;
    private boolean IsMoving = false;
    private boolean IsDead = false;
    private int jumpCounter = 0;

    private int groundY;

    public MainCharacter(GameSurface gameSurface, Bitmap[][] images, int x, int y) {
        super(images, 5, 5, x, y, 0);

        this.gameSurface= gameSurface;

        this.images = images;

        this.originX = x;
        this.originY = y;

    }

    public Bitmap[] getMoveBitmaps()  {
        switch (rowUsing)  {
            case RIGHT:
                return  this.images[RIGHT];
            case JUMP:
                return this.images[JUMP];
            case STAND:
                return this.images[STAND];
            case FALL:
                return this.images[FALL];
            case DEAD:
                return this.images[DEAD];
            default:
                return null;
        }
    }

    public Bitmap getCurrentMoveBitmap()  {
        Bitmap[] bitmaps = this.getMoveBitmaps();
        return bitmaps[this.colUsing];
    }


    public void update()  {
        if(IsDead){
            this.rowUsing = DEAD;
        } else if(IsMoving && !IsJumping && !IsFallingFromJump && !IsFalling)  {
            this.rowUsing = RIGHT;
        } else if(IsJumping || IsFallingFromJump) {
            this.rowUsing = JUMP;
        } else if(IsFalling) {
            this.rowUsing = FALL;
        }else{
            this.rowUsing = STAND;
        }

        if (updateCounter > 2) {
            this.colUsing++;
            this.updateCounter = 0;
        }
        else {
            this.updateCounter++;
        }
        if(colUsing >= this.colCount)  {
            this.colUsing =0;
        }
        long now = System.nanoTime();

        if(lastDrawNanoTime==-1) {
            lastDrawNanoTime= now;
        }
        int deltaTime = (int) ((now - lastDrawNanoTime)/ 1000000 );

        float distance = VELOCITY * deltaTime;

            if(IsMoving){
                if(IsJumping){
                    if(jumpCounter < 15){
                        this.x = x +  (int)(distance* movingVectorX );
                        this.y = y -  (int)(distance* movingVectorY );
                        jumpCounter++;
                    }else{
                        jumpCounter = 0;
                        IsJumping = false;
                        IsFallingFromJump = true;
                    }
                }
                else if(IsFallingFromJump){
                    if(this.y <= groundY ){
                        this.x = x +  (int)(distance* movingVectorX );
                        this.y = y +  (int)(distance* movingVectorY );
                    }else{
                        IsFallingFromJump = false;
                    }
                }
                else if(IsFalling){
                    this.y = y +  (int)(distance* movingVectorY );
                }
                else{
                    this.x = x +  (int)(distance* movingVectorX );
                }
            }

         if(this.y > this.gameSurface.getHeight()- 500)  {
                this.x = this.originX;
                this.y = this.originY;
                this.IsFalling = false;
                this.IsFallingFromJump = false;
                this.IsJumping = false;
                this.IsMoving = false;
                jumpCounter = 0;
            }

    }
    public void move()  {
        this.IsMoving = !this.IsMoving;
    }

    public void jump()  {
        this.IsJumping = true;
        this.IsFallingFromJump = false;
        this.jumpCounter = 0;
        this.groundY = this.y;
        //this.IsMoving = false;
        //this.rowUsing = JUMPR;

    }
    public void draw(Canvas canvas)  {
        Bitmap bitmap = this.getCurrentMoveBitmap();
        canvas.drawBitmap(bitmap,x, y, null);
        // Last draw time.
        this.lastDrawNanoTime= System.nanoTime();
    }

    public void IsOnGround(Collection<GameObject> objects){
        for(GameObject b : objects){
            if(b.getType() == 1){
                if(this.x >= b.x && this.x <= b.x + b.getWidth(0,0)){
                    if(b.y > this.y + getCurrentMoveBitmap().getHeight() + 3){
                        this.IsFalling = true;
                    }
                    else{
                        this.IsFalling = false;
                    }
                    return;
                }
                this.IsFalling = true;
            }
        }
    }

    public void CheckCollision(Collection<GameObject> objects){
        for(GameObject b : objects){
            if(b.getType() == 2){
                if(this.x + getCurrentMoveBitmap().getWidth() >= b.x && this.x < b.x + b.getWidth(0,0)){
                    if(this.y + 3 < b.y + b.getHeight(0,0) && b.y + b.getHeight(0,0) <= this.y - 3 + getCurrentMoveBitmap().getHeight()){
                        this.IsMoving = false;
                    }

                }
            }
            else if(b.getType() == 3){
                if(this.x + getCurrentMoveBitmap().getWidth() >= b.x && this.x < b.x + b.getWidth(0,0)){
                    if(this.y + 3 < b.y + b.getHeight(0,0) && b.y + b.getHeight(0,0) <= this.y - 3 + getCurrentMoveBitmap().getHeight()){
                        this.x = this.originX;
                        this.y = this.originY;
                        this.IsFalling = false;
                        this.IsFallingFromJump = false;
                        this.IsJumping = false;
                        this.IsMoving = false;
                        jumpCounter = 0;
                    }
                }
            }
        }
    }

    public boolean IsOnScreen(){
        if(this.x > this.gameSurface.getWidth() - getWidth(rowUsing, colUsing))  {
            return false;
        }else {
            return true;
        }
    }

    public boolean IsEnd(Gate gate){
        if(this.x > gate.getX() + 30 )  {
            return true;
        }else {
            return false;
        }
    }


    public void setMovingVector(int movingVectorX, int movingVectorY)  {
        this.movingVectorX= movingVectorX;
        this.movingVectorY = movingVectorY;
    }
}

