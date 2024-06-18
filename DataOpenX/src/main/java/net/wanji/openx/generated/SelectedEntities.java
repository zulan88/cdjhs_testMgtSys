//
// ���ļ����� JavaTM Architecture for XML Binding (JAXB) ����ʵ�� v2.2.8-b130911.1802 ���ɵ�
// ����� <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �����±���Դģʽʱ, �Դ��ļ��������޸Ķ�����ʧ��
// ����ʱ��: 2023.10.24 ʱ�� 10:56:57 AM CST 
//


package net.wanji.openx.generated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>SelectedEntities enity
 * 
 * 
 * 
 * <pre>
 * complexType name="SelectedEntities">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       choice>
 *         element name="EntityRef" type="{}EntityRef" maxOccurs="unbounded" minOccurs="0"/>
 *         element name="ByType" type="{}ByType" maxOccurs="unbounded" minOccurs="0"/>
 *       /choice>
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SelectedEntities", propOrder = {
    "entityRef",
    "byType"
})
public class SelectedEntities {

    @XmlElement(name = "EntityRef")
    protected List<EntityRef> entityRef;
    @XmlElement(name = "ByType")
    protected List<ByType> byType;

    /**
     * Gets the value of the entityRef property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the entityRef property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEntityRef().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EntityRef }
     * 
     * 
     */
    public List<EntityRef> getEntityRef() {
        if (entityRef == null) {
            entityRef = new ArrayList<EntityRef>();
        }
        return this.entityRef;
    }

    /**
     * Gets the value of the byType property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the byType property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getByType().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ByType }
     * 
     * 
     */
    public List<ByType> getByType() {
        if (byType == null) {
            byType = new ArrayList<ByType>();
        }
        return this.byType;
    }

}
