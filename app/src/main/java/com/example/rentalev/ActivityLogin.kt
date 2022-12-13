package com.example.rentalev

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.rentalev.model.Pengguna
import com.example.rentalev.pengguna.ActivityUtama
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_login.*

class ActivityLogin : AppCompatActivity() {
    lateinit var SP: SharedPreferences
    lateinit var alertDialog: AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        SP = getSharedPreferences("Pengguna", Context.MODE_PRIVATE)
        alertDialog = AlertDialog.Builder(this)

        btnLogin.setOnClickListener {
            if(validate()) {
                login()
              }
        }
        textRegister.setOnClickListener {
            val intent = Intent(this, ActivityRegister::class.java)
            startActivity(intent)
        }
    }

    private fun validate(): Boolean {
        if(emailLogin.text.toString() == "") {
            Toast.makeText(this, "Email kosong", Toast.LENGTH_SHORT).show()
            return false
        }
        if(emailLogin.text.toString() == "") {
            Toast.makeText(this, "Password kosong", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun login() {
        btnLogin.isClickable = false
        Toast.makeText(this@ActivityLogin, "Mohon Tunggu...", Toast.LENGTH_SHORT).show()
        FirebaseDatabase.getInstance().getReference("pengguna").orderByChild("email")
            .equalTo(emailLogin.text.toString()).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {}
                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        for (h in p0.children) {
                            val us = h.getValue(Pengguna::class.java)
                            if(us!!.password == passwordLogin.text.toString()) {
                                val editor = SP.edit()
                                editor.putString("id_pengguna", us.id_pengguna)
                                editor.putString("nama", us.nama)
                                editor.putString("email", us.email)
                                editor.putString("password", us.password)
                                editor.putString("telp", us.telp)
                                editor.putString("level", us.level)
                                editor.apply()

//                                if(us.level == "Pengguna") {
                                btnLogin.isClickable = false
                                val intent = Intent(this@ActivityLogin, ActivityUtama::class.java)
                                startActivity(intent)
                                finish()
//                                } else if(us.level == "Admin") {
//                                    btnLogin.isClickable = false
//                                    val intent = Intent(this@ActivityLogin,
//                                        com.example.projectskripsi.admin.ActivityUtama::class.java)
//                                    startActivity(intent)
//                                    finish()
//                                }
                            } else {
                                btnLogin.isClickable = true
                                Toast.makeText(this@ActivityLogin, "Password salah", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        btnLogin.isClickable = true
                        Toast.makeText(this@ActivityLogin, "Email salah", Toast.LENGTH_SHORT).show()
                    }
                }
            })
    }

    override fun onBackPressed() {
        alertDialog.setTitle("Keluar Aplikasi")
        alertDialog.setMessage("Apakah anda ingin keluar aplikasi ?").setCancelable(false)
            .setPositiveButton("YA", object: DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, id:Int) {
                    finishAffinity()
                }
            })
            .setNegativeButton("TIDAK", object: DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, id:Int) {
                    dialog.cancel()
                }
            }).create().show()
    }
}