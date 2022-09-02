package net.notcoded.namefabric.utilities;

import java.util.Base64;

public class Utilities {

    public static byte[] Decode64(String base64){
        return(Base64.getDecoder().decode(base64));
        // new String(base64)
    }
}
