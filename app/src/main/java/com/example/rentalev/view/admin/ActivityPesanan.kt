package com.example.rentalev.view.admin

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.example.rentalev.R
import com.example.rentalev.model.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_pesanan.*
import kotlinx.android.synthetic.main.activity_pesanan.adminPesan
import kotlinx.android.synthetic.main.activity_pesanan.durasiPesan
import kotlinx.android.synthetic.main.activity_pesanan.hargaPesan
import kotlinx.android.synthetic.main.activity_pesanan.idPesan
import kotlinx.android.synthetic.main.activity_pesanan.imgPesan
import kotlinx.android.synthetic.main.activity_pesanan.invoicePesan
import kotlinx.android.synthetic.main.activity_pesanan.jaminanPesan
import kotlinx.android.synthetic.main.activity_pesanan.jumlahPesan
import kotlinx.android.synthetic.main.activity_pesanan.laydurasiPesan
import kotlinx.android.synthetic.main.activity_pesanan.layjaminanPesan
import kotlinx.android.synthetic.main.activity_pesanan.laytglPesan
import kotlinx.android.synthetic.main.activity_pesanan.laywaktuPesan
import kotlinx.android.synthetic.main.activity_pesanan.lokasiPesan
import kotlinx.android.synthetic.main.activity_pesanan.metodePesan
import kotlinx.android.synthetic.main.activity_pesanan.namaprodukPesan
import kotlinx.android.synthetic.main.activity_pesanan.ongkirPesan
import kotlinx.android.synthetic.main.activity_pesanan.subtotalPesan
import kotlinx.android.synthetic.main.activity_pesanan.tglPesan
import kotlinx.android.synthetic.main.activity_pesanan.totalPesan
import kotlinx.android.synthetic.main.activity_pesanan.waktuPesan
import kotlinx.android.synthetic.main.activity_pesanan_admin.*
import java.text.DecimalFormat
import java.text.NumberFormat

class ActivityPesanan : AppCompatActivity() {
    lateinit var alertDialog: AlertDialog.Builder
    var formatNumber: NumberFormat = DecimalFormat("#,###")
    var idPesanan = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pesanan_admin)

        alertDialog = AlertDialog.Builder(this)
        idPesanan = intent.getStringExtra("id_pesanan").toString()
        setDetail()
        saveData()
    }

    override fun onStart() {
        super.onStart()
        if(intent.getStringExtra("status").toString() == "pending") {
            btnSelesai.visibility = View.GONE
            btnKirim.visibility = View.VISIBLE
            btnBatal.visibility = View.VISIBLE
        } else if(intent.getStringExtra("status").toString() == "delivery") {
            btnSelesai.visibility = View.VISIBLE
            btnKirim.visibility = View.GONE
            btnBatal.visibility = View.GONE
        } else {
            btnSelesai.visibility = View.GONE
            btnKirim.visibility = View.GONE
            btnBatal.visibility = View.GONE
        }
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
                    Picasso.get().load(j.gambar).into(imgPesan)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })

        val queryPesanan = FirebaseDatabase.getInstance().getReference("pesanan")
            .orderByChild("id_pesanan").equalTo(idPesanan)
        queryPesanan.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(datasnapshot: DataSnapshot) {
                for (snapshot4 in datasnapshot.children) {
                    val k = snapshot4.getValue(Pesanan::class.java)
                    invoicePesan.text = ": " + k!!.id_pesanan
                    jumlahPesan.text = "Jumlah : " + k.jumlah
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
            .orderByChild("id_pesanan").equalTo(idPesanan)
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

    private fun saveData() {
        btnBatal.setOnClickListener {
            alertDialog.setMessage("Apakah anda yakin membatalkan pesanan ?")
                .setCancelable(false)
                .setPositiveButton("YA", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        FirebaseDatabase.getInstance().getReference("pesanan")
                            .child(idPesanan).child("status_pesanan").setValue("cancel")

                        val query = FirebaseDatabase.getInstance().getReference("inbox")
                        query.orderByChild("id_pesanan").equalTo(idPesanan)
                            .addListenerForSingleValueEvent(object: ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    for (snapshot in dataSnapshot.children) {
                                        val inbox = snapshot.getValue(Inbox::class.java)
                                        query.child(inbox!!.id_inbox).child("judul")
                                            .setValue("Pesanan Dibatalkan")
                                        query.child(inbox.id_inbox).child("keterangan")
                                            .setValue("Pesananmu dengan invoice : " + idPesanan +" telah dibatalkan.")
                                        finish()
                                    }
                                }
                                override fun onCancelled(databaseError: DatabaseError) {}
                            })
                    }
                })
                .setNegativeButton("TIDAK", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        dialog.cancel()
                    }
                }).create().show()
        }
        btnKirim.setOnClickListener {
            alertDialog.setMessage("Apakah anda ingin mengirim barang pesanan ?")
                .setCancelable(false)
                .setPositiveButton("YA", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        FirebaseDatabase.getInstance().getReference("pesanan")
                            .child(idPesanan).child("status_pesanan").setValue("delivery")

                        val query = FirebaseDatabase.getInstance().getReference("inbox")
                        query.orderByChild("id_pesanan").equalTo(idPesanan)
                            .addListenerForSingleValueEvent(object: ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    for (snapshot in dataSnapshot.children) {
                                        val inbox = snapshot.getValue(Inbox::class.java)
                                        query.child(inbox!!.id_inbox).child("judul")
                                            .setValue("Pesanan Dikirim")
                                        query.child(inbox.id_inbox).child("keterangan")
                                            .setValue("Pesananmu dengan invoice : " + idPesanan +" telah dikirim, mohon ditunggu.")
                                        finish()
                                    }
                                }
                                override fun onCancelled(databaseError: DatabaseError) {}
                            })
                    }
                })
                .setNegativeButton("TIDAK", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        dialog.cancel()
                    }
                }).create().show()
        }
        btnSelesai.setOnClickListener {
            alertDialog.setMessage("Apakah anda ingin menyelesaikan pesanan ?")
                .setCancelable(false)
                .setPositiveButton("YA", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        FirebaseDatabase.getInstance().getReference("pesanan")
                            .child(idPesanan).child("status_pesanan").setValue("success")

                        val query = FirebaseDatabase.getInstance().getReference("inbox")
                        query.orderByChild("id_pesanan").equalTo(idPesanan)
                            .addListenerForSingleValueEvent(object: ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    for (snapshot in dataSnapshot.children) {
                                        val inbox = snapshot.getValue(Inbox::class.java)
                                        query.child(inbox!!.id_inbox).child("judul")
                                            .setValue("Pesanan Selesai")
                                        query.child(inbox.id_inbox).child("keterangan")
                                            .setValue("Pesananmu dengan invoice : " + idPesanan +" telah Selesai, terimakasih.")
                                        finish()
                                    }
                                }
                                override fun onCancelled(databaseError: DatabaseError) {}
                            })
                    }
                })
                .setNegativeButton("TIDAK", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        dialog.cancel()
                    }
                }).create().show()
        }
    }
}