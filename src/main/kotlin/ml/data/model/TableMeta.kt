package ml.data.model

data class TableMeta(
        var columnCount:Int = 0,
        var rowCount:Int = 0,
        var minValue: Double = 0.0,
        var maxValue: Double = 0.0
)