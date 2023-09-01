import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        AccountDao accountDao = new AccountDao();
        AccountServiceImpl service = new AccountServiceImpl();
        Scanner scanner = new Scanner(System.in);
        boolean checkError = true;
        System.out.print("Введите своё имя: ");
        String name = scanner.nextLine();
        System.out.print("Введите свою фамилию: ");
        String lastName = scanner.nextLine();
        if (name.length() > 1 && lastName.length() > 1) {
            while (checkError) {
                service.singUp(name, lastName);
                break;
            }
        } else {
            System.out.println(Color.ANSI_RED + "Неподходящее имя или фамилия! Попробуйте снова." + Color.ANSI_RESET);
            checkError=false;
        } if (checkError) {
            System.out.print("Введите номер карты: ");
            String cardNumber = scanner.nextLine();
            System.out.print("Введите пин код карты: ");
            String pinCode = scanner.nextLine();
            service.singIn(cardNumber, pinCode);
        }
    }
}