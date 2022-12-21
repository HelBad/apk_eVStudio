package com.example.rentalev.view.pengguna.fragment

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.rentalev.view.ActivityLogin
import com.example.rentalev.R
import com.example.rentalev.view.pengguna.ActivityProfil
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
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

        setHasOptionsMenu(true)
        val actionBar = requireActivity().findViewById(R.id.toolbarAkun) as Toolbar
        (activity as AppCompatActivity).setSupportActionBar(actionBar)

        ref = FirebaseDatabase.getInstance().getReference("pengguna")
        SP = requireActivity().applicationContext.getSharedPreferences("Pengguna", Context.MODE_PRIVATE)
        alertDialog = AlertDialog.Builder(requireActivity())

        dataAkun()
        editAkun.setOnClickListener {
            startActivity(Intent(activity, ActivityProfil::class.java))
        }
    }

    private fun dataAkun() {
        namaAkun.text = SP.getString("nama", "")
        emailAkun.text = SP.getString("email", "")
        telpAkun.text = SP.getString("telp", "")
        if(SP.getString("status", "") == "empty") {
            nikAkun.text = "-"
            ttlAkun.text = "-"
            genderAkun.text = "-"
            alamatAkun.text = "-"
        } else if(SP.getString("status", "") == "pending") {
            nikAkun.text = "-"
            ttlAkun.text = "-"
            genderAkun.text = "-"
            alamatAkun.text = "-"
        } else {
            nikAkun.text = SP.getString("nik", "")
            ttlAkun.text = SP.getString("tempat", "") + ", " + SP.getString("tanggal", "")
            genderAkun.text = SP.getString("gender", "")
            alamatAkun.text = SP.getString("alamat", "")
            Picasso.get().load(SP.getString("foto", "")).into(fotoAkun)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        val inflater = requireActivity().menuInflater
        inflater.inflate(R.menu.bar_profil, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.logout) {
            alertDialog.setTitle("Keluar Akun")
            alertDialog.setMessage("Apakah anda ingin keluar dari akun ini ?").setCancelable(false)
                .setPositiveButton("YA", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        val editor = SP.edit()
                        editor.putString("id_pengguna", "")
                        editor.putString("nama", "")
                        editor.putString("email", "")
                        editor.putString("password", "")
                        editor.putString("telp", "")
                        editor.putString("level", "")
                        editor.putString("id_identitas", "")
                        editor.putString("nik", "")
                        editor.putString("tempat", "")
                        editor.putString("tanggal", "")
                        editor.putString("gender", "")
                        editor.putString("alamat", "")
                        editor.putString("foto", "")
                        editor.putString("status", "")
                        editor.apply()

                        startActivity(Intent(context, ActivityLogin::class.java))
                        activity!!.finish()
                    }
                })
                .setNegativeButton("TIDAK", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        dialog.cancel()
                    }
                }).create().show()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}