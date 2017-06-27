package ml.nn

import formDataSetFromTables
import ml.shell.downloadDataSetMNIST
import ml.shell.visualizeDataSet
import ml.shell.visualizeTable
import org.junit.Ignore
import org.junit.Test
import splitDataSetToTables

internal class MlpTest {

    @Test
    @Ignore
    fun testDownloadMNIST() {
        val numRows = 28
        val numColumns = 28
        val inputCount = numRows * numColumns
        val outputCount = 10 // number of output classes
        val batchSize = 1000 // batch size for each epoch
        val learningRate = batchSize / 1000.0 // learning rate
        val epochCount = (10 / learningRate).toInt() // number of epochs to perform

        val model = mlp(inputCount, 500, outputCount, learningRate)
        train(model, "mnist.train", "mnist.test", 1000, epochCount)
    }

}