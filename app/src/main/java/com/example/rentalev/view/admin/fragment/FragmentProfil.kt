package com.example.rentalev.view.admin.fragment

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rentalev.R
import com.example.rentalev.adapter.ViewholderProfil
import com.example.rentalev.model.Identitas
import com.example.rentalev.view.ActivityLogin
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import kotlinx.android.synthetic.main.fragment_profil_admin.*

class FragmentProfil : Fragment() {
    lateinit var ref: DatabaseReference
    lateinit var SP: SharedPreferences
    lateinit var alertDialog: AlertDialog.Builder
    lateinit var mLayoutManager: LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profil_admin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ref = FirebaseDatabase.getInstance().getReference("identitas")
        SP = requireActivity().applicationContext.getSharedPreferences("Pengguna", Context.MODE_PRIVATE)
        alertDialog = AlertDialog.Builder(requireActivity())

        mLayoutManager = LinearLayoutManager(requireActivity())
        recyclerAkun.setHasFixedSize(true)
        recyclerAkun.layoutManager = mLayoutManager

        pilihStatus()
        imgbarAkun.setOnClickListener {
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
        }
    }

    private fun pilihStatus() {
        val pilihStatus = arrayOf("Menunggu", "Disetujui", "Ditolak")
        spinnerAkun.adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, pilihStatus)
        spinnerAkun.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                statusAkun.text = pilihStatus[position]
                loadData()
            }
        }
    }

    private fun loadData() {
        if(statusAkun.text.toString() == "Menunggu") {
            val query = ref.orderByChild("status").equalTo("pending")
            listData(query)
        } else if(statusAkun.text.toString() == "Disetujui") {
            val query = ref.orderByChild("status").equalTo("approve")
            listData(query)
        } else if(statusAkun.text.toString() == "Ditolak") {
            val query = ref.orderByChild("status").equalTo("reject")
            listData(query)
        }
    }

    private fun listData(query: Query){
        val firebaseRecyclerAdapter = object: FirebaseRecyclerAdapter<Identitas, ViewholderProfil>(
            Identitas::class.java,
            R.layout.list_profil,
            ViewholderProfil::class.java,
            query
        ) {
            override fun populateViewHolder(viewHolder: ViewholderProfil, model: Identitas, position:Int) {
                viewHolder.setDetails(model)
            }
            override fun onCreateViewHolder(parent:ViewGroup, viewType:Int): ViewholderProfil {
                val viewHolder = super.onCreateViewHolder(parent, viewType)
                viewHolder.setOnClickListener(object: ViewholderProfil.ClickListener {
                    override fun onItemClick(view:View, position:Int) {}
                    override fun onItemLongClick(view:View, position:Int) {}
                })
                return viewHolder
            }
        }
        recyclerAkun.adapter = firebaseRecyclerAdapter
    }
}