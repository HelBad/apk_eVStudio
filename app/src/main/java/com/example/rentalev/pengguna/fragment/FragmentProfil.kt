package com.example.rentalev.pengguna.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.example.rentalev.R
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_profil.*

class FragmentProfil : Fragment() {
    lateinit var ref: DatabaseReference
    lateinit var SP: SharedPreferences
    lateinit var alertDialog: AlertDialog.Builder

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ref = FirebaseDatabase.getInstance().getReference("izin")
        SP = requireActivity().applicationContext.getSharedPreferences("Pengguna", Context.MODE_PRIVATE)
        alertDialog = AlertDialog.Builder(requireActivity())

        dataIzin()
    }

    private fun dataIzin() {
        namaProfil.text = SP.getString("nama", "")
        emailProfil.text = SP.getString("email", "")
        telpProfil.text = SP.getString("telp", "")
    }
}