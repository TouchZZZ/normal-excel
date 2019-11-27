package demo;

import lombok.Data;

import java.util.List;

/**
 * ClassName:Goods <br/>
 * Description:  <br/>
 * Date:     2019年11月25日 14:35 <br/>
 *
 * @author jinwei.zhang
 * @version 1.0
 * @since JDK 1.8
 */
@Data
public class Goods {
    private String goodsName;

    private List<GoodsStand> goodsStand;



}
