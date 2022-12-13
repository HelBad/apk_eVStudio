package com.example.rentalev.pengguna

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import com.example.rentalev.R
import kotlinx.android.synthetic.main.activity_profil.*
import kotlinx.android.synthetic.main.fragment_profil.*

class ActivityProfil : AppCompatActivity() {
    lateinit var SP: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)

        SP = applicationContext.getSharedPreferences("Pengguna", Context.MODE_PRIVATE)
        dataProfil()
        btnProfil.setOnClickListener {}
    }

    private fun dataProfil() {
        namaProfil.text = Editable.Factory.getInstance().newEditable(SP.getString("nama", ""))
        emailProfil.text = Editable.Factory.getInstance().newEditable(SP.getString("email", ""))
        telpProfil.text = Editable.Factory.getInstance().newEditable(SP.getString("telp", ""))
        nikProfil.text = Editable.Factory.getInstance().newEditable(SP.getString("nik", ""))
        tempatProfil.text = Editable.Factory.getInstance().newEditable(SP.getString("tempat", ""))
        tanggalProfil.text = Editable.Factory.getInstance().newEditable(SP.getString("tanggal", ""))
        genderProfil.text = Editable.Factory.getInstance().newEditable(SP.getString("gender", ""))
        alamatProfil.text = Editable.Factory.getInstance().newEditable(SP.getString("alamat", ""))
        fotoProfil.text = Editable.Factory.getInstance().newEditable(SP.getString("foto", ""))
    }
}