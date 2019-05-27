package roman.common.cfgcenter.util;

public interface PropertyHolder {

    String getValue(String key);

    PropertyHolder SYS_PROPERTY_HOLDER = new SysPropertyHolder();

    PropertyHolder LOCAL_PROPERTY_HOLDER = new LocalPropertyHolder();

}
