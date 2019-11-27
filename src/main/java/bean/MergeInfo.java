package bean;

import lombok.Data;

/**
 * ClassName:MergeInfo <br/>
 * Description:  <br/>
 * Date:     2019年11月26日 15:01 <br/>
 *
 * @author jinwei.zhang
 * @version 1.0
 * @since JDK 1.8
 */
@Data
public class MergeInfo {
    private Integer start;
    private Integer end;

    public MergeInfo(Integer start) {
        this.start = start;
    }
}
