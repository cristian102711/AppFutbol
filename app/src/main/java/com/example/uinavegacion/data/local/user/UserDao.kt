package com.example.uinavegacion.data.local.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao{

    //permite insertar datos en la entidad
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(user: UserEntity): Long
    
    //buscar los datos del usuario por su correo(null)
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getByEmail(email: String): UserEntity?
    
    //cuente la cantidad de registros en la tabla
    @Query("SELECT COUNT(*) FROM users")
    suspend fun count(): Int
    
    //retornar todos los registros ordenados por id de manera ascendente
    @Query("SELECT * FROM users ORDER BY id ASC")
    suspend fun getAll(): List<UserEntity>
    
}