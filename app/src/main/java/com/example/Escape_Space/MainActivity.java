package com.example.Escape_Space;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Html;
import android.view.ContextThemeWrapper;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;


import static com.example.Escape_Space.GameView.gameObjsType;
import static com.example.Escape_Space.GameView.gameThread;
import static com.example.Escape_Space.GameView.healthCount;
import static com.example.Escape_Space.GameView.level;
import static com.example.Escape_Space.GameView.lifeCount;
import static com.example.Escape_Space.GameView.maxlevel;
import static com.example.Escape_Space.GameView.penalty;
import static com.example.Escape_Space.GameView.playerPos;
import static com.example.Escape_Space.GameView.shieldCount;
import static com.example.Escape_Space.GameView.uniquenums;
import static com.example.Escape_Space.GameView.xspeed;

public class MainActivity extends AppCompatActivity {
    public static boolean newGame = false;
    public static int gameId = 1;
    private static SQLiteDatabase db = null;
    public int id;
    private final String DB_NAME = "eDB";
    public static int[][] opjType; // Create an ArrayList object type
    public static int[][] opjPos ;  // Create an ArrayList object position X
    public static int[][] opjPenalty ;
    public static int plPos;
    public static int speed;
    public static int retLevel;
    public static int lifeData;
    public static int heartData;
    public static int shieldData;
    public static List<Integer> assetsData = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dbL1Setup();
    }
    @Override
    public void onResume() {
        super.onResume();

        retrieveData();
    }

    @Override
    public void onPause() {
        super.onPause();
        saveData();
        gameThread.setRunning(false);
    }

//    @Override
//    public void onStop() {
//        super.onStop();
//
//    }

    /////////////////// create tables ////////////////////////
    public void dbL1Setup() {

        final String[] status = {"New Game", "Resume Game"};
        //this.deleteDatabase(DB_NAME); //to delete tables
        db = this.openOrCreateDatabase(DB_NAME, MODE_PRIVATE,null);

        ///////////////// main tables
        createTable();

        Cursor c = db.rawQuery("SELECT * FROM tblMain", null);

        if(c != null) {     //if the tables are exists are there any record

            if (c.getCount() <= 0) {   //no record
                story();
                insertDefault();
                newGame = true;
                c.close();
                runGame();
            } else {
                final Cursor c2 = db.rawQuery("SELECT * FROM tblMain WHERE status = 'A'", null);
                if (c2.getCount() > 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setCancelable(false);
                    builder.setTitle("There is an active game ?? ");
                    builder.setItems(status, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if ("New Game".equals(status[which])) {
                                newGame = true;
                                story();
                                destroyDataBase();
                                createTable();
                                insertDefault();
                                runGame();
                            } else if ("Resume Game".equals(status[which])) {

                                Cursor gameC = db.rawQuery("SELECT * FROM tblMain WHERE status = 'A'", null);
                                gameC.moveToFirst();
                                int gameId = gameC.getInt(gameC.getColumnIndex("gameID"));
                                gameC.close();
                                Cursor opjC = db.rawQuery("SELECT * FROM tblOpject WHERE gameID = " + gameId +" AND opjectType <>"+ 8, null);
                                int hhhhh = opjC.getCount();
                                if (opjC.getCount() > 0) {
                                    newGame = false;
                                    opjC.moveToFirst();
                                    int opCount = opjC.getCount();
                                    retLevel = (opjC.getInt(opjC.getColumnIndex("level")));
                                    opjType = new int[maxlevel][opCount];
                                    opjPos = new int[maxlevel][opCount];
                                    opjPenalty = new int[maxlevel][opCount];
                                    for (int i = 0; i < opCount; i++) {
                                        opjType[retLevel][i] = (opjC.getInt(opjC.getColumnIndex("opjectType")));
                                        opjPos[retLevel][i] = (opjC.getInt(opjC.getColumnIndex("position")));
                                        opjPenalty[retLevel][i] = (opjC.getInt(opjC.getColumnIndex("penalty")));
                                        opjC.moveToNext();
                                    }

                                    opjC.close();
                                    Cursor playerC =  db.rawQuery("SELECT * FROM tblOpject WHERE gameID = "+ gameId+" AND opjectType = 8 ", null);
                                    int dgf = playerC.getCount();
                                    playerC.moveToFirst();
                                    plPos = (playerC.getInt(playerC.getColumnIndex("position")));
                                    speed = (playerC.getInt(playerC.getColumnIndex("penalty")));
                                    Cursor picC = db.rawQuery("SELECT * FROM tblPicks WHERE gameID = " + gameId , null);
                                    picC.moveToFirst();
                                    lifeData = (picC.getInt(picC.getColumnIndex("Life")));
                                    heartData = (picC.getInt(picC.getColumnIndex("Hearts")));
                                    shieldData = (picC.getInt(picC.getColumnIndex("Shield")));
                                    picC.close();
                                    Cursor assC = db.rawQuery("SELECT * FROM tblAssets WHERE gameID = " + gameId , null);
                                    assC.moveToFirst();
                                    for(int i = 0 ; i < assC.getCount();i++) {
                                        assetsData.add(assC.getInt(assC.getColumnIndex("asset")));
                                        assC.moveToNext();
                                        assC.close();
                                    }

                                }
                                c2.close();
                                runGame();
                            }
                        }
                    });
                    builder.show();
                }
            }
        }
    } // dbSetup()
    /////////////////// SAVE DATA ON PAUSE
    public void  saveData(){
        Cursor upSave = db.rawQuery("SELECT * FROM tblOpject WHERE gameID = " + gameId + "", null);
        int hhh = upSave.getCount();
        if(upSave.getCount() <=0) {
            db.beginTransaction();
            for (int j = 0; j < gameObjsType[level].length; j++) {
                int rrrrrr = gameObjsType[level][j];
                db.execSQL("INSERT INTO tblOpject VALUES(?1," + gameObjsType[level][j] + "," + uniquenums[level][j] + "," + penalty[level][j] + ","+ level +"," + gameId + ");");
            }

            for (int i = 0 ; i < GameView.assets.size(); i++){
                db.execSQL("INSERT INTO tblAssets VALUES(?1,"+ GameView.assets.get(i) +"," + gameId + ");");
            }

            db.execSQL("INSERT INTO tblOpject VALUES(?1, 8, " + playerPos + ","+ xspeed +","+ level +"," + gameId + ");");
            if (level < 1){
                db.execSQL("INSERT INTO tblPicks VALUES(?1," + healthCount +"," + lifeCount +"," + shieldCount +","+ gameId + ");");
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            upSave.close();
        }
        else {
            db.beginTransaction();
            db.execSQL("UPDATE tblPicks SET Hearts = " + healthCount +" WHERE  gameID = 1 ");
            db.execSQL("UPDATE tblPicks SET Life =" + lifeCount +" WHERE  gameID = 1");
            db.execSQL("UPDATE tblPicks SET Shield =" + shieldCount +" WHERE  gameID = 1 ");
            db.execSQL("UPDATE tblOpject SET position = " + playerPos + ", penalty = "+ xspeed +", level = "+ level +" WHERE  opjectType = 8");
            db.execSQL("DELETE FROM tblAssets");
            for (int i = 0 ; i < GameView.assets.size(); i++){
                db.execSQL("UPDATE tblAssets SET asset ="+ GameView.assets.get(i) +" WHERE  gameID = 1 ");
            }

            db.setTransactionSuccessful();
            db.endTransaction();
        }
    }
    public  void createTable(){
        db.execSQL("CREATE TABLE IF NOT EXISTS tblMain" +
                "(gameID INTEGER PRIMARY KEY AUTOINCREMENT, status char);");

        db.execSQL("CREATE TABLE IF NOT EXISTS tblPicks" +
                "(picksID INTEGER PRIMARY KEY AUTOINCREMENT, Hearts int, Life int, Shield int, gameID int NOT NULL,FOREIGN KEY(gameID) REFERENCES tblMaim(gameID));");

        db.execSQL("CREATE TABLE IF NOT EXISTS tblOpject" +
                "(opjectID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "opjectType int,position int ,penalty int, level int, gameID int NOT NULL,FOREIGN KEY(gameID) REFERENCES tblMaim(gameID));");

        db.execSQL("CREATE TABLE IF NOT EXISTS tblAssets" +
                "(assetID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "asset int, gameID int NOT NULL,FOREIGN KEY(gameID) REFERENCES tblMaim(gameID));");
    }

    public static void insertDefault(){
        db.execSQL("INSERT INTO tblMain VALUES(?1,'A')");
        Cursor cStart = db.rawQuery("SELECT * FROM tblMain WHERE status = 'A'", null);
        cStart.moveToFirst();
        gameId = cStart.getInt(cStart.getColumnIndex("gameID"));
    }

    public void retrieveData(){
        db = this.openOrCreateDatabase(DB_NAME, MODE_PRIVATE,null);
        Cursor c = db.rawQuery("SELECT * FROM tblMain WHERE status = 'A'", null);
        if(c.getCount() > 0) {     //if the tables are exists are there any record
            c.close();
            Cursor gameC = db.rawQuery("SELECT * FROM tblMain WHERE status = 'A'", null);
            gameC.moveToFirst();
            if (gameC.getCount() > 0) {
                int gameId = gameC.getInt(gameC.getColumnIndex("gameID"));
                gameC.close();
                Cursor opjC = db.rawQuery("SELECT * FROM tblOpject WHERE gameID = " + gameId + " AND opjectType <>" + 8, null);
                int opCount = opjC.getCount();
                opjC.moveToFirst();
                if (opjType != null) {
                    retLevel = (opjC.getInt(opjC.getColumnIndex("level")));
                    opjType = new int[maxlevel][opCount];
                    opjPos = new int[maxlevel][opCount];
                    opjPenalty = new int[maxlevel][opCount];
                    for (int i = 0; i < opCount; i++) {
                        opjType[retLevel][i] = (opjC.getInt(opjC.getColumnIndex("opjectType")));
                        opjPos[retLevel][i] = (opjC.getInt(opjC.getColumnIndex("position")));
                        opjPenalty[retLevel][i] = (opjC.getInt(opjC.getColumnIndex("penalty")));
                        opjC.moveToNext();
                    }
                    opjC.close();
                    Cursor playerC =  db.rawQuery("SELECT * FROM tblOpject WHERE gameID = "+ gameId+" AND opjectType = 8", null);
                    playerC.moveToFirst();
                    plPos = (playerC.getInt(playerC.getColumnIndex("position")));
                    speed = (playerC.getInt(playerC.getColumnIndex("penalty")));
                    playerC.close();

                    Cursor picC = db.rawQuery("SELECT * FROM tblPicks WHERE gameID = " + gameId , null);
                    picC.moveToFirst();
                    lifeData = (picC.getInt(picC.getColumnIndex("Life")));
                    heartData = (picC.getInt(picC.getColumnIndex("Hearts")));
                    shieldData = (picC.getInt(picC.getColumnIndex("Shield")));
                    picC.close();

                    Cursor assC = db.rawQuery("SELECT * FROM tblAssets WHERE gameID = " + gameId , null);
                    assC.moveToFirst();
                    for(int i = 0 ; i < assC.getCount();i++) {
                        assetsData.add(assC.getInt(assC.getColumnIndex("asset")));
                        assC.moveToNext();
                        assC.close();
                    }
                }
            }
        }
    }



    public void story(){
        AlertDialog alertDialog2 = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom)).create();
        alertDialog2.setTitle(Html.fromHtml("<font color='#FF7F27'>GAME GUIDE</font>"));
        alertDialog2.setMessage(Html.fromHtml("<font color='#FFFFF'>ROL THE DICE 1TO 6 </font>" + "<br/>" +
                                                     "<font color='#FFFFF'>if the player stop on spider or blob will return back  </font>" + "<br/>"+
                                                     "<font color='#FFFFF'>if the player stop on port will go forward  </font>" + "<br/>"+
                                                     "<font color='#FFFFF'>if the player stop on hearts or life or shield will add to player assets  </font>" + "<br/>"
                ));

        alertDialog2.setIcon(R.drawable.escape_space);
        alertDialog2.setButton(AlertDialog.BUTTON_POSITIVE, "START GAME", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
            }
        });

        alertDialog2.show();
        Button btn2 = alertDialog2.getButton(DialogInterface.BUTTON_POSITIVE);//Whatever button it is.
        ViewGroup.MarginLayoutParams params2 = (ViewGroup.MarginLayoutParams) btn2.getLayoutParams();
        params2.setMargins(0,0,200,0);
        btn2.setLayoutParams(params2);


        AlertDialog alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom)).create();
        alertDialog.setTitle(Html.fromHtml("<font color='#FF7F27'>GAME STORY</font>"));
        alertDialog.setMessage(Html.fromHtml("<font color='#FFFFF'>June 20, 2084\n" +
                "It was a normal Tuesday night when a comet shower suddenly struck. Our space crew was scrambling to strap everything down when our alarms began to sound. \n" +
                "\n" +
                "Our station had taken heavy damage and we needed to get into our emergency pods. \n" +
                "\n" +
                "We all began to race in different directions towards our pods. \n" +
                "\n" +
                "A few moments after strapping myself safely into my pod our station began breaking apart and before I could safely eject my pod from the station, I began to crash. \n" +
                "\n" +
                "\n" +
                "A few days later....\n" +
                "\n" +
                "I’ve woken up, my pod is destroyed and the space station is ripped apart. I don’t know where any of my team members are or what day it is but I seem to be on another planet. \n" +
                "\n" +
                "I need to find my way out of here, and back to my crew. \n" +
                "\n</font>"));

        alertDialog.setIcon(R.drawable.escape_space);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "NEXT", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.show();
        Button btn = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);//Whatever button it is.
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) btn.getLayoutParams();
        params.setMargins(0,0,200,0);
        btn.setLayoutParams(params);
    }
    public void gameRoll(){

    }
    public static void destroyDataBase(){
        db.execSQL("DELETE FROM  tblMain");
        db.execSQL("DELETE FROM  tblOpject");
        db.execSQL("DELETE FROM tblPicks");
        db.execSQL("DELETE FROM  tblAssets");
    }
    public static void destDataLevel2(){
//        db.execSQL("DELETE FROM  tblMain");
        db.execSQL("DELETE FROM  tblOpject");
        db.execSQL("DELETE FROM tblAssets");


    }



    public void runGame(){

        setContentView(new GameView(this, this));
    }




}