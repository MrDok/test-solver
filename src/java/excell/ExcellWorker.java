package excell;

import excell.exceptions.FileWriteException;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

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

    public Set<Question> getData(String directory){
        File[] files = getFiles(directory, HTML_EXTENSION);

        Set<Question> result = new HashSet<>();
        HtmlHelper htmlHelper = new HtmlHelper();

        for (File file : files){
            try{
                result.addAll(htmlHelper.getQuestions(file));
            }catch (IOException e){
                System.out.println("Eror working with file: " + file.getName());
            }
        }

        return result;
    }

    public void createFile(Set<Question> data, String fileName) throws FileWriteException{
        HSSFWorkbook book = new HSSFWorkbook();
        HSSFSheet sheet = book.createSheet();

        int i = -1;
        Row row;
        for(Question question : data){
            row = createRow(sheet, ++i);

            row.getCell(0).setCellValue(question.getQuestion() + "\n\n" + question.getCode());
            row.getCell(1).setCellValue(question.getAnswer());
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
            Set<Question> result = worker.getData("./files");
            System.out.println("Unique count of questions: "+ result.size());
            worker.createFile(result, "output.xls");
        }catch (FileWriteException e){
            System.out.println("Couldn't write to output.xls");
        }catch (Exception e){
            System.out.println("Inner exception");
        }
    }
}
