package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {

    private AccountDAO accountDAO = new AccountDAO();

    // Registers a new account
    public Account register(Account account) {
        if (account.getUsername().isBlank() || account.getPassword().length() < 4) {
            throw new IllegalArgumentException("Invalid account details");
        }
        if (accountDAO.findByUsername(account.getUsername()) != null) {
            throw new IllegalArgumentException("Username already exists");
        }
        return accountDAO.save(account);
    }

    // Logs in an existing account
    public Account login(String username, String password) {
        Account account = accountDAO.findByUsername(username);
        if (account == null || !account.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        return account;
    }

    // Checks if a user exists by ID
    public boolean userExists(int userId) {
        return accountDAO.existsById(userId);
    }
}
