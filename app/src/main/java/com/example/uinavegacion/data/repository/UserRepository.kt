package com.example.uinavegacion.data.repository

import com.example.uinavegacion.data.local.user.UserDao
import com.example.uinavegacion.data.local.user.UserEntity

//orquestador para las reglas de negocio: login / register
class UserRepository(
    private val userDao: UserDao
){
    //login: buscar por email y clave si puede ingresar a la app
    suspend fun login(email: String, password: String): Result<UserEntity>{
        val user = userDao.getByEmail(email)
        //valido si existe o no
        return if(user !=null && user.password == password){
            Result.success(user)
        }
        else{
            Result.failure(IllegalArgumentException("Crendenciales Inválidas"))
        }
    }

    //register: Inserte los datos del usuario siempre que el correo no exista
    suspend fun register(name: String, email: String, phone: String, password: String): Result<Long>{
        //verifico si el correo existe
        val exists = userDao.getByEmail(email) != null
        if(exists){
            return Result.failure(IllegalArgumentException("El correo ya esta registrado"))
        }
        else{
            val id = userDao.insert(
                UserEntity(
                    name = name,
                    email = email,
                    phone = phone,
                    password = password
                )
            )
            return Result.success(id)
        }
    }


}
