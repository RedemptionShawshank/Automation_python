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