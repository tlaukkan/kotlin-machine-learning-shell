package ml.data

data class DataSetMeta(
        var key: String = "",
        var columnCount:Int = 0,
        var rowCount:Int = 0,
        var inputColumnCount: Int = 0,
        var outputColumnCount: Int = 0)