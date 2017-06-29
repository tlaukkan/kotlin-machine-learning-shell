package ml.data

import ml.data.model.DataSetMeta
import org.apache.commons.io.FileUtils
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator
import org.nd4j.shade.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import java.io.File
import java.io.PrintWriter
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator
import org.datavec.api.split.FileSplit
import org.datavec.api.records.reader.impl.csv.CSVRecordReader

private val log = LoggerFactory.getLogger("ml.data.dataset")

var DATASET_PATH = getDirectoryAbsolutePath("$DATA_PATH/datasets")

/**
 * Gets data set keys from local storage.
 */
fun getDataSetKeys(): List<String> {
    val directory = File(DATASET_PATH)
    val keys = mutableListOf<String>()
    for (file in directory.listFiles()) {
        if (file.isDirectory) {
            keys.add(file.name)
        }
    }
    return keys
}

/**
 * Deletes data set from local storage.
 */
fun deleteDataSet(key: String) {
    val metaFilePath = "$DATASET_PATH/$key.json"
    val directoryPath = getDirectoryAbsolutePath("$DATASET_PATH/$key")
    File(metaFilePath).delete()
    File(directoryPath).deleteRecursively()
}

/**
 * Load data set meta data from local storage.
 */
fun loadDataSetMeta(key: String) : DataSetMeta {
    val metaFilePath = "$DATASET_PATH/$key.json"
    val mapper = ObjectMapper()
    return mapper.readValue(File(metaFilePath), DataSetMeta::class.java)
}

/**
 * Load data set from local storage.
 */
fun loadDataSet(key: String, batchSize: Int) : DataSetIterator {
    val directoryPath = getDirectoryAbsolutePath("$DATASET_PATH/$key")
    val dataSetMeta = loadDataSetMeta(key)

    val csvRecordReader = CSVRecordReader(0, ",")
    csvRecordReader.initialize(FileSplit(File(directoryPath)))

    return RecordReaderDataSetIterator(csvRecordReader, batchSize,
            dataSetMeta.inputColumnCount, dataSetMeta.inputColumnCount + dataSetMeta.outputColumnCount - 1, true)
}

/**
 * Save data set to local storage.
 */
fun saveDataSet(key: String, dateSetIterator: DataSetIterator): Unit {
    val directoryPath = getDirectoryAbsolutePath("$DATASET_PATH/$key")
    val metaFilePath = "$DATASET_PATH/$key.json"

    var batchIndex = 0

    var rowCount = 0
    var columnCount = 0
    var featureColumnCount = 0
    var labelColumnCount = 0

    var minInputValue: Double = Double.MAX_VALUE
    var maxInputValue: Double = Double.MIN_VALUE
    var minOutputValue: Double = Double.MAX_VALUE
    var maxOutputValue: Double = Double.MIN_VALUE

    while(dateSetIterator.hasNext()) {
        log.info("Saving: $key batch $batchIndex")
        val dataSet = dateSetIterator.next()
        val dataSetIndexString = batchIndex.toString().padStart(4, '0')

        val filePath = "$directoryPath/$dataSetIndexString.csv"

        val out = PrintWriter(filePath)
        val featureMatrix = dataSet.features
        val labelMatrix = dataSet.labels

        featureColumnCount = featureMatrix.columns()
        labelColumnCount = labelMatrix.columns()
        columnCount = featureColumnCount + labelColumnCount

        for (r in 0..featureMatrix.rows() - 1) {

            val featureRow = featureMatrix.getRow(r)
            val labelRow = labelMatrix.getRow(r)

            for (c in 0..featureColumnCount - 1) {
                if (c != 0) {
                    out.print(',')
                }
                val value = featureRow.getDouble(c)
                minInputValue = Math.min(value, minInputValue)
                maxInputValue = Math.max(value, maxInputValue)
                out.print(value)
            }
            for (c in 0..labelColumnCount - 1) {
                out.print(',')
                val value = labelRow.getDouble(c)
                minOutputValue = Math.min(value, minOutputValue)
                maxOutputValue = Math.max(value, maxOutputValue)
                out.print(value)
            }
            out.println()

            rowCount++
        }

        out.close()

        log.info("Saved: $key batch $batchIndex")
        batchIndex++
    }

    val setMeta = DataSetMeta(
            key,
            columnCount, rowCount,
            featureColumnCount, labelColumnCount,
            minInputValue, maxInputValue, minOutputValue, maxOutputValue)

     val mapper = ObjectMapper()
    FileUtils.write(File(metaFilePath), mapper.writeValueAsString(setMeta), false)

    log.info("Saved: " + key)
}

