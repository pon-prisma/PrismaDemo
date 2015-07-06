package it.prisma.domain.dsl.paas.services;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Interface defining the shared properties of each PaaSService Representation
 * (ie APPaaSEnvironment, QueueaaS, ...).
 * 
 * @author l.biava
 * 
 */
public interface GenericPaaSServiceRepresentation extends Serializable {

	public Long getId();

	public void setId(Long Id);

	public Long getUserId();

	public void setUserId(Long userId);

	public Long getWorkgroupId();

	public void setWorkgroupId(Long workgroupId);

	public String getName();

	public void setName(String name);

	public String getDescription();

	public void setDescription(String description);

	public String getStatus();

	public void setStatus(String status);

	public String getOperation();

	public void setOperation(String operation);

	public String getErrorDescription();

	public void setErrorDescription(String errorDescription);

	public Date getCreatedAt();

	public void setCreatedAt(Date createdAt);

	public String getDomainName();

	public void setDomainName(String domainName);

	public String getNotificationEmail();

	public void setNotificationEmail(String notificationEmail);

	public Boolean getSecureConnectionEnabled();

	public void setSecureConnectionEnabled(Boolean secureConnectionEnabled);

	public String getCertificatePath();

	public void setCertificatePath(String certificatePath);

	public String getSecurityGroup();

	public void setSecurityGroup(String securityGroup);

	public String getIaaSFlavor();

	public void setIaaSFlavor(String iaaSFlavor);

	public Integer getLoadBalancingInstances();

	public void setLoadBalancingInstances(Integer loadBalancingInstances);

	public String getQoS();

	public void setQoS(String QoS);

	public boolean isPublicVisibility();

	public void setPublicVisibility(boolean publicVisibility);

	public String getAvailabilityZone();

	public void setAvailabilityZone(String availabilityZone);

	public Set<PaaSServiceEventRepresentation> getPaaSServiceEvents();

	public void setPaaSServiceEvents(
			Set<PaaSServiceEventRepresentation> paaSServiceEvents);

	public ProductSpecificParameters getProductSpecificParameters();

	public void setProductSpecificParameters(
			ProductSpecificParameters productSpecificParameters);

	public List<PaaSServiceEndpointRepresentation> getPaasServiceEndpoints();

	public void setPaasServiceEndpoints(
			List<PaaSServiceEndpointRepresentation> paasServiceEndpoints);

}
