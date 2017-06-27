package ml.data

import ml.shell.downloadDataMNIST
import ml.shell.visualizeData
import org.junit.Ignore
import org.junit.Test

internal class DataTest {

    @Test
    @Ignore
    fun testDownloadMNIST() {
        downloadDataMNIST()
    }

    @Test
    @Ignore
    fun testVisualize() {
        visualizeData("mnist.train", 28, 10)
    }

}