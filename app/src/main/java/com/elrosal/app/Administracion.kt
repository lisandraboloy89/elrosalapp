package com.elrosal.app

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Base64
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.elrosal.app.administracion.AdminInfoFragment
import com.elrosal.app.administracion.AdminMenuFragment
import com.elrosal.app.api.ApiService
import com.elrosal.app.api.envioDatoMenu
import com.elrosal.app.api.respuestaRegistroMenu
import com.elrosal.app.databinding.ActivityAdministracionBinding
import com.elrosal.app.utiles.AdminFragmentActionListener
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit

class Administracion : AppCompatActivity(),AdminFragmentActionListener {
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
        pasarFragment(AdminMenuFragment())
        binding.btnMenuAtras.setOnClickListener {
            onBackPressed()
        }
        binding.btnAdminInfo.setOnClickListener {
            pasarFragment(AdminInfoFragment())
            binding.btnAdminInfo.setTextColor(ContextCompat.getColor(this, R.color.naranja))
            binding.btnAdminMenu.setTextColor(ContextCompat.getColor(this, R.color.white))
            binding.btnAdminInfo.setBackgroundResource(R.drawable.fondo_boton)
            binding.btnAdminMenu.setBackgroundResource(R.drawable.fondo_boton_n)
        }
        binding.btnAdminMenu.setOnClickListener {
            pasarFragment(AdminMenuFragment())
            binding.btnAdminMenu.setTextColor(ContextCompat.getColor(this, R.color.naranja))
            binding.btnAdminInfo.setTextColor(ContextCompat.getColor(this, R.color.white))
            binding.btnAdminMenu.setBackgroundResource(R.drawable.fondo_boton)
            binding.btnAdminInfo.setBackgroundResource(R.drawable.fondo_boton_n)
        }
    }
    override fun pasarFragment(fragment: Fragment) {
        val fragmnetManger = supportFragmentManager
        val fragmentTransaction = fragmnetManger.beginTransaction()
        fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
        fragmentTransaction.replace(R.id.fragmenContainerAdmin, fragment)
        fragmentTransaction.commit()
    }
}