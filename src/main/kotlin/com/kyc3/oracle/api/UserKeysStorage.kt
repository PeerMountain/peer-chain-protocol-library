package com.kyc3.oracle.api

import com.kyc3.oracle.model.UserKeys

interface UserKeysStorage {

    fun store(address: String, userKeys: UserKeys)

    fun getUserKeys(address: String): UserKeys?
}