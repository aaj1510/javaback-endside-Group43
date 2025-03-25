package com.example.java1d;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {


    private static final int VERSION = 1 ;

    public DBHelper(@Nullable Context context) {
        super(context, "TaskTales.db",null, VERSION);

    }

    //ITEM TABLE --------------------------------------------------------------------
    public static final String ITEM_TABLE_NAME = "ITEM_TABLE";
    public static final String I_ID = "_ITEM_ID";
    public static final String I_NAME = "ITEM_NAME";
    public static final String I_TYPE = "ITEM_TYPE";
    public static final String I_DESC = "ITEM_DESCRIPTION";

    public static final String CREATE_ITEM_TABLE =
            "CREATE TABLE " + ITEM_TABLE_NAME + "(" +
                    I_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    I_NAME + " STRING NOT NULL, " +
                    I_TYPE + " STRING NOT NULL, " +
                    I_DESC + " STRING NOT NULL " + ")";

    public static final String DROP_ITEM_TABLE = "DROP TABLE IF EXISTS " + ITEM_TABLE_NAME;
    public static final String SELECT_ITEM_TABLE = "SELECT * FROM " + ITEM_TABLE_NAME;


    //BOSS TABLE --------------------------------------------------------------------
    private static final String BOSS_TABLE_NAME = "BOSS_TABLE";
    private static final String BOSS_ID = "_BID";
    private static final String BOSS_NAME = "BOSS_NAME";
    private static final String LEVEL = "BOSS_LEVEL";
    private static final String HEALTH = "HEALTH";
    private static final String B_GOLD = "BOSS_GOLD";
    private static final String CREATE_BOSS_TABLE =
            "CREATE TABLE " + BOSS_TABLE_NAME + "(" +
                    BOSS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    BOSS_NAME + " STRING NOT NULL, " +
                    LEVEL + " INTEGER NOT NULL, " +
                    HEALTH + " INTEGER NOT NULL, " +
                    B_GOLD + " INTEGER NOT NULL, " +
                    I_ID + " INTEGER NOT NULL, " +
                    " FOREIGN KEY ( " + I_ID + " ) REFERENCES " + ITEM_TABLE_NAME + "(" + I_ID + ")" + ");";


    private static final String DROP_BOSS_TABLE = "DROP TABLE IF EXISTS " + BOSS_TABLE_NAME;
    private static final String SELECT_BOSS_TABLE = "SELECT * FROM " + BOSS_TABLE_NAME;



    //Column Names for User Table --------------------------------------------------------------------
    private static final String USERS_TABLE_NAME = "USER_TABLE";
    private static final String USER_ID = "_UID";
    private static final String USERNAME = "USERNAME";
    private static final String EMAIL = "EMAIL";
    private static final String USER_CLASS = "CLASS";
    private static final String ACTION_PTS = "ACTION_PTS";
    private static final String PASSWORD = "PASSWORD";
    private static final String GOLD = "GOLD";
    private static final String TOTAL_BOSSES_DEFEATED = "TOTAL_BOSSES_DEFEATED";
    private static final String TOTAL_DAMAGE_DEALT = "TOTAL_DAMAGE_DEALT";

    private static final String CREATE_USER_TABLE =
            "CREATE TABLE " + USERS_TABLE_NAME + "(" +
                    USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    EMAIL + " STRING NOT NULL, " +
                    USERNAME + " STRING NOT NULL, " +
                    USER_CLASS + " STRING DEFAULT 'NIL',  " +
                    ACTION_PTS + " INTEGER NOT NULL DEFAULT 0, " +
                    PASSWORD + " STRING NOT NULL, " +
                    TOTAL_BOSSES_DEFEATED + " INTEGER NOT NULL DEFAULT 0, " +
                    TOTAL_DAMAGE_DEALT + " INTEGER NOT NULL DEFAULT 0, " +
                    GOLD + " INTEGER NOT NULL DEFAULT 0, " +
                    BOSS_ID + " INTEGER, " +
                    " FOREIGN KEY ( " + BOSS_ID + " ) REFERENCES " + BOSS_TABLE_NAME + "(" + BOSS_ID + ")" + ");";

    private static final String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + USERS_TABLE_NAME;
    private static final String SELECT_USER_TABLE = "SELECT * FROM " + USERS_TABLE_NAME;

    //preset minor tasks table: --------------------------------------------------------------------
    private static final String PRESET_MINOR_TABLE_NAME = "MINOR_TASKS_TABLE";
    private static final String PMT_ID = "_MINOR_TASKID";
    private static final String PM_CLASS = "CLASS";
    private static final String PM_TASK = "TASK_NAME";
    private static final String PM_TASK_DESC = "TASK_DESCRIPTION";
    private static final String PM_TASK_DIFF = "TASK_DIFFICULTY";

    private static final String PM_TASK_STATUS = "TASK_STATUS";
    private static final String PM_TASK_DEADLINE = "TASK_DEADLINE";


    private static final String CREATE_MINOR_TASKS_TABLE =
            "CREATE TABLE " + PRESET_MINOR_TABLE_NAME + "(" +
                    PMT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    PM_CLASS + " STRING NOT NULL, " +
                    PM_TASK + " STRING NOT NULL, " +
                    PM_TASK_DESC + " STRING NOT NULL, " +
                    PM_TASK_DIFF + " INTEGER NOT NULL, " +
                    PM_TASK_STATUS + " BOOLEAN NOT NULL, " +
                    PM_TASK_DEADLINE + " INTEGER NOT NULL, " +
                    USER_ID + " INTEGER, " +
                    " FOREIGN KEY ( " + USER_ID + " ) REFERENCES " + USERS_TABLE_NAME + "(" + USER_ID + ")" + ");";

    private static final String DROP_MINOR_TASKS_TABLE = "DROP TABLE IF EXISTS " + PRESET_MINOR_TABLE_NAME;
    private static final String SELECT_MINOR_TASKS_TABLE = "SELECT * FROM " + PRESET_MINOR_TABLE_NAME;


    //Major (User Created) tasks table: --------------------------------------------------------------------
    private static final String MAJOR_TASKS_TABLE_NAME = "MAJOR_TASKS_TABLE";
    private static final String MJT_ID = "_MAJOR_TASKID";
    private static final String MJ_TASK = "TASK_NAME";
    private static final String MJ_TASK_DESC = "TASK_DESCRIPTION";
    private static final String MJ_TASK_DIFF = "TASK_DIFFICULTY";

    private static final String MJ_TASK_STATUS = "TASK_STATUS";
    private static final String MJ_DATETIME_STARTED = "TASK_DATETIME_STARTED";
    private static final String MJ_DATETIME_COMPLETED = "TASK_DATETIME_COMPLETED";
    private static final String MJ_TASK_DEADLINE = "TASK_DEADLINE";

    private static final String CREATE_MAJOR_TASKS_TABLE =
            "CREATE TABLE " + MAJOR_TASKS_TABLE_NAME + "(" +
                    MJT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    MJ_TASK + " STRING NOT NULL, " +
                    MJ_TASK_DESC + " STRING NOT NULL, " +
                    MJ_TASK_DIFF + " INTEGER NOT NULL, " +
                    MJ_TASK_STATUS + " BOOLEAN NOT NULL, " +
                    MJ_DATETIME_STARTED + " STRING NOT NULL, " +
                    MJ_DATETIME_COMPLETED + " STRING NOT NULL, " +
                    MJ_TASK_DEADLINE + " STRING NOT NULL, " +
                    USER_ID + " INTEGER NOT NULL, " +
                    " FOREIGN KEY ( " + USER_ID + " ) REFERENCES " + USERS_TABLE_NAME + "(" + USER_ID + ")" + ");";

    private static final String DROP_MAJOR_TASKS_TABLE = "DROP TABLE IF EXISTS " + MAJOR_TASKS_TABLE_NAME;
    private static final String SELECT_MAJOR_TASKS_TABLE = "SELECT * FROM " + MAJOR_TASKS_TABLE_NAME;


    //Class TABLE --------------------------------------------------------------------
    private static final String CLASS_TABLE_NAME = "CLASS_TABLE";
    private static final String CLASS_ID = "_CLASSID";
    private static final String CLASS_TRAIT = "TRAIT";
    private static final String ARR_SKILL_IDS = "SKILL_IDS";


    private static final String CREATE_CLASS_TABLE =
            "CREATE TABLE " + CLASS_TABLE_NAME + "(" +
                    CLASS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    CLASS_TRAIT + " STRING NOT NULL, " +
                    ARR_SKILL_IDS + " STRING NOT NULL, " +
                    USER_ID + " INTEGER NOT NULL, " +
                    " FOREIGN KEY ( " + USER_ID + " ) REFERENCES " + USERS_TABLE_NAME + "(" + USER_ID + ")" + ");";



    private static final String DROP_CLASS_TABLE = "DROP TABLE IF EXISTS " + CLASS_TABLE_NAME;
    private static final String SELECT_CLASS_TABLE = "SELECT * FROM " + CLASS_TABLE_NAME;

    //Skills Table  --------------------------------------------------------------------
    private static final String SKILLS_TABLE_NAME = "SKILLS_TABLE";
    private static final String SKILL_ID = "_SKILLID";
    private static final String SKILL_NAME = "SKILL_NAME";
    private static final String SKILL_DESC = "SKILL_DESCRIPTION";
    private static final String SKILL_DAMAGE = "SKILL_DAMAGE";


    private static final String CREATE_SKILLS_TABLE =
            "CREATE TABLE " + SKILLS_TABLE_NAME + "(" +
                    SKILL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    SKILL_NAME + " STRING NOT NULL, " +
                    SKILL_DESC + " STRING NOT NULL, " +
                    SKILL_DAMAGE + " INTEGER NOT NULL " + ")";

    private static final String DROP_SKILLS_TABLE = "DROP TABLE IF EXISTS " + SKILLS_TABLE_NAME;
    private static final String SELECT_SKILLS_TABLE = "SELECT * FROM " + SKILLS_TABLE_NAME;


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ITEM_TABLE);
        db.execSQL(CREATE_BOSS_TABLE);
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_MINOR_TASKS_TABLE);
        db.execSQL(CREATE_MAJOR_TASKS_TABLE);
        db.execSQL(CREATE_CLASS_TABLE);
        db.execSQL(CREATE_SKILLS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_ITEM_TABLE);
        db.execSQL(DROP_BOSS_TABLE);
        db.execSQL(DROP_USER_TABLE);
        db.execSQL(DROP_MINOR_TASKS_TABLE);
        db.execSQL(DROP_MAJOR_TASKS_TABLE);
        db.execSQL(DROP_CLASS_TABLE);
        db.execSQL(DROP_SKILLS_TABLE);
    }


}
