package com.elrosal.app.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.elrosal.app.R
import com.elrosal.app.api.respuestaMenu
import com.elrosal.app.cache.pedido

class pedidoAdapter(val respuesta:List<pedido>): RecyclerView.Adapter<pedidoAdapter.ViewHolder>() {
    private lateinit var mlistener:onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(listener:onItemClickListener){
        mlistener=listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): pedidoAdapter.ViewHolder {
        val layaotInflater: LayoutInflater = LayoutInflater.from(parent.context)
        return pedidoAdapter.ViewHolder(
            layaotInflater.inflate(R.layout.modelo_pedido, parent, false),mlistener
        )
    }
    override fun getItemCount(): Int {
        return respuesta.size
    }
    class ViewHolder(ItemView: View, listener: pedidoAdapter.onItemClickListener) : RecyclerView.ViewHolder(ItemView) {
        val nombrePedido: TextView = itemView.findViewById(R.id.text_nombre_pedido)
        val precioPedido: TextView = itemView.findViewById(R.id.text_precio_pedido)
        val cantidadPedido: TextView = itemView.findViewById(R.id.text_cantidad_pedido)
        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)

            }
        }
    }
    //----------------------------------Acciones del recycleview---------------------------
    override fun onBindViewHolder(holder: pedidoAdapter.ViewHolder, position: Int) {

        val resp: pedido = respuesta[position]
        holder.nombrePedido.setText(resp.producto)
        holder.precioPedido.setText(resp.precio)
        holder.cantidadPedido.setText(resp.cantidad)

    }
}