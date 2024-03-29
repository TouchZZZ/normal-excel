package demo;

import lombok.Data;

import java.util.List;

/**
 * ClassName:Gift <br/>
 * Description:  <br/>
 * Date:     2019年11月26日 16:27 <br/>
 *
 * @author jinwei.zhang
 * @version 1.0
 * @since JDK 1.8
 */
@Data
public class Gift {
    private String giftName;
    private List<GiftStand> giftStands;
}
