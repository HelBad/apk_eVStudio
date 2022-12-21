package com.example.rentalev.model

class Inbox {
    lateinit var id_inbox: String
    lateinit var id_pengguna: String
    lateinit var id_identitas: String
    lateinit var id_pesanan: String
    lateinit var judul: String
    lateinit var keterangan: String
    lateinit var jenis_inbox: String

    constructor() {}
    constructor(id_inbox: String, id_pengguna: String, id_identitas: String, id_pesanan: String,
                judul: String, keterangan: String, jenis_inbox: String) {
        this.id_inbox = id_inbox
        this.id_pengguna = id_pengguna
        this.id_identitas = id_identitas
        this.id_pesanan = id_pesanan
        this.judul = judul
        this.keterangan = keterangan
        this.jenis_inbox = jenis_inbox
    }
}