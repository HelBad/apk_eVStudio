package com.example.rentalev.pengguna.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.rentalev.R
import kotlinx.android.synthetic.main.fragment_beranda.*

class FragmentBeranda : Fragment() {

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
    }
}