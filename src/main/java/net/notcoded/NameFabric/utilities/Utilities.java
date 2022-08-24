package net.notcoded.namefabric.utilities;

import java.util.Base64;

public class Utilities {

    public static byte[] Decode64(String base64){
        return(Base64.getDecoder().decode(base64));
        // new String(base64)
    }

    public static byte[] UrlDecode64(String base64){
        return(Base64.getUrlDecoder().decode(base64));
    }

    public static String Encode64(byte[] base64){
        return(new String(Base64.getEncoder().encode(base64)));
        // new String(base64)
    }

    public static String UrlEncode64(byte[] base64){
        return(new String(Base64.getUrlEncoder().encode(base64)));
    }
}
