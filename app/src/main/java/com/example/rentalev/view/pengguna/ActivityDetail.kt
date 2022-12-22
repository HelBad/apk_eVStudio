package com.example.rentalev.view.pengguna

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
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
    lateinit var SP: SharedPreferences
    lateinit var alertDialog: AlertDialog.Builder
    var formatNumber: NumberFormat = DecimalFormat("#,###")
    var idProduk = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        SP = applicationContext.getSharedPreferences("Pengguna", Context.MODE_PRIVATE)
        alertDialog = AlertDialog.Builder(this)
        setDetail()

        btnOrder.setOnClickListener {
            if(SP.getString("status", "") == "approve") {
                val intent = Intent(this@ActivityDetail, ActivityCheckout::class.java)
                intent.putExtra("id_produk", idProduk)
                intent.putExtra("pesanan", "order")
                startActivity(intent)
            } else {
                alertDialog.setMessage("Ajukan identitas anda di halaman profil, sebelum melakukan transaksi")
                    .setCancelable(false)
                    .setPositiveButton("OK", object: DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface, id:Int) {
                            dialog.cancel()
                        }
                    }).create().show()
            }
        }
        btnBooking.setOnClickListener {
            if(SP.getString("status", "") == "approve") {
                val intent = Intent(this@ActivityDetail, ActivityCheckout::class.java)
                intent.putExtra("id_produk", idProduk)
                intent.putExtra("pesanan", "booking")
                startActivity(intent)
            } else {
                alertDialog.setMessage("Ajukan identitas anda di halaman profil, sebelum melakukan transaksi")
                    .setCancelable(false)
                    .setPositiveButton("OK", object: DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface, id:Int) {
                            dialog.cancel()
                        }
                    }).create().show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if(intent.getStringExtra("kategori").toString() == "sewa") {
            btnOrder.visibility = View.GONE
            btnBooking.visibility = View.VISIBLE
        } else {
            btnOrder.visibility = View.VISIBLE
            btnBooking.visibility = View.GONE
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