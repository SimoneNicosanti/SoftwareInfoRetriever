package it.uniroma2.isw2.writer;

import it.uniroma2.isw2.model.ClassInfo;
import it.uniroma2.isw2.model.VersionInfo;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CSVWriter {

    private static final String SEPARATOR = "," ;
    private final String projectName ;
    private final Path testPath ;
    private final Path trainPath ;

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

    public CSVWriter(String projectName) throws IOException {
        this.projectName = projectName ;
        this.trainPath = Path.of(projectName.toUpperCase(), "CSV", "Train");
        this.testPath = Path.of(projectName.toUpperCase(), "CSV", "Test");

        Files.createDirectories(trainPath);
        Files.createDirectories(testPath);
    }

    public void writeInfoAsCSV(List<VersionInfo> versionInfoList, Integer index, boolean training) throws IOException {
        Path outputPath ;
        if (training) {
            outputPath = Path.of(trainPath.toString(), projectName.toUpperCase() + "_" + index + "_training.csv") ;
        }
        else {
            outputPath = Path.of(testPath.toString(), projectName.toUpperCase() + "_" + index + "_testing.csv") ;
        }

        File csvFile = new File(outputPath.toString()) ;
        try(Writer writer = new BufferedWriter(new FileWriter(csvFile))) {
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
