package it.uniroma2.isw2;

import it.uniroma2.isw2.flows.ExecutionFlow;

import java.nio.file.Path;


public class Main {

    private enum ProjectEnum {
        BOOKKEEPER,
        //STORM
    }

    private static final Path PROJECT_PATH = Path.of("/home", "simone", "Scrivania", "University", "ISW2", "Projects") ;

    public static void main(String[] args) throws Exception {
        for (ProjectEnum project : ProjectEnum.values()) {
            ExecutionFlow.execute(PROJECT_PATH.toString(), project.name().toLowerCase());
        }

    }
}