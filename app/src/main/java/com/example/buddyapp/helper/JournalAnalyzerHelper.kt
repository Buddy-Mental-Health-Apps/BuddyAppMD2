package com.example.buddyapp.helper

import android.content.Context
import com.example.buddyapp.ml.JurnalModel
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder

fun journalAnalyzerHelper(context: Context, inputText: String): FloatArray {
    val byteBuffer: ByteBuffer = preprocessTextToByteBuffer(inputText)

    val model = JurnalModel.newInstance(context)

    val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 44), DataType.FLOAT32)
    inputFeature0.loadBuffer(byteBuffer)

    val outputs = model.process(inputFeature0)

    val outputFeature0 = outputs.outputFeature0AsTensorBuffer
    val result = outputFeature0.floatArray

    model.close()

    return result
}

fun preprocessTextToByteBuffer(inputText: String): ByteBuffer {
    val inputBytes = inputText.toByteArray(Charsets.UTF_8)
    val byteBuffer = ByteBuffer.allocateDirect(44 * 4)
    byteBuffer.order(ByteOrder.nativeOrder())

    for (i in inputBytes.indices) {
        if (i < 44) {
            byteBuffer.putFloat(inputBytes[i].toFloat())
        }
    }

    while (byteBuffer.position() < 44 * 4) {
        byteBuffer.putFloat(0f)
    }

    byteBuffer.rewind()
    return byteBuffer
}
