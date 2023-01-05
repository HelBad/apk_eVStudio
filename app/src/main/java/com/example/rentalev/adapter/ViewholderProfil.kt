package com.example.rentalev.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rentalev.R
import com.example.rentalev.model.Identitas
import com.example.rentalev.model.Pengguna
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class ViewholderProfil(itemView: View): RecyclerView.ViewHolder(itemView) {
    private var mView: View = itemView
    private var mClickListener: ClickListener? = null
    var identitas = Identitas()

    init {
        itemView.setOnClickListener { view -> mClickListener!!.onItemClick(view, adapterPosition) }
        itemView.setOnLongClickListener { view ->
            mClickListener!!.onItemLongClick(view, adapterPosition)
            true
        }
    }

    fun setDetails(identitas: Identitas) {
        this.identitas = identitas
        val namaProfil = mView.findViewById(R.id.namaProfil) as TextView
        val emailProfil = mView.findViewById(R.id.emailProfil) as TextView
        val telpProfil = mView.findViewById(R.id.telpProfil) as TextView
        val statusProfil = mView.findViewById(R.id.statusProfil) as TextView
        val imgProfil = mView.findViewById(R.id.imgProfil) as ImageView

        val query = FirebaseDatabase.getInstance().getReference("pengguna")
            .orderByChild("id_pengguna").equalTo(identitas.id_pengguna)
        query.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(datasnapshot: DataSnapshot) {
                for (snapshot1 in datasnapshot.children) {
                    val allocation = snapshot1.getValue(Pengguna::class.java)
                    if(identitas.nik == "") {
                        namaProfil.text = allocation!!.nama
                    } else {
                        namaProfil.text = allocation!!.nama + " (" + identitas.nik + ")"
                    }
                    emailProfil.text = allocation.email
                    telpProfil.text = allocation.telp
                    statusProfil.text = "Status : " + identitas.status
                    if(identitas.foto != "") {
                        Picasso.get().load(identitas.foto).into(imgProfil)
                    }

                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    interface ClickListener {
        fun onItemClick(view: View, position:Int)
        fun onItemLongClick(view: View, position:Int)
    }

    fun setOnClickListener(clickListener: ClickListener) {
        mClickListener = clickListener
    }
}