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

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.globalsight.dispatcher.bo.AppConstants;
import com.globalsight.dispatcher.bo.Domain;
import com.globalsight.dispatcher.bo.Domains;

/**
 * Dispatcher Domain DAO.
 * 
 * @author 
 * 
 */

@Repository
public class DomainDAO implements AppConstants
{
    protected static final Logger logger = Logger.getLogger(DomainDAO.class);
    private static final String fileName = "Domain.xml";
    private static String filePath;
    private Domains domains;

    public DomainDAO()
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

    private void saveDomains(Domains p_domains) throws JAXBException
    {
        // create JAXB context and instantiate marshaller
        JAXBContext context = JAXBContext.newInstance(Domains.class);
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        // Write to File
        m.marshal(p_domains, new File(getFilePath()));
    }

    public void saveDomain(Domain P_domain) throws JAXBException
    {
        if (P_domain.getId() < 0)
        {
            if (domains == null) {
            	getAllDomains();
            }
            P_domain.setId(domains.getAndIncrement());
        }
        
        getAllDomains().add(P_domain);
        saveDomains(domains);
    }

    public void updateDomain(Domain p_domain) throws JAXBException
    {
        for (Domain domain : getAllDomains())
        {
            if (domain.getId() == p_domain.getId())
            {
                domains.getDomains().remove(domain);
                domains.getDomains().add(p_domain);
                saveDomains(domains);
                break;
            }
        }
    }

    public void saveOrUpdateDomain(Domain p_domain) throws JAXBException
    {
        if (p_domain.getId() < 0)
        {
            saveDomain(p_domain);
        }
        else
        {
            updateDomain(p_domain);
        }
    }

    public void deleteDomain(long p_domainID) throws JAXBException
    {
        for (Domain domain : getAllDomains())
        {
            if (domain.getId() == p_domainID)
            {
                logger.info("Remove Domain: " + p_domainID);
                domains.getDomains().remove(domain);
                saveDomains(domains);
                break;
            }
        }
    }
    
    public Set<Domain> getAllDomains()
    {
        if (domains == null)
        {
            try
            {
                File file = new File(getFilePath());
                if (!file.exists())
                {
                    file.createNewFile();
                    domains = new Domains();
                }
                else if (file.length() == 0)
                {
                    domains = new Domains();
                }
                else {
                    JAXBContext context = JAXBContext.newInstance(Domains.class);
                    Unmarshaller um = context.createUnmarshaller();
                    domains = (Domains) um.unmarshal(new FileReader(getFilePath()));
                }
            }
            catch (JAXBException jaxbEx)
            {
                String message = "getAllDomains --> JAXBException:" + getFilePath();
                logger.error(message, jaxbEx);
                return null;
            }
            catch (IOException ioEx)
            {
                String message = "getAllDomains --> JAXBException:" + getFilePath();
                logger.error(message, ioEx);
                return null;
            }
        }
        return domains.getDomains();
    }

    public Boolean getDomainObj(String domainName)
    {
    	if(domainName == ""){
    		return null;
    	}
        for (Domain domain : getAllDomains())
        {
            if (domainName.equalsIgnoreCase(domain.getDomainName())){
            	return false;
            }
        }
        return true;
    }

    public String getDomainName(String p_domainID) {
        for (Domain domain : getAllDomains())
        {
            if (String.valueOf(domain.getId()).equals(p_domainID)) {
                return domain.getDomainName();
            }
        }
        return null;

    }
    
}
