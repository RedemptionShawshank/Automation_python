public class SDOHKafkaProducer{
    private final static Logger logger  = LoggerFactory.getLogger(SDOHKafkaProducer.class);

    Object jsonMsg;
    AppProperties appProps;

    public SDOHKafkaProducer( AppProperties appProps, Object jsonMsg)
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
//        logger.info("SSL_TRUSTSTORE_LOCATION_CONFIG_PATH : "+SSL_TRUSTSTORE_LOCATION_CONFIG_PATH);
//        logger.info("SSL_TRUSTSTORE_PASSWORD_CONFIG : "+SSL_TRUSTSTORE_PASSWORD_CONFIG);
//        logger.info("SSL_KEYSTORE_LOCATION_CONFIG_PATH : "+SSL_KEYSTORE_LOCATION_CONFIG_PATH);
//        logger.info("SSL_KEYSTORE_PASSWORD_CONFIG : "+SSL_KEYSTORE_PASSWORD_CONFIG);
//        logger.info("Kafka Topic : " + Kafka_Topic);
//        logger.info("Broker : "+Broker);

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
