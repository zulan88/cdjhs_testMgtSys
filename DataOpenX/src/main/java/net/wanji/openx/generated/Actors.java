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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Actors enity
 * 
 * 
 * 
 * <pre>
 * complexType name="Actors">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       sequence>
 *         element name="EntityRef" type="{}EntityRef" maxOccurs="unbounded" minOccurs="0"/>
 *       /sequence>
 *       attribute name="selectTriggeringEntities" use="required" type="{}Boolean" />
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Actors", propOrder = {
    "entityRef"
})
public class Actors {

    @XmlElement(name = "EntityRef")
    protected List<EntityRef> entityRef;
    @XmlAttribute(name = "selectTriggeringEntities", required = true)
    protected String selectTriggeringEntities;

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
     * ��ȡselectTriggeringEntities���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSelectTriggeringEntities() {
        return selectTriggeringEntities;
    }

    /**
     * ����selectTriggeringEntities���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSelectTriggeringEntities(String value) {
        this.selectTriggeringEntities = value;
    }

}
