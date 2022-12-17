package com.example.rentalev.pengguna.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rentalev.R
import com.example.rentalev.adapter.ViewholderInbox
import com.example.rentalev.model.Inbox
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.FirebaseDatabase

class FragmentInbox : Fragment() {
    lateinit var mLayoutManager: LinearLayoutManager
    lateinit var mRecyclerView: RecyclerView
    lateinit var SP: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_inbox, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        SP = requireActivity().applicationContext.getSharedPreferences("Pengguna", Context.MODE_PRIVATE)

        mLayoutManager = LinearLayoutManager(requireActivity())
        mRecyclerView = requireView().findViewById(R.id.recyclerInbox)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = mLayoutManager
    }

    override fun onStart() {
        super.onStart()
        val query = FirebaseDatabase.getInstance().getReference("inbox")
            .orderByChild("id_pengguna").equalTo(SP.getString("id_pengguna", "").toString())
        val firebaseRecyclerAdapter = object: FirebaseRecyclerAdapter<Inbox, ViewholderInbox>(
            Inbox::class.java,
            R.layout.list_inbox,
            ViewholderInbox::class.java,
            query
        ) {
            override fun populateViewHolder(viewHolder: ViewholderInbox, model: Inbox, position:Int) {
                viewHolder.setDetails(model)
            }
            override fun onCreateViewHolder(parent:ViewGroup, viewType:Int): ViewholderInbox {
                val viewHolder = super.onCreateViewHolder(parent, viewType)
                viewHolder.setOnClickListener(object: ViewholderInbox.ClickListener {
                    override fun onItemClick(view:View, position:Int) {}
                    override fun onItemLongClick(view:View, position:Int) {}
                })
                return viewHolder
            }
        }
        mRecyclerView.adapter = firebaseRecyclerAdapter
    }
}