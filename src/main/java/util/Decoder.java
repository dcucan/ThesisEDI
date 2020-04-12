package util;

import java.util.Base64;

public class Decoder {

    public String decode(String encodedString) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        String decodedString = new String(decodedBytes);
        return decodedString;
    }

    public String encode(String toEncode) {
        byte[] bytesEncoded = org.apache.commons.codec.binary.Base64.encodeBase64(toEncode.getBytes());
        String encodedString = new String(bytesEncoded);
        return encodedString;
    }

    public byte[] encode(byte[] toEncode){
        byte[] bytesEncoded = org.apache.commons.codec.binary.Base64.encodeBase64(toEncode);
        return bytesEncoded;
    }

    public byte[] decode(byte[] encodedBytes){
        byte[] bytesDecoded = Base64.getMimeDecoder().decode(encodedBytes);
        return bytesDecoded;
    }
}
