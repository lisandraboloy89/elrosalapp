package com.elrosal.app.adapter

import android.annotation.SuppressLint
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
import com.elrosal.app.cache.menu

class menuAdapter(val respuesta:List<menu>): RecyclerView.Adapter<menuAdapter.ViewHolder>() {
    private lateinit var mlistener:onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(listener:onItemClickListener){
        mlistener=listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): menuAdapter.ViewHolder {
        val layaotInflater: LayoutInflater = LayoutInflater.from(parent.context)
        return menuAdapter.ViewHolder(
            layaotInflater.inflate(R.layout.modelo_menu, parent, false),mlistener
        )
    }
    override fun getItemCount(): Int {
        return respuesta.size
    }
    class ViewHolder(ItemView: View, listener: menuAdapter.onItemClickListener) : RecyclerView.ViewHolder(ItemView) {
        val nombreMenu: TextView = itemView.findViewById(R.id.text_nombre_menu)
        val precioMenu: TextView = itemView.findViewById(R.id.text_precio_menu)
        val gramajeMenu: TextView = itemView.findViewById(R.id.text_gramaje_menu)
        val imagenMenu: ImageView = itemView.findViewById(R.id.img_menu)
        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)

            }
        }
    }
    //----------------------------------Acciones del recycleview---------------------------
    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: menuAdapter.ViewHolder, position: Int) {

        val resp: menu = respuesta[position]
        var tipo_plato=resp.tipo

        //if(tipo_plato.equals("asados")) {
        holder.nombreMenu.setText(resp.nombre.toString())
        holder.precioMenu.setText(resp.precio)
        holder.gramajeMenu.setText(resp.gramaje)


            var bitmapString = resp.muestra
            if (bitmapString.equals("0")) {
            } else {
                val decodedByteArray = Base64.decode(bitmapString, Base64.DEFAULT)
                val decodedBitmap =
                    BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.size)
                holder.imagenMenu.setImageBitmap(decodedBitmap)

            }
       // }
    }
}