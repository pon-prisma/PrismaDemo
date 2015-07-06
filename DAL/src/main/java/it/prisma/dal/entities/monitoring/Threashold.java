package it.prisma.dal.entities.monitoring;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

/**
 * Threashold
 */
@Entity
@Table(name = "Threashold")
public class Threashold implements java.io.Serializable {

    private static final long serialVersionUID = 3101235952310673986L;

    private Long threasholdID;

    /**
     * Name in commons between monitoring pillar and BL
     */
    private String name;

    /**
     * User readeable name
     */
    private String label;
    private String description;

    /**
     * 0 - INFORMATION: only for information. Example: UsedRam is over 50% 
     * 1 - WARNING: Example: UsedRam is over 70% 3 - PROBLEM: Example: UsedRam is over 90%
     * 2 - ERROR: Example: ssh not working
     */
    private Integer severity;
    
    private Metric metric;

    public Threashold() {
    }

    public Threashold(String name, String label, String description, Integer severity) {
	this.name = name;
	this.label = label;
	this.description = description;
	this.severity = severity;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "threasholdID", unique = true, nullable = false)
    public Long getId() {
	return this.threasholdID;
    }

    public void setId(Long threasholdId) {
	this.threasholdID = threasholdId;
    }

    @Column(name = "name", nullable = false, length = 128)
    public String getName() {
	return this.name;
    }

    public void setName(String name) {
	this.name = name;
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
	this.description = description;
    }

    @Column(name = "severity", nullable = true)
    public Integer getSeverity() {
	return this.severity;
    }

    public void setSeverity(Integer severity) {
	this.severity = severity;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="metricID")
    public Metric getMetric() {
	return metric;
    }

    public void setMetric(Metric metric) {
	this.metric = metric;
    }

}
