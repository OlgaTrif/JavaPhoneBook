import java.io.*;
import java.util.*;


/*Реализуйте структуру телефонной книги с помощью HashMap.
Программа также должна учитывать, что во входной структуре будут повторяющиеся имена с разными телефонами,
их необходимо считать, как одного человека с разными телефонами.

Вывод должен быть отсортирован по убыванию числа телефонов.

Номер уникален, имя - нет.
*/

public class PhoneBook {
    private static final String FILE_NAME = "phoneBook.txt";
    private static final String COMMANDS = "Bыбор действия:\n(add)добавить данные\n(del)удалить данные" +
            "\n(num)найти номера по имени\n(name)найти имя\n(save)сохранить\n(exit)выход";
    private static final String SPACE = " ";
    private static HashMap<String, String> phoneBook = new HashMap<String, String>();

    //добавляет запись по заданным номеру телефона и фамилии
    private static void AddNewContact(String phone, String name) {
        phoneBook.put(phone, name);
    }

    //удаляет запись по номеру телефона
    private static void DelContact(String phone) {
        phoneBook.remove(phone);
    }

    //сохраняет БД в текстовом файле phoneBook.txt
    private static void SavePhoneBook() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(FILE_NAME)));
        //сортировка по алфавиту
        phoneBook.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(k -> {
                    try {
                        writer.write(k.getKey() + SPACE + k.getValue() + System.lineSeparator());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
        writer.close();
    }

    //загружает БД из текстового файла phoneBook.txt
    public static void LoadPhoneBook() throws IOException {
        File file = new File(FILE_NAME);
        if (file.exists()){
            BufferedReader reader = new BufferedReader(new FileReader(new File(FILE_NAME)));
            String act;
            while ((act = reader.readLine()) != null) {
                String[] dat = act.split(SPACE);
                phoneBook.put(dat[0], dat[1]);
            }
            reader.close();
        }
    }

    //выводит на экран все записи БД
    public static void PrintPhonebook(){
        System.out.println("Телефонный справочник: ");
        for(Map.Entry<String,String> k: phoneBook.entrySet()){
            System.out.println(k.getValue() + ": " + k.getKey());
        }
    }

    //производит поиск имени по номеру телефона заданому в качестве аргумента
    public static String FindName(String number){
        String result = phoneBook.get(number);
        if (result == null) return "Абонент с таким номером не найден";
        return result;
    }

    //производит поиск списка номеров по имени заданой в качестве аргумента
    public static String[] FindNumberPhone(String name){
        List <String> result = new ArrayList<String>();
        for (Map.Entry entry : phoneBook.entrySet()) {
            if (name.equalsIgnoreCase((String)entry.getValue())){
                result.add((String)entry.getKey());
            }
        }
        if (result.isEmpty()) result.add("Абонент с таким именем не найден");
        return result.toArray(new String[0]);
    }

    public static void main(String[] args) throws IOException {
        //переменная описывает вызываемое действие
        String act;
        //загрузка БД
        LoadPhoneBook();
        //вывод записей на екран
        PrintPhonebook();

        //вывод на екран описания возможных действий с указанием команд
        System.out.println(COMMANDS);

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        act = reader.readLine();
        while(!act.equals("exit")){
                //добавление записи
                if (act.equals("add")) {
                    System.out.println("Введите имя:");
                    String name = reader.readLine();
                    System.out.println("Введите телефон:");
                    String phone = reader.readLine();
                    AddNewContact(phone, name);
                } else {
                    //удаление записи
                    if (act.equals("del")) {
                        System.out.println("Введите телефон:");
                        String phone = reader.readLine();
                        DelContact(phone);
                    } else {
                        //поиск номеров по имени
                        if (act.equals("num")) {
                            System.out.println("Введите имя:");
                            String name = reader.readLine();
                            String[] numbers = FindNumberPhone(name);
                            for (String number : numbers) {
                                System.out.println(number);
                            }
                        } else {
                            //поиск имени по номеру
                            if (act.equals("name")) {
                                System.out.println("Введите номер:");
                                String number = reader.readLine();
                                System.out.println(FindName(number));
                            } else {
                                //сохранение БД в файл
                                if (act.equals("save")) {
                                    SavePhoneBook();
                                }
                            }
                        }
                    }
                }
            //запрос на следующее действие
            System.out.println(COMMANDS);
            act = reader.readLine();
        }
    }
}