//package it.prisma.presentationlayer.webui.configs;
//
//import static org.apache.commons.io.filefilter.TrueFileFilter.TRUE;
//
//import java.io.File;
//import java.util.Date;
//import java.util.Iterator;
//
//import org.apache.commons.io.FileUtils;
//import org.apache.commons.io.filefilter.AgeFileFilter;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//
//@Configuration
//@EnableScheduling
//public class ScheduledTasks {
//
//	static final Logger LOG = LogManager.getLogger(ScheduledTasks.class
//			.getName());
//
//	@Value("${upload.path}")
//	private String folder;
//
//	Date tresholdDate = new Date();
//
//	// schedulo il task ogni 10 minuti
//	@Scheduled(fixedDelay = 60000 * 10)
//	public void deleteUploadedFiles() {
//		//LOG.trace("Scheduled delete started");
//
//		deleteFiles(new File(folder));
//
//		// Ricarico la data per il prossimo scheduled delete
//		tresholdDate = new Date();
//	}
//
//	private void deleteFiles(File file) {
//		Iterator<File> filesToDelete = FileUtils.iterateFiles(file,
//				new AgeFileFilter(tresholdDate), TRUE);
//
//		while (filesToDelete.hasNext()) {
//			filesToDelete.next().delete();
//		}
//	}
//}
