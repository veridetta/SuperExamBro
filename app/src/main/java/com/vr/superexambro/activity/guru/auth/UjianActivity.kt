package com.vr.superexambro.activity.guru.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.vr.superexambro.R
import com.vr.superexambro.adapter.SiswaUjianAdapter
import com.vr.superexambro.adapter.UjianAdapter
import com.vr.superexambro.helper.DataCallback
import com.vr.superexambro.helper.readDataFirebase
import com.vr.superexambro.helper.showSnackBar
import com.vr.superexambro.model.PaketModel
import com.vr.superexambro.model.UjianModel

class UjianActivity : AppCompatActivity() {
    private val mFirestore = FirebaseFirestore.getInstance()
    private lateinit var dataAdapter: SiswaUjianAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var shimmer: ShimmerFrameLayout
    private lateinit var etCari: EditText
    val TAG = "LOAD DATA"
    private val dataList: MutableList<UjianModel> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ujian)
        initView()
        readDataFirebase(mFirestore, shimmer, "ujian", UjianModel::class.java, callback)
    }

    fun initView(){
        shimmer = findViewById(R.id.shimmerContainer)
        etCari = findViewById(R.id.etCari)
        recyclerView = findViewById(R.id.rcData)
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(this@UjianActivity, 1)
            // set the custom adapter to the RecyclerView
            dataAdapter = SiswaUjianAdapter(
                dataList,
                this@UjianActivity
            )
        }
        recyclerView.adapter = dataAdapter
        dataAdapter.filter("")
        etCari.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                dataAdapter.filter(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    val callback = object : DataCallback<UjianModel> {
        override fun onDataLoaded(datas: List<UjianModel>) {
            // Misalnya, tambahkan datas ke adapter, dan sebagainya
            dataList.addAll(datas)
            dataAdapter.filteredDataList.addAll(datas)
            dataAdapter.notifyDataSetChanged()
        }

        override fun onError(message: String) {
            // Handle kesalahan, misalnya tampilkan pesan kesalahan kepada pengguna
            Log.d("LOAD",message)
        }
    }
}