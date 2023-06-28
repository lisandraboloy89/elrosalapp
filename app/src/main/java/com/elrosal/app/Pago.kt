package com.elrosal.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.elrosal.app.administracion.AdminMenuFragment
import com.elrosal.app.databinding.ActivityAdministracionBinding
import com.elrosal.app.databinding.ActivityPagoBinding

class Pago : AppCompatActivity() {

    private lateinit var binding:ActivityPagoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPagoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //-----------------------Pinta barra superior de status de android-------------------------------------
        try {
            window.statusBarColor = ContextCompat.getColor(this, R.color.oscuro)
        }catch (ex:Exception){}

        //-------------------------------------------------------------------------
        //permiso()
        //----------------------Acciones---------------------------------------------
        binding.btnPagoAtras.setOnClickListener {
            onBackPressed()
        }
        binding.btnEnzona.setOnClickListener {

            val packageName="cu.xetid.apk.enzona"
            val intent=packageManager.getLaunchIntentForPackage(packageName)
            if(intent!=null){
                startActivity(intent)
            }else{
                Toast.makeText(this,"No se puede abrir ENZONA",Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnTransfermovil.setOnClickListener {

        }

        //---cu.xetid.apk.enzona
        //---cu.etecsa.cubacel.tr.tm

    }
    fun permiso(){
        val intent=Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
        startActivity(intent)
    }
}