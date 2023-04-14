package com.elrosal.app.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.elrosal.app.R
import com.elrosal.app.adapter.menuAdapter
import com.elrosal.app.adapter.pedidoAdapter
import com.elrosal.app.api.respuestaMenu
import com.elrosal.app.cache.cacheDB
import com.elrosal.app.cache.pedido
import com.elrosal.app.databinding.FragmentCarritoBinding
import com.elrosal.app.databinding.FragmentMenuBinding
import com.elrosal.app.utiles.MainFragmentActionListener
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FragmentCarrito : Fragment() {

    private lateinit var binding: FragmentCarritoBinding
    private var listener: MainFragmentActionListener? = null
    lateinit var listaPedidos: List<pedido>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainFragmentActionListener) {
            listener = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCarritoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //---------------------------acciones--------------------------
        comprobar_pedido()
        binding.btnBorrarPedido.setOnClickListener {
            var dataBase: cacheDB = Room
                .databaseBuilder(requireContext(), cacheDB::class.java, cacheDB.DATABASE_NAME)
                .build()
            CoroutineScope(Dispatchers.IO).launch {
                dataBase.pedidoDao().allTableDelete()
                comprobar_pedido()
                            }
        }
    }

    private fun comprobar_pedido() {
        var dataBase: cacheDB = Room
            .databaseBuilder(requireContext(), cacheDB::class.java, cacheDB.DATABASE_NAME)
            .build()
        CoroutineScope(Dispatchers.IO).launch {
            listaPedidos = dataBase.pedidoDao().getListaPedidosAll()
            rellenarRecycleView()
        }
    }

    private fun rellenarRecycleView() {
        Log.d("SERVIDOR", listaPedidos.toString())
        activity?.runOnUiThread(java.lang.Runnable {
            Handler().postDelayed({
                //binding.animationEspera.visibility=View.GONE
                try {
                    //binding.textoError.visibility=View.GONE
                    val recycle_area: RecyclerView = binding.recyclePedido
                    var adapter = pedidoAdapter(listaPedidos)
                    recycle_area.layoutManager = LinearLayoutManager(
                        requireContext(),
                        LinearLayoutManager.VERTICAL, false
                    )
                    recycle_area.adapter = adapter
                    adapter!!.setOnItemClickListener(object : pedidoAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {
                            //----------------------------------------
                        }
                    })
                } catch (Ex: Exception) {
                    // binding.textoError.visibility=View.VISIBLE
                }

            }, 1000)
        })
        Logger.addLogAdapter(AndroidLogAdapter())
        Logger.d(listaPedidos);
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

}