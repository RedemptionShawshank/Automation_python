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
