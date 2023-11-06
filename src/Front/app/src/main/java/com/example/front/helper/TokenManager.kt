package com.example.front.helper

import kotlinx.coroutines.flow.first
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import javax.inject.Inject


class TokenManager() {

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    val JWT_SECRET = "10e13609875c047a26c74fcba54bde5b"

    // Function to retrieve and decode the token
    suspend fun getUsernameFromToken(): String? {
        // Retrieve the token from the DataStoreManager
        val token = dataStoreManager.tokenFlow.first()

        // Decode the token and extract the username (you need to implement your decoding logic here)
        val username = decodeTokenAndGetUsername(token)

        return username
    }

    // You need to implement your own token decoding logic here
    private fun decodeTokenAndGetUsername(token: String?): String? {
        if (token.isNullOrBlank()) {
            return null
        }

        try {
            val algorithm = Algorithm.HMAC256(JWT_SECRET)
            val verifier = JWT.require(algorithm).build()
            val decodedJWT = verifier.verify(token)

            // Extract the "name" claim from the decoded JWT
            return decodedJWT.getClaim("name").asString()
        } catch (e: Exception) {
            // Handle any exceptions that may occur during decoding
            e.printStackTrace()
            return null
        }
    }
}
