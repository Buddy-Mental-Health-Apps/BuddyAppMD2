package com.example.buddyapp.helper

import android.content.Context
import com.example.buddyapp.ml.Surveyy
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

class QuizHelper(private val context: Context) {

    /**
     * Memproses prediksi dari model ML dengan jawaban user.
     * @param answers Array jawaban user dalam bentuk FloatArray (1-23 jawaban).
     * @return Array hasil prediksi (FloatArray) dari model.
     */
    fun predictResults(answers: FloatArray): FloatArray {
        // Inisialisasi model
        val model = Surveyy.newInstance(context)

        // Siapkan input untuk model
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 23), DataType.FLOAT32)
        inputFeature0.loadArray(answers)

        // Jalankan model untuk prediksi
        val outputs = model.process(inputFeature0)
        val predictions = outputs.outputFeature0AsTensorBuffer.floatArray
        println("Raw Predictions: ${predictions.joinToString()}")

        // Tutup model untuk menghemat resource
        model.close()

        // Kembalikan hasil prediksi
        return predictions
    }
}
