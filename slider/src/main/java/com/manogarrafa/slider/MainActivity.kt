package com.manogarrafa.slider

import android.os.Bundle
import com.heinrichreimersoftware.materialintro.app.IntroActivity
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide

open class MainActivity : IntroActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        setContentView(R.layout.activity_main)

        isButtonBackVisible = false
        isButtonNextVisible = false
        //Simples
//        val simpleSlide1 =
//            SimpleSlide.Builder().title("Titulo").description("Descrição").image(R.drawable.um)
//                .background(android.R.color.holo_orange_light).build()
//        val simpleSlide2 = SimpleSlide.Builder().title("Titulo 2").description("Descrição 2")
//            .image(R.drawable.dois).background(android.R.color.holo_orange_light).build()
//        val simpleSlide3 = SimpleSlide.Builder().title("Titulo 3").description("Descrição 3")
//            .image(R.drawable.tres).background(android.R.color.holo_orange_light).build()
//        val simpleSlide4 = SimpleSlide.Builder().title("Titulo 4").description("Descrição 4")
//            .image(R.drawable.quatro).background(android.R.color.holo_orange_light).build()
//        addSlides(listOf(simpleSlide1, simpleSlide2, simpleSlide3, simpleSlide4))

        val fragmentSlide1 = FragmentSlide.Builder().background(android.R.color.holo_orange_light)
            .fragment(R.layout.intro_1).build()
        val fragmentSlide2 = FragmentSlide.Builder().background(android.R.color.holo_orange_light)
            .fragment(R.layout.intro_2).build()

        addSlides(listOf(fragmentSlide1, fragmentSlide2))
    }
}