package excell;

import excell.exceptions.FileWriteException;
import excell.exceptions.WrongFormatDataException;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by dokuchaev on 06.12.16.
 */
public class ExcellWorker{

    public static final String HTML_EXTENSION = ".html";

    /**
     * Получаем список всех файлов с рсширением .html
     * @param fileName
     * @param extension
     * @return
     */
    public File[] getFiles(String fileName, final String extension){
        File directory = new File(fileName);

        if (directory.isDirectory()){
            FilenameFilter filter = new FilenameFilter(){
                @Override
                public boolean accept(File dir, String name){

                    return name.endsWith(extension);
                }
            };

            File[] result = directory.listFiles(filter);

            if (result != null && result.length > 0){
                return result;
            }else {
                return null;
            }
        }else{
            if (directory.isFile()){
                String path = directory.getPath();

                if (path.endsWith(extension));
                return new File[]{new File(fileName)};
            }
        }

        return null;
    }

    public Map<String, String> getData(String directory){
        File[] files = getFiles(directory, HTML_EXTENSION);

        Map<String, String> result = new HashMap<>();
        HtmlHelper htmlHelper = new HtmlHelper();

        for (File file : files){
            try{
                result.putAll(htmlHelper.getQuestions(file));
            }catch (IOException e){
                System.out.println("Eror working with file: " + file.getName());
            }
        }

        return result;
    }
    /**
     * Возвращает значение ячейки в текстовом представлении
     * @param cell
     * @return
     */
    public String getText(Cell cell) throws WrongFormatDataException{
        if (cell != null){
            String str;

            switch (cell.getCellType()){
                case Cell.CELL_TYPE_STRING:
                    str = cell.getRichStringCellValue().getString();
                    break;

                case Cell.CELL_TYPE_NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)){
                        str = cell.getDateCellValue().toString();
                    }else{
                        str = Double.toString(cell.getNumericCellValue());
                    }
                    break;

                case Cell.CELL_TYPE_BOOLEAN:
                    str = Boolean.toString(cell.getBooleanCellValue());
                    break;

                case Cell.CELL_TYPE_FORMULA:
                    try{
                        str =  Double.toString(cell.getNumericCellValue());
                    }catch(IllegalStateException e){
                        throw new WrongFormatDataException("Неверный формат данных в ячейке: " + (cell.getColumnIndex() + 1));
                    }
                    break;

                case Cell.CELL_TYPE_ERROR:
                    throw new WrongFormatDataException("Неверный формат данных в ячейке: " + (cell.getColumnIndex() + 1));

                default:
                    str = null;
            }

            return str.trim();
        }

        return null;
    }

    public void createFile(Map<String, String> data, String fileName) throws FileWriteException{
        HSSFWorkbook book = new HSSFWorkbook();
        HSSFSheet sheet = book.createSheet();

        int i = -1;
        Row row;
        for(Map.Entry<String, String> entry : data.entrySet()){
            row = createRow(sheet, ++i);

            row.getCell(0).setCellValue(entry.getKey());
            row.getCell(1).setCellValue(entry.getValue());
        }

        try{
            book.write(new FileOutputStream(fileName));
        }catch (IOException e){
            throw new FileWriteException();
        }
    }

    public Row createRow(HSSFSheet sheet, int index){
        Row row = sheet.createRow(index);
        row.setHeight((short) 5000);
        row.createCell(0);
        row.createCell(1);

        return row;
    }

    public static void main(String[] args){
        try{
            ExcellWorker worker = new ExcellWorker();
            Map<String, String> result = worker.getData("./files");
            System.out.println("Unique count of questions: "+ result.size());
            worker.createFile(result, "output.xls");
        }catch (FileWriteException e){
            System.out.println("Couldn't write to output.xls");
        }catch (Exception e){
            System.out.println("Inner exception");
        }
    }
}
