package com.neobyte.footbalschedule.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.ManagedSQLiteOpenHelper
import org.jetbrains.anko.db.PRIMARY_KEY
import org.jetbrains.anko.db.TEXT
import org.jetbrains.anko.db.createTable
import org.jetbrains.anko.db.dropTable

class DatabaseHelper(ctx: Context) : ManagedSQLiteOpenHelper(
    ctx, "SuppaAvengerTeam.db", null, 1
) {

  companion object {
    const val TABLE_FAVORITE: String = "TABLE_FAVORITE"
    const val EVENT_ID: String = "EVENT_ID"
    const val EVENT_CONTENT: String = "EVENT_CONTENT"

    private var instance: DatabaseHelper? = null

    @Synchronized fun getInstance(ctx: Context): DatabaseHelper {
      if (instance == null) {
        instance = DatabaseHelper(ctx.applicationContext)
      }
      return instance as DatabaseHelper
    }
  }

  override fun onCreate(db: SQLiteDatabase) {
    // Here you create tables
    db.createTable(
        TABLE_FAVORITE, true,
        EVENT_ID to TEXT + PRIMARY_KEY,
        EVENT_CONTENT to TEXT
    )
  }

  override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    // Here you can upgrade tables, as usual
    db.dropTable(TABLE_FAVORITE, true)
  }

}