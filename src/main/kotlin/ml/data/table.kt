import ml.data.*
import ml.data.model.DataSetMeta
import ml.data.model.TableMeta
import ml.util.getDirectoryAbsolutePath
import org.slf4j.LoggerFactory
import java.io.File
import org.apache.commons.io.FileUtils
import org.nd4j.shade.jackson.databind.ObjectMapper


private val log = LoggerFactory.getLogger("ml.data.table")

var TABLE_PATH = getDirectoryAbsolutePath("$DATA_PATH/tables")

/**
 * Load data set meta data from local storage.
 */
fun loadTableMeta(key: String) : TableMeta {
    val metaFilePath = "$TABLE_PATH/$key.json"
    val mapper = ObjectMapper()
    return mapper.readValue(File(metaFilePath), TableMeta::class.java)
}

fun splitDataSetToTables(sourceDataSetKey: String, targetInputTableKey: String, targetOutputTableKey: String) {
    val sourceDataSetMeta = loadDataSetMeta(sourceDataSetKey)
    val sourceDirectoryPath = "$DATASET_PATH/$sourceDataSetKey"
    val targetInputDirectoryPath = "$TABLE_PATH/$targetInputTableKey"
    val targetOutputDirectoryPath = "$TABLE_PATH/$targetOutputTableKey"
    val targetInputTableMeta = splitCsv(sourceDirectoryPath, targetInputDirectoryPath, 0, sourceDataSetMeta.inputColumnCount - 1)
    val targetOutputTableMeta = splitCsv(sourceDirectoryPath, targetOutputDirectoryPath, 0, sourceDataSetMeta.inputColumnCount - 1)
    val mapper = ObjectMapper()
    FileUtils.write(File("$targetInputDirectoryPath.json"), mapper.writeValueAsString(targetInputTableMeta), false)
    FileUtils.write(File("$targetOutputDirectoryPath.json"), mapper.writeValueAsString(targetOutputTableMeta), false)

}

fun formDataSetFromTables(sourceInputTableKey: String, sourceOutputTableKey: String, targetDataSetKey: String) {
    val sourceInputDirectoryPath = "$TABLE_PATH/$sourceInputTableKey"
    val sourceOutputDirectoryPath = "$TABLE_PATH/$sourceOutputTableKey"
    val targetDirectoryPath = "$DATASET_PATH/$targetDataSetKey"
    val targetDataSetMetaFilePath = "$DATASET_PATH/$targetDataSetKey.json"
    joinCsv(sourceInputDirectoryPath, sourceOutputDirectoryPath, targetDirectoryPath)

    val sourceInputTableMeta = loadTableMeta(sourceInputTableKey)
    val sourceOutputTableMeta = loadTableMeta(sourceOutputTableKey)

    val targetDataSetMeta = DataSetMeta(targetDirectoryPath,
            sourceInputTableMeta.columnCount + sourceOutputTableMeta.columnCount,
            sourceInputTableMeta.rowCount,
            sourceInputTableMeta.columnCount,
            sourceOutputTableMeta.columnCount,
            sourceInputTableMeta.minValue,
            sourceInputTableMeta.maxValue,
            sourceOutputTableMeta.minValue,
            sourceOutputTableMeta.maxValue
            )

    val mapper = ObjectMapper()
    FileUtils.write(File(targetDataSetMetaFilePath), mapper.writeValueAsString(targetDataSetMeta), false)
}

