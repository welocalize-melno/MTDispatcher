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
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.globalsight.dispatcher.bo.MachineTranslationProfile;
import com.globalsight.dispatcher.bo.MachineTranslationProfiles;
import com.globalsight.dispatcher.util.StringUtils;

/**
 * Machine Translation Profile DAO
 * 
 * @author Joey
 * 
 */
@Repository
public class MTProfilesDAO
{
    protected static final Logger logger = Logger.getLogger(MTProfilesDAO.class);
    protected static final String fileName = "MachineTranslationProfiles.xml";
    protected static String filePath;
    protected MachineTranslationProfiles mtProfiles;
    
    public MTProfilesDAO()
    {
    }    

    public static String getFilePath()
    {
        if (filePath == null)
        {
            filePath = CommonDAO.getDataFolderPath() + fileName;
        }

        return filePath;
    }
    
    protected void saveMTProfiles(MachineTranslationProfiles p_mtProfiles) throws JAXBException
    {
        // create JAXB context and instantiate marshaller
        JAXBContext context = JAXBContext.newInstance(MachineTranslationProfiles.class);
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        // Write to File
        m.marshal(p_mtProfiles, new File(getFilePath()));
    }
    
    public void saveMTProfile(MachineTranslationProfile p_mtProfile) throws JAXBException
    {
        if(p_mtProfile.getId() < 0)
        {
            if (mtProfiles == null)
                getAllMTProfiles();
            p_mtProfile.setId(mtProfiles.getAndIncrement());
        } 
        
        getAllMTProfiles().add(p_mtProfile);        
        saveMTProfiles(mtProfiles);
    }
    
    public void updateMTProfile(MachineTranslationProfile p_mtProfile) throws JAXBException
    {
        for(MachineTranslationProfile mtProfile : getAllMTProfiles())
        {
            if(mtProfile.getId() == p_mtProfile.getId())
            {
                mtProfiles.getMtProfiles().remove(mtProfile);
                mtProfiles.getMtProfiles().add(p_mtProfile);
                saveMTProfiles(mtProfiles);
                break;
            }
        }  
    }
    
    public void saveOrUpdateMTProfile(MachineTranslationProfile p_mtProfile) throws JAXBException
    {
        if (p_mtProfile.getId() < 0)
        {
            saveMTProfile(p_mtProfile);
        }
        else
        {
            updateMTProfile(p_mtProfile);
        }
    }
    
    public void deleteMTProfile(long p_mtProfileID) throws JAXBException 
    {
        for(MachineTranslationProfile mtProfile : getAllMTProfiles())
        {
            if(mtProfile.getId() == p_mtProfileID)
            {
                logger.info("Remove MTProfile: " + getMTProfileString(mtProfile));
                mtProfiles.getMtProfiles().remove(mtProfile);
                saveMTProfiles(mtProfiles);
                break;
            }
        }  
    }
    
    public void deleteMTProfile(String[] p_mtProfileIDS) throws JAXBException 
    {
        if(p_mtProfileIDS == null || p_mtProfileIDS.length == 0)
        {
            return;
        }
        
        Set<Long> mtProfileIDSet = new HashSet<Long>();
        for(String id : p_mtProfileIDS)
        {
            mtProfileIDSet.add(Long.valueOf(id));
        }
        
        for(MachineTranslationProfile mtProfile : getAllMTProfiles())
        {
            if(mtProfileIDSet.contains(mtProfile.getId()))
            {
                logger.info("Remove MTProfile: " + getMTProfileString(mtProfile));
                mtProfiles.getMtProfiles().remove(mtProfile);
            }
        }  
        
        saveMTProfiles(mtProfiles);
    }
    
    public MachineTranslationProfile getMTProfile(long p_mtProfileID)
    {        
        for(MachineTranslationProfile mtProfile : getAllMTProfiles())
        {
            if(mtProfile.getId() == p_mtProfileID)
            {
                return mtProfile;
            }
        }  
        
        return null;
    }
    
    public MachineTranslationProfile getMTProfile(String p_mtProfileName)
    {        
        for(MachineTranslationProfile mtProfile : getAllMTProfiles())
        {
            if(mtProfile.getMtProfileName() == p_mtProfileName)
            {
                return mtProfile;
            }
        }  
        
        return null;
    }
    
    public Set<MachineTranslationProfile> getAllMTProfiles()
    {
        if (mtProfiles == null)
        {
            try
            {
                File file = new File(getFilePath());
                if (!file.exists())
                {
                    file.createNewFile();
                    mtProfiles = new MachineTranslationProfiles();
                    return mtProfiles.getMtProfiles(); 
                }
                else if (file.length() == 0)
                {
                    mtProfiles = new MachineTranslationProfiles();
                    return mtProfiles.getMtProfiles(); 
                }

                JAXBContext context = JAXBContext.newInstance(MachineTranslationProfiles.class);
                Unmarshaller um = context.createUnmarshaller();
                mtProfiles = (MachineTranslationProfiles) um.unmarshal(new FileReader(getFilePath()));
            }
            catch (JAXBException jaxbEx)
            {
                String message = "getAllMTProfiles --> JAXBException:" + getFilePath();
                logger.error(message, jaxbEx);
                return null;
            }
            catch (IOException ioEx)
            {
                String message = "getAllMTProfiles --> JAXBException:" + getFilePath();
                logger.error(message, ioEx);
            }
        }
        
        return mtProfiles.getMtProfiles();
    }
    
    // Check whether the MT Profile Name already exist.
    public boolean isMtProfileNameExisted(MachineTranslationProfile p_mtProfile)
    {
        for (MachineTranslationProfile mtProfile : getAllMTProfiles())
        {
            if (mtProfile.getId() == p_mtProfile.getId())
                continue;
            
            if (mtProfile.getMtProfileName().equals(p_mtProfile.getMtProfileName()))
            {
                return true;
            }
        }

        return false;
    }
    
    private String getMTProfileString(MachineTranslationProfile p_mtProfile)
    {
        StringBuffer msg = new StringBuffer();
        msg.append("[");
        msg.append("ID:").append(p_mtProfile.getId()).append(",")
           .append("mtProfileName:").append(p_mtProfile.getMtProfileName()).append(",")
           .append("mtEngine:").append(p_mtProfile.getMtEngine());
        msg.append("]");
        return msg.toString();
    }
    
    /*
	 * function to get check  created MTprofiles with empty domain
	 * Boolean 
	 */
    public boolean getEmptyDomainProfileCount(long profileId)
    {
     	for (MachineTranslationProfile mtProfile : getAllMTProfiles())
    	{
    		if (!StringUtils.isNull(mtProfile.getDomain()) && (1 == mtProfile.getDomain())){
    			//while updating ignore existing mtprofile id
    			if( profileId != -1 &&  profileId == mtProfile.getId()){//for update
    				return false;
    			}
    			else{
    			//while creating new profiles
    				return true;
    			}
    			
    		}
    	}
    	return false;
    }
}
