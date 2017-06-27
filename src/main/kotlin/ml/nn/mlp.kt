package ml.nn

import org.deeplearning4j.nn.api.OptimizationAlgorithm
import org.deeplearning4j.nn.conf.NeuralNetConfiguration
import org.deeplearning4j.nn.conf.Updater
import org.deeplearning4j.nn.conf.layers.DenseLayer
import org.deeplearning4j.nn.conf.layers.OutputLayer
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.deeplearning4j.nn.weights.WeightInit
import org.nd4j.linalg.activations.Activation
import org.nd4j.linalg.lossfunctions.LossFunctions

/**
 * Construct default multilayer perceptron.
 */
fun mlp(inputCount: Int, hiddenNeuronCount: Int, outputCount: Int, learningRate: Double) : MultiLayerNetwork {
    val conf = NeuralNetConfiguration.Builder()
            .seed(123)
            .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
            .iterations(1)
            .activation(Activation.RELU)
            .weightInit(WeightInit.XAVIER)
            .learningRate(learningRate)
            .updater(Updater.ADAM)
            .list()
            .layer(0, DenseLayer.Builder()
                    .nIn(inputCount)
                    .nOut(hiddenNeuronCount)
                    .build())
            .layer(1, OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                    .activation(Activation.SOFTMAX)
                    .nIn(hiddenNeuronCount)
                    .nOut(outputCount)
                    .build())
            .pretrain(false).backprop(true)
            .build()

    val model = MultiLayerNetwork(conf)
    model.init()
    return model
}