package com.example.rentalev.model

class Pengguna {
    lateinit var id_pengguna: String
    lateinit var nama: String
    lateinit var email: String
    lateinit var password: String
    lateinit var telp: String
    lateinit var level: String

    constructor() {}
    constructor(id_pengguna:String, nama:String, email:String, password:String,
                telp: String, level: String) {
        this.id_pengguna = id_pengguna
        this.nama = nama
        this.email = email
        this.password = password
        this.telp = telp
        this.level = level
    }
}