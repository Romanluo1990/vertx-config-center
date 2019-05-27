package roman.common.cfgcenter.util;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;

@Slf4j
class LocalPropertyHolder implements PropertyHolder  {

    private static final String DEFAULT_PROPERTIES = "application.properties";

    private volatile Properties localProperties;

    @Override
    public String getValue(String key) {
        Properties roperties = getLocalProperties();
        return (String)roperties.get(key);
    }

    private Properties getLocalProperties(){
        if(localProperties == null){
            synchronized (this){
                if(localProperties == null){
                    localProperties = new Properties();
                    InputStream inputStream = null;
                    String uriPath = PropertyHolder.SYS_PROPERTY_HOLDER.getValue("cfgcenter.property.uri");
                    if(uriPath != null){
                        URI uri = URI.create(uriPath);
                        String scheme = uri.getScheme();
                        String schemeSpecificPart = uri.getSchemeSpecificPart();
                        switch (scheme){
                            case "file":
                                try {
                                    inputStream = loadFromFile(schemeSpecificPart);
                                } catch (FileNotFoundException e) {
                                    log.warn("{}配置文件不存在", schemeSpecificPart);
                                }
                                break;
                            case "classpath":
                                inputStream = loadFromClassPath(schemeSpecificPart);
                                break;
                            default:
                                log.warn("无效uri资源:{}", uriPath);
                        }
                    }else{
                        inputStream = ClassLoader.getSystemResourceAsStream(DEFAULT_PROPERTIES);
                    }
                    if (inputStream == null) {
                        log.warn("{}配置文件不存在", DEFAULT_PROPERTIES);
                    }else{
                        try {
                            localProperties.load(inputStream);
                        } catch (IOException e) {
                            log.warn("本地配置加载失败", e);
                        }
                    }
                }
            }
        }
        return localProperties;
    }

    private InputStream loadFromFile(String schemeSpecificPart) throws FileNotFoundException {
        return new FileInputStream(schemeSpecificPart);
    }

    private InputStream loadFromClassPath(String schemeSpecificPart) {
        return ClassLoader.getSystemResourceAsStream(schemeSpecificPart);
    }

}
