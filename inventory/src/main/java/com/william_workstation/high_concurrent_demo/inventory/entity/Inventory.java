package com.william_workstation.high_concurrent_demo.inventory.entity;

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
    @ApiModel(value="Inventory对象", description="")
public class Inventory extends Model<Inventory> {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

    private String sku;

    private Integer quantity;

    private Integer occupiedQuantity;


    @Override
    protected Serializable pkVal() {
          return this.id;
      }

}
