import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class AccountServiceImpl implements AccountService{
    private final AccountDao accountDao = new AccountDao();
    private final Random random = new Random();
    private final Scanner scanner = new Scanner(System.in);
    private final Scanner scannerInt = new Scanner(System.in);
    private boolean checkError = true;

    @Override
    public void singUp(String name, String lastName) {
        ArrayList<UserAccount> users = new ArrayList<>();
        int num;
        int code;
        num = random.nextInt(10000000, 99999999);
        code = random.nextInt(1000, 9999);
        String cardNumber = Integer.toString(num);
        String cardPinCode = Integer.toString(code);
        System.out.println(Color.ANSI_GREEN + "Ваш номер карты: " + cardNumber + "\nВаш пин код карты: " + cardPinCode + Color.ANSI_RESET);
        users.add(new UserAccount("Husein", "Obama", "00000000", "0000", 0));
        users.add(new UserAccount(name, lastName, cardNumber, cardPinCode, 0));
        accountDao.setUserAccounts(users);
    }

    @Override
    public void singIn(String cardNumber, String pinCode) {
        do {
            for (UserAccount user : accountDao.getUserAccounts()) {
                if (cardNumber.equals(user.getCardNumber()) && pinCode.equals(user.getPinCode())) {
                    System.out.println(Color.ANSI_GREEN + "Что бы выбирать действия вводите в консоль их номера.");
                    System.out.println("1 Баланс." +
                            "\n2 Депозит." +
                            "\n3 Отправить деньги другу." +
                            "\n4 Снять деньги." +
                            "\n5 Пополнить баланс.");
                    System.out.print(Color.ANSI_WHITE + "Выберите дальнейшие действия: ");
                    String move = scanner.nextLine();
                    switch (move) {
                        case "1" -> balance(cardNumber, pinCode);
                        case "2" -> deposit(cardNumber, pinCode);
                        case "3" -> {
                            System.out.print("Введите номер карты друга: ");
                            String friendCode = scanner.nextLine();
                            sendToFriend(friendCode);
                        }
                        case "4" -> {
                            System.out.print("Напишите сумму которую хотите вывести кратную 100: ");
                            int amount = scannerInt.nextInt();
                            withdrawMoney(amount);
                        }
                        case "5" -> {
                            System.out.print("Напишите сумму на которую хотите пополнить баланс: ");
                            int amount = scannerInt.nextInt();
                            replenishBalance(amount);
                        }
                    }
                } else if (user.getCardNumber().equals(cardNumber) && !user.getPinCode().equals(pinCode)) {
                    System.out.println(Color.ANSI_RED + "Неверный пин код.");
                    checkError = false;
                }
            }
        } while (checkError);
    }


    @Override
    public void balance(String cardNumber, String pinCode) {
        for (UserAccount userAccount : accountDao.getUserAccounts()) {
            if (cardNumber.equals(userAccount.getCardNumber()) && pinCode.equals(userAccount.getPinCode())) {
                System.out.println(Color.ANSI_BLUE + "Ваш баланс: " + userAccount.getBalance() + Color.ANSI_RESET);
            }
        }
    }

    @Override
    public void deposit(String cardNumber, String pinCode) {
        System.out.print(Color.ANSI_WHITE + "Сумма депозита который хотите внести : ");
        int deposit = scannerInt.nextInt();
        System.out.print("Введите свой номер карты: ");
        String cardNumber1 = scanner.nextLine();
        System.out.print("Введите пин код своей карты: ");
        String pinCode1 = scanner.nextLine();
        for (UserAccount userAccount : accountDao.getUserAccounts()) {
            if (userAccount.getCardNumber().equals(cardNumber1) && userAccount.getPinCode().equals(pinCode1)) {
                userAccount.setBalance(userAccount.getBalance() + deposit);
                System.out.println(Color.ANSI_YELLOW + "Депозит успешно внесён на этот счёт.");
                System.out.println(Color.ANSI_BLUE + "Баланс на этом счету: " + userAccount.getBalance() + Color.ANSI_RESET);
            } else if (userAccount.getCardNumber().equals(cardNumber1) && !userAccount.getPinCode().equals(pinCode1)) {
                System.out.println(Color.ANSI_RED + "Неверный пин код.");
                checkError = false;
            }
        }
    }

    @Override
    public void sendToFriend(String friendCardNumber) {
        int myBalance = 0;
        int moneyForFriend = 0;
        System.out.print(Color.ANSI_WHITE + "Введите номер своей карты: ");
        String cardNumber = scanner.nextLine();
        System.out.print("Введите пин код карты: ");
        String cardPinCode = scanner.nextLine();
        for (UserAccount userAccount : accountDao.getUserAccounts()) {
            if (userAccount.getCardNumber().equals(cardNumber) && userAccount.getPinCode().equals(cardPinCode)) {
                System.out.print(Color.ANSI_WHITE + "Введите сумму которую хотите отправить другу: ");
                moneyForFriend = scannerInt.nextInt();
                if (userAccount.getBalance() >= moneyForFriend) {
                    myBalance = userAccount.getBalance();
                    userAccount.setBalance(userAccount.getBalance() - moneyForFriend);
                    System.out.println(Color.ANSI_BLUE + "Ваш баланс: " + userAccount.getBalance());
                } else if (userAccount.getBalance() < moneyForFriend) {
                    System.out.println(Color.ANSI_RED + "Недостаточно средств!!!");
                    checkError = false;
                }
                for (UserAccount userAccount1 : accountDao.getUserAccounts()) {
                    if (friendCardNumber.equals(userAccount1.getCardNumber()) && moneyForFriend <= myBalance) {
                        userAccount1.setBalance(userAccount1.getBalance() + moneyForFriend);
                        System.out.println(Color.ANSI_BLUE + "Баланс вашего друга: " + userAccount1.getBalance());
                    } else if (moneyForFriend > myBalance) {
                        System.out.println(Color.ANSI_RED + "Недостаточно средств!!!");
                        checkError = false;
                    }
                }
            }else if (userAccount.getCardNumber().equals(cardNumber) && !userAccount.getPinCode().equals(cardPinCode)) {
                System.out.println(Color.ANSI_RED + "Неверный пин код.");
                checkError = false;
            }
        }
    }

    @Override
    public void withdrawMoney(int amount) {
        System.out.print(Color.ANSI_WHITE + "Введите свой номер карты: ");
        String cardNumber = scanner.nextLine();
        System.out.print("Введите свой пин код карты: ");
        String cardPinCode = scanner.nextLine();
        for (UserAccount account : accountDao.getUserAccounts()) {
            if (account.getCardNumber().equals(cardNumber) && account.getPinCode().equals(cardPinCode)) {
                if (amount % 1000 == 0) {
                    int shtuk = amount / 1000;
                    System.out.println(Color.ANSI_CYAN + "Получить " + amount + " рублей по (" + shtuk + ") 1000 рублёвыми купюрами (нажмите на 1)");
                }
                if (amount % 500 == 0) {
                    int shtuk = amount / 500;
                    System.out.println("Получить " + amount + " рублей по (" + shtuk + ") 500 рублёвыми купюрами (нажмите на 2)");
                }
                if (amount % 200 == 0) {
                    int shtuk = amount / 200;
                    System.out.println("Получить " + amount + " рублей по (" + shtuk + ") 200 рублёвыми купюрами (нажмите на 3)");
                }
                if (amount % 100 == 0) {
                    int shtuk = amount / 100;
                    System.out.println("Получить " + amount + " рублей по (" + shtuk + ") 100 рублёвыми купюрами (нажмите на 4)");
                }
                if (amount % 50 == 0) {
                    int shtuk = amount / 50;
                    System.out.println("Получить " + amount + " рублей по (" + shtuk + ") 50 рублёвыми купюрами (нажмите на 5)");
                }
                if (amount % 10 == 0) {
                    int shtuk = amount / 10;
                    System.out.println("Получить " + amount + " рублей по (" + shtuk + ") 10 рублёвыми монетами (нажмите на 6)");
                }
                System.out.print(Color.ANSI_WHITE + "Введите номер действия: ");
                int move = scannerInt.nextInt();

                int balance = account.getBalance() - amount;
                if (move == 1 && account.getBalance() >= amount) {
                    account.setBalance(balance);
                    System.out.println(Color.ANSI_BLUE + "Ваш баланс: " + account.getBalance());
                    System.out.println(Color.ANSI_YELLOW + "Выведено: " + amount + " рублей по 1000 рублей (" + amount / 1000 + " штук).");
                } else if (move == 2 && account.getBalance() >= amount) {
                    account.setBalance(balance);
                    System.out.println(Color.ANSI_BLUE + "Ваш баланс: " + account.getBalance());
                    System.out.println(Color.ANSI_YELLOW + "Выведено: " + amount + " рублей по 500 рублей (" + amount / 500 + " штук).");
                } else if (move == 3 && account.getBalance() >= amount) {
                    account.setBalance(balance);
                    System.out.println(Color.ANSI_BLUE + "Ваш баланс: " + account.getBalance());
                    System.out.println(Color.ANSI_YELLOW + "Выведено: " + amount + " рублей по 200 рублей (" + amount / 200 + " штук).");
                } else if (move == 4 && account.getBalance() >= amount) {
                    account.setBalance(balance);
                    System.out.println(Color.ANSI_BLUE + "Ваш баланс: " + account.getBalance());
                    System.out.println(Color.ANSI_YELLOW + "Выведено: " + amount + " рублей по 100 рублей (" + amount / 100 + " штук).");
                } else if (move == 5 && account.getBalance() >= amount) {
                    account.setBalance(balance);
                    System.out.println(Color.ANSI_BLUE + "Ваш баланс: " + account.getBalance());
                    System.out.println(Color.ANSI_YELLOW + "Выведено: " + amount + " рублей по 50 рублей (" + amount / 50 + " штук).");
                } else if (move == 6 && account.getBalance() >= amount) {
                    account.setBalance(balance);
                    System.out.println(Color.ANSI_BLUE + "Ваш баланс: " + account.getBalance());
                    System.out.println(Color.ANSI_YELLOW + "Выведено: " + amount + " рублей по 10 рублей (" + amount / 10 + " штук).");
                }
            } else if (account.getCardNumber().equals(cardNumber) && !account.getPinCode().equals(cardPinCode)) {
                System.out.println(Color.ANSI_RED + "Неверный пин код.");
                checkError = false;
            }
        }
    }

    @Override
    public void replenishBalance(int amount) {
        System.out.print(Color.ANSI_WHITE + "Введите номер своей карты: ");
        String cardNumber = scanner.nextLine();
        System.out.print("Введите пин код своей карты: ");
        String pinCode = scanner.nextLine();
        for (UserAccount userAccount : accountDao.getUserAccounts()) {
            if (userAccount.getCardNumber().equals(cardNumber) && userAccount.getPinCode().equals(pinCode)) {
                userAccount.setBalance(userAccount.getBalance() + amount);
                System.out.println(Color.ANSI_YELLOW + "Баланс успешно пополнен.");
                System.out.println(Color.ANSI_BLUE + "Ваш баланс: " + userAccount.getBalance());
            } else if (userAccount.getCardNumber().equals(cardNumber) && !userAccount.getPinCode().equals(pinCode)) {
                System.out.println(Color.ANSI_RED + "Неверный пин код.");
                checkError = false;
            }
        }
    }
}
