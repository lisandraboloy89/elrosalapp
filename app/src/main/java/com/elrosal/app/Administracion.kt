package com.elrosal.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.elrosal.app.databinding.ActivityAdministracionBinding
import com.elrosal.app.databinding.ActivityMainBinding

class Administracion : AppCompatActivity() {
    private lateinit var binding:ActivityAdministracionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdministracionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //-----------------------Pinta barra superior de status de android-------------------------------------
        try {
            window.statusBarColor = ContextCompat.getColor(this, R.color.oscuro)
        }catch (ex:Exception){}
        //----------------------Acciones---------------------------------------------
        binding.btnMenuAtras.setOnClickListener {
            onBackPressed()
        }
        binding.btnAgregarMenu.setOnClickListener {
            AgregarMenuNuevoBD()
        }
    }

    private fun AgregarMenuNuevoBD() {

    }

}