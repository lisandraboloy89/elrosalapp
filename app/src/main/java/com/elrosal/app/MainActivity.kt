package com.elrosal.app

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.elrosal.app.api.ApiService
import com.elrosal.app.api.dato
import com.elrosal.app.databinding.ActivityMainBinding
import com.elrosal.app.fragment.FragmentAjustes
import com.elrosal.app.fragment.FragmentCarrito
import com.elrosal.app.fragment.FragmentMenu
import com.elrosal.app.fragment.InicioFragment
import com.elrosal.app.utiles.MainFragmentActionListener
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
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), MainFragmentActionListener {

    private lateinit var binding:ActivityMainBinding
    lateinit var slideInLeft:Animation
    lateinit var hideInLeft:Animation
    private var lista_resp: dato? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //--------------------Definición de variable iniciales------------------------------------
        slideInLeft = AnimationUtils.loadAnimation(this, R.anim.slide_in_left)
        hideInLeft = AnimationUtils.loadAnimation(this, R.anim.hide_in_left)
        //-----------------------Ajustes iniciales-----------------------------
        //comprobar_conexion()   //------Obtener Datos de la API
        pasarFragment(InicioFragment())                     //---------------Iniciar fragmento--------
        binding.barraMenuIzquierdo.visibility= View.GONE    //---------------Oculta barra al iniciar activity
        //-----------------------Pinta barra superior de status de android-------------------------------------
        try {
            window.statusBarColor = ContextCompat.getColor(this, R.color.oscuro)
        }catch (ex:Exception){}
        //---------------------Muestra Barra de menu izquierda-------------------------------------
        binding.btnMenuPrincipal.setOnClickListener {
            if(binding.barraMenuIzquierdo.visibility==View.VISIBLE){
               ocultar_barraMenuIzquirda()
            }else {
               mostrar_barraMenuIzquirda()
            }
        }
        //--------------------Acciones de los botones de la barra de menu izquierdo------
        binding.btnAjustes.setOnClickListener {
            binding.textoTitulosBarra.setText("Ajustes")
            pasarFragment(FragmentAjustes())             //---------------Fragmento ajustes--------
            ocultar_barraMenuIzquirda()
        }
        binding.btnPago.setOnClickListener {
            ocultar_barraMenuIzquirda()
        }
        binding.btnBebida.setOnClickListener {
            ocultar_barraMenuIzquirda()
        }
        binding.btnCompraCarro.setOnClickListener {
            binding.textoTitulosBarra.setText("Lista de compra")
            pasarFragment(FragmentCarrito())
            ocultar_barraMenuIzquirda()
        }
        binding.btnMenucomida.setOnClickListener {
            binding.textoTitulosBarra.setText("Menú ofertado")
            pasarFragment(FragmentMenu())
            ocultar_barraMenuIzquirda()
        }
        binding.imgIncio.setOnClickListener {
            binding.textoTitulosBarra.setText("El Rosal")
            pasarFragment(InicioFragment())             //---------------Fragmento inicio--------
            ocultar_barraMenuIzquirda()
        }

    }
    //-------------------Mostrar y ocultar barra de menu----------------------------------------------
    private fun ocultar_barraMenuIzquirda(){
        binding.barraMenuIzquierdo.startAnimation(hideInLeft)    //-----animacion
        binding.barraMenuIzquierdo.visibility= View.GONE         //-----ocultar barra
        binding.btnMenuPrincipal.background.setTint(ContextCompat.getColor(this, R.color.white))
    }
    private fun mostrar_barraMenuIzquirda(){
        binding.barraMenuIzquierdo.visibility = View.VISIBLE       //-----mostrar barra
        binding.barraMenuIzquierdo.startAnimation(slideInLeft)    //------animacion
        binding.btnMenuPrincipal.background.setTint(ContextCompat.getColor(this, R.color.naranja))
    }
    //---------------Presionar la tecla atras para salir---------------------------
    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setMessage("¿Desea salir de la aplicación?")
            .setCancelable(false)
            .setPositiveButton("Si"){
                    dialog,whichButton ->
                finishAffinity()
            }
            .setNegativeButton("No"){
                    dialog,whichButton ->

            }
            .show()
    }
//-----------------Funciones compatidas con los fragmetos-------------------
    override fun pasarFragment(fragment: Fragment) {  //--------Metodo para cambiar entre fragment
    val fragmnetManger = supportFragmentManager
    val fragmentTransaction = fragmnetManger.beginTransaction()
    fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
    fragmentTransaction.replace(R.id.fragmenContainer, fragment)
    fragmentTransaction.commit()
    }

    override fun rellenarRecycleView(): dato {
        TODO("Not yet implemented")
    }


}