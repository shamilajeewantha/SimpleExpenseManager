package lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class DataBaseHelper extends SQLiteOpenHelper {
    public DataBaseHelper(@Nullable Context context) {
        super(context, "database3.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CreateTableAccounts="CREATE TABLE account_table (ACCOUNT_NO TEXT primary key,BANK_NAME TEXT,ACCOUNT_HOLDER_NAME TEXT,BALANCE REAL )";
        String CreateTablesTransactions="CREATE TABLE transaction_table (DATE TEXT,ACCOUNT_NUM TEXT,EXPENSE_TYPE TEXT,AMOUNT REAL)";

        db.execSQL(CreateTableAccounts);
        db.execSQL(CreateTablesTransactions);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean addToAccountsTable(Account account){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("ACCOUNT_NO",account.getAccountNo());
        cv.put("BANK_NAME",account.getBankName());
        cv.put("ACCOUNT_HOLDER_NAME",account.getAccountHolderName());
        cv.put("BALANCE",account.getBalance());

        db.insert("account_table",null, cv);
        return true;
    }

    public boolean addToTransactionsTable(Transaction transaction){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        SimpleDateFormat format=new SimpleDateFormat("dd-MM-yyyy");

        cv.put("DATE",format.format(transaction.getDate()));
        cv.put("ACCOUNT_NUM",transaction.getAccountNo());
        cv.put("EXPENSE_TYPE",transaction.getExpenseType().name());
        cv.put("AMOUNT",transaction.getAmount());

        db.insert("transaction_table",null, cv);
        return true;
    }

    public List<Account> getAccountsFromTable() {
        List<Account> returnList =new ArrayList<>();

        String queryString="SELECT * FROM account_table";
        SQLiteDatabase db =this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString,null);

        if(cursor.moveToFirst()){
            do{
                String accNum=cursor.getString(0);
                String bankName=cursor.getString(1);
                String accHolderName=cursor.getString(2);
                double balance=cursor.getDouble(3);

                Account account = new Account(accNum,bankName,accHolderName,balance);
                returnList.add(account);

            } while(cursor.moveToNext());
        }
        else{

        }
        cursor.close();
        db.close();

        return returnList;
    }

    public List<String> getAccountNumsFromTable() {
        List<String> returnList =new ArrayList<>();

        String queryString="SELECT * FROM account_table";
        SQLiteDatabase db =this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString,null);

        if(cursor.moveToFirst()){
            do{
                String accNum=cursor.getString(0);
                returnList.add(accNum);

            } while(cursor.moveToNext());
        }
        else{

        }
        cursor.close();
        db.close();

        return returnList;
    }


    public boolean removeAccFromTable(String accountNo){

        SQLiteDatabase db = this.getWritableDatabase();
        String queryString ="DELETE FROM account_table WHERE ACCOUNT_NO = "+ "'" +accountNo+ "'";

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            return true;
        }
        else{
            return false;
        }

    }


    public Account getAccountFromTable(String accountNo){
        SQLiteDatabase db =this.getReadableDatabase();
        String queryString = "SELECT * FROM account_table WHERE ACCOUNT_NO = "+ "'" +accountNo+ "'";
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            String accNum=cursor.getString(0);
            String bankName=cursor.getString(1);
            String accHolderName=cursor.getString(2);
            double balance=cursor.getDouble(3);
            cursor.close();
            db.close();

            Account account = new Account(accNum,bankName,accHolderName,balance);
            return account;
        }
        else{
            cursor.close();
            db.close();
            return null;

        }
    }







    public List<Transaction> getAlltrans() throws ParseException {
        List<Transaction> returnList =new ArrayList<>();

        String queryString="SELECT * FROM transaction_table";
        SQLiteDatabase db =this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString,null);

        if(cursor.moveToFirst()){
            do{
                String StrDate=cursor.getString(0);
                SimpleDateFormat format=new SimpleDateFormat("dd-MM-yyyy");
                Date OriginalDate=format.parse(StrDate);


                String accNUMB=cursor.getString(1);

                String strEXPENSE_TYPE=cursor.getString(2);
                ExpenseType expenseType = ExpenseType.valueOf(strEXPENSE_TYPE);

                double amount=cursor.getDouble(3);

                Transaction transaction = new Transaction(OriginalDate,accNUMB,expenseType,amount);
                returnList.add(transaction);

            } while(cursor.moveToNext());
        }
        else{

        }
        cursor.close();
        db.close();

        return returnList;
    }



    public List<Transaction> getlimtrans(int limit) throws ParseException {
        List<Transaction> returnList =new ArrayList<>();

        String queryString="SELECT * FROM transaction_table";
        SQLiteDatabase db =this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString,null);

        if(cursor.moveToFirst()){
            int index=0;
            do {
                if(index<limit){
                String StrDate = cursor.getString(0);
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                Date OriginalDate = format.parse(StrDate);


                String accNUMB = cursor.getString(1);

                String strEXPENSE_TYPE = cursor.getString(2);
                ExpenseType expenseType = ExpenseType.valueOf(strEXPENSE_TYPE);

                double amount = cursor.getDouble(3);

                Transaction transaction = new Transaction(OriginalDate, accNUMB, expenseType, amount);
                returnList.add(transaction);
                index += 1;
                }

            } while(cursor.moveToNext());
        }
        else{

        }
        cursor.close();
        db.close();

        return returnList;
    }




}
