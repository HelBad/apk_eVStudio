package com.example.rentalev.pengguna

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.rentalev.R
import com.example.rentalev.model.Identitas
import com.example.rentalev.model.Pengguna
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_profil.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.fragment_profil.*

class ActivityProfil : AppCompatActivity() {
    lateinit var alertDialog: AlertDialog.Builder
    lateinit var ref: DatabaseReference
    lateinit var SP: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)

        alertDialog = AlertDialog.Builder(this)
        ref = FirebaseDatabase.getInstance().getReference("pengguna")
        SP = applicationContext.getSharedPreferences("Pengguna", Context.MODE_PRIVATE)
        dataProfil()
        btnSimpan.setOnClickListener {
            alertDialog.setMessage("Apakah data yang dimasukkan sudah benar ?")
                .setCancelable(false)
                .setPositiveButton("YA", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        if(validate()){
                            ref.orderByChild("id_identitas").equalTo(SP.getString("id_identitas", ""))
                                .addListenerForSingleValueEvent( object : ValueEventListener {
                                    override fun onDataChange(p0: DataSnapshot) {
//                                        identitas()
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
        btnBatal.setOnClickListener {
            finish()
        }
    }

    private fun dataProfil() {
        namaProfil.text = Editable.Factory.getInstance().newEditable(SP.getString("nama", ""))
        emailProfil.text = Editable.Factory.getInstance().newEditable(SP.getString("email", ""))
        telpProfil.text = Editable.Factory.getInstance().newEditable(SP.getString("telp", ""))
        nikProfil.text = Editable.Factory.getInstance().newEditable(SP.getString("nik", ""))
        tempatProfil.text = Editable.Factory.getInstance().newEditable(SP.getString("tempat", ""))
        tanggalProfil.text = Editable.Factory.getInstance().newEditable(SP.getString("tanggal", ""))
        genderProfil.text = Editable.Factory.getInstance().newEditable(SP.getString("gender", ""))
        alamatProfil.text = Editable.Factory.getInstance().newEditable(SP.getString("alamat", ""))
        fotoProfil.text = Editable.Factory.getInstance().newEditable(SP.getString("foto", ""))
    }

    private fun validate(): Boolean {
        if(namaProfil.text.toString() == "") {
            Toast.makeText(this, "Nama masih kosong", Toast.LENGTH_SHORT).show()
            return false
        }
        if(emailProfil.text.toString() == "") {
            Toast.makeText(this, "Email masih kosong", Toast.LENGTH_SHORT).show()
            return false
        }
        if(telpProfil.text.toString() == "") {
            Toast.makeText(this, "Nomor telepon kosong", Toast.LENGTH_SHORT).show()
            return false
        }
        if(nikProfil.text.toString() == "") {
            Toast.makeText(this, "NIK tidak cocok", Toast.LENGTH_SHORT).show()
            return false
        }
        if(tempatProfil.text.toString() == "") {
            Toast.makeText(this, "Tempat lahir kosong", Toast.LENGTH_SHORT).show()
            return false
        }
        if(tanggalProfil.text.toString() == "") {
            Toast.makeText(this, "Tanggal lahir kosong", Toast.LENGTH_SHORT).show()
            return false
        }
        if(genderProfil.text.toString() == "-") {
            Toast.makeText(this, "Jenis Kelamin kosong", Toast.LENGTH_SHORT).show()
            return false
        }
        if(alamatProfil.text.toString() == "") {
            Toast.makeText(this, "Alamat masih kosong", Toast.LENGTH_SHORT).show()
            return false
        }
        if(fotoProfil.text.toString() == "") {
            Toast.makeText(this, "Foto masih kosong", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun updateProfil() {
        Toast.makeText(this, "Mohon Tunggu...", Toast.LENGTH_SHORT).show()
        ref.orderByChild("id_pengguna").equalTo(SP.getString("id_pengguna", ""))
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(datasnapshot: DataSnapshot) {
                    for (s1 in datasnapshot.children) {
                        val h = s1.getValue(Pengguna::class.java)
                        ref.child(h!!.nama).setValue(namaProfil.text.toString())
                        ref.child(h.email).setValue(emailProfil.text.toString())
                        ref.child(h.telp).setValue(telpProfil.text.toString())

                        ref = FirebaseDatabase.getInstance().getReference("identitas")
                        ref.orderByChild("id_pengguna").equalTo(h.id_pengguna)
                            .addListenerForSingleValueEvent(object: ValueEventListener {
                                override fun onDataChange(datasnapshot: DataSnapshot) {
                                    for (s2 in datasnapshot.children) {
                                        val i = s2.getValue(Identitas::class.java)


                                        
                                        fotoProfil
                                        ref.child(i!!.nik).setValue(nikProfil.text.toString())
                                        ref.child(i.tempat).setValue(tempatProfil.text.toString())
                                        ref.child(i.tanggal).setValue(tanggalProfil.text.toString())

                                        ref.child(i.gender).setValue(genderProfil.text.toString())
                                        ref.child(i.tanggal).setValue(tanggalProfil.text.toString())
                                        ref.child(i.tanggal).setValue(tanggalProfil.text.toString())


                                    }
                                }
                                override fun onCancelled(databaseError: DatabaseError) {}
                            })
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }

//    private fun identitas() {
//        Toast.makeText(this, "Mohon Tunggu...", Toast.LENGTH_SHORT).show()
//
//        val idIdentitas  = ref.push().key.toString()
//        val addData = Identitas(idIdentitas, namaRegister.text.toString(), emailRegister.text.toString(),
//            passwordRegister.text.toString(), telpRegister.text.toString(), "Pengguna")
//
//        ref.child(idPengguna).setValue(addData).addOnCompleteListener {
////            val editor = SP.edit()
////            editor.putString("id_pengguna", idPengguna)
////            editor.putString("nama", namaRegister.text.toString())
////            editor.putString("email", emailRegister.text.toString())
////            editor.putString("password", passwordRegister.text.toString())
////            editor.putString("telp", telpRegister.text.toString())
////            editor.putString("level", addData.level)
////            editor.apply()
//        }.addOnFailureListener {
//            btnRegister.isClickable = true
//            Toast.makeText(this, "Gagal Register", Toast.LENGTH_SHORT).show()
//        }
//    }
}