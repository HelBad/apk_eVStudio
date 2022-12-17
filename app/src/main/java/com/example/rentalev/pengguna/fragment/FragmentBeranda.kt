package com.example.rentalev.pengguna.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.rentalev.R
import com.example.rentalev.adapter.ViewholderBeranda
import com.example.rentalev.model.Produk
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_beranda.*

class FragmentBeranda : Fragment() {
    lateinit var gridLayoutManager: GridLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_beranda, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val slideModels = arrayListOf<SlideModel>()
        slideModels.add(SlideModel(R.drawable.tes_img_1))
        slideModels.add(SlideModel(R.drawable.tes_img_2))
        slideModels.add(SlideModel(R.drawable.tes_img_3))
        imageSlider.setImageList(slideModels, ScaleTypes.FIT)

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
                        //
                    }
                    override fun onItemLongClick(view:View, position:Int) {}
                })
                return viewHolder
            }
        }
        recyclerBeranda.adapter = firebaseRecyclerAdapter
    }
}