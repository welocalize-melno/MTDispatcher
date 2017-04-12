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
import javax.xml.bind.annotation.XmlValue;

public class Domain implements Comparable<Domain>
{
    private long id;
    private String domainName;
    
    public Domain()
    {
        this(-1);
    }

    public Domain(long id)
    {
        this.id = id;
    }

    public Domain(long id,
                   String p_domainName)
    {
        this.id = id;
        this.domainName = p_domainName;
    }
    
    public Domain(Domain p_domain)
    {
        this(p_domain.getId(),
             p_domain.getDomainName());
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

    
    public String getDomainName()
    {
        return domainName;
    }
    @XmlValue
    public void setDomainName(String domainName)
    {
        this.domainName = domainName;
    }

	public String toJSON()
    {
        StringBuilder json = new StringBuilder();
        json.append("{")
            .append("\"id\":").append(id).append(", ")
            .append("\"domainName\":\"").append(domainName).append("\", ")
            .append("}");

        return json.toString();
    }

	 public int compareTo(Domain domain)
    {
        return getDomainName().compareToIgnoreCase(domain.getDomainName());
    }
	
}
