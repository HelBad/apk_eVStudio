package com.example.rentalev.pengguna

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.rentalev.R
import com.example.rentalev.model.Produk
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_checkout.*
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_profil.*
import java.text.SimpleDateFormat
import java.util.*


class ActivityCheckout : AppCompatActivity() {
    lateinit var SP: SharedPreferences
    var formatTanggal = SimpleDateFormat("dd MMM YYYY")
    val kalender = Calendar.getInstance()
    var formatWaktu = SimpleDateFormat("hh:mm aa")
    var countJumlah = 0
    var countDurasi = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        SP = applicationContext.getSharedPreferences("Pengguna", Context.MODE_PRIVATE)
        setJumlah()
        setDurasi()
        setData()
        tglCo.setOnClickListener {
            setTanggal()
        }
        waktuCo.setOnClickListener {
            setWaktu()
        }
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
        plusJumlahCo.setOnClickListener {
            if ("tersisa : " + jumlahCo.text.toString() == stokCo.text.toString()) {
            } else {
                countJumlah = jumlahCo.text.toString().toInt()
                countJumlah++
                jumlahCo.setText(countJumlah.toString())
            }
        }
        minJumlahCo.setOnClickListener {
            if (jumlahCo.text.toString() == "1") {
            } else {
                countJumlah = jumlahCo.text.toString().toInt()
                countJumlah--
                jumlahCo.setText(countJumlah.toString())
            }
        }
    }

    private fun setDurasi() {
//        bayarCo1.text =

        plusDurasiCo.setOnClickListener {
            if (durasiCo.text.toString() == "3") {
            } else {
                countDurasi = durasiCo.text.toString().toInt()
                countDurasi++
                durasiCo.setText(countDurasi.toString())


            }
        }
        minDurasiCo.setOnClickListener {
            if (durasiCo.text.toString() == "1") {
            } else {
                countDurasi = durasiCo.text.toString().toInt()
                countDurasi--
                durasiCo.setText(countDurasi.toString())
            }
        }
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
        val waktuBooking = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener {
                view, hourOfDay, minute -> val selectedTime = Calendar.getInstance()
            selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
            selectedTime.set(Calendar.MINUTE, minute)
            waktuCo.text = Editable.Factory.getInstance().newEditable(formatWaktu.format(selectedTime.time))
        }, kalender.get(Calendar.HOUR_OF_DAY), kalender.get(Calendar.MINUTE), false)
        waktuBooking.show()
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