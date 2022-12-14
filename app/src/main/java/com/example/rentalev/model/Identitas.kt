package com.example.rentalev.model

class Identitas {
    lateinit var id_Identitas: String
    lateinit var id_pengguna: String
    lateinit var nik: String
    lateinit var tempat: String
    lateinit var tanggal: String
    lateinit var gender: String
    lateinit var alamat: String
    lateinit var foto: String
    lateinit var status: String

    constructor() {}
    constructor(id_Identitas: String, id_pengguna: String, nik:String, tempat: String,
                tanggal: String, gender: String, alamat: String, foto: String, status: String) {
        this.id_Identitas = id_Identitas
        this.id_pengguna = id_pengguna
        this.nik = nik
        this.tempat = tempat
        this.tanggal = tanggal
        this.gender = gender
        this.alamat = alamat
        this.foto = foto
        this.status = status
    }
}