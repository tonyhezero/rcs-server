package com.geo.rcs.common.util.excel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.exception.RcsException;
import com.geo.rcs.common.regex.RegexUtil;
import com.geo.rcs.common.validator.ValidateNull;
import com.geo.rcs.modules.engine.util.DatetimeFormattor;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.*;

import static com.geo.rcs.common.util.WDWUtil.isExcel2007;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.common.util.excel
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年01月02日 上午9:54
 */
public class ReadExcel {
    //总行数
    private int totalRows = 0;
    //总条数
    private int totalCells = 0;
    //错误信息接收器
    private String errorMsg;

    //构造方法
    public ReadExcel() {
    }

    //获取总行数
    public int getTotalRows() {
        return totalRows;
    }

    //获取总列数
    public int getTotalCells() {
        return totalCells;
    }

    //获取错误信息
    public String getErrorInfo() {
        return errorMsg;
    }


    public static Map<String,String> getExcelMap(MultipartFile mFile){
        Map<String,String> map = null;
        //获取文件名
        String fileName = mFile.getOriginalFilename();
        // 根据文件名判断文件是2003版本还是2007版本
        boolean isExcel2003 = true;
        if (isExcel2007(fileName)) {
            isExcel2003 = false;
        }
        try {
            Workbook wb = null;
            // 当excel是2003时,创建excel2003
            if (isExcel2003) {
                wb = new HSSFWorkbook(mFile.getInputStream());
            } else {// 当excel是2007时,创建excel2007
                wb = new XSSFWorkbook(mFile.getInputStream());
            }
            Sheet sheet = wb.getSheetAt(0);
            // 得到Excel的行数
            int totalRows = sheet.getPhysicalNumberOfRows();

            int totalCells = 0;
            // 得到Excel的列数(前提是有行数)
            if (totalRows > 1 && sheet.getRow(1) != null) {
                totalCells = sheet.getRow(1).getPhysicalNumberOfCells();
            }
            map = new HashMap<>(totalCells);
            // 循环Excel行数

            Row enParmName = sheet.getRow(1);
            Row cnParmName = sheet.getRow(2);
            for (int i = 0; i < totalCells; i++) {
                Cell cell = enParmName.getCell(i);
                Cell cnCell = cnParmName.getCell(i);
                String enName = cell.getStringCellValue();
                String cnName = cnCell.getStringCellValue();
                if (StringUtils.isBlank(enName)) {
                    throw new RcsException("解析异常,文件已被破坏!");
                } else {
                    map.put(enName,cnName);
                }
            }
        } catch (IOException e) {
            throw new RcsException(e.getMessage());
        }

        return map;
    }

    /**
     * 验证EXCEL文件
     *
     * @param filePath
     * @return
     */
    public boolean validateExcel(String filePath) {
        if (filePath == null || !(isExcel2003(filePath) || isExcel2007(filePath))) {
            errorMsg = "文件名不是excel格式";
            return false;
        }
        return true;
    }

    // @描述：是否是2003的excel，返回true是2003
    public static boolean isExcel2003(String filePath) {
        return filePath.matches("^.+\\.(?i)(xls)$");
    }
}