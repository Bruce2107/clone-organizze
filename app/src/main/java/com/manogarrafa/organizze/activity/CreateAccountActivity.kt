package com.manogarrafa.organizze.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.manogarrafa.organizze.R
import com.manogarrafa.organizze.config.FirebaseConfig
import com.manogarrafa.organizze.databinding.ActivityCreateAccountBinding
import com.manogarrafa.organizze.databinding.ActivityLoginBinding
import com.manogarrafa.organizze.model.User
import com.manogarrafa.organizze.utils.ConverterBase64

class CreateAccountActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateAccountBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Cadastro"

        with(binding) {
            btnCadastar.setOnClickListener {
                val tName = editTextTextPersonName.text.toString()
                val tEmail = editTextTextPersonEmail.text.toString()
                val tPassword = editTextTextPersonPassword.text.toString()

                if (validateField(tName, tEmail, tPassword)) {

                    createAccount(User(id = "", tName, tEmail, tPassword))
                }
            }
        }
    }

    private fun validateField(name: String, email: String, password: String): Boolean {
        if (name.isEmpty()) {
            Toast.makeText(this, "Preencha o nome", Toast.LENGTH_SHORT).show()
            return false
        }
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

    private fun createAccount(user: User) {
        firebaseAuth = FirebaseConfig.getFirebaseAuth()
        firebaseAuth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
//                    Toast.makeText(this, "Sucesso ao cadastrar", Toast.LENGTH_SHORT).show()
                    val base64Email =
                        ConverterBase64.encode(binding.editTextTextPersonEmail.text.toString())
                    user.id = base64Email
                    user.salvar()
                    finish()
                } else {
                    val toastMessage: String =
                        when (it.exception) {
                            is FirebaseAuthWeakPasswordException -> "Senha fraca"
                            is FirebaseAuthInvalidCredentialsException -> "Email invalido"
                            is FirebaseAuthUserCollisionException -> "Email já cadastrado"
                            else -> "Erro ao cadastrar"
                        }
                    Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()
                }
            }
    }

    companion object {
        fun getStaterIntent(context: Context) =
            Intent(context, CreateAccountActivity::class.java)
    }

}