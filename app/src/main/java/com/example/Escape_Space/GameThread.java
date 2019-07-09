package com.example.Escape_Space;

import android.annotation.SuppressLint;
import android.graphics.Canvas;

class GameThread extends Thread{

    private GameView view;
    private boolean running = false;

    public GameThread(GameView view)
    {
        this.view = view;
    }

    public void setRunning(boolean run)
    {
        this.running = run;
    }

    @SuppressLint("WrongCall")
    @Override
    public void run() {
        while(running)
        {
            Canvas canvas = null;

            try{
                canvas = view.getHolder().lockCanvas();
                synchronized (view.getHolder()){
                    view.onDraw(canvas);
                }
            }
            finally {
                if(canvas != null)
                {
                    view.getHolder().unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}