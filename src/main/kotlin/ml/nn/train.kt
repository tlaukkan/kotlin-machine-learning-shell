package ml.nn

import ml.data.loadDataSet
import ml.data.loadDataSetMeta
import org.deeplearning4j.eval.Evaluation
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.deeplearning4j.optimize.listeners.PerformanceListener
import org.slf4j.LoggerFactory

private val log = LoggerFactory.getLogger("ml.nn.train")

/**
 * Trains model.
 */
fun train(model: MultiLayerNetwork, trainDataSetKey: String, testDataSetKey: String, batchSize: Int, epochCount: Int) {

    val startTimeMillis = System.currentTimeMillis()

    val mnistTrainMeta = loadDataSetMeta(trainDataSetKey)
    val mnistTrain = loadDataSet(trainDataSetKey, batchSize)
    val mnistTest = loadDataSet(testDataSetKey, batchSize)

    model.setListeners(PerformanceListener(10))

    log.info("Training begin... ($epochCount epochs * ${mnistTrainMeta.rowCount / batchSize} iterations)")
    for (i in 0..epochCount - 1) {
        log.info("Epoch $i begin...")
        model.fit(mnistTrain)

        val eval = Evaluation(mnistTrainMeta.outputColumnCount)
        while (mnistTest.hasNext()) {
            val next = mnistTest.next()
            val output = model.output(next.features)
            eval.eval(next.labels, output)
        }
        mnistTest.reset()

        log.info("Epoch $i end. Accuracy: " + eval.accuracy() + " precision: " + eval.precision() + " recall: " + eval.recall() + " F1 score: " + eval.f1())
    }

    log.info("Training end. Total time ${System.currentTimeMillis() - startTimeMillis} ms.")
}