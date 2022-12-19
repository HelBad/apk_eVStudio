package com.example.rentalev.pengguna

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.rentalev.R
import com.example.rentalev.model.Produk
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_checkout.*
import kotlinx.android.synthetic.main.activity_detail.*

class ActivityCheckout : AppCompatActivity() {
    var countJumlah = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        setJumlah()
    }

    private fun setJumlah() {
        tambahCo.setOnClickListener {
            countJumlah = jumlahCo.text.toString().toInt()
            countJumlah++
            jumlahCo.setText(countJumlah.toString())
        }
        kurangCo.setOnClickListener {
            if (jumlahCo.text.toString() == "1") {
            } else if (jumlahCo.text.toString() == "2") {
                countJumlah = jumlahCo.text.toString().toInt()
                countJumlah--
                jumlahCo.setText(countJumlah.toString())
            } else {
                countJumlah = jumlahCo.text.toString().toInt()
                countJumlah--
                jumlahCo.setText(countJumlah.toString())
            }
        }
    }

    private fun setData() {
        val query = FirebaseDatabase.getInstance().getReference("produk")
            .orderByChild("id_produk").equalTo(intent.getStringExtra("id_produk").toString())
        query.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(datasnapshot: DataSnapshot) {
                for (snapshot1 in datasnapshot.children) {
                    val allocation = snapshot1.getValue(Produk::class.java)
                    namaDetail.text = allocation!!.nama_produk
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