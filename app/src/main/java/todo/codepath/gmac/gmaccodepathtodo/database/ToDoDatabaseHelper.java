package todo.codepath.gmac.gmaccodepathtodo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import todo.codepath.gmac.gmaccodepathtodo.ToDoItem;

public class ToDoDatabaseHelper extends SQLiteOpenHelper
{
    private static final String TAG = ToDoDatabaseHelper.class.getSimpleName();

    // Database Info
    private static final String DATABASE_NAME = "TodoDatabase";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_TODO_TASKS = "todoTasks";
    private static final String TABLE_USERS = "users";

    // Task Table Columns
    private static final String KEY_TODO_ID = "id";
    private static final String KEY_TODO_USER_ID_FK = "userId";
    private static final String KEY_TODO_TEXT = "task";
    private static final String KEY_TODO_DATE_TIME = "dateTime";

    // User Table Columns
    private static final String KEY_USER_ID = "id";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_PROFILE_PICTURE_URL = "profilePictureUrl";

    // Make it singleton
    private static ToDoDatabaseHelper sInstance;

    public static synchronized ToDoDatabaseHelper getInstance(final Context context)
    {
        if (sInstance == null)
        {
            sInstance = new ToDoDatabaseHelper(context.getApplicationContext());
        }

        return sInstance;
    }

    private ToDoDatabaseHelper(final Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        String CREATE_POSTS_TABLE = "CREATE TABLE " + TABLE_TODO_TASKS +
                "(" +
                KEY_TODO_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                KEY_TODO_USER_ID_FK + " INTEGER REFERENCES " + TABLE_USERS + "," + // Define a foreign key
                KEY_TODO_TEXT + " TEXT" + "," + KEY_TODO_DATE_TIME + "DATETIME" +
                ")";

        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS +
                "(" +
                KEY_USER_ID + " INTEGER PRIMARY KEY," +
                KEY_USER_NAME + " TEXT," +
                KEY_USER_PROFILE_PICTURE_URL + " TEXT" +
                ")";

        sqLiteDatabase.execSQL(CREATE_POSTS_TABLE);
        sqLiteDatabase.execSQL(CREATE_USERS_TABLE);
    }

    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override
    public void onUpgrade(final SQLiteDatabase sqLiteDatabase, final int oldVersion, final int newVersion)
    {
        if (oldVersion != newVersion)
        {
            // Simplest implementation is to drop all old tables and recreate them
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO_TASKS);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            onCreate(sqLiteDatabase);
        }
    }

    public void addToDoTask(final ToDoTaskModel toDoTaskModel)
    {
        // Create and/or open the database for writing
        final SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {
            // The user might already exist in the database (i.e. the same user created multiple posts).
            long userId = addOrUpdateUser(toDoTaskModel.mUserModel);

            ContentValues values = new ContentValues();
            values.put(KEY_TODO_USER_ID_FK, userId);
            values.put(KEY_TODO_TEXT, toDoTaskModel.mTask);
            values.put(KEY_TODO_DATE_TIME, toDoTaskModel.mDateTime);

            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            db.insertOrThrow(TABLE_TODO_TASKS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add post to database");
        } finally {
            db.endTransaction();
        }
    }

    // Insert or update a user in the database
    // Since SQLite doesn't support "upsert" we need to fall back on an attempt to UPDATE (in case the
    // user already exists) optionally followed by an INSERT (in case the user does not already exist).
    // Unfortunately, there is a bug with the insertOnConflict method
    // (https://code.google.com/p/android/issues/detail?id=13045) so we need to fall back to the more
    // verbose option of querying for the user's primary key if we did an update.
    public long addOrUpdateUser(final UserModel user)
    {
        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.
        SQLiteDatabase db = getWritableDatabase();
        long userId = -1;

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_USER_NAME, user.mUserName);
            values.put(KEY_USER_PROFILE_PICTURE_URL, user.mProfilePictureUrl);

            // First try to update the user in case the user already exists in the database
            // This assumes userNames are unique
            int rows = db.update(TABLE_USERS, values, KEY_USER_NAME + "= ?", new String[]{user.mUserName});

            // Check if update succeeded
            if (rows == 1)
            {
                // Get the primary key of the user we just updated
                String usersSelectQuery = String.format("SELECT %s FROM %s WHERE %s = ?",
                        KEY_USER_ID, TABLE_USERS, KEY_USER_NAME);
                Cursor cursor = db.rawQuery(usersSelectQuery, new String[]{String.valueOf(user.mUserName)});
                try
                {
                    if (cursor.moveToFirst())
                    {
                        userId = cursor.getInt(0);
                        db.setTransactionSuccessful();
                    }
                }
                finally
                {
                    if (cursor != null && !cursor.isClosed())
                    {
                        cursor.close();
                    }
                }
            }
            else
            {
                // user with this userName did not already exist, so insert new user
                userId = db.insertOrThrow(TABLE_USERS, null, values);
                db.setTransactionSuccessful();
            }
        }
        catch (Exception e)
        {
            Log.d(TAG, "Error while trying to add or update user");
        }
        finally
        {
            db.endTransaction();
        }

        return userId;
    }
}
