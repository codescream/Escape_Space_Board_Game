package com.example.Escape_Space;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.io.InputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static com.example.Escape_Space.MainActivity.assetsData;
import static com.example.Escape_Space.MainActivity.destDataLevel2;
import static com.example.Escape_Space.MainActivity.destroyDataBase;
import static com.example.Escape_Space.MainActivity.heartData;
import static com.example.Escape_Space.MainActivity.lifeData;
import static com.example.Escape_Space.MainActivity.newGame;
import static com.example.Escape_Space.MainActivity.opjPenalty;
import static com.example.Escape_Space.MainActivity.opjPos;
import static com.example.Escape_Space.MainActivity.opjType;
import static com.example.Escape_Space.MainActivity.plPos;
import static com.example.Escape_Space.MainActivity.retLevel;
import static com.example.Escape_Space.MainActivity.shieldData;
import static com.example.Escape_Space.MainActivity.speed;

class GameView extends SurfaceView {

    private Bitmap logo;
    InputStream is;
    Movie movie;
    InputStream isII;
    Movie congrats;
    InputStream isIII;
    Movie gamelost;
    long moviestart;
    private int[][] penaltyrange;
    private int[] background;
    private Bitmap player;
    private Bitmap blackhole;
    private Bitmap blob;
    private Bitmap pod;
    private Bitmap portal;
    private Bitmap spider;
    private Bitmap heart;
    private Bitmap life;
    private Bitmap shield;
    private Bitmap button;
    private Bitmap restartBtn;
    private SurfaceHolder holder;
    public static GameThread gameThread;
    private Bitmap mFinalbitmap;
    public static int maxlevel = 2;
    private float xcord;
    private int bwidth;
    private float ycord;
    private int bheight;
    private boolean drawbkg = true;
    private boolean bounce = false;
    private boolean bouncedone = false;
    private boolean podspeed = false;
    private boolean podcollide = false;
    private boolean newlevel = false;
    private int penaltyindex = 0;
    private int bounceCount = 0;
    private int[][] board;
    private float[][][] objPos = new float[maxlevel][][];
    //    private Context context;
    private Activity activity;


    //character position info
    private float x1 = 20;
    private float  y1;
    public static int xspeed;//*************************************************
    private int xspeedtemp;
    private int yspeed = 0;
    private float xinterim;
    private float yinterim;
    private float ybubble;
    private int resizedPW = 0;
    private int resizedPH = 0;
    private int spritewidth = 0;
    private int spriteheight = 0;
    private int diff = -5;
    private int roll;
    private int[] numEnemy = new int[]{6, 9}; // enemies and pods
    private int[] enemyrowpos;
    private int row;
    private int col;
    private int count = 0;
    private int counter = 1;
    private int reverse = 9;
    private int columncheck = 0;
    private int heartcount = 3; // starts with 3 hearts
    private int lifecount = 1;
    private int shieldcount = 0;
    private int step;
    private int ystep;
    private Bitmap gameObjs[][];
    public static int gameObjsType[][]; //******************************Osama
    private int[] rowlayout;
    private int[][] randcells;
    private int stepcheck = 0;
    private String[][] gameitems;
    private int[][] boardPos = new int[maxlevel][];
    public  static int[][] penalty = new int[maxlevel][];//******************************Osama
    private List<Integer> arrayList = new ArrayList<>();
    private List<Integer> arrayNums = new ArrayList<>();
    public static List<Integer> assets = new ArrayList<>();//*******************************
    public static int[][] uniquenums = new int[maxlevel][];//******************************Osama
    public static int playerPos = 1; // initialize player position//******************************Osama
    int width;
    int height;
    private boolean newEnd = true;
    private boolean collided = false;
    private boolean temp = false;
    private boolean show = true;
    private boolean rolling = true;
    private boolean gameEnd = false;
    private int rollcount = 0;
    private int backwardcount = 0;
    public static int level = 0;//*******************************osama

    //stats
    private Canvas statCanvas;
    private Bitmap statHeart;
    private int heartLeft = 762;
    private List<Bitmap> healthCountImg = new ArrayList<Bitmap>();
    private Bitmap emptyHeart;
    private  int emptyHeartCount = 3;
    private int emptyHeartLeft = 760;
    private List<Bitmap> emptyCountImg = new ArrayList<Bitmap>();
    public static int healthCount = 3;//*******************************osama
    private int healthMax = 3;
    private Bitmap statShield;
    private int shieldLeft = 1450;
    private List<Bitmap> shieldCountImg = new ArrayList<Bitmap>();
    public static int shieldCount = 0;//*******************************osama
    private int shieldMax = 3;
    private Bitmap statLife;
    private int lifeLeft = 200;
    private List<Bitmap> lifeCountImg = new ArrayList<Bitmap>();
    public static int lifeCount = 1;//*******************************osama
    private int lifeMax = 3;
    private boolean showCongratulations = false;
    private boolean gameOver = false;

    public GameView(Context context, Activity activity) {
        super(context);
        gameThread = new GameThread(this);

        this.activity = activity;

        background = new int[]{R.drawable.moon, R.drawable.mars};

        /*******************************************************/
        logo = BitmapFactory.decodeResource(getResources(), R.drawable.escape_space);
        emptyHeart = BitmapFactory.decodeResource(getResources(), R.drawable.empty_heart);
        /*****************************************************/

        is = context.getResources().openRawResource(+ R.drawable.dicerolling);
        movie = Movie.decodeStream(is);
        isII = context.getResources().openRawResource(+ R.drawable.gameend3);
        congrats = Movie.decodeStream(isII);
        isIII = context.getResources().openRawResource(+ R.drawable.gameover1);
        gamelost = Movie.decodeStream(isIII);
        player = BitmapFactory.decodeResource(getResources(), R.drawable.player);
//        playercopy = player.copy(Bitmap.Config.ARGB_8888, true); make bitmap mutable
        blackhole = BitmapFactory.decodeResource(getResources(), R.drawable.blackhole);
        blob = BitmapFactory.decodeResource(getResources(), R.drawable.blob);
        pod = BitmapFactory.decodeResource(getResources(), R.drawable.pod);
        portal = BitmapFactory.decodeResource(getResources(), R.drawable.portal);
        spider = BitmapFactory.decodeResource(getResources(), R.drawable.spider);
        button = BitmapFactory.decodeResource(getResources(), R.drawable.rolldicebtn2);
        heart = BitmapFactory.decodeResource(getResources(), R.drawable.heart);
        life = BitmapFactory.decodeResource(getResources(), R.drawable.head);
        shield = BitmapFactory.decodeResource(getResources(), R.drawable.shield);
        restartBtn = BitmapFactory.decodeResource(getResources(), R.drawable.restart_button_mod);

//        this.context = context;

        pod = Bitmap.createScaledBitmap(pod, 2*pod.getWidth()/3, 3*pod.getHeight()/4, true);
        heart = Bitmap.createScaledBitmap(heart, heart.getWidth(), heart.getHeight(), true);
        life = Bitmap.createScaledBitmap(life, life.getWidth(), life.getHeight(), true);
        shield = Bitmap.createScaledBitmap(shield, shield.getWidth(), shield.getHeight(), true);


        /***************************************************************************************/
        //stats

        statHeart = Bitmap.createScaledBitmap(heart, heart.getWidth()/3, heart.getHeight()/3, true);
        emptyHeart = Bitmap.createScaledBitmap(emptyHeart, emptyHeart.getWidth()/3, emptyHeart.getHeight()/3,true);
        statShield = Bitmap.createScaledBitmap(shield, shield.getWidth()/2, shield.getHeight()/2, true);
        statLife = Bitmap.createScaledBitmap(life, life.getWidth()/2, life.getHeight()/2, true);

        if(newGame == false){//**********************************************************************
            healthCount = heartData;
            shieldCount = shieldData;
            lifeCount = lifeData;
        }


        /************************************************************************************/



        rowlayout = new int[]{5, 4, 3, 2, 1};

        enemyrowpos = createUniqueRandomNumbers(2, 5, 4);
        arrayList.clear();
        gameObjs = new Bitmap[][]{{blob, blob, spider, spider, spider, pod, shield, portal},
                                    {blob, blob, blob, spider, spider, spider, spider, pod, pod, heart, shield, portal}};
        penaltyrange = new int[][]{{6, 3, 6, 3, 6, 6, 1},
                                    {6, 3, 6, 4, 6, 5, 4, 6, 6, 1}}; // max spaces back or forward
        gameitems = new String[][]{{"blob1", "blob2", "spider", "spider", "spider", "pod", "shield", "portal"},
                {"blob1", "blob2", "blob3", "spider", "spider", "spider", "spider", "pod", "pod", "heart", "shield", "portal"}};
        gameObjsType = new int[][]{{1, 1, 2, 2, 2, 3, 6, 7},
                                    {1,1,1,2,2,2,2,3,3,4,6,7}}; ////////////////////////////////////////////////////////// add new aliens
        for(int i = 0; i < 2; i++)
        {
            objPos[i] = new float[gameObjs[i].length][2];
            uniquenums[i] = new int[gameObjs[i].length];
            penalty[i] = new int[gameObjs[i].length];
            boardPos[i] = new int[gameObjs[i].length];
        }

        restartGame();

        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        width = dm.widthPixels;
        height = dm.heightPixels;

        //board number layout
        board = new int[5][10];

        for(int i = board.length - 1; i >= 0; i--) // number game board 1 - 50
        {
            for(int j = 0; j < board[i].length; j++)
            {
                if(i % 2 == 0)
                {
                    board[i][j] = counter++;
                }
                else
                {
                    int subindex = reverse - j;
                    board[i][subindex] = counter++;
                }
            }
        }

        spriteheight = player.getHeight();
        spritewidth = player.getWidth();

        resizedPW = spritewidth;
        resizedPH = spriteheight;

        holder = getHolder();

        holder.addCallback(new SurfaceHolder.Callback() {
            @SuppressLint("WrongCall")
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                gameThread.setRunning(true);
                gameThread.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                boolean retry = true;
                gameThread.setRunning(false);

                while (retry)
                {
                    try {
                        gameThread.join();
                        retry = false;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private int rolldice() {
        show =  true;
        Random r = new Random();
        final int rolled = r.nextInt(7 - 1) + 1;
        playerPos += rolled;

        activity.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(activity, "you rolled a " + rolled, Toast.LENGTH_SHORT).show();
            }
        });
        rolling =  false;
        return rolled;
    }

    @Override
    public void onDraw(Canvas canvas) {
        /////////////////////////////////////////////////////////// RETRIVE DTAT ////////////////////////////////////////////
        if(newGame) {//*****************************osama
            //hi
        }
        else {
            level = retLevel;
        }//*******************************end

        if(gameOver)
        {
            player = BitmapFactory.decodeResource(getResources(), R.drawable.dead);
            player = Bitmap.createScaledBitmap(player, player.getWidth()/2, player.getHeight()/2, true);
        }
        else if(xspeed < 0) // controls player direction
            player = BitmapFactory.decodeResource(getResources(), R.drawable.player_left);
        else
            player = BitmapFactory.decodeResource(getResources(), R.drawable.player);

        statCanvas = canvas;
        if(canvas != null)
        {
            if(drawbkg){
                gameEnd = false;
                if(newlevel)
                {
                     level++;
                     roll = 0;
                     count = 0;
                     playerPos = 1;
                     newlevel = false;
                }
                else
                {
                    level = 0;
                }
                player = BitmapFactory.decodeResource(getResources(), R.drawable.player);
                player = Bitmap.createScaledBitmap(player, spritewidth, spriteheight, true);
                mFinalbitmap = BitmapFactory.decodeResource(getResources(), background[level]);

                resizedPW = spritewidth;
                resizedPH = spriteheight;

                mFinalbitmap = resizeImage(mFinalbitmap, canvas.getWidth(), canvas.getHeight());
                step = Math.round(mFinalbitmap.getWidth()/10);
                xspeed = step/50;
                y1 = mFinalbitmap.getHeight() - spriteheight - 15; // 15 is arbitrary offset. I guess spriteheight alone should do okay, places obj right at bottom.
                yinterim = y1;
                x1 = 20;
                yspeed = Math.round((mFinalbitmap.getHeight())/5);
                ystep = yspeed;

                logo = Bitmap.createScaledBitmap(logo, step/2, step/2,true);

                drawbkg = false;

                lifeLeft = 2*step;
                heartLeft = 9*step/2;
                emptyHeartLeft = 9*step/2;
                shieldLeft = 8*step;
            }
            /////////////////////////////////////////////////////////// RETRIVE DTAT ////////////////////////////////////////////
            if(newGame) {
                //hi
            }
            else {
                int[][] opType = opjType;
                xspeed = speed;
                randcells = opjPos;
                healthCount = heartData;
                shieldCount = shieldData;
                lifeCount = lifeData;
                int[][] opPan = opjPenalty;
                playerPos = plPos;
                level = retLevel;
                assets = assetsData;

                columncheck = 1;
                row = playerPos / 10;
                col = playerPos % 10;

                if(col != 0) {
                    columncheck++;
                }
                else
                {
                    if(row % 2 == 0)
                    {
                        col = 1;
                    }
                    else
                    {
                        col = 10;
                    }
                    row--;
                }

                if(row % 2 != 0 && columncheck % 2 == 0)
                {
                    col = 11 - col;
                }

                x1 = (col*step - step) + step/2 - player.getWidth()/2;

                yinterim = (rowlayout[row]*ystep - ystep) + ystep/2 - player.getHeight()/2;
                y1 = yinterim;

                xcord = mFinalbitmap.getWidth()/2 - button.getWidth()/2;
                ycord = mFinalbitmap.getHeight()/2 - button.getHeight()/2;
                newGame = true;
            }

            //check border collision left/right
            if((x1 < 0 || (x1 + spritewidth) > mFinalbitmap.getWidth()) && newEnd) // (x1 < spritewidth/2 || (x1 + 2*spritewidth)
            {
                xspeed *= -1;
                if(newEnd)
                {
                    if(collided)
                    {
//                    xspeed *= -1;
                        y1 += yspeed;
                        diff = 5;
                    }
                    else
                    {
                        y1 -= yspeed;
                        diff = -5;
                    }

                    if(xspeed < 0) // ensures player obj is centered as it moves up
                        x1 = x1 - 30;
                    else
                        x1 = x1 + 30;

                    newEnd = false;
                    if(xspeed < 0)
                        player = BitmapFactory.decodeResource(getResources(), R.drawable.player_left);
                    else
                        player = BitmapFactory.decodeResource(getResources(), R.drawable.player);
                }

                if(y1 < 0 || y1+spriteheight > mFinalbitmap.getHeight())
                {
                    yspeed *= -1;
                    if(y1 < 0)
                        diff = 5;

                    if(y1+spriteheight > mFinalbitmap.getHeight())
                        diff = -5;

                    y1 -= yspeed;
                }
            }
////////////////////////////////////////////////////////////////////////

            //check border collision left/right
            if((x1 < 0 || (x1 + spritewidth) > mFinalbitmap.getWidth()) && newEnd) // (x1 < spritewidth/2 || (x1 + 2*spritewidth)
            {
                xspeed *= -1;
                if(newEnd)
                {
                    if(collided)
                    {
//                    xspeed *= -1;
                        y1 += yspeed;
                        diff = 5;
                    }
                    else
                    {
                        y1 -= yspeed;
                        diff = -5;
                    }

                    if(xspeed < 0) // ensures player obj is centered as it moves up
                        x1 = x1 - 30;
                    else
                        x1 = x1 + 30;

                    newEnd = false;
                }

                if(y1 < 0 || y1+spriteheight > mFinalbitmap.getHeight())
                {
                    yspeed *= -1;
                    if(y1 < 0)
                        diff = 5;

                    if(y1+spriteheight > mFinalbitmap.getHeight())
                        diff = -5;

                    y1 -= yspeed;
                }
            }

            if(!rolling)
            {
                if(!bounce)
                {
                    if(count != roll)
                    {
                        if(yinterim != y1)
                        {
                            yinterim += diff;

                            if(diff > 0)
                            {
                                if(yinterim >= y1)
                                {
                                    yinterim = y1;
                                    if(collided)
                                    {
                                        backwardcount++;
                                    }
                                    count++;
                                    stepcheck = 0;
                                }
                            }
                            else
                            {
                                if(yinterim <= y1)
                                {
                                    yinterim = y1;
                                    if(collided)
                                    {
                                        backwardcount++;
                                    }
                                    count++;
                                    stepcheck = 0;
                                }
                            }

                            if(collided && count == roll)
                            {
                                xspeed = -xspeed/2;
//                                temp = true;
                                collided = false;
                                backwardcount = 0;
                            }

                        }
                        else
                        {
                            x1 += xspeed;
                            newEnd = true;
                            stepcheck += (xspeed < 0) ? (-xspeed) : xspeed;
                            if(stepcheck >= step)
                            {
                                count++;
                                stepcheck = 0;
                                if((x1 + step) > mFinalbitmap.getWidth() && y1 < spriteheight) // detects when player get to the end of the board i.e 50
                                {
                                    player = BitmapFactory.decodeResource(getResources(), R.drawable.player);
                                    count = roll;
                                    activity.runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast.makeText(activity, "congratulations!!! you have escaped Mars!", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    xinterim = x1;

                                }

                                if(collided)
                                {
                                    backwardcount++;
                                    x1 = x1 - 2*xspeed/Math.abs(xspeed); // this makes up for the 2X xspeed deduction
                                    if(backwardcount == roll)
                                    {
                                        xspeed *= -1;

                                        backwardcount = 0;
                                        collided = false;

                                        xspeed = xspeed/2;
                                    }
                                }
                            }
                        }
                    }
                    else if((x1 + step) > mFinalbitmap.getWidth() && y1 < spriteheight)
                    {
                        // code for when player reaches the end of the game - block 50

                        if(resizedPH <= 1 || resizedPW <= 1)
                        {
                            resizedPH = 1;
                            resizedPW = 1;
                            destDataLevel2();////////////////////////
                            gameEnd = true;
                            newGame = true;////////////////////////////////////////////////////////////////////////
                            randcells = uniquenums;////////////////////////////////////////
                            /* reset game and set next level*/

                            if(level != maxlevel - 1)
                            {
                                drawbkg = true;
                                newlevel = true;
                                playerPos = 1;
                                assets.clear();
                            }
                            else
                            {
                                // congratulatory gif
                                showCongratulations = true;
                            }
                        }
                        else
                        {
                            resizedPW -= 1;
                            resizedPH -= 1;

                            if(x1 < xinterim + (float)step/6)
                            {
                                x1 += (float)step/157;
                            }
                            else
                            {
                                x1 += (float)step/900;
                            }
                        }
                        yinterim += 0.30;
                        player = Bitmap.createScaledBitmap(player, resizedPW, resizedPH, true); // code to make player disappear on reaching portal
                    }
                    else
                    {
                        if(podspeed)
                        {
                            xspeed = xspeed/2;
                            podspeed = false;
                        }
                        count = 0;

                        String objColl = null; // variable to check if player has collided with other board obj
                        try{
                            for(int i = 0; i < boardPos[level].length; i++)
                            {
                                final String obj;
                                if(playerPos == boardPos[level][i])
                                {
                                    if(!assets.contains(i)) // prevents assets from being collided into after being picked up
                                    {
                                        penaltyindex = i;
                                        obj = gameitems[level][i];
                                        objColl = obj;
                                        activity.runOnUiThread(new Runnable() {
                                            public void run() {
                                                Toast.makeText(activity, "player collided with " + obj, Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        ybubble = yinterim;
                                        bouncedone = false;
                                        bounce = true;

                                        switch (obj)
                                        {
                                            case "heart":
                                                if(!assets.contains(i))
                                                {
                                                    if(healthCountImg.size() != healthMax)
                                                    {
                                                        assets.add(i);
                                                        healthCount++;
                                                        healthCountImg.add(statHeart);
                                                    }
                                                    else
                                                    {
                                                        if(lifeCountImg.size() != lifeMax)
                                                        {
                                                            assets.add(i);
                                                            lifeCount++;
                                                            lifeCountImg.add(statLife);
                                                            healthCountImg.clear();
                                                            healthCountImg.add(statHeart);
                                                            healthCount = 1;
                                                        }
                                                    }
                                                }
                                                break;
                                            case "life":
                                                if(!assets.contains(i))
                                                {
                                                    if(lifeCountImg.size() != lifeMax)
                                                    {
                                                        assets.add(i);
                                                        lifeCount++;
                                                        lifeCountImg.add(statLife);
                                                    }
                                                }
                                                break;
                                            case "shield":
                                                if(!assets.contains(i))
                                                {
                                                    if(shieldCountImg.size() != shieldMax)
                                                    {
                                                        assets.add(i);
                                                        shieldCount++;
                                                        shieldCountImg.add(statShield);
                                                    }
                                                }
                                                break;
                                        }

                                    }
                                }
                            }
                        }

                        finally {
                            if(objColl == null)
                            {
                                roll = 0;
                                rolling = true;
                                show = true;
                            }
                        }
                    }
                }
                else
                {
                    // put code to create bounce
                    if(!bouncedone)
                    {
                        if(yinterim >= ybubble - 15 && bounceCount == 0) // bubbles player up
                        {
                            yinterim -= 1;
                        }
                        else
                        {
                            if(yinterim <= ybubble) // bubbles player back down
                            {
                                yinterim += 1;
                                bounceCount++;
                            }
                            else
                            {
                                yinterim = ybubble; // sets back initial value of yinterim
                                bounce = false;
                                bounceCount = 0;
                                bouncedone = true;
;
                                applyPenalty();
                            }
                        }
                    }
                }
            }
        }


        if(canvas != null)
        {
            canvas.drawColor(Color.LTGRAY);
            canvas.drawBitmap(mFinalbitmap, 0, 10*(float)canvas.getHeight()/100, null);


            /*****************************************************************************/

            canvas.drawBitmap(logo, 20, 10,null);
            for(int i = 0; i < lifeCountImg.size(); i++)
            {
                canvas.drawBitmap(lifeCountImg.get(i), lifeLeft,20,null);
                lifeLeft += step/2;
                if(i == lifeCountImg.size() -1)
                {
                    lifeLeft = 2*step;
                }
            }
            for(int i = 0; i < emptyCountImg.size(); i++)
            {
                canvas.drawBitmap(emptyHeart, emptyHeartLeft,28,null);
                emptyHeartLeft += step/2;
                if(i == emptyCountImg.size() -1)
                {
                    emptyHeartLeft = 9*step/2;
                }
            }
            for(int i = 0; i < healthCountImg.size(); i++)
            {
                canvas.drawBitmap(statHeart, heartLeft,28,null);
                heartLeft += step/2;
                if(i == healthCountImg.size() -1)
                {
                    heartLeft = 9*step/2;
                }
            }
            for(int i = 0; i < shieldCountImg.size(); i++)
            {
                canvas.drawBitmap(statShield,shieldLeft, 20, null);
                shieldLeft += step/2;
                if(i == shieldCountImg.size() -1)
                {
                    shieldLeft = 8*step;
                }
            }

            /****************************************************************************/

            for(int i = 0; i < randcells[level].length; i++) // randomly positioning objects
            {
                columncheck = 1;
                row = randcells[level][i] / 10;
                col = randcells[level][i] % 10;

                if(col != 0) {
                    columncheck++;
                }
                else
                {
                    if(row % 2 == 0)
                    {
                        col = 1;
                    }
                    else
                    {
                        col = 10;
                    }
                    row--;
                }

                if(row % 2 != 0 && columncheck % 2 == 0)
                {
                    col = 11 - col;
                }

                if(gameitems[level][i] != "portal")
                {
                    objPos[level][i][0] = (col*step - step) + step/2 - gameObjs[level][i].getWidth()/2;
                    if(gameitems[level][i] == "pod")
                    {
                        objPos[level][i][1] = (rowlayout[row]*ystep - ystep) + ystep/2 - 2*gameObjs[level][i].getHeight()/3;
                    }
                    else
                    {
                        objPos[level][i][1] = (rowlayout[row]*ystep - ystep) + ystep/2 - gameObjs[level][i].getHeight()/2;
                    }
                    boardPos[level][i] = randcells[level][i];
                }
                else // fixes the portal obj to the last block - i.e 50
                {
                    boardPos[level][i] = 50;
                    objPos[level][i][0] = (10*step - step) + step/2 - gameObjs[level][i].getWidth()/2;
                    objPos[level][i][1] = (1*ystep - ystep) + ystep/2 - gameObjs[level][i].getHeight()/2;
                }
                if(!assets.contains(i))
                {
                    canvas.drawBitmap(gameObjs[level][i], objPos[level][i][0], objPos[level][i][1] + 11*(float)canvas.getHeight()/100, null);
                }
            }

            canvas.drawBitmap(player, x1, yinterim + 10*(float)canvas.getHeight()/100, null);

            if(!show)
            {
                long now = android.os.SystemClock.uptimeMillis();

                if (moviestart == 0) { // first time
                    moviestart = now;
                }

                int relTime = (int)((now - moviestart) % movie.duration());

                movie.setTime(relTime);

                movie.draw(canvas,mFinalbitmap.getWidth()/2 - step/2,mFinalbitmap.getHeight()/2);

                rollcount++;
                if(rollcount == 100) // show dice rolling animation for 100 game frames
                {
                    roll = rolldice();
                    rollcount = 0;
                }
            }
            else if(gameOver)
            {
                long now = android.os.SystemClock.uptimeMillis();

                if (moviestart == 0) { // first time
                    moviestart = now;
                }


                int relTime = (int)((now - moviestart) % gamelost.duration());

                gamelost.setTime(relTime);

                gamelost.draw(canvas,21*step/8,mFinalbitmap.getHeight()/2 - 3*step/2);

                bwidth =  mFinalbitmap.getHeight()/4;
                bheight = mFinalbitmap.getHeight()/4;
                restartBtn = Bitmap.createScaledBitmap(restartBtn, bwidth, bheight, true);
                xcord = mFinalbitmap.getWidth()/2 - button.getWidth()/2;
                ycord = mFinalbitmap.getHeight()/2 - button.getHeight()/16;

                canvas.drawBitmap(restartBtn, xcord, ycord, null);
            }
            else if(rolling)
            {
                bwidth =  mFinalbitmap.getHeight()/4;
                bheight = mFinalbitmap.getHeight()/4;
                button = Bitmap.createScaledBitmap(button, bwidth, bheight, true);
                xcord = mFinalbitmap.getWidth()/2 - button.getWidth()/2;
                ycord = mFinalbitmap.getHeight()/2 - button.getHeight()/16;
                canvas.drawBitmap(button, xcord, ycord, null);
                gameEnd = false;
            }
            else if(showCongratulations)
            {
                long now = android.os.SystemClock.uptimeMillis();

                if (moviestart == 0) { // first time
                    moviestart = now;
                }


                int relTime = (int)((now - moviestart) % congrats.duration());

                congrats.setTime(relTime);

                congrats.draw(canvas,mFinalbitmap.getWidth()/2 - 21*step/10,mFinalbitmap.getHeight()/2 - 3*step/2);

                bwidth =  mFinalbitmap.getHeight()/4;
                bheight = mFinalbitmap.getHeight()/4;
                restartBtn = Bitmap.createScaledBitmap(restartBtn, bwidth, bheight, true);
                xcord = mFinalbitmap.getWidth()/2 - button.getWidth()/2;
                ycord = mFinalbitmap.getHeight()/2 - button.getHeight()/16;

                canvas.drawBitmap(restartBtn, xcord, ycord, null);
            }
        }
    }

    private void applyPenalty() {
        // apply code to move player forward or backward
        if(gameitems[level][penaltyindex] == "pod")
        {
            int collcount = 0;
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(activity, "You get to move ahead " + penalty[level][penaltyindex] + " spaces", Toast.LENGTH_SHORT).show();
                }
            });

            podspeed = true;
            xspeed *= 2;

            playerPos += penalty[level][penaltyindex];
            roll = penalty[level][penaltyindex];

            while(collcount < boardPos[level].length) // ensure pod doesn't drop player on enemy
            {
                if(playerPos == boardPos[level][collcount])
                {
                    if(!(gameitems[level][collcount] == "heart" || gameitems[level][collcount] == "shield" || gameitems[level][collcount] == "life"))
                    {
                        playerPos++;
                        roll++;
                        collcount = 0;
                    }
                    else
                    {
                        collcount++;
                    }
                }
                else
                {
                    collcount++;
                }
            }
        }
        else if(gameitems[level][penaltyindex] == "heart" || gameitems[level][penaltyindex] == "shield" || gameitems[level][penaltyindex] == "life")
        {
            roll = 0;
            rolling = true;
            show = true;
        }
        else
        {
            if(shieldCountImg.size() > 0)
            {
                shieldCount--;
                shieldCountImg.remove(shieldCount);
                roll = 0;
                rolling = true;
                show = true;
            }
            else if(!gameOver)
            {
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(activity, "moving player backward " + penalty[level][penaltyindex] + " spaces", Toast.LENGTH_SHORT).show();
                    }
                });

                playerPos += penalty[level][penaltyindex];
                roll = -penalty[level][penaltyindex];

                xspeed *= -2;
                collided = true;

                //stat deduction
                healthCount--;
                healthCountImg.remove(healthCount);
                if(healthCountImg.size() == 0 && lifeCountImg.size() > 0)
                {
                    healthCount = 3;
                    lifeCount--;
                    lifeCountImg.remove(lifeCount);
                    for(int i = 0; i < healthCount; i++)
                    {
                        statCanvas.drawBitmap(statHeart, heartLeft,28,null);
                        heartLeft += step/2;
                        if(i == healthCount - 1)
                        {
                            heartLeft = 9*step/2;
                        }

                        healthCountImg.add(statHeart);
                    }
                }
                if(healthCountImg.size() == 0 && lifeCountImg.size() == 0)
                {
                    count = 0;
                    roll = 0;
                    gameOver = true;
                    rolling = true;
                    collided = false;
                }
            }
        }
    }

    private int createUniqueRandomNumber(int from, int to) {

        int uniquenum = 0;
        boolean numadded = false;
        Random r = new Random();
        int randnum = r.nextInt(to + 1 - from) + from;

        while(!numadded)
        {
            if(!arrayList.contains(randnum))
            {
                arrayList.add(randnum);
                uniquenum = randnum;

                numadded = true;
            }
            else
            {
                randnum = r.nextInt(to + 1 - from) + from;
            }
        }

        return uniquenum;
    }

    public Bitmap resizeImage(Bitmap image, int maxWidth, int maxHeight)
    {
        Bitmap resizedImage = null;
        try {
            int imageHeight = image.getHeight();


            if (imageHeight > maxHeight)
                imageHeight = maxHeight;
            int imageWidth = (imageHeight * image.getWidth())
                    / image.getHeight();

            if (imageWidth > maxWidth) {
                imageWidth = maxWidth;
                imageHeight = (imageWidth * image.getHeight())
                        / image.getWidth();
            }

            if (imageHeight > maxHeight)
                imageHeight = maxHeight;
            if (imageWidth > maxWidth)
                imageWidth = maxWidth;


            resizedImage = Bitmap.createScaledBitmap(image, maxWidth,
                    90*maxHeight/100, true);
        } catch (OutOfMemoryError e) {

            e.printStackTrace();
        }catch(NullPointerException e)
        {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return resizedImage;
    }

    private int[] createUniqueRandomNumbers(int from, int to, int numints) {
        arrayNums.clear();
        int[] enemyrowpos = new int[numints];
        int count = 0;

        while(count < numints)
        {
            Random r = new Random();
            int randnum = r.nextInt(to + 1 - from) + from;

            if(!arrayNums.contains(randnum)) // checks to see if array contains randnum value
            {
                arrayNums.add(randnum);
                enemyrowpos[count] = randnum;
                count++;
            }
        }

        return enemyrowpos;
    }

    private void restartGame()
    {
        newlevel = false;
        healthCountImg.clear();
        emptyCountImg.clear();
        lifeCountImg.clear();
        shieldCountImg.clear();
        resizedPH = spriteheight;
        resizedPW =  spritewidth;
        showCongratulations = false;
        gameEnd = false;

        for(int i = 0; i < healthCount; i++)
        {
            healthCountImg.add(statHeart);
        }
        for(int i = 0; i < emptyHeartCount; i++)
        {
            emptyCountImg.add(emptyHeart);
        }
        for(int i = 0; i < shieldCount; i++)
        {
            shieldCountImg.add(statShield);
        }
        for(int i = 0; i < lifeCount; i++)
        {
            lifeCountImg.add(statLife);
        }

        for(int j = 0; j < maxlevel;  j++)
        {
            for(int i = 0; i < uniquenums[j].length; i++) // create random penalties or reward
            {
                if(i >= numEnemy[j]) // no penalty or reward for portal object ; num enemies in the level j
                {
                    penalty[j][i] = 0;
                }
                else
                {
                    if(i >= 5)
                    {
                        arrayList.clear();
                    }
                    if(gameitems[j][i] == "pod")
                    {
                        penalty[j][i] = createUniqueRandomNumber(1, penaltyrange[j][i]);
                    }
                    else
                    {
                        penalty[j][i] = -createUniqueRandomNumber(1, penaltyrange[j][i]);
                    }
                }
            }
            arrayList.clear();
        }


        arrayList.clear();
        Random r = new Random();
        int highestplacedenemy = r.nextInt(3);

        for(int j = 0; j < gameObjs.length; j++)
        {
            int count = 0;
            for(int i = 0; i < uniquenums[j].length; i++) // create position on the board
            {
                if(gameitems[j][i] == "pod") // places pod in between 3 enemies at all times???
                {
                    Collections.sort(arrayList);
                    int max = arrayList.get(arrayList.size() - 1);
                    int min = arrayList.get(0);
                    uniquenums[j][i] = createUniqueRandomNumber(min, max);
                }
                else // place enemies
                {
                    int enemy = numEnemy[j] - i;
                    if(enemy > 0)
                    {
                        int pos = i - count;

                        int numend = enemyrowpos[pos] * 10;
                        int numstart = numend - 9;
                        if(enemyrowpos[pos] == 5)
                        {
                            uniquenums[j][i] = createUniqueRandomNumber(numstart, numend - 1);
                        }
                        else
                        {
                            uniquenums[j][i] = createUniqueRandomNumber(numstart, numend);
                        }

                        if((i + 1)%4 == 0)
                        {
                            count = i + 1;
                            enemyrowpos = createUniqueRandomNumbers(2,5, 4);
                        }
                    }
                    else // place other board objects
                    {
                        uniquenums[j][i] = createUniqueRandomNumber(11, 49);
                    }
                }
            }
            arrayList.clear();
        }

        randcells = uniquenums;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float x = event.getX();
        float y = event.getY();
        float xmin = xcord;
        float xmax = xmin + bwidth;
        float ymin = ycord;
        float ymax = ymin + bheight;
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                //Check if the x and y position of the touch is inside the bitmap
                return true;

            case MotionEvent.ACTION_UP:
                if((x > xmin) && (x < xmax) && (y > ymin) && (y < ymax))
                {
                    if(!gameEnd)
                    {
                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(activity, "rolling dice...", Toast.LENGTH_SHORT).show();
                            }
                        });

                        show = false;
                        gameEnd = true;
                    }
                    else if(showCongratulations || gameOver)
                    {
                        x1 = 20;
                        roll = 0;
                        count = 0;
//                        level = 0;
                        showCongratulations = false;
                        gameOver = false;
                        show = true;
                        rolling = true;
                        gameEnd = false;
                        drawbkg = true;
                        playerPos = 1;
                        healthCount = 3;
                        lifeCount = 1;
                        shieldCount = 0;
                        collided = false;
                        assets.clear();
                        destroyDataBase();
                        restartGame();
                    }
                    return true;
                }
        }
        return false;
    }
}