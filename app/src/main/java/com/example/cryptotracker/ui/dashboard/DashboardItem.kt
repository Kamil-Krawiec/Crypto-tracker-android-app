import java.time.Instant

/**
 * UI model that combines stored asset data with the latest live price.
 */
data class DashboardItem(
    val id: Long,
    val symbol: String,
    val name: String,
    val quantity: Double,
    val purchasePrice: Double,
    val purchaseTimestamp: Instant,
    val imageUrl: String?,
    val livePrice: Double
) {
    /** Current total value of this holding */
    val currentValue: Double
        get() = quantity * livePrice

    /** Total amount invested */
    val investedValue: Double
        get() = quantity * purchasePrice

    /** Profit or loss (currentValue - investedValue) */
    val profitLoss: Double
        get() = currentValue - investedValue
}
