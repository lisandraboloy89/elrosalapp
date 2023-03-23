package com.elrosal.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.elrosal.app.databinding.ActivitySplashScreenBinding

class Splash_screen : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private val tiempo:Long = 5000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        binding= ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //------------------------------------------------------------
        Handler().postDelayed({
                startActivity(Intent(this,MainActivity::class.java))
            finish()
        },tiempo)
    }
}