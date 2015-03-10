package project.alarm.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper{

	private static String TAG = "DataBaseHelper"; // Tag just for the LogCat
	private static String DB_PATH = "";
	private static String DB_NAME = "alarm.sqlite.sqlite";// Database name
	private SQLiteDatabase mDataBase;
	private Context mContext = null;
	
	public DatabaseHandler(Context context) {
		super(context, DB_NAME, null, 1);
		// TODO Auto-generated constructor stub
		DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
		this.mContext = context;
	}

	public void createDataBase() throws IOException {
		// If database not exists copy it from the assets

		boolean mDataBaseExist = checkDataBase();
		if (!mDataBaseExist) {
			this.getReadableDatabase();
			this.close();
			try {
				// Copy the database from assests
				copyDataBase();
				Log.e(TAG, "createDatabase database created");
			} catch (IOException mIOException) {
				throw new Error("ErrorCopyingDataBase");
			}
		}
	}
	
	// Check that the database exists here: /data/data/your package/databases/Da
		// Name
		public boolean checkDataBase() {
			File dbFile = new File(DB_PATH + DB_NAME);
			// Log.v("dbFile", dbFile + "   "+ dbFile.exists());
			return dbFile.exists();
		}
		
		// Copy the database from assets
		private void copyDataBase() throws IOException {
			Log.e("", "Copydatabase-->>>>>>>>>");
			InputStream mInput = mContext.getAssets().open(DB_NAME);

			String outFileName = DB_PATH + DB_NAME;
			Log.e("", "Copydatabase--" + outFileName);
			OutputStream mOutput = new FileOutputStream(outFileName);
			byte[] mBuffer = new byte[1024];
			int mLength;
			while ((mLength = mInput.read(mBuffer)) > 0) {
				mOutput.write(mBuffer, 0, mLength);
			}
			mOutput.flush();
			mOutput.close();
			mInput.close();
		}
		
		public boolean deleteDataBase() {

			if (checkDataBase()) {
				return mContext.deleteDatabase(DB_PATH + DB_NAME);
			} else
				return false;

		}
		
		public Boolean openDataBase() throws SQLException
		{
			String mPath = DB_PATH + DB_NAME;
			// Log.v("mPath", mPath);
			mDataBase = SQLiteDatabase.openDatabase(mPath, null,
					SQLiteDatabase.CREATE_IF_NECESSARY);
			// mDataBase = SQLiteDatabase.openDatabase(mPath, null,
			// SQLiteDatabase.NO_LOCALIZED_COLLATORS);
			return mDataBase != null;
		}
		
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
		}
}
