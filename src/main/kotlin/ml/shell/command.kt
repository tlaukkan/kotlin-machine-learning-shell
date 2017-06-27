package ml.shell

import ml.data.loadDataSetMeta
import ml.data.saveDataSet
import ml.data.saveDataSetImages
import org.slf4j.LoggerFactory

import org.deeplearning4j.datasets.iterator.impl.MnistDataSetIterator

private val log = LoggerFactory.getLogger("ml.data.command")

/**
 * Downloads MNIST train and test data sets.
 */
fun downloadDataMNIST() {
    print("Downloading MNIST data.\n")
    saveDataSet("mnist.train", MnistDataSetIterator(1000, true, 0))
    saveDataSet("mnist.test", MnistDataSetIterator(1000, false, 0))
    print("Downloaded MNIST data.\n")
}

/**
 * Describes data set.
 */
fun describeData(key: String) {
    val dataSetMeta = loadDataSetMeta(key)
    print("Data description:\n")
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

fun visualizeData(key: String, inputImageWidth: Int, outputImageWidth: Int) {
    saveDataSetImages(key, inputImageWidth, outputImageWidth)
    println("Saved data as images: $key")
}