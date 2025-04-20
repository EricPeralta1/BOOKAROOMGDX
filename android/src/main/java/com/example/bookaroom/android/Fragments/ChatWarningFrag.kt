package com.example.bookaroom.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import com.example.bookaroom.android.Activities.SearchEventActivity
import com.example.bookaroom.R

class ChatWarningFrag : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
                             ): View {
        val view = inflater.inflate(R.layout.fragment_chat_warning, container, false)

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeButton()
    }

    /**
     * Es la sección de aceptar términos del chat, si se aceptan, se puede acceder, sino se
     * devuelve a la pantalla inicial.
     */
    private fun initializeButton() {
        val acceptWarning = view?.findViewById<TextView>(R.id.accept_agreement_bt)!!
        val notAcceptWarning = view?.findViewById<TextView>(R.id.cancel_agreement_bt)!!

        acceptWarning.setOnClickListener {
            val fragmentView = activity?.findViewById<FragmentContainerView>(R.id.chat_contract)
            fragmentView?.visibility = View.GONE
            val shade = activity?.findViewById<View>(R.id.shadowBgChat)
            shade?.visibility = View.GONE
        }

        notAcceptWarning.setOnClickListener {
            val intent = Intent(requireContext(), SearchEventActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }
}
