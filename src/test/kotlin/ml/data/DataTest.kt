package ml.data

import formDataSet
import ml.shell.downloadDataSetMNIST
import ml.shell.visualizeDataSet
import org.junit.Ignore
import org.junit.Test
import splitDataSet

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
    fun testSplitDataSet() {
        splitDataSet("mnist.train", "mnist.train.input", "mnist.train.output")
    }

    @Test
    @Ignore
    fun testFormDataSet() {
        formDataSet("mnist.train.input", "mnist.train.input", "mnist.train.regression")
    }

    @Test
    @Ignore
    fun testVisualize2() {
        splitDataSet("mnist.train", "mnist.train.input", "mnist.train.output")
        formDataSet("mnist.train.input", "mnist.train.input", "mnist.train.regression")
        visualizeDataSet("mnist.train.regression", 28, 28)
    }

}