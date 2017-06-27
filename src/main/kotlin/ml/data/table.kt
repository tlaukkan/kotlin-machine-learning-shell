import ml.data.DATA_PATH
import ml.data.DATASET_PATH
import ml.data.loadDataSetMeta
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

fun splitDataSet(sourceDataSetKey: String, targetInputTableKey: String, targetOutputTableKey: String) {
    val sourceDataSetMeta = loadDataSetMeta(sourceDataSetKey)
    val sourceDirectoryPath = "$DATASET_PATH/$sourceDataSetKey"
    val targetInputDirectoryPath = "$TABLE_PATH/$targetInputTableKey"
    val targetOutputDirectoryPath = "$TABLE_PATH/$targetOutputTableKey"
    splitCsv(sourceDirectoryPath,targetInputDirectoryPath, 0, sourceDataSetMeta.inputColumnCount - 1)
    splitCsv(sourceDirectoryPath,targetOutputDirectoryPath, 0, sourceDataSetMeta.inputColumnCount - 1)
}

fun formDataSet(sourceInputTableKey: String, sourceOutputTableKey: String, targetDataSetKey: String) {
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

fun joinCsv(sourceOneDirectoryPath: String, sourceTwoDirectoryPath: String, targetDirectoryPath: String) {
    val sourceOneDirectory = File(sourceOneDirectoryPath)
    val sourceTwoDirectory = File(sourceTwoDirectoryPath)
    val targetDirectory = File(targetDirectoryPath)

    if (targetDirectory.exists()) {
        targetDirectory.deleteRecursively()
    }

    targetDirectory.mkdir()

    var targetBatchSize = 1000
    var targetFileIndex = 0
    var targetLineIndex = 0
    var targetFile = File("${getFileName(targetDirectoryPath, targetFileIndex)}")

    val sourceOneFiles = sourceOneDirectory.listFiles()
    val sourceTwoFiles = sourceTwoDirectory.listFiles()

    if (sourceOneFiles.size != sourceTwoFiles.size) {
        throw IllegalArgumentException("Source directories do not have same amount of files.")
    }

    for (f in 0..sourceOneFiles.size - 1) {
        val sourceOneFile = sourceOneFiles[f]
        val sourceTwoFile = sourceTwoFiles[f]
        val sourceOneLines = FileUtils.readLines(sourceOneFile, "UTF-8")
        val sourceTwoLines = FileUtils.readLines(sourceTwoFile, "UTF-8")

        if (sourceOneLines.size != sourceTwoLines.size) {
            throw IllegalArgumentException("Source files do not have same amount of lines.")
        }

        for (l in 0..sourceOneLines.size - 1) {
            val lineOne = sourceOneLines[l]
            val lineTwo = sourceTwoLines[l]

            if (targetLineIndex == targetBatchSize) {
                targetLineIndex = 0
                targetFileIndex++
                targetFile = File("${getFileName(targetDirectoryPath, targetFileIndex)}")
            }

            val lineBuilder = StringBuilder()

            lineBuilder.append(lineOne)
            lineBuilder.append(',')
            lineBuilder.append(lineTwo)
            lineBuilder.append('\n')

            FileUtils.write(targetFile, lineBuilder.toString(), true)

            targetLineIndex++
        }
    }
}

fun splitCsv(sourceDirectoryPath: String, targetDirectoryPath: String, beginColumnIndex: Int, endColumnIndex: Int) {
    val sourceDirectory = File(sourceDirectoryPath)
    val targetDirectory = File(targetDirectoryPath)
    val targetMetaFilePath = "$targetDirectoryPath.json"

    if (targetDirectory.exists()) {
        targetDirectory.deleteRecursively()
    }

    targetDirectory.mkdir()

    var targetBatchSize = 1000
    var targetFileIndex = 0
    var targetLineIndex = 0
    var targetLineCount = 0
    var targetFile = File("${getFileName(targetDirectoryPath, targetFileIndex)}")

    var minValue = Double.MAX_VALUE
    var maxValue = Double.MIN_VALUE

    for (sourceFile in sourceDirectory.listFiles()) {
        val lines = FileUtils.readLines(sourceFile, "UTF-8")
        for (line in lines) {
            val values = line.split(',')

            if (targetLineIndex == targetBatchSize) {
                targetLineIndex = 0
                targetFileIndex++
                targetFile = File("${getFileName(targetDirectoryPath, targetFileIndex)}")
            }

            val lineBuilder = StringBuilder()

            for (v in beginColumnIndex..endColumnIndex) {
                val value = values[v]
                if (lineBuilder.length > 0) {
                    lineBuilder.append(',')
                }
                lineBuilder.append(value)
                val doubleValue = value.toDouble()
                minValue = Math.min(doubleValue, minValue)
                maxValue = Math.max(doubleValue, maxValue)
            }

            lineBuilder.append('\n')

            FileUtils.write(targetFile, lineBuilder.toString(), true)

            targetLineIndex++
            targetLineCount++
        }
    }

    val mapper = ObjectMapper()
    val tableMeta = TableMeta(endColumnIndex - beginColumnIndex + 1, targetLineCount, minValue, maxValue)
    FileUtils.write(File(targetMetaFilePath), mapper.writeValueAsString(tableMeta), false)
}

fun getFileName(directoryPath: String, fileIndex: Int) : String {
    return "$directoryPath/${fileIndex.toString().padStart(4, '0')}.csv"
}