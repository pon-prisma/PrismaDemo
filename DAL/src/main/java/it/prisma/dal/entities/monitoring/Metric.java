package it.prisma.dal.entities.monitoring;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

/**
 * Metric
 */
@Entity
@Table(name = "Metric")
public class Metric implements java.io.Serializable {

    private static final long serialVersionUID = 3101235952310673986L;

    private Long metricID;

    /**
     * Name in commons between monitoring pillar and BL
     */
    private String name;

    /**
     * User readeable name
     */
    private String label;
    private String description;

    public static int PURPOSE_INFORMATION = 1;
    public static int PURPOSE_STATUS = 2;
    // Example for new CONSTANT public static int PURPOSE_CUSTOM = 4;
    /**
     * Bitmap See PURPOSE_XXXXXX
     * 
     * 1 - INFORMATION: Example totalRam 2 -
     */
    private Integer purpose;

    /**
     * Unità di misura della metrica.
     * 
     */
    private String measurementUnit;

    private Set<Threashold> threasholds = new HashSet<Threashold>(0);

    public Metric() {
    }

    public Metric(String name, String description) {
	this.name = name;
	this.description = description;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "metricID", unique = true, nullable = false)
    public Long getId() {
	return this.metricID;
    }

    public void setId(Long metricID) {
	this.metricID = metricID;
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
	this.name = description;
    }

    @Column(name = "measurementUnit", nullable = false, length = 128)
    public String getMeasurementUnit() {
	return this.measurementUnit;
    }

    public void setMeasurementUnit(String measurementUnit) {
	this.measurementUnit = measurementUnit;
    }

    @Column(name = "purpose", nullable = false, columnDefinition = "int default 1")
    public Integer getPurpose() {
	return this.purpose;
    }

    /**
     * <pre>
     * setPurpose(PURPOSE_STATUS | PURPOSE_XXX)
     * </pre>
     * 
     * @param purpose
     */
    public void setPurpose(int purpose) {
	this.purpose = purpose;
    }

    // @JsonBackReference
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "metric")
    public Set<Threashold> getThreasholds() {
	return threasholds;
    }

    public void setThreasholds(Set<Threashold> threasholds) {
	this.threasholds = threasholds;
    }
}
