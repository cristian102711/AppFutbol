package com.example.uinavegacion.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.uinavegacion.data.local.user.UserDao
import com.example.uinavegacion.data.local.user.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//creamos la base de datos indicando las entidades que se crearan
@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase: RoomDatabase(){
    //traigo el DAO de las tablas con insert por defecto
    abstract fun userDao(): UserDao

    companion object {
        //variable para la instancia de la BD - Volatil
        @Volatile
        private var INSTANCE: AppDatabase? = null
        //variable para el nombre del archivo de mi BD
        private const val DB_NAME = "ui_navegacion.db"

        //obtenga la unica instancia de la BD
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this){
                //construir la BD con un callBack
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DB_NAME
                )
                //crear un callback para cuando se ejecute por primera vez
                    .addCallback(object : RoomDatabase.Callback(){
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            //lanzar una corrutina para los insert por defecto
                            CoroutineScope(Dispatchers.IO).launch {
                                val dao = getInstance(context).userDao()
                                //precargamos los usuarios
                                val seed = listOf(
                                    UserEntity(
                                        name = "Admin",
                                        email = "a@a.cl",
                                        phone = "12345678",
                                        password = "Admin123!"
                                    ),
                                    UserEntity(
                                        name = "jose",
                                        email = "b@b.cl",
                                        phone = "12345678",
                                        password = "Jose123!"
                                    )
                                )
                             // inserte solo si no hay nada en la tabla
                             if(dao.count() == 0){
                                 seed.forEach { dao.insert(it) }
                             }
                            }
                        }
                    })
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

    }

}