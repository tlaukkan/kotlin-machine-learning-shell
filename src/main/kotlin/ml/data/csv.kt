package ml.data

import ml.data.model.TableMeta
import ml.util.getDirectoryAbsolutePath
import org.apache.commons.io.FileUtils
import java.io.File

var CSV_PATH = getDirectoryAbsolutePath("$DATA_PATH/csvs")

/**
 * Gets CSV keys from local storage.
 */
fun getCsvKeys(): List<String> {
    val directory = File(CSV_PATH)
    val keys = mutableListOf<String>()
    for (file in directory.listFiles()) {
        if (file.isDirectory) {
            keys.add(file.name)
        }
    }
    return keys
}

/**
 * Deletes CSV from local storage.
 */
fun deleteCsv(key: String) {
    val metaFilePath = "$CSV_PATH/$key.json"
    val directoryPath = getDirectoryAbsolutePath("$CSV_PATH/$key")
    File(metaFilePath).delete()
    File(directoryPath).deleteRecursively()
}

/**
 * Created by tlaukkan on 6/27/2017.
 */
fun joinCsv(sourceOneDirectoryPath: String, sourceTwoDirectoryPath: String, targetDirectoryPath: String) : TableMeta {
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

    var targetColumnCount = 0
    var targetLineCount = 0

    var targetFile = File("${getFileName(targetDirectoryPath, targetFileIndex)}")
    var minValue = Double.MAX_VALUE
    var maxValue = Double.MIN_VALUE

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

            val lineOneValues = lineOne.split(',')
            for (value in lineOneValues) {
                val doubleValue = value.toDouble()
                minValue = Math.min(doubleValue, minValue)
                maxValue = Math.max(doubleValue, maxValue)
            }

            val lineTwoValues = lineTwo.split(',')
            for (value in lineTwo.split(',')) {
                val doubleValue = value.toDouble()
                minValue = Math.min(doubleValue, minValue)
                maxValue = Math.max(doubleValue, maxValue)
            }

            val lineBuilder = StringBuilder()

            lineBuilder.append(lineOne)
            lineBuilder.append(',')
            lineBuilder.append(lineTwo)
            lineBuilder.append('\n')

            FileUtils.write(targetFile, lineBuilder.toString(), true)

            targetLineIndex++
            targetColumnCount = lineOneValues.size + lineTwoValues.size
            targetLineCount++
        }
    }

    return TableMeta(targetColumnCount, targetLineCount, minValue, maxValue)
}

fun splitCsv(sourceDirectoryPath: String, targetDirectoryPath: String, beginColumnIndex: Int, endColumnIndex: Int) : TableMeta {
    val sourceDirectory = File(sourceDirectoryPath)
    val targetDirectory = File(targetDirectoryPath)

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

    return TableMeta(endColumnIndex - beginColumnIndex + 1, targetLineCount, minValue, maxValue)
}

fun copyCsv(sourceDirectoryPath: String, targetDirectoryPath: String) : TableMeta {
    val sourceDirectory = File(sourceDirectoryPath)
    val targetDirectory = File(targetDirectoryPath)

    if (targetDirectory.exists()) {
        targetDirectory.deleteRecursively()
    }

    targetDirectory.mkdir()

    var targetBatchSize = 1000
    var targetFileIndex = 0
    var targetLineIndex = 0
    var targetColumnCount = 0
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

            for (v in 0..values.size - 1) {
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
            targetColumnCount = values.size
        }
    }

    return TableMeta(targetColumnCount, targetLineCount, minValue, maxValue)
}

fun getFileName(directoryPath: String, fileIndex: Int) : String {
    return "$directoryPath/${fileIndex.toString().padStart(4, '0')}.csv"
}