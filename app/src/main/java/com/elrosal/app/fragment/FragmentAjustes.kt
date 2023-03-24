package com.elrosal.app.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.elrosal.app.Administracion
import com.elrosal.app.databinding.FragmentAjustesBinding
import com.elrosal.app.utiles.MainFragmentActionListener

class FragmentAjustes : Fragment() {

    private lateinit var binding: FragmentAjustesBinding
    private var listener: MainFragmentActionListener?=null

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
        binding=FragmentAjustesBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //---------------------------acciones--------------------------
         binding.btnAdministrar.setOnClickListener {
             startActivity(Intent(requireContext(), Administracion::class.java))
         }
         /*binding.btnBuscar.setOnClickListener {
             listener?.cambiarActivityDP()
         }*/
    }

    override fun onDetach() {
        super.onDetach()
        listener=null
    }
}