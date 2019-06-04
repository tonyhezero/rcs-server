package com.geo.rcs.common.util;

/**
 * @author guoyujie
 * @Date 2017/12/29
 * 工具类验证Excel文档
 */
public class WDWUtil {
    /**
     * @描述：是否是2003的excel，返回true是2003
     * @param filePath
     * @return
     */
    public static boolean isExcel2003(String filePath)  {
        return filePath.matches("^.+\\.(?i)(xls)$");
    }

    /**
     * @描述：是否是2007的excel，返回true是2007
     * @param filePath
     * @return
     */
    public static boolean isExcel2007(String filePath)  {
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }

    /**
     * 验证是否是EXCEL文件
     * @param filePath
     * @return
     */
    public static boolean validateExcel(String filePath){
        return filePath != null && (isExcel2003(filePath) || isExcel2007(filePath));
    }
}
