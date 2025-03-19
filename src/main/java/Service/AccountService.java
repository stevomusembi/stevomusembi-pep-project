package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    public AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    public Account registerAccount(Account account){
        if(account.getUsername() == null || account.getUsername().length() == 0){
            return null;
            
        }
        if(account.getPassword().length() < 4){
            return null;
            

        }
        if(accountDAO.findByusername(account.getUsername()) != null){
            return null;
           
        }
      
        return accountDAO.registerAccount(account);
    }

    public Account login(Account account){
        return accountDAO.login(account);
    }
    
}
