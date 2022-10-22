# Secure-Keychain
Many apps need to handle passwords and other short but sensitive bits of data, such as keys and login tokens. The keychain provides a secure way to store these items.

## Implementation
Each keychain item is a key-value pair. 

The item's 'value' represents the secret data that must be protected.

The items's 'key' represents the meta-data for that value.

Each 'value' is encrypted individually using AEM_GCM authenticated encryption algorithm. See [EncryptionService.java](https://github.com/WaheedHafez/Secure-Keychain/blob/master/src/keychain/crypto/EncryptionService.java).

Each 'key' is hashed using HmacSHA256 algorithm to support searching on the encrypted data. See [MacService.java](https://github.com/WaheedHafez/Secure-Keychain/blob/master/src/keychain/crypto/MacService.java).

The keychain is protected by a master user password.

Couple secret keys are derived from the master password using PBKDF2 and HKDF algorithms, those secret keys are then used 
for different cryptographic services (e.g. encryption, signing). See [SessionKeys.java](https://github.com/WaheedHafez/Secure-Keychain/blob/master/src/keychain/crypto/SessionKeys.java).

## API
The Public API of the Keychain can be found at [KeyChain.java](https://github.com/WaheedHafez/Secure-Keychain/blob/master/src/keychain/KeyChain.java)