package ml.data

import java.io.File

var ROOT_PATH = getDirectoryAbsolutePath("${System.getProperty("user.home")}/kotlin-machine-learning-shell")

fun getDirectoryAbsolutePath(directoryPath: String) : String {
    val directory = File(directoryPath)
    if (!File(directoryPath).exists()) {
        File(directoryPath).mkdir()
    }
    return directory.absolutePath
}