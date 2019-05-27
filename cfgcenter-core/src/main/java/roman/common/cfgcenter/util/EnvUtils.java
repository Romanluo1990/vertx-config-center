package roman.common.cfgcenter.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class EnvUtils {

    private static final String APPLATION_DOMAIN_KEY = "applation.domain";

    private static final String APPLATION_PROPERTY_URI_KEY = "applation.property.uri";

    private static final String APPLATION_PROPERTY_CHARSET_KEY = "applation.property.charset";

    private static final String APPLATION_ZOO_KEY = "applation.zoo";

    private static final String APPLATION_EVENT_BUS_PULL_ADDRESS_KEY = "applation.eventBusPull.address";

    private static final String DEFAULT_URI = "classpath:application.properties";

    private static final String DEFAULT_EVENT_BUS_PULL_ADDRESS = "event-bus-pull";

    private static final String DEFAULT_PROPERTY_CHARSET = "UTF-8";

    private static List<PropertyHolder> propertyHolderChain = new LinkedList<>();

    static {
        propertyHolderChain.add(PropertyHolder.SYS_PROPERTY_HOLDER);
        propertyHolderChain.add(PropertyHolder.LOCAL_PROPERTY_HOLDER);
    }

    private EnvUtils() {
    }

    private static String getValue(String key){
        Optional<String> optional = propertyHolderChain.stream()
                .map(propertyHolder -> propertyHolder.getValue(key))
                .filter(Objects::nonNull)
                .findFirst();
        return optional.orElse(null);
    }

    public static String getEventBusPullAddress(){
        String address = getValue(APPLATION_EVENT_BUS_PULL_ADDRESS_KEY);
        if(address == null)
            address = DEFAULT_EVENT_BUS_PULL_ADDRESS;
        return address;
    }

    public static String getDomain(){
        String domain = getValue(APPLATION_DOMAIN_KEY);
        if(domain == null)
            throw new IllegalStateException("properties文件，app环境变量，系统环境变量都未设置applation.domain");
        return domain;
    }

    public static String getPropertyURI(){
        String uri = getValue(APPLATION_PROPERTY_URI_KEY);
        if(uri == null)
            uri = DEFAULT_URI;
        return uri;
    }

    public static String getZoo(){
        return getValue(APPLATION_ZOO_KEY);
    }

    public static String getPropertyCharset() {
        String charset = getValue(APPLATION_PROPERTY_CHARSET_KEY);
        if(charset == null)
            charset = DEFAULT_PROPERTY_CHARSET;
        return charset;
    }
}
