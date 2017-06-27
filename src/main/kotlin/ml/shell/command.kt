package ml.shell

import deleteTable
import getTableKeys
import ml.data.*
import org.bubblecloud.logi.analysis.saveDataSetImages
import org.slf4j.LoggerFactory

import org.deeplearning4j.datasets.iterator.impl.MnistDataSetIterator
import splitDataSetToTables

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

/**
 * Splits data set to input and output table.
 */
fun splitDataSet(sourceDataSetKey: String, targetInputTableKey: String, targetOutputTableKey: String) {
    splitDataSetToTables(sourceDataSetKey, targetInputTableKey, targetOutputTableKey)
    print("Splitted $sourceDataSetKey to $targetInputTableKey and $targetOutputTableKey\n");
}

/**
 * Forms data set from input and output table.
 */
fun formDataSet(sourceInputTableKey: String, sourceOutputTableKey: String, targetDataSetKey: String) {
    print("Formed $targetDataSetKey from $sourceInputTableKey and $sourceOutputTableKey\n")
}

/**
 * Lists tables.
 */
fun listTables() {
    val keys = getTableKeys()
    print("Tables: \n")
    for (key in keys) {
        print("$key\n")
    }
}

/**
 * Removes tables.
 */
fun removeTable(key: String) {
    deleteTable(key)
    print("Removed table: $key")
}