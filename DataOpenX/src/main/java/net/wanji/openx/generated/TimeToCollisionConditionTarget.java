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
 * <p>TimeToCollisionConditionTarget enity
 * 
 * 
 * 
 * <pre>
 * complexType name="TimeToCollisionConditionTarget">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       choice>
 *         element name="Position" type="{}Position" minOccurs="0"/>
 *         element name="EntityRef" type="{}EntityRef" minOccurs="0"/>
 *       /choice>
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TimeToCollisionConditionTarget", propOrder = {
    "position",
    "entityRef"
})
public class TimeToCollisionConditionTarget {

    @XmlElement(name = "Position")
    protected Position position;
    @XmlElement(name = "EntityRef")
    protected EntityRef entityRef;

    /**
     * ��ȡposition���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Position }
     *     
     */
    public Position getPosition() {
        return position;
    }

    /**
     * ����position���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Position }
     *     
     */
    public void setPosition(Position value) {
        this.position = value;
    }

    /**
     * ��ȡentityRef���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link EntityRef }
     *     
     */
    public EntityRef getEntityRef() {
        return entityRef;
    }

    /**
     * ����entityRef���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link EntityRef }
     *     
     */
    public void setEntityRef(EntityRef value) {
        this.entityRef = value;
    }

}
