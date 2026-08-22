package ir.amirhesambandegan.easify_security

/**
 * Helper to generate SSL Pinning configurations to prevent MITM attacks.
 */
object CertificatePinnerHelper {

    /**
     * Example OkHttp integration:
     * val pinner = CertificatePinner.Builder()
     *    .add("api.example.com", "sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=")
     *    .build()
     * OkHttpClient.Builder().certificatePinner(pinner).build()
     */
     
    fun getPinnerConfiguration(domain: String, vararg sha256Pins: String): Map<String, List<String>> {
        return mapOf(domain to sha256Pins.toList())
    }
}
