package com.elrosal.app.administracion

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Looper
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.elrosal.app.R
import com.elrosal.app.api.ApiService
import com.elrosal.app.api.envioDatoMenu
import com.elrosal.app.api.respuestaRegistroMenu
import com.elrosal.app.databinding.FragmentAdminMenuBinding
import com.elrosal.app.databinding.FragmentAjustesBinding
import com.elrosal.app.utiles.AdminFragmentActionListener
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
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit


class AdminMenuFragment : Fragment() {
    private lateinit var binding:FragmentAdminMenuBinding
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
        binding= FragmentAdminMenuBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //---------------------------acciones--------------------------
        //----------------------------------Agregar el signo de gramo despues de escribir el gramaje---------
        binding.btnAgregarMenu.setOnClickListener {
            AgregarMenuNuevoBD()
        }
        binding.imgMenuAd.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)     //-------obtener imagen de la galeria
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }
        binding.textGramajeAd.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val text = binding.textGramajeAd.text.toString()
                if (text.isNotEmpty() && !text.endsWith("g")) {
                    binding.textGramajeAd.setText("$text g")
                }
            }
        }
        binding.textPrecioAd.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val text = binding.textPrecioAd.text.toString()
                if (text.isNotEmpty() && !text.endsWith("MN")) {
                    binding.textPrecioAd.setText("$text MN")
                }
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener=null
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
    private fun AgregarMenuNuevoBD() {
        //-------convertir la imagen en un Strning------------------------
        val bitmap = (binding.imgMenuAd.drawable as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 5, stream)
        val byteArray = stream.toByteArray()
        val encodedString = Base64.encodeToString(byteArray, Base64.DEFAULT)
        //------------Almacenar la cadena codificada en base64 en una variable de tipo String
        val fotoString = encodedString
        //----
        try {

            CoroutineScope(Dispatchers.IO).launch {
                val call: Response<respuestaRegistroMenu> = getRetrofit().create(ApiService::class.java)
                    .postRegistrarMenu(
                        envioDatoMenu(
                            binding.textNombreAd.text.toString(),
                            binding.textPrecioAd.text.toString(),
                            binding.textGramajeAd.text.toString(),
                            binding.textDescripAd.text.toString(),
                            fotoString,
                            "ensalada",
                            true
                        )

                    )
                val registroPaciente: respuestaRegistroMenu? = call.body()
                if (call.isSuccessful) {
                    Looper.prepare()
                    registrarPacientes(registroPaciente!!)
                    Looper.loop()
                } else {
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
            Toast.makeText(requireContext(),"Error de conexión con el servidor", Toast.LENGTH_SHORT).show()
        }
    }

    private fun registrarPacientes(registroPaciente: respuestaRegistroMenu) {
        Logger.addLogAdapter(AndroidLogAdapter())
        var userID: String = registroPaciente!!.objectId
        Logger.d(userID)
        Toast.makeText(requireContext(),"Palto registrado", Toast.LENGTH_SHORT).show()
    }
    fun ToasDeError(code: String, error: String) {
        Logger.addLogAdapter(AndroidLogAdapter())
        Logger.d("$code $error")
    }
    //------------------Metodo para obtener una foto de la galeria hacia el ImagenView y redimensionarla------------------------
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == 1) {
            val imageUri = data?.data
            binding.imgMenuAd.setImageURI(imageUri)
            val layoutParams = binding.imgMenuAd.layoutParams
            layoutParams.width = 520
            layoutParams.height = 480
            binding.imgMenuAd.layoutParams = layoutParams                   //------establecer tamaño a la imagen
            binding.imgMenuAd.scaleType = ImageView.ScaleType.CENTER_CROP  //------establecer la escala de la imagen
        }
    }
}