package com.example.rentalev.model

class Pesanan {
    lateinit var id_pesanan: String
    lateinit var id_pengguna: String
    lateinit var id_produk: String
    lateinit var lokasi: String
    lateinit var tgl_pesan: String
    lateinit var waktu_pesan: String
    lateinit var durasi: String
    lateinit var jumlah: String
    lateinit var subtotal: String
    lateinit var jenis_pesanan: String
    lateinit var status_pesanan: String

    constructor() {}
    constructor(id_pesanan: String, id_pengguna: String, id_produk: String, lokasi: String,
                tgl_pesan: String, waktu_pesan: String, durasi: String, jumlah: String,
                subtotal: String, jenis_pesanan: String, status_pesanan: String) {
        this.id_pesanan = id_pesanan
        this.id_pengguna = id_pengguna
        this.id_produk = id_produk
        this.lokasi = lokasi
        this.tgl_pesan = tgl_pesan
        this.waktu_pesan = waktu_pesan
        this.durasi = durasi
        this.jumlah = jumlah
        this.subtotal = subtotal
        this.jenis_pesanan = jenis_pesanan
        this.status_pesanan = status_pesanan
    }
}