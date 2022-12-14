package com.example.rentalev.pengguna

import android.Manifest
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.rentalev.R
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_profil.*
import kotlinx.android.synthetic.main.fragment_profil.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ActivityProfil : AppCompatActivity() {
    lateinit var alertDialog: AlertDialog.Builder
    lateinit var ref: DatabaseReference
    lateinit var SP: SharedPreferences

    var formatTanggal = SimpleDateFormat("dd MMM YYYY")
    val kalender = Calendar.getInstance()
    val CAMERA_PERM_CODE = 101
    val CAMERA_REQUEST_CODE = 102
    lateinit var currentPhotoPath: String
    var url: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)

        alertDialog = AlertDialog.Builder(this)
        ref = FirebaseDatabase.getInstance().getReference("pengguna")
        SP = applicationContext.getSharedPreferences("Pengguna", Context.MODE_PRIVATE)

        dataProfil()
        jenisKelamin()
        tanggalProfil.setOnClickListener {
            setTanggal()
        }
        fotoProfil.setOnClickListener {
            askCameraPermissions()
        }

        btnSimpan.setOnClickListener {
            alertDialog.setMessage("Apakah data yang dimasukkan sudah benar ?")
                .setCancelable(false)
                .setPositiveButton("YA", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        if(validate()){
                            updateProfil()
                            finish()
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

    //
    override fun onStart() {
        super.onStart()
        if(SP.getString("status", "") == "disetujui") {
            namaProfil.isEnabled = false
            namaProfil.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorGrey))
            cardNikProfil.visibility = View.GONE
            cardTempatProfil.visibility = View.GONE
            cardTanggalProfil.visibility = View.GONE
            cardGenderProfil.visibility = View.GONE
            cardAlamatProfil.visibility = View.GONE
            cardFotoProfil.visibility = View.GONE
        } else {
            namaProfil.isEnabled = true
            namaProfil.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorBlack))
            cardNikProfil.visibility = View.VISIBLE
            cardTempatProfil.visibility = View.VISIBLE
            cardTanggalProfil.visibility = View.VISIBLE
            cardGenderProfil.visibility = View.VISIBLE
            cardAlamatProfil.visibility = View.VISIBLE
            cardFotoProfil.visibility = View.VISIBLE
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

    private fun setTanggal() {
        val tanggalLahir = DatePickerDialog(this, {
                view, year, month, dayOfMonth -> val selectedDate = Calendar.getInstance()
            selectedDate.set(Calendar.YEAR, year)
            selectedDate.set(Calendar.MONTH, month)
            selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            tanggalProfil.text = Editable.Factory.getInstance().newEditable(formatTanggal.format(selectedDate.time))
        }, kalender.get(Calendar.YEAR), kalender.get(Calendar.MONTH), kalender.get(Calendar.DAY_OF_MONTH))
        tanggalLahir.show()
    }

    private fun jenisKelamin() {
        val genderUser = arrayOf("Laki-laki", "Perempuan")
        spinnerProfil.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, genderUser)
        spinnerProfil.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                genderProfil.text = Editable.Factory.getInstance().newEditable("Masukkan Jenis Kelamin")
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                genderProfil.text = Editable.Factory.getInstance().newEditable(genderUser[position])
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == this.CAMERA_PERM_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent()
            } else {
                Toast.makeText(this, "Koneksi Gagal", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == this.CAMERA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Mohon tunggu ...", Toast.LENGTH_SHORT).show()
                val f = File(currentPhotoPath)
                val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                val contentUri = Uri.fromFile(f)
                mediaScanIntent.data = contentUri
                this.sendBroadcast(mediaScanIntent)
                uploadImageToFirebase(SP.getString("id_identitas", "").toString(), contentUri)
            }
        }
    }

    private fun uploadImageToFirebase(id: String, contentUri: Uri) {
        val image = FirebaseStorage.getInstance().reference.child("identitas/$id")
        image.putFile(contentUri).addOnSuccessListener {
            image.downloadUrl.addOnSuccessListener { uri ->
                url = uri
                fotoProfil.text = Editable.Factory.getInstance().newEditable("Berhasil diupload")
            }
        }.addOnFailureListener {
            Toast.makeText(this@ActivityProfil, "Presensi Gagal", Toast.LENGTH_SHORT).show()
        }
    }

    private fun askCameraPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), this.CAMERA_PERM_CODE)
        } else {
            dispatchTakePictureIntent()
        }
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) { }
            if (photoFile != null) {
                val photoURI = FileProvider.getUriForFile(this, "com.example.rentalev", photoFile)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, this.CAMERA_REQUEST_CODE)
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File? {
        val imageFileName = SP.getString("id_identitas", "").toString()
        val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(imageFileName, ".jpg", storageDir)
        currentPhotoPath = image.absolutePath
        return image
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

        val db = ref.child(SP.getString("id_pengguna", "").toString())
        db.child("nama").setValue(namaProfil.text.toString())
        db.child("email").setValue(emailProfil.text.toString())
        db.child("telp").setValue(telpProfil.text.toString())

        val db1 = FirebaseDatabase.getInstance().getReference("identitas")
            .child(SP.getString("id_identitas", "").toString())
        db1.child("nik").setValue(nikProfil.text.toString())
        db1.child("tempat").setValue(tempatProfil.text.toString())
        db1.child("tanggal").setValue(tanggalProfil.text.toString())
        db1.child("gender").setValue(genderProfil.text.toString())
        db1.child("alamat").setValue(alamatProfil.text.toString())

        //
        db1.child("foto").setValue(url.toString())

        val editor = SP.edit()
        editor.putString("nama", namaProfil.text.toString())
        editor.putString("email", emailProfil.text.toString())
        editor.putString("telp", telpProfil.text.toString())
        editor.putString("nik", nikProfil.text.toString())
        editor.putString("tempat", tempatProfil.text.toString())
        editor.putString("tanggal", tanggalProfil.text.toString())
        editor.putString("gender", genderProfil.text.toString())
        editor.putString("alamat", alamatProfil.text.toString())
        editor.putString("foto", url.toString())
        editor.apply()
    }
}