package bean;

import util.BeanUtil;
import util.NormalMathUtil;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

import static util.BeanUtil.getNotBaseBeanList;

/**
 * ClassName:BeanInfo <br/>
 * Description:  <br/>
 * Date:     2019年11月22日 16:26 <br/>
 *
 * @author jinwei.zhang
 * @version 1.0
 * @since JDK 1.8
 */
public class BeanInfo<T> {

    private List<T> list;

    private Integer singleBeanMaxSize;

    private List<Map<String, String>> lineMap;

    private Map<String, Integer> sortMap;

    //倍化数
    private List<List<Integer>> listSize;

    public BeanInfo(List<T> list) {
        this.list = list;

        defaultSort();
    }

    /**
     * 获取记录总数
     */
    public Integer getSize(){
        if(null==list){
            return 0;
        }else{
            return list.size();
        }
    }


    public Integer get(){
        T t = list.get(0);
        t.getClass();

        return 0;
    }

    /**
     * 获取占据的excel总行数，可能是倍增行数
     * @return
     */
    public Integer getSingleBeanMaxSize() {
        return singleBeanMaxSize;
    }

//    public Map<Integer,Map<Integer,String>> getLineMap(){
//
//        int i=0;
////        for (T t : list) {
//
////            t.getClass().
////            map.put(,"");
//        }
//    }

    private static void getMaxRowSize(List<Object> objectList, List<Integer> rowList) {
        if(null==objectList||objectList.size()<=0){
            multiRowList(rowList,1);
            return;
        }
        for (Object obj : objectList) {
            List<List<Object>> notBaseBeanList = getNotBaseBeanList(obj);
            if(notBaseBeanList.size()<=0) {
                multiRowList(rowList,1);
            }else{
                getMaxRowSize(notBaseBeanList.get(0),rowList);
            }
        }
    }

    //计算每个list的最大行数，取最小公倍数
    public void defaultListLine(){

        List<List<Integer>> listSize = new ArrayList<List<Integer>>();

        T bean = list.get(0);
        Map<String, Integer> map = new HashMap<String, Integer>();
        Field[] fields = bean.getClass().getDeclaredFields();

        //是否需要开启倍化
        boolean mutiply=false;

        for (T t: list) {
            List<Integer> countList = new ArrayList<Integer>();
            for (Field field : fields) {
                int listCalculate=0;
                if(field.getType().equals(List.class)){
                    AtomicInteger count = new AtomicInteger(0);
                    for (Object obj : (List<Object>)BeanUtil.getMethodByFieldName(t,field.getName())) {
                        //每个list的最大行数
                        listLine(obj,count);
                    }
                    countList.add(count.intValue());
                    listCalculate++;
                    if(listCalculate>1){
                        //开启倍化数
                        mutiply=true;
                    }
                }
            }
            listSize.add(countList);
        }


        if(mutiply){
            List<Integer> multipleList = new ArrayList<Integer>();
            for (List<Integer> integers : listSize) {
                Integer[] arr = new Integer[list.size()];
                integers.toArray(arr);
                int multiple = NormalMathUtil.getMinMultiCommonMultiple(arr);
                multipleList.add(multiple);
            }

            //倍化数
            for (int i = 0; i < listSize.size(); i++) {
                List<Integer> iList =listSize.get(i);
                for (int j = 0; j < iList.size(); j++) {
                    iList.set(j,multipleList.get(i)/iList.get(j));
                }
            }
            this.listSize=listSize;
        }
    }

    private void listLine(Object obj, AtomicInteger count) {
        Class<?> clz = obj.getClass();
        Field[] fields = clz.getDeclaredFields();
        boolean hasList=false;
        for (Field field : fields) {
            if(field.getType().equals(List.class)){
                for (Object o : (List<Object>)BeanUtil.getMethodByFieldName(obj,field.getName())) {
                    listLine(o,count);
                    hasList=true;
                }
            }
        }
        if(!hasList){
            count.getAndIncrement();
        }
    }

    //计算排序
    public Map<String, Integer> defaultSort(){

        T t = list.get(0);
        Map<String, Integer> map = new HashMap<String, Integer>();
        Field[] fields = t.getClass().getDeclaredFields();
        Queue<Boolean> queue = new LinkedList<Boolean>();
//        int sort = 0;
        for (Field field : fields) {
            boolean isBaseType = BeanUtil.isBaseType(field.getType());
            if(isBaseType){
                map.put(field.getName(),queue.size());
                queue.offer(true);
//                sort++;
//                queue.offer(String.valueOf(BeanUtil.getMethodByFieldName(t,field.getName())));
            }else if(field.getType().equals(List.class)){
                //list对象
                ParameterizedType genericType = (ParameterizedType) field.getGenericType();
                Class clz = (Class) genericType.getActualTypeArguments()[0];
                sort(clz,queue,map);
//                sort();
//                List list = (List) BeanUtil.getMethodByFieldName(t, field.getName());

            }else{
                //普通的bean对象
                sort(field.getType(),queue,map);

            }
        }
        sortMap=map;
        return map;
    }

    private void sort(Class clz, Queue<Boolean> queue, Map<String, Integer> map) {
        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            boolean isBaseType = BeanUtil.isBaseType(field.getType());
            if(isBaseType){
                map.put(field.getName(),queue.size());
                queue.offer(true);
//                queue.offer(String.valueOf(BeanUtil.getMethodByFieldName(t,field.getName())));
            }else if(field.getType().equals(List.class)){
                //list对象
                ParameterizedType genericType = (ParameterizedType) field.getGenericType();
                Class type = (Class) genericType.getActualTypeArguments()[0];
                sort(type,queue,map);
//                sort();
//                List list = (List) BeanUtil.getMethodByFieldName(t, field.getName());

            }else{
                //普通的bean对象
                sort(field.getType(),queue,map);
//                mapLine(BeanUtil.getMethodByFieldName(t, field.getName()),queue);

            }
        }
    }


    //计算每行数据
    public List<Map<String, String>> defaultLineMap(){
        int count =0;
        List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
        Queue<Boolean> queue = new LinkedList<Boolean>();
        Integer size = 0;
        for (T t : list) {
            mapList.add(new HashMap<String, String>(16));
            Field[] fields = t.getClass().getDeclaredFields();
            List<List<Object>> lists = new ArrayList<List<Object>>();
            for (Field field : fields) {
                boolean isBaseType = BeanUtil.isBaseType(field.getType());
                if(isBaseType){
                    put2MapList(mapList,field.getName(),String.valueOf(BeanUtil.getMethodByFieldName(t,field.getName())));
//                    map.put(field.getName(),String.valueOf(BeanUtil.getMethodByFieldName(t,field.getName())));
                }else if(field.getType().equals(List.class)){
                    //list对象,首个是支持多个list对象的
                    List<Object> list = (List<Object>) BeanUtil.getMethodByFieldName(t, field.getName());
                    lists.add(list);
                }else{
                    //普通的bean对象
                    mapLine(BeanUtil.getMethodByFieldName(t, field.getName()),mapList,size);
                }

            }
            //第一个正常list处理
            if(lists.size()>0){
                for (int i = 0; i < lists.size(); i++) {
                    if(i==0){
                        for (Object o : lists.get(0)) {
                            mapLine(o, mapList, size);
                        }
                    }else{
                        for (Object o : lists.get(i)) {
                            multipleMapLine(o,mapList,queue,size,i);
                        }

                    }
                }

            }
//            for (List<Object> objectList : lists) {
//                for (Object o : objectList) {
//                    mapLine(o, mapList);
//                }
//            }

            //最后需要清除最后多余的一条空行
            if(mapList.get(mapList.size()-1).size()<=0){
                mapList.remove(mapList.size()-1);
            }
            size++;
        }

        lineMap=mapList;
        return mapList;
    }

    /**
     * 将对应的值塞入对应的行数
     * @param obj
     * @param mapList
     * @param queue
     * @param size
     * @param i
     */
    private void multipleMapLine(Object obj, List<Map<String, String>> mapList, Queue<Boolean> queue, Integer size, int i) {
        Field[] fields = obj.getClass().getDeclaredFields();
        List<Object> list = new ArrayList<Object>();
        for (Field field : fields) {
            boolean isBaseType = BeanUtil.isBaseType(field.getType());
            if(isBaseType){
                set2MapList(mapList,field.getName(),String.valueOf(BeanUtil.getMethodByFieldName(obj,field.getName())),queue);
                if(field.equals(fields[fields.length-1])&&list.size()<=0){//field为最后一个且没有list
                    //最后一个，添加新的map
                    int value =1;
                    if(null!=listSize){
                        value=listSize.get(size).get(i);
                    }
                    for (int j = 0; j < value; j++) {
                        queue.offer(true);
                    }
//                    mapList.add(new HashMap<String, String>(16));
//                    mapList.add(new HashMap<String, String>(16));
                }
            }else if(field.getType().equals(List.class)){
                //list对象-放后处理
                list=(List<Object>)BeanUtil.getMethodByFieldName(obj, field.getName());
            }else{
                //普通的bean对象
                multipleMapLine(BeanUtil.getMethodByFieldName(obj, field.getName()), mapList, queue, size, i);
            }
        }

        //处理list
        for (Object o : list) {
            multipleMapLine(o, mapList, queue, size, i);
        }
    }

    private void set2MapList(List<Map<String, String>> mapList, String name, String value, Queue<Boolean> queue) {
        Map<String, String> map = mapList.get(queue.size());
        map.put(name,value);
    }

    public Map<Integer,List<MergeInfo>> defaultMergeMap(){

        Map<Integer,List<MergeInfo>> mergeInfoMap = new HashMap<Integer, List<MergeInfo>>();


        for (Map.Entry<String, Integer> entry : sortMap.entrySet()) {

            //初始化MergeInfo
            List<MergeInfo> mergeInfoList = new ArrayList<MergeInfo>();
            mergeInfoMap.put(entry.getValue(),mergeInfoList);
//            MergeInfo defaultMergeInfo = new MergeInfo(0);
//            mergeInfoList.add(defaultMergeInfo);

            int i=-1;
            for (Map<String, String> map : lineMap) {
                i++;
                String val = map.get(entry.getKey());
                if(null!=val){
                    setMergeInfoEnd(mergeInfoList,i);
                }
            }

            mergeInfoList.get(mergeInfoList.size()-1).setEnd(i);

        }

        return mergeInfoMap;
    }

    private void setMergeInfoEnd(List<MergeInfo> mergeInfoList, int i) {
        if(mergeInfoList.size()>0){
            mergeInfoList.get(mergeInfoList.size()-1).setEnd(i-1);
            mergeInfoList.add(new MergeInfo(i));
        }else{
            mergeInfoList.add(new MergeInfo(0));
        }
    }

    private void put2MapList(List<Map<String, String>> mapList, String name, String value) {
        Map<String, String> map = mapList.get(mapList.size() - 1);
        map.put(name,value);
    }

    /**
     *
     * @param obj
     * @param mapList
     * @param count 列表中标识是第几个对象
     */
    private void mapLine(Object obj, List<Map<String, String>> mapList, Integer count) {
        Field[] fields = obj.getClass().getDeclaredFields();
        List<Object> list = new ArrayList<Object>();
        for (Field field : fields) {
            boolean isBaseType = BeanUtil.isBaseType(field.getType());
            if(isBaseType){
                put2MapList(mapList,field.getName(),String.valueOf(BeanUtil.getMethodByFieldName(obj,field.getName())));
                if(field.equals(fields[fields.length-1])&&list.size()<=0){//field为最后一个且没有list
                    //最后一个，添加新的map
                    int value =1;
                    if(null!=listSize){
                        value=listSize.get(count).get(0);
                    }
                    for (int i = 0; i < value; i++) {
                        mapList.add(new HashMap<String, String>(16));
                    }
//                    mapList.add(new HashMap<String, String>(16));
                }
            }else if(field.getType().equals(List.class)){
                //list对象-放后处理
                list=(List<Object>)BeanUtil.getMethodByFieldName(obj, field.getName());
            }else{
                //普通的bean对象
                mapLine(BeanUtil.getMethodByFieldName(obj, field.getName()), mapList, count);
            }
        }

        //处理list
        for (Object o : list) {
            mapLine(o, mapList, count);
        }



    }

    private static void multiRowList(List<Integer> rowList, int size) {
        if(rowList.size()>0){
//            int row = rowList.get(rowList.size() - 1)+size-1;
            rowList.set(rowList.size()-1,rowList.get(rowList.size() - 1)+size);
        }
    }
}
