package scheduler;

import java.io.*;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.HSSFRegionUtil;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;

/**
 * ExcelSchedule builds an Excel Workbook of Class Schedules
 * 
 * @author Phillip Ngo
 * @version 1.0
 */
public class ExcelSchedule {
    
    private static HSSFWorkbook workbook;
    private static HSSFCellStyle timeStyle;
    private static HSSFCellStyle rightBorderStyle;
    private static HSSFCellStyle botBorderStyle;
    private static HSSFCellStyle borderStyle;
    private static HSSFCellStyle dayStyle;
    private static HSSFCellStyle classStyle;
    
    /**
     * Constructor that takes an array of schedules and
     * sets up all excel sheets
     * 
     * @param schedules an array of schedules
     * @throws TimeException 
     * @throws IOException 
     */
    public static void outputFile(LinkedList<Schedule> schedules) throws TimeException, IOException {
        if (schedules == null || schedules.size() == 0) {
            throw new IllegalArgumentException();
        }
        
        workbook = new HSSFWorkbook();
        
        timeStyle = workbook.createCellStyle();
        timeStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        timeStyle.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        
        rightBorderStyle = workbook.createCellStyle();
        rightBorderStyle.setBorderRight(CellStyle.BORDER_THIN);
        
        botBorderStyle = workbook.createCellStyle();
        botBorderStyle.setBorderBottom(CellStyle.BORDER_THIN);
        
        borderStyle = workbook.createCellStyle();
        borderStyle.setBorderBottom(CellStyle.BORDER_THIN);
        borderStyle.setBorderRight(CellStyle.BORDER_THIN);
        
        dayStyle = workbook.createCellStyle();
        dayStyle.setAlignment(CellStyle.ALIGN_CENTER);
        dayStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        dayStyle.setBorderBottom(CellStyle.BORDER_THIN);
        dayStyle.setBorderTop(CellStyle.BORDER_THIN);
        dayStyle.setBorderLeft(CellStyle.BORDER_THIN);
        dayStyle.setBorderRight(CellStyle.BORDER_THIN);
        
        classStyle = workbook.createCellStyle();
        classStyle.setWrapText(true);
        classStyle.setAlignment(CellStyle.ALIGN_LEFT);
        classStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        classStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        classStyle.setFillForegroundColor(new HSSFColor.GREY_25_PERCENT().getIndex());
        
        int i = 1;
        for (Schedule s : schedules) {
            createScheduleSheet(s, "Schedule " + (i++));
        }
        
        FileOutputStream fileOut = new FileOutputStream("WebContent/excelsheets/schedules.xls");
        workbook.write(fileOut);
        fileOut.close();
    }
    
    /**
     * Creates an excel sheet from a schedule
     * 
     * @param schedule the schedule to be created
     * @param sheetName the name of the sheet
     * @throws TimeException 
     */
    private static void createScheduleSheet(Schedule schedule, String sheetName) throws TimeException {
        HSSFSheet sheet = workbook.createSheet(sheetName);
        HSSFRow rowhead = sheet.createRow(0);
        
        sheet.setColumnWidth(0, 5000);
        for (int i = 1; i < 6; i++) {
            sheet.setColumnWidth(i, 8500);
        }
        
        int early = schedule.earliestTime()/60;
        int late = schedule.lastestTime()/60;
        
        for (int time = 0; time < late - early + 1; time++) {
            CellRangeAddress cellRange = new CellRangeAddress(1 + 12*time, 12 + 12*time, 0, 0);
            sheet.addMergedRegion(cellRange);
            for (int rows = 1; rows < 12; rows++) {
                HSSFRow currRow = sheet.createRow(1 + 12*time + rows);
                currRow.setHeight((short) 140);
            }
            HSSFCell cell = sheet.createRow(1 + 12*time).createCell(0);
            cell.setCellValue(Time.timeString(early*60 + time*60));
            cell.setCellStyle(timeStyle);
            sheet.getRow(1 + 12*time).setHeight((short) 140); 
            setMergeBorder(sheet, cellRange, true, true, true, true);
        }
        
        for (int i = 1; i < 12 + 12*(late - early); i++) {
            sheet.getRow(i).createCell(5).setCellStyle(rightBorderStyle);
        }
        for (int i = 0; i < 5; i++) {
            sheet.getRow(12 + 12*(late - early)).createCell(i+1).setCellStyle(botBorderStyle);
        }
        sheet.getRow(12 + 12*(late - early)).createCell(5).setCellStyle(borderStyle);
        
        sheet.createRow(0).setHeight((short) 700);    
        rowhead.createCell(1).setCellValue("Monday");
        rowhead.getCell(1).setCellStyle(dayStyle);
        rowhead.createCell(2).setCellValue("Tuesday");
        rowhead.getCell(2).setCellStyle(dayStyle);
        rowhead.createCell(3).setCellValue("Wednesday");
        rowhead.getCell(3).setCellStyle(dayStyle);
        rowhead.createCell(4).setCellValue("Thursday");
        rowhead.getCell(4).setCellStyle(dayStyle);
        rowhead.createCell(5).setCellValue("Friday");
        rowhead.getCell(5).setCellStyle(dayStyle);
       
        
        for (VTCourse c : schedule) {
            //System.out.println(schedule.get(i));
            addClass(sheet, early, c);
        }   
    }
    
    /**
     * Adds a class to an excel sheet
     * 
     * @param sheet the sheet to have the class added
     * @param sheetStartTime the earliest hour of the sheet
     * @param c the class to be added
     */
    private static void addClass(HSSFSheet sheet, int sheetStartTime, VTCourse c) {
        if (c.getTimeSlot() == null) {
            return;
        }
        int startTime = Time.timeNumber(c.getTimeSlot().getStart())/5;
        int endTime = Time.timeNumber(c.getTimeSlot().getEnd())/5;
        String[] days = c.getDays();
        
        for (String d : days) {
            HSSFCell cell = sheet.getRow(startTime - sheetStartTime*12 + 1).createCell(Schedule.DAYS.indexOf(d) + 1);
            cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
            cell.setCellFormula(classString(c, false));
            cell.setCellStyle(classStyle);
            CellRangeAddress cellRange = new CellRangeAddress(startTime - sheetStartTime*12 + 1, endTime - sheetStartTime*12, 
                    Schedule.DAYS.indexOf(d) + 1, Schedule.DAYS.indexOf(d) + 1);
            setMergeBorder(sheet, cellRange, true, true, true, true);
            sheet.addMergedRegion(cellRange);
        }
        if (c.getAdditionalTime() != null && c.getAdditionalDays() != null && c.getAdditionalDays().length > 0) {
            days = c.getAdditionalDays();
            startTime = Time.timeNumber(c.getAdditionalTime().getStart())/5;
            endTime = Time.timeNumber(c.getAdditionalTime().getEnd())/5;
            for (String d : days) {
                HSSFCell cell = sheet.getRow(startTime - sheetStartTime*12 + 1).createCell(Schedule.DAYS.indexOf(d) + 1);
                cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                cell.setCellFormula(classString(c, true));
                cell.setCellStyle(classStyle);
                CellRangeAddress cellRange = new CellRangeAddress(startTime - sheetStartTime*12 + 1, endTime - sheetStartTime*12, 
                        Schedule.DAYS.indexOf(d) + 1, Schedule.DAYS.indexOf(d) + 1);
                setMergeBorder(sheet, cellRange, true, true, true, true);
                sheet.addMergedRegion(cellRange);
            }
        }
    }
    
    /**
     * The text to be put into a cell for a class
     * 
     * @param c the class 
     * @param additional if the class has an additional time
     * @return the text of the class
     */
    private static String classString(VTCourse c, boolean additional) {
        String s = "";
        String br = "&CHAR(10)&";
        s += "\"" + c.getSubject() + " " + c.getNum() + "\"" + br +
             "\"CRN: " + c.getCRN() + "\"" + br;   
        if (additional) {
            s += "\"" + c.getAdditionalTime().getStart() + " - " + c.getAdditionalTime().getEnd() + "\"" + br +
                 "\"" + c.getAdditionalLocation() + "\"" + br; 
        }
        else {
            s += "\"" + c.getTimeSlot().getStart() + " - " + c.getTimeSlot().getEnd() + "\"" + br +
                 "\"" + c.getLocation() + "\"" + br;
        }
        s += "\"Prof: " + c.getProf() + "\"";
        return s;
    }
    
    /**
     * Sets the border for a merged cell block
     * 
     * @param sheet the sheet the cell block is on
     * @param cellRange the cell range of the merged block
     * @param top true for a top border
     * @param bottom true for a bottom border
     * @param left true for a left border
     * @param right true for a right border
     */
    private static void setMergeBorder(HSSFSheet sheet, CellRangeAddress cellRange, boolean top, boolean bottom, boolean left, boolean right) {
        if (top) {
            HSSFRegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRange, sheet, workbook);
        }
        if (bottom) {
            HSSFRegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRange, sheet, workbook);
        }
        if (left) {
            HSSFRegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRange, sheet, workbook);
        }
        if (right) {
            HSSFRegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRange, sheet, workbook);
        }
    }
}
