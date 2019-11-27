package util;

/**
 * ClassName:NormalMathUtil <br/>
 * Description:  <br/>
 * Date:     2019年11月27日 14:35 <br/>
 *
 * @author jinwei.zhang
 * @version 1.0
 * @since JDK 1.8
 */
public class NormalMathUtil {
    public static void main(String[] args) {
        //测试
        int arr[] = {4,6,8,9};
//        System.out.println(getMinMultiCommonMultiple(arr));
    }
    //先求两个数的最大公约数（使用辗转相除法）
    public static int getMaxCommonDivisor(int a,int b) {
        //定义一个交换站值
        int temp =0;
        while(a%b!=0) {
            temp = a%b;
            a = b;
            b =temp;
        }
        return b;
    }
    //求两个数的最小公倍数（两个数相乘   等于   这两个数的最大公约数和最小公倍数的 积）
    public static int getMinCommonMultiple(int a,int b) {
        return a*b/getMaxCommonDivisor(a,b);
    }
    //求多个数的最小公倍数
    public static int getMinMultiCommonMultiple(Integer []arrays) {
        int val = arrays[0];
        //实现原理：拿前两个数的最小公约数和后一个数比较，求他们的公约数以此来推。。。
        for (int i = 1; i < arrays.length; i++) {
            val = getMinCommonMultiple(val, arrays[i]);
        }
        return val;
    }
}
