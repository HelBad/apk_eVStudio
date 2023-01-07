package com.example.rentalev.view.pengguna

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.rentalev.R
import com.example.rentalev.model.*
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_checkout.*
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class ActivityCheckout : AppCompatActivity() {
    lateinit var SP: SharedPreferences
    lateinit var ref: DatabaseReference
    lateinit var alertDialog: AlertDialog.Builder
    var formatNumber: NumberFormat = DecimalFormat("#,###")
    var formatTanggal = SimpleDateFormat("dd MMM YYYY")
    var formatWaktu = SimpleDateFormat("hh:mm aa")
    val kalender = Calendar.getInstance()
    var biaya: ArrayList<String> = arrayListOf()
    var radioBtn: ArrayList<String> = arrayListOf()
    var jenisPesanan = ""
    var idProduk = ""
    var stokCo = ""
    var countJumlah = 1
    var countDurasi = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        SP = applicationContext.getSharedPreferences("Pengguna", Context.MODE_PRIVATE)
        ref = FirebaseDatabase.getInstance().getReference("pesanan")
        alertDialog = AlertDialog.Builder(this)

        jenisPesanan = intent.getStringExtra("pesanan").toString()
        biaya = arrayListOf("0", "0","25000","0", "25000")
        radioBtn = arrayListOf("", "")
        setData()
        setJumlah()
        setDurasi()

        tglCo.setOnClickListener {
            setTanggal()
        }
        waktuCo.setOnClickListener {
            setWaktu()
        }
        btnPesanCo.setOnClickListener {
            alertDialog.setMessage("Apakah data yang dimasukkan sudah benar ?").setCancelable(false)
                .setPositiveButton("YA", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        if(validate()) {
                            simpanPesanan()
                        }
                    }
                })
                .setNegativeButton("TIDAK", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        dialog.cancel()
                    }
                }).create().show()

        }
    }

    override fun onStart() {
        super.onStart()
        if(jenisPesanan == "order") {
            laytglCo.visibility = View.GONE
            laydurasiCo.visibility = View.GONE
            laywaktuCo.visibility = View.GONE
            layjaminanCo.visibility = View.GONE
        } else {
            laytglCo.visibility = View.VISIBLE
            laydurasiCo.visibility = View.VISIBLE
            laywaktuCo.visibility = View.VISIBLE
            layjaminanCo.visibility = View.VISIBLE
        }
    }

    private fun setJumlah() {
        plusJumlahCo.setOnClickListener {
            if (jumlahCo.text.toString() == stokCo) {
            } else {
                countJumlah = jumlahCo.text.toString().toInt()
                countJumlah++
                jumlahCo.setText(countJumlah.toString())

                biaya[1] = (biaya[0].toInt() * countJumlah * countDurasi).toString()
                subtotalCo.text = ": Rp. " + formatNumber.format(biaya[1].toInt()) + ",00"
                biaya[4] = (biaya[1].toInt() + biaya[2].toInt() + biaya[3].toInt()).toString()
                totalCo.text = ": Rp. " + formatNumber.format(biaya[4].toInt()) + ",00"
            }
        }
        minJumlahCo.setOnClickListener {
            if (jumlahCo.text.toString() == "1") {
            } else {
                countJumlah = jumlahCo.text.toString().toInt()
                countJumlah--
                jumlahCo.setText(countJumlah.toString())

                biaya[1] = (biaya[0].toInt() * countJumlah * countDurasi).toString()
                subtotalCo.text = ": Rp. " + formatNumber.format(biaya[1].toInt()) + ",00"
                biaya[4] = (biaya[1].toInt() + biaya[2].toInt() + biaya[3].toInt()).toString()
                totalCo.text = ": Rp. " + formatNumber.format(biaya[4].toInt()) + ",00"
            }
        }
    }

    private fun setDurasi() {
        plusDurasiCo.setOnClickListener {
            if (durasiCo.text.toString() == "3") {
            } else {
                countDurasi = durasiCo.text.toString().toInt()
                countDurasi++
                durasiCo.setText(countDurasi.toString())

                biaya[1] = (biaya[0].toInt() * countJumlah * countDurasi).toString()
                subtotalCo.text = ": Rp. " + formatNumber.format(biaya[1].toInt()) + ",00"
                biaya[4] = (biaya[1].toInt() + biaya[2].toInt() + biaya[3].toInt()).toString()
                totalCo.text = ": Rp. " + formatNumber.format(biaya[4].toInt()) + ",00"
            }
        }
        minDurasiCo.setOnClickListener {
            if (durasiCo.text.toString() == "1") {
            } else {
                countDurasi = durasiCo.text.toString().toInt()
                countDurasi--
                durasiCo.setText(countDurasi.toString())

                biaya[1] = (biaya[0].toInt() * countJumlah * countDurasi).toString()
                subtotalCo.text = ": Rp. " + formatNumber.format(biaya[1].toInt()) + ",00"
                biaya[4] = (biaya[1].toInt() + biaya[2].toInt() + biaya[3].toInt()).toString()
                totalCo.text = ": Rp. " + formatNumber.format(biaya[4].toInt()) + ",00"
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
                    idProduk = allocation!!.id_produk
                    namaprodukCo.text = allocation.nama_produk
                    stokCo = allocation.stok
                    biaya[0] = allocation.harga
                    hargaCo.text = "Rp. " + formatNumber.format(biaya[0].toInt()) + ",00"
                    idCo.text = Editable.Factory.getInstance().newEditable(
                        SP.getString("nama", "") + " ( "
                                + SP.getString("nik", "") + " )"
                    )
                    Picasso.get().load(allocation.gambar).into(imgCo)

                    biaya[1] = (biaya[0].toInt() * countJumlah * countDurasi).toString()
                    biaya[4] = (biaya[1].toInt() + biaya[2].toInt() + biaya[3].toInt()).toString()
                    subtotalCo.text = ": Rp. " + formatNumber.format(biaya[1].toInt()) + ",00"
                    adminCo.text = ": Rp. " + formatNumber.format(biaya[2].toInt()) + ",00"
                    ongkirCo.text = ": Rp. " + formatNumber.format(biaya[3].toInt()) + ",00"
                    totalCo.text = ": Rp. " + formatNumber.format(biaya[4].toInt()) + ",00"
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun setTanggal() {
        val tglBooking = DatePickerDialog(this, {
                view, year, month, dayOfMonth -> val selectedDate = Calendar.getInstance()
            selectedDate.set(Calendar.YEAR, year)
            selectedDate.set(Calendar.MONTH, month)
            selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            tglCo.text = Editable.Factory.getInstance().newEditable(formatTanggal.format(selectedDate.time))
        }, kalender.get(Calendar.YEAR), kalender.get(Calendar.MONTH), kalender.get(Calendar.DAY_OF_MONTH))
        tglBooking.show()
    }

    private fun setWaktu() {
        val waktuBooking = TimePickerDialog(this, {
                view, hourOfDay, minute -> val selectedTime = Calendar.getInstance()
            selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
            selectedTime.set(Calendar.MINUTE, minute)
            waktuCo.text = Editable.Factory.getInstance().newEditable(formatWaktu.format(selectedTime.time))
        }, kalender.get(Calendar.HOUR_OF_DAY), kalender.get(Calendar.MINUTE), false)
        waktuBooking.show()
    }

    private fun validate(): Boolean {
        if(intent.getStringExtra("pesanan").toString() == "order") {
            if(namaprodukCo.text.toString() == "") {
                Toast.makeText(this, "Data produk kosong", Toast.LENGTH_SHORT).show()
                return false
            }
            if(idCo.text.toString() == "") {
                Toast.makeText(this, "Data pengguna kosong", Toast.LENGTH_SHORT).show()
                return false
            }
            if(lokasiCo.text.toString() == "") {
                Toast.makeText(this, "Lokasi masih kosong", Toast.LENGTH_SHORT).show()
                return false
            }
            if(metodeCo.checkedRadioButtonId == -1) {
                Toast.makeText(this, "Pilih metode pembayaran", Toast.LENGTH_SHORT).show()
                return false
            }
        } else {
            if(namaprodukCo.text.toString() == "") {
                Toast.makeText(this, "Data produk kosong", Toast.LENGTH_SHORT).show()
                return false
            }
            if(idCo.text.toString() == "") {
                Toast.makeText(this, "Data pengguna kosong", Toast.LENGTH_SHORT).show()
                return false
            }
            if(lokasiCo.text.toString() == "") {
                Toast.makeText(this, "Lokasi masih kosong", Toast.LENGTH_SHORT).show()
                return false
            }
            if(tglCo.text.toString() == "") {
                Toast.makeText(this, "Tanggal masih kosong", Toast.LENGTH_SHORT).show()
                return false
            }
            if(waktuCo.text.toString() == "") {
                Toast.makeText(this, "Waktu masih kosong", Toast.LENGTH_SHORT).show()
                return false
            }
            if(jaminanCo.checkedRadioButtonId == -1) {
                Toast.makeText(this, "Pilih jaminan", Toast.LENGTH_SHORT).show()
                return false
            }
            if(metodeCo.checkedRadioButtonId == -1) {
                Toast.makeText(this, "Pilih metode pembayaran", Toast.LENGTH_SHORT).show()
                return false
            }
        }
        return true
    }

    private fun simpanPesanan() {
        btnPesanCo.isClickable = false
        Toast.makeText(this, "Mohon Tunggu...", Toast.LENGTH_SHORT).show()

        val idPesanan  = ref.push().key.toString()
        val addPesan = Pesanan(idPesanan, SP.getString("id_pengguna", "").toString(),
            idProduk, lokasiCo.text.toString(), tglCo.text.toString(), waktuCo.text.toString(),
            durasiCo.text.toString(), jumlahCo.text.toString(), biaya[1], jenisPesanan,
            "pending")

        ref.child(idPesanan).setValue(addPesan).addOnCompleteListener {
            ref = FirebaseDatabase.getInstance().getReference("pembayaran")
            val idPembayaran  = ref.push().key.toString()
            if(jaminanCo.checkedRadioButtonId == -1) {
                radioBtn[0] = ""
            } else {
                if(jaminanCo.checkedRadioButtonId == R.id.jaminanCo1) {
                    radioBtn[0] = "KTP"
                } else {
                    radioBtn[0] = "SIM"
                }
            }
            if(metodeCo.checkedRadioButtonId == R.id.metodeCo1) {
                radioBtn[1] = "Tunai"
            }
            val addBayar = Pembayaran(idPembayaran, idPesanan, radioBtn[0], radioBtn[1], biaya[2],
                biaya[3], biaya[4])
            ref.child(idPembayaran).setValue(addBayar)

            ref = FirebaseDatabase.getInstance().getReference("inbox")
            val idInbox  = ref.push().key.toString()
            val addInbox = Inbox(idInbox, SP.getString("id_pengguna", "").toString(),
                SP.getString("id_identitas", "").toString(), idPesanan,
                "Pesanan Diproses", "Pesananmu dengan invoice : " + idPesanan +
                        " telah dikirim. Harap tunggu Admin merespon pesanan anda. Terimakasih.", "pesanan")
            ref.child(idInbox).setValue(addInbox)

            val intent = Intent(this@ActivityCheckout, ActivityUtama::class.java)
            intent.putExtra("pesanan", "true")
            startActivity(intent)
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "Gagal melakukan pesanan", Toast.LENGTH_SHORT).show()
        }
    }
}