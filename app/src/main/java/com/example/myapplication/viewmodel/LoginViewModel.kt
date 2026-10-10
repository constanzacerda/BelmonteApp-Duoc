package com.example.myapplication.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.myapplication.model.Usuario

class LoginViewModel: ViewModel() {


    val usuarioCorrecto = Usuario(
        "admin",
        "123"
    )


    var mail by mutableStateOf("")
    var pass by mutableStateOf("")

    fun cambiarMail(nuevoMail: String) {
        mail = nuevoMail
    }

    fun cambiarPass(nuevaPass: String) {
        pass = nuevaPass
    }

    fun validarLogin(): Boolean {
        var validador = false

        if (mail == usuarioCorrecto.mail &&
            pass == usuarioCorrecto.pass) {
            validador = true
        }

        return validador
    }
}