package it.prisma.dal.entities.paas.services.mqaas;
//package it.prisma.dal.entities.paas.services.mqaas;
//
//// Generated 17-set-2014 21.25.53 by Hibernate Tools 3.4.0.CR1
//
//import javax.persistence.AttributeOverride;
//import javax.persistence.AttributeOverrides;
//import javax.persistence.Column;
//import javax.persistence.EmbeddedId;
//import javax.persistence.Entity;
//import javax.persistence.Table;
//
///**
// * QueueaaSdetails generated by hbm2java
// */
//@Entity
//@Table(name = "QueueaaSDetails")
//public class QueueaaSDetails implements java.io.Serializable {
//
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 8409662037076368183L;
//	private QueueaaSDetailsId id;
//
//	public QueueaaSDetails() {
//	}
//
//	public QueueaaSDetails(QueueaaSDetailsId id) {
//		this.id = id;
//	}
//
//	@EmbeddedId
//	@AttributeOverrides({
//			@AttributeOverride(name = "queueaaSid", column = @Column(name = "QueueaaSID", nullable = false)),
//			@AttributeOverride(name = "productType", column = @Column(name = "productType", nullable = false, length = 45)),
//			@AttributeOverride(name = "productName", column = @Column(name = "productName", nullable = false, length = 45)),
//			@AttributeOverride(name = "loadBalancingInstances", column = @Column(name = "loadBalancingInstances", nullable = false)),
//			@AttributeOverride(name = "domainName", column = @Column(name = "domainName", nullable = false, length = 256)),
//			@AttributeOverride(name = "secureConnectionEnabled", column = @Column(name = "secureConnectionEnabled", nullable = false)),
//			@AttributeOverride(name = "certificatePath", column = @Column(name = "certificatePath", length = 45)),
//			@AttributeOverride(name = "iaaSflavor", column = @Column(name = "IaaSFlavor", nullable = false, length = 256)),
//			@AttributeOverride(name = "securityGroup", column = @Column(name = "securityGroup", nullable = false, length = 45)),
//			@AttributeOverride(name = "notificationEmail", column = @Column(name = "notificationEmail", nullable = false, length = 256)),
//			@AttributeOverride(name = "paaSserviceId", column = @Column(name = "PaaSServiceID", nullable = false)),
//			@AttributeOverride(name = "workgroupId", column = @Column(name = "workgroupID", nullable = false)),
//			@AttributeOverride(name = "userAccountId", column = @Column(name = "userAccountID", nullable = false)),
//			@AttributeOverride(name = "paaSdeploymentId", column = @Column(name = "PaaSDeploymentID")),
//			@AttributeOverride(name = "type", column = @Column(name = "type", nullable = false, length = 8)),
//			@AttributeOverride(name = "name", column = @Column(name = "name", nullable = false, length = 128)),
//			@AttributeOverride(name = "description", column = @Column(name = "description", nullable = false, length = 2048)),
//			@AttributeOverride(name = "status", column = @Column(name = "status", length = 8)),
//			@AttributeOverride(name = "operation", column = @Column(name = "operation", length = 45)),
//			@AttributeOverride(name = "errorDescription", column = @Column(name = "errorDescription", length = 45)),
//			@AttributeOverride(name = "cretedAt", column = @Column(name = "cretedAt", nullable = false, length = 19)) })
//	public QueueaaSDetailsId getId() {
//		return this.id;
//	}
//
//	public void setId(QueueaaSDetailsId id) {
//		this.id = id;
//	}
//
//}
