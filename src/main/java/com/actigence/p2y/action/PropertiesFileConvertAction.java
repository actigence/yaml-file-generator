package com.actigence.p2y.action;

import com.actigence.p2y.constants.PluginConstants;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PropertiesFileConvertAction extends AnAction {

    private static final String ACTION_TEXT = "Generate Yaml file";

    public PropertiesFileConvertAction() {
        super(ACTION_TEXT);
    }

    public void actionPerformed(AnActionEvent event) {
        Project project = event.getProject();

        Editor editor = event.getData(CommonDataKeys.EDITOR);
        if (editor == null) {
            return;
        }

        String str = getYamlString(event);

        String yamlFilePath = createYamlFile((EditorImpl) editor, str);

        try {
            openYamlFile(project, yamlFilePath);
        } catch (Exception e) {
            Messages.showMessageDialog(project, "Unable to create Yaml file.", "Error", Messages.getInformationIcon());
        }
    }

    private void openYamlFile(Project project, String yamlFilePath) {
        VirtualFile yamlVirtualFile = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(new File(yamlFilePath));
        if (project != null && yamlVirtualFile != null) {
            new OpenFileDescriptor(project, yamlVirtualFile).navigate(true);
        }
    }

    @NotNull
    private String createYamlFile(EditorImpl editor, String str) {
        VirtualFile virtualFile = editor.getVirtualFile();
        String yamlFilePath = virtualFile.getPath().substring(0, virtualFile.getPath().length() - PluginConstants.PROPERTIES_FILE_EXTENSION.length()) + "yml";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(yamlFilePath))) {
            writer.write(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return yamlFilePath;
    }

    @NotNull
    private String getYamlString(AnActionEvent event) {
        Editor editor = event.getData(CommonDataKeys.EDITOR);
        if (editor == null) {
            throw new RuntimeException("No editor object.");
        }

        String propertiesFilePath = ((EditorImpl) editor).getVirtualFile().getPath();

        //call properties to yaml converter to get yaml String and return that.
        //.....

        return propertiesFilePath;
    }

    @Override
    public void update(AnActionEvent e) {
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        e.getPresentation().setVisible(isVisible(editor));
    }

    /**
     * Show only if current file is a Properties file.
     *
     * @param editor
     * @return
     */
    private boolean isVisible(Editor editor) {
        return editor != null && PluginConstants.PROPERTIES_FILE_TYPE.equals(((EditorImpl) editor).getVirtualFile().getFileType());
    }
}