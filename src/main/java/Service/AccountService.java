package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public Account registerAccount(String username, String password) {
        if (username == null || username.equals("")|| username.isBlank() || password == null || password.length() < 4 || accountDAO.isUsernameTaken(username)) {
            return null;
        }
        Account acc = new Account(username, password); 
        return accountDAO.createAccount(acc);
    }

    public Account login(String username, String password) {
        return accountDAO.getAccountByUsernameAndPassword(username, password);
    }
}

