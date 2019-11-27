package util;



import bean.BeanInfo;
import bean.MergeInfo;
import com.alibaba.fastjson.JSON;
import com.sun.org.apache.xpath.internal.operations.Or;
import demo.Category;
import demo.DemoBean;
import demo.Goods;
import demo.GoodsStand;
import demo.Order;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ClassName:NormalExcel <br/>
 * Description:  <br/>
 * Date:     2019年11月22日 15:42 <br/>
 *
 * @author jinwei.zhang
 * @version 1.0
 * @since JDK 1.8
 */
public class NormalExcel {

    public static void main(String[] args) throws Exception{
        List<Order> ordersList = new ArrayList<Order>();

        List<Category> categoryList = new ArrayList<Category>();
        Category category = new Category();
        category.setCategoryName("数码产品");

        List<Goods> goodsList = new ArrayList<Goods>();
        Goods goods = new Goods();
        goods.setGoodsName("电脑");
        List<GoodsStand> goodsStandList = new ArrayList<GoodsStand>();
        GoodsStand goodsStand = new GoodsStand();
        goodsStand.setGoodsStand("联想");
        goodsStand.setAmount("2");
        goodsStandList.add(goodsStand);

        GoodsStand goodsStand2 = new GoodsStand();
        goodsStand2.setGoodsStand("惠普");
        goodsStand2.setAmount("3");
        goodsStandList.add(goodsStand2);

        goods.setGoodsStand(goodsStandList);

        Goods goods1 = new Goods();
//        goods1.setAmount("1");
        goods1.setGoodsName("相机");
        List<GoodsStand> goodsStandList1 = new ArrayList<GoodsStand>();
        GoodsStand goodsStand1 = new GoodsStand();
        goodsStand1.setGoodsStand("佳能");
        goodsStand1.setAmount("2");
        goodsStandList1.add(goodsStand1);
        goods1.setGoodsStand(goodsStandList1);
        goodsList.add(goods);
        goodsList.add(goods1);
        category.setGoodsList(goodsList);

        categoryList.add(category);

        Order order = new Order();
        order.setPrice("2790");
        order.setCategoryList(categoryList);

        String json ="{\"categoryList\":[{\"categoryName\":\"数码产品\",\"goodsList\":[{\"goodsName\":\"电脑\",\"goodsStand\":[{\"amount\":\"2\",\"goodsStand\":\"联想\"},{\"amount\":\"3\",\"goodsStand\":\"惠普\"}]},{\"goodsName\":\"相机\",\"goodsStand\":[{\"amount\":\"2\",\"goodsStand\":\"佳能\"}]}]}],\"price\":\"2790\"}";
        Order order1 = JSON.parseObject(json, Order.class);
//        JSON.toJSONString(order);
        ordersList.add(order);
        ordersList.add(order1);
        Order order2 = JSON.parseObject(json, Order.class);
        order1.getCategoryList().add(order2.getCategoryList().get(0));


        writeMultiple(ordersList);



        System.out.println();
    }

    /**
     * 只允许第一次models里包含多个bean对象，里层bean不允许多个Bean对象
     * @param models
     * @param <T>
     */
    public static <T> void writeMultiple(List<T> models) throws Exception{
        if(models.size()<=0){
            return;
        }

//        Integer size = beanInfo.getSize();

        SXSSFWorkbook sxssfWorkbook = null;
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream("F:\\s\\excel\\normal.xlsx"));
        sxssfWorkbook = new SXSSFWorkbook(xssfWorkbook);

        XSSFWorkbook modelWorkbook = sxssfWorkbook.getXSSFWorkbook();

        SXSSFSheet sheet = sxssfWorkbook.getSheetAt(0);


        BeanInfo beanInfo = new BeanInfo<T>(models);
        beanInfo.defaultListLine();
        List<Map<String, String>> list = beanInfo.defaultLineMap();
        Map<String, Integer> map = beanInfo.defaultSort();
        Map<Integer,List<MergeInfo>> mergeMap = beanInfo.defaultMergeMap();


        int head=0;
        int i=0;
        for (Map<String, String> stringStringMap : list) {
            if(stringStringMap.size()>0){
                Row row = sheet.createRow(head+i);
//                for (int j = 0; j < 2; j++) {
//                    i++;
//                    sheet.createRow(head+i);
//                }
                for (Map.Entry<String, String> entry : stringStringMap.entrySet()) {
                    Cell cell = row.createCell(map.get(entry.getKey()));
                    cell.setCellValue(entry.getValue());
                }
            }
            i++;

        }

        //合并
        for (Map.Entry<Integer, List<MergeInfo>> entry : mergeMap.entrySet()) {
            for (MergeInfo mergeInfo : entry.getValue()) {
                if(!mergeInfo.getStart().equals(mergeInfo.getEnd())){
                    sheet.addMergedRegion(new CellRangeAddress(mergeInfo.getStart(),mergeInfo.getEnd(),entry.getKey(),entry.getKey()));
                }
            }
        }



        FileOutputStream fos = null;
        fos = new FileOutputStream("F:\\s\\excel\\normal111.xlsx");
        sxssfWorkbook.write(fos);

        System.out.println();

    }
}
