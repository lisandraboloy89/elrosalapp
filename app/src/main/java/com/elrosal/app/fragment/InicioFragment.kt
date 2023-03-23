package com.elrosal.app.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.elrosal.app.databinding.FragmentInicioBinding
import com.elrosal.app.utiles.MainFragmentActionListener


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
       /* binding.btnPacientes.setOnClickListener {
            listener?.cambiarActivityPaciente()
        }
        binding.btnBuscar.setOnClickListener {
            listener?.cambiarActivityDP()
        }*/
    }

    override fun onDetach() {
        super.onDetach()
        listener=null
    }

}