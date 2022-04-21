package io.vteial.salestap.utils;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.jasypt.iv.RandomIvGenerator;
import org.jasypt.properties.EncryptableProperties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

/**
 * <a href="https://github.com/eclipse/microprofile-config">Eclipse MicroProfile Config</a> ConfigSource that
 * supports <a href="http://www.jasypt.org/">Jasypt</a>-encoded properties.
 */
@Slf4j
public class JasyptConfigSource implements ConfigSource {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Syntax: JasyptConfigSource <propertyToEncrypt>...");
            System.exit(1);
        }
        StringEncryptor stringEncryptor = new JasyptConfigSource().createStringEncryptor();
        for (String arg : args) {
            System.out.println(String.format("%s -> ENC(%s)", arg, stringEncryptor.encrypt(arg)));
        }
    }

    public static final String JASYPT_PASSWORD = "jasypt.password";
    public static final String JASYPT_KEY = "jasypt.key";
    public static final String JASYPT_ALGORITHM = "jasypt.algorithm";
    public static final String JASYPT_ITERATIONS = "jasypt.iterations";
    public static final String JASYPT_PROPERTIES = "jasypt.properties";

    private static final Pattern PATTERN = Pattern.compile("[^a-zA-Z0-9_]");
    private static final String CLASSPATH_PREFIX = "classpath:";
    private static final String DECRYPTION_FAILURE_MESSAGE = "Could not decrypt property {}; falling back to unencrypted property";

    private final Properties properties;
    private final EncryptableProperties encryptableProperties;
    private final String propertyFilename;

    public JasyptConfigSource() {
        final PropertiesAndName propertiesAndName = loadProperties();
        this.properties = propertiesAndName.getProperties();
        this.propertyFilename = propertiesAndName.getFilename();
        this.encryptableProperties = new EncryptableProperties(properties, getEncryptor());
    }

    public String toString() {
        return getName();
    }

    public StringEncryptor getEncryptor() {
        return createStringEncryptor();
    }

    public StringEncryptor createStringEncryptor() {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(property(JASYPT_PASSWORD, () -> property(JASYPT_KEY, getDefaultPassword())));
        encryptor.setAlgorithm(property(JASYPT_ALGORITHM, getDefaultAlgorithm()));
        encryptor.setKeyObtentionIterations(parseInt(property(JASYPT_ITERATIONS, Integer.toString(getDefaultIterations()))));
        encryptor.setIvGenerator(new RandomIvGenerator());
        return encryptor;
    }

    @Override
    public String getName() {
        return String.format("JasyptProperties[source=%s]", propertyFilename);
    }

    /**
     * Config source priority. Chosen to be higher than Quarkus application.properties (250), but lower than
     * Eclipse Microprofile EnvConfigSource (300).
     */
    @Override
    public int getOrdinal() {
        return 275;
    }

    @Override
    public Map<String, String> getProperties() {
        final Map<String, String> propertyMap = new HashMap<>();
        for (final String name : encryptableProperties.stringPropertyNames()) {
            propertyMap.put(name, getValue(name));
        }
        return propertyMap;
    }

    @Override
    public String getValue(String key) {
        try {
            return encryptableProperties.getProperty(key);
        } catch (EncryptionOperationNotPossibleException e) {
            if (log.isDebugEnabled()) {
                log.debug(DECRYPTION_FAILURE_MESSAGE, key, e);
            } else {
                log.warn(DECRYPTION_FAILURE_MESSAGE, key);
            }
            return properties.getProperty(key);
        }
    }

    @Override
    public Set<String> getPropertyNames() {
        return this.getProperties().keySet();
    }

    protected String property(String propertyName, Supplier<String> defaultValue) {
        String envVarName = envVarName(propertyName);
        return Optional.ofNullable(System.getenv(envVarName))
                .orElseGet(() -> Optional.ofNullable(System.getProperty(propertyName)).orElse(defaultValue.get()));
    }

    protected String property(String propertyName, String defaultValue) {
        return property(propertyName, () -> defaultValue);
    }

    protected String envVarName(String propertyName) {
        return PATTERN.matcher(propertyName).replaceAll("_").toUpperCase();
    }

    /**
     * Default number of Jasypt key obtention iterations: 1000.
     */
    private int getDefaultIterations() {
        return 1000;
    }

    /**
     * Default Jasypt encryption algorithm: PBEWithHMACSHA512AndAES_256.
     */
    protected String getDefaultAlgorithm() {
        return "PBEWithHMACSHA512AndAES_256";
    }

    /**
     * Default Jasypt encryption password. Override this if a custom password resolution strategy is desired.
     */
    protected String getDefaultPassword() {
        // The non-empty default password defined here is not supposed to be used for encryption, but simplifies instantiating
        // this class if no password is set, e.g. as part of the Quarkus build-time property resolution.
        return " ";
    }

    /**
     * Comma-separated property filenames, resolved from filesystem or classpath if prefixed with <code>classpath:</code>.
     */
    protected String getCommaSeparatedPropertyFilenames() {
        return property(JASYPT_PROPERTIES, "classpath:application.properties,config/application.properties");
    }

    protected PropertiesAndName loadProperties() {
        final List<String> propertyFilenames = Arrays.asList(getCommaSeparatedPropertyFilenames().split(","));
        for (final String propertyFilename : propertyFilenames) {
            log.trace("Trying to load properties from {}", propertyFilename);
            try (final InputStream is = createInputStream(propertyFilename)) {
                return createProperties(propertyFilename, is);
            } catch (Exception e) {
                if (log.isTraceEnabled()) {
                    log.trace("Could not open input stream for {}", propertyFilename, e);
                } else {
                    log.debug("Could not open input stream for {}", propertyFilename);
                }
            }
        }
        log.warn("Could not read properties from any file in {}", propertyFilenames);
        return new PropertiesAndName(new Properties(), "n/a");
    }

    private PropertiesAndName createProperties(String propertyFilename, InputStream is) throws IOException {
        final Properties properties = new Properties();
        properties.load(is);
        log.info("Loaded {} {} from {}", properties.size(), properties.size() == 1 ? "property" : "properties", propertyFilename);
        return new PropertiesAndName(properties, propertyFilename);
    }

    private InputStream createInputStream(String location) throws Exception {
        if (location.startsWith(CLASSPATH_PREFIX)) {
            return Thread.currentThread().getContextClassLoader().getResourceAsStream(location.substring(CLASSPATH_PREFIX.length()));
        } else {
            return new FileInputStream(new File(location));
        }
    }

    @Value
    static class PropertiesAndName {
        Properties properties;
        String filename;
    }
}
