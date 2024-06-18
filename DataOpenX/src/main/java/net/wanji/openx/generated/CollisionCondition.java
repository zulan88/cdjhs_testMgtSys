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
 * <p>CollisionCondition enity
 * 
 * 
 * 
 * <pre>
 * complexType name="CollisionCondition">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       choice>
 *         element name="EntityRef" type="{}EntityRef" minOccurs="0"/>
 *         element name="ByType" type="{}ByObjectType" minOccurs="0"/>
 *       /choice>
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CollisionCondition", propOrder = {
    "entityRef",
    "byType"
})
public class CollisionCondition {

    @XmlElement(name = "EntityRef")
    protected EntityRef entityRef;
    @XmlElement(name = "ByType")
    protected ByObjectType byType;

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

    /**
     * ��ȡbyType���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link ByObjectType }
     *     
     */
    public ByObjectType getByType() {
        return byType;
    }

    /**
     * ����byType���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link ByObjectType }
     *     
     */
    public void setByType(ByObjectType value) {
        this.byType = value;
    }

}
