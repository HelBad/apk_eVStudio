package com.example.rentalev.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rentalev.R
import com.example.rentalev.model.Pembayaran
import com.example.rentalev.model.Pesanan
import com.example.rentalev.model.Produk
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import java.text.DecimalFormat
import java.text.NumberFormat

class ViewholderPesanan(itemView: View): RecyclerView.ViewHolder(itemView) {
    private var mView: View = itemView
    private var mClickListener: ClickListener? = null
    var pesanan = Pesanan()
    var formatNumber: NumberFormat = DecimalFormat("#,###")

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

        val queryProduk = FirebaseDatabase.getInstance().getReference("produk")
            .orderByChild("id_produk").equalTo(pesanan.id_produk)
        queryProduk.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(datasnapshot: DataSnapshot) {
                for (snapshot1 in datasnapshot.children) {
                    val h = snapshot1.getValue(Produk::class.java)

                    val queryBayar = FirebaseDatabase.getInstance().getReference("pembayaran")
                        .orderByChild("id_pesanan").equalTo(pesanan.id_pesanan)
                    queryBayar.addListenerForSingleValueEvent(object: ValueEventListener {
                        override fun onDataChange(datasnapshot: DataSnapshot) {
                            for (snapshot2 in datasnapshot.children) {
                                val i = snapshot2.getValue(Pembayaran::class.java)

                                statusPesanan.text = "Status : " + pesanan.status_pesanan
                                namaprodukPesanan.text = h!!.nama_produk
                                Picasso.get().load(h.gambar).into(imgPesanan)
                                hargaprodukPesanan.text = "Rp. " + formatNumber.format(i!!.total_bayar.toInt()) + ",00"
                            }
                        }
                        override fun onCancelled(databaseError: DatabaseError) {}
                    })
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