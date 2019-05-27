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
    *
    */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
    *
    */
    @Column(name = "`domain`")
    private String domain;

    /**
    *
    */
    @Column(name = "`key`")
    private String key;

    /**
    *
    */
    @Column(name = "`value`")
    private String value;

    /**
    *
    */
    @Column(name = "`is_deleted`")
    private Boolean isDeleted;

    /**
    *
    */
    @Column(name = "`update_time`")
    private Date updateTime;

    /**
     *
     */
    @Column(name = "`create_time`")
    private Date createTime;
}