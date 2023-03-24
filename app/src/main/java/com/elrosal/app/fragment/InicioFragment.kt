package com.elrosal.app.fragment

import android.app.Activity
import android.content.Context
import android.graphics.drawable.TransitionDrawable
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
import com.elrosal.app.R
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
        //animacionFoto()
    }

    private fun animacionFoto() {
        val images = listOf(R.drawable.photo_elrosal, R.drawable.photo_25)
        var currentIndex = 0
        val handler = Handler()
        val runnable = object : Runnable {
            override fun run() {
                val nextIndex = (currentIndex + 1) % images.size
                val nextImage = resources.getDrawable(images[nextIndex], null)
                binding.imagenPrincipal.background = nextImage
                currentIndex = nextIndex
                handler.postDelayed(this, 200)
            }
        }

        handler.postDelayed(runnable, 200)
    }

    override fun onDetach() {
        super.onDetach()
        listener=null
    }
//-----------------------------------------------------------------------------------------------------


}