package com.example.rentalev.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rentalev.R
import com.example.rentalev.model.Pesanan
import com.example.rentalev.model.Produk
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class ViewholderPesanan(itemView: View): RecyclerView.ViewHolder(itemView) {
    private var mView: View = itemView
    private var mClickListener: ClickListener? = null
    var pesanan = Pesanan()

    init {
        itemView.setOnClickListener { view -> mClickListener!!.onItemClick(view, adapterPosition) }
        itemView.setOnLongClickListener { view ->
            mClickListener!!.onItemLongClick(view, adapterPosition)
            true
        }
    }

    fun setDetails(pesanan: Pesanan) {
        this.pesanan = pesanan
        val namaprodukPesanan = mView.findViewById(R.id.namaprodukPesanan) as TextView
        val hargaprodukPesanan = mView.findViewById(R.id.hargaprodukPesanan) as TextView
        val statusPesanan = mView.findViewById(R.id.statusPesanan) as TextView
        val imgPesanan = mView.findViewById(R.id.imgPesanan) as ImageView

        val query = FirebaseDatabase.getInstance().getReference("produk")
            .orderByChild("id_produk").equalTo(pesanan.id_produk)
        query.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(datasnapshot: DataSnapshot) {
                for (snapshot1 in datasnapshot.children) {
                    val allocation = snapshot1.getValue(Produk::class.java)
                    statusPesanan.text = pesanan.status_pesanan
                    namaprodukPesanan.text = allocation!!.nama_produk
                    hargaprodukPesanan.text = allocation.harga
                    Picasso.get().load(allocation.gambar).into(imgPesanan)
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