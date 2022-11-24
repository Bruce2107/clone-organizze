package com.manogarrafa.organizze.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.manogarrafa.organizze.config.FirebaseConfig
import com.manogarrafa.organizze.databinding.ActivityLoginBinding
import com.manogarrafa.organizze.model.User

class LoginActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        with(binding) {
            btnLogin.setOnClickListener {
                val tEmail = editTextTextPersonEmail.text.toString()
                val tPassword = editTextTextPersonPassword.text.toString()
                if (validateField(tEmail, tPassword)) {
                    loginAccount(User(email = tEmail, password = tPassword, name = null, id = ""))
                }
            }
        }

    }

    private fun validateField(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            Toast.makeText(this, "Preencha o email", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.isEmpty()) {
            Toast.makeText(this, "Preencha a senha", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun loginAccount(user: User) {
        firebaseAuth = FirebaseConfig.getFirebaseAuth()

        firebaseAuth.signInWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
//                    Toast.makeText(this, "Logado", Toast.LENGTH_SHORT).show()
                    navigateToPrincipal()
                } else {
                    val toastMessage: String =
                        when (it.exception) {
                            is FirebaseAuthInvalidUserException -> "Email não encontrado"
                            is FirebaseAuthInvalidCredentialsException -> "Email ou senha incorretos"
                            else -> "Erro ao logar"
                        }

                    Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun navigateToPrincipal() {
        PrincipalActivity.getStaterIntent(this@LoginActivity).run(::startActivity)
        finish()
    }

    companion object {
        fun getStaterIntent(context: Context) =
            Intent(context, LoginActivity::class.java)
    }
}