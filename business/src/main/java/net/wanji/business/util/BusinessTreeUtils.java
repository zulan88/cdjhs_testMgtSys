package net.wanji.business.util;

import net.wanji.business.domain.BusinessTreeSelect;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: guanyuduo
 * @Date: 2023/7/20 13:34
 * @Descriptoin:
 */

public class BusinessTreeUtils {

    public static BusinessTreeSelect fuzzySearch(BusinessTreeSelect node, String query) {
        if (StringUtils.isEmpty(query) || node.getName().contains(query)) {
            // 如果这个节点匹配查询，返回这个节点和它的所有子节点...
            return node;
        } else {
            // 如果这个节点不匹配查询，对它的每个子节点执行模糊查询...
            List<BusinessTreeSelect> matchingChildren = new ArrayList<>();
            for (BusinessTreeSelect child : node.getChildren()) {
                BusinessTreeSelect matchingChild = fuzzySearch(child, query);
                if (matchingChild != null) {
                    matchingChildren.add(matchingChild);
                }
            }
            if (!matchingChildren.isEmpty()) {
                // 如果有任何匹配的子节点，创建一个新的节点，包含这些子节点...
                BusinessTreeSelect newNode = new BusinessTreeSelect();
                newNode.setId(node.getId());
                newNode.setParentId(node.getParentId());
                newNode.setName(node.getName());
                newNode.setChildren(matchingChildren);
                return newNode;
            } else {
                // 如果没有任何匹配的子节点，返回null...
                return null;
            }
        }
    }

}
