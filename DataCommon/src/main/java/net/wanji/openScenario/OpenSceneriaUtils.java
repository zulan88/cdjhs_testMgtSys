package net.wanji.openScenario;

import net.wanji.openScenario.properties.OpenScenarioProject;

/**
 * @Auther: guanyuduo
 * @Date: 2023/9/7 15:40
 * @Descriptoin:
 */

public class OpenSceneriaUtils {

    public static void main(String[] args) {
        try {
            String xoscFile = "D:\\OpenSCENARIO\\example\\opp24\\opp24_exam.xosc";
            OpenScenarioProject openScenarioProject = new OpenScenarioProject(xoscFile);

            // 解析Storyboard部分



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
