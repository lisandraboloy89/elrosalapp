package com.elrosal.app

import android.app.Activity
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.room.Room
import com.elrosal.app.api.ApiService
import com.elrosal.app.api.dataGenerales
import com.elrosal.app.api.dato
import com.elrosal.app.api.datosGenerales
import com.elrosal.app.cache.cacheDB
import com.elrosal.app.cache.general
import com.elrosal.app.cache.generalDao
import com.elrosal.app.databinding.ActivitySplashScreenBinding
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class Splash_screen : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private val tiempo:Long = 5000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //-------------------------Ajustes Iniciales----------------------------------
        binding.animationSplash.playAnimation()
        carga_datosIniciales()
        //------------------------------------------------------------
       /* Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, tiempo)*/
    }

    fun carga_datosIniciales() {
        comprobar_conexion()
    }
    //----------------------------Conexion con API--------------------------------------------
    fun comprobar_conexion(){
        val connectivityManager =
            getSystemService(Activity.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo

        //--------verifica la conexion de internet para continuar-----------------------------
        if (networkInfo != null && networkInfo.isConnected) {
            obtenerDatosIniciales()
        } else {
            //toastExt("Active el Internet para seguir")
            //mensajeDialog.startMenssageDialogo("Active el Internet para seguir")
        }
    }
    //-------------------------------Hacer pedido a API en Hilo secundario------------------------------------
    private fun obtenerDatosIniciales() {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val call: Response<datosGenerales> = getRetrofit().create(ApiService::class.java)
                    .getObtenerDatosG()
                val listaDatos: datosGenerales? = call.body()
                if (call.isSuccessful) {
                    Looper.prepare()
                    listarDatosGeneral(listaDatos!!)  //-------------Pasar Respuesta obtenido a metodo
                    Looper.loop()
                } else { //--------------------Respuesta Error------------------------------
                    if (call.code() == 400 || call.code() == 404 ) {
                        var jsonObject: JSONObject? = null
                        try {
                            jsonObject = JSONObject(call.errorBody()?.string())
                            val Codigo = jsonObject!!.getString("code")
                            val Errors = jsonObject!!.getString("error")
                            Looper.prepare()
                            ToasDeError(Codigo,Errors)
                            Looper.loop()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                }

            }
        } catch (e: Exception) {
            //toastExt("Error de conexión con el servidor")
        }
    }

    ////------------------API-RESET-Retrofit------------------------------------------/////
    private fun getRetrofit(): Retrofit {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES) // write timeout
            .readTimeout(1, TimeUnit.MINUTES) // read timeout
            .addInterceptor(logging)
            .build()
        return Retrofit.Builder()
            .baseUrl("https://parseapi.back4app.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }
    //----------------Procesar los resusltados de la API-----------------------
    private fun listarDatosGeneral(listaDatos: datosGenerales) {
        Logger.addLogAdapter(AndroidLogAdapter())
        var userID: String = listaDatos!!.results[0].objectId
        Logger.d(userID)
        Logger.d(listaDatos?.results)
        Log.d("SERVIDOR", listaDatos?.results.toString())
        guardarTodaListaInfoGeneral(listaDatos.results)
    }

    //----------Mostrar errores en las respuestas de API------------------------
    fun ToasDeError(code: String, error: String) {
        Logger.addLogAdapter(AndroidLogAdapter())
        Logger.d("$code $error")
    }
    //--------------------Guardar todos los datos del server----------------------------
    fun guardarTodaListaInfoGeneral(listaDatosGenerales:List<dataGenerales>?){
        var dataBase: cacheDB  = Room
            .databaseBuilder(this, cacheDB::class.java, cacheDB.DATABASE_NAME)
            .build()
        CoroutineScope(Dispatchers.IO).launch {
            dataBase.generalDao().allTableDeleteInfoGeneral()
            for (i in listaDatosGenerales!!.indices) {
                val datos = general(
                    listaDatosGenerales!![i].objectId,
                    listaDatosGenerales!![i].info,
                    listaDatosGenerales!![i].hora_inicio,
                    listaDatosGenerales!![i].hora_cierre,
                    listaDatosGenerales!![i].fijo,
                    listaDatosGenerales!![i].telefono,
                    listaDatosGenerales!![i].direccion,
                    listaDatosGenerales!![i].servicios,
                    listaDatosGenerales!![i].numero_pago
                )
                dataBase.generalDao().insertInfoGeneral(datos)
            }
            withContext(Dispatchers.Main) {
                accionInicio()
            }

        }

    }
    fun accionInicio(){
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}