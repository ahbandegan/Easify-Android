package ir.amirhesambandegan.easify_format

import kotlin.math.max

/**
 * Calculates the similarity percentage between this string and another string
 * based on the Levenshtein distance.
 *
 * @param other The string to compare against.
 * @return A double value between 0.0 and 100.0 representing the similarity percentage.
 */
fun String.similarityPercentage(other: String): Double {
    if (this == other) return 100.0
    if (this.isEmpty() || other.isEmpty()) return 0.0

    val maxLen = max(this.length, other.length)
    val distance = levenshteinDistance(this.lowercase(), other.lowercase())
    return (1.0 - (distance.toDouble() / maxLen)) * 100
}

/**
 * Computes the Levenshtein distance between two character sequences.
 *
 * @param lhs The first character sequence.
 * @param rhs The second character sequence.
 * @return The minimum number of single-character edits (insertions, deletions, or substitutions)
 * required to change one sequence into the other.
 */
private fun levenshteinDistance(lhs: CharSequence, rhs: CharSequence): Int {
    val lhsLength = lhs.length
    val rhsLength = rhs.length
    
    var cost = IntArray(lhsLength + 1) { it }
    var newCost = IntArray(lhsLength + 1)

    for (i in 1..rhsLength) {
        newCost[0] = i
        for (j in 1..lhsLength) {
            val match = if (lhs[j - 1] == rhs[i - 1]) 0 else 1
            val costReplace = cost[j - 1] + match
            val costInsert = cost[j] + 1
            val costDelete = newCost[j - 1] + 1
            newCost[j] = minOf(costInsert, costDelete, costReplace)
        }
        val swap = cost
        cost = newCost
        newCost = swap
    }
    return cost[lhsLength]
}
