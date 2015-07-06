package it.prisma.businesslayer.orchestrator.dsl;

import it.prisma.businesslayer.orchestrator.dsl.deployment.DeployerType;

public class CloudifyDeploymentData extends DeployerDeploymentData {

	private String recipeName;
	private String recipeVersion;
	private String recipeURL;
	private String recipePropertiesFile;
	private String customizedRecipePath;
	private String recipeUploadKey;
	private String applicationName;
	private String applicationDeploymentID;	
	
	public String getRecipePropertiesFile() {
		return recipePropertiesFile;
	}

	public void setRecipePropertiesFile(String recipePropertiesFile) {
		this.recipePropertiesFile = recipePropertiesFile;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getApplicationDeploymentID() {
		return applicationDeploymentID;
	}

	public void setApplicationDeploymentID(String applicationDeploymentID) {
		this.applicationDeploymentID = applicationDeploymentID;
	}

	public String getRecipeName() {
		return recipeName;
	}

	public void setRecipeName(String recipeName) {
		this.recipeName = recipeName;
	}

	public String getRecipeVersion() {
		return recipeVersion;
	}

	public void setRecipeVersion(String recipeVersion) {
		this.recipeVersion = recipeVersion;
	}

	public String getRecipeURL() {
		return recipeURL;
	}

	public void setRecipeURL(String recipeURL) {
		this.recipeURL = recipeURL;
	}

	public String getCustomizedRecipePath() {
		return customizedRecipePath;
	}

	public void setCustomizedRecipePath(String customizedRecipePath) {
		this.customizedRecipePath = customizedRecipePath;
	}

	@Override
	public DeployerType getDeployerType() {
		return DeployerType.CLOUDIFY;
	}

	public String getRecipeUploadKey() {
		return recipeUploadKey;
	}

	public void setRecipeUploadKey(String recipeUploadKey) {
		this.recipeUploadKey = recipeUploadKey;
	}
}
