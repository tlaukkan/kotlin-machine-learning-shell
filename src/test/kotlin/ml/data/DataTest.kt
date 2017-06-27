package ml.data

import ml.shell.downloadMNIST
import ml.shell.visualizeDataSet
import org.junit.Ignore
import org.junit.Test

internal class DataTest {

    @Test
    @Ignore
    fun testDownloadMNIST() {
        downloadMNIST()
    }

    @Test
    @Ignore
    fun testVisualize() {
        visualizeDataSet("mnist-train", 28, 10)
    }

}