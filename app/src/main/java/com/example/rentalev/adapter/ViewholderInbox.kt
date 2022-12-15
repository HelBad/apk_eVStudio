package com.example.rentalev.adapter

import android.content.DialogInterface
import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rentalev.R
import com.example.rentalev.model.Identitas
import com.example.rentalev.model.Inbox
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class ViewholderInbox(itemView: View): RecyclerView.ViewHolder(itemView) {
    private var mView: View = itemView
    private var mClickListener: ClickListener? = null
    var inbox = Inbox()

    init {
        itemView.setOnClickListener { view -> mClickListener!!.onItemClick(view, adapterPosition) }
        itemView.setOnLongClickListener { view ->
            mClickListener!!.onItemLongClick(view, adapterPosition)
            true
        }
    }

    fun setDetails(inbox: Inbox) {
        this.inbox = inbox
        val judulInbox = mView.findViewById(R.id.judulInbox) as TextView
        val ketInbox = mView.findViewById(R.id.ketInbox) as TextView
        val statusInbox = mView.findViewById(R.id.statusInbox) as TextView

        judulInbox.text = inbox.judul
        ketInbox.text = inbox.keterangan

        val query = FirebaseDatabase.getInstance().getReference("identitas")
            .orderByChild("id_identitas").equalTo(inbox.id_identitas)
        query.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(datasnapshot: DataSnapshot) {
                for (snapshot1 in datasnapshot.children) {
                    val allocation = snapshot1.getValue(Identitas::class.java)
                    statusInbox.text = allocation!!.id_identitas
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