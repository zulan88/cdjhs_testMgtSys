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
import javax.xml.bind.annotation.XmlType;


/**
 * <p>LaneOffsetActionDynamics enity
 * 
 *
 * 
 * <pre>
 * complexType name="LaneOffsetActionDynamics">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       attribute name="dynamicsShape" use="required" type="{}DynamicsShape" />
 *       attribute name="maxLateralAcc" type="{}Double" />
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LaneOffsetActionDynamics")
public class LaneOffsetActionDynamics {

    @XmlAttribute(name = "dynamicsShape", required = true)
    protected String dynamicsShape;
    @XmlAttribute(name = "maxLateralAcc")
    protected String maxLateralAcc;

    /**
     * ��ȡdynamicsShape���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDynamicsShape() {
        return dynamicsShape;
    }

    /**
     * ����dynamicsShape���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDynamicsShape(String value) {
        this.dynamicsShape = value;
    }

    /**
     * ��ȡmaxLateralAcc���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaxLateralAcc() {
        return maxLateralAcc;
    }

    /**
     * ����maxLateralAcc���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaxLateralAcc(String value) {
        this.maxLateralAcc = value;
    }

}
