package com.example.rentalev.view.admin

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.example.rentalev.R
import com.example.rentalev.model.Identitas
import com.example.rentalev.model.Inbox
import com.example.rentalev.model.Pengguna
import com.example.rentalev.model.Produk
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail_admin.*
import kotlinx.android.synthetic.main.activity_profil.*
import kotlinx.android.synthetic.main.activity_profil_admin.*
import kotlinx.android.synthetic.main.activity_profil_admin.alamatProfil
import kotlinx.android.synthetic.main.activity_profil_admin.emailProfil
import kotlinx.android.synthetic.main.activity_profil_admin.fotoProfil
import kotlinx.android.synthetic.main.activity_profil_admin.genderProfil
import kotlinx.android.synthetic.main.activity_profil_admin.namaProfil
import kotlinx.android.synthetic.main.activity_profil_admin.nikProfil
import kotlinx.android.synthetic.main.activity_profil_admin.telpProfil
import kotlinx.android.synthetic.main.fragment_profil_admin.*

class ActivityProfil : AppCompatActivity() {
    lateinit var alertDialog: AlertDialog.Builder
    var idIdentitas = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil_admin)

        alertDialog = AlertDialog.Builder(this)
        setDetail()
    }

    override fun onStart() {
        super.onStart()
        if(intent.getStringExtra("status").toString() == "pending") {
            layProfil.visibility = View.VISIBLE
        } else {
            layProfil.visibility = View.GONE
        }
    }

    private fun setDetail() {
        FirebaseDatabase.getInstance().getReference("pengguna")
            .orderByChild("id_pengguna").equalTo(intent.getStringExtra("id_pengguna").toString())
            .addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(datasnapshot: DataSnapshot) {
                for (snapshot1 in datasnapshot.children) {
                    val h = snapshot1.getValue(Pengguna::class.java)
                    namaProfil.text = h!!.nama
                    emailProfil.text = h.email
                    telpProfil.text = h.telp

                    FirebaseDatabase.getInstance().getReference("identitas")
                        .orderByChild("id_pengguna").equalTo(h.id_pengguna)
                        .addListenerForSingleValueEvent(object: ValueEventListener {
                        override fun onDataChange(datasnapshot: DataSnapshot) {
                            for (snapshot2 in datasnapshot.children) {
                                val i = snapshot2.getValue(Identitas::class.java)
                                idIdentitas = i!!.id_identitas
                                nikProfil.text = i.nik
                                ttlProfil.text = i.tempat + ", " + i.tanggal
                                genderProfil.text = i.gender
                                alamatProfil.text = i.alamat
                                Picasso.get().load(i.foto).into(fotoProfil)

                                btnAction()
                            }
                        }
                        override fun onCancelled(databaseError: DatabaseError) {}
                    })
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun btnAction() {
        btnTolak.setOnClickListener {
            alertDialog.setMessage("Apakah anda ingin menolak pengajuan identitas pengguna ?")
                .setCancelable(false)
                .setPositiveButton("YA", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        val query = FirebaseDatabase.getInstance().getReference("identitas")
                            .child(idIdentitas)
                        query.child("nik").setValue("")
                        query.child("tempat").setValue("")
                        query.child("tanggal").setValue("")
                        query.child("gender").setValue("")
                        query.child("alamat").setValue("")
                        query.child("foto").setValue("")
                        query.child("status").setValue("empty")

                        FirebaseDatabase.getInstance().getReference("inbox")
                            .orderByChild("id_identitas").equalTo(idIdentitas)
                            .addListenerForSingleValueEvent(object: ValueEventListener {
                                override fun onDataChange(datasnapshot: DataSnapshot) {
                                    for (snapshot2 in datasnapshot.children) {
                                        val inbox = snapshot2.getValue(Inbox::class.java)
                                        val refInbox = FirebaseDatabase.getInstance()
                                            .getReference("inbox").child(inbox!!.id_inbox)
                                        refInbox.child("keterangan")
                                            .setValue("Identitas yang anda ajukan ditolak. Harap isi " +
                                                    "dengan benar dan ajukan kembali agar dapat " +
                                                    "melakukan transaksi.")
                                    }
                                }
                                override fun onCancelled(databaseError: DatabaseError) {}
                            })
                        finish()
                    }
                })
                .setNegativeButton("TIDAK", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        dialog.cancel()
                    }
                }).create().show()
        }
        btnSetuju.setOnClickListener {
            alertDialog.setMessage("Apakah anda ingin menyetujui pengajuan identitas pengguna ?")
                .setCancelable(false)
                .setPositiveButton("YA", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        FirebaseDatabase.getInstance().getReference("identitas")
                            .child(idIdentitas).child("status").setValue("approve")

                        FirebaseDatabase.getInstance().getReference("inbox")
                            .orderByChild("id_identitas").equalTo(idIdentitas)
                            .addListenerForSingleValueEvent(object: ValueEventListener {
                                override fun onDataChange(datasnapshot: DataSnapshot) {
                                    for (snapshot2 in datasnapshot.children) {
                                        val inbox = snapshot2.getValue(Inbox::class.java)
                                        val refInbox = FirebaseDatabase.getInstance()
                                            .getReference("inbox").child(inbox!!.id_inbox)
                                        refInbox.child("keterangan")
                                            .setValue("Identitas yang anda ajukan disetujui. Terimakasih " +
                                                    "telah memakai layanan kami.")
                                    }
                                }
                                override fun onCancelled(databaseError: DatabaseError) {}
                            })
                        finish()
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