/**
 *  Copyright 2014 Welocalize, Inc. 
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
package com.globalsight.dispatcher.bo;

import javax.xml.bind.annotation.XmlAttribute;

//@XmlType(propOrder = { "id", "accountName", "accountFullName", "password", "accountType", "e-mail", "description" , "ssoUser"})
public class Account
{
    private long id;
    private String accountName;
    private String fullName;
    private String password;
    private String type;
    private String email;
    private String description;
    private String ssoUser;
    
    public Account()
    {
        this(-1);
    }

    public Account(long id)
    {
        this.id = id;
    }

    public Account(long id,
                   String p_accountName,
                   String p_fullName,
                   String p_password,
                   String p_type,
                   String p_email,
                   String p_description,
                   String p_ssoUser)
    {
        this.id = id;
        this.accountName = p_accountName;
        this.fullName = p_fullName;
        this.password = p_password;
        this.type = p_type;
        this.email = p_email;
        this.description = p_description;
        this.ssoUser = p_ssoUser;
    }
    
    public Account(Account p_account)
    {
        this(p_account.getId(),
             p_account.getAccountName(),
             p_account.getFullName(),
             p_account.getPassword(),
             p_account.getType(),
             p_account.getEmail(),
             p_account.getDescription(),
             p_account.getSsoUser());
    }
    
    public long getId()
    {
        return this.id;
    }

    @XmlAttribute(name="ID")
    public void setId(long id)
    {
        this.id = id;
    }

    public String getAccountName()
    {
        return accountName;
    }
    public void setAccountName(String accountName)
    {
        this.accountName = accountName;
    }

    public String getPassword()
    {
        return password;
    }
    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getFullName()
    {
        return fullName;
    }
    public void setFullName(String accountFullName)
    {
        this.fullName = accountFullName;
    }

    public String getType()
    {
        return type;
    }
    public void setType(String type)
    {
        this.type = type;
    }


    public String getEmail()
    {
        return email;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getDescription()
    {
        return description;
    }
    public void setDescription(String description)
    {
        this.description = (description == null) ? "" : description.trim();
    }
    
		public String getSsoUser () {
			return ssoUser;
		}

		
		public void setSsoUser (String ssoUser) {
			this.ssoUser = ssoUser;
		}

		public String toJSON()
    {
        StringBuilder json = new StringBuilder();
        json.append("{")
            .append("\"id\":").append(id).append(", ")
            .append("\"accountName\":\"").append(accountName).append("\", ")
            .append("\"fullName\":\"").append(fullName).append("\", ")
            .append("\"password\":\"").append(password).append("\", ")
            .append("\"type\":\"").append(type).append("\", ")
            .append("\"email\":\"").append(email).append("\", ")
            .append("\"description\":\"").append((description == null)?"":description.trim()).append("\"")
            .append("\"SSOUser\":\"").append(ssoUser).append("\"")
            .append("}");

        return json.toString();
    }
}
