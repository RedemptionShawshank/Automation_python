import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.security.Key;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.util.Base64;

public class JksToPemConverter {
    public static void main(String[] args) {
        String keystoreFilePath = "path/to/keystore.jks";  // Replace with your keystore path
        String keystorePassword = "yourKeystorePassword";  // Replace with your keystore password
        String privateKeyAlias = "yourPrivateKeyAlias";    // Replace with your private key alias
        String certificateAlias = "yourCertificateAlias";  // Replace with your certificate alias

        try {
            // Load the JKS keystore
            FileInputStream is = new FileInputStream(keystoreFilePath);
            KeyStore keystore = KeyStore.getInstance("JKS");
            keystore.load(is, keystorePassword.toCharArray());

            // Extract the private key using its alias
            Key privateKey = keystore.getKey(privateKeyAlias, keystorePassword.toCharArray());
            if (privateKey == null) {
                throw new Exception("No private key found for alias: " + privateKeyAlias);
            }

            // Extract the certificate using its alias
            Certificate certificate = keystore.getCertificate(certificateAlias);
            if (certificate == null) {
                throw new Exception("No certificate found for alias: " + certificateAlias);
            }

            // Convert the private key to PEM format
            String privateKeyPem = convertToPem("PRIVATE KEY", privateKey.getEncoded());

            // Convert the certificate to PEM format
            String certificatePem = convertToPem("CERTIFICATE", certificate.getEncoded());

            // Write the private key PEM to a file
            try (Writer writer = new OutputStreamWriter(new FileOutputStream("private-key.pem"), "UTF-8")) {
                writer.write(privateKeyPem);
            }

            // Write the certificate PEM to a file
            try (Writer writer = new OutputStreamWriter(new FileOutputStream("certificate.pem"), "UTF-8")) {
                writer.write(certificatePem);
            }

            System.out.println("Conversion complete. PEM files generated: private-key.pem, certificate.pem");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String convertToPem(String type, byte[] encoded) {
        StringBuilder pem = new StringBuilder();
        pem.append("-----BEGIN ").append(type).append("-----\n");
        pem.append(Base64.getMimeEncoder(64, "\n".getBytes()).encodeToString(encoded));
        pem.append("\n-----END ").append(type).append("-----\n");
        return pem.toString();
    }
}



import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.cert.Certificate;

public class JksToPfxConverter {
    public static void main(String[] args) {
        String jksFilePath = "path/to/keystore.jks";  // Replace with your JKS keystore path
        String jksPassword = "yourJksPassword";       // Replace with your JKS keystore password
        String privateKeyAlias = "yourPrivateKeyAlias"; // Replace with your private key alias
        String pfxFilePath = "path/to/keystore.pfx";  // Path to save the PFX file
        String pfxPassword = "yourPfxPassword";       // Password for the PFX file

        try {
            // Load the JKS keystore
            FileInputStream jksInputStream = new FileInputStream(jksFilePath);
            KeyStore jksKeystore = KeyStore.getInstance("JKS");
            jksKeystore.load(jksInputStream, jksPassword.toCharArray());
            jksInputStream.close();

            // Load the private key and certificate chain from the JKS keystore
            Key privateKey = jksKeystore.getKey(privateKeyAlias, jksPassword.toCharArray());
            Certificate[] certChain = jksKeystore.getCertificateChain(privateKeyAlias);

            // Create a new PKCS12 keystore
            KeyStore pfxKeystore = KeyStore.getInstance("PKCS12");
            pfxKeystore.load(null, null);  // Initialize an empty keystore

            // Store the private key and certificate chain in the PKCS12 keystore
            pfxKeystore.setKeyEntry(privateKeyAlias, privateKey, pfxPassword.toCharArray(), certChain);

            // Save the PKCS12 keystore to a .pfx file
            FileOutputStream pfxOutputStream = new FileOutputStream(pfxFilePath);
            pfxKeystore.store(pfxOutputStream, pfxPassword.toCharArray());
            pfxOutputStream.close();

            System.out.println("Conversion complete. PFX file generated: " + pfxFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}




import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.util.Enumeration;

public class TruststoreToPfxConverter {
    public static void main(String[] args) {
        String truststoreFilePath = "path/to/truststore.jks";  // Replace with your truststore path
        String truststorePassword = "yourTruststorePassword";  // Replace with your truststore password
        String pfxFilePath = "path/to/truststore.pfx";         // Path to save the PFX file
        String pfxPassword = "yourPfxPassword";                // Password for the PFX file

        try {
            // Load the JKS truststore
            FileInputStream truststoreInputStream = new FileInputStream(truststoreFilePath);
            KeyStore truststore = KeyStore.getInstance("JKS");
            truststore.load(truststoreInputStream, truststorePassword.toCharArray());
            truststoreInputStream.close();

            // Create a new PKCS12 keystore
            KeyStore pfxKeystore = KeyStore.getInstance("PKCS12");
            pfxKeystore.load(null, null);  // Initialize an empty keystore

            // Iterate over the aliases in the JKS truststore
            Enumeration<String> aliases = truststore.aliases();
            while (aliases.hasMoreElements()) {
                String alias = aliases.nextElement();
                Certificate cert = truststore.getCertificate(alias);

                // Store the certificate in the PKCS12 keystore
                if (cert != null) {
                    pfxKeystore.setCertificateEntry(alias, cert);
                }
            }

            // Save the PKCS12 keystore to a .pfx file
            try (FileOutputStream pfxOutputStream = new FileOutputStream(pfxFilePath)) {
                pfxKeystore.store(pfxOutputStream, pfxPassword.toCharArray());
            }

            System.out.println("Conversion complete. PFX file generated: " + pfxFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}





import java.io.FileInputStream;
import java.io.FileWriter;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.util.Base64;
import java.util.Enumeration;

public class TruststoreExporter {
    public static void main(String[] args) {
        String truststoreFilePath = "path/to/truststore.jks";  // Replace with your truststore path
        String truststorePassword = "yourTruststorePassword";  // Replace with your truststore password

        try {
            // Load the JKS truststore
            FileInputStream truststoreInputStream = new FileInputStream(truststoreFilePath);
            KeyStore truststore = KeyStore.getInstance("JKS");
            truststore.load(truststoreInputStream, truststorePassword.toCharArray());
            truststoreInputStream.close();

            // Iterate over the aliases in the JKS truststore
            Enumeration<String> aliases = truststore.aliases();
            while (aliases.hasMoreElements()) {
                String alias = aliases.nextElement();
                Certificate cert = truststore.getCertificate(alias);

                if (cert != null) {
                    // Convert the certificate to PEM format
                    String certPem = convertToPem(cert);

                    // Write the PEM to a file
                    try (FileWriter fw = new FileWriter(alias + ".pem")) {
                        fw.write(certPem);
                    }
                }
            }

            System.out.println("Certificates exported to PEM format.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String convertToPem(Certificate cert) throws Exception {
        StringBuilder pem = new StringBuilder();
        pem.append("-----BEGIN CERTIFICATE-----\n");
        pem.append(Base64.getMimeEncoder(64, "\n".getBytes()).encodeToString(cert.getEncoded()));
        pem.append("\n-----END CERTIFICATE-----\n");
        return pem.toString();
    }
}




import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.util.Base64;

public class JksToPemConverter {
    public static void main(String[] args) throws Exception {
        // Path to your keystore.jks file
        String keystorePath = "path/to/keystore.jks";
        // Keystore password
        char[] keystorePassword = "keystorePassword".toCharArray();
        // Alias for the key entry in the keystore
        String alias = "yourAlias";
        // Password for the private key (if different from the keystore password)
        char[] keyPassword = "keyPassword".toCharArray();

        // Load the JKS keystore
        KeyStore keystore = KeyStore.getInstance("JKS");
        try (FileInputStream fis = new FileInputStream(keystorePath)) {
            keystore.load(fis, keystorePassword);
        }

        // Extract the private key
        Key key = keystore.getKey(alias, keyPassword);
        if (key instanceof PrivateKey) {
            PrivateKey privateKey = (PrivateKey) key;

            // Get the certificate chain
            Certificate[] certChain = keystore.getCertificateChain(alias);

            // Write private key and certificates to PEM format
            try (FileWriter writer = new FileWriter("keystore.pem")) {
                // Write the private key in PEM format
                writer.write("-----BEGIN PRIVATE KEY-----\n");
                writer.write(Base64.getMimeEncoder(64, new byte[]{'\n'}).encodeToString(privateKey.getEncoded()));
                writer.write("\n-----END PRIVATE KEY-----\n");

                // Write each certificate in the chain in PEM format
                for (Certificate cert : certChain) {
                    writer.write("-----BEGIN CERTIFICATE-----\n");
                    writer.write(Base64.getMimeEncoder(64, new byte[]{'\n'}).encodeToString(cert.getEncoded()));
                    writer.write("\n-----END CERTIFICATE-----\n");
                }
            }
            System.out.println("Keystore successfully converted to keystore.pem");
        } else {
            throw new KeyStoreException("Alias does not contain a private key");
        }
    }
}




import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.util.Enumeration;

public class JksToPfxConverter {

    public static void main(String[] args) {
        try {
            // Paths to input JKS truststore and output PFX file
            String jksPath = "path/to/truststore.jks";
            String pfxPath = "path/to/truststore.pfx";
            String jksPassword = "yourJksPassword";
            String pfxPassword = "yourPfxPassword";

            // Load the JKS truststore
            KeyStore jksStore = KeyStore.getInstance("JKS");
            try (FileInputStream fis = new FileInputStream(jksPath)) {
                jksStore.load(fis, jksPassword.toCharArray());
            }

            // Create a PKCS12 keystore to store the certificates
            KeyStore pfxStore = KeyStore.getInstance("PKCS12");
            pfxStore.load(null, pfxPassword.toCharArray());

            // Iterate over the entries in the JKS truststore
            Enumeration<String> aliases = jksStore.aliases();
            while (aliases.hasMoreElements()) {
                String alias = aliases.nextElement();
                Certificate cert = jksStore.getCertificate(alias);
                pfxStore.setCertificateEntry(alias, cert);
            }

            // Save the PKCS12 keystore to the output .pfx file
            try (OutputStream os = new FileOutputStream(pfxPath)) {
                pfxStore.store(os, pfxPassword.toCharArray());
            }

            System.out.println("Conversion to .pfx completed successfully.");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error occurred during conversion.");
        }
    }
}



import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Enumeration;

public class JksToPemConverter {

    public static void main(String[] args) {
        try {
            // Paths to input JKS truststore and output PEM file
            String jksPath = "path/to/truststore.jks";
            String jksPassword = "yourJksPassword";

            // Load the JKS truststore
            KeyStore jksStore = KeyStore.getInstance("JKS");
            try (FileInputStream fis = new FileInputStream(jksPath)) {
                jksStore.load(fis, jksPassword.toCharArray());
            }

            // Iterate over the entries in the JKS truststore
            Enumeration<String> aliases = jksStore.aliases();
            while (aliases.hasMoreElements()) {
                String alias = aliases.nextElement();
                Certificate cert = jksStore.getCertificate(alias);

                // Convert the certificate to PEM format and write to a file
                if (cert instanceof X509Certificate) {
                    X509Certificate x509Cert = (X509Certificate) cert;
                    writeCertificateToPem(x509Cert, alias + ".pem");
                }
            }

            System.out.println("Conversion to .pem completed successfully.");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error occurred during conversion.");
        }
    }

    private static void writeCertificateToPem(X509Certificate cert, String fileName) throws IOException {
        try (Writer writer = new FileWriter(fileName)) {
            writer.write("-----BEGIN CERTIFICATE-----\n");
            writer.write(Base64.getMimeEncoder(64, new byte[]{'\n'}).encodeToString(cert.getEncoded()));
            writer.write("\n-----END CERTIFICATE-----\n");
        }
    }
}




import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Enumeration;

public class JksToSinglePemConverter {

    public static void main(String[] args) {
        try {
            // Paths to input JKS truststore and output PEM file
            String jksPath = "path/to/truststore.jks";
            String outputPemPath = "path/to/output/truststore_combined.pem";
            String jksPassword = "yourJksPassword";

            // Load the JKS truststore
            KeyStore jksStore = KeyStore.getInstance("JKS");
            try (FileInputStream fis = new FileInputStream(jksPath)) {
                jksStore.load(fis, jksPassword.toCharArray());
            }

            // Initialize writer for the combined PEM file
            try (Writer writer = new FileWriter(outputPemPath)) {

                // Iterate over the entries in the JKS truststore
                Enumeration<String> aliases = jksStore.aliases();
                while (aliases.hasMoreElements()) {
                    String alias = aliases.nextElement();
                    Certificate cert = jksStore.getCertificate(alias);

                    // Convert each certificate to PEM format and append to the output file
                    if (cert instanceof X509Certificate) {
                        X509Certificate x509Cert = (X509Certificate) cert;
                        writeCertificateToPem(x509Cert, writer);
                    }
                }
            }

            System.out.println("All certificates have been combined into the single PEM file: " + outputPemPath);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error occurred during conversion.");
        }
    }

    private static void writeCertificateToPem(X509Certificate cert, Writer writer) throws IOException {
        writer.write("-----BEGIN CERTIFICATE-----\n");
        writer.write(Base64.getMimeEncoder(64, new byte[]{'\n'}).encodeToString(cert.getEncoded()));
        writer.write("\n-----END CERTIFICATE-----\n");
    }
}




import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.util.Enumeration;

public class JksToPfxConverter {

    public static void main(String[] args) {
        try {
            // Load the keystore (which contains the private key)
            String keystorePath = "keystore.jks";
            String keystorePassword = "keystore-password";
            String keyAlias = "your-key-alias";
            KeyStore keystore = KeyStore.getInstance("JKS");
            keystore.load(new FileInputStream(keystorePath), keystorePassword.toCharArray());

            // Extract the private key from the keystore
            Key key = keystore.getKey(keyAlias, keystorePassword.toCharArray());
            if (key == null) {
                throw new Exception("No key found with alias: " + keyAlias);
            }

            // Load the truststore (which contains the certificates)
            String truststorePath = "truststore.jks";
            String truststorePassword = "truststore-password";
            KeyStore truststore = KeyStore.getInstance("JKS");
            truststore.load(new FileInputStream(truststorePath), truststorePassword.toCharArray());

            // Create a new PKCS12 keystore
            KeyStore pfxStore = KeyStore.getInstance("PKCS12");
            pfxStore.load(null, null);

            // Get all certificates from the truststore
            Enumeration<String> aliases = truststore.aliases();
            while (aliases.hasMoreElements()) {
                String alias = aliases.nextElement();
                Certificate certificate = truststore.getCertificate(alias);
                pfxStore.setCertificateEntry(alias, certificate);
            }

            // Get the certificate chain for the private key
            Certificate[] keyCertChain = keystore.getCertificateChain(keyAlias);

            // Add the private key and its associated certificate chain to the PKCS12 keystore
            pfxStore.setKeyEntry(keyAlias, key, keystorePassword.toCharArray(), keyCertChain);

            // Save the PKCS12 keystore to a file
            String pfxPath = "truststore.pfx";
            String pfxPassword = "pfx-password";
            try (FileOutputStream pfxOutputStream = new FileOutputStream(pfxPath)) {
                pfxStore.store(pfxOutputStream, pfxPassword.toCharArray());
            }

            System.out.println("PKCS#12 keystore created successfully: " + pfxPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StringReader;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Properties;

public class KafkaSslConsumerWithTextTruststore {

    public static void main(String[] args) throws Exception {
        // Path to your text file containing the PEM-like certificate content
        String truststoreTextPath = "path/to/your-truststore-text-file.txt";

        // Load the certificate from the text file
        X509Certificate certificate = loadCertificateFromTextFile(truststoreTextPath);

        // Create a TrustStore and add the certificate
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(null, null);  // Initialize an empty keystore
        trustStore.setCertificateEntry("ca-cert", certificate);  // Add the certificate to the keystore

        // Initialize a TrustManagerFactory with the TrustStore
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trustStore);

        // Set up the SSL context
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);

        // Set up Kafka consumer properties
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "your.kafka.broker:9092");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "your-consumer-group");

        // SSL properties for Kafka
        props.put(SslConfigs.SSL_PROTOCOL_CONFIG, "TLS");
        props.put(SslConfigs.SSL_TRUSTSTORE_TYPE_CONFIG, "PEM");
        props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, "");

        // Create Kafka consumer
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList("your-topic"));

        // Consume messages from Kafka
        while (true) {
            consumer.poll(Duration.ofMillis(1000)).forEach(record ->
                System.out.printf("Consumed record with key %s and value %s%n", record.key(), record.value())
            );
        }
    }

    // Helper method to load certificate from text file
    private static X509Certificate loadCertificateFromTextFile(String filePath) throws Exception {
        StringBuilder certStringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                certStringBuilder.append(line).append("\n");
            }
        }

        String certString = certStringBuilder.toString();

        // Load the certificate using CertificateFactory
        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
        try (StringReader certReader = new StringReader(certString)) {
            return (X509Certificate) certFactory.generateCertificate(new BufferedReader(certReader));
        }
    }
}
