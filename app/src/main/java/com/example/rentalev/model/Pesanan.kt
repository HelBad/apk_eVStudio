package com.example.rentalev.model

class Pesanan {
    lateinit var id_pesanan: String
    lateinit var id_identitas: String
    lateinit var id_produk: String
    lateinit var lokasi: String
    lateinit var tgl_pesan: String
    lateinit var waktu_pesan: String
    lateinit var jumlah: String
    lateinit var subtotal: String

    constructor() {}
    constructor(id_pesanan: String, id_identitas: String, id_produk: String, lokasi: String,
                tgl_pesan: String, waktu_pesan: String, jumlah: String, subtotal: String) {
        this.id_pesanan = id_pesanan
        this.id_identitas = id_identitas
        this.id_produk = id_produk
        this.lokasi = lokasi
        this.tgl_pesan = tgl_pesan
        this.waktu_pesan = waktu_pesan
        this.jumlah = jumlah
        this.subtotal = subtotal
    }
}