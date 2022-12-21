package com.example.rentalev.view.pengguna

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.rentalev.R
import com.example.rentalev.model.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_pesanan.*
import java.text.DecimalFormat
import java.text.NumberFormat

class ActivityPesanan : AppCompatActivity() {
    var formatNumber: NumberFormat = DecimalFormat("#,###")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pesanan)

        setDetail()
    }

    private fun setDetail() {
        val queryPengguna = FirebaseDatabase.getInstance().getReference("pengguna")
            .orderByChild("id_pengguna").equalTo(intent.getStringExtra("id_pengguna").toString())
        queryPengguna.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(datasnapshot: DataSnapshot) {
                for (snapshot1 in datasnapshot.children) {
                    val h = snapshot1.getValue(Pengguna::class.java)
                    val queryIdentitas = FirebaseDatabase.getInstance().getReference("identitas")
                        .orderByChild("id_pengguna").equalTo(h!!.id_pengguna)
                    queryIdentitas.addListenerForSingleValueEvent(object: ValueEventListener {
                        override fun onDataChange(datasnapshot: DataSnapshot) {
                            for (snapshot2 in datasnapshot.children) {
                                val i = snapshot2.getValue(Identitas::class.java)
                                idPesan.text = ": " + h.nama + " ( " + i!!.nik + " )"
                            }
                        }
                        override fun onCancelled(databaseError: DatabaseError) {}
                    })
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })

        val queryProduk = FirebaseDatabase.getInstance().getReference("produk")
            .orderByChild("id_produk").equalTo(intent.getStringExtra("id_produk").toString())
        queryProduk.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(datasnapshot: DataSnapshot) {
                for (snapshot3 in datasnapshot.children) {
                    val j = snapshot3.getValue(Produk::class.java)
                    namaprodukPesan.text = j!!.nama_produk
                    hargaPesan.text = "Rp. " + formatNumber.format(j.harga.toInt()) + ",00"
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })

        val queryPesanan = FirebaseDatabase.getInstance().getReference("pesanan")
            .orderByChild("id_pesanan").equalTo(intent.getStringExtra("id_pesanan").toString())
        queryPesanan.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(datasnapshot: DataSnapshot) {
                for (snapshot4 in datasnapshot.children) {
                    val k = snapshot4.getValue(Pesanan::class.java)
                    jumlahPesan.text = "Jumlah : " + k!!.jumlah
                    lokasiPesan.text = ": " + k.lokasi
                    subtotalPesan.text = ": Rp. " + formatNumber.format(k.subtotal.toInt()) + ",00"
                    if(k.tgl_pesan == "") {
                        laytglPesan.visibility = View.GONE
                        laydurasiPesan.visibility = View.GONE
                        laywaktuPesan.visibility = View.GONE
                    } else {
                        tglPesan.text = ": " + k.tgl_pesan
                        waktuPesan.text = ": " + k.waktu_pesan
                        durasiPesan.text = ": " + k.durasi + " hari"
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })

        val queryBayar = FirebaseDatabase.getInstance().getReference("pembayaran")
            .orderByChild("id_pesanan").equalTo(intent.getStringExtra("id_pesanan").toString())
        queryBayar.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(datasnapshot: DataSnapshot) {
                for (snapshot5 in datasnapshot.children) {
                    val l = snapshot5.getValue(Pembayaran::class.java)
                    metodePesan.text = ": " + l!!.metode
                    adminPesan.text = ": Rp. " + formatNumber.format(l.admin.toInt()) + ",00"
                    ongkirPesan.text = ": Rp. " + formatNumber.format(l.ongkir.toInt()) + ",00"
                    totalPesan.text = ": Rp. " + formatNumber.format(l.total_bayar.toInt()) + ",00"
                    if(l.jaminan == "") {
                        layjaminanPesan.visibility = View.GONE
                    } else {
                        jaminanPesan.text = ": " + l!!.jaminan
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}