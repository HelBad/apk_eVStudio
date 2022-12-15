package com.example.rentalev.model

class Inbox {
    lateinit var id_inbox: String
    lateinit var id_pengguna: String
    lateinit var id_identitas: String
    lateinit var judul: String
    lateinit var keterangan: String

    constructor() {}
    constructor(id_inbox: String, id_pengguna: String, id_identitas: String,
                judul: String, keterangan: String) {
        this.id_inbox = id_inbox
        this.id_pengguna = id_pengguna
        this.id_identitas = id_identitas
        this.judul = judul
        this.keterangan = keterangan
    }
}