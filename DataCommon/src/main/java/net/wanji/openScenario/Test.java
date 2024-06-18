package net.wanji.openScenario;

import net.wanji.openScenario.field.OpenScenario;
import net.wanji.openScenario.field.ScenarioObject;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;

/**
 * @Auther: guanyuduo
 * @Date: 2023/7/21 11:21
 * @Descriptoin:
 */

public class Test {
    public static void main(String[] args) {
        try {
            File file = new File("D:\\OpenSCENARIO\\standard_download64b5f92a288a9_30617\\openscenario-v1.2.0\\ADemo\\DoubleLaneChanger.xosc");
            JAXBContext jaxbContext = JAXBContext.newInstance(OpenScenario.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            OpenScenario openScenario = (OpenScenario) unmarshaller.unmarshal(file);

            // Print the ScenarioObjects
            for (ScenarioObject scenarioObject : openScenario.getScenarioObjects()) {
                System.out.println("Name: " + scenarioObject.getName());
                System.out.println("CatalogName: " + scenarioObject.getCatalogReference().getCatalogName());
                System.out.println("EntryName: " + scenarioObject.getCatalogReference().getEntryName());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
