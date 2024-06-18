package net.wanji.business.schedule;

import net.wanji.business.domain.Label;
import net.wanji.business.service.ILabelsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SceneLabelMap {

    private Map<Long,String> sceneMap = new ConcurrentHashMap<>();

    @Autowired
    private ILabelsService labelsService;

    @Async
    public void reset(Long id){
        clear();
        toSelect(id);
    }

    private void toSelect(Long id){
        Label label = new Label();
        label.setParentId(id);
        List<Label> labelList = labelsService.selectLabelsList(label);
        if(labelList.size()==0){
            return;
        }
        String prelabel = sceneMap.getOrDefault(id,null);
        for(Label tlabel : labelList){
            if(prelabel==null){
                sceneMap.put(tlabel.getId(),tlabel.getName());
            }else {
                sceneMap.put(tlabel.getId(),prelabel+"-"+tlabel.getName());
            }
            toSelect(tlabel.getId());
        }
    }

    public void clear(){
        sceneMap.clear();
    }

    public String getSceneLabel(Long id){
        return sceneMap.getOrDefault(id,null);
    }

}
