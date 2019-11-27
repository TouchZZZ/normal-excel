package demo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

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
public class DemoBean {
    private String name="DemoBean";

    private List<DemoOne> demoOne=new ArrayList<DemoOne>();

    private String age="age";

    private DemoTwo demoTwo=new DemoTwo();


}
