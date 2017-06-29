package ml.data

import ml.data.model.TableMeta
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.lang.IndexOutOfBoundsException

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
 * Join two CSV files column wise.
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

    var targetPrinter = PrintWriter(File("${getCsvFileName(targetDirectoryPath, targetFileIndex)}"))
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
        val sourceOneReader = BufferedReader(FileReader(sourceOneFile))
        val sourceTwoReader = BufferedReader(FileReader(sourceTwoFile))

        while (true) {
            val lineOne = sourceOneReader.readLine()
            val lineTwo = sourceTwoReader.readLine()
            if (lineOne == null && lineTwo != null) {
                throw IndexOutOfBoundsException("Input files do not have equal number of lines.")
            }
            if (lineOne != null && lineTwo == null) {
                throw IndexOutOfBoundsException("Input files do not have equal number of lines.")
            }
            if (lineOne == null && lineTwo == null) {
                break
            }

            if (targetLineIndex == targetBatchSize) {
                targetLineIndex = 0
                targetFileIndex++
                targetPrinter.close()
                targetPrinter = PrintWriter(File("${getCsvFileName(targetDirectoryPath, targetFileIndex)}"))
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

            targetPrinter.print(lineOne)
            targetPrinter.print(',')
            targetPrinter.print(lineTwo)
            targetPrinter.println()

            targetLineIndex++
            targetColumnCount = lineOneValues.size + lineTwoValues.size
            targetLineCount++
        }
    }

    targetPrinter.close()

    return TableMeta(targetColumnCount, targetLineCount, minValue, maxValue)
}

/**
 * Split given columns from CSV file.
 */
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
    var targetPrinter = PrintWriter(File("${getCsvFileName(targetDirectoryPath, targetFileIndex)}"))

    var minValue = Double.MAX_VALUE
    var maxValue = Double.MIN_VALUE

    for (sourceFile in sourceDirectory.listFiles()) {
        sourceFile.forEachLine { line ->
            val values = line.split(',')

            if (targetLineIndex == targetBatchSize) {
                targetLineIndex = 0
                targetFileIndex++
                targetPrinter.close()
                targetPrinter = PrintWriter(File("${getCsvFileName(targetDirectoryPath, targetFileIndex)}"))
            }

            for (v in beginColumnIndex..endColumnIndex) {
                val value = values[v]
                if (v > beginColumnIndex) {
                    targetPrinter.print(',')
                }
                targetPrinter.print(value)
                val doubleValue = value.toDouble()
                minValue = Math.min(doubleValue, minValue)
                maxValue = Math.max(doubleValue, maxValue)
            }

            targetPrinter.println()

            targetLineIndex++
            targetLineCount++
        }
    }

    targetPrinter.close()

    return TableMeta(endColumnIndex - beginColumnIndex + 1, targetLineCount, minValue, maxValue)
}

/**
 * Copy CSV file.
 */
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
    var targetWriter = File("${getCsvFileName(targetDirectoryPath, targetFileIndex)}").printWriter()

    var minValue = Double.MAX_VALUE
    var maxValue = Double.MIN_VALUE

    for (sourceFile in sourceDirectory.listFiles()) {
        sourceFile.forEachLine { line ->
            val values = line.split(',')

            if (targetLineIndex == targetBatchSize) {
                targetLineIndex = 0
                targetFileIndex++
                targetWriter.close()
                targetWriter = File("${getCsvFileName(targetDirectoryPath, targetFileIndex)}").printWriter()
            }

            for (v in 0..values.size - 1) {
                val value = values[v]
                if (v > 0) {
                    targetWriter.print(',')
                }
                targetWriter.print(value)
                val doubleValue = value.toDouble()
                minValue = Math.min(doubleValue, minValue)
                maxValue = Math.max(doubleValue, maxValue)
            }

            targetWriter.println()

            targetLineIndex++
            targetLineCount++
            targetColumnCount = values.size

        }
    }

    targetWriter.close()

    return TableMeta(targetColumnCount, targetLineCount, minValue, maxValue)
}

/**
 * Gets CSF file name from parent directory path and child file index.
 */
private fun getCsvFileName(directoryPath: String, fileIndex: Int) : String {
    return "$directoryPath/${fileIndex.toString().padStart(4, '0')}.csv"
}