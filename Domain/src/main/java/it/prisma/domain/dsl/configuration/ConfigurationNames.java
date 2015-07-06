package it.prisma.domain.dsl.configuration;

/**
 * This interface contains the names of the DB properties
 *
 */
public interface ConfigurationNames {

	String AVAILABILITY_ZONE 				= "availability_zone_infn";
	String IAAS_FLAVOR 						= "iaas_flavor";
	String IAAS_IMAGES 						= "iaas_images";
	String IAAS_PROFILE 					= "iaas_profile";
	String PAAS_APPAAS_SERVER_TYPE 			= "paas_appaas_server_servertype";
	String PAAS_APPAAS_WEB_SERVER			= "paas_appaas_web_server";
	String PAAS_APPAAS_APPLICATION_SERVER	= "paas_appaas_application_server";
	String PAAS_BIAAS_PENTAHO_DATABASE 		= "paas_biaas_pentaho_database";
	String PAAS_BIAAS_PENTAHO_VOLUME		= "paas_biaas_pentaho_volume";
	String PAAS_DBAAS_BACKUP 				= "paas_dbaas_backup";
	String PAAS_DBAAS_DBMS 					= "paas_dbaas_dbms";
	String PAAS_LOAD_BALANCING				= "paas_load_balancing";
	String PAAS_RABBIT_VERSIONS 			= "paas_rabbit_versions";
	String UPLOAD_FILE_SIZE 				= "uploadFileSize";
}
