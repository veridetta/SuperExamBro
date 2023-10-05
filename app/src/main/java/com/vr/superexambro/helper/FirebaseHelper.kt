package com.vr.superexambro.helper

import android.view.View
import com.facebook.shimmer.ShimmerFrameLayout
import com.flurry.sdk.it
import com.google.firebase.firestore.FirebaseFirestore
import com.vr.superexambro.model.PaketModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

// Interface untuk callback generik
interface DataCallback<T> {
    fun onDataLoaded(datas: List<T>)
    fun onError(message: String)
}

// Fungsi readData dengan tipe data generik
fun <T> readDataFirebase(
    db: FirebaseFirestore,
    shimmerContainer: ShimmerFrameLayout,
    collection: String,
    dataType: Class<T>, // Tipe data yang sesuai dengan callback
    callback: DataCallback<T>
) {
    shimmerContainer.startShimmer() // Start shimmer effect
    GlobalScope.launch(Dispatchers.IO) {
        try {
            val result = db.collection(collection).get().await()
            val datas = mutableListOf<T>()
            for (document in result.documents) {
                val data = document.toObject(dataType)
                data?.let {
                    datas.add(it)
                }
            }

            withContext(Dispatchers.Main) {
                callback.onDataLoaded(datas) // Panggil callback dengan data yang dimuat
                shimmerContainer.stopShimmer() // Stop shimmer effect
                shimmerContainer.visibility = View.GONE // Hide shimmer container
            }
        } catch (e: Exception) {
            val errorMessage = "Error getting documents : $e"
            withContext(Dispatchers.Main) {
                callback.onError(errorMessage) // Panggil callback dengan pesan kesalahan
                shimmerContainer.stopShimmer() // Stop shimmer effect
                shimmerContainer.visibility = View.GONE // Hide shimmer container
            }
        }
    }
}
