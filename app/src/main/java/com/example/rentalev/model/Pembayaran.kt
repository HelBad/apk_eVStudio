package com.example.rentalev.model

class Pembayaran {
    lateinit var id_pembayaran: String
    lateinit var id_pesanan: String
    lateinit var jaminan: String
    lateinit var metode: String
    lateinit var admin: String
    lateinit var ongkir: String
    lateinit var total_bayar: String

    constructor() {}
    constructor(id_pembayaran: String, id_pesanan: String, jaminan: String, metode: String,
                admin: String, ongkir: String, total_bayar: String) {
        this.id_pembayaran = id_pembayaran
        this.id_pesanan = id_pesanan
        this.jaminan = jaminan
        this.metode = metode
        this.admin = admin
        this.ongkir = ongkir
        this.total_bayar = total_bayar
    }
}