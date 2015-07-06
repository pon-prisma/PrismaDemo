package it.prisma.dal.entities.monitoring;

import static javax.persistence.GenerationType.IDENTITY;
import it.prisma.dal.entities.paas.deployments.PaaSDeploymentService.PaaSDeploymentServiceType;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

/**
 * AtomicService. 
 * 
 * This table is used to read the configurations.
 */
@Entity
@Table(name = "AtomicService")
public class AtomicService implements java.io.Serializable {

    private static final long serialVersionUID = 3101235952310673986L;

    private Long atomicServiceID;

    /**
     * Name into monitoring pillar
     */
    private String name;
    
    /**
     * {@link PaaSDeploymentServiceType}
     */
    private String type;

    /**
     * User readeable name
     */
    private String label;
    private String description;

    private Set<Metric> metrics = new HashSet<Metric>(0);

    public AtomicService() {
    }

    public AtomicService(String name, String type, String label, String description) {
	this.name = name;
	this.type = type;
	this.label = label;
	this.description = description;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "atomicServiceID", unique = true, nullable = false)
    public Long getId() {
	return this.atomicServiceID;
    }

    public void setId(Long atomicServiceID) {
	this.atomicServiceID = atomicServiceID;
    }

    @Column(name = "name", nullable = false, unique = false, length = 128)
    public String getName() {
	return this.name;
    }

    public void setName(String name) {
	this.name = name;
    }

    @Column(name = "type", nullable = false, unique = true, length = 128)
    public String getType() {
	return this.type;
    }

    public void setType(String type) {
	this.type = type;
    }
    
    @Column(name = "label", nullable = false, length = 128)
    public String getLabel() {
	return this.label;
    }

    public void setLabel(String label) {
	this.label = label;
    }

    @Column(name = "description", nullable = true, length = 65535)
    @Type(type = "text")
    public String getDescription() {
	return this.description;
    }

    public void setDescription(String description) {
	this.name = description;
    }

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "AtomicService_has_Metric", joinColumns = { 
		@JoinColumn(name = "atomicServiceID", nullable = false, updatable = false) }, 
		inverseJoinColumns = { @JoinColumn(name = "metricID", 
				nullable = false, updatable = false) })
    public Set<Metric> getMetrics() {
	return this.metrics;
    }

    public void setMetrics(Set<Metric> metrics) {
	this.metrics = metrics;
    }
}
