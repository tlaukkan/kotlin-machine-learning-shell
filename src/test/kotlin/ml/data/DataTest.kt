package ml.data

import joinInputAndOutputTable
import splitInputTable
import ml.shell.downloadDataSetMNIST
import ml.shell.visualizeDataSet
import org.junit.Ignore
import org.junit.Test

internal class DataTest {

    @Test
    @Ignore
    fun testDownloadMNIST() {
        downloadDataSetMNIST()
    }

    @Test
    @Ignore
    fun testVisualize() {
        visualizeDataSet("mnist.train", 28, 10)
    }

    @Test
    @Ignore
    fun testExtractInput() {
        splitInputTable("mnist.train", "mnist.train.input")
    }

    @Test
    @Ignore
    fun testJoinInputAndOutput() {
        joinInputAndOutputTable("mnist.train.input", "mnist.train.input", "mnist.train.regression")
    }

    @Test
    @Ignore
    fun testVisualize2() {
        visualizeDataSet("mnist.train.regression", 28, 28)
    }

}