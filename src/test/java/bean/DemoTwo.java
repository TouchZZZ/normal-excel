package bean;

import demo.DemoTwoOne;
import demo.DemoTwoTwo;
import lombok.Data;

/**
 * ClassName:DemoBean <br/>
 * Description:  <br/>
 * Date:     2019年11月22日 15:59 <br/>
 *
 * @author jinwei.zhang
 * @version 1.0
 * @since JDK 1.8
 */
@Data
public class DemoTwo {
    private String name="DemoTwo";

    private String age="DemoTwo";
    private demo.DemoTwoOne demoTwoOne=new DemoTwoOne();

}
