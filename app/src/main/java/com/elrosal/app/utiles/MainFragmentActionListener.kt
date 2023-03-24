package com.elrosal.app.utiles

import android.app.Activity
import androidx.fragment.app.Fragment
import com.elrosal.app.Administracion
import com.elrosal.app.api.dato
import java.lang.StringBuilder

interface MainFragmentActionListener {
    fun pasarFragment(fragment: Fragment)    //-------Metodo compartido para pasar a otro fragmento
    fun rellenarRecycleView():dato           //------Metodo para pasar los resultado del Menu de API
}