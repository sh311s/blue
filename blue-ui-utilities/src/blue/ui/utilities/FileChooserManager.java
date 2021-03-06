package blue.ui.utilities;

import java.awt.Component;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FileChooserManager {

    private HashMap<Object, DialogInfoSet> dialogInfoSets
            = new HashMap<>();

//    private FileChooser fileChooser;
    private ExtensionFilter allFilter = new ExtensionFilter("All Files", "*.*", "*");

    private static FileChooserManager instance = null;

    public static FileChooserManager getDefault() {
        if (instance == null) {
            instance = new FileChooserManager();
        }
        return instance;
    }

    public void addFilter(Object fileChooserId, ExtensionFilter filter) {
        DialogInfoSet temp = getDialogInfoSet(fileChooserId);
        temp.filters.add(filter);
    }

    public void setSelectedFile(Object fileChooserId, File f) {
        DialogInfoSet temp = getDialogInfoSet(fileChooserId);
        temp.currentDirectory = null;
        temp.selectedFile = f;
    }

    public void setCurrentDirectory(Object fileChooserId, File f) {
        DialogInfoSet temp = getDialogInfoSet(fileChooserId);
        temp.selectedFile = null;
        temp.currentDirectory = f;
    }

    public void setDialogTitle(Object fileChooserId, String title) {
        DialogInfoSet temp = getDialogInfoSet(fileChooserId);
        temp.dialogTitle = title;
    }

    public void setMultiSelectionEnabled(Object fileChooserId, boolean val) {
        DialogInfoSet temp = getDialogInfoSet(fileChooserId);
        temp.isMultiSelect = val;
    }

    public List<File> showOpenDialog(Object fileChooserId, Component parent) {
        final DialogInfoSet temp = getDialogInfoSet(fileChooserId);

        final CountDownLatch c = new CountDownLatch(1);

        final List<File> retVal = new ArrayList<>();

        // TEST AWT IMPL
//        java.awt.FileDialog ff = new java.awt.FileDialog((Frame)
//                SwingUtilities.windowForComponent(parent));
//        
//        ff.setFilenameFilter((dir, name) ->
//            true
//        );
//        
//        ff.setMode(FileDialog.LOAD);
//        ff.setTitle(temp.dialogTitle);
//        ff.setMultipleMode(temp.isMultiSelect);
//        ff.setVisible(true);
//        
//        if(temp.isMultiSelect) {
//            File[] files = ff.getFiles();
//            for(File f : files) {
//                retVal.add(f);
//            }
//        } else {
//            String f = ff.getFile();
//            if(f != null) {
//                retVal.add(new File(f));
//            }
//        }
        Runnable r = () -> {
            Stage s = new Stage();
            s.initOwner(null);
            s.initModality(Modality.APPLICATION_MODAL);

            if (temp.directoriesOnly) {
                DirectoryChooser chooser = new DirectoryChooser();
                chooser.setTitle(temp.dialogTitle);

                if(temp.currentDirectory != null) {
                    chooser.setInitialDirectory(temp.currentDirectory);
                }    

                File f = chooser.showDialog(s);
                if (f != null) {
                    retVal.add(f);
                    temp.currentDirectory = f;
                }
            } else {
                FileChooser f = new FileChooser();
                f.setTitle(temp.dialogTitle);
                f.getExtensionFilters().addAll(temp.filters);
                f.getExtensionFilters().add(allFilter);

                if (temp.currentDirectory != null) {
                    f.setInitialDirectory(temp.currentDirectory);
                } else if(temp.selectedFile != null){
                    f.setInitialDirectory(temp.selectedFile.getParentFile());
                }
                if (temp.selectedFile != null) {
                    f.setInitialFileName(temp.selectedFile.getName());
                }

                if (temp.isMultiSelect) {
                    temp.selectedFile = null;
                    List<File> found = f.showOpenMultipleDialog(s);
                    if (found != null) {
                        temp.currentDirectory = found.get(0).getParentFile();
                        retVal.addAll(found);
                    }
                } else {
                    File ret = f.showOpenDialog(s);
                    if (ret != null) {
                        temp.currentDirectory = ret.getParentFile();
                        retVal.add(ret);
                    }
                }

            }

            c.countDown();
        };

        if (Platform.isFxApplicationThread()) {
            r.run();
        } else {
            Platform.runLater(r);
            try {
                c.await();
            } catch (InterruptedException ex) {
                Logger.getLogger(FileChooserManager.class.getName()).log(
                        Level.SEVERE,
                        null, ex);
            }
        }


        return retVal;
    }

    public File showSaveDialog(Object fileChooserId, Component parent) {
        DialogInfoSet temp = getDialogInfoSet(fileChooserId);

        final CountDownLatch c = new CountDownLatch(1);

        final AtomicReference<File> retVal = new AtomicReference<>(null);

        // TEST AWT IMPLEMENTATION
//        java.awt.FileDialog ff = new java.awt.FileDialog((Frame)
//                SwingUtilities.windowForComponent(parent));
//        
//        ff.setFilenameFilter((File dir, String name) ->
//            true
//        );
//        
//        ff.setMode(FileDialog.SAVE);
//        ff.setTitle(temp.dialogTitle);
////        ff.setMultipleMode(temp.isMultiSelect);
//        ff.setVisible(true);
//        
//        return new File(ff.getFile());
        Platform.runLater(() -> {
            Stage s = new Stage();
            s.initOwner(null);
            s.initModality(Modality.APPLICATION_MODAL);

            FileChooser f = new FileChooser();
            f.setTitle(temp.dialogTitle);
            f.getExtensionFilters().addAll(temp.filters);

            if (temp.currentDirectory != null) {
                f.setInitialDirectory(temp.currentDirectory);
            } else if(temp.selectedFile != null) {
                f.setInitialDirectory(temp.selectedFile.getParentFile());
            }
            if (temp.selectedFile != null) {
                f.setInitialFileName(temp.selectedFile.getName());
            }

            File ret = f.showSaveDialog(s);
            if (ret != null) {
                temp.currentDirectory = null;
                temp.selectedFile = ret;
                retVal.set(ret);
            }

            c.countDown();
        });

        try {
            c.await();
        } catch (InterruptedException ex) {
            Logger.getLogger(FileChooserManager.class.getName()).log(
                    Level.SEVERE,
                    null, ex);
        }

        return retVal.get();
    }

    private DialogInfoSet getDialogInfoSet(Object fileChooserId) {
        if (dialogInfoSets.containsKey(fileChooserId)) {
            DialogInfoSet infoSet = dialogInfoSets.get(fileChooserId);
            return infoSet;
        } else {
            DialogInfoSet temp = new DialogInfoSet();

            temp.selectedFile = new File(System.getProperty("user.home"));
            temp.currentDirectory = new File(System.getProperty("user.home"));

            dialogInfoSets.put(fileChooserId, temp);

            return temp;
        }
    }

    public boolean isDialogDefined(Object fileChooserId) {
        return dialogInfoSets.containsKey(fileChooserId);
    }

    public static class DialogInfoSet {

        List<ExtensionFilter> filters = new ArrayList<>();

        File selectedFile;

        File currentDirectory;

        File[] selectedFiles;

        String dialogTitle = "Select File";

        boolean directoriesOnly = false;

        boolean isMultiSelect = false;

    }
}
