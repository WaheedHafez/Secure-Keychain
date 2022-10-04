package keychain.crypto;

import org.json.JSONObject;

import java.io.IOException;
import java.security.AlgorithmParameters;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

class AESGCMCipherText {

    final String json;
    final byte[] encryptionBytes;
    final AlgorithmParameters algorithmParameters;

    AESGCMCipherText(byte[] encryptionBytes, AlgorithmParameters algorithmParameters) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("enc", HexFormat.of().formatHex(encryptionBytes));
        try {
            jsonObject.put("params", HexFormat.of().formatHex(algorithmParameters.getEncoded()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.json = jsonObject.toString();
        this.encryptionBytes = encryptionBytes;
        this.algorithmParameters = algorithmParameters;
    }

    AESGCMCipherText(String jsonStr) {
        JSONObject jsonObject = new JSONObject(jsonStr);

        String enc = (String) jsonObject.get("enc");
        String params = (String) jsonObject.get("params");

        this.encryptionBytes = HexFormat.of().parseHex(enc);
        AlgorithmParameters algorithmParameters;
        try {
            algorithmParameters = AlgorithmParameters.getInstance("GCM");
            algorithmParameters.init(HexFormat.of().parseHex(params));
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException(e);
        }
        this.algorithmParameters = algorithmParameters;

        this.json = jsonStr;
    }
}
