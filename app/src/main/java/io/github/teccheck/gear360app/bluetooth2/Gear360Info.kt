package io.github.teccheck.gear360app.bluetooth2

data class Gear360Info(
    val modelName: String,
    val modelVersion: String,
    val channel: Int,
    val wifiDirectMac: String,
    val apSSID: String,
    val apPassword: String,
    val boardRevision: String,
    val serialNumber: String,
    val uniqueNumber: String,
    val wifiMac: String,
    val btMac: String,
    val btFotaTestUrl: String,
    val fwType: Int
) {
    fun isCM200() = modelName.equals("SM-C200", true)
    fun isR210() = modelName.equals("SM-R210", true)

    fun getVersionName() = modelVersion.replaceAfter('_', "").replace("_", "")

    fun getSemanticVersion() = modelVersion.replaceBefore('_', "").replace("_", "")
}