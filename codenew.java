public class kafkaProducer{
    private final static Logger logger  = LoggerFactory.getLogger(kafkaProducer.class);

    Object jsonMsg;
    AppProperties appProps;

    public kafkaProducer( AppProperties appProps, Object jsonMsg)
    {

        this.appProps = appProps;
        this.jsonMsg = jsonMsg;
    }

    private static Producer<String, String> createProducer( AppProperties appProps) throws Exception {
        //logger.info("Creating Kafka Producer");
        String SSL_TRUSTSTORE_LOCATION_CONFIG_PATH = appProps.getTrustoreLocation();
        String SSL_TRUSTSTORE_PASSWORD_CONFIG = appProps.getTrustorePassword();
        String SSL_KEYSTORE_LOCATION_CONFIG_PATH = appProps.getKeystoreLocation();
        String SSL_KEYSTORE_PASSWORD_CONFIG = appProps.getKeystorePassword();
        String Kafka_Topic = appProps.getTopicName();
        String Broker = appProps.getBootstrapServers();

        String truststorepath = "src/main/resources/kafka_nonprod_certs/truststore.txt";
        String keystorepath = "src/main/resources/kafka_nonprod_certs/keystore.txt" ;

        X509Certificate keystore = loadCertificateFromTextFile(keystorepath);
        X509Certificate truststore = loadCertificateFromTextFile(truststorepath);

        Properties props = new Properties();
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Broker);

        //configure the following three settings for SSL Encryption
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
        props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, SSL_TRUSTSTORE_LOCATION_CONFIG_PATH);
        props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, SSL_TRUSTSTORE_PASSWORD_CONFIG);
        // configure the following three settings for SSL Authentication
        props.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, SSL_KEYSTORE_LOCATION_CONFIG_PATH);
        props.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, SSL_KEYSTORE_PASSWORD_CONFIG);

        props.put("retries", 3);
        props.put("acks", "all");
        props.put("kafka.topic", Kafka_Topic);
        //logger.info("KafkaProducer created");
        return new KafkaProducer<String, String>(props);

    }


    public void pushRecordToKafka()throws Exception{

        Producer producer = createProducer(appProps);
        int counter=0 ;

        try {
            if (jsonMsg != null) {

                Future<RecordMetadata> ack = producer.send(new ProducerRecord<String, Object>(appProps.getTopicName(), jsonMsg));
                RecordMetadata data = ack.get();
                logger.info("Record offset: " + data.offset() + "  Record partition: " +data.partition());
                counter++;

            }
            //logger.info("Total no of record published: "+ counter);
        }finally {
            producer.close();
            //logger.info("Producer closed");
        }
    }



}












import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.config.CommonClientConfigs;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.KeyManagerFactory;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Properties;
import java.util.concurrent.Future;
import java.util.logging.Logger;

public class KafkaProducerExample {
    private final static Logger logger = Logger.getLogger(KafkaProducerExample.class.getName());

    Object jsonMsg;
    AppProperties appProps;

    public KafkaProducerExample(AppProperties appProps, Object jsonMsg) {
        this.appProps = appProps;
        this.jsonMsg = jsonMsg;
    }

    private static Producer<String, String> createProducer(AppProperties appProps) throws Exception {
        // Load the certificates from text files
        String truststorePath = "src/main/resources/kafka_nonprod_certs/truststore.txt";
        String keystorePath = "src/main/resources/kafka_nonprod_certs/keystore.txt";

        X509Certificate truststoreCertificate = loadCertificateFromTextFile(truststorePath);
        X509Certificate keystoreCertificate = loadCertificateFromTextFile(keystorePath);

        // Create TrustStore and KeyStore in memory
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(null, null); // Initialize an empty keystore
        trustStore.setCertificateEntry("truststore-cert", truststoreCertificate);

        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null, null);
        keyStore.setCertificateEntry("keystore-cert", keystoreCertificate);

        // Initialize TrustManagerFactory and KeyManagerFactory
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trustStore);

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, appProps.getKeystorePassword().toCharArray());

        // Create SSLContext with the KeyManager and TrustManager
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        // Set Kafka properties
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, appProps.getBootstrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // SSL properties for Kafka
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
        props.put(SslConfigs.SSL_TRUSTSTORE_TYPE_CONFIG, "PEM");
        props.put(SslConfigs.SSL_KEYSTORE_TYPE_CONFIG, "PEM");

        // SSL settings: Directly set keystore and truststore using SSLContext
        props.put(SslConfigs.SSL_CONTEXT_PROVIDER_CLASS_CONFIG, sslContext.getProvider().getClass().getName());
        
        props.put("retries", 3);
        props.put("acks", "all");

        return new KafkaProducer<>(props);
    }

    // Helper method to load a certificate from a text file
    private static X509Certificate loadCertificateFromTextFile(String filePath) throws Exception {
        StringBuilder certStringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                certStringBuilder.append(line).append("\n");
            }
        }

        String certString = certStringBuilder.toString();

        // Convert certificate string to InputStream
        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
        try (ByteArrayInputStream certInputStream = new ByteArrayInputStream(certString.getBytes(StandardCharsets.UTF_8))) {
            return (X509Certificate) certFactory.generateCertificate(certInputStream);
        }
    }

    public void pushRecordToKafka() throws Exception {
        Producer<String, String> producer = createProducer(appProps);
        int counter = 0;

        try {
            if (jsonMsg != null) {
                Future<RecordMetadata> ack = producer.send(new ProducerRecord<>(appProps.getTopicName(), jsonMsg.toString()));
                RecordMetadata data = ack.get();
                logger.info("Record offset: " + data.offset() + "  Record partition: " + data.partition());
                counter++;
            }
        } finally {
            producer.close();
        }
    }
}

