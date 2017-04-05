package scheduler;

import java.io.*;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.HSSFRegionUtil;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;

import time.Time;
import time.TimeException;

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
    
    private HSSFWorkbook workbook;
    private HSSFCellStyle timeStyle;
    private HSSFCellStyle rightBorderStyle;
    private HSSFCellStyle botBorderStyle;
    private HSSFCellStyle borderStyle;
    private HSSFCellStyle dayStyle;
    private HSSFCellStyle[] classStyles;
    private short[] colors = new short[]{new HSSFColor.BRIGHT_GREEN().getIndex(), new HSSFColor.BLUE_GREY().getIndex(), new HSSFColor.GREY_25_PERCENT().getIndex(),
                                         new HSSFColor.LIGHT_BLUE().getIndex(), new HSSFColor.LIGHT_ORANGE().getIndex(), new HSSFColor.LIGHT_YELLOW().getIndex(),
                                         new HSSFColor.LIGHT_TURQUOISE().getIndex(), new HSSFColor.TAN().getIndex(), new HSSFColor.TEAL().getIndex(),
                                         new HSSFColor.SKY_BLUE().getIndex(), new HSSFColor.CORAL().getIndex(), new HSSFColor.GOLD().getIndex()};
    
    /**
     * Constructor for ExcelShedule
     * @param schedules the schedules to create
     * @throws TimeException
     */
    public ExcelSchedule(LinkedList<Schedule> schedules) throws TimeException {
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
        
        classStyles = new HSSFCellStyle[12];
        for (int s = 0; s < classStyles.length; s++) {
            classStyles[s] = workbook.createCellStyle();
            classStyles[s].setWrapText(true);
            classStyles[s].setAlignment(CellStyle.ALIGN_CENTER);
            classStyles[s].setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            classStyles[s].setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            classStyles[s].setFillForegroundColor(colors[s]);
        }
        
        int i = 1;
        for (Schedule s : schedules) {
            createScheduleSheet(s, "Schedule " + (i++));
        }
    }
    
    /**
     * Returns the completed workbook
     */
    public HSSFWorkbook getWorkbook() throws IOException {
        return workbook;
    }
    
    /**
     * Creates an excel sheet from a schedule
     * 
     * @param schedule the schedule to be created
     * @param sheetName the name of the sheet
     * @throws TimeException 
     */
    private void createScheduleSheet(Schedule schedule, String sheetName) throws TimeException {
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
       
        
        for (int i = 0; i < schedule.size(); i++) {
            //System.out.println(schedule.get(i));
            addClass(sheet, early, schedule.get(i), i);
        }   
    }
    
    /**
     * Adds a class to an excel sheet
     * 
     * @param sheet the sheet to have the class added
     * @param sheetStartTime the earliest hour of the sheet
     * @param c the class to be added
     * @param colorInd the color to fill
     */
    private void addClass(HSSFSheet sheet, int sheetStartTime, VTCourse c, int colorInd) {
        
        for (int i = 0; i < c.getTimes().size(); i++) {
            Time t = c.getTimes().get(i);
            String[] days = c.getDays().get(i);
            if (t != null) {
                int startTime = Time.timeNumber(t.getStart())/5;
                int endTime = Time.timeNumber(t.getEnd())/5;
                for (String d : days) {
                    HSSFCell cell = sheet.getRow(startTime - sheetStartTime*12 + 1).createCell(Schedule.DAYS.indexOf(d) + 1);
                    cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                    cell.setCellFormula(classString(c, i));
                    cell.setCellStyle(classStyles[colorInd]);
                    CellRangeAddress cellRange = new CellRangeAddress(startTime - sheetStartTime*12 + 1, endTime - sheetStartTime*12, 
                            Schedule.DAYS.indexOf(d) + 1, Schedule.DAYS.indexOf(d) + 1);
                    setMergeBorder(sheet, cellRange, true, true, true, true);
                    sheet.addMergedRegion(cellRange);
                }
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
    private String classString(VTCourse c, int timeIndex) {
        String s = "";
        String br = "&CHAR(10)&";
        s += "\"" + c.getSubject() + " " + c.getNum() + "\"" + br +
             "\"" + c.getCRN() + "\"" + br;   
        
        Time t = c.getTimes().get(timeIndex);
        String location = c.getLocations().get(timeIndex);
        
        s += "\"" + t.getStart() + " - " + t.getEnd() + "\"" + br + 
             "\"" + location + "\"" + br +
             "\"" + c.getProf() + "\""; 
        
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
    private void setMergeBorder(HSSFSheet sheet, CellRangeAddress cellRange, boolean top, boolean bottom, boolean left, boolean right) {
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
