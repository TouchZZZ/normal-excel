package util;

import demo.DemoBean;
import demo.DemoOne;
import demo.DemoOneOne;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ClassName:BeanUtil <br/>
 * Description:  <br/>
 * Date:     2019年11月22日 17:20 <br/>
 *
 * @author jinwei.zhang
 * @version 1.0
 * @since JDK 1.8
 */
public class BeanUtil {
    /**
     * 判断object是否为基本类型
     * @return
     */
    public static boolean isBaseType(Class className) {
//        Class className = object.getClass();
        if (className.equals(java.lang.Integer.class) ||
                className.equals(java.lang.Byte.class) ||
                className.equals(java.lang.Long.class) ||
                className.equals(java.lang.Double.class) ||
                className.equals(java.lang.Float.class) ||
                className.equals(java.lang.Character.class) ||
                className.equals(java.lang.Short.class) ||
                className.equals(String.class) ||
                className.equals(BigDecimal.class) ||
                className.equals(Date.class) ||
                className.equals(java.lang.Boolean.class)) {
            return true;
        }
        return false;
    }


    /**
     * 获取非基础类的属性值
     * @param object
     * @return
     */
    public static List<List<Object>> getNotBaseBeanList(Object object) {
        List<List<Object>> list = new ArrayList<List<Object>>();
        Class<?> clz = object.getClass();
        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            boolean isBaseBean = isBaseType(field.getType());
            if(!isBaseBean){
                if(field.getType().equals(List.class)){
                    Object data = getMethodByFieldName(object, field.getName());
                    list.add((List<Object>)data);
//                    list.add(field.getName());
                }else{
                    Object data = getMethodByFieldName(object, field.getName());
                    List<Object> objList = new ArrayList<Object>();
                    objList.add(data);
                    list.add(objList);
                }
            }
        }
        return list;
    }

    public static Object getMethodByFieldName(Object object,String name) {
        String methodName = getGetMethodString(name);
        Method method = null;
        try {
            method = object.getClass().getDeclaredMethod(methodName);
            try {
                return method.invoke(object);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }



    private static String getGetMethodString(String name) {
        return "get"+name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public static void main(String[] args) {
        DemoBean demoBean = new DemoBean();
        DemoOne demoOne = new DemoOne();
        demoOne.getDemoOneOne().add(new DemoOneOne());
        demoOne.getDemoOneOne().add(new DemoOneOne());
        demoBean.getDemoOne().add(demoOne);
        demoBean.getDemoOne().add(new DemoOne());
        List<List<Object>> notBaseBeanList = getNotBaseBeanList(demoBean);

        List<Integer> rowList = new ArrayList<Integer>();
        for (List<Object> objectList : notBaseBeanList) {
            rowList.add(0);
//            if(null==objectList||null==objectList.get(0)){
//                continue;
//            }
//            getNotBaseBeanList(objectList.get(0));
//            getMaxRowSize(objectList,rowList);
        }
    }




}
