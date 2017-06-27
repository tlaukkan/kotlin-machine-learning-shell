package ml.data.model

data class DataSetMeta(
        var key: String = "",
        var columnCount:Int = 0,
        var rowCount:Int = 0,
        var inputColumnCount: Int = 0,
        var outputColumnCount: Int = 0,
        var inputMinValue: Double = 0.0,
        var inputMaxValue: Double = 0.0,
        var outputMinValue: Double = 0.0,
        var outputMaxValue: Double = 0.0
)