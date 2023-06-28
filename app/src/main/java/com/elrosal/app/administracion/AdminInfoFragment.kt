package com.elrosal.app.administracion

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.room.Room
import com.elrosal.app.R
import com.elrosal.app.api.ApiService
import com.elrosal.app.api.datosGenerales
import com.elrosal.app.api.envioDatosGenerales
import com.elrosal.app.api.pulldatosGenerales
import com.elrosal.app.cache.cacheDB
import com.elrosal.app.cache.general
import com.elrosal.app.databinding.FragmentAdminInfoBinding
import com.elrosal.app.databinding.FragmentAdminMenuBinding
import com.elrosal.app.utiles.AdminFragmentActionListener
import com.google.android.material.snackbar.Snackbar
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


class AdminInfoFragment : Fragment() {
    private lateinit var binding:FragmentAdminInfoBinding
    private var listener: AdminFragmentActionListener?=null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is AdminFragmentActionListener){
            listener=context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentAdminInfoBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //---------------------------acciones--------------------------
        obtenerDatosCahe()
        binding.btnGuardarInfo.setOnClickListener {
            comprobar_conexion()
        }
    }
    override fun onDetach() {
        super.onDetach()
        listener=null
    }
    //-----------------------------------------------Cache de Datos-----------------------------------
    private fun obtenerDatosCahe() {

        var dataBase: cacheDB = Room
            .databaseBuilder(requireContext(), cacheDB::class.java, cacheDB.DATABASE_NAME)
            .build()
        CoroutineScope(Dispatchers.IO).launch {
            var listaDatosGenerales = dataBase.generalDao().getListaInfoAll()
            Looper.prepare()
            cargarDatos(listaDatosGenerales!!)
            Looper.loop()
        }
    }

    private fun cargarDatos(listaDatosGenerales: List<general>) {
        binding.textCelAd.setText(listaDatosGenerales[0].telf_celular)
        binding.textFijoAd.setText(listaDatosGenerales[0].telf_fijo)
        binding.textHoraInicioAd.setText(listaDatosGenerales[0].horarioInicio)
        binding.textHoraCierreAd.setText(listaDatosGenerales[0].horarioCierre)
        binding.textServicioAd.setText(listaDatosGenerales[0].servicios)
        binding.textDeireccionAd.setText(listaDatosGenerales[0].direccion)
        binding.textInfoAd.setText(listaDatosGenerales[0].info)
        binding.textCuentaAd.setText(listaDatosGenerales[0].tarjetaPago)
    }
    //----------------------------Conexion con API--------------------------------------------
    fun comprobar_conexion(){
        val connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo

        //--------verifica la conexion de internet para continuar-----------------------------
        if (networkInfo != null && networkInfo.isConnected) {
            modificarDatosIniciales()
        } else {
            Toast.makeText(requireContext(),"No hay conexión a Internet", Toast.LENGTH_SHORT).show()
        }
    }
    //-------------------------------Hacer pedido a API en Hilo secundario------------------------------------
    private fun modificarDatosIniciales() {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val call: Response<pulldatosGenerales> = getRetrofit().create(ApiService::class.java)
                    .putModificarDatosG(envioDatosGenerales(
                        binding.textDeireccionAd.text.toString(),
                        binding.textHoraInicioAd.text.toString(),
                        binding.textHoraCierreAd.text.toString(),
                        binding.textServicioAd.text.toString(),
                        binding.textCelAd.text.toString(),
                        binding.textCuentaAd.text.toString(),
                        binding.textInfoAd.text.toString(),
                        binding.textFijoAd.text.toString()
                    ))
                val listaDatos: pulldatosGenerales? = call.body()
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
    private fun listarDatosGeneral(listaDatos: pulldatosGenerales) {
        Logger.addLogAdapter(AndroidLogAdapter())
        var userID: String = listaDatos!!.results
        Logger.d(userID)
        Logger.d(listaDatos?.results)
        Log.d("SERVIDOR", listaDatos?.results.toString())
        Toast.makeText(requireContext(), "Cambios realizados", Toast.LENGTH_SHORT).show()
        //guardarTodaListaInfoGeneral(listaDatos.results)
    }

    //----------Mostrar errores en las respuestas de API------------------------
    fun ToasDeError(code: String, error: String) {
        Logger.addLogAdapter(AndroidLogAdapter())
        Logger.d("$code $error")
    }
}