package com.william_workstation.high_concurrent_demo.finance.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author william
 * @since 2020-11-05
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @ApiModel(value="Balance对象", description="")
public class Balance extends Model<Balance> {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

    private String userName;

    private BigDecimal balance;


    @Override
    protected Serializable pkVal() {
          return this.id;
      }

}
