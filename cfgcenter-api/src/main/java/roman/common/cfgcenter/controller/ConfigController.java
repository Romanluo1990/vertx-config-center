package roman.common.cfgcenter.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import roman.common.cfgcenter.ConfigFacade;
import roman.common.cfgcenter.entity.Config;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("config")
public class ConfigController {

    @Resource
    private ConfigFacade configFacade;

    @PostMapping
    @ApiOperation("保存配置")
    protected Config save(@Valid @RequestBody final Config config) {
        config.setId(null);
        configFacade.saveOrUpdate(config);
        return config;
    }

    @PutMapping("{id}")
    @ApiOperation("更新配置")
    protected void update(@PathVariable final long id, @RequestBody final Config config) {
        config.setId(id);
        configFacade.saveOrUpdate(config);
    }

    @GetMapping("list")
    @ApiOperation("list获取配置")
    protected List<Config> list(@ModelAttribute Config config) {
        return configFacade.list(config);
    }

    @GetMapping("publish")
    @ApiOperation("手动下发配置")
    protected void publishAll() {
        configFacade.publishAll();
    }

    @GetMapping("{domain}/publish")
    @ApiOperation("手动下发配置(比如数据库sql操作更新后下发)")
    protected void publish(@PathVariable final String domain) {
        configFacade.publish(domain);
    }
}
