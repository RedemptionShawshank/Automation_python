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


