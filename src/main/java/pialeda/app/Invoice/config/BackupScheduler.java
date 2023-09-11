package pialeda.app.Invoice.config;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Component for scheduling and performing database backups.
 */
@Component
public class BackupScheduler {

    private static final Logger logger = LoggerFactory.getLogger(BackupScheduler.class);

    /**
     * Scheduled method to perform the database backup at a specified time.
     */
    @Scheduled(cron = "0 0 16 * * ?") // 4:00 PM daily
    public void performDatabaseBackup() {
        // Execute the backup.bat script
        executeBackupScript();
        // You can also call your backupService.createDatabaseBackup() method here if needed.
    }

    /**
     * Execute the database backup script.
     */
    private void executeBackupScript() {
        try {
            // Provide the full path to the backup.bat script or place it in the project directory
            String scriptPath = "F:/Dev/Client1/BackupDB/backup_script.bat";

            // Create a process builder for the script
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", scriptPath);

            // Redirect standard output and error streams (optional)
            processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
            processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);

            // Start the process
            Process process = processBuilder.start();

            // Wait for the process to complete (optional)
            int exitCode = process.waitFor();

            // Check the exit code if needed
            if (exitCode == 0) {
                logger.info("Backup script executed successfully.");
            } else {
                logger.error("Backup script execution failed. Exit code: {}", exitCode);
            }
        } catch (IOException | InterruptedException e) {
            logger.error("Error executing backup script: {}", e.getMessage(), e);
        }
    }
}
