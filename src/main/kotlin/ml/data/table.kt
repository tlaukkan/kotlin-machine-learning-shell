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
 * Gets table keys from local storage.
 */
fun getTableKeys(): List<String> {
    val directory = File(TABLE_PATH)
    val keys = mutableListOf<String>()
    for (file in directory.listFiles()) {
        if (file.isDirectory) {
            keys.add(file.name)
        }
    }
    return keys
}

/**
 * Deletes table from local storage.
 */
fun deleteTable(key: String) {
    val metaFilePath = "$TABLE_PATH/$key.json"
    val directoryPath = getDirectoryAbsolutePath("$TABLE_PATH/$key")
    File(metaFilePath).delete()
    File(directoryPath).deleteRecursively()
}


/**
 * Load data set meta data from local storage.
 */
fun loadTableMeta(key: String) : TableMeta {
    val metaFilePath = "$TABLE_PATH/$key.json"
    val mapper = ObjectMapper()
    return mapper.readValue(File(metaFilePath), TableMeta::class.java)
}

/**
 * Import CSV to table.
 */
fun importCsvToTable(sourceCsvKey: String, targetTableKey: String) {
    val sourceCsvDirectoryPath = "$CSV_PATH/$sourceCsvKey"
    val targetTableDirectoryPath = "$TABLE_PATH/$targetTableKey"
    val targetTableMetaFilePath = "$TABLE_PATH/$targetTableKey.json"
    val targetTableMeta = copyCsv(sourceCsvDirectoryPath, targetTableDirectoryPath)
    val mapper = ObjectMapper()
    FileUtils.write(File(targetTableMetaFilePath), mapper.writeValueAsString(targetTableMeta), false)
}

/**
 * Splits data set to tables.
 */
fun splitDataSetToTables(sourceDataSetKey: String, targetInputTableKey: String, targetOutputTableKey: String) {
    val sourceDataSetMeta = loadDataSetMeta(sourceDataSetKey)
    val sourceDirectoryPath = "$DATASET_PATH/$sourceDataSetKey"
    val targetInputDirectoryPath = "$TABLE_PATH/$targetInputTableKey"
    val targetOutputDirectoryPath = "$TABLE_PATH/$targetOutputTableKey"
    val targetInputTableMeta = splitCsv(sourceDirectoryPath, targetInputDirectoryPath, 0, sourceDataSetMeta.inputColumnCount - 1)
    val targetOutputTableMeta = splitCsv(sourceDirectoryPath, targetOutputDirectoryPath, sourceDataSetMeta.inputColumnCount, sourceDataSetMeta.inputColumnCount + sourceDataSetMeta.outputColumnCount - 1)
    val mapper = ObjectMapper()
    FileUtils.write(File("$targetInputDirectoryPath.json"), mapper.writeValueAsString(targetInputTableMeta), false)
    FileUtils.write(File("$targetOutputDirectoryPath.json"), mapper.writeValueAsString(targetOutputTableMeta), false)
}

/**
 * Forms data set from tables.
 */
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

