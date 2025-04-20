package com.example.bookaroom.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.bookaroom.R

/**
 * Vista por defecto del fragment de inventario.
 */
class EmptyInventoryFrag : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
                             ): View {
        val view = inflater.inflate(R.layout.fragment_no_inventory, container, false)

        return view
    }
}
