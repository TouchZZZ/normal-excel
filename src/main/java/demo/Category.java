package demo;

import lombok.Data;

import java.util.List;

/**
 * ClassName:Category <br/>
 * Description:  <br/>
 * Date:     2019年11月25日 14:36 <br/>
 *
 * @author jinwei.zhang
 * @version 1.0
 * @since JDK 1.8
 */
@Data
public class Category {
    private String categoryName;
    private List<Goods> goodsList;
}
