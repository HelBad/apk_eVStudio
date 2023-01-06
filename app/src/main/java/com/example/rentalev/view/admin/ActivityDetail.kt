package com.example.rentalev.view.admin

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.rentalev.R
import com.example.rentalev.model.Produk
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail_admin.*
import java.text.DecimalFormat
import java.text.NumberFormat

class ActivityDetail : AppCompatActivity() {
    lateinit var ref: DatabaseReference
    lateinit var alertDialog: AlertDialog.Builder
    var formatNumber: NumberFormat = DecimalFormat("#,###")
    var idProduk = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_admin)

        ref = FirebaseDatabase.getInstance().getReference("produk")
        alertDialog = AlertDialog.Builder(this)
        setDetail()

        btnEdit.setOnClickListener {
            val intent = Intent(this@ActivityDetail, ActivityEdit::class.java)
            intent.putExtra("id_produk", idProduk)
            intent.putExtra("status", "edit")
            startActivity(intent)
        }
        btnHapus.setOnClickListener {
            alertDialog.setMessage("Apakah anda ingin menghapus produk ini ?")
                .setCancelable(false)
                .setPositiveButton("YA", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        ref.child(intent.getStringExtra("id_produk").toString()).removeValue()
                    }
                })
                .setNegativeButton("TIDAK", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        dialog.cancel()
                    }
                }).create().show()
        }
    }

    private fun setDetail() {
        val query = ref.orderByChild("id_produk").equalTo(intent.getStringExtra("id_produk").toString())
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