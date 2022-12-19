package com.example.rentalev.model

class Produk {
    lateinit var id_produk: String
    lateinit var nama_produk: String
    lateinit var deskripsi: String
    lateinit var harga: String
    lateinit var stok: String
    lateinit var gambar: String

    constructor() {}
    constructor(id_produk: String, nama_produk: String, deskripsi: String, harga: String,
                stok: String, gambar: String) {
        this.id_produk = id_produk
        this.nama_produk = nama_produk
        this.deskripsi = deskripsi
        this.harga = harga
        this.stok = stok
        this.gambar = gambar
    }
}