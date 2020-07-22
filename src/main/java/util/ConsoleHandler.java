package util;

import model.FruitData;
import service.FileService;
import service.FruitService;
import service.impl.FileServiceImpl;
import service.impl.FruitServiceImpl;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import static service.impl.FileServiceImpl.DATE;
import static service.impl.FileServiceImpl.TYPE;
import static service.impl.FileServiceImpl.FRUIT_NAME;
import static service.impl.FileServiceImpl.QUANTITY;

public class ConsoleHandler {

    public static final String FILE_EXTENSION = ".csv";
    private FileService fileService = new FileServiceImpl();
    private Validator validator = new Validator();
    private FruitService fruitService = new FruitServiceImpl();

    public void run() {
        try {
            System.out.println("Enter the input file name ");
            String inputFileName = validator.getDataFromConsole() + FILE_EXTENSION;
            System.out.println("Enter the output file name ");
            String outputFileName = validator.getDataFromConsole() + FILE_EXTENSION;
            if (new File(outputFileName).exists()) {
                new File(outputFileName).delete();
            }
            if (!new File(inputFileName).exists()) {
                fileService.writeToFile(inputFileName, TYPE, FRUIT_NAME, QUANTITY, DATE);
            }
            if (!new File(outputFileName).exists()) {
                fileService.writeToFile(outputFileName, FRUIT_NAME, QUANTITY);
            }
            fileService.writeToFile(inputFileName, getDataFromUser());
            List<FruitData> fruitData = fileService.readFile(inputFileName);
            Map<String, Integer> fruitAndQuantity = fruitService.getFruitAndQuantity(fruitData);
            for (Map.Entry<String, Integer> entry : fruitAndQuantity.entrySet()) {
                fileService.writeToFile(outputFileName, entry.getKey(), entry.getValue());
            }
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }

    private String[] getDataFromUser() {
        String type = "";
        String fruitName = "";
        String quantity = "";
        String date = "";
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Press key: \n" +
                    "s - supply, means you are receiving fruits from suppliers \n" +
                    "b - buy, means someone bought a fruit \n" +
                    "r - return, means someone who have bought fruits now returns it to you");
            type = validator.checkLetter(validator.getDataFromConsole());
            System.out.println("Enter a fruit name:");
            fruitName = validator.checkString(validator.getDataFromConsole());
            System.out.println("Enter a quantity:");
            quantity = validator.checkQuantity(validator.getDataFromConsole());
            System.out.println("Enter a date");
            date = reader.readLine();
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
        return new String[]{type, fruitName, quantity, date};
    }
}
