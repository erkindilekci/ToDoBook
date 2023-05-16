package com.erkindilekci.todobook.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.erkindilekci.todobook.data.ToDoDatabase
import com.erkindilekci.todobook.util.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    val migration2to3 = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
        }
    }

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        ToDoDatabase::class.java,
        DATABASE_NAME
    ).addMigrations(migration2to3).build()

    @Singleton
    @Provides
    fun provideDao(database: ToDoDatabase) = database.toDoDao()
}

// ctrl + p icinde istedigi parametrelere bak
// ctrl + q islevin ozelliklerine bak