package ml.nn

import org.junit.Ignore
import org.junit.Test

internal class MlpTest {

    @Test
    @Ignore
    fun testMlp() {
        // MNIST image size
        val rows = 28
        val columns = 28

        // Input neuron count equals pixes in MNIST images
        val inputCount = rows * columns
        // Hidden neuron count could feasibly be between 300 - 1000
        val hiddenNeuronCount = 500
        // Output neuron count is ten as MNIST images contain hand written digits i.e. ten classes
        val outputCount = 10 // number of output classes

        // Maximum batch size for fast GPU aided computation
        val batchSize = 1000 // batch size for each epoch
        val learningRate = batchSize / 1000.0 // learning rate
        val epochCount = (10 / learningRate).toInt() // number of epochs (times entire training set is fitted to model)

        // Use multi layer perceptron.
        val model = mlp(inputCount, hiddenNeuronCount, outputCount, learningRate)
        train(model, "mnist.train", "mnist.test", batchSize, epochCount)
    }

}