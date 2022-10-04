package keychain;

public interface KeyChain {

    //********* Creators ********//

    /**
     * Creates an empty keychain with the given password. Once the constructor
     * has finished, the password manager should be in a ready state.
     *
     * @param password  password used to protect the keychain
     * @return keychain.KeyChain object
     */
    static KeyChain init(String password) {
        // This static method should create a new KVS. This function is also responsible for generating the
        // necessary keys you need to provide for the various functionality of the password manager. Once
        // initialized, the returned password manager should be in ready to support the other functionality
        // described in the API.
        return new KeyChainImpl(password.toCharArray());
    }


    /**
     * Loads the keychain state from the provided representation (repr). The
     * repr variable will contain a JSON encoded serialization of the contents
     * of the KVS (as returned by the dump function). The trustedDataCheck
     * is an *optional* SHA-256 checksum that can be used to validate the
     * integrity of the contents of the KVS. If the checksum is provided and the
     * integrity check fails, an exception should be thrown. You can assume that
     * the representation passed to load is well-formed (i.e., it will be
     * a valid JSON object).Returns a Keychain object that contains the data
     * from repr.
     *
     * @param password password used to authenticate keychain
     * @param repr JSON encoded serialization of the keychain
     * @param trustedDataCheck  SHA-256 hash of the keychain; note that this is an optional parameter
     */
    static KeyChain load(String password, String repr, String trustedDataCheck) {
        return new KeyChainImpl(password.toCharArray(), repr, trustedDataCheck);
    }

    //********* API ********//

    /**
     * Returns a JSON serialization of the contents of the keychain that can be
     * loaded back using the load function. The return value should consist of
     * an array of two strings:
     *   arr[0] = JSON encoding of password manager
     *   arr[1] = SHA-256 checksum (as a string)
     * The first element of the array should contain
     * all of the data in the password manager. The second element is a SHA-256
     * checksum computed over the password manager to preserve integrity. If the
     * password manager is not in a ready-state, return null.
     */
    String[] dump();

    /**
     * Inserts the domain and associated data into the KVS. If the domain is
     * already in the password manager, this method should update its value. If
     * not, create a new entry in the password manager. If the password manager is
     * not in a ready state, throw an exception.
     */
    void set(String name, String value);

    /**
     * Fetches the data (as a string) corresponding to the given domain from the KVS.
     * If there is no entry in the KVS that matches the given domain, then return
     * null. If the password manager is not in a ready state, throw an exception. If
     * tampering has been detected with the records, throw an exception.
     */
    String get(String name);

    /**
     * Removes the record with name from the password manager. Returns true
     * if the record with the specified name is removed, false otherwise. If
     * the password manager is not in a ready state, throws an exception.
     */
    boolean remove(String name);

}
