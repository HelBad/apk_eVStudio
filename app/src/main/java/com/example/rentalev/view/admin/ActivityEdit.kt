package com.example.rentalev.view.admin

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import com.example.rentalev.R
import com.example.rentalev.model.Inbox
import com.example.rentalev.model.Pembayaran
import com.example.rentalev.model.Pesanan
import com.example.rentalev.model.Produk
import com.example.rentalev.view.pengguna.ActivityUtama
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_checkout.*
import kotlinx.android.synthetic.main.activity_edit.*
import java.util.ArrayList

class ActivityEdit : AppCompatActivity() {
    lateinit var ref: DatabaseReference
    lateinit var storageReference: StorageReference
    var produk: ArrayList<String> = arrayListOf()
    lateinit var uri: Uri
    var url: Uri? = null
    var countJumlah = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        ref = FirebaseDatabase.getInstance().getReference("produk")
        storageReference = FirebaseStorage.getInstance().getReference("produk")
        produk = arrayListOf("", "", "", "", "", "", "")

        setJumlah()
        setData()
        gambarEdit.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
        btnBatal.setOnClickListener {
            finish()
        }
        btnSimpan.setOnClickListener {
            if(validate()) {
                simpanProduk()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if(intent.getStringExtra("status").toString() == "edit") {
            produk[0] = intent.getStringExtra("id_produk").toString()
        } else {
            produk[0] = ref.push().key.toString()
        }
    }

    private fun setJumlah() {
        plusJumlahEdit.setOnClickListener {
            countJumlah = jumlahEdit.text.toString().toInt()
            countJumlah++
            jumlahEdit.setText(countJumlah.toString())
        }
        minJumlahEdit.setOnClickListener {
            if (jumlahEdit.text.toString() == "1") {
            } else {
                countJumlah = jumlahEdit.text.toString().toInt()
                countJumlah--
                jumlahEdit.setText(countJumlah.toString())
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK) {
            if(requestCode == 0) {
                uri = data!!.data!!
                var mStorage = storageReference.child(produk[0])
                try {
                    mStorage.putFile(uri).addOnFailureListener {}.addOnSuccessListener {
                        mStorage.downloadUrl.addOnCompleteListener { taskSnapshot ->
                            url = taskSnapshot.result
                            gambarEdit.setText(url.toString())
                            Toast.makeText(this, "Foto berhasil di upload", Toast.LENGTH_SHORT).show()
                        }}
                } catch(ex:Exception) {
                    Toast.makeText(this, "Foto gagal di upload", Toast.LENGTH_SHORT).show()
                }
            }}
    }

    private fun setData() {
        ref.orderByChild("id_produk").equalTo(intent.getStringExtra("id_produk").toString())
            .addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(datasnapshot: DataSnapshot) {
                for (snapshot1 in datasnapshot.children) {
                    val allocation = snapshot1.getValue(Produk::class.java)
                    namaEdit.text = Editable.Factory.getInstance().newEditable(allocation!!.nama_produk)
                    deskripsiEdit.text = Editable.Factory.getInstance().newEditable(allocation.deskripsi)
                    hargaEdit.text = Editable.Factory.getInstance().newEditable(allocation.harga)
                    jumlahEdit.text = Editable.Factory.getInstance().newEditable(allocation.stok)
                    gambarEdit.text = Editable.Factory.getInstance().newEditable(allocation.gambar)
                    if(kategoriEdit.checkedRadioButtonId == R.id.kategoriEdit1) {
                        kategoriEdit1.isChecked = true
                    } else {
                        kategoriEdit2.isChecked = true
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun validate(): Boolean {
        if(namaEdit.text.toString() == "") {
            Toast.makeText(this, "Nama produk kosong", Toast.LENGTH_SHORT).show()
            return false
        }
        if(deskripsiEdit.text.toString() == "") {
            Toast.makeText(this, "Deskripsi masih kosong", Toast.LENGTH_SHORT).show()
            return false
        }
        if(hargaEdit.text.toString() == "") {
            Toast.makeText(this, "Harga produk kosong", Toast.LENGTH_SHORT).show()
            return false
        }
        if(gambarEdit.text.toString() == "") {
            Toast.makeText(this, "Gambar produk kosong", Toast.LENGTH_SHORT).show()
            return false
        }
        if(kategoriEdit.checkedRadioButtonId == -1) {
            Toast.makeText(this, "Pilih kategori produk", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun simpanProduk() {
        produk[1] = namaEdit.text.toString()
        produk[2] = deskripsiEdit.text.toString()
        produk[3] = hargaEdit.text.toString()
        produk[4] = jumlahEdit.text.toString()
        produk[5] = gambarEdit.text.toString()
        if(kategoriEdit.checkedRadioButtonId == R.id.kategoriEdit1) {
            produk[6] = "jual"
        } else {
            produk[6] = "sewa"
        }

        val addProduk = Produk(produk[0], produk[1], produk[2], produk[3], produk[4], produk[5], produk[6])
        ref.child(produk[0]).setValue(addProduk).addOnCompleteListener {
            startActivity(Intent(this@ActivityEdit, ActivityEdit::class.java))
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "Gagal menambahkan produk", Toast.LENGTH_SHORT).show()
        }
    }
}