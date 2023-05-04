package it.uniroma2.isw2.flows;

import it.uniroma2.isw2.builder.WekaClassifierListBuilder;
import it.uniroma2.isw2.model.weka.WekaClassifier;
import it.uniroma2.isw2.model.weka.WekaEvaluation;
import it.uniroma2.isw2.utils.PathBuilder;
import it.uniroma2.isw2.writer.EvaluationWriter;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;


public class WekaFlow {

    private WekaFlow() {}
    public static void weka(String projectName, int maxIndex) throws Exception {

        WekaClassifierListBuilder listBuilder = new WekaClassifierListBuilder() ;
        EvaluationWriter evaluationWriter = new EvaluationWriter(projectName) ;

        List<WekaEvaluation> wekaEvaluationList = new ArrayList<>() ;


        for (int index = 0 ; index < maxIndex ; index++) {
            Logger.getGlobal().log(Level.INFO, "{0}", "Valutazione Versione " + index + "\n");
            DataSource trainSource = new DataSource(PathBuilder.buildTrainingDataSetPath(projectName, index).toString());
            Instances trainingSet = trainSource.getDataSet() ;
            DataSource testSource = new DataSource(PathBuilder.buildTestingDataSetPath(projectName, index).toString());
            Instances testingSet = testSource.getDataSet() ;

            int trueNumber = trainingSet.attributeStats(trainingSet.numAttributes() - 1).nominalCounts[0] ;
            int falseNumber = trainingSet.attributeStats(trainingSet.numAttributes() - 1).nominalCounts[1] ;

            trainingSet.setClassIndex(trainingSet.numAttributes() - 1);
            testingSet.setClassIndex(testingSet.numAttributes() - 1);

            List<WekaClassifier> classifierList = listBuilder.buildClassifierList(trueNumber, falseNumber) ;

            for (WekaClassifier wekaClassifier : classifierList) {
                Classifier classifier = wekaClassifier.getClassifier();
                classifier.buildClassifier(trainingSet);

                Evaluation evaluation = new Evaluation(testingSet) ;
                evaluation.evaluateModel(classifier, testingSet) ;

                wekaClassifier.setEvaluation(evaluation);

                wekaEvaluationList.add(new WekaEvaluation(wekaClassifier, index, evaluation));
            }
        }

        evaluationWriter.writeClassifiersEvaluation(projectName, wekaEvaluationList) ;
    }

}
