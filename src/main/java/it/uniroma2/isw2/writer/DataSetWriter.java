package it.uniroma2.isw2.writer;

import it.uniroma2.isw2.model.rerieve.ClassInfo;
import it.uniroma2.isw2.model.rerieve.VersionInfo;
import it.uniroma2.isw2.utils.PathBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DataSetWriter {

    private static final List<String> ATTRIBUTES_LIST = new ArrayList<>(List.of(
            "LinesOfCode",
            "AddedLOC",
            "MaxAddedLOC",
            "AvgAddedLOC",
            //"RemovedLoc",
            //"MaxRemovedLoc",
            //"AvgRemovedLoc",
            "TouchedLOC",
            "Churn",
            "MaxChurn",
            "AvgChurn",
            "NumberOfAuthors",
            "NumberOfRevisions",
            "NumberOfDefectsFixed",
            "Buggy")) ;
    
    private static final List<String> CLASS_INFO_LIST = new ArrayList<>(List.of(
            "VersionNumber",
            "ClassName"
    )) ;
    
    private String projectName ;

    public DataSetWriter(String projectName) throws IOException {
        this.projectName = projectName ;
        
        Files.createDirectories(PathBuilder.buildDataSetDirectoryPath(projectName, true, true));
        Files.createDirectories(PathBuilder.buildDataSetDirectoryPath(projectName, false, true));

        Files.createDirectories(PathBuilder.buildDataSetDirectoryPath(projectName, true, false));
        Files.createDirectories(PathBuilder.buildDataSetDirectoryPath(projectName, false, false));
    }

    public void writeDataSet(List<VersionInfo> versionInfoList, int index, boolean isTraining) throws IOException {
        Path csvOutputPath = PathBuilder.buildDataSetFilePath(projectName, isTraining, true, index) ;
        Path arffOutputPath = PathBuilder.buildDataSetFilePath(projectName, isTraining, false, index) ; 
        
        writeCSV(csvOutputPath, versionInfoList) ;
        writeARFF(arffOutputPath, versionInfoList, index, isTraining) ;
    }

    private void writeARFF(Path arffOutputPath, List<VersionInfo> versionInfoList, int index, boolean isTraining) throws IOException {
        ARFFWriter arffWriter = new ARFFWriter() ;

        String relationName = buildRelationName(index, isTraining) ;
        List<ARFFAttribute> arffAttributeList = buildARFFAttributeList() ;


        List<List<String>> rowDataList = new ArrayList<>() ;
        for (VersionInfo versionInfo : versionInfoList) {
            for (ClassInfo classInfo : versionInfo.getClassInfoList()) {
                rowDataList.add(buildClassInfoAsArray(classInfo));
            }
        }

        arffWriter.writeARFF(arffOutputPath, relationName, arffAttributeList, rowDataList);

    }

    private List<ARFFAttribute> buildARFFAttributeList() {
        List<ARFFAttribute> attributeList = new ArrayList<>() ;
        for (int i = 0 ; i < ATTRIBUTES_LIST.size() ; i++) {
            if (i != ATTRIBUTES_LIST.size() - 1) {
                attributeList.add(new ARFFAttribute(ATTRIBUTES_LIST.get(i), "numeric"));
            }
        }
        attributeList.add(new ARFFAttribute(ATTRIBUTES_LIST.get(ATTRIBUTES_LIST.size() - 1), "{'True', 'False'}"));

        return attributeList ;
    }

    private String buildRelationName(int index, boolean isTraining) {
        StringBuilder relationBuilder = new StringBuilder() ;
        relationBuilder.append(projectName).append("_") ;
        if (isTraining) {
            relationBuilder.append("training_") ;
        }
        else {
            relationBuilder.append("testing_") ;
        }
        relationBuilder.append(index) ;

        return relationBuilder.toString() ;
    }

    private void writeCSV(Path csvOutputPath, List<VersionInfo> versionInfoList) throws IOException {
        CSVWriter csvWriter = new CSVWriter() ;
        List<String> headerArray = new ArrayList<>() ;
        headerArray.addAll(CLASS_INFO_LIST) ;
        headerArray.addAll(ATTRIBUTES_LIST) ;
        
        List<List<String>> lineInfoArray = buildLineInfoArray(versionInfoList) ;
        csvWriter.writeCSV(csvOutputPath, headerArray, lineInfoArray);
    }

    private List<List<String>> buildLineInfoArray(List<VersionInfo> versionInfoList) {
        List<List<String>> lineInfoArray = new ArrayList<>() ;
        for (VersionInfo versionInfo : versionInfoList) {
            List<List<String>> versionRepresentationList = buildVersionRepresentation(versionInfo) ;
            lineInfoArray.addAll(versionRepresentationList) ;
        }
        return lineInfoArray ;
    }

    private List<List<String>> buildVersionRepresentation(VersionInfo versionInfo) {
        List<List<String>> versionRepresentation = new ArrayList<>() ;
        for (ClassInfo classInfo : versionInfo.getClassInfoList()) {
            List<String> rowArray = new ArrayList<>(List.of(Integer.toString(versionInfo.getReleaseNumber()), classInfo.getName())) ;
            rowArray.addAll(buildClassInfoAsArray(classInfo)) ;

            versionRepresentation.add(rowArray);
        }
        return versionRepresentation ;
    }


    private List<String> buildClassInfoAsArray(ClassInfo classInfo) {
        return new ArrayList<>(List.of(
                Float.toString(classInfo.getLoc()),
                Float.toString(classInfo.getAddedLoc()),
                Float.toString(classInfo.getMaxAddedLoc()),
                Float.toString(classInfo.getAvgAddedLoc()),
                /*
                + classInfo.getRemovedLoc()
                ,
                + classInfo.getMaxRemovedLoc()
                ,
                + classInfo.getAvgRemovedLoc()
                ,
                 */
                Float.toString(classInfo.getTouchedLoc()),
                Float.toString(classInfo.getChurn()),
                Float.toString(classInfo.getMaxChurn()),
                Float.toString(classInfo.getAvgChurn()),
                Integer.toString(classInfo.getNumberOfAuthors()),
                Integer.toString(classInfo.getNumberOfRevisions()),
                Integer.toString(classInfo.getNumberDefectsFixed()),
                (classInfo.isBuggy() ? "True" : "False")
        ));
    }

}
