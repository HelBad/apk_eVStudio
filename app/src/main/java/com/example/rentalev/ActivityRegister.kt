package com.example.rentalev

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.rentalev.model.Identitas
import com.example.rentalev.model.Pengguna
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_register.*

class ActivityRegister : AppCompatActivity() {
    lateinit var alertDialog: AlertDialog.Builder
    lateinit var ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        alertDialog = AlertDialog.Builder(this)
        ref = FirebaseDatabase.getInstance().getReference("pengguna")

        btnRegister.setOnClickListener {
            alertDialog.setMessage("Apakah data yang dimasukkan sudah benar ?")
                .setCancelable(false)
                .setPositiveButton("YA", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        if(validate()){
                            ref.orderByChild("email").equalTo(emailRegister.text.toString())
                                .addListenerForSingleValueEvent( object : ValueEventListener {
                                    override fun onDataChange(p0: DataSnapshot) {
                                        if(p0.exists()) {
                                            Toast.makeText(this@ActivityRegister,
                                                "Email sudah terdaftar", Toast.LENGTH_SHORT).show()
                                        } else {
                                            register()
                                        }
                                    }
                                    override fun onCancelled(p0: DatabaseError) { }
                                })
                        }
                    }
                })
                .setNegativeButton("TIDAK", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        dialog.cancel()
                    }
                }).create().show()
        }
        textLogin.setOnClickListener {
            finish()
        }
    }

    private fun validate(): Boolean {
        if(namaRegister.text.toString() == "") {
            Toast.makeText(this, "Nama masih kosong", Toast.LENGTH_SHORT).show()
            return false
        }
        if(emailRegister.text.toString() == "") {
            Toast.makeText(this, "Email masih kosong", Toast.LENGTH_SHORT).show()
            return false
        }
        if(passwordRegister.text.toString() == "") {
            Toast.makeText(this, "Password masih kosong", Toast.LENGTH_SHORT).show()
            return false
        }
        if(konfirmasiRegister.text.toString() != passwordRegister.text.toString()) {
            Toast.makeText(this, "Password tidak cocok", Toast.LENGTH_SHORT).show()
            return false
        }
        if(telpRegister.text.toString() == "") {
            Toast.makeText(this, "Nomor telepon kosong", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun register() {
        btnRegister.isClickable = false
        Toast.makeText(this, "Mohon Tunggu...", Toast.LENGTH_SHORT).show()

        val idPengguna  = ref.push().key.toString()
        val addData = Pengguna(idPengguna, namaRegister.text.toString(), emailRegister.text.toString(),
            passwordRegister.text.toString(), telpRegister.text.toString(), "Pengguna")

        ref.child(idPengguna).setValue(addData).addOnCompleteListener {
            ref = FirebaseDatabase.getInstance().getReference("identitas")
            val idIdentitas  = ref.push().key.toString()
            val addIdentitas = Identitas(idIdentitas, idPengguna, "", "", "",
                "", "", "", "menunggu")

            ref.child(idIdentitas).setValue(addIdentitas).addOnCompleteListener {
                finish()
            }.addOnFailureListener {
                btnRegister.isClickable = true
                Toast.makeText(this, "Gagal Register", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            btnRegister.isClickable = true
            Toast.makeText(this, "Gagal Register", Toast.LENGTH_SHORT).show()
        }
    }
}