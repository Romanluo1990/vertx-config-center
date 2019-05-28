package roman.common.cfgcenter.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @desc：模型
 * @author romanluo
 * @date 2019/05/24
 */
@Data
@Table(name="config")
public class Config implements Identity {

    /**
    * 自增ID
    */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
    * 配置域
    */
    @Column(name = "`domain`")
    private String domain;

    /**
    * 配置key
    */
    @Column(name = "`key`")
    private String key;

    /**
    * 配置value
    */
    @Column(name = "`value`")
    private String value;

    /**
     * 配置说明
     */
    @Column(name = "`desc`")
    private String desc;

    /**
    * 是否删除
    */
    @Column(name = "`is_deleted`")
    private Boolean isDeleted;

    /**
    * 更新时间
    */
    @Column(name = "`update_time`")
    private Date updateTime;

    /**
     * 创建时间
     */
    @Column(name = "`create_time`")
    private Date createTime;
}