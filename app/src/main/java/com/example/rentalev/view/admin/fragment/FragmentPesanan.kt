package com.example.rentalev.view.admin.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rentalev.R
import com.example.rentalev.adapter.ViewholderPesananAdmin
import com.example.rentalev.model.Pesanan
import com.example.rentalev.view.admin.ActivityPesanan
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import kotlinx.android.synthetic.main.fragment_pesanan_admin.*

class FragmentPesanan : Fragment() {
    lateinit var mLayoutManager: LinearLayoutManager
    lateinit var ref: DatabaseReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pesanan_admin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ref = FirebaseDatabase.getInstance().getReference("pesanan")

        mLayoutManager = LinearLayoutManager(requireActivity())
        recyclerPesanan.setHasFixedSize(true)
        recyclerPesanan.layoutManager = mLayoutManager
        pilihStatus()
    }

    private fun pilihStatus() {
        val pilihStatus = arrayOf("Diproses", "Dikirim", "Selesai", "Dibatalkan")
        spinnerPesanan.adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, pilihStatus)
        spinnerPesanan.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                statusPesanan.text = pilihStatus[position]
                loadData()
            }
        }
    }

    private fun loadData() {
        if(statusPesanan.text.toString() == "Diproses") {
            val query = ref.orderByChild("status_pesanan").equalTo("pending")
            listData(query)
        } else if(statusPesanan.text.toString() == "Dikirim") {
            val query = ref.orderByChild("status_pesanan").equalTo("delivery")
            listData(query)
        } else if(statusPesanan.text.toString() == "Selesai") {
            val query = ref.orderByChild("status_pesanan").equalTo("success")
            listData(query)
        } else if(statusPesanan.text.toString() == "Dibatalkan") {
            val query = ref.orderByChild("status_pesanan").equalTo("cancel")
            listData(query)
        }
    }

    private fun listData(query: Query){
        val firebaseRecyclerAdapter = object: FirebaseRecyclerAdapter<Pesanan, ViewholderPesananAdmin>(
            Pesanan::class.java,
            R.layout.list_pesanan_admin,
            ViewholderPesananAdmin::class.java,
            query
        ) {
            override fun populateViewHolder(viewHolder: ViewholderPesananAdmin, model: Pesanan, position:Int) {
                viewHolder.setDetails(model)
            }
            override fun onCreateViewHolder(parent:ViewGroup, viewType:Int): ViewholderPesananAdmin {
                val viewHolder = super.onCreateViewHolder(parent, viewType)
                viewHolder.setOnClickListener(object: ViewholderPesananAdmin.ClickListener {
                    override fun onItemClick(view:View, position:Int) {
                        val intent = Intent(view.context, ActivityPesanan::class.java)
                        intent.putExtra("id_pesanan", viewHolder.pesanan.id_pesanan)
                        intent.putExtra("id_pengguna", viewHolder.pesanan.id_pengguna)
                        intent.putExtra("id_produk", viewHolder.pesanan.id_produk)
                        intent.putExtra("status", viewHolder.pesanan.status_pesanan)
                        startActivity(intent)
                    }
                    override fun onItemLongClick(view:View, position:Int) {}
                })
                return viewHolder
            }
        }
        recyclerPesanan.adapter = firebaseRecyclerAdapter
    }
}