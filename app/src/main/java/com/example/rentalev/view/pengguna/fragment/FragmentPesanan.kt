package com.example.rentalev.view.pengguna.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rentalev.R
import com.example.rentalev.adapter.ViewholderPesanan
import com.example.rentalev.model.Pesanan
import com.example.rentalev.view.pengguna.ActivityPesanan
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_pesanan.*

class FragmentPesanan : Fragment() {
    lateinit var mLayoutManager: LinearLayoutManager
    lateinit var SP: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pesanan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        SP = requireActivity().applicationContext.getSharedPreferences("Pengguna", Context.MODE_PRIVATE)

        mLayoutManager = LinearLayoutManager(requireActivity())
        recyclerPesanan.setHasFixedSize(true)
        recyclerPesanan.layoutManager = mLayoutManager
    }

    override fun onStart() {
        super.onStart()
        val query = FirebaseDatabase.getInstance().getReference("pesanan")
            .orderByChild("id_pengguna").equalTo(SP.getString("id_pengguna", "").toString())
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