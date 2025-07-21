package Service;

import DAO.AccountDAO;
import Model.Account;


public class AccountService {
    private AccountDAO accDAO;

    /**
     * Creation of accountDAO which will create and use DAO's.
     */
    public AccountService(){
        accDAO = new AccountDAO();
    }
    
    /**
     * In case an account is provided in the future. MAY DELETE THIS.
     * @param account 
     */
    public AccountService(AccountDAO account) {
        this.accDAO = account;
    }

    /**
     * @param user Username of the account
     * @return Return an account
     */
    public Account getAccountByUsername(String user) {
        return accDAO.getAccountByUsername(user);
    }

    /**
     * Account will be provided. 
     * Username can't be blank, Password >= 4 Characters, Account username is unique.
     * @param acc a account object
     * @return an account
     */
    public Account addAccount (Account acc) {
        // Checks for restrictions on account creation
        if (acc.getUsername() == "") {
            return null;
        } else if (acc.getPassword().length() < 4) {
            return null; 
        } else if (accDAO.getAccountByUsername(acc.getUsername()) != null) {
            return null;
        }

        return accDAO.insertAccount(acc);
    }

    /**
     * Helps login to account by checking DAO if it exists.
     * Check username and password match.
     * @param uncheckedAcc Account Object, Doesn't contain account_id. Unchecked means 
     * did not check if password matches yet.
     * @return Account with account_id.
     */
    public Account login(Account uncheckedAcc) {
        Account fullAcc = getAccountByUsername(uncheckedAcc.getUsername());
        if (fullAcc == null) {
            // No account by that username exists
            return null;
        }
        if (!uncheckedAcc.getPassword().equals(fullAcc.getPassword())) {
            return null;
        }

        // Should not be null and passwords equal
        return fullAcc;
    }
}
