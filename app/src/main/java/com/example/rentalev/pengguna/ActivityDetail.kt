package com.example.rentalev.pengguna

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.rentalev.R
import com.example.rentalev.model.Produk
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import java.text.DecimalFormat
import java.text.NumberFormat

class ActivityDetail : AppCompatActivity() {
    var formatNumber: NumberFormat = DecimalFormat("#,###")
    var idProduk = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        setDetail()
        btnOrder.setOnClickListener {
            val intent = Intent(this@ActivityDetail, ActivityCheckout::class.java)
            intent.putExtra("id_produk", idProduk)
            intent.putExtra("pesanan", "order")
            startActivity(intent)
        }
        btnBooking.setOnClickListener {
            val intent = Intent(this@ActivityDetail, ActivityCheckout::class.java)
            intent.putExtra("id_produk", idProduk)
            intent.putExtra("pesanan", "booking")
            startActivity(intent)
        }
    }

    private fun setDetail() {
        val query = FirebaseDatabase.getInstance().getReference("produk")
            .orderByChild("id_produk").equalTo(intent.getStringExtra("id_produk").toString())
        query.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(datasnapshot: DataSnapshot) {
                for (snapshot1 in datasnapshot.children) {
                    val allocation = snapshot1.getValue(Produk::class.java)
                    idProduk = allocation!!.id_produk
                    namaDetail.text = allocation.nama_produk
                    deskripsiDetail.text = allocation.deskripsi
                    hargaDetail.text = "Rp. " + formatNumber.format(allocation.harga.toInt()) + ",00"
                    stokDetail.text = allocation.stok + " tersisa"
                    Picasso.get().load(allocation.gambar).into(imgDetail)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}