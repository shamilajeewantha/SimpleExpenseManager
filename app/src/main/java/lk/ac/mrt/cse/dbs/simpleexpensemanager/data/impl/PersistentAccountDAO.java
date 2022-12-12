package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.DataBaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO implements AccountDAO {

    private DataBaseHelper dbhelper;

    public PersistentAccountDAO(DataBaseHelper dbhelper) {
        this.dbhelper = dbhelper;
    }

    @Override
    public List<String> getAccountNumbersList() {
        List<String> result=dbhelper.getAccountNumsFromTable();
        return result;
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> result =dbhelper.getAccountsFromTable();
        return result;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Account result = dbhelper.getAccountFromTable(accountNo);
        if(result==null){
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
        return result;
    }

    @Override
    public void addAccount(Account account) {
        dbhelper.addToAccountsTable(account);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        if (dbhelper.removeAccFromTable(accountNo)){

        }
        else{
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }

    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Account account =dbhelper.getAccountFromTable(accountNo);
        dbhelper.removeAccFromTable(accountNo);
        switch (expenseType) {
            case EXPENSE:
                account.setBalance(account.getBalance() - amount);
                break;
            case INCOME:
                account.setBalance(account.getBalance() + amount);
                break;
        }
        dbhelper.addToAccountsTable(account);
    }
}
