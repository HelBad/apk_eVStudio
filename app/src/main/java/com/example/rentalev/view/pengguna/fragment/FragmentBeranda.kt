package com.example.rentalev.view.pengguna.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.rentalev.R
import com.example.rentalev.adapter.ViewholderBeranda
import com.example.rentalev.model.Produk
import com.example.rentalev.view.pengguna.ActivityDetail
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
        slideModels.add(SlideModel("https://firebasestorage.googleapis.com/v0/b/rentalev-2d4e4.appspot.com/o/konten%2Fkonten1.jpg?alt=media&token=7682a435-3270-45a4-a56f-708a5d6d6a80"))
        slideModels.add(SlideModel("https://firebasestorage.googleapis.com/v0/b/rentalev-2d4e4.appspot.com/o/konten%2Fkonten2.jpg?alt=media&token=991f059f-7f08-455b-9e8b-25d49e9459c9"))
        slideModels.add(SlideModel("https://firebasestorage.googleapis.com/v0/b/rentalev-2d4e4.appspot.com/o/konten%2Fkonten3.jpg?alt=media&token=e2473d39-b70d-4133-8559-4ccfc0605747"))
        slideModels.add(SlideModel("https://firebasestorage.googleapis.com/v0/b/rentalev-2d4e4.appspot.com/o/konten%2Fkonten4.jpg?alt=media&token=ddf2827c-e19e-4fc3-aaca-ddd36fa62cf6"))
        slideModels.add(SlideModel("https://firebasestorage.googleapis.com/v0/b/rentalev-2d4e4.appspot.com/o/konten%2Fkonten5.webp?alt=media&token=1958dd3d-96e2-4616-9110-51749d1c5277"))
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
                        val intent = Intent(view.context, ActivityDetail::class.java)
                        intent.putExtra("id_produk", viewHolder.produk.id_produk)
                        intent.putExtra("kategori", viewHolder.produk.kategori)
                        startActivity(intent)
                    }
                    override fun onItemLongClick(view:View, position:Int) {}
                })
                return viewHolder
            }
        }
        recyclerBeranda.adapter = firebaseRecyclerAdapter
    }
}