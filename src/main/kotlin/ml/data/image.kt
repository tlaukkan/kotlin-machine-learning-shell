package org.bubblecloud.logi.analysis

import TABLE_PATH
import loadTableMeta
import ml.data.DATA_PATH
import ml.data.loadDataSet
import ml.data.loadDataSetMeta
import ml.data.getDirectoryAbsolutePath
import org.apache.commons.io.FileUtils
import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator
import org.slf4j.LoggerFactory
import java.io.File
import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import java.awt.image.DataBufferByte

private val log = LoggerFactory.getLogger("ml.visualize")

var IMAGE_PATH = getDirectoryAbsolutePath("$DATA_PATH/images")

/**
 * Saves dataset image to local storage.
 */
fun saveDataSetImages(key: String, inputImageWidth: Int, outputImageWidth: Int) {
    val directoryPath = getDirectoryAbsolutePath("${IMAGE_PATH}/$key")
    val dataSetMeta = loadDataSetMeta(key)
    val dataSetIterator = loadDataSet(key, 1000)

    saveDataSetImages(directoryPath, dataSetIterator, dataSetMeta.inputMinValue, dataSetMeta.inputMaxValue, dataSetMeta.outputMinValue, dataSetMeta.outputMaxValue, inputImageWidth, outputImageWidth)
}

/**
 * Visualize data as images stored to given directory.
 */
fun saveDataSetImages(imageDirectoryPath: String, dataSetIterator: DataSetIterator, minInputValue: Double, maxInputValue: Double, minOutputValue: Double, maxOutputValue: Double, featureImageWidth: Int, labelImageWidth: Int) : Unit {
    val imageDirectory = File(imageDirectoryPath)
    log.info("Saving images to : ${imageDirectory.path}")

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
            val imageIndexLabel = imageIndex.toString().padStart(10, '0')
            saveDataArrayImage("${imageDirectory.path}/${imageIndexLabel}_input.png", features.getRow(r), minInputValue, maxInputValue, featureImageWidth)
            saveDataArrayImage("${imageDirectory.path}/${imageIndexLabel}_output.png", labels.getRow(r), minInputValue, maxOutputValue, labelImageWidth)
            imageIndex++
        }
    }

}

/**
 * Visualize data as image stored to given file.
 */
fun saveDataArrayImage(imagePath: String, dataArray: INDArray, minValue: Double, maxValue: Double, imageWidth: Int) : Unit {
    val imageFile = File(imagePath)
    val height = dataArray.columns() / imageWidth
    val bufferedImage = BufferedImage(imageWidth, height, BufferedImage.TYPE_BYTE_GRAY)
    val a = (bufferedImage.raster.dataBuffer as DataBufferByte).data
    val data = ByteArray(dataArray.columns())

    for (i in 0..dataArray.columns() - 1) {
        var value = (dataArray.getDouble(i) -  minValue) / (maxValue - minValue)
        if (value < 0.0) {
            value = 0.0
        }
        if (value > 1.0) {
            value = 1.0
        }
        data[i] = (255.0 * value).toByte()
    }

    System.arraycopy(data, 0, a, 0, data.size)
    ImageIO.write(bufferedImage, "png", imageFile)
}

/**
 * Saves table images to local storage.
 */
fun saveTableAsImages(key: String, imageWidth: Int) {
    val tableDirectoryPath = getDirectoryAbsolutePath("$TABLE_PATH/$key")
    val imageDirectoryPath = getDirectoryAbsolutePath("${IMAGE_PATH}/$key")
    val tableMeta = loadTableMeta(key)

    saveTableAsImages(tableDirectoryPath, imageDirectoryPath, tableMeta.minValue, tableMeta.maxValue, imageWidth)
}

/**
 * Saves table images to local storage.
 */
fun saveTableAsImages(tableDirectoryPath: String, imageDirectoryPath: String, minValue: Double, maxValue: Double, imageWidth: Int) : Unit {
    val tableDirectory = File(tableDirectoryPath)
    val imageDirectory = File(imageDirectoryPath)
    log.info("Saving images to : ${imageDirectory.path}")

    if (!imageDirectory.exists()) {
        imageDirectory.mkdir()
    }
    if (!imageDirectory.isDirectory) {
        throw IllegalArgumentException("Given path is not imageDirectory.")
    }

    var imageIndex = 0
    for (sourceFile in tableDirectory.listFiles()) {
        val lines = FileUtils.readLines(sourceFile, "UTF-8")
        for (line in lines) {
            val imageIndexLabel = imageIndex.toString().padStart(10, '0')
            saveLineImage("${imageDirectory.path}/${imageIndexLabel}.png", line, minValue, maxValue, imageWidth)
            imageIndex++
        }
    }

}

/**
 * Saves line image to local storage.
 */
fun saveLineImage(imagePath: String, line: String, minValue: Double, maxValue: Double, imageWidth: Int) : Unit {
    val imageFile = File(imagePath)
    val values = line.split(',')
    val height = values.size / imageWidth
    val bufferedImage = BufferedImage(imageWidth, height, BufferedImage.TYPE_BYTE_GRAY)
    val a = (bufferedImage.raster.dataBuffer as DataBufferByte).data
    val data = ByteArray(values.size)

    for (i in 0..values.size - 1) {
        val rawValue = values[i].toDouble()
        var value = (rawValue -  minValue) / (maxValue - minValue)
        if (value < 0.0) {
            value = 0.0
        }
        if (value > 1.0) {
            value = 1.0
        }
        data[i] = (255.0 * value).toByte()
    }

    System.arraycopy(data, 0, a, 0, data.size)
    ImageIO.write(bufferedImage, "png", imageFile)
}
