package ml.shell

import ml.data.loadDataSetMeta
import ml.data.saveDataSet
import org.slf4j.LoggerFactory

import org.deeplearning4j.datasets.iterator.impl.MnistDataSetIterator

private val log = LoggerFactory.getLogger("ml.data.command")

/**
 * Downloads MNIST train and test data sets.
 */
fun downloadMNIST() {
    print("Downloading MNIST data set.\n")
    saveDataSet("mnist-train", MnistDataSetIterator(1000, true, 0))
    saveDataSet("mnist-test", MnistDataSetIterator(1000, false, 0))
    print("Downloaded MNIST data set.\n")
}

/**
 * Views data set meta.
 */
fun viewDataSetMeta(key: String) {
    val dataSetMeta = loadDataSetMeta(key)
    print("Data set meta:\n")
    print("                 key: ${dataSetMeta.key}\n")
    print("        column count: ${dataSetMeta.columnCount}\n")
    print("           row count: ${dataSetMeta.rowCount}\n")
    print("  input column count: ${dataSetMeta.inputColumnCount}\n")
    print(" output column count: ${dataSetMeta.outputColumnCount}\n")
}