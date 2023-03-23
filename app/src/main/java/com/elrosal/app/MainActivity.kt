package com.elrosal.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import com.elrosal.app.databinding.ActivityMainBinding
import com.elrosal.app.databinding.ActivitySplashScreenBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.barraMenuIzquierdo.visibility= View.GONE
        val slideInLeft = AnimationUtils.loadAnimation(this, R.anim.slide_in_left)
        val hideInLeft = AnimationUtils.loadAnimation(this, R.anim.hide_in_left)
        binding.barraMenuIzquierdo.startAnimation(slideInLeft)
        //------------------------------------------------------------
        try {
            window.statusBarColor = ContextCompat.getColor(this, R.color.oscuro)
        }catch (ex:Exception){}
        //--------------------------------------------------------------
        binding.btnMenuPrincipal.setOnClickListener {
            if(binding.barraMenuIzquierdo.visibility==View.VISIBLE){
                binding.barraMenuIzquierdo.startAnimation(hideInLeft)
                binding.barraMenuIzquierdo.visibility= View.GONE
            }else {
                binding.barraMenuIzquierdo.visibility = View.VISIBLE
                binding.barraMenuIzquierdo.startAnimation(slideInLeft)
            }
        }

    }
}