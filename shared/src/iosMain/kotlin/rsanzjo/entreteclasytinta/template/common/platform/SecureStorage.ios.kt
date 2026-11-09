@file:OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)

package rsanzjo.entreteclasytinta.template.common.platform

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.MemScope
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.alloc
import kotlinx.cinterop.allocArrayOf
import kotlinx.cinterop.convert
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.readBytes
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.usePinned
import kotlinx.cinterop.value
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.CoreFoundation.CFDictionaryCreate
import platform.CoreFoundation.CFDictionaryRef
import platform.CoreFoundation.CFRelease
import platform.CoreFoundation.CFStringRef
import platform.CoreFoundation.CFTypeRef
import platform.CoreFoundation.CFTypeRefVar
import platform.CoreFoundation.kCFAllocatorDefault
import platform.CoreFoundation.kCFBooleanTrue
import platform.Foundation.CFBridgingRelease
import platform.Foundation.CFBridgingRetain
import platform.Foundation.NSData
import platform.Foundation.create
import platform.Security.SecItemAdd
import platform.Security.SecItemCopyMatching
import platform.Security.SecItemDelete
import platform.Security.errSecSuccess
import platform.Security.kSecAttrAccessible
import platform.Security.kSecAttrAccessibleAfterFirstUnlock
import platform.Security.kSecAttrAccount
import platform.Security.kSecAttrService
import platform.Security.kSecClass
import platform.Security.kSecClassGenericPassword
import platform.Security.kSecMatchLimit
import platform.Security.kSecMatchLimitOne
import platform.Security.kSecReturnData
import platform.Security.kSecValueData

actual val platformModule: Module = module {
    single<SecureStorage> { KeychainSecureStorage() }
}

/** Guarda los valores como contraseñas genéricas del Keychain de iOS. */
private class KeychainSecureStorage : SecureStorage {

    override fun getString(key: String): String? = memScoped {
        val account = CFBridgingRetain(key)
        val service = CFBridgingRetain(SERVICE)
        try {
            val query = cfDictionaryOf(
                mapOf(
                    kSecClass to kSecClassGenericPassword,
                    kSecAttrService to service,
                    kSecAttrAccount to account,
                    kSecReturnData to kCFBooleanTrue,
                    kSecMatchLimit to kSecMatchLimitOne
                )
            )
            val result = alloc<CFTypeRefVar>()
            val status = SecItemCopyMatching(query, result.ptr)
            CFRelease(query)
            if (status != errSecSuccess) {
                null
            } else {
                (CFBridgingRelease(result.value) as? NSData)?.toKotlinString()
            }
        } finally {
            CFRelease(account)
            CFRelease(service)
        }
    }

    override fun putString(key: String, value: String) {
        remove(key)
        memScoped {
            val account = CFBridgingRetain(key)
            val service = CFBridgingRetain(SERVICE)
            val data = CFBridgingRetain(value.toNSData())
            try {
                val attributes = cfDictionaryOf(
                    mapOf(
                        kSecClass to kSecClassGenericPassword,
                        kSecAttrService to service,
                        kSecAttrAccount to account,
                        kSecValueData to data,
                        kSecAttrAccessible to kSecAttrAccessibleAfterFirstUnlock
                    )
                )
                SecItemAdd(attributes, null)
                CFRelease(attributes)
            } finally {
                CFRelease(account)
                CFRelease(service)
                CFRelease(data)
            }
        }
    }

    override fun remove(key: String) {
        memScoped {
            val account = CFBridgingRetain(key)
            val service = CFBridgingRetain(SERVICE)
            try {
                val query = cfDictionaryOf(
                    mapOf(
                        kSecClass to kSecClassGenericPassword,
                        kSecAttrService to service,
                        kSecAttrAccount to account
                    )
                )
                SecItemDelete(query)
                CFRelease(query)
            } finally {
                CFRelease(account)
                CFRelease(service)
            }
        }
    }

    private companion object {
        const val SERVICE = "rsanzjo.entreteclasytinta.template.session"
    }
}

private fun MemScope.cfDictionaryOf(entries: Map<CFStringRef?, CFTypeRef?>): CFDictionaryRef? {
    val keys = allocArrayOf(*entries.keys.toTypedArray())
    val values = allocArrayOf(*entries.values.toTypedArray())
    return CFDictionaryCreate(
        kCFAllocatorDefault,
        keys.reinterpret(),
        values.reinterpret(),
        entries.size.convert(),
        null,
        null
    )
}

private fun String.toNSData(): NSData {
    val bytes = encodeToByteArray()
    if (bytes.isEmpty()) return NSData()
    return bytes.usePinned { pinned ->
        NSData.create(bytes = pinned.addressOf(0), length = bytes.size.convert())
    }
}

private fun NSData.toKotlinString(): String? {
    val size = length.toInt()
    if (size == 0) return ""
    return bytes?.readBytes(size)?.decodeToString()
}
