package keychain;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.fail;

class KeyChainRollBackAttackTest {

    private static final String password = "password123!";
    private static final Map<String, String> kvs = new HashMap<>();

    static {
        kvs.put("service1", "value1");
        kvs.put("service2", "value2");
        kvs.put("service3", "value3");
    }

    @Test
    void fails_to_restore_the_database_if_checksum_is_wrong() {
        // given
        KeyChain keyChain1 = KeyChain.init(password);
        for (var entry : kvs.entrySet()) {
            keyChain1.set(entry.getKey(), entry.getValue());
        }
        String[] dump = keyChain1.dump();
        String contents1 = dump[0];

        KeyChain keyChain2 = KeyChain.init(password);
        var checksum2 = keyChain2.dump()[1];

        try {
            KeyChain.load(password, contents1, checksum2);
            fail();
        } catch (Exception e) {

        }
    }

    @Test
    void can_dump_and_restore_the_database() {
        // given
        KeyChain keyChain = KeyChain.init(password);
        var url = "www.stanford.edu";
        var pw = "sunetpassword";
        keyChain.set(url, pw);
        String[] oldDump = keyChain.dump();
        String oldContentDump = oldDump[0];
        String oldChecksum = oldDump[1];

        // when
        // user updates existing record
        keyChain.set(url, pw + "new");
        String[] newDump = keyChain.dump();
        String newChecksum = newDump[1];

        // adversary rollback to previous state
        // then user when loading his data, an exception should be thrown
        try {
            KeyChain newKeyChain = KeyChain.load(password, oldContentDump, newChecksum);
            fail();
        } catch (Exception e) {

        }
    }

}