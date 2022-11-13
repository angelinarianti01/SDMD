package com.example.stockmanagement4.databasehelpers

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.example.stockmanagement4.models.Product


class ProductHelper(context: Context?) : SQLiteOpenHelper(context, "StockManagement.db", null, 2) {
    private val context = context

    private val TABLE_NAME = "PRODUCT"
    private val COLUMN_ID = "_id"
    private val COLUMN_NAME = "product_name"
    private val COLUMN_CATEGORY = "product_category"
    private val COLUMN_BRAND = "product_brand"
    private val COLUMN_COST = "product_cost"
    private val COLUMN_WATCH = "product_watch"
    private val COLUMN_MINSTOCK = "product_minstock"

    private val TABLE_NAME2 = "PRODUCTQUANTITY"
    private val COLUMN_PRODUCTID = "product_id"
    private val COLUMN_EXP = "expiry_date"
    private val COLUMN_QTY = "product_quantity"

    override fun onConfigure(db: SQLiteDatabase?) {
        super.onConfigure(db)
        db?.setForeignKeyConstraintsEnabled(true);
    }

    override public fun onCreate(db: SQLiteDatabase) {
        val query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_CATEGORY + " TEXT, " +
                COLUMN_BRAND + " TEXT, " +
                COLUMN_COST + " INTEGER, " +
                COLUMN_WATCH + " INTEGER, " +
                COLUMN_MINSTOCK + " INTEGER);"

        db.execSQL(query)
    }

   override fun onUpgrade(db: SQLiteDatabase, i: Int, i1: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    //public
    fun createTable() {
        val db: SQLiteDatabase = this.writableDatabase
        val query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_CATEGORY + " TEXT, " +
                COLUMN_BRAND + " TEXT, " +
                COLUMN_COST + " INTEGER, " +
                COLUMN_WATCH + " INTEGER, " +
                COLUMN_MINSTOCK + " INTEGER);"

        db.execSQL(query)
    }

    // public
    fun dropTable() {
        val db: SQLiteDatabase = this.writableDatabase
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
    }

    fun addProduct(product: Product) {
        val db: SQLiteDatabase = this.writableDatabase

        val cv = ContentValues()
        cv.put(COLUMN_NAME, product.name)
        cv.put(COLUMN_CATEGORY, product.category)
        cv.put(COLUMN_BRAND, product.brand)
        cv.put(COLUMN_COST, product.cost)
        cv.put(COLUMN_WATCH, product.watch)
        cv.put(COLUMN_MINSTOCK, product.minstock)
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

    fun readDataWhereString(column: String, stringValue: String):Cursor{
        val query = "SELECT * FROM $TABLE_NAME WHERE $column='$stringValue'"
        val db: SQLiteDatabase = this.getReadableDatabase()
        lateinit var cursor: Cursor
        if (db != null) {
            cursor = db.rawQuery(query, null)
        }
        return cursor
    }

    fun readDataWhereInt(column: String, intValue: Int):Cursor{
        val query = "SELECT * FROM $TABLE_NAME WHERE $column=$intValue"
        val db: SQLiteDatabase = this.getReadableDatabase()
        lateinit var cursor: Cursor
        if (db != null) {
            cursor = db.rawQuery(query, null)
        }
        return cursor
    }

    fun updateData(product: Product) {
        val db: SQLiteDatabase = this.getWritableDatabase()
        val cv = ContentValues()
        cv.put(COLUMN_NAME, product.name)
        cv.put(COLUMN_CATEGORY, product.category)
        cv.put(COLUMN_BRAND, product.brand)
        cv.put(COLUMN_COST, product.cost)
        cv.put(COLUMN_WATCH, product.watch)
        cv.put(COLUMN_MINSTOCK, product.minstock)
        val result = db.update(TABLE_NAME, cv, "_id=?", arrayOf(product.id)).toLong()
        if (result == -1L) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show()
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