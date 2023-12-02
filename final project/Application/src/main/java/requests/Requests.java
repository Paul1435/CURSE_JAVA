package requests;

import org.server.containers.Income;
import org.server.dao.DaoExpense;
import org.server.dao.DaoIncome;
import org.server.dao.DaoMoneyRequest;
import org.server.exceptions.DaoException;

import java.sql.SQLException;

public class Requests {
    public static void doMoneyRequest(int userId, MoneyRequest moneyRequest) throws SQLException {
        DaoMoneyRequest.getInstance().insertMoneyRequest(userId, moneyRequest);
    }
    public static String getList(){
        return DaoMoneyRequest.getInstance().allRecords();
    }
    public static void addIncome(int userId, SetIncomeRequest income) {
        try {
            DaoIncome.getInstance().saveRecord(new Income(userId, income.getDecimal(), income.getDescription()));
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }

    public static String doPersonalIncomeRequest(int userId) {
        String result = null;
        try {
            result = DaoIncome.getInstance().getSumRecordsOfCandidate(userId).toString();
        } catch (SQLException e) {
            result = "не удалось выполнить запрос";
        }
        return result;
    }

    public static String doPersonalExpenseRequest(int userId) {
        String result = null;
        try {
            result = DaoExpense.getInstance().getSumRecordsOfCandidate(userId).toString();
        } catch (SQLException e) {
            result = "не удалось выполнить запрос";
        }
        return result;
    }
}
