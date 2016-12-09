package excell;

import excell.exceptions.FileWriteException;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import java.io.*;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by dokuchaev on 06.12.16.
 */
public class ExcellWorker {

    private static final String HTML_EXTENSION = ".html";

    /**
     * Получаем список всех файлов с рсширением .html
     *
     * @param fileName
     * @param extension
     * @return
     */
    private File[] getFiles(String fileName, final String extension) {
        File directory = new File(fileName);

        if (directory.isDirectory()) {

            File[] result = directory.listFiles((dir, name) -> name.endsWith(extension));

            if (result != null && result.length > 0) {
                return result;
            } else {
                return new File[0];
            }
        } else {
            if (directory.isFile()) {
                String path = directory.getPath();

                if (path.endsWith(extension)) return new File[]{new File(fileName)};
            }
        }

        return new File[0];
    }

    private Set<Question> getData(String directory) {
        return Arrays.stream(getFiles(directory, HTML_EXTENSION))
                .flatMap(file -> new HtmlHelper().getQuestions(file).stream())
                .collect(Collectors.toSet());
    }

    private void createFile(Set<Question> data, String fileName) throws FileWriteException {
        HSSFWorkbook book = new HSSFWorkbook();
        HSSFSheet sheet = book.createSheet();
        HSSFCellStyle style = book.createCellStyle();
        style.setWrapText(false);

        final int[] i = {0};

        data.forEach(question -> {
            Row row = sheet.createRow(i[0]++);
            row.setHeight((short) 5000);
            row.createCell(0);
            row.createCell(1);

            row.getCell(0).setCellValue("Вопрос " + i[0] + ": \n" + question.getQuestion() + "\n\n" + question.getCode());
            row.getCell(1).setCellValue(question.getAnswer());
        });

//        int i = -1;
//        Row row;

//        for (Question question : data) {
//            row = createRow(sheet, ++i);
//            final int[] j = {1};
//
//            row.getCell(0).setCellValue("Вопрос " + j[0]++ + ":  \n" + question.getQuestion() + "\n\n" + question.getCode());
//            row.getCell(1).setCellValue(question.getAnswer());
//        }

        try {
            book.write(new FileOutputStream(fileName));
        } catch (IOException e) {
            throw new FileWriteException();
        }
    }

    public static void main(String[] args) {
        try {
            ExcellWorker worker = new ExcellWorker();
            Set<Question> result = worker.getData("./files");
            System.out.println("Unique count of questions: " + result.size());
            worker.createFile(result, "output.xls");
            BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
            final int[] i = {1};
            result.forEach(question -> {
                try {
                    writer.write("Вопрос " + i[0]++ + ":  " + question.getQuestion() + "\n\n");
                    writer.write(question.getCode() + "\n");
                    writer.write(question.getAnswer() + "\n____________\n");
                } catch (IOException e) {
                    System.err.println("ERROR!!!");
                }
            });
            writer.flush();
        } catch (FileWriteException e) {
            System.out.println("Couldn't write to output.xls");
        } catch (Exception e) {
            System.out.println("Inner exception");
        }
    }

//    /**
//     //     * Возвращает значение ячейки в текстовом представлении
//     //     *
//     //     * @param cell
//     //     * @return
//     //     */
//    public String getText(Cell cell) throws WrongFormatDataException {
//        if (cell != null) {
//            String str;
//
//            switch (cell.getCellType()) {
//                case Cell.CELL_TYPE_STRING:
//                    str = cell.getRichStringCellValue().getString();
//                    break;
//
//                case Cell.CELL_TYPE_NUMERIC:
//                    if (DateUtil.isCellDateFormatted(cell)) {
//                        str = cell.getDateCellValue().toString();
//                    } else {
//                        str = Double.toString(cell.getNumericCellValue());
//                    }
//                    break;
//
//                case Cell.CELL_TYPE_BOOLEAN:
//                    str = Boolean.toString(cell.getBooleanCellValue());
//                    break;
//
//                case Cell.CELL_TYPE_FORMULA:
//                    try {
//                        str = Double.toString(cell.getNumericCellValue());
//                    } catch (IllegalStateException e) {
//                        throw new WrongFormatDataException("Неверный формат данных в ячейке: " + (cell.getColumnIndex() + 1));
//                    }
//                    break;
//
//                case Cell.CELL_TYPE_ERROR:
//                    throw new WrongFormatDataException("Неверный формат данных в ячейке: " + (cell.getColumnIndex() + 1));
//
//                default:
//                    str = null;
//            }
//
//            return str.trim();
//        }
//
//        return null;
//    }

    //    private Row createRow(HSSFSheet sheet, int index) {
//        Row row = sheet.createRow(index);
//        row.setHeight((short) 5000);
//        row.createCell(0);
//        row.createCell(1);
//
//        return row;
//    }
}
