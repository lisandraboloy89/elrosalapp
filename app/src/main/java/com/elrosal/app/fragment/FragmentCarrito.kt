package com.elrosal.app.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.elrosal.app.Pago
import com.elrosal.app.adapter.pedidoAdapter
import com.elrosal.app.cache.cacheDB
import com.elrosal.app.cache.menu
import com.elrosal.app.cache.pedido
import com.elrosal.app.databinding.FragmentCarritoBinding
import com.elrosal.app.utiles.MainFragmentActionListener
import com.google.android.material.snackbar.Snackbar
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
        binding.btnPagar.setOnClickListener {
            startActivity(Intent(activity, Pago::class.java))
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
                    caluclarCuentaTotal(listaPedidos)
                    val recycle_area: RecyclerView = binding.recyclePedido
                    var adapter = pedidoAdapter(listaPedidos)
                    recycle_area.layoutManager = LinearLayoutManager(
                        requireContext(),
                        LinearLayoutManager.VERTICAL, false
                    )
                    recycle_area.adapter = adapter
                    adapter!!.setOnItemClickListener(object : pedidoAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {
                            eliminarPedido(listaPedidos[position].IdPedido)
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
    //--------calcula el monto total de los pedidos----------------------------
    fun caluclarCuentaTotal(listaPedidos: List<pedido>) {
        var total:Float= 0.0F
        var aux:String=""

        for (i in listaPedidos!!.indices){
            aux=listaPedidos[i].precio.dropLast(2)
            total+=listaPedidos[i].cantidad.toInt() * aux.toFloat()
        }
        binding.textoTotalCuetna.setText("$total MN")
    }
    //-----------elimina un pedido--------------------------
    fun eliminarPedido(id:Int){
        var dataBase: cacheDB = Room
            .databaseBuilder(requireContext(), cacheDB::class.java, cacheDB.DATABASE_NAME)
            .build()
        CoroutineScope(Dispatchers.IO).launch {
            dataBase.pedidoDao().borrarPedido(id)
            comprobar_pedido()
        }
        Toast.makeText(requireContext(), "Pedido eliminado", Toast.LENGTH_SHORT).show()
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

}