package com.example.rentalev.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rentalev.R
import com.example.rentalev.model.Produk
import com.squareup.picasso.Picasso
import java.text.DecimalFormat
import java.text.NumberFormat

class ViewholderBeranda(itemView: View): RecyclerView.ViewHolder(itemView) {
    private var mView: View = itemView
    private var mClickListener: ClickListener? = null
    var produk = Produk()
    var formatNumber: NumberFormat = DecimalFormat("#,###")

    init {
        itemView.setOnClickListener { view -> mClickListener!!.onItemClick(view, adapterPosition) }
        itemView.setOnLongClickListener { view ->
            mClickListener!!.onItemLongClick(view, adapterPosition)
            true
        }
    }

    fun setDetails(produk: Produk) {
        this.produk = produk
        val namaProduk = mView.findViewById(R.id.namaProduk) as TextView
        val hargaProduk = mView.findViewById(R.id.hargaProduk) as TextView
        val stokProduk = mView.findViewById(R.id.stokProduk) as TextView
        val imgProduk = mView.findViewById(R.id.imgProduk) as ImageView

        namaProduk.text = produk.nama_produk
        stokProduk.text = "tersisa : " + produk.stok
        Picasso.get().load(produk.gambar).into(imgProduk)
        if(produk.kategori == "sewa") {
            hargaProduk.text = "Rp. " + formatNumber.format(produk.harga.toInt()) + ",00 / hari"
        } else {
            hargaProduk.text = "Rp. " + formatNumber.format(produk.harga.toInt()) + ",00"
        }
    }

    interface ClickListener {
        fun onItemClick(view: View, position:Int)
        fun onItemLongClick(view: View, position:Int)
    }

    fun setOnClickListener(clickListener: ClickListener) {
        mClickListener = clickListener
    }
}