package demo.core.dsl

import demo.core.chart.ChartDataSet
import demo.core.chart.DataSetManager
import demo.core.dsl.IdeaFlowReader
import demo.core.dsl.IdeaFlowWriter
import demo.core.model.BandType
import demo.core.model.Conflict
import demo.core.model.IdeaFlowModel
import demo.core.timeline.ConflictBand
import demo.core.timeline.GenericBand
import demo.core.timeline.Timeline
import demo.core.timeline.TimelineFactory
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
@Slf4j
class IfmDataGenerator {

    @Autowired
    DataSetManager dataSetManager

    void extrapolateDataSet(File destDir) {

        ChartDataSet dataSet = dataSetManager.defaultDataSet()

        dataSet.ifmFileList.each { ifmFile ->
            log.info("Extrapolating data for: ${ifmFile.name}")

            IdeaFlowModel model = new IdeaFlowReader().readModel(ifmFile)
            Timeline timeline = new TimelineFactory().create(model)
            markupModel(timeline)

            writeModel(destDir, ifmFile, model)
         }
    }

    private markupModel(Timeline timeline) {
        timeline.conflictBands.each { conflictBand ->
            annotateConflictRandomly(conflictBand)
        }
        timeline.genericBands.each { band ->
            if (band.bandType == BandType.learning) {
                annotateLearningRandomly(band)
            } else {
                annotateReworkRandomly(band)
            }
        }

    }

    private String chooseRandomItem(List<String> list) {
        list.get(new Random().nextInt(list.size()));
    }

    private String annotateConflictRandomly(ConflictBand conflict) {
        List<String> hashtags = ['#ExperimentPain', '#ExperimentPain','#ExperimentPain','#CollaborationPain', '#AlarmPain', '']
        List<String> secondaryHashtags = ['', '', '', '', '#LackOfFamiliarityPain', '#DisruptionPain']

        if (conflict.duration.duration > 15*60) {
            conflict.conflict.question  += " "+ chooseRandomItem(hashtags)
        }
        if (conflict.duration.duration > 30*60) {
            conflict.conflict.question += " "+ chooseRandomItem(secondaryHashtags)
        }
    }

    private String annotateLearningRandomly(GenericBand learningBand) {
        List<String> hashtags = ['#ModelingPain', '#ModelingPain','#ModelingPain','#DesignFitPain', '']
        List<String> secondaryHashtags = ['', '', '', '', '#LackOfFamiliarityPain', '#DisruptionPain']

        if (learningBand.duration.duration > 15*60) {
            learningBand.bandStart.comment  += " "+ chooseRandomItem(hashtags)
        }
        if (learningBand.duration.duration > 30*60) {
            learningBand.bandStart.comment += " "+ chooseRandomItem(secondaryHashtags)
        }
    }

    private String annotateReworkRandomly(GenericBand reworkBand) {
        List<String> hashtags = ['#RequirementsPain', '#DesignFitPain', '', '', '']

        if (reworkBand.duration.duration > 15*60) {
            reworkBand.bandStart.comment  += " "+ chooseRandomItem(hashtags)
        }
    }

    private void writeModel(File destDir, File ifmFile, IdeaFlowModel model) {
        String parentFolderName = determineAuthor(ifmFile)

        File parentDir = new File(destDir, parentFolderName)
        parentDir.mkdirs()

        File newIfmFile = new File(parentDir, ifmFile.name)
        newIfmFile.createNewFile()
        new IdeaFlowWriter(newIfmFile.newWriter()).writeModel(model)
    }

    private String determineAuthor(File ifmFile) {
        String filePath = ifmFile.absolutePath
        String author = "unknown"

        if (filePath.contains("mike")) {
            author = "drgonzo"
        } else if (filePath.contains("james")) {
            author = "jenny"
        } else if (filePath.contains("john")) {
            author = "george"
        } else if (filePath.contains("ghaney")) {
            author = "alex"
        } else if (filePath.contains("janelle")) {
            author = "wendy"
        }
        return author
    }

}
