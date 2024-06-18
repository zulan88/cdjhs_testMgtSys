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
 * <p>RelativeTargetSpeed enity
 * 
 * 
 * 
 * <pre>
 * complexType name="RelativeTargetSpeed">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       attribute name="entityRef" use="required" type="{}String" />
 *       attribute name="continuous" use="required" type="{}Boolean" />
 *       attribute name="speedTargetValueType" use="required" type="{}SpeedTargetValueType" />
 *       attribute name="value" use="required" type="{}Double" />
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RelativeTargetSpeed")
public class RelativeTargetSpeed {

    @XmlAttribute(name = "entityRef", required = true)
    protected String entityRef;
    @XmlAttribute(name = "continuous", required = true)
    protected String continuous;
    @XmlAttribute(name = "speedTargetValueType", required = true)
    protected String speedTargetValueType;
    @XmlAttribute(name = "value", required = true)
    protected String value;

    /**
     * ��ȡentityRef���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntityRef() {
        return entityRef;
    }

    /**
     * ����entityRef���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntityRef(String value) {
        this.entityRef = value;
    }

    /**
     * ��ȡcontinuous���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContinuous() {
        return continuous;
    }

    /**
     * ����continuous���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContinuous(String value) {
        this.continuous = value;
    }

    /**
     * ��ȡspeedTargetValueType���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpeedTargetValueType() {
        return speedTargetValueType;
    }

    /**
     * ����speedTargetValueType���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpeedTargetValueType(String value) {
        this.speedTargetValueType = value;
    }

    /**
     * ��ȡvalue���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue() {
        return value;
    }

    /**
     * ����value���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue(String value) {
        this.value = value;
    }

}
