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
package com.globalsight.dispatcher.bo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;

import com.globalsight.everest.projecthandler.MachineTranslationExtentInfo;

/**
 * Copy From GlobalSight.
 */
//@XmlType(propOrder = { "id", "mtProfileName", "mtEngine", "mtConfidenceScore", "description"})
public class MachineTranslationProfile
{

//    private static final long serialVersionUID = 3321290565544942964L;

    private long id;
//    private Timestamp timestamp;
    private String mtProfileName;
    private String mtEngine;
    private String description;
    private String url;
    private Integer port;
    private String username;
    private String password;
    private String category;
    private String accountinfo;
//    private Long companyid;
    private long mtConfidenceScore;
    private Set<com.globalsight.everest.projecthandler.MachineTranslationExtentInfo> exInfo = new HashSet<com.globalsight.everest.projecthandler.MachineTranslationExtentInfo>();
    private String jsonInfo;
    private String subscriptionKey;
//    private boolean showInEditor;
//  private boolean active;
  private HashMap paramHM;
  	private long domain;
    public MachineTranslationProfile()
    {
        this.id = -1;
        this.mtProfileName = "";
        this.mtEngine = "MS_Translator";
        this.description = "";
        this.url = "";
        this.username = "";
        this.category = "";
        this.accountinfo = "";
        this.mtConfidenceScore = 100;
        this.subscriptionKey = "";
    }

    public MachineTranslationProfile(long id)
    {
        this.id = id;
    }

    public MachineTranslationProfile(long id, String mtProfileName,
            String mtEngin, String description, long mtConfidenceScore,
            String url, Integer port, String username, String password,
            String category, String accountinfo, Long companyid, String subscriptionKey)
    {
        this.id = id;
        this.mtProfileName = mtProfileName;
        this.mtEngine = mtEngin;
        this.description = description;
        this.mtConfidenceScore = mtConfidenceScore;
        this.url = url;
        this.port = port;
        this.username = username;
        this.password = password;
        this.category = category;
        this.accountinfo = accountinfo;
//        this.companyid = companyid;
        this.subscriptionKey = subscriptionKey;
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

    public String getJsonInfo()
    {
        return jsonInfo;
    }

    public void setJsonInfo(String jsonInfo)
    {
        this.jsonInfo = jsonInfo;
    }

//    public boolean isShowInEditor()
//    {
//        return showInEditor;
//    }
//
//    public void setShowInEditor(boolean showInEditor)
//    {
//        this.showInEditor = showInEditor;
//    }

//    public boolean isActive()
//    {
//        return active;
//    }
//
//    public void setActive(boolean active)
//    {
//        this.active = active;
//    }

    public long getMtConfidenceScore()
    {
        return mtConfidenceScore;
    }

    public void setMtConfidenceScore(long mtConfidenceScore)
    {
        this.mtConfidenceScore = mtConfidenceScore;
    }

    public HashMap getParamHM()
    {
        paramHM = new HashMap();
        EngineEnum ee = EngineEnum.getEngine(getMtEngine());
        String[] s = ee.getInfo();
        String[] me = this.toArray();
        for (int i = 0; i < s.length; i++)
        {
            paramHM.put(s[i], me[i]);
        }
        return paramHM;
    }

    public String[] toArray()
    {
        return new String[]
        { String.valueOf(getId()), getUrl(), getPort().toString(),
                getUsername(),
                getPassword(),
                getCategory(), getAccountinfo(),getSubscriptionKey() };
    }    

//    public Timestamp getTimestamp()
//    {
//        return timestamp;
//    }
//
//    public void setTimestamp(Timestamp timestamp)
//    {
//        this.timestamp = timestamp;
//    }

    public String getMtProfileName()
    {
        return this.mtProfileName;
    }

    public void setMtProfileName(String mtProfileName)
    {
        this.mtProfileName = mtProfileName;
    }

    public String getMtEngine()
    {
        return this.mtEngine;
    }

    public void setMtEngine(String mtEngine)
    {
        this.mtEngine = mtEngine;
    }

    public String getDescription()
    {
        return this.description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getUrl()
    {
        return this.url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public Integer getPort()
    {
        if (null == port)
        {
            port = new Integer(80);
        }
        return this.port;
    }

    public void setPort(Integer port)
    {
        this.port = port;
    }

    public String getUsername()
    {
        return this.username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return this.password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getCategory()
    {
        return this.category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public String getAccountinfo()
    {
        return this.accountinfo;
    }

    public void setAccountinfo(String accountinfo)
    {
        this.accountinfo = accountinfo;
    }

//    public Long getCompanyid()
//    {
//        return this.companyid;
//    }
//
//    public void setCompanyid(Long companyid)
//    {
//        this.companyid = companyid;
//    }

    public Set<MachineTranslationExtentInfo> getExInfo()
    {
        return exInfo;
    }

    public void setExInfo(Set<MachineTranslationExtentInfo> exInfo)
    {
        this.exInfo = exInfo;
    }
    
    public String getExInfoVal()
    {
        if (this.getExInfo() == null || this.getExInfo().size() == 0)
        {
            return "";
        }
        Iterator i = this.getExInfo().iterator();
        StringBuffer node = new StringBuffer();
        while (i.hasNext())
        {
            MachineTranslationExtentInfo mInfo = (MachineTranslationExtentInfo) i
                    .next();
            node.append(mInfo.getSelfInfo()).append(",");
        }
        return node.toString();
    }
    
    public void setSubscriptionKey(String subscriptionKey)
    {
        this.subscriptionKey = subscriptionKey;
    }

    public String getSubscriptionKey()
    {
        return this.subscriptionKey;
    }

	public long getDomain() {
		return domain;
	}

	public void setDomain(long domain) {
		this.domain = domain;
	}

}
