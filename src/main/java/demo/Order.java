package demo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName:Order <br/>
 * Description:  <br/>
 * Date:     2019年11月25日 14:34 <br/>
 *
 * @author jinwei.zhang
 * @version 1.0
 * @since JDK 1.8
 */
@Data
public class Order {
    private String price;
    private List<Category> categoryList;

    private String orderId="258";

    private List<Gift> giftList;


    public Order() {

        GiftStand giftStand = new GiftStand();
        giftStand.setGistPrice("221");
        giftStand.setStandName("规格1");

        this.giftList = new ArrayList<Gift>();
        Gift gift = new Gift();
        gift.setGiftName("赠品-保护壳");




        Gift gift1 = new Gift();
        gift1.setGiftName("赠品-电脑包");

        List<GiftStand> giftStandList = new ArrayList<GiftStand>();
        giftStandList.add(giftStand);
        giftStandList.add(giftStand);
        gift.setGiftStands(giftStandList);
        giftList.add(gift);
//        giftList.add(gift1);

        Gift gift2 = new Gift();
        gift2.setGiftName("赠品-电脑包");
//        giftList.add(gift2);

    }
}
