package util;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * ClassName:NormalExportUtil <br/>
 * Description:  <br/>
 * Date:     2019年11月29日 10:38 <br/>
 *
 * @author jinwei.zhang
 * @version 1.0
 * @since JDK 1.8
 */
public class NormalExportUtil {

    public static void download(SXSSFWorkbook wb, HttpServletResponse response, String filename) {
        try {
            OutputStream out = response.getOutputStream();
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-disposition", String.format("attachment; filename=%s", filename));
            write(wb, out);
        } catch (IOException var4) {
            var4.printStackTrace();
        }

    }

    public static void write(SXSSFWorkbook wb, OutputStream out) {
        try {
            if (null != out) {
                wb.write(out);
                out.flush();
            }
        } catch (IOException var11) {
            var11.printStackTrace();
        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (IOException var10) {
                    var10.printStackTrace();
                }
            }

        }

    }
}
