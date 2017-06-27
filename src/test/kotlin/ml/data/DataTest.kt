package ml.data

import formDataSetFromTables
import ml.shell.downloadDataSetMNIST
import ml.shell.formDataSet
import ml.shell.visualizeDataSet
import ml.shell.visualizeTable
import org.junit.Ignore
import org.junit.Test
import splitDataSetToTables

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
        splitDataSetToTables("mnist.train", "mnist.train.input", "mnist.train.output")
    }

    @Test
    @Ignore
    fun testFormDataSet() {
        formDataSet("mnist.train.input", "mnist.train.input", "mnist.train.regression")
    }

    @Test
    @Ignore
    fun testVisualize2() {
        visualizeTable("mnist.train.output", 10)
    }



}