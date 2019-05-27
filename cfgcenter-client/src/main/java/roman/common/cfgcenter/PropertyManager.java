package roman.common.cfgcenter;

import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Map;

@Slf4j
public class PropertyManager {

    private static class PopertyManagerHolder{
        private static final PropertyClient single = new PropertyClient();
    }

    private PropertyManager() {
    }

    public static Map<String, String> getPropertyMap(){
        return Collections.unmodifiableMap(PopertyManagerHolder.single.getPropertyMap());
    }

    public static String getString(String key) {
        return PopertyManagerHolder.single.getPropertyMap().get(key);
    }

    public static Integer getInteger(String key) {
        try {
            return Integer.parseInt(getString(key));
        } catch (Exception e) {
            log.warn("{}属性获取异常", key, e);
        }
        return null;
    }

    public static Long getLong(String key) {
        try {
            return Long.parseLong(getString(key));
        } catch (Exception e) {
            log.warn("{}属性获取异常", key, e);
        }
        return null;
    }

    public static Float getFloat(String key) {
        try {
            return Float.parseFloat(getString(key));
        } catch (Exception e) {
            log.warn("{}属性获取异常", key, e);
        }
        return null;
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(getString(key));
    }

    public static void register(PropertyListener propertyListener) {
        PopertyManagerHolder.single.register(propertyListener);
    }

    public static void unregister(PropertyListener propertyListener) {
        PopertyManagerHolder.single.unregister(propertyListener);
    }

}
