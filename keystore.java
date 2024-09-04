public static KeyStore createKeyStoreFromTextFile(String filePath, String keystorePassword) throws Exception {
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null, keystorePassword.toCharArray());  // Initialize an empty keystore

        StringBuilder keyStringBuilder = new StringBuilder();
        StringBuilder certStringBuilder = new StringBuilder();
        PrivateKey privateKey = null;
        X509Certificate certificate = null;
        String aliasKey = null;
        String aliasCert = null;
        boolean isKey = false;
        boolean isCert = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("alias_name_private_key:")) {
                    aliasKey = line.split(":")[1].trim();  // Extract the alias name for the private key
                } else if (line.startsWith("alias_name_cert:")) {
                    aliasCert = line.split(":")[1].trim();  // Extract the alias name for the certificate
                } else if (line.startsWith("-----BEGIN PRIVATE KEY-----")) {
                    isKey = true;
                    keyStringBuilder = new StringBuilder();
                } else if (line.startsWith("-----END PRIVATE KEY-----")) {
                    isKey = false;
                    String keyString = keyStringBuilder.toString();
                    byte[] keyBytes = Base64.getDecoder().decode(keyString.replaceAll("-----\\w+ PRIVATE KEY-----", "").replaceAll("\n", ""));
                    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
                    privateKey = keyFactory.generatePrivate(keySpec);
                } else if (line.startsWith("-----BEGIN CERTIFICATE-----")) {
                    isCert = true;
                    certStringBuilder = new StringBuilder();
                } else if (line.startsWith("-----END CERTIFICATE-----")) {
                    isCert = false;
                    String certString = certStringBuilder.toString();
                    try (ByteArrayInputStream certInputStream = new ByteArrayInputStream(certString.getBytes())) {
                        certificate = (X509Certificate) certFactory.generateCertificate(certInputStream);
                    }
                } else if (isKey) {
                    keyStringBuilder.append(line).append("\n");
                } else if (isCert) {
                    certStringBuilder.append(line).append("\n");
                }
            }

            if (privateKey != null && certificate != null) {
                // Add the private key and certificate to the KeyStore
                keyStore.setKeyEntry(aliasKey, privateKey, keystorePassword.toCharArray(), new X509Certificate[]{certificate});
            }

        }

        return keyStore;
    }






public static KeyStore createTrustStoreFromTextFile(String filePath) throws Exception {
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(null, null);  // Initialize an empty keystore

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            StringBuilder certStringBuilder = new StringBuilder();
            String alias = null;
            boolean isCert = false;
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("alias_name")) {
                    // Extract the alias name from the line
                    alias = line.split(":")[0].trim();
                } else if (line.startsWith("-----BEGIN CERTIFICATE-----")) {
                    isCert = true;
                    certStringBuilder = new StringBuilder();  // Start collecting certificate content
                    certStringBuilder.append(line).append("\n");
                } else if (line.startsWith("-----END CERTIFICATE-----")) {
                    isCert = false;
                    certStringBuilder.append(line).append("\n");

                    // Load the certificate
                    String certString = certStringBuilder.toString();
                    try (ByteArrayInputStream certInputStream = new ByteArrayInputStream(certString.getBytes())) {
                        X509Certificate certificate = (X509Certificate) certFactory.generateCertificate(certInputStream);
                        if (alias != null && !alias.isEmpty()) {
                            trustStore.setCertificateEntry(alias, certificate);
                        }
                    }
                } else if (isCert) {
                    certStringBuilder.append(line).append("\n");
                }
            }
        }

        return trustStore;
    }




public static KeyStore loadKeyStore(String keystorePath, String keystorePassword) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        try (FileInputStream fis = new FileInputStream(keystorePath)) {
            keyStore.load(fis, keystorePassword.toCharArray());
        }
        return keyStore;
    }
    
    public static KeyStore loadGeneratedKeyStore(String filePath, String keystorePassword) throws Exception {
        return KeyStoreCreator.createKeyStoreFromTextFile(filePath, keystorePassword);
    }



public static boolean compareKeyStores(KeyStore ks1, KeyStore ks2) throws Exception {
    Enumeration<String> aliases1 = ks1.aliases();
    while (aliases1.hasMoreElements()) {
        String alias = aliases1.nextElement();
        if (!ks2.containsAlias(alias)) {
            System.out.println("Alias not found in second keystore: " + alias);
            return false;
        }

        X509Certificate cert1 = (X509Certificate) ks1.getCertificate(alias);
        X509Certificate cert2 = (X509Certificate) ks2.getCertificate(alias);

        if (cert1 != null && cert2 != null && !cert1.equals(cert2)) {
            System.out.println("Certificates for alias " + alias + " do not match.");
            return false;
        }

        if (ks1.isKeyEntry(alias)) {
            if (!ks1.getKey(alias, keystorePassword.toCharArray()).equals(ks2.getKey(alias, keystorePassword.toCharArray()))) {
                System.out.println("Private keys for alias " + alias + " do not match.");
                return false;
            }
        }
    }
    return true;
}

public static void main(String[] args) {
    try {
        KeyStore originalKeyStore = loadKeyStore("original.jks", "original-password");
        KeyStore generatedKeyStore = loadGeneratedKeyStore("generated-keystore.txt", "generated-password");

        boolean match = compareKeyStores(originalKeyStore, generatedKeyStore);
        if (match) {
            System.out.println("The keystores match.");
        } else {
            System.out.println("The keystores do not match.");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}




