package com.geo.rcs.modules.rule.util;


import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.exception.RcsException;
import com.geo.rcs.common.util.Encodes;
import com.geo.rcs.common.util.JSONUtil;
import com.geo.rcs.modules.engine.entity.FunctionField;
import com.geo.rcs.modules.rule.condition.entity.Conditions;
import com.geo.rcs.modules.rule.entity.EngineRule;
import com.geo.rcs.modules.rule.field.entity.EngineField;
import com.geo.rcs.modules.rule.ruleset.entity.EngineRules;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.geo.rcs.common.util.WDWUtil.isExcel2003;
import static com.geo.rcs.common.util.WDWUtil.isExcel2007;


/**
 * 导出规则集Excel
 * @Project : rcs
 * @Package Name : com.geo.rcs.common.util.excel
 * @Description : TODD
 * @Author zhengxingwang
 * @email zhengxingwang@geotmt.com
 * @Creation Date : 2019年03月11日 下午18:20
 */


public class RulesExcelUtil {

    /**
     * 工作簿对象
     */
    private SXSSFWorkbook wb;

    /**
     * 解析时工作簿对象
     */
    private Workbook wbb;
    /**
     * 工作表对象
     */
    private Sheet sheet;

    /**
     * 函数集工作表对象
     */
    private Sheet funcSheet;

    /**
     * 样式列表
     */
    private Map<String, CellStyle> styles;

    /**
     * 当前行号
     */
    private int rownum;

    /**
     * 函数集行数
     */
    private int funcRownum;

    /**
     * 错误消息
     */
    private String errorMsg;

    /**
     * 函数集Sheet标记
     */
    private Boolean funcFlag = true;

    /**
     *  函数集sheet数据,字段id 对应 规则集字段
     */
    private Map<Long, EngineField> funcSheetData = new HashMap<>();

    //列名
    private static List<String> columnName;
    //合理运算符集合
    private static Map<String,List<String>> operaterValidate = new HashMap<>();
    //fieldType id和name映射
    private static List<Integer> fieldTypeIdList;
    private static List<String> fieldTypeNameList;
    static{
        columnName = Arrays.asList("A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z");

        operaterValidate.put("int",Arrays.asList("==","!=",">=","<=",">","<","isnull","notnull"));
        operaterValidate.put("float",Arrays.asList("==","!=",">=","<=",">","<","isnull","notnull"));
        operaterValidate.put("string",Arrays.asList("==","!=","isnull","notnull"));
        operaterValidate.put("datetime",Arrays.asList("==","!=",">=","<=",">","<","->","->=","-<","-!=","-==","-<=","isnull","notnull"));
        operaterValidate.put("date",Arrays.asList("==","!=",">=","<=",">","<","->","->=","-<","-!=","-==","-<=","isnull","notnull"));
        operaterValidate.put("function",Arrays.asList("==","!=",">=","<=",">","<","isnull","notnull"));
        operaterValidate.put("sum",Arrays.asList("==","!=",">=","<=",">","<","isnull","notnull"));
        operaterValidate.put("avg",Arrays.asList("==","!=",">=","<=",">","<","isnull","notnull"));
        operaterValidate.put("max",Arrays.asList("==","!=",">=","<=",">","<","isnull","notnull"));
        operaterValidate.put("min",Arrays.asList("==","!=",">=","<=",">","<","isnull","notnull"));
        operaterValidate.put("day_diff",Arrays.asList("==","!=",">=","<=",">","<","isnull","notnull"));
        operaterValidate.put("month_diff",Arrays.asList("==","!=",">=","<=",">","<","isnull","notnull"));
        operaterValidate.put("decreasing",Arrays.asList("==","!=",">=","<=",">","<","isnull","notnull"));


        fieldTypeIdList = Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13);
        fieldTypeNameList = Arrays.asList("int","string","datetime","date","float","function","sum","avg","max","min","day_diff","month_diff","decreasing");
    }

    /**列名对应列数常量*/
    //规则ID
    private static int RULE_ID_COLUMN = 0;
    //规则名称
    private static int RULE_NAME_COLUMN = 1;
    //风险等级
    private static int RISK_LEVEL_COLUMN = 2;
    //风险分数
    private static int RISK_SCORE_COLUMN = 2;
    //返回码
    private static int RULE_RETURN_CODE_COLUM = 3;
    //规则描述
    private static int RULE_DESC_COLUMN = 3+1;
    //激活状态
    private static int ACTIVE_STATUS_COLUMN = 4+1;
    //条件逻辑关系
    private static int CONDITION_RELATIONSHIP_COLUMN = 5+1;
    //条件ID
    private static int CONDITION_ID_COLUMN = 6+1;
    //条件名称
    private static int CONDITION_NAME_COLUMN = 7+1;
    //条件描述
    private static int CONDITION_DESC_COLUMN = 8+1;
    //字段逻辑关系
    private static int FIELD_RELATIONSHIP_COLUMN = 9+1;
    //字段ID
    private static int FIELD_ID_COLUMN = 10+1;
    //字段key
    private static int FIELD_KEY_COLUMN = 11+1;
    //字段名
    private static int FIELD_NAME_COLUMN = 12+1;
    //运算符
    private static int FIELD_OPERATION_COLUMN = 13+1;
    //对比值
    private static int PARAMETER_VLAUE_COLUMN = 14+1;
    //字段备注
    private static int FIELD_REMARK_COLUMN = 15+1;

    //函数id
    private static int FUNCTION_ID_COLUMN = 0;
    //函数名称
    private static int FUNCTION_NAME_COLUMN = 1;
    //函数运算符
    private static int FUNCTION_OPERATION_COLUMN = 2;
    //函数字段id
    private static int FUNCTION_FIELD_ID_COLUMN = 3;
    //函数字段
    private static int FUNCTION_FIELD_COLUMN = 4;

    //规则集开始列的位置
    private static int RULE_START_NUM = 0;
    //规则集结束列的位置
    private static int RULE_END_NUM = 5+1;
    //条件开始列的位置
    private static int CONDITION_START_NUM = 6+1;
    //条件结束列的位置
    private static int CONDITION_END_NUM = 9+1;
    //字段开始列的位置
    private static int FIELD_START_NUM = 10+1;
    //字段结束列的位置
    private static int FIELD_END_NUM = 15+1;

    //规则集总列数
    private static int RULE_COLUMN_TOTAL_NUM = 16+1;
    //函数集总列数
    private static int FUNCTION_COLUMN_TOTAL_NUM = 5;

    public String getErrorMsg() {
        return errorMsg;
    }


    /**
     * 默认构造函数
     */
    public RulesExcelUtil(){

    }
    /**
     * 导出规则集到Excel
     * @param engineRules
     * @param rulesInfoMap 规则集业务名称和场景名称补充
     */
    public RulesExcelUtil(EngineRules engineRules,Map<String,String> rulesInfoMap,List<Map> fieldInfoList){

        Map<Integer,String> fieldNameMapping = new HashMap<>();
        //id映射字段中文名
        for (int i = 0; i < fieldInfoList.size(); i++){
            BigInteger id = (BigInteger)(fieldInfoList.get(i).get("id"));
            fieldNameMapping.put(id.intValue(),(String)(fieldInfoList.get(i).get("describ")));
        }

        //规则集基本信息
        createRulesInfo(engineRules,rulesInfoMap);
        //规则集详细信息
        createRulesDetail(engineRules,fieldNameMapping);

    }


    /**
     * Excel显示规则集详细信息
     * @param rules
     */
    public void createRulesDetail(EngineRules rules,Map<Integer,String> fieldNameMapping){

        //空一行
        rownum++;
        List<String> titleList = new ArrayList();
        if (rules.getMatchType() == 0){
            titleList =  Arrays.asList("规则ID","规则名称","风险分数","返回码","规则描述","激活状态","条件逻辑关系",
                                         "条件ID","条件名称","条件描述","字段逻辑关系","字段ID","字段key","字段名",
                                         "运算符","对比值","字段备注");
        }else if (rules.getMatchType() == 1) {
            titleList = Arrays.asList("规则ID", "规则名称", "风险等级","返回码", "规则描述", "激活状态", "条件逻辑关系",
                    "条件ID", "条件名称", "条件描述", "字段逻辑关系", "字段ID", "字段key", "字段名",
                    "运算符", "对比值", "字段备注");
        }else{
            return;
        }

        createTitleRow(titleList);
        List<EngineRule> engineRuleList = rules.getRuleList();
        for (int i = 0; i < engineRuleList.size(); i++){

            Boolean rowConditionFlag = true;
            Row row = sheet.createRow(rownum++);
            fillRuleRow(row,RULE_START_NUM,engineRuleList.get(i),rules);
            List<Conditions> conditionList = engineRuleList.get(i).getConditionsList();
            for (int j = 0; j < conditionList.size(); j++){

                if (!rowConditionFlag){
                    row = sheet.createRow(rownum++);
                }
                fillConditionRow(row,CONDITION_START_NUM,conditionList.get(j));
                rowConditionFlag = false;
                Boolean rowFieldFlag = true;
                List<EngineField> engineFieldList = conditionList.get(j).getFieldList();
                for (int k = 0; k < engineFieldList.size(); k++){

                    if (!rowFieldFlag){
                        row = sheet.createRow(rownum++);
                    }
                    fillFieldRow(row,FIELD_START_NUM,engineFieldList.get(k),fieldNameMapping);
                    rowFieldFlag = false;
                }
            }

        }


    }


    /**
     * 填充规则详细信息中的字段
     * @param row
     * @param columnNum
     * @param field
     */
    public void fillFieldRow(Row row, int columnNum, EngineField field, Map<Integer,String> fieldNameMapping){

        Cell cellID = row.createCell(columnNum++);
        cellID.setCellStyle(styles.get("rulesInfo"));
        cellID.setCellValue(field.getId());

        Cell cellFieldId = row.createCell(columnNum++);
        cellFieldId.setCellStyle(styles.get("rulesInfo"));
        cellFieldId.setCellValue(field.getFieldId());

        Cell cellFieldName = row.createCell(columnNum++);
        cellFieldName.setCellStyle(styles.get("rulesInfo"));
        cellFieldName.setCellValue(fieldNameMapping.get(field.getFieldId()));

        Cell cellOperator = row.createCell(columnNum++);
        cellOperator.setCellStyle(styles.get("rulesInfo"));
        cellOperator.setCellValue(field.getOperator());

        Cell cellParameter = row.createCell(columnNum++);
        cellParameter.setCellStyle(styles.get("rulesInfo"));
        cellParameter.setCellValue(field.getParameter());

        Cell cellDesc = row.createCell(columnNum++);
        cellDesc.setCellStyle(styles.get("rulesInfo"));
        cellDesc.setCellValue(field.getDescrib());

        if (field.getFunctionSet() == null || field.getFunctionSet().size() == 0){
            return;
        }
        //添加函数集信息
        fillFunctionSheet(field);

    }

    /**
     * 将函数集信息添加到函数集sheet
     * @param field
     */
    public void fillFunctionSheet(EngineField field){

        if (funcFlag) {
            this.funcSheet = this.wb.createSheet("函数集");
            for (int i = 0; i < FUNCTION_COLUMN_TOTAL_NUM; i++) {
                funcSheet.setColumnWidth(i, 4000);
            }
            //标题栏
            Row titleRow = funcSheet.createRow(0);
            List<String> funcTitleList = Arrays.asList("函数id", "函数名称", "函数运算符", "字段id", "字段");
            for (int i = 0; i < funcTitleList.size(); i++) {
                Cell cell = titleRow.createCell(i);
                cell.setCellValue(funcTitleList.get(i));
                cell.setCellStyle(styles.get("titleStyle"));
            }
            this.funcRownum++;
            funcFlag = false;
        }
        //内容
        Row row = funcSheet.createRow(funcRownum);
        //函数id
        Cell cellFuncId = row.createCell(FUNCTION_ID_COLUMN);
        cellFuncId.setCellValue(field.getId());
        cellFuncId.setCellStyle(styles.get("rulesInfo"));
        //函数名称
        Cell cellFuncName = row.createCell(FUNCTION_NAME_COLUMN);
        cellFuncName.setCellValue(field.getFieldName());
        cellFuncName.setCellStyle(styles.get("rulesInfo"));
        //函数运算符
        Cell cellFuncOperator = row.createCell(FUNCTION_OPERATION_COLUMN);
        cellFuncOperator.setCellValue(fieldTypeNameList.get(fieldTypeIdList.indexOf(field.getFieldTypeId())));
        cellFuncOperator.setCellStyle(styles.get("rulesInfo"));
        //字段id+字段名称
        List<FunctionField> funcFieldList = field.getFunctionSet();
        for (int i = 0; i < funcFieldList.size(); i++){
            Row fieldRow = i==0 ? funcSheet.getRow(funcRownum++) : funcSheet.createRow(funcRownum++);
            Cell fieldIdCell = fieldRow.createCell(FUNCTION_FIELD_ID_COLUMN);
            fieldIdCell.setCellValue(funcFieldList.get(i).getFieldId());
            fieldIdCell.setCellStyle(styles.get("rulesInfo"));
            Cell fieldNameCell = fieldRow.createCell(FUNCTION_FIELD_COLUMN);
            fieldNameCell.setCellValue(funcFieldList.get(i).getFieldName());
            fieldNameCell.setCellStyle(styles.get("rulesInfo"));
        }


    }


    /**
     * 填充规则详细信息中的条件
     * @param row
     * @param columnNum
     * @param conditions
     */
    public void fillConditionRow(Row row, int columnNum, Conditions conditions){

        Cell cellID = row.createCell(columnNum++);
        cellID.setCellStyle(styles.get("rulesInfo"));
        cellID.setCellValue(conditions.getId());

         Cell cellConditionName = row.createCell(columnNum++);
         cellConditionName.setCellStyle(styles.get("rulesInfo"));
         cellConditionName.setCellValue(conditions.getName());

         Cell cellDesc = row.createCell(columnNum++);
         cellDesc.setCellStyle(styles.get("rulesInfo"));
         cellDesc.setCellValue(conditions.getDescrib());

         Cell cellFieldRelationShip = row.createCell(columnNum++);
         cellFieldRelationShip.setCellStyle(styles.get("rulesInfo"));
         cellFieldRelationShip.setCellValue(conditions.getFieldRelationship());

    }
    /**
     * 填充规则详细信息中的规则
     * @param row
     * @param columNum
     * @param rule
     * @param rules
     * @return
     */
    public void fillRuleRow(Row row, int columNum, EngineRule rule, EngineRules rules){

        Cell cellID = row.createCell(columNum++);
        cellID.setCellStyle(styles.get("rulesInfo"));
        cellID.setCellValue(rule.getId());

        Cell cellName = row.createCell(columNum++);
        cellName.setCellStyle(styles.get("rulesInfo"));
        cellName.setCellValue(rule.getName());

        Cell cellRisk = row.createCell(columNum++);
        cellRisk.setCellStyle(styles.get("rulesInfo"));
        if (rules.getMatchType() == 0){
            cellRisk.setCellValue(rule.getThreshold());
        }else if (rules.getMatchType() == 1){
            String cellRiskValue = "";
            if (rule.getLevel() == 1){
                cellRiskValue = "无风险";
            }else if (rule.getLevel() == 2){
                cellRiskValue = "低风险";
            }else if (rule.getLevel() == 3){
                cellRiskValue = "高风险";
            }
            cellRisk.setCellValue(cellRiskValue);
        }

        Cell cellReturnCode = row.createCell(columNum++);
        cellReturnCode.setCellStyle(styles.get("rulesInfo"));
        cellReturnCode.setCellValue(rule.getErrorCode());

        Cell cellDescrib = row.createCell(columNum++);
        cellDescrib.setCellStyle(styles.get("rulesInfo"));
        cellDescrib.setCellValue(rule.getDecision());

        Cell cellActive = row.createCell(columNum++);
        cellActive.setCellStyle(styles.get("rulesInfo"));
        cellActive.setCellValue(rule.getActive() == 0 ? "未激活" : "已激活");

        Cell cellConditionRelationShip = row.createCell(columNum++);
        cellConditionRelationShip.setCellStyle(styles.get("rulesInfo"));
        cellConditionRelationShip.setCellValue(rule.getConditionRelationship());

    }

    /**
     * 生成标题栏
     * @param titleList
     */
    public void createTitleRow(List<String> titleList){

        Row row = sheet.createRow(rownum++);
        row.setHeight((short)500);
        for (int i = 0; i < titleList.size(); i++){
            Cell cell = row.createCell(i);
            cell.setCellStyle(styles.get("titleStyle"));
            cell.setCellValue(titleList.get(i));
        }

    }

    /**
     * Excel显示规则集基本信息
     * @param rules
     */
    public void createRulesInfo(EngineRules rules,Map<String,String> rulesInfoMap){

        this.wb = new SXSSFWorkbook(500);
        String sheetName = "";
        if (rules.getMatchType() == 0){
            sheetName = "权重匹配模式";
        }else if (rules.getMatchType() == 1){
            sheetName  = "最坏匹配模式";
        }else{
            sheetName = "sheet";
        }
        this.sheet = wb.createSheet(sheetName);
        for (int i = 0; i < RULE_COLUMN_TOTAL_NUM; i++) {
            sheet.setColumnWidth(i, 3800);
        }
        this.styles = createStyles(wb);

        List<String> params = new ArrayList<>();
        params.add("规则集名称");
        params.add(rules.getName());
        fileOneRow(params);
        params.clear();
        params.add("业务类型");
        params.add(rulesInfoMap.get("businessName"));
        fileOneRow(params);
        params.clear();
        params.add("场景");
        params.add(rulesInfoMap.get("sceneName"));
        fileOneRow(params);
        params.clear();
        params.add("匹配模式");
        params.add(sheetName);
        fileOneRow(params);
        if (rules.getMatchType() == 0){
            params.clear();
            params.add("阈值范围");
            params.add(rules.getThresholdMin()+"-"+rules.getThresholdMax());
            fileOneRow(params);
        }

    }

    /**
     * 封装好数据，Excel填充一行
     * @param params
     */
    public void fileOneRow(List<String> params){

        if (params.size() <= 0){
            return;
        }
        Row row = sheet.createRow(rownum++);
        row.setHeight((short)500);

         Cell titleCell = row.createCell(0);
         titleCell.setCellStyle(styles.get("titleStyle"));
         titleCell.setCellValue(params.get(0));

        Cell valueCell = row.createCell(1);
        valueCell.setCellStyle(styles.get("rulesInfo"));
        valueCell.setCellValue(params.get(1));
    }


    /**
     * 创建表格样式
     * @param wb 工作薄对象
     * @return 样式列表
     */
    private Map<String, CellStyle> createStyles(Workbook wb) {
        Map<String, CellStyle> styles = new HashMap<String, CellStyle>();

        CellStyle style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setBorderBottom(CellStyle.BORDER_NONE);
        Font infoFont = wb.createFont();
        infoFont.setFontName("DengXian");
        infoFont.setFontHeightInPoints((short)12);
        style.setFont(infoFont);
        styles.put("rulesInfo",style);

        CellStyle titleStyle = wb.createCellStyle();
        titleStyle.setAlignment(CellStyle.ALIGN_CENTER);
        titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        titleStyle.setBorderBottom(CellStyle.BORDER_NONE);
        Font titleFont = wb.createFont();
        titleFont.setFontName("DengXian");
        titleFont.setFontHeightInPoints((short)13);
        titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        titleStyle.setFont(titleFont);
        styles.put("titleStyle",titleStyle);

        return styles;
    }

    /**
     * 输出数据流
     * @param os 输出数据流
     */
    public RulesExcelUtil write(OutputStream os) throws IOException{
        wb.write(os);
        return this;
    }

    /**
     * 输出到客户端
     * @param fileName 输出文件名
     */
    public RulesExcelUtil write(HttpServletResponse response, String fileName) throws IOException{
        response.reset();
        response.setContentType("application/octet-stream; charset=utf-8");
        response.setHeader("Content-Disposition", "attachment; filename="+ Encodes.urlEncode(fileName));
        write(response.getOutputStream());
        return this;
    }
    /**
     * 输出到客户端兼容多版本浏览器
     * @param fileName 输出文件名
     */
    public RulesExcelUtil write(HttpServletRequest request, HttpServletResponse response, String fileName) throws IOException{
        String userAgent = request.getHeader("User-Agent");
        // 针对IE或者以IE为内核的浏览器：
        if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
        } else {
            // 非IE浏览器的处理：
            fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
        }
        response.reset();
        response.setHeader("Content-disposition",String.format("attachment; filename=\"%s\"", fileName));
        response.setContentType("multipart/form-data");
        response.setCharacterEncoding("UTF-8");
        write(response.getOutputStream());
        return this;
    }
    /**
     * 输出到文件
     * @param name 输出文件名
     */
    public RulesExcelUtil writeFile(String name) throws IOException{
        FileOutputStream os = new FileOutputStream(name);
        this.write(os);
        return this;
    }

    /**
     * 清理临时文件
     */
    public RulesExcelUtil dispose(){
        wb.dispose();
        return this;
    }


    /*************************************************** 解析规则集Excel分割线*****************************************************/


/*    public static void main(String[] args){

         String filePath = "E://test.xlsx";
         File file = new File(filePath);
         RulesExcelUtil rulesExcelUtil = new RulesExcelUtil();
        try {
            String result = rulesExcelUtil.getRulesFromExcel(new MockMultipartFile(file.getName(),new FileInputStream(file)));
            System.out.println(result);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }*/
    /**
     * 解析Excel返回规则集Json串
     * @param file
     * @return
     */
    public String getRulesFromExcel(MultipartFile file, List<Map> fieldInfoList){

        String fileName = file.getOriginalFilename();
        String rulesJson = "";
        if (!validateExcel(fileName)){
            throw new RcsException(errorMsg);
        }
        Boolean isExcel2003Flag  = isExcel2003(fileName) ? true : false;
        try {
            rulesJson = parseRulesExcel(file.getInputStream(),isExcel2003Flag,fieldInfoList);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return rulesJson;

    }

    /**
     * 解析规则集Excel文件为规则集Json
     * @param is
     * @param isExcel2003Flag
     * @return
     */
    public String parseRulesExcel(InputStream is,Boolean isExcel2003Flag,List<Map> fieldInfoList) throws IOException {

          wbb = isExcel2003Flag ? new HSSFWorkbook(is) : new XSSFWorkbook(is);

          //解析函数集Sheet
          parseFuncSheet();
          //解析规则集Sheet并整合函数集
          String ruleSetJson = parseBaseRuleSetSheet(fieldInfoList);
          return ruleSetJson;

    }

    /**
     * 解析函数集Sheet表中的数据
     */
    public void parseFuncSheet(){

        int sheetNum = wbb.getNumberOfSheets();
        if (sheetNum < 2){
            return;
        }
        Sheet sheet = wbb.getSheetAt(1);
        funcRownum = sheet.getPhysicalNumberOfRows();

        EngineField field = new EngineField();
        boolean firstFlag = false;
        for (int i = 1; i < funcRownum; i++){
            Row row = sheet.getRow(i);
            //规则字段信息
            if (checkFuncBase(row,i)){
                if (firstFlag){
                    funcSheetData.put(field.getId(),field);
                }
                field = new EngineField();
                field.setFunctionSet(new ArrayList<>());
                field.setId(Long.valueOf(getCellValue(i,FUNCTION_ID_COLUMN,row.getCell(FUNCTION_ID_COLUMN))));
                field.setFieldName(String.valueOf(getCellValue(i,FUNCTION_NAME_COLUMN,row.getCell(FUNCTION_NAME_COLUMN))));
                field.setFieldTypeId(fieldTypeIdList.get(fieldTypeNameList.indexOf(String.valueOf(getCellValue(i,FUNCTION_OPERATION_COLUMN,row.getCell(FUNCTION_OPERATION_COLUMN))))));
                firstFlag = true;
            }
            //函数集字段信息
            FunctionField functionField = new FunctionField();
            functionField.setFieldId(String.valueOf(getCellValue(i,FUNCTION_FIELD_ID_COLUMN,row.getCell(FUNCTION_FIELD_ID_COLUMN))));
            functionField.setFieldName(String.valueOf(getCellValue(i,FUNCTION_FIELD_COLUMN,row.getCell(FUNCTION_FIELD_COLUMN))));
            functionField.setFunctionId(field.getId());
            field.getFunctionSet().add(functionField);
        }
        funcSheetData.put(field.getId(),field);

    }

    /**
     * 检查函数集Sheet表的函数id，函数名称，函数运算符
     * @param row
     * @param r
     * @return
     */
    public boolean checkFuncBase(Row row, int r){

        List<Integer> emptyCellList = new ArrayList<>();
        for (int i = 0; i < 3; i++){
            if (getCellValue(r,i,row.getCell(i)).equalsIgnoreCase("")){
                emptyCellList.add(i);
            }
        }

        if (emptyCellList.size() == 3){
            return false;
        }
        if (emptyCellList.contains(FUNCTION_ID_COLUMN)){
            throw new RcsException("请勿修改函数集Sheet表中第"+(r+1)+"行"+columnName.get(FUNCTION_ID_COLUMN)+"列的函数id");
        }
        if (emptyCellList.contains(FUNCTION_NAME_COLUMN)){
            throw new RcsException("请填写函数集Sheet表中第"+(r+1)+"行"+columnName.get(FUNCTION_NAME_COLUMN)+"列的函数名称");
        }
        if (emptyCellList.contains(FUNCTION_OPERATION_COLUMN)){
            throw new RcsException("请填写函数集Sheet表中第"+(r+1)+"行"+columnName.get(FUNCTION_OPERATION_COLUMN)+"列的函数运算符");
        }
        if (fieldTypeNameList.indexOf(getCellValue(r,FUNCTION_OPERATION_COLUMN,row.getCell(FUNCTION_OPERATION_COLUMN))) == -1){
            throw new RcsException("请勿随意修改函数集Sheet表中第"+(r+1)+"行"+columnName.get(FUNCTION_OPERATION_COLUMN)+"列的函数运算符");
        }
        return true;
    }
    /**
     * 解析规则集Sheet，整合函数集Sheet数据
     * @param fieldInfoList
     * @return
     */
    public String parseBaseRuleSetSheet(List<Map> fieldInfoList){

        EngineRules rules = new EngineRules();
        //get规则集sheet
        Sheet sheet = wbb.getSheetAt(0);
        //总行数
        int totalRows = sheet.getPhysicalNumberOfRows();
        //规则详细数据开始行
        int ruleStartRow = 0;

        int matchType = getRulesInfoFromExcel(rules,sheet);

        getRulesDetailFromExcel(rules,sheet,totalRows,matchType,fieldInfoList);

        String result = JSONUtil.beanToJson(rules);
        return result;
    }

    /**
     * 获取规则集详细信息
     * @param rules
     * @param sheet
     * @param totalRows
     * @param matchType
     */
    public void getRulesDetailFromExcel(EngineRules rules, Sheet sheet, int totalRows, int matchType, List<Map> fieldInfoList){

        //获取字段信息映射关系
        Map<Integer,String> fieldDescMapping = new HashMap<>();
        Map<Integer,String> fieldNameMapping = new HashMap<>();
        Map<Integer,Integer> fieldTypeMapping = new HashMap<>();

        for (int i = 0; i < fieldInfoList.size(); i++){
            BigInteger id = (BigInteger) fieldInfoList.get(i).get("id");
            fieldDescMapping.put(id.intValue(),(String)fieldInfoList.get(i).get("describ"));
            fieldNameMapping.put(id.intValue(),(String)fieldInfoList.get(i).get("name"));
            fieldTypeMapping.put(id.intValue(),(Integer)fieldInfoList.get(i).get("field_type"));
        }
        rules.setRuleList(new ArrayList<>());
        int startRowNum = 0;
        if (matchType == 0){          //权重匹配模型
            startRowNum = 7;
            if (sheet.getRow(6) == null ||!getCellValue(6,RISK_SCORE_COLUMN,sheet.getRow(6).getCell(RISK_SCORE_COLUMN)).equalsIgnoreCase("风险分数")){
                throw new RcsException("请勿在Excel中修改匹配模式，可选择导入到平台再修改！");
            }
        }else if (matchType == 1){    //最坏匹配模式
            startRowNum = 6;

            if (sheet.getRow(5) == null || !getCellValue(5,RISK_LEVEL_COLUMN,sheet.getRow(5).getCell(RISK_LEVEL_COLUMN)).equalsIgnoreCase("风险等级")){
                throw new RcsException("请勿在Excel中修改匹配模式，可选择导入到平台再修改！");
            }

        }

        EngineRule rule = new EngineRule();
        Conditions conditions = new Conditions();
        EngineField engineField = new EngineField();
        boolean firstFlag = false;

        for (int i = startRowNum; i <= totalRows; i++) {

            Row row = sheet.getRow(i);

            /**规则信息*/
            if (checkRuleInfoForRow(row,i,matchType)) {
                if (firstFlag) {
                    rules.getRuleList().add(rule);
                }
                rule = new EngineRule();
                rule.setConditionsList(new ArrayList<>());
                firstFlag = true;

                rule.setId(Long.valueOf(getCellValue(i, RULE_ID_COLUMN, row.getCell(RULE_ID_COLUMN)).trim()));
                rule.setName(getCellValue(i, RULE_NAME_COLUMN, row.getCell(RULE_NAME_COLUMN)).trim());

                if (matchType == 0) {   //权重匹配模式
                    rule.setThreshold(Integer.valueOf(getCellValue(i,RISK_SCORE_COLUMN, row.getCell(RISK_SCORE_COLUMN)).trim()));
                } else if (matchType == 1) {    //最坏匹配模式
                    String levelStr = getCellValue(i, RISK_LEVEL_COLUMN, row.getCell(RISK_LEVEL_COLUMN)).trim();
                    if (levelStr.equalsIgnoreCase("无风险")) {
                        rule.setLevel(1);
                    } else if (levelStr.equalsIgnoreCase("低风险")) {
                        rule.setLevel(2);
                    } else if (levelStr.equalsIgnoreCase("高风险")) {
                        rule.setLevel(3);
                    } else {
                        throw new RcsException("第" + (i + 1) + "行" + columnName.get(RISK_LEVEL_COLUMN) + "列风险等级，请选择填写无风险、低风险、高风险，再重新上传文件");
                    }
                }
                rule.setErrorCode(getCellValue(i,RULE_RETURN_CODE_COLUM,row.getCell(RULE_RETURN_CODE_COLUM)));
                rule.setDescrib(getCellValue(i, RULE_DESC_COLUMN, row.getCell(RULE_DESC_COLUMN)));
                String activeStr = getCellValue(i, ACTIVE_STATUS_COLUMN, row.getCell(ACTIVE_STATUS_COLUMN));
                if (activeStr.equalsIgnoreCase("已激活")) {
                    rule.setActive(1);
                } else if (activeStr.equalsIgnoreCase("未激活")) {
                    rule.setActive(0);
                } else {
                    throw new RcsException("第" + (i + 1) + "行" + "第" + columnName.get(ACTIVE_STATUS_COLUMN) + "列激活状态，请选择填写已激活或未激活,再重新上传文件");
                }
                String conditionRelationShipStr = getCellValue(i, CONDITION_RELATIONSHIP_COLUMN, row.getCell(CONDITION_RELATIONSHIP_COLUMN));
                rule.setConditionRelationship(conditionRelationShipStr);
            }

            /**条件信息*/
            if (checkConditionInfoForRow(row,i)) {
                conditions = new Conditions();
                conditions.setFieldList(new ArrayList<>());

                conditions.setId(Long.valueOf(getCellValue(i, CONDITION_ID_COLUMN, row.getCell(CONDITION_ID_COLUMN)).trim()));
                conditions.setName(getCellValue(i, CONDITION_NAME_COLUMN, row.getCell(CONDITION_NAME_COLUMN)).trim());
                conditions.setDescrib(getCellValue(i, CONDITION_DESC_COLUMN, row.getCell(CONDITION_DESC_COLUMN)).trim());
                conditions.setFieldRelationship(getCellValue(i, FIELD_RELATIONSHIP_COLUMN, row.getCell(FIELD_RELATIONSHIP_COLUMN)));
                rule.getConditionsList().add(conditions);
            }
            /**字段信息*/
             if (checkFieldInfoForRow(row,i)) {
                 engineField = new EngineField();

                 Long id = Long.valueOf(getCellValue(i, FIELD_ID_COLUMN, row.getCell(FIELD_ID_COLUMN)));
                 engineField.setId(id);
                 Integer fieldId = Integer.valueOf(getCellValue(i, FIELD_KEY_COLUMN, row.getCell(FIELD_KEY_COLUMN)));
                 if (fieldTypeMapping.get(fieldId) == null){
                     throw new RcsException("请勿随意修改第"+(i+1)+"行"+columnName.get(FIELD_KEY_COLUMN)+"列的字段key！");
                 }
                 engineField.setFieldId(fieldId);
                 engineField.setFieldName(fieldNameMapping.get(fieldId));
                 engineField.setFieldTypeId(fieldTypeMapping.get(fieldId));
                 engineField.setFieldType(fieldTypeNameList.get(fieldTypeIdList.indexOf(fieldTypeMapping.get(fieldId))));
                 if (funcSheetData.get(id) != null){
                     engineField.setFunctionSet(funcSheetData.get(id).getFunctionSet());
                     engineField.setFieldName(funcSheetData.get(id).getFieldName());
                 }
                 String operator = getCellValue(i, FIELD_OPERATION_COLUMN, row.getCell(FIELD_OPERATION_COLUMN)).trim();
                 if (!operaterValidate.get(fieldTypeNameList.get(fieldTypeIdList.indexOf(fieldTypeMapping.get(fieldId)))).contains(operator)){
                     throw new RcsException("对于"+fieldTypeNameList.get(fieldTypeIdList.indexOf(fieldTypeMapping.get(fieldId)))+"类型的字段，第"+(i+1)+"行"+""+columnName.get(FIELD_OPERATION_COLUMN)+"列的运算符配置不合理！");
                 }
                 engineField.setOperator(getCellValue(i, FIELD_OPERATION_COLUMN, row.getCell(FIELD_OPERATION_COLUMN)).trim());
                 engineField.setParameter(getCellValue(i, PARAMETER_VLAUE_COLUMN, row.getCell(PARAMETER_VLAUE_COLUMN)));
                 engineField.setDescrib(getCellValue(i, FIELD_REMARK_COLUMN, row.getCell(FIELD_REMARK_COLUMN)));
                 rule.getConditionsList().get(rule.getConditionsList().indexOf(conditions)).getFieldList().add(engineField);
             }
        }
        rules.getRuleList().add(rule);

    }


    /**
     * 1、判断该行是否存在字段信息
     * 2、对必填项进行空值校验，抛出提示异常
     * @param row
     * @param r
     * @return
     */
    public boolean checkFieldInfoForRow(Row row, int r){

        List<Integer> emptyCellColum = new ArrayList<>();
        for (int i = FIELD_START_NUM; i <= FIELD_END_NUM; i++){
            if (getCellValue(r,i,row.getCell(i)).equalsIgnoreCase("")){
                emptyCellColum.add(i);
            }
        }
        if (emptyCellColum.size() >= 5){
            return false;
        }
        //未填写字段ID
        if (getCellValue(r,FIELD_ID_COLUMN,row.getCell(FIELD_ID_COLUMN)).equalsIgnoreCase("")){
            throw new RcsException("请填写第"+(r+1)+"行"+""+columnName.get(FIELD_ID_COLUMN)+"列的字段ID！");
        }
        //未填写字段key
        if (getCellValue(r,FIELD_KEY_COLUMN,row.getCell(FIELD_KEY_COLUMN)).equalsIgnoreCase("")){
            throw new RcsException("第"+(r+1)+"行"+""+columnName.get(FIELD_KEY_COLUMN)+"列的字段Key请勿随意修改！");
        }
        //未填写字段名
        if (getCellValue(r,FIELD_NAME_COLUMN,row.getCell(FIELD_NAME_COLUMN)).equalsIgnoreCase("")){
            throw new RcsException("第"+(r+1)+"行"+""+columnName.get(FIELD_NAME_COLUMN)+"列的字段名请勿随意修改！");
        }
        //未填写运算符
        if (getCellValue(r,FIELD_OPERATION_COLUMN,row.getCell(FIELD_OPERATION_COLUMN)).equalsIgnoreCase("")){
            throw new RcsException("请填写第"+(r+1)+"行"+""+columnName.get(FIELD_OPERATION_COLUMN)+"列的运算符！");
        }
        //未填写对比值
        if (getCellValue(r,PARAMETER_VLAUE_COLUMN,row.getCell(PARAMETER_VLAUE_COLUMN)).equalsIgnoreCase("")){
            throw new RcsException("请填写第"+(r+1)+"行"+""+columnName.get(PARAMETER_VLAUE_COLUMN)+"列的对比值");
        }
        //未填写字段备注
        if (getCellValue(r,FIELD_REMARK_COLUMN,row.getCell(FIELD_REMARK_COLUMN)).equalsIgnoreCase("")){
            throw new RcsException("请填写第"+(r+1)+"行"+""+columnName.get(FIELD_REMARK_COLUMN)+"列的字段备注");
        }
        return true;
    }

    /**
     * 1、判断该行是否存在条件信息
     * 2、对必填项进行空值校验，抛出提示异常
     * @param row
     * @param r
     * @return
     */
    public boolean checkConditionInfoForRow(Row row, int r){

        List<Integer> emptyCellColum = new ArrayList<>();
        for (int i = CONDITION_START_NUM; i <= CONDITION_END_NUM; i++){
            if(getCellValue(r,i,row.getCell(i)).equalsIgnoreCase("")){
                emptyCellColum.add(i);
            }
        }
        if (emptyCellColum.size() == 4){
            return false;
        }
        //未填写条件ID
        if (getCellValue(r,CONDITION_ID_COLUMN,row.getCell(CONDITION_ID_COLUMN)).equalsIgnoreCase("")){
            throw new RcsException("请填写第"+(r+1)+"行"+""+columnName.get(CONDITION_ID_COLUMN)+"列的条件ID！");
        }
        //未填写条件名称
        if (getCellValue(r,CONDITION_NAME_COLUMN,row.getCell(CONDITION_NAME_COLUMN)).equalsIgnoreCase("")){
            throw new RcsException("请填写第"+(r+1)+"行"+""+columnName.get(CONDITION_NAME_COLUMN)+"列的条件名称！");
        }
        //未填写字段逻辑关系
        if (getCellValue(r,FIELD_RELATIONSHIP_COLUMN,row.getCell(FIELD_RELATIONSHIP_COLUMN)).equalsIgnoreCase("")){
            throw new RcsException("请填写第"+(r+1)+"行"+""+columnName.get(FIELD_RELATIONSHIP_COLUMN)+"列的字段逻辑关系！");
        }

        return true;
    }

    /**
     * 若规则ID、规则名称、风险分数、激活状态、条件逻辑关系、条件ID、条件名称、条件描述、字段逻辑关系都为null时，
     *              则认定为该行不包含规则信息
     *        对必填选项进行空值校验，抛出提示异常
     * @param row
     * @param r
     * @param matchType
     * @return
     */
      public boolean checkRuleInfoForRow(Row row, int r, int matchType){

          List<Integer> emptyCellColumn = new ArrayList<>();
         for (int i = RULE_START_NUM; i <= RULE_END_NUM; i++){
             if (getCellValue(r, i, row.getCell(i)).equalsIgnoreCase("")){
                emptyCellColumn.add(i);
             }
         }
         //作为空行处理
         if (emptyCellColumn.size() >= 5){
             return false;
         }

         //未填写规则名称列
         if (emptyCellColumn.contains(RULE_NAME_COLUMN)){
             throw new RcsException("请填写第"+(r+1)+"行"+""+columnName.get(RULE_NAME_COLUMN)+"列的规则名称！");
         }
         //未填写风险分数（风险等级）列
         if (emptyCellColumn.contains(RISK_LEVEL_COLUMN)){
            if(matchType == 0){
                throw new RcsException("请填写第"+(r+1)+"行"+""+columnName.get(RISK_LEVEL_COLUMN)+"列的风险分数！");
            }else if (matchType == 1){
                throw new RcsException("请填写第"+(r+1)+"行"+""+columnName.get(RISK_LEVEL_COLUMN)+"列的风险等级，选择无风险或低风险或高风险！");
            }
         }
         //未填写激活状态
          if (emptyCellColumn.contains(ACTIVE_STATUS_COLUMN)){
              throw new RcsException("请填写第"+(r+1)+"行"+""+columnName.get(ACTIVE_STATUS_COLUMN)+"列的激活状态,选择已激活或未激活！");
          }
          // 未填写条件逻辑关系
          if (emptyCellColumn.contains(CONDITION_RELATIONSHIP_COLUMN)){
              throw new RcsException("请填写第"+(r+1)+"行"+""+columnName.get(CONDITION_RELATIONSHIP_COLUMN)+"列的条件逻辑关系,选择条件ID进行表达式匹配！");
          }
          return true;
      }
    /**
     * 对表达式语法进行校验
     * @param relationShip
     * @return
     */
    public boolean checkRelationShipIsTrue(String relationShip){

        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        try{
            Object result = engine.eval(relationShip);
        }catch (ScriptException e){
            throw new RcsException("表达式语法错误，请检查后重试！");
        }
        return true;
    }

    /**
     * 解析规则集Excel中的规则集信息，并且返回匹配模式MatchType 0：权重匹配模式 1：最坏匹配模式
     * @param rules
     * @param sheet
     * @return
     */
    public int getRulesInfoFromExcel(EngineRules rules, Sheet sheet){

        int matchType = 0;

        Row rulesNameRow = sheet.getRow(0);
        Cell rulesNameCell = rulesNameRow.getCell(1);
        if (checkRulesInfoCellIsNull(0,1,rulesNameCell)){
            rules.setName(getCellValue(0,1,rulesNameCell));
        }

        Row businessTypeRow = sheet.getRow(1);
        Cell businessTypeCell = businessTypeRow.getCell(1);
        if (checkRulesInfoCellIsNull(1,1,businessTypeCell)){
            rules.setBusinessName(getCellValue(1,1,businessTypeCell));
        }

        Row sceneRow = sheet.getRow(2);
        Cell sceneCell = sceneRow.getCell(1);
        if (checkRulesInfoCellIsNull(2,1,sceneCell)){
            rules.setSceneName(getCellValue(2,1,sceneCell));
        }

        Row matchTypeRow = sheet.getRow(3);
        Cell matchTypeCell = matchTypeRow.getCell(1);
        if (checkRulesInfoCellIsNull(3,1,matchTypeCell)){
            String matchTypeString = getCellValue(3,1,matchTypeCell);
            if (matchTypeString.equalsIgnoreCase("权重匹配模式")){
                matchType = 0;
                Row thresholdRow = sheet.getRow(4);
                if (thresholdRow == null){
                    throw new RcsException("请勿删除风险阈值或修改匹配模式!");
                }
                Cell thresholdCell = thresholdRow.getCell(1);
                if (checkRulesInfoCellIsNull(4,1,thresholdCell)){
                     String thresholdValue = getCellValue(4,1,thresholdCell);
                     String[] thresholdArr = thresholdValue.split("-");
                    int thresholdMin = 0;
                    int thresholdMax = 0;
                     try {
                         thresholdMin = Integer.valueOf(thresholdArr[0]);
                         thresholdMax = Integer.valueOf(thresholdArr[1]);
                     }catch(Exception e){
                         throw new RcsException("第"+(4+1)+"行"+"B列单元格阈值范围填写错误！参照样例：10-100");
                     }
                     rules.setThresholdMin(String.valueOf(thresholdMin));
                     rules.setThresholdMax(String.valueOf(thresholdMax));
                }
            }else if (matchTypeString.equalsIgnoreCase("最坏匹配模式")){
                matchType = 1;
            }else{
                throw new RcsException("第4行B列匹配模式填写错误，请选择权重匹配模式或最坏匹配模式！");
            }
            rules.setMatchType(matchType);
        }

        return matchType;

    }

    public boolean checkRulesInfoCellIsNull(int r, int c, Cell cell){
        if (cell == null){
            throw new RcsException("第"+(r+1)+"行"+columnName.get(c)+"列单元格缺失必要数据，请检查后重试！", StatusCode.FAIL_PARSE_ERRO.getCode());
        }
        return true;
    }



    /**
     * 获取单元格数据
     * @param row
     * @param column
     * @param cell
     * @return
     */
    public String getCellValue(int row, int column, Cell cell){

        String value = "";
        if (cell == null){
            return "";
        }
        try {
            switch (cell.getCellType()) {
                case 0:         // 整数型、日期类型、浮点型
                    value = cell.getNumericCellValue() + "";
                    if (DateUtil.isCellDateFormatted(cell)) {    //日期
                        Date date = cell.getDateCellValue();
                        if (date != null) {
                            value = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                        } else {
                            value = "";
                        }
                    } else {
                        //若是1.0自动格式化整型1，double类型不变
                        value = new DecimalFormat("#.#########").format(cell.getNumericCellValue());
                    }
                    break;
                case 1:     //字符串
                    value = cell.getStringCellValue();
                    break;
                case 2:     //公式
                    value = cell.getCellFormula();
                    break;
                case 3:    //空值
                    value = "";
                    break;
                case 4:   //布尔值
                    value = cell.getBooleanCellValue() ? 1 + "" : 0 + "";
                    break;
                case 5:  //故障
                    throw new RcsException("非法字符");
                default: //未知
                    value = cell.getStringCellValue();
                    break;
            }
        }catch(RcsException e){
            e.printStackTrace();
            throw new RcsException("第"+(row+1)+"行"+"第"+(column+1)+"列数据格式错误，请检查后重试!", StatusCode.FAIL_PARSE_ERRO.getCode());
        }

        return value;
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


}
