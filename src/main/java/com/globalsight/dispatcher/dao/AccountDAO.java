/**
 *  Copyright 2013 Welocalize, Inc. 
 *  
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  
 *  You may obtain a copy of the License at 
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  
 */
package com.globalsight.dispatcher.dao;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.globalsight.dispatcher.util.PasswordUtils;
import org.apache.log4j.Logger;

import com.globalsight.dispatcher.bo.Account;
import com.globalsight.dispatcher.bo.Accounts;
import com.globalsight.dispatcher.bo.AppConstants;

/**
 * Dispatcher Account DAO.
 * 
 * @author Joey
 * 
 */
public class AccountDAO implements AppConstants
{
    protected static final Logger logger = Logger.getLogger(AccountDAO.class);
    private static final String fileName = "Account.xml";
    private static String filePath;
    private Accounts accounts;

    public AccountDAO()
    {
    }

    private static String getFilePath()
    {
        if (filePath == null)
        {
            filePath = CommonDAO.getDataFolderPath() + fileName;
        }

        return filePath;
    }

    private void saveAccounts(Accounts p_accounts) throws JAXBException
    {
        // create JAXB context and instantiate marshaller
        JAXBContext context = JAXBContext.newInstance(Accounts.class);
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        // Write to File
        m.marshal(p_accounts, new File(getFilePath()));
    }

    public void saveAccount(Account p_account) throws JAXBException
    {
        if (p_account.getId() < 0)
        {
            if (accounts == null) {
                getAllAccounts();
            }
            p_account.setId(accounts.getAndIncrement());
        }
        getAllAccounts().add(p_account);
        saveAccounts(accounts);
    }

    public void updateAccount(Account p_account) throws JAXBException
    {
        for (Account account : getAllAccounts())
        {
            if (account.getId() == p_account.getId())
            {
                accounts.getAccounts().remove(account);
                accounts.getAccounts().add(p_account);
                saveAccounts(accounts);
                break;
            }
        }
    }

    public void saveOrUpdateAccount(Account p_account) throws JAXBException
    {
        if (p_account.getId() < 0)
        {
            saveAccount(p_account);
        }
        else
        {
            updateAccount(p_account);
        }
    }

    public void deleteAccount(long p_accountID) throws JAXBException
    {
        for (Account account : getAllAccounts())
        {
            if (account.getId() == p_accountID)
            {
                logger.info("Remove Account: " + p_accountID);
                accounts.getAccounts().remove(account);
                saveAccounts(accounts);
                break;
            }
        }
    }
    
    public Set<Account> getAllAccounts()
    {
        if (accounts == null)
        {
            try
            {
                File file = new File(getFilePath());
                if (!file.exists())
                {
                    file.createNewFile();
                    accounts = new Accounts();
                }
                else if (file.length() == 0)
                {
                    accounts = new Accounts();
                }
                else {
                    JAXBContext context = JAXBContext.newInstance(Accounts.class);
                    Unmarshaller um = context.createUnmarshaller();
                    accounts = (Accounts) um.unmarshal(new FileReader(getFilePath()));
                }
            }
            catch (JAXBException jaxbEx)
            {
                String message = "getAllAccounts --> JAXBException:" + getFilePath();
                logger.error(message, jaxbEx);
                return null;
            }
            catch (IOException ioEx)
            {
                String message = "getAllAccounts --> JAXBException:" + getFilePath();
                logger.error(message, ioEx);
                return null;
            }
        }
        boolean foundAdmin = false;
        for(Account account: accounts.getAccounts()) {
            if (account.getAccountName().equalsIgnoreCase(GADMIN_USER_NAME)) {
                foundAdmin = true;
                break;
            }
        }
        if(!foundAdmin) {
            Account p_account = new Account();
            p_account.setId(accounts.getAndIncrement());
            p_account.setAccountName(GADMIN_USER_NAME);
            p_account.setFullName(GADMIN_FULL_NAME);
            p_account.setPassword(PasswordUtils.md5(GADMIN_PASSWORD));
            p_account.setType(USER_TYPE_ADMIN);
            p_account.setEmail(GADMIN_EMAIL);
            p_account.setDescription(GADMIN_DESCRIPTION);
            accounts.getAccounts().add(p_account);
            try {
                saveAccounts(accounts);
            } catch (JAXBException jaxbEx) {
                String message = "getAllAccounts --> JAXBException:" + getFilePath();
                logger.error(message, jaxbEx);
            }
        }
        return accounts.getAccounts();
    }

    public Account getAccount(long p_accountID)
    {
        for (Account account : getAllAccounts())
        {
            if (account.getId() == p_accountID)
                return account;
        }
        return null;
    }
    
    public Account getAccountByAccountName(String p_name)
    {
        if(null == p_name || 0 == p_name.length()) return null;
        for (Account account : getAllAccounts())
        {
            if (account.getAccountName().equalsIgnoreCase(p_name))
                return account;
        }
        return null;
    }

    public String getAccountName(String p_accountID) {
        for (Account account : getAllAccounts())
        {
            if (String.valueOf(account.getId()).equals(p_accountID)) {
                return account.getAccountName();
            }
        }
        return null;

    }
    
}
