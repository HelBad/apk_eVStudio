package com.example.rentalev.view.admin.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rentalev.R
import com.example.rentalev.adapter.ViewholderBeranda
import com.example.rentalev.model.Produk
import com.example.rentalev.view.admin.ActivityEdit
import com.example.rentalev.view.admin.ActivityDetail
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_beranda.*

class FragmentBeranda : Fragment() {
    lateinit var gridLayoutManager: GridLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_beranda_admin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        val actionBar = requireActivity().findViewById(R.id.toolbarBeranda) as Toolbar
        (activity as AppCompatActivity).setSupportActionBar(actionBar)

        gridLayoutManager = GridLayoutManager(activity, 2)
        recyclerBeranda.setHasFixedSize(true)
        recyclerBeranda.layoutManager = gridLayoutManager
    }

    override fun onStart() {
        super.onStart()
        val query = FirebaseDatabase.getInstance().getReference("produk")
        val firebaseRecyclerAdapter = object: FirebaseRecyclerAdapter<Produk, ViewholderBeranda>(
            Produk::class.java,
            R.layout.list_beranda,
            ViewholderBeranda::class.java,
            query
        ) {
            override fun populateViewHolder(viewHolder: ViewholderBeranda, model: Produk, position:Int) {
                viewHolder.setDetails(model)
            }
            override fun onCreateViewHolder(parent:ViewGroup, viewType:Int): ViewholderBeranda {
                val viewHolder = super.onCreateViewHolder(parent, viewType)
                viewHolder.setOnClickListener(object: ViewholderBeranda.ClickListener {
                    override fun onItemClick(view:View, position:Int) {
                        val intent = Intent(view.context, ActivityDetail::class.java)
                        intent.putExtra("id_produk", viewHolder.produk.id_produk)
                        startActivity(intent)
                    }
                    override fun onItemLongClick(view:View, position:Int) {}
                })
                return viewHolder
            }
        }
        recyclerBeranda.adapter = firebaseRecyclerAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        val inflater = requireActivity().menuInflater
        inflater.inflate(R.menu.bar_beranda, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.add) {
            startActivity(Intent(activity, ActivityEdit::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}