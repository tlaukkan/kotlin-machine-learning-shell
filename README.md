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
    train(model, "mnist.train", "mnist.test", 1000, epochCount)
    
## Data management

Place your folder containing CSV batches under "data/csvs/"

For example:

* data/csvs/train.input
* data/csvs/train.output
* data/csvs/test.input
* data/csvs/test.output

Import CSV to table:

    importCsv("data/csvs/train.input")
    importCsv("data/csvs/train.output")
    importCsv("data/csvs/test.input")
    importCsv("data/csvs/test.output")
    
Form data sets:

    formDataSet("train.input", "train.output", "train")
    formDataSet("test.input", "test.output", "test")

Visualize data set to verify correctness:

    visualizeDataSet("train", <inputSampleImageWidth>, <outputSampleImageWidth>)
    visualizeDataSet("test", <inputSampleImageWidth>, <outputSampleImageWidth>)