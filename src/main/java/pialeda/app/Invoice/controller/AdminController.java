package pialeda.app.Invoice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pialeda.app.Invoice.dto.GlobalUser;
import pialeda.app.Invoice.model.User;
import pialeda.app.Invoice.service.UserService;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller for handling admin-related actions.
 */
@Controller
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private UserService userService;

    /**
     * Displays the list of users in the admin panel.
     *
     * @param model The model to add attributes.
     * @return The view name for the admin users page.
     */
    @GetMapping("admin-users")
    public String users(Model model){
        String role = GlobalUser.getUserRole();
        String userFname = GlobalUser.getUserFirstName();
        String userLname = GlobalUser.getUserLastName();
        String fullName = userLname+", "+userFname;
        String destination=null;
        if(role == null){
            return destination = "redirect:/login";
        } else if (role.equals("vr-staff")) {
            return destination = "redirect:/vr/user/invoices";
        } else if (role.equals(("marketing"))) {
            return destination = "redirect:marketing-invoice";
        } else if (role.equals("admin")) {
            model.addAttribute("userList", userService.getAllUser());
            model.addAttribute("user", new User());
            model.addAttribute("fullName",fullName);
            return destination = "admin/users";
        }
        return destination;
    }

    /**
     * Performs the creation of a new user.
     *
     * @param user The user object to create.
     * @return A redirection to the admin users page.
     */
    @PostMapping("/createUser")
    public String createUser(@ModelAttribute("user") User user) {
        userService.createUser(user);
        logger.info("Created user: {}", user.getEmail());
        return "redirect:/admin-users";
    }

    /**
     * Updates an existing user.
     *
     * @param uPassword The new password for the user (optional).
     * @param user      The user object to update.
     * @return A redirection to the admin users page.
     */
    @PostMapping("/updateUser")
    public String updateUser(@RequestParam(value = "uPassword", required = false) String uPassword, @ModelAttribute("user") User user) {
        userService.updateUser(user, uPassword);
        logger.info("Updated user: {}", user.getEmail());
        return "redirect:/admin-users";
    }

    /**
     * Deletes a user.
     *
     * @param user The user object to delete.
     * @return A redirection to the admin users page.
     */
    @PostMapping("/deleteUser")
    public String deleteItem(@ModelAttribute("user") User user) {
        userService.deleteUser(user.getId());
        logger.info("Deleted user: {}", user.getEmail());
        return "redirect:/admin-users";
    }

    /**
     * Performs a database backup.
     *
     * @return The view name for the backup and restore page.
     */
    @PostMapping("/backup")
    public String backupData() {
        try {
            String backupPath = createBackupPath();
            String command = buildBackupCommand(backupPath);
            Process process = Runtime.getRuntime().exec(command);
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                logger.info("Database backup completed successfully.");
            } else {
                logger.error("Database backup failed. Exit code: {}", exitCode);
            }
            return "admin/backupandrestore";
        } catch (IOException | InterruptedException e) {
            logger.error("Error performing database backup: {}", e.getMessage(), e);
            return "admin/backupandrestore";
        }
    }

    // Private methods with logger messages (as provided in the previous response)

    /**
     * Create a timestamp-based backup file path.
     *
     * @return The backup file path.
     */
    private String createBackupPath() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String timestamp = now.format(formatter);
        String backupPath = "C:/backup/backup_" + timestamp + ".sql";
        return backupPath;
    }

    /**
     * Build the command to execute the database backup.
     *
     * @param backupPath The path where the backup file will be saved.
     * @return The backup command.
     */
    private String buildBackupCommand(String backupPath) {
        String mysqlDumpPath = "C:/Program Files/MySQL/MySQL Workbench 8.0/mysqldump.exe";
        String username = "root";
        String password = "Root!123";
        String database = "pialedadb";
        String command = mysqlDumpPath + " -u " + username + " -p" + password + " " + database + " > " + backupPath;
        return command;
    }
}
