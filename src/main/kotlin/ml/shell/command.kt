package ml.shell

import ml.data.*
import org.bubblecloud.logi.analysis.saveDataSetImages
import org.slf4j.LoggerFactory

import org.deeplearning4j.datasets.iterator.impl.MnistDataSetIterator

private val log = LoggerFactory.getLogger("ml.data.command")

/**
 * Downloads MNIST train and test data sets.
 */
fun downloadDataSetMNIST() {
    print("Downloading MNIST dataset.\n")
    saveDataSet("mnist.train", MnistDataSetIterator(1000, true, 0))
    saveDataSet("mnist.test", MnistDataSetIterator(1000, false, 0))
    print("Downloaded MNIST dataset.\n")
}

/**
 * Describes data set.
 */
fun describeDataSet(key: String) {
    val dataSetMeta = loadDataSetMeta(key)
    print("Dataset description:\n")
    print("                 key: ${dataSetMeta.key}\n")
    print("        column count: ${dataSetMeta.columnCount}\n")
    print("           row count: ${dataSetMeta.rowCount}\n")
    print("  input column count: ${dataSetMeta.inputColumnCount}\n")
    print(" output column count: ${dataSetMeta.outputColumnCount}\n")
    print("     input min value: ${dataSetMeta.inputMinValue}\n")
    print("     input max value: ${dataSetMeta.inputMaxValue}\n")
    print("    output min value: ${dataSetMeta.outputMinValue}\n")
    print("    output max value: ${dataSetMeta.outputMaxValue}\n")
}

/**
 * Lists data set.
 */
fun listDataSets() {
    val keys = getDataSetKeys()
    print("Datasets: \n")
    for (key in keys) {
        print("$key\n")
    }
}

/**
 * Removes data set.
 */
fun removeDataSet(key: String) {
    deleteDataSet(key)
    print("Removed dataset: $key")
}

/**
 * Visualizes data set as grayscale images.
 */
fun visualizeDataSet(key: String, inputImageWidth: Int, outputImageWidth: Int) {
    saveDataSetImages(key, inputImageWidth, outputImageWidth)
    println("Saved dataset as images: $key")
}