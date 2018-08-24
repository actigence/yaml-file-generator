package com.actigence.p2y.constants;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeRegistry;

public interface PluginConstants {

    String PROPERTIES_FILE_EXTENSION = "properties";

    FileType PROPERTIES_FILE_TYPE = FileTypeRegistry.getInstance().getFileTypeByExtension(PROPERTIES_FILE_EXTENSION);

}
