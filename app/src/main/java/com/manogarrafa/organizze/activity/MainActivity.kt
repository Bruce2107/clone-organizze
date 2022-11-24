package com.manogarrafa.organizze.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.heinrichreimersoftware.materialintro.app.IntroActivity
import com.manogarrafa.organizze.R
import com.manogarrafa.organizze.config.FirebaseConfig
import com.manogarrafa.organizze.utils.CustomSlider

class MainActivity : IntroActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isButtonBackVisible = false
        isButtonNextVisible = false
        val customSlider = CustomSlider()
        val fragmentSlide1 = customSlider.create(R.layout.intro_1, true)
        val fragmentSlide2 = customSlider.create(R.layout.intro_2)
        val fragmentSlide3 = customSlider.create(R.layout.intro_3)
        val fragmentSlide4 = customSlider.create(R.layout.intro_4)
        val fragmentSlide5 = customSlider.create(R.layout.intro_cadastro, last = true)

        addSlides(
            listOf(
                fragmentSlide1,
                fragmentSlide2,
                fragmentSlide3,
                fragmentSlide4,
                fragmentSlide5
            )
        )
    }

    override fun onStart() {
        validateLogin()
        super.onStart()
    }

    fun btnCreateAccount(view: View) {
        CreateAccountActivity.getStaterIntent(this@MainActivity).run(::startActivity)
    }

    fun btnLogin(view: View) {
//        startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        LoginActivity.getStaterIntent(this@MainActivity).run(::startActivity)
    }

    private fun validateLogin() {
        firebaseAuth = FirebaseConfig.getFirebaseAuth()
//        logout()
        if(firebaseAuth.currentUser != null) {
            navigateToPrincipal()
        }
    }

    private fun logout(){
        firebaseAuth = FirebaseConfig.getFirebaseAuth()
        firebaseAuth.signOut()
    }

    private fun navigateToPrincipal() {
        PrincipalActivity.getStaterIntent(this@MainActivity).run(::startActivity)
    }

    companion object {
        fun getStaterIntent(context: Context) =
            Intent(context, MainActivity::class.java)
    }

}