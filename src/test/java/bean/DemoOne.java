package bean;

import demo.DemoOneOne;
import demo.DemoOneTwo;
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
public class DemoOne {
    private String name="DemoOne";

    private String age="DemoOne";

    private demo.DemoOneOne demoOneOne=new DemoOneOne();

}
