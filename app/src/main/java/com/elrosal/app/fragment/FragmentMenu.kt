package com.elrosal.app.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import android.text.InputType
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.elrosal.app.adapter.menuAdapter
import com.elrosal.app.api.ApiService
import com.elrosal.app.api.dato
import com.elrosal.app.api.respuestaMenu
import com.elrosal.app.cache.cacheDB
import com.elrosal.app.cache.menu
import com.elrosal.app.cache.pedido
import com.elrosal.app.databinding.FragmentMenuBinding
import com.elrosal.app.utiles.MainFragmentActionListener
import com.google.android.material.snackbar.Snackbar
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
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class FragmentMenu : Fragment() {

    private lateinit var binding: FragmentMenuBinding
    private var listener: MainFragmentActionListener?=null
    private lateinit var lista_resp: dato
    var nombre:String=""
    var precio:String=""
    var cantidad:Int=0

    var c: Calendar = Calendar.getInstance()
    var sdf: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
    var hora: SimpleDateFormat = SimpleDateFormat("HH:mm:ss")
    var capturarFecha: String = sdf.format(c.getTime())
    var capturarHora: String = hora.format(c.getTime())

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
        binding=FragmentMenuBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //---------------------------acciones--------------------------
        comprobar_conexion()
        //animacionFoto()
    }
    //-------------------------Metodo para rellenar el recycleview con los datos del menu---------------
    private fun rellenarRecycleView(results: List<menu>) {
        activity?.runOnUiThread(java.lang.Runnable {
            Handler().postDelayed({
                //binding.animationEspera.visibility=View.GONE
                try {
                    //binding.textoError.visibility=View.GONE
                    val recycle_area: RecyclerView = binding.recycleMenu1
                    var listaFiltrada=results.filter { it.tipo=="asados"}
                    var adapter = menuAdapter(listaFiltrada)
                    recycle_area.layoutManager = LinearLayoutManager(requireContext(),
                        LinearLayoutManager.HORIZONTAL, false)
                    recycle_area.adapter = adapter
                    adapter!!.setOnItemClickListener(object: menuAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            nombre=listaFiltrada[position].nombre.toString()
                            precio=listaFiltrada[position].precio.toString()
                            pedirCantidadAletDialogo()
                        }


                    })
                }catch (Ex:Exception){
                }

            }, 10)
        })
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
            usarBDInternaMenu()
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
                    withContext(Dispatchers.Main) {
                        listarMenu(listaMenu!!)  //-------------Pasar Respuesta obtenido a metodo
                    }
                } else { //--------------------Respuesta Error------------------------------
                    if (call.code() == 400 || call.code() == 404 ) {
                        var jsonObject: JSONObject? = null
                        try {
                            jsonObject = JSONObject(call.errorBody()?.string())
                            val Codigo = jsonObject!!.getString("code")
                            val Errors = jsonObject!!.getString("error")
                            withContext(Dispatchers.Main) {
                                ToasDeError(Codigo, Errors)
                            }
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
        Log.d("SERVIDOR", listaMenu?.results.toString());
        lista_resp=listaMenu
        guardarTodaListaMenu(listaMenu.results)

        binding.textoFechaDatos.setText("$capturarFecha a las $capturarHora")
        val shareprefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val editor = shareprefs.edit()
        editor.putString("fechaUpdate_key", "$capturarFecha a las $capturarHora")
        editor.apply()
    }

    //----------Mostrar errores en las respuestas de API------------------------
    fun ToasDeError(code: String, error: String) {
        Logger.addLogAdapter(AndroidLogAdapter())
        Logger.d("$code $error")
    }
    //----------------Dialogo de pedit cantidad de producto al usaurio-----------
    fun pedirCantidadAletDialogo() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("¿Que cantidad desea?")

        val input = EditText(requireContext())
        input.inputType = InputType.TYPE_CLASS_NUMBER
        val params = LinearLayout.LayoutParams(100, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.gravity = Gravity.CENTER
        input.layoutParams = params
        input.setText("1")
        builder.setView(input)
        builder.setCancelable(false)
        builder.setPositiveButton("Aceptar") { dialog, which ->
            cantidad = input.text.toString().toInt()
            //if (cantidad != 0) {
                realiarPedidoBD()
           // }
        }

        builder.setNegativeButton("Cancelar") { dialog, which ->
            dialog.dismiss()
        }

        builder.show()
    }
    fun usarBDInternaMenu(){
        var dataBase: cacheDB = Room
            .databaseBuilder(requireContext(), cacheDB::class.java, cacheDB.DATABASE_NAME)
            .build()
        CoroutineScope(Dispatchers.IO).launch {
            var listaMenuBd = dataBase.menuDao().getListaMenuAll()
            withContext(Dispatchers.Main) {
                rellenarRecycleViewBebidas(listaMenuBd)
                rellenarRecycleView(listaMenuBd)
                rellenarRecycleViewGuarniciones(listaMenuBd)
                rellenarRecycleViewEnsalada(listaMenuBd)
                val shareprefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
                var fechaUpdate = shareprefs.getString("fechaUpdate_key", "0/0/0")!!
                binding.textoFechaDatos.setText(fechaUpdate)
            }
        }
    }
//------------------Guardar el pedido en la base de datos interna cache-----------------------------------
    private fun realiarPedidoBD() {

        var dataBase: cacheDB = Room
            .databaseBuilder(requireContext(), cacheDB::class.java, cacheDB.DATABASE_NAME)
            .build()
        CoroutineScope(Dispatchers.IO).launch {
           dataBase.pedidoDao().insertPedido(pedido(producto=nombre, cantidad=cantidad.toString(), precio=precio))
            withContext(Dispatchers.Main) {
                val snackbar = view?.let { Snackbar.make(it, "Pedido agregado", Snackbar.LENGTH_LONG) }
                if (snackbar != null) {
                    snackbar.setAction("Mostrar pedidos") {
                        listener?.pasarFragment(FragmentCarrito())
                    }
                }
                if (snackbar != null) {
                    snackbar.show()
                }
            }
        }
    }
    //----------------------------Rellenar la lista de bebidas-----------------------------------------
    private fun rellenarRecycleViewBebidas(results: List<menu>) {
        activity?.runOnUiThread(java.lang.Runnable {
            Handler().postDelayed({
                //binding.animationEspera.visibility=View.GONE
                try {
                    //binding.textoError.visibility=View.GONE
                    val recycle_area: RecyclerView = binding.recycleMenuBebidas
                   var listaFiltrada1=results.filter { it.tipo=="bebida"}
                    var adapter = menuAdapter(listaFiltrada1)
                    recycle_area.layoutManager = LinearLayoutManager(requireContext(),
                        LinearLayoutManager.HORIZONTAL, false)
                    recycle_area.adapter = adapter
                    adapter!!.setOnItemClickListener(object: menuAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            nombre=listaFiltrada1[position].nombre.toString()
                            precio=listaFiltrada1[position].precio.toString()
                            pedirCantidadAletDialogo()
                        }
                    })
                }catch (Ex:Exception){
                    // binding.textoError.visibility=View.VISIBLE
                }

            }, 10)
        })

    }
    private fun rellenarRecycleViewGuarniciones(results: List<menu>) {
        activity?.runOnUiThread(java.lang.Runnable {
            Handler().postDelayed({
                //binding.animationEspera.visibility=View.GONE
                try {
                    //binding.textoError.visibility=View.GONE
                    val recycle_area: RecyclerView = binding.recycleMenuGua
                    var listaFiltrada=results.filter { it.tipo=="guarnición"}
                    var adapter = menuAdapter(listaFiltrada)
                    recycle_area.layoutManager = LinearLayoutManager(requireContext(),
                        LinearLayoutManager.HORIZONTAL, false)
                    recycle_area.adapter = adapter
                    adapter!!.setOnItemClickListener(object: menuAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            nombre=listaFiltrada[position].nombre.toString()
                            precio=listaFiltrada[position].precio.toString()
                            pedirCantidadAletDialogo()
                        }
                    })
                }catch (Ex:Exception){
                    // binding.textoError.visibility=View.VISIBLE
                }

            }, 10)
        })

    }
    private fun rellenarRecycleViewEnsalada(results: List<menu>) {
        activity?.runOnUiThread(java.lang.Runnable {
            Handler().postDelayed({
                //binding.animationEspera.visibility=View.GONE
                try {
                    //binding.textoError.visibility=View.GONE
                    val recycle_area: RecyclerView = binding.recycleMenuEnsalada
                    var listaFiltrada=results.filter { it.tipo=="ensalada"}
                    var adapter = menuAdapter(listaFiltrada)
                    recycle_area.layoutManager = LinearLayoutManager(requireContext(),
                        LinearLayoutManager.HORIZONTAL, false)
                    recycle_area.adapter = adapter
                    adapter!!.setOnItemClickListener(object: menuAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            nombre=listaFiltrada[position].nombre.toString()
                            precio=listaFiltrada[position].precio.toString()
                            pedirCantidadAletDialogo()
                        }
                    })
                }catch (Ex:Exception){
                    // binding.textoError.visibility=View.VISIBLE
                }

            }, 10)
        })

    }
    //--------------------Guardar todos los datos del server----------------------------
    fun guardarTodaListaMenu(listaMenu:List<respuestaMenu>?){
        var dataBase: cacheDB  = Room
            .databaseBuilder(requireContext(), cacheDB::class.java, cacheDB.DATABASE_NAME)
            .build()
        CoroutineScope(Dispatchers.IO).launch {
            dataBase.menuDao().allTableDeleteMenu()
            for (i in listaMenu!!.indices) {
                val datos = menu(
                    listaMenu!![i].objectId,
                    listaMenu!![i].nombre,
                    listaMenu!![i].precio,
                    listaMenu!![i].descripcion,
                    listaMenu!![i].grmaje,
                    listaMenu!![i].estado,
                    listaMenu!![i].muestraFoto,
                    listaMenu!![i].tipo,
                )
                dataBase.menuDao().insertMenu(datos)
            }
            withContext(Dispatchers.Main) {
               // accionInicio()
                usarBDInternaMenu()
            }

        }
    }
}