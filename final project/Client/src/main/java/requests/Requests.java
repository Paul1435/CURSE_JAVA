package requests;

import java.io.*;
import java.math.BigDecimal;
import java.net.Socket;

public class Requests {
    public static <T extends Request> void sendObject(T objectToSend, Socket src) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(src.getOutputStream());
        objectOutputStream.writeObject(objectToSend);
        objectOutputStream.flush();
    }

    public static String readFromServer(Socket socket) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        return reader.readLine();
    }

    public static String requestForGetMoney(BigDecimal decimal, String description, Socket socket) {
        var request = new MoneyRequest(decimal, description);
        String answer = null;
        try {
            sendObject(request, socket);
            answer = readFromServer(socket);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            answer = "Простите, сервер не отвечает";
        }
        return answer;
    }

    public static String incomeInfoRequest(IncomeInfoRequest incomeRequest, Socket socket) {
        String answer = null;
        try {
            sendObject(incomeRequest, socket);
            answer = readFromServer(socket);
        } catch (IOException e) {
            answer = "Простите, сервер не отвечает";
        }
        return answer;
    }

    public static void incomeSetRequest(SetIncomeRequest income, Socket socket) {
        try {
            sendObject(income, socket);
        } catch (IOException e) {
            System.out.println("Сообщение не отправлено");
        }
    }

    public static String getListMoneyRequest(Socket socket) {
        try {
            sendObject(new GetListMoneyRequest(), socket);
            return readFromServer(socket);
        } catch (IOException e) {
            System.out.println("не удалось отправить запрос");
        }
        return null;
    }

    public static String expenseRequest(ExpenseRequest expenseRequest, Socket socket) {
        String answer = null;
        try {
            sendObject(expenseRequest, socket);
            answer = readFromServer(socket);
        } catch (IOException e) {
            answer = "Простите, сервер не отвечает";
        }
        return answer;
    }
}
