//
// ���ļ����� JavaTM Architecture for XML Binding (JAXB) ����ʵ�� v2.2.8-b130911.1802 ���ɵ�
// ����� <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �����±���Դģʽʱ, �Դ��ļ��������޸Ķ�����ʧ��
// ����ʱ��: 2023.10.24 ʱ�� 10:56:57 AM CST 
//


package net.wanji.openx.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>ByEntityCondition enity
 * 
 * 
 * 
 * <pre>
 * complexType name="ByEntityCondition">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       all>
 *         element name="TriggeringEntities" type="{}TriggeringEntities"/>
 *         element name="EntityCondition" type="{}EntityCondition"/>
 *       /all>
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ByEntityCondition", propOrder = {

})
public class ByEntityCondition {

    @XmlElement(name = "TriggeringEntities", required = true)
    protected TriggeringEntities triggeringEntities;
    @XmlElement(name = "EntityCondition", required = true)
    protected EntityCondition entityCondition;

    /**
     * ��ȡtriggeringEntities���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link TriggeringEntities }
     *     
     */
    public TriggeringEntities getTriggeringEntities() {
        return triggeringEntities;
    }

    /**
     * ����triggeringEntities���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link TriggeringEntities }
     *     
     */
    public void setTriggeringEntities(TriggeringEntities value) {
        this.triggeringEntities = value;
    }

    /**
     * ��ȡentityCondition���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link EntityCondition }
     *     
     */
    public EntityCondition getEntityCondition() {
        return entityCondition;
    }

    /**
     * ����entityCondition���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link EntityCondition }
     *     
     */
    public void setEntityCondition(EntityCondition value) {
        this.entityCondition = value;
    }

}
