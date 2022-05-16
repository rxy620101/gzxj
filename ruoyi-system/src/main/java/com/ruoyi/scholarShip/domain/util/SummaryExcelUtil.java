package com.ruoyi.scholarShip.domain.util;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excels;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelHandlerAdapter;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.scholarShip.domain.SummaryInfo;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

public class SummaryExcelUtil <T> {
    private static final Logger log = LoggerFactory.getLogger(SummaryExcelUtil.class);

    public static final String FORMULA_REGEX_STR = "=|-|\\+|@";

    public static final String[] FORMULA_STR = { "=", "-", "+", "@" };

    /**
     * Excel sheet最大行数，默认65536
     */
    public static final int sheetSize = 65536;

    /**
     * 工作表名称
     */
    private String sheetName;

    /**
     * 导出类型（EXPORT:导出数据；IMPORT：导入模板）
     */
    private Excel.Type type;

    /**
     * 工作薄对象
     */
    private Workbook wb;

    /**
     * 工作表对象
     */
    private Sheet sheet;

    /**
     * 样式列表
     */
    private Map<String, CellStyle> styles;

    /**
     * 导入导出数据列表
     */
    private List<SummaryInfo> list;

    /**
     * 注解列表
     */
    private List<Object[]> fields;

    /**
     * 当前行号
     */
    private int rownum;

    /**
     * 标题
     */
    private String title;

    /**
     * 最大高度
     */
    private short maxHeight;

    /**
     * 统计列表
     */
    private Map<Integer, Double> statistics = new HashMap<Integer, Double>();

    /**
     * 数字格式
     */
    private static final DecimalFormat DOUBLE_FORMAT = new DecimalFormat("######0.00");

    /**
     * 实体对象
     */
    public Class<T> clazz;
    public SummaryExcelUtil(Class<T> clazz)
    {
        this.clazz = clazz;
    }

    public void init(List<SummaryInfo> list, String sheetName, String title, Excel.Type type)
    {
        if (list == null)
        {
            list = new ArrayList<>();
        }
        this.list = list;
        this.sheetName = sheetName;
        this.type = type;
        this.title = title;
        createExcelField();
        createWorkbook();
        createTitle();
    }
    /**
     * 对list数据源将其里面的数据导入到excel表单
     *
     * @param response 返回数据
     * @param list 导出数据集合
     * @param sheetName 工作表的名称
     * @param title 标题
     * @return 结果
     */
    public void exportExcel(HttpServletResponse response, List<SummaryInfo> list, String sheetName, String title)
    {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        this.init(list, sheetName, title, Excel.Type.EXPORT);
        exportExcel(response);
    }
    /**
     * 对list数据源将其里面的数据导入到excel表单
     *
     * @return 结果
     */
    public void exportExcel(HttpServletResponse response)
    {
        try
        {
            writeSheet();
            wb.write(response.getOutputStream());
        }
        catch (Exception e)
        {
            log.error("导出Excel异常{}", e.getMessage());
        }
        finally
        {
            IOUtils.closeQuietly(wb);
        }
    }
    /**
     * 创建写入数据到Sheet
     */
    public void writeSheet()
    {
        // 取出一共有多少个sheet.
        int sheetNo = Math.max(1, (int) Math.ceil(list.size() * 1.0 / sheetSize));
        for (int index = 0; index < sheetNo; index++)
        {
            createSheet(sheetNo, index);
            // 产生一行， 有标题行
            Row row = sheet.createRow(1);
            int column = 0;
            // 写入各个字段的列头名称
            for (Object[] os : fields)
            {
                Excel excel = (Excel) os[1];
             //标题
                this.createCell(excel, row, column++);
            }
            //设置自动列宽
            for (int i = 0; i < row.getLastCellNum(); i++) {
              //学院名称
                if(i==0){
                    sheet.setColumnWidth(0,35*256);
                }
                //等级名称
                else if(i==1){
                    sheet.setColumnWidth(1,18*256);
                }
                else{
                    int length = row.getCell(i).getStringCellValue().getBytes().length;
                    sheet.setColumnWidth((short)i,(short)(length*256));
                }
            }
            if (Excel.Type.EXPORT.equals(type))
            {
                fillExcelData(index, row);
                addStatisticsRow();
            }
        }
    }
    /**
     * 创建统计行
     */
    public void addStatisticsRow()
    {
        if (statistics.size() > 0)
        {
            Row row = sheet.createRow(sheet.getLastRowNum() + 1);
            Set<Integer> keys = statistics.keySet();
            Cell cell = row.createCell(0);
            cell.setCellStyle(styles.get("total"));
            cell.setCellValue("合计");

            for (Integer key : keys)
            {
                cell = row.createCell(key);
                cell.setCellStyle(styles.get("total"));
                cell.setCellValue(DOUBLE_FORMAT.format(statistics.get(key)));
            }
            statistics.clear();
        }
    }

    /**
     * 创建单元格
     */
    public Cell createCell(Excel attr, Row row, int column)
    {
        // 创建列
        Cell cell = row.createCell(column);
        // 写入列信息
        cell.setCellValue(attr.name());
        cell.setCellStyle(styles.get("header"));
        return cell;
    }
    /**
     * 填充excel数据
     *
     * @param index 序号
     * @param row 单元格行
     */
    public void fillExcelData(int index, Row row)
    {
        int startNo = index * sheetSize;
        int endNo = Math.min(startNo + sheetSize, list.size());
        //创建内容
        List<String> tempDate = new ArrayList<>();
        //初始化起始行和结束行
        int startRow = 2;
        int endRow = 2;
        //从第一行开始
        int temNum = 2;
        //当前学院
        String lastCollege = "";
        //用来记录是否是第一次循环
        boolean flag = true;
        for (int i = startNo; i < endNo; i++) {
            //利用相同学院名称合并单元格(合并行)
           String currentCollege=list.get(i).getCollegeName();
            //int firstRow 第几行开始, int lastRow 第几行结束, int firstCol 第几个单元格开始, int lastCol 第几个单元格结束
            if (flag) {
                //如果是第一次循环，将lastDate初始化，以便后面的第一次判断
                lastCollege = currentCollege;
                flag = false;
            }
            if (i != 0 && lastCollege.equals(currentCollege)) {
                endRow++;
            } else {
                //两者不相同时，融合之前相同的行
                if (endRow > startRow) {
                    sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 0, 0));
                }
                startRow = temNum;
                endRow = startRow;
                lastCollege = currentCollege;
            }
            temNum++;
            if (i==list.size()-1&&startRow!=endRow) {
                //融合最后相同的行
                sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 0, 0));
            }
            row = sheet.createRow(i + 2);
            // 得到导出对象.
            T vo = (T) list.get(i);
            int column = 0;
            for (Object[] os : fields)
            {
                Field field = (Field) os[0];
                Excel excel = (Excel) os[1];
                //填充数据
                this.addCell(excel, row, vo, field, column++);
            }
        }
        }

    /**
     * 添加单元格
     */
    public Cell addCell(Excel attr, Row row, T vo, Field field, int column)
    {
        Cell cell = null;
        try
        {
            // 设置行高
            row.setHeight(maxHeight);
            // 根据Excel中设置情况决定是否导出,有些情况需要保持为空,希望用户填写这一列.
            if (attr.isExport())
            {
                // 创建cell
                cell = row.createCell(column);
                int align = attr.align().value();
                cell.setCellStyle(styles.get("data" + (align >= 1 && align <= 3 ? align : "")));
                // 用于读取对象中的属性
                Object value = getTargetValue(vo, field, attr);
               if (value instanceof BigDecimal && -1 != attr.scale())
                {
                    cell.setCellValue((((BigDecimal) value).setScale(attr.scale(), attr.roundingMode())).toString());
                }
               else{
                   cell.setCellValue(value.toString());
               }
                addStatisticsData(column, Convert.toStr(value), attr);
            }
        }
        catch (Exception e)
        {
            log.error("导出Excel失败{}", e);
        }
        return cell;
    }
    /**
     * 合计统计信息
     */
    private void addStatisticsData(Integer index, String text, Excel entity)
    {
        if (entity != null && entity.isStatistics())
        {
            Double temp = 0D;
            if (!statistics.containsKey(index))
            {
                statistics.put(index, temp);
            }
            try
            {
                temp = Double.valueOf(text);
            }
            catch (NumberFormatException e)
            {
            }
            statistics.put(index, statistics.get(index) + temp);
        }
    }
    /**
     * 获取bean中的属性值
     *
     * @param vo 实体对象
     * @param field 字段
     * @param excel 注解
     * @return 最终的属性值
     * @throws Exception
     */
    private Object getTargetValue(T vo, Field field, Excel excel) throws Exception
    {
        Object o = field.get(vo);
        if (StringUtils.isNotEmpty(excel.targetAttr()))
        {
            String target = excel.targetAttr();
            o = getValue(o, target);
        }
        return o;
    }
    /**
     * 以类的属性的get方法方法形式获取值
     *
     * @param o
     * @param name
     * @return value
     * @throws Exception
     */
    private Object getValue(Object o, String name) throws Exception
    {
        if (StringUtils.isNotNull(o) && StringUtils.isNotEmpty(name))
        {
            Class<?> clazz = o.getClass();
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            o = field.get(o);
        }
        return o;
    }

    /**
     * 得到所有定义字段
     */
    private void createExcelField()
    {
        this.fields = getFields();
        this.fields = this.fields.stream().sorted(Comparator.comparing(objects -> ((Excel) objects[1]).sort())).collect(Collectors.toList());
        this.maxHeight = getRowHeight();
    }

    /**
     * 获取字段注解信息
     */
    public List<Object[]> getFields()
    {
        List<Object[]> fields = new ArrayList<Object[]>();
        List<Field> tempFields = new ArrayList<>();
        tempFields.addAll(Arrays.asList(clazz.getSuperclass().getDeclaredFields()));
        tempFields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        for (Field field : tempFields)
        {
            // 单注解
            if (field.isAnnotationPresent(Excel.class))
            {
                Excel attr = field.getAnnotation(Excel.class);
                if (attr != null && (attr.type() == Excel.Type.ALL || attr.type() == type))
                {
                    field.setAccessible(true);
                    fields.add(new Object[] { field, attr });
                }
            }

            // 多注解
            if (field.isAnnotationPresent(Excels.class))
            {
                Excels attrs = field.getAnnotation(Excels.class);
                Excel[] excels = attrs.value();
                for (Excel attr : excels)
                {
                    if (attr != null && (attr.type() == Excel.Type.ALL || attr.type() == type))
                    {
                        field.setAccessible(true);
                        fields.add(new Object[] { field, attr });
                    }
                }
            }
        }
        return fields;
    }
    /**
     * 根据注解获取最大行高
     */
    public short getRowHeight()
    {
        double maxHeight = 0;
        for (Object[] os : this.fields)
        {
            Excel excel = (Excel) os[1];
            maxHeight = Math.max(maxHeight, excel.height());
        }
        return (short) (maxHeight * 20);
    }

    /**
     * 创建一个工作簿
     */
    public void createWorkbook()
    {
        this.wb = new SXSSFWorkbook(500);
        this.sheet = wb.createSheet();
        wb.setSheetName(0, sheetName);
        this.styles = createStyles(wb);
    }

    /**
     * 创建工作表
     *
     * @param sheetNo sheet数量
     * @param index 序号
     */
    public void createSheet(int sheetNo, int index)
    {
        // 设置工作表的名称.
        if (sheetNo > 1 && index > 0)
        {
            this.sheet = wb.createSheet();
//            this.createTitle();
            wb.setSheetName(index, sheetName + index);
        }
    }

    /**
     * 获取单元格值
     *
     * @param row 获取的行
     * @param column 获取单元格列号
     * @return 单元格值
     */
    public Object getCellValue(Row row, int column)
    {
        if (row == null)
        {
            return row;
        }
        Object val = "";
        try
        {
            Cell cell = row.getCell(column);
            if (StringUtils.isNotNull(cell))
            {
                if (cell.getCellType() == CellType.NUMERIC || cell.getCellType() == CellType.FORMULA)
                {
                    val = cell.getNumericCellValue();
                    if (DateUtil.isCellDateFormatted(cell))
                    {
                        val = DateUtil.getJavaDate((Double) val); // POI Excel 日期格式转换
                    }
                    else
                    {
                        if ((Double) val % 1 != 0)
                        {
                            val = new BigDecimal(val.toString());
                        }
                        else
                        {
                            val = new DecimalFormat("0").format(val);
                        }
                    }
                }
                else if (cell.getCellType() == CellType.STRING)
                {
                    val = cell.getStringCellValue();
                }
                else if (cell.getCellType() == CellType.BOOLEAN)
                {
                    val = cell.getBooleanCellValue();
                }
                else if (cell.getCellType() == CellType.ERROR)
                {
                    val = cell.getErrorCellValue();
                }

            }
        }
        catch (Exception e)
        {
            return val;
        }
        return val;
    }

    /**
     * 判断是否是空行
     *
     * @param row 判断的行
     * @return
     */
    private boolean isRowEmpty(Row row)
    {
        if (row == null)
        {
            return true;
        }
        for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++)
        {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK)
            {
                return false;
            }
        }
        return true;
    }
    /**
     * 创建excel第一行标题
     */
    public void createTitle()
    {
        if (StringUtils.isNotEmpty(title))
        {
            Row titleRow = sheet.createRow(rownum == 0 ? rownum++ : 0);
//            sheet.autoSizeColumn(1, true);
            titleRow.setHeightInPoints(30);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellStyle(styles.get("title"));
            titleCell.setCellValue(title);
            sheet.addMergedRegion(new CellRangeAddress(titleRow.getRowNum(), titleRow.getRowNum(), titleRow.getRowNum(),
                    this.fields.size()-1));
        }
    }
    /**
     * 创建表格样式
     *
     * @param wb 工作薄对象
     * @return 样式列表
     */
    private Map<String, CellStyle> createStyles(Workbook wb)
    {
        // 写入各条记录,每条记录对应excel表中的一行
        Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        Font titleFont = wb.createFont();
        titleFont.setFontName("Arial");
        titleFont.setFontHeightInPoints((short) 16);
        titleFont.setBold(true);
        style.setFont(titleFont);
        styles.put("title", style);

        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        Font dataFont = wb.createFont();
        dataFont.setFontName("Arial");
        dataFont.setFontHeightInPoints((short) 10);
        style.setFont(dataFont);
        styles.put("data", style);

        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font headerFont = wb.createFont();
        headerFont.setFontName("Arial");
        headerFont.setFontHeightInPoints((short) 10);
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(headerFont);
        styles.put("header", style);

        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        Font totalFont = wb.createFont();
        totalFont.setFontName("Arial");
        totalFont.setFontHeightInPoints((short) 10);
        style.setFont(totalFont);
        styles.put("total", style);

        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        style.setAlignment(HorizontalAlignment.LEFT);
        styles.put("data1", style);

        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        style.setAlignment(HorizontalAlignment.CENTER);
        styles.put("data2", style);

        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        style.setAlignment(HorizontalAlignment.RIGHT);
        styles.put("data3", style);

        return styles;
    }

}
