package roman.common.cfgcenter.client;

import lombok.extern.slf4j.Slf4j;
import roman.common.cfgcenter.PropertyManager;

@Slf4j
public class PropertyManagerTest {

    public static void main(String[] args) {
        //property改变事件
        PropertyManager.register(event -> {
            String propertyName = event.getPropertyName();
            String newValue = event.getNewValue();
            log.info("property {} update to: {}", propertyName, newValue);
        });
        try {
            while (true){
                log.info("white_list: {}",PropertyManager.getString("white_list"));
                Thread.sleep(3000l);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}