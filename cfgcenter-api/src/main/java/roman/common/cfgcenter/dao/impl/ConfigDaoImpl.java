package roman.common.cfgcenter.dao.impl;

import org.springframework.stereotype.Repository;
import roman.common.cfgcenter.dao.ConfigDao;
import roman.common.cfgcenter.dao.repository.ConfigRepository;
import roman.common.cfgcenter.entity.Config;

/**
 * @desc：Dao
 * @author romanluo
 * @date 2019/05/24
 */
@Repository
public class ConfigDaoImpl extends BaseDaoImpl<ConfigRepository, Config> implements ConfigDao {

}