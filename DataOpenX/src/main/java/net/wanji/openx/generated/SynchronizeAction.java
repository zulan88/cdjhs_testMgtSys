//
// ���ļ����� JavaTM Architecture for XML Binding (JAXB) ����ʵ�� v2.2.8-b130911.1802 ���ɵ�
// ����� <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �����±���Դģʽʱ, �Դ��ļ��������޸Ķ�����ʧ��
// ����ʱ��: 2023.10.24 ʱ�� 10:56:57 AM CST 
//


package net.wanji.openx.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>SynchronizeAction enity
 * 
 * 
 * 
 * <pre>
 * complexType name="SynchronizeAction">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       all>
 *         element name="TargetPositionMaster" type="{}Position"/>
 *         element name="TargetPosition" type="{}Position"/>
 *         element name="FinalSpeed" type="{}FinalSpeed" minOccurs="0"/>
 *       /all>
 *       attribute name="masterEntityRef" use="required" type="{}String" />
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SynchronizeAction", propOrder = {

})
public class SynchronizeAction {

    @XmlElement(name = "TargetPositionMaster", required = true)
    protected Position targetPositionMaster;
    @XmlElement(name = "TargetPosition", required = true)
    protected Position targetPosition;
    @XmlElement(name = "FinalSpeed")
    protected FinalSpeed finalSpeed;
    @XmlAttribute(name = "masterEntityRef", required = true)
    protected String masterEntityRef;

    /**
     * ��ȡtargetPositionMaster���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Position }
     *     
     */
    public Position getTargetPositionMaster() {
        return targetPositionMaster;
    }

    /**
     * ����targetPositionMaster���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Position }
     *     
     */
    public void setTargetPositionMaster(Position value) {
        this.targetPositionMaster = value;
    }

    /**
     * ��ȡtargetPosition���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Position }
     *     
     */
    public Position getTargetPosition() {
        return targetPosition;
    }

    /**
     * ����targetPosition���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Position }
     *     
     */
    public void setTargetPosition(Position value) {
        this.targetPosition = value;
    }

    /**
     * ��ȡfinalSpeed���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link FinalSpeed }
     *     
     */
    public FinalSpeed getFinalSpeed() {
        return finalSpeed;
    }

    /**
     * ����finalSpeed���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link FinalSpeed }
     *     
     */
    public void setFinalSpeed(FinalSpeed value) {
        this.finalSpeed = value;
    }

    /**
     * ��ȡmasterEntityRef���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMasterEntityRef() {
        return masterEntityRef;
    }

    /**
     * ����masterEntityRef���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMasterEntityRef(String value) {
        this.masterEntityRef = value;
    }

}
