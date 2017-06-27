package org.bubblecloud.logi.analysis

import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator
import java.io.File
import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import java.awt.image.DataBufferByte

/**
 * Visualize data as images stored to given directory.
 */
fun visualize(imageDirectory: File, dataSetIterator: DataSetIterator, maxFeatureValue: Float, maxLabelValue: Float,featureImageWidth: Int, labelImageWidth: Int) : Unit {
    if (!imageDirectory.exists()) {
        imageDirectory.mkdir()
    }
    if (!imageDirectory.isDirectory) {
        throw IllegalArgumentException("Given path is not imageDirectory.")
    }

    var imageIndex = 0
    while (dataSetIterator.hasNext()) {
        val dataSet = dataSetIterator.next()

        val features = dataSet.features
        val labels = dataSet.labels

        val rowCount = features.rows()

        for (r in 0..rowCount - 1) {
            features.getRow(r)
            val imageIndexLabel = imageIndex.toString().padStart(10, '0')
            visualize(File("${imageDirectory.path}/${imageIndexLabel}F.png"),features.getRow(r), maxFeatureValue, featureImageWidth)
            visualize(File("${imageDirectory.path}/${imageIndexLabel}L.png"),labels.getRow(r), maxLabelValue, labelImageWidth)
            imageIndex++
        }

        imageIndex++
    }

}

/**
 * Visualize data as image stored to given file.
 */
fun visualize(imageFile: File, intArray: INDArray, maxValue: Float, maxWidth: Int) : Unit {
    val height = intArray.columns() / maxWidth
    val bufferedImage = BufferedImage(maxWidth, height, BufferedImage.TYPE_BYTE_GRAY)
    val a = (bufferedImage.raster.dataBuffer as DataBufferByte).data
    val data = ByteArray(intArray.columns())

    for (i in 0..intArray.columns() - 1) {
        var value = intArray.getFloat(i) / maxValue
        if (value < 0f) {
            value = 0f
        }
        if (value > 1.0f) {
            value = 1.0f
        }
        data[i] = (255.0 * value).toByte()
    }

    System.arraycopy(data, 0, a, 0, data.size)
    ImageIO.write(bufferedImage, "png", imageFile)
}