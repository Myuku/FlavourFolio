package com.example.flavourfolio.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Recipe::class, Step::class, Detail::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract val recipeDao: RecipeDao
    abstract val stepDao: StepDao
    abstract val detailDao: DetailDao


    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context) : AppDatabase{
            synchronized(this){
                var instance = INSTANCE
                if(instance == null){
                    instance = Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java, "recipe_table").build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }


}