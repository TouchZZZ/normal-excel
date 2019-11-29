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
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
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

    private HttpServletResponse mResponse;

    public static NormalExcel create(){
        return new NormalExcel();
    }

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


        NormalExcel.create().writeMultiple(null,ordersList,"F:\\NMP\\FEBS-Vue-master\\backend\\target\\classes\\model\\normal.xlsx",13);



        System.out.println();
    }

    /**
     * 只允许第一次models里包含多个bean对象，里层bean不允许多个Bean对象
     * @param models
     * @param <T>
     */
    public  <T> void writeMultiple(HttpServletResponse response,List<T> models,String path,int head) throws Exception{
        if(models.size()<=0){
            return;
        }
//        Integer size = beanInfo.getSize();
        SXSSFWorkbook sxssfWorkbook = null;
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream(this.getClass().getResource("/"+path).getPath()));
//        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream(path));
        sxssfWorkbook = new SXSSFWorkbook(xssfWorkbook);

        XSSFWorkbook modelWorkbook = sxssfWorkbook.getXSSFWorkbook();

        SXSSFSheet sheet = sxssfWorkbook.getSheetAt(0);


        BeanInfo beanInfo = new BeanInfo<T>(models);
        beanInfo.defaultListLine();
        Map<String, Integer> map = beanInfo.defaultSort();
        List<Map<String, String>> list = beanInfo.defaultLineMap();

        Map<Integer,List<MergeInfo>> mergeMap = beanInfo.defaultMergeMap();


        //样式
        //获取样式对象
        CellStyle cellStyle = sxssfWorkbook.createCellStyle();
        //设置样式对象，这里仅设置了边框属性
        cellStyle.setBorderBottom(BorderStyle.THIN); //下边框
        cellStyle.setBorderLeft(BorderStyle.THIN);//左边框
        cellStyle.setBorderTop(BorderStyle.THIN);//上边框
        cellStyle.setBorderRight(BorderStyle.THIN);//右边框
        cellStyle.setAlignment(HorizontalAlignment.CENTER);//左右居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);//上下居中

        int i=0;
        Map<Integer,String> maxSize = new HashMap<Integer, String>(16);
        for (Map<String, String> stringStringMap : list) {
            if(stringStringMap.size()>0){
                Row row = sheet.createRow(head+i);
                System.out.println(row.getRowNum());
//                for (int j = 0; j < 2; j++) {
//                    i++;
//                    sheet.createRow(head+i);
//                }
                if(maxSize.size()<=0){
                    for (Map.Entry<String, Integer> entry : map.entrySet()) {
                        maxSize.put(entry.getValue(),entry.getKey());
                    }
                }
                for (int j = 0; j < maxSize.size(); j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellStyle(cellStyle);
                    if(null!=maxSize.get(j)){
                        cell.setCellValue(stringStringMap.get(maxSize.get(j)));
                    }
                }

//                for (Map.Entry<String, String> entry : stringStringMap.entrySet()) {
//                    Cell cell = row.createCell(map.get(entry.getKey()));
//                    cell.setCellValue(entry.getValue());
//                    cell.setCellStyle(cellStyle);
//                }
            }else{
                //整行为空，则需要给整行加边框
                Row row = sheet.createRow(head+i);
                for (int j = 0; j < maxSize.size(); j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellStyle(cellStyle);
                }
            }
            i++;
        }

        //合并
        for (Map.Entry<Integer, List<MergeInfo>> entry : mergeMap.entrySet()) {
            for (MergeInfo mergeInfo : entry.getValue()) {
                if(!mergeInfo.getStart().equals(mergeInfo.getEnd())){
                    sheet.addMergedRegion(new CellRangeAddress(mergeInfo.getStart()+head,mergeInfo.getEnd()+head,entry.getKey(),entry.getKey()));
                }
            }
        }


        NormalExportUtil.download(sxssfWorkbook,response,"aaa");
//        FileOutputStream fos = null;
//        fos = new FileOutputStream("F:\\s\\excel\\normal111.xlsx");
//        sxssfWorkbook.write(fos);


    }
}
