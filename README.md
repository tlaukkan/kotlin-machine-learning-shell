# Kotlin Machine Learning Shell

Machine learning functions for kotlin REPL shell.

Based on deeplearning4j library.

## Usage

Fork this repository.

Open it in IntelliJ Idea and launch Tools -> Kotlin -> Kotlin REPL (Shell)

Load MNIST data set of handwritten digits by typing to REPL the following and hitting CTRL + ENTER

    downloadDataSetMNIST()

List your data sets:

    listDataSets()

Verify your data set by visualizing it as greyscale images:

    visualizeDataSet("mnist.train", 28, 10)

View the images under user home directory: ./kotlin-machine-learning-shell/data/images

Train multi layer perceptron:

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

Output of training:

    [main] INFO ml.nn.train - Training begin... (10 epochs * 60 iterations)
    [main] INFO ml.nn.train - Epoch 0 begin...
    [main] INFO org.deeplearning4j.optimize.listeners.PerformanceListener - iteration 1; iteration time: 0 ms; samples/sec: Infinity; batches/sec: Infinity; 
    [main] INFO org.deeplearning4j.optimize.listeners.PerformanceListener - iteration 11; iteration time: 258 ms; samples/sec: 3875.969; batches/sec: 3.876; 
    [main] INFO org.deeplearning4j.optimize.listeners.PerformanceListener - iteration 21; iteration time: 259 ms; samples/sec: 3861.004; batches/sec: 3.861; 
    [main] INFO org.deeplearning4j.optimize.listeners.PerformanceListener - iteration 31; iteration time: 263 ms; samples/sec: 3802.281; batches/sec: 3.802; 
    [main] INFO org.deeplearning4j.optimize.listeners.PerformanceListener - iteration 41; iteration time: 259 ms; samples/sec: 3861.004; batches/sec: 3.861; 
    [main] INFO org.deeplearning4j.optimize.listeners.PerformanceListener - iteration 51; iteration time: 264 ms; samples/sec: 3787.879; batches/sec: 3.788; 
    [main] INFO ml.nn.train - Epoch 0 end. Accuracy: 0.9239 precision: 0.9234734639786781 recall: 0.9228546153267925 F1 score: 0.923163935940492
    [main] INFO ml.nn.train - Epoch 1 begin...
    [main] INFO org.deeplearning4j.optimize.listeners.PerformanceListener - iteration 61; iteration time: 2847 ms; samples/sec: 351.247; batches/sec: 0.351; 
    [main] INFO org.deeplearning4j.optimize.listeners.PerformanceListener - iteration 71; iteration time: 269 ms; samples/sec: 3717.472; batches/sec: 3.717; 
    [main] INFO org.deeplearning4j.optimize.listeners.PerformanceListener - iteration 81; iteration time: 266 ms; samples/sec: 3759.398; batches/sec: 3.759; 
    [main] INFO org.deeplearning4j.optimize.listeners.PerformanceListener - iteration 91; iteration time: 281 ms; samples/sec: 3558.719; batches/sec: 3.559; 
    [main] INFO org.deeplearning4j.optimize.listeners.PerformanceListener - iteration 101; iteration time: 248 ms; samples/sec: 4032.258; batches/sec: 4.032; 
    [main] INFO org.deeplearning4j.optimize.listeners.PerformanceListener - iteration 111; iteration time: 258 ms; samples/sec: 3875.969; batches/sec: 3.876; 
    [main] INFO ml.nn.train - Epoch 1 end. Accuracy: 0.9473 precision: 0.9470260715861236 recall: 0.946662019248046 F1 score: 0.946844010423438
    [main] INFO ml.nn.train - Epoch 2 begin...
    
## Data management

Place your folder containing CSV batches under "data/csvs/"

For example:

* data/csvs/train.input
* data/csvs/train.output
* data/csvs/test.input
* data/csvs/test.output

Import CSV to table:

    importCsv("train.input")
    importCsv("train.output")
    importCsv("test.input")
    importCsv("test.output")
    
Form data sets:

    formDataSet("train.input", "train.output", "train")
    formDataSet("test.input", "test.output", "test")

Visualize data set to verify correctness:

    visualizeDataSet("train", <inputSampleImageWidth>, <outputSampleImageWidth>)
    visualizeDataSet("test", <inputSampleImageWidth>, <outputSampleImageWidth>)