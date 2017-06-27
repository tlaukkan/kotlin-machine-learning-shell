package ml.data

import ml.util.ROOT_PATH
import ml.util.getDirectoryAbsolutePath
import org.apache.commons.io.FileUtils
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator
import org.nd4j.shade.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import java.io.File
import java.io.PrintWriter

private val log = LoggerFactory.getLogger("ml.data")

var DATA_PATH = getDirectoryAbsolutePath("${ROOT_PATH}/data")

fun loadDataSetMeta(set: String) : DataSetMeta {
    val directoryPath = getDirectoryAbsolutePath("${DATA_PATH}/$set")
    val metaFilePath = "$directoryPath/meta.json"

    val metaString = FileUtils.readFileToString(File(metaFilePath))

    val mapper = ObjectMapper()
    return mapper.readValue(File(metaFilePath), DataSetMeta::class.java)
}

fun saveDataSet(key: String, dateSetIterator: DataSetIterator): Unit {
    val directoryPath = getDirectoryAbsolutePath("${DATA_PATH}/$key")
    val metaFilePath = "$directoryPath/meta.json"

    var batchIndex = 0

    var rowCount = 0
    var columnCount = 0
    var featureColumnCount = 0
    var labelColumnCount = 0

    while(dateSetIterator.hasNext()) {
        log.info("Saving: $key batch $batchIndex")
        val dataSet = dateSetIterator.next()
        val dataSetIndexString = batchIndex.toString().padStart(4, '0')

        val filePath = "$directoryPath/$dataSetIndexString.csv"

        val out = PrintWriter(filePath)
        val features = dataSet.features
        val labels = dataSet.labels

        featureColumnCount = features.columns()
        labelColumnCount = labels.columns()
        columnCount = featureColumnCount + labelColumnCount

        for (r in 0..features.rows() - 1) {
            for (c in 0..features.columns() - 1) {
                if (c != 0) {
                    out.print(',')
                }
                out.print(features.getDouble(c))
            }
            for (c in 0..labels.columns() - 1) {
                if (c != 0) {
                    out.print(',')
                }
                out.print(labels.getDouble(c))
            }
            out.println()

            rowCount++
        }

        out.close()

        log.info("Saved: $key batch $batchIndex")
        batchIndex++
    }

    val setMeta = DataSetMeta(key, columnCount, rowCount, featureColumnCount, labelColumnCount)

     val mapper = ObjectMapper()
    FileUtils.write(File(metaFilePath), mapper.writeValueAsString(setMeta), false)

    log.info("Saved: " + key)
}