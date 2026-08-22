package ir.amirhesambandegan.easify_security

/**
 * Helper object to generate SSL Pinning configurations to prevent MITM (Man-In-The-Middle) attacks.
 */
object CertificatePinnerHelper {

    /**
     * Generates a certificate pinner configuration for a specific domain.
     *
     * Example OkHttp integration:
     * val pinner = CertificatePinner.Builder()
     *    .add("api.example.com", "sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=")
     *    .build()
     * OkHttpClient.Builder().certificatePinner(pinner).build()
     *
     * @param domain The domain name for which the pins should be applied (e.g., "api.example.com").
     * @param sha256Pins A variable number of SHA-256 hash pins representing the accepted certificates.
     * @return A map containing the domain as the key and a list of the provided pins as the value.
     */
    fun getPinnerConfiguration(domain: String, vararg sha256Pins: String): Map<String, List<String>> {
        return mapOf(domain to sha256Pins.toList())
    }
}
