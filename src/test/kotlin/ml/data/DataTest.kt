package ml.data

import formDataSetFromTables
import importCsvToTable
import loadTableMeta
import ml.shell.*
import org.bubblecloud.logi.analysis.saveTableAsImages
import org.junit.*
import splitDataSetToTables

internal class DataTest {

    @Before
    fun before() {
        ROOT_PATH = getDirectoryAbsolutePath("./build/root")
    }

    @After
    fun after() {
        ROOT_PATH = getDirectoryAbsolutePath("${System.getProperty("user.home")}/kotlin-machine-learning-shell")
    }

    @Test
    fun testCsvToDataSetPerformance() {
        copyCsv("./src/test/resources/data/csvs/test.data.input", "./build/root/data/csvs/test.data.input")
        copyCsv("./src/test/resources/data/csvs/test.data.output", "./build/root/data/csvs/test.data.output")
        importCsvToTable("test.data.input", "test.data.1.input")
        importCsvToTable("test.data.output", "test.data.1.output")
        formDataSetFromTables("test.data.1.input", "test.data.1.output", "test.data.1")
        splitDataSetToTables("test.data.1", "test.data.2.input", "test.data.2.output")
        formDataSetFromTables("test.data.2.input", "test.data.2.output", "test.data.2")
        saveTableAsImages("test.data.2.input", 28)
        saveTableAsImages("test.data.2.output", 10)

        val testData2InputMeta = loadTableMeta("test.data.2.input")
        Assert.assertEquals(784, testData2InputMeta.columnCount)
        Assert.assertEquals(2000, testData2InputMeta.rowCount)

        val testData2OutputMeta = loadTableMeta("test.data.2.output")
        Assert.assertEquals(10, testData2OutputMeta.columnCount)
        Assert.assertEquals(2000, testData2OutputMeta.rowCount)
    }

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