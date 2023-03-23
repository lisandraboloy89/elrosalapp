package com.elrosal.app.utiles

import androidx.fragment.app.Fragment
import java.lang.StringBuilder

interface MainFragmentActionListener {
    fun pasarFragment(fragment: Fragment)
    fun cambiarActivity()
}