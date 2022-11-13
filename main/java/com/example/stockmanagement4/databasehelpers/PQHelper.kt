package com.example.stockmanagement4.databasehelpers

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.example.stockmanagement4.models.ProductQuantity
import java.util.*

class PQHelper(context: Context?)  : SQLiteOpenHelper(context, "StockManagement.db", null, 2) {
    private val context = context

    private val TABLE_NAME = "PRODUCTQUANTITY"
    private val COLUMN_ID = "product_id"
    private val COLUMN_DATE = "delivery_date"
    private val COLUMN_QTY = "product_quantity"

    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " TEXT PRIMARY KEY, " +
                COLUMN_DATE + " DATE PRIMARY KEY, " +
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
                COLUMN_DATE + " DATE, " +
                COLUMN_QTY + " INTEGER);"

        db.execSQL(query)
    }

    fun dropTable() {
        val db: SQLiteDatabase = this.writableDatabase
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
    }

    fun addRecord(productQuantity: ProductQuantity) {
        val db: SQLiteDatabase = this.writableDatabase

        val cv = ContentValues()
        cv.put(COLUMN_ID, productQuantity.productId?.trim()?.toInt())
        cv.put(COLUMN_DATE, productQuantity.date)
        cv.put(COLUMN_QTY, productQuantity.quantity)
        val result = db.insert(TABLE_NAME, null, cv)
        db.close()
        if (result == -1L) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show()
        }
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

    fun updateData(productQuantity: ProductQuantity) : Boolean{
        val db: SQLiteDatabase = this.getWritableDatabase()
        val cv = ContentValues()
        cv.put(COLUMN_ID, productQuantity.productId)
        cv.put(COLUMN_DATE, productQuantity.date)
        cv.put(COLUMN_QTY, productQuantity.quantity)
        val result = db.update(TABLE_NAME, cv, "${COLUMN_ID}=?", arrayOf(productQuantity.productId)).toLong()
        if (result == -1L) {
            return false
        } else {
            return true
        }
    }

    fun deleteOneRow(row_id: String) {
        val db: SQLiteDatabase = this.getWritableDatabase()
        val result = db.delete(TABLE_NAME, "_id=?", arrayOf(row_id)).toLong()
        if (result == -1L) {
            Toast.makeText(context, "Failed to Delete.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Successfully Deleted.", Toast.LENGTH_SHORT).show()
        }
    }

    fun deleteAllData() {
        val db: SQLiteDatabase = this.getWritableDatabase()
        db.execSQL("DELETE FROM $TABLE_NAME")
    }
}