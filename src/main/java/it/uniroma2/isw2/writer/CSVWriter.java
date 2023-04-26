package it.uniroma2.isw2.writer;

import it.uniroma2.isw2.model.ClassInfo;
import it.uniroma2.isw2.model.VersionInfo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CSVWriter {

    private static final String SEPARATOR = "," ;
    private final String outputPath ;

    private static final String[] HEADER_STRING = {
            "Version",
            "ClassName",
            "LinesOfCode",
            "AddedLOC",
            "MaxAddedLOC",
            "AvgAddedLOC",
            "TouchedLOC",
            "Churn",
            "MaxChurn",
            "AvgChurn",
            "NumberOfAuthors",
            "NumberOfRevisions",
            "NumberOfDefectsFixed",
            "Buggy"} ;

    public CSVWriter(String projectName) {
        this.outputPath = projectName + ".csv" ;
    }

    public void writeAllVersionInfo(List<VersionInfo> versionInfoList) throws IOException {
        Logger.getGlobal().log(Level.INFO, "Scrittura CSV\n");

        try(Writer writer = new BufferedWriter(new FileWriter(outputPath))) {
            writeHeader(writer);
            for (VersionInfo versionInfo : versionInfoList) {
                writeVersionInfo(writer, versionInfo);
            }
        }
    }

    private void writeHeader(Writer writer) throws IOException {
        StringBuilder stringBuilder = new StringBuilder() ;
        for (int i = 0 ; i < HEADER_STRING.length ; i++) {
            stringBuilder.append(HEADER_STRING[i]) ;
            if (i != HEADER_STRING.length - 1) {
                stringBuilder.append(SEPARATOR) ;
            }
        }
        stringBuilder.append("\n") ;
        writer.write(stringBuilder.toString());
    }

    private void writeVersionInfo(Writer writer, VersionInfo versionInfo) throws IOException {
        List<ClassInfo> classInfoList = versionInfo.getClassInfoList() ;
        StringBuilder stringBuilder = new StringBuilder() ;
        for (ClassInfo classInfo : classInfoList) {
            stringBuilder.append(versionInfo.getReleaseNumber()).append(SEPARATOR) ;

            String classString = buildClassString(classInfo) ;
            stringBuilder.append(classString).append("\n") ;

        }
        writer.write(stringBuilder.toString());
    }

    private String buildClassString(ClassInfo classInfo) {

        return classInfo.getName()
                + SEPARATOR
                + classInfo.getLoc()
                + SEPARATOR
                + classInfo.getAddedLoc()
                + SEPARATOR
                + classInfo.getMaxAddedLoc()
                + SEPARATOR
                + classInfo.getAvgAddedLoc()
                + SEPARATOR
                + classInfo.getTouchedLoc()
                + SEPARATOR
                + classInfo.getChurn()
                + SEPARATOR
                + classInfo.getMaxChurn()
                + SEPARATOR
                + classInfo.getAvgChurn()
                + SEPARATOR
                + classInfo.getNumberOfAuthors()
                + SEPARATOR
                + classInfo.getNumberOfRevisions()
                + SEPARATOR
                + classInfo.getNumberDefectsFixed()
                + SEPARATOR
                + (classInfo.isBuggy() ? "True" : "False");
    }
}
