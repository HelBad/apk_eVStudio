package com.example.rentalev.pengguna

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
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
    lateinit var SP: SharedPreferences
    var countJumlah = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        SP = applicationContext.getSharedPreferences("Pengguna", Context.MODE_PRIVATE)
        setJumlah()
        setData()
    }

    override fun onStart() {
        super.onStart()
        if(intent.getStringExtra("pesanan").toString() == "order") {
            laytglCo.visibility = View.GONE
            laykembaliCo.visibility = View.GONE
            laywaktuCo.visibility = View.GONE
            layjaminanCo.visibility = View.GONE
        } else {
            laytglCo.visibility = View.VISIBLE
            laykembaliCo.visibility = View.VISIBLE
            laywaktuCo.visibility = View.VISIBLE
            layjaminanCo.visibility = View.VISIBLE
        }
    }

    private fun setJumlah() {
        tambahCo.setOnClickListener {
            if ("tersisa : " + jumlahCo.text.toString() == stokCo.text.toString()) {
            } else {
                countJumlah = jumlahCo.text.toString().toInt()
                countJumlah++
                jumlahCo.setText(countJumlah.toString())
            }
        }
        kurangCo.setOnClickListener {
            if (jumlahCo.text.toString() == "1") {
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
                    namaprodukCo.text = allocation!!.nama_produk
                    stokCo.text = "tersisa : " + allocation.stok
                    idCo.text = Editable.Factory.getInstance().newEditable(
                        SP.getString("nama", "") + " ( "
                                + SP.getString("nik", "") + " )"
                    )
//                    stokCo.text = allocation.stok
//                    hargaDetail.text = "Rp. " + formatNumber.format(allocation.harga.toInt()) + ",00"
//                    stokDetail.text = allocation.stok + " tersisa"
//                    Picasso.get().load(allocation.gambar).into(imgDetail)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}