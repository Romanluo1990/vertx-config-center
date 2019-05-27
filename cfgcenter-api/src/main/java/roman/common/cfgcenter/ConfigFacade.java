package roman.common.cfgcenter;

import org.springframework.stereotype.Service;
import roman.common.cfgcenter.dao.ConfigDao;
import roman.common.cfgcenter.entity.Config;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ConfigFacade {

    @Resource
    private ConfigDao configDao;

    private PropertyServer propertyServer;

    public ConfigFacade() {
        this.propertyServer = new PropertyServer();
    }

    public Config saveOrUpdate(Config config) {
        if(config.getId()==null){
            configDao.save(config);
        }else{
            configDao.updateByIdSelective(config);
        }
        Config newConfig = configDao.getById(config.getId());
        propertyServer.updateProperty(newConfig.getDomain(), newConfig.getKey(), newConfig.getValue());
        return config;
    }

    public List<Config> list(Config config) {
        return configDao.select(config);
    }
}
