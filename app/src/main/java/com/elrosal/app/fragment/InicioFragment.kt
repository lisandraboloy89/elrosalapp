package com.elrosal.app.fragment

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elrosal.app.adapter.menuAdapter
import com.elrosal.app.api.ApiService
import com.elrosal.app.api.dato
import com.elrosal.app.api.respuestaMenu
import com.elrosal.app.databinding.FragmentInicioBinding
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


class InicioFragment : Fragment() {

    private lateinit var binding:FragmentInicioBinding
    private var listener:MainFragmentActionListener?=null
    private lateinit var lista_resp: dato
    //lateinit var adapter: menuAdapter


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is MainFragmentActionListener){
            listener=context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentInicioBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //---------------------------acciones--------------------------
        comprobar_conexion()
        //rellenarRecycleView()
       /* binding.btnPacientes.setOnClickListener {
            listener?.cambiarActivityPaciente()
        }
        binding.btnBuscar.setOnClickListener {
            listener?.cambiarActivityDP()
        }*/
    }
    private fun rellenarRecycleView(results: List<respuestaMenu>) {
        activity?.runOnUiThread(java.lang.Runnable {
            Handler().postDelayed({
                //binding.animationEspera.visibility=View.GONE
                try {
                    //binding.textoError.visibility=View.GONE
                    val recycle_area: RecyclerView = binding.recycleMenu
                     var adapter = menuAdapter(results)
                    recycle_area.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL, false)
                    recycle_area.adapter = adapter
                    adapter!!.setOnItemClickListener(object: menuAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            //var nombre=lista_resp[position].nombre.toString()
                            //var rc=lista_resp[position].valor_rc.toString()
                        }
                    })
                }catch (Ex:Exception){
                    // binding.textoError.visibility=View.VISIBLE
                }

            }, 1000)
        })
        Logger.addLogAdapter(AndroidLogAdapter())
        Logger.d(lista_resp);
        }



    override fun onDetach() {
        super.onDetach()
        listener=null
    }
//-----------------------------------------------------------------------------------------------------
//----------------------------Conexion con API--------------------------------------------
fun comprobar_conexion(){
    val connectivityManager =
        context?.getSystemService(Activity.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo

    //--------verifica la conexion de internet para continuar-----------------------------
    if (networkInfo != null && networkInfo.isConnected) {
        obtenerListaMenu()
    } else {
        //toastExt("Active el Internet para seguir")
        //mensajeDialog.startMenssageDialogo("Active el Internet para seguir")
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
    //-------------------------------Hacer pedido a API en Hilo secundario------------------------------------
    private fun obtenerListaMenu() {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val call: Response<dato> = getRetrofit().create(ApiService::class.java)
                    .getObtenerMenu()
                val listaMenu: dato? = call.body()
                if (call.isSuccessful) {
                    Looper.prepare()
                    listarMenu(listaMenu!!)  //-------------Pasar Respuesta obtenido a metodo
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
    //----------------Procesar los resusltados de la API-----------------------
    private fun listarMenu(listaMenu: dato) {
        Logger.addLogAdapter(AndroidLogAdapter())
        var userID: String = listaMenu!!.results[0].objectId
        Logger.d(userID)
        Logger.d(listaMenu?.results)
        lista_resp=listaMenu
        rellenarRecycleView(listaMenu?.results)
    }

    //----------Mostrar errores en las respuestas de API------------------------
    fun ToasDeError(code: String, error: String) {
        Logger.addLogAdapter(AndroidLogAdapter())
        Logger.d("$code $error")
    }
}