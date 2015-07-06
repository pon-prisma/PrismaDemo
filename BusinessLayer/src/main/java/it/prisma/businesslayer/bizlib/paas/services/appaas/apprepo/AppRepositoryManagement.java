package it.prisma.businesslayer.bizlib.paas.services.appaas.apprepo;

import it.prisma.dal.entities.accounting.UserAccount;
import it.prisma.dal.entities.accounting.Workgroup;
import it.prisma.dal.entities.paas.services.appaas.AppaaSEnvironment;
import it.prisma.dal.entities.paas.services.appaas.ApplicationRepositoryEntry;
import it.prisma.dal.entitywrappers.paas.services.appaas.AppaaS;

import java.io.InputStream;
import java.net.URL;
import java.util.Collection;

import org.javatuples.Pair;

public interface AppRepositoryManagement {

	/**
	 * Adds a new App Src File in the Application Repository.
	 * 
	 * @param userAccount
	 * @param workgroup
	 * @param applicationName
	 * @param tag
	 * @param description
	 * @param public_
	 * @param fileName
	 * @param filePath
	 * @param createdAt
	 * @param file
	 * @return A pair containing the Id and the filePath of the new file entry.
	 */
	public Pair<Long, String> addAppSrcFile(UserAccount userAccount,
			Workgroup workgroup, String applicationName, String tag,
			String description, boolean public_, String fileName, InputStream is)
			throws Exception;

	/**
	 * Adds a new App Src File in the Application Repository, retrieving it from
	 * the given URL.
	 * 
	 * TODO Set max file size
	 * 
	 * @param userAccount
	 * @param workgroup
	 * @param applicationName
	 * @param tag
	 * @param description
	 * @param public_
	 * @param fileName
	 * @param filePath
	 * @param createdAt
	 * @param URL
	 *            the URL to retrieve the file from.
	 * @return A pair containing the Id and the filePath of the new file entry.
	 */
	public Pair<Long, String> addAppSrcFileByUrl(UserAccount userAccount,
			Workgroup workgroup, String applicationName, String tag,
			String description, boolean public_, String fileName, URL url)
			throws Exception;

	public StoredObject getAPPaaSEnvAppSrcFile(Workgroup workgroup, AppaaS app,
			AppaaSEnvironment env) throws Exception;

	public StoredObject getAPPaaSEnvAppSrcFileMeta(Workgroup workgroup,
			AppaaS app, AppaaSEnvironment env) throws Exception;

	public Collection<ApplicationRepositoryEntry> getPublicAppSrcFiles()
			throws Exception;

	public Collection<ApplicationRepositoryEntry> getPrivateAppSrcFiles(
			Long workgroupId) throws Exception;
}
