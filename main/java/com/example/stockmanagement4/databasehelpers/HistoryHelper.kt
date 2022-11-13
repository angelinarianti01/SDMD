package com.example.stockmanagement4.databasehelpers

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.example.stockmanagement4.models.History

class HistoryHelper(context: Context?) : SQLiteOpenHelper(context, "StockManagement.db", null, 2) {
    private val context = context

    private val TABLE_NAME = "HISTORY"
    private val COLUMN_ID = "product_id"
    private val COLUMN_TIME = "date_time"
    private val COLUMN_QTY = "stock_quantity"

    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " TEXT PRIMARY KEY, " +
                COLUMN_TIME + " DATETIME PRIMARY KEY, " +
                COLUMN_QTY + " INTEGER);"
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
    //public
    fun createTable() {
        val db: SQLiteDatabase = this.writableDatabase

        val query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER, " +
                COLUMN_TIME + " DATE, " +
                COLUMN_QTY + " INTEGER);"

        db.execSQL(query)
    }

    fun dropTable() {
        val db: SQLiteDatabase = this.writableDatabase
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
    }

    fun addRecord(history: History) {
        val db: SQLiteDatabase = this.writableDatabase

        val cv = ContentValues()
        cv.put(COLUMN_ID, history.productId?.trim()?.toInt())
        cv.put(COLUMN_TIME, history.date)
        cv.put(COLUMN_QTY, history.quantity)
        val result = db.insert(TABLE_NAME, null, cv)
        db.close()
    }

    fun readAllData(): Cursor {
        val query = "SELECT * FROM $TABLE_NAME"
        val db: SQLiteDatabase = this.getReadableDatabase()
        lateinit var cursor: Cursor
        if (db != null) {
            cursor = db.rawQuery(query, null)
        }
        return cursor
    }

    fun readDataWhereString(column: String, stringValue: String): Cursor {
        val query = "SELECT * FROM $TABLE_NAME WHERE $column='$stringValue'"
        val db: SQLiteDatabase = this.getReadableDatabase()
        lateinit var cursor: Cursor
        if (db != null) {
            cursor = db.rawQuery(query, null)
        }
        return cursor
    }

    fun readDataWhereInt(column: String, intValue: Int): Cursor {
        val query = "SELECT * FROM $TABLE_NAME WHERE $column=$intValue"
        val db: SQLiteDatabase = this.getReadableDatabase()
        lateinit var cursor: Cursor
        if (db != null) {
            cursor = db.rawQuery(query, null)
        }
        return cursor
    }

    fun updateData(history: History) {
        val db: SQLiteDatabase = this.getWritableDatabase()
        val cv = ContentValues()
        cv.put(COLUMN_ID, history.productId)
        cv.put(COLUMN_TIME, history.date)
        cv.put(COLUMN_QTY, history.quantity)
        val result = db.update(TABLE_NAME, cv, "${COLUMN_ID}=?", arrayOf(history.productId)).toLong()
        if (result == -1L) {
        } else {
        }
    }

    fun deleteOneRow(row_id: String) {
        val db: SQLiteDatabase = this.getWritableDatabase()
        val result = db.delete(TABLE_NAME, "product_id=?", arrayOf(row_id)).toLong()
    }

    fun deleteAllData() {
        val db: SQLiteDatabase = this.getWritableDatabase()
        db.execSQL("DELETE FROM $TABLE_NAME")
    }

}