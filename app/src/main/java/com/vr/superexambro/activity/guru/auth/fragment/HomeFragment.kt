package com.vr.superexambro.activity.guru.auth.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.vr.superexambro.R
import com.vr.superexambro.activity.guru.auth.EditActivity
import com.vr.superexambro.adapter.UjianAdapter
import com.vr.superexambro.model.PaketModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment() {

    private val mFirestore = FirebaseFirestore.getInstance()
    private lateinit var dataAdapter: UjianAdapter
    private lateinit var recyclerView: RecyclerView
    val TAG = "LOAD DATA"
    private val dataList: MutableList<PaketModel> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
        recyclerView = itemView.findViewById(R.id.rcData)
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(activity, 1)
            // set the custom adapter to the RecyclerView
            dataAdapter = UjianAdapter(
                dataList,
                requireContext(),
                { data -> editData(data) }
            )
        }
        val shimmerContainer = itemView.findViewById<ShimmerFrameLayout>(R.id.shimmerContainer)
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        uid?.let { readData(mFirestore,shimmerContainer, it) }

        recyclerView.adapter = dataAdapter

        val searchEditText = itemView.findViewById<EditText>(R.id.btnCari)
        dataAdapter.filter("")
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                dataAdapter.filter(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun readData(db: FirebaseFirestore, shimmerContainer: ShimmerFrameLayout, uid: String) {
        shimmerContainer.startShimmer() // Start shimmer effect
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val result = db.collection("paket").whereEqualTo("userId", uid).get().await()
                val datas = mutableListOf<PaketModel>()
                for (document in result) {
                    val data = document.toObject(PaketModel::class.java)
                    val docId = document.id
                    data.documentId = docId
                    datas.add(data)
                    Log.d(TAG, "Datanya : ${document.id} => ${document.data}")
                }

                withContext(Dispatchers.Main) {
                    dataList.addAll(datas)
                    dataAdapter.filteredDataList.addAll(datas)
                    dataAdapter.notifyDataSetChanged()
                    shimmerContainer.stopShimmer() // Stop shimmer effect
                    shimmerContainer.visibility = View.GONE // Hide shimmer container
                }
            } catch (e: Exception) {
                Log.w(TAG, "Error getting documents : $e")
                shimmerContainer.stopShimmer() // Stop shimmer effect
                shimmerContainer.visibility = View.GONE // Hide shimmer container
            }
        }
    }

    private fun editData(data: PaketModel) {
        //intent ke homeActivity fragment add
        val intent = Intent(requireContext(), EditActivity::class.java)
        intent.putExtra("documentId", data.documentId)
        intent.putExtra("uid", data.uid)
        intent.putExtra("userId", data.userId)
        intent.putExtra("mapel", data.mapel)
        intent.putExtra("kelas", data.kelas)
        intent.putExtra("namaUjian", data.namaUjian)
        intent.putExtra("url", data.url)
        intent.putExtra("shortUrl", data.shortUrl)
        intent.putExtra("key", data.key)
        intent.putExtra("durasi", data.durasi)
        startActivity(intent)
        requireActivity().finish()
    }

}