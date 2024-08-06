package com.example.springboot.service;

import java.util.Base64;

public class Base64DecoderTest {
    public static void main(String[] args) {
        String publicKeyString = "-----BEGIN PUBLIC KEY-----" +
                "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqd3QiLCJuYW1lIjoic3ByaW5nLWJvb3QiLCJhZG1pbiI6dHJ1ZSwiaWF0IjoxNTE2MjM5MDIyfQ.k9sUQu2p9xt33sh_m1kLu8Vk0t-HQV4HOYNt9zE3cbizubnx_wairs9MI5SmNgPwgyaFeEWT7VQLkqPX88qgW9GSeGfNckwF-JYgB_WP-bYSlMDMPEsmlSvpxlgAzUbesxImsMIdVZzwhtLqQEkkZKM7SoTeVXxah0SqpuIqA2o2il88GxA9Y7zNCvtFCelDrYSIqPiniUst03E2dtdrEaPUFHC9SEwNcx2B7XhafMgfAUoLwQsc0n3Ooj726ln5DyJtF0l4ZwIy86slYXvisHRFMf_Xzmp_XUzxLcFa8gw-K5ecnojRGbA1LAXREuC_D6PqMFJRowEoNa-3ATBk_A" +
                "-----END PUBLIC KEY-----";

        try {
            byte[] keyBytes = Base64.getDecoder().decode(publicKeyString);
            System.out.println("Decoded successfully: " + keyBytes.length + " bytes");
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to decode: " + e.getMessage());
        }
    }
}