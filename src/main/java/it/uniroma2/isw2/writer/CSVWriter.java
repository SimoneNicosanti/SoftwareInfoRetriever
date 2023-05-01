package it.uniroma2.isw2.writer;

import it.uniroma2.isw2.model.rerieve.ClassInfo;
import it.uniroma2.isw2.model.rerieve.VersionInfo;
import it.uniroma2.isw2.utils.CSVUtils;
import it.uniroma2.isw2.utils.PathBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CSVWriter {

    private static final String SEPARATOR = "," ;
    private final String projectName ;

    private static final String[] HEADER_ARRAY = {
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

    public CSVWriter(String projectName) throws IOException {
        this.projectName = projectName ;

        Files.createDirectories(PathBuilder.buildDataSetDirectoryPath(projectName, true, true));
        Files.createDirectories(PathBuilder.buildDataSetDirectoryPath(projectName, false, true));
    }

    public void writeInfoAsCSV(List<VersionInfo> versionInfoList, Integer index, boolean training) throws IOException {
        Path outputPath = PathBuilder.buildDataSetFilePath(projectName, training, true, index) ;

        File csvFile = new File(outputPath.toString()) ;
        try(Writer writer = new BufferedWriter(new FileWriter(csvFile))) {
            CSVUtils.writeHeader(writer, HEADER_ARRAY, SEPARATOR);
            for (VersionInfo versionInfo : versionInfoList) {
                writeVersionInfo(writer, versionInfo);
            }
        }
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
