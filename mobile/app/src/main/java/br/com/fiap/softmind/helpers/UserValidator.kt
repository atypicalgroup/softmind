package br.com.fiap.softmind.helpers

import android.content.Context
import br.com.fiap.softmind.data.db.AppDatabase
import br.com.fiap.softmind.data.entity.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

suspend fun validateOrCreateUser(email: String, role: String, context: Context): User {
    val db = AppDatabase.getDatabase(context)
    val userDao = db.userDao()

    val existingUser = userDao.getUserByEmail(email)
    return (if (existingUser != null) {
        existingUser
    } else {
        val token = UUID.randomUUID().toString()
        val user = User(email = email, token = token, role = role)
        userDao.insertUser(user)
        user
    }) as User
}

