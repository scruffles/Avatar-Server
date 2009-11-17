package net.scruffles.avatar;

import java.io.File;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

public class Main {
    private static final String PREFERENCES_PATH = "/net/scruffles/avatarServer";
    private static final String PREFERENCES_KEY = "path";
    private static final String CLEAR_PREFERENCES_FLAG = "-clearConfigDir";

    public static void main(String[] arguments) throws Exception {
        if (isClearPreferencesFlagProvided(arguments)) {
            clearPreferencesAndExit();
        }

        String path = getPathToFiles(arguments);

        if (isInvalid(path)) {
            exitWithMessage("Could not find a valid images path");
        }

        saveToPreferencesApi(path);

        startServer(path);
    }

    private static void startServer(String path) throws Exception {
        Server server = new Server(8080);
        Context root = new Context(server, "/", Context.SESSIONS);
        root.addServlet(new ServletHolder(new IncludedImageServlet(path)), "/included/*");
        root.addServlet(new ServletHolder(new ImageByHashcodeServlet(path)), "/*");
        server.start();
    }

    private static void exitWithMessage(String message) {
        showError(message);
        System.exit(0);
    }

    private static boolean isInvalid(String path) {
        return !isValid(path);
    }

    private static boolean isClearPreferencesFlagProvided(String[] arguments) {
        return arguments.length == 1 && arguments[0].equals(CLEAR_PREFERENCES_FLAG);
    }

    private static void clearPreferencesAndExit() throws BackingStoreException {
        Preferences.userRoot().node(PREFERENCES_PATH).clear();
        exitWithMessage("Directory preferences cleared");
    }

    private static String getPathToFiles(String[] arguments) {
        if (arguments.length == 1) {
            String path = arguments[0];
            if (isInvalid(path)) {
                exitWithMessage("An invalid path was specified on the command line");
            }
            else {
                return path;
            }
        }

        String path = getPathFromPreferencesApi();
        if (path != null) {
            return path;
        }

        return getPathFromUserPrompt();
    }

    private static void saveToPreferencesApi(String path) {
        Preferences.userRoot().node(PREFERENCES_PATH).put(PREFERENCES_KEY, path);
    }

    private static void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Avatar Server", JOptionPane.ERROR_MESSAGE);
    }

    private static boolean isValid(String path) {
        return path != null && new File(path).exists();
    }

    private static String getPathFromUserPrompt() {
        showError("Please specify a path to a directory where icons and configuration will be saved.");
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Images Directory");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.showSaveDialog(null);
        return chooser.getSelectedFile().getAbsolutePath();
    }

    private static String getPathFromPreferencesApi() {
        return Preferences.userRoot().node(PREFERENCES_PATH).get(PREFERENCES_KEY, null);
    }
}
