package com.kyc3.oracle.api

import com.kyc3.oracle.utils.SignatureHelper
import org.slf4j.LoggerFactory
import org.web3j.crypto.Credentials
import org.web3j.crypto.ECKeyPair
import org.web3j.crypto.Hash
import org.web3j.crypto.Keys
import org.web3j.crypto.Sign
import org.web3j.utils.Numeric

class Web3Service(
    private val privateKey: String
) {

    private val ecKeyPair: ECKeyPair = Credentials.create(privateKey).ecKeyPair

    fun verifySignature(body: String, signature: String, address: String): Boolean =
        SignatureHelper.fromString(signature)
            .let { signatureData ->
                Numeric.hexStringToByteArray(Hash.sha3String(body))
                    .let { data ->
                        Sign.signedPrefixedMessageToKey(data, signatureData)
                    }
            }
            .let { Keys.getAddress(it) }
            .let { address.contains(it, true) }

    fun sign(body: String): Sign.SignatureData =
        Sign.signPrefixedMessage(Numeric.hexStringToByteArray(Hash.sha3String(body)), ecKeyPair)

    fun signHex(body: String): Sign.SignatureData =
        Sign.signPrefixedMessage(Numeric.hexStringToByteArray(Hash.sha3(body)), ecKeyPair)
}
