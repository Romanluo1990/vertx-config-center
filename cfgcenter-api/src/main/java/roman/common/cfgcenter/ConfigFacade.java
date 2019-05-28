package roman.common.cfgcenter;

import io.vertx.config.ConfigStoreOptions;
import org.springframework.stereotype.Service;
import roman.common.cfgcenter.dao.ConfigDao;
import roman.common.cfgcenter.entity.Config;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

@Service
public class ConfigFacade {

    @Resource
    private ConfigDao configDao;

    private PropertyServer propertyServer;

    public ConfigFacade() {
        ConfigStoreOptions sqlStore = new ConfigStoreOptions()
                .setType("jdbc")
                .setFormat("raw")
                .setConfig(new HikariConfigStoreConfig());
        this.propertyServer = new PropertyServer(sqlStore);
    }

    public Config saveOrUpdate(Config config) {
        if(config.getId()==null){
            configDao.save(config);
        }else{
            configDao.updateByIdSelective(config);
        }
        Config newConfig = configDao.getById(config.getId());
        propertyServer.publishProperty(newConfig.getDomain(), newConfig.getKey());
        return config;
    }

    public List<Config> list(Config config) {
        return configDao.select(config);
    }

    public void publishAll() {
        propertyServer.publishProperty();
    }

    public void publish(String domain) {
        propertyServer.publishProperty(domain);
    }

    @PostConstruct
    private void init(){
        publishAll();
    }
}
