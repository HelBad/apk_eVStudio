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
import com.example.rentalev.adapter.ViewholderPesanan
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
        val pilihStatus = arrayOf("Menunggu", "Disetujui", "Ditolak")
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
        if(statusPesanan.text.toString() == "Menunggu") {
            val query = ref.orderByChild("status_pesanan").equalTo("pending")
            listData(query)
        } else if(statusPesanan.text.toString() == "Disetujui") {
            val query = ref.orderByChild("status_pesanan").equalTo("approve")
            listData(query)
        } else if(statusPesanan.text.toString() == "Ditolak") {
            val query = ref.orderByChild("status_pesanan").equalTo("reject")
            listData(query)
        }
    }

    private fun listData(query: Query){
        val firebaseRecyclerAdapter = object: FirebaseRecyclerAdapter<Pesanan, ViewholderPesanan>(
            Pesanan::class.java,
            R.layout.list_pesanan,
            ViewholderPesanan::class.java,
            query
        ) {
            override fun populateViewHolder(viewHolder: ViewholderPesanan, model: Pesanan, position:Int) {
                viewHolder.setDetails(model)
            }
            override fun onCreateViewHolder(parent:ViewGroup, viewType:Int): ViewholderPesanan {
                val viewHolder = super.onCreateViewHolder(parent, viewType)
                viewHolder.setOnClickListener(object: ViewholderPesanan.ClickListener {
                    override fun onItemClick(view:View, position:Int) {
                        val intent = Intent(view.context, ActivityPesanan::class.java)
                        intent.putExtra("id_pesanan", viewHolder.pesanan.id_pesanan)
                        intent.putExtra("id_pengguna", viewHolder.pesanan.id_pengguna)
                        intent.putExtra("id_produk", viewHolder.pesanan.id_produk)
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